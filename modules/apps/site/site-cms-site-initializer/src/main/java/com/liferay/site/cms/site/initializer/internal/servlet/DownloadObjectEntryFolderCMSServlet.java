/**
 * SPDX-FileCopyrightText: (c) 2025 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.site.cms.site.initializer.internal.servlet;

import com.liferay.document.library.kernel.service.DLAppLocalService;
import com.liferay.object.constants.ObjectFieldConstants;
import com.liferay.object.model.ObjectEntry;
import com.liferay.object.model.ObjectEntryFolder;
import com.liferay.object.model.ObjectField;
import com.liferay.object.service.ObjectEntryFolderLocalService;
import com.liferay.object.service.ObjectEntryFolderService;
import com.liferay.object.service.ObjectEntryLocalService;
import com.liferay.object.service.ObjectFieldLocalService;
import com.liferay.petra.io.StreamUtil;
import com.liferay.petra.string.StringBundler;
import com.liferay.petra.string.StringPool;
import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.interval.IntervalActionProcessor;
import com.liferay.portal.kernel.json.JSONArray;
import com.liferay.portal.kernel.json.JSONException;
import com.liferay.portal.kernel.json.JSONFactory;
import com.liferay.portal.kernel.json.JSONObject;
import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.portal.kernel.repository.model.FileEntry;
import com.liferay.portal.kernel.security.auth.PrincipalException;
import com.liferay.portal.kernel.security.permission.ActionKeys;
import com.liferay.portal.kernel.security.permission.PermissionChecker;
import com.liferay.portal.kernel.security.permission.resource.ModelResourcePermission;
import com.liferay.portal.kernel.security.permission.resource.ModelResourcePermissionRegistryUtil;
import com.liferay.portal.kernel.servlet.ContentDispositionUtil;
import com.liferay.portal.kernel.servlet.HttpHeaders;
import com.liferay.portal.kernel.theme.ThemeDisplay;
import com.liferay.portal.kernel.util.ContentTypes;
import com.liferay.portal.kernel.util.FileUtil;
import com.liferay.portal.kernel.util.GetterUtil;
import com.liferay.portal.kernel.util.StringUtil;
import com.liferay.portal.kernel.util.Time;
import com.liferay.portal.kernel.util.Validator;
import com.liferay.portal.kernel.util.WebKeys;
import com.liferay.portal.search.document.Document;
import com.liferay.portal.search.hits.SearchHit;
import com.liferay.portal.search.hits.SearchHits;

import jakarta.servlet.Servlet;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;

import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

/**
 * @author Roberto Díaz
 */
@Component(
	property = {
		"osgi.http.whiteboard.servlet.name=com.liferay.site.cms.site.initializer.internal.servlet.DownloadObjectEntryFolderCMSServlet",
		"osgi.http.whiteboard.servlet.pattern=/cms/download-folder/*",
		"servlet.init.httpMethods=GET,POST"
	},
	service = Servlet.class
)
public class DownloadObjectEntryFolderCMSServlet extends BaseCMSServlet {

	@Override
	protected void doGet(
			HttpServletRequest httpServletRequest,
			HttpServletResponse httpServletResponse)
		throws IOException, ServletException {

		try {
			_downloadObjectEntryFolder(httpServletRequest, httpServletResponse);
		}
		catch (PortalException portalException) {
			throw new ServletException(portalException);
		}
	}

	@Override
	protected void doPost(
			HttpServletRequest httpServletRequest,
			HttpServletResponse httpServletResponse)
		throws IOException, ServletException {

		try {
			_downloadBulkAction(httpServletRequest, httpServletResponse);
		}
		catch (PortalException portalException) {
			throw new ServletException(portalException);
		}
	}

	private void _downloadBulkAction(
			HttpServletRequest httpServletRequest,
			HttpServletResponse httpServletResponse)
		throws IOException, PortalException {

		JSONObject jsonObject = null;

		try {
			jsonObject = _jsonFactory.createJSONObject(
				StreamUtil.toString(
					httpServletRequest.getInputStream(), StringPool.UTF8));
		}
		catch (JSONException jsonException) {
			httpServletResponse.setStatus(HttpServletResponse.SC_BAD_REQUEST);

			if (_log.isWarnEnabled()) {
				_log.warn(jsonException);
			}

			return;
		}

		if (!StringUtil.equalsIgnoreCase(
				"DownloadBulkAction", jsonObject.getString("type"))) {

			httpServletResponse.setStatus(HttpServletResponse.SC_BAD_REQUEST);

			if (_log.isWarnEnabled()) {
				_log.warn("Type is not \"DownloadBulkAction\"");
			}

			return;
		}

		ThemeDisplay themeDisplay =
			(ThemeDisplay)httpServletRequest.getAttribute(
				WebKeys.THEME_DISPLAY);

		_setHeaders(
			httpServletResponse, "download-" + Time.getTimestamp() + ".zip");

		ZipOutputStream zipOutputStream = new ZipOutputStream(
			httpServletResponse.getOutputStream());

		Set<String> fileNames = new HashSet<>();

		JSONObject selectionScopeJSONObject = jsonObject.getJSONObject(
			"selectionScope");

		if ((selectionScopeJSONObject != null) &&
			selectionScopeJSONObject.getBoolean("selectAll")) {

			_selectAllDownload(
				fileNames, httpServletRequest, themeDisplay, zipOutputStream);
		}
		else {
			JSONArray jsonArray = jsonObject.getJSONArray("bulkActionItems");

			for (int i = 0; i < jsonArray.length(); i++) {
				JSONObject bulkActionItemJSONObject = jsonArray.getJSONObject(
					i);

				_zipEntry(
					bulkActionItemJSONObject.getString("className"),
					bulkActionItemJSONObject.getLong("classPK"), fileNames,
					themeDisplay, zipOutputStream);
			}
		}

		zipOutputStream.finish();
	}

	private void _downloadObjectEntryFolder(
			HttpServletRequest httpServletRequest,
			HttpServletResponse httpServletResponse)
		throws IOException, PortalException {

		String[] pathArray = StringUtil.split(
			_getObjectEntryFolderPath(httpServletRequest), StringPool.SLASH);

		long classNameId = portal.getClassNameId(ObjectEntryFolder.class);

		if (classNameId != GetterUtil.getLong(pathArray[0])) {
			throw new RuntimeException();
		}

		long objectEntryFolderId = GetterUtil.getLong(pathArray[1]);

		ObjectEntryFolder objectEntryFolder =
			_objectEntryFolderService.getObjectEntryFolder(objectEntryFolderId);

		ThemeDisplay themeDisplay =
			(ThemeDisplay)httpServletRequest.getAttribute(
				WebKeys.THEME_DISPLAY);

		_setHeaders(httpServletResponse, objectEntryFolder.getName() + ".zip");

		ZipOutputStream zipOutputStream = new ZipOutputStream(
			httpServletResponse.getOutputStream());

		_zipObjectEntryFolder(
			objectEntryFolder.getGroupId(), objectEntryFolderId,
			objectEntryFolder.getName(), themeDisplay, zipOutputStream);

		zipOutputStream.finish();
	}

	private String _getObjectEntryFolderPath(
		HttpServletRequest httpServletRequest) {

		String requestURI = httpServletRequest.getRequestURI();

		String path =
			httpServletRequest.getContextPath() +
				httpServletRequest.getServletPath();

		return requestURI.substring(path.length() + 1);
	}

	private String _getPath(String path, String name) {
		if (Validator.isNull(path)) {
			return name;
		}

		return StringBundler.concat(path, StringPool.SLASH, name);
	}

	private String _getUniqueFileName(Set<String> fileNames, String fileName) {
		if (fileNames.add(fileName)) {
			return fileName;
		}

		int i = 1;

		while (true) {
			String uniqueFileName = FileUtil.appendParentheticalSuffix(
				fileName, String.valueOf(i++));

			if (fileNames.add(uniqueFileName)) {
				return uniqueFileName;
			}
		}
	}

	private void _selectAllDownload(
			Set<String> fileNames, HttpServletRequest httpServletRequest,
			ThemeDisplay themeDisplay, ZipOutputStream zipOutputStream)
		throws IOException, PortalException {

		SearchHits searchHits = getSelectAllSearchHits(httpServletRequest);

		for (SearchHit searchHit : searchHits.getSearchHits()) {
			Document document = searchHit.getDocument();

			_zipEntry(
				document.getString("entryClassName"),
				document.getLong("entryClassPK"), fileNames, themeDisplay,
				zipOutputStream);
		}
	}

	private void _setHeaders(
		HttpServletResponse httpServletResponse, String zipFileName) {

		httpServletResponse.setContentType(ContentTypes.APPLICATION_ZIP);
		httpServletResponse.setHeader(
			HttpHeaders.CONTENT_DISPOSITION,
			ContentDispositionUtil.getContentDispositionHeaderValue(
				zipFileName));
	}

	private void _zipEntry(
			String className, long classPK, Set<String> fileNames,
			ThemeDisplay themeDisplay, ZipOutputStream zipOutputStream)
		throws IOException, PortalException {

		if (StringUtil.equalsIgnoreCase(
				className, ObjectEntryFolder.class.getName())) {

			ObjectEntryFolder objectEntryFolder = null;

			try {
				objectEntryFolder =
					_objectEntryFolderService.getObjectEntryFolder(classPK);
			}
			catch (PrincipalException principalException) {
				if (_log.isDebugEnabled()) {
					_log.debug(principalException);
				}

				return;
			}

			_zipObjectEntryFolder(
				objectEntryFolder.getGroupId(), classPK,
				objectEntryFolder.getName(), themeDisplay, zipOutputStream);
		}
		else {
			_zipObjectEntry(
				fileNames, _objectEntryLocalService.getObjectEntry(classPK),
				StringPool.BLANK, themeDisplay.getPermissionChecker(),
				zipOutputStream);
		}
	}

	private void _zipObjectEntry(
			Set<String> fileNames, ObjectEntry objectEntry, String path,
			PermissionChecker permissionChecker,
			ZipOutputStream zipOutputStream)
		throws IOException, PortalException {

		ObjectField attachmentObjectField = null;
		long fileEntryId = 0;
		Map<String, Serializable> values = objectEntry.getValues();

		List<ObjectField> attachmentObjectFields =
			_objectFieldLocalService.getObjectFieldsByBusinessType(
				objectEntry.getObjectDefinitionId(),
				ObjectFieldConstants.BUSINESS_TYPE_ATTACHMENT);

		for (ObjectField objectField : attachmentObjectFields) {
			long candidateFileEntryId = GetterUtil.getLong(
				values.get(objectField.getName()));

			if (candidateFileEntryId != 0) {
				attachmentObjectField = objectField;
				fileEntryId = candidateFileEntryId;

				break;
			}
		}

		if (fileEntryId == 0) {
			return;
		}

		ModelResourcePermission<ObjectEntry>
			objectEntryModelResourcePermission =
				ModelResourcePermissionRegistryUtil.getModelResourcePermission(
					objectEntry.getModelClassName());

		if (objectEntryModelResourcePermission == null) {
			if (_log.isWarnEnabled()) {
				_log.warn(
					"No model resource permission registered for " +
						objectEntry.getModelClassName());
			}

			return;
		}

		if (!objectEntryModelResourcePermission.contains(
				permissionChecker, objectEntry, ActionKeys.VIEW) ||
			!objectEntryModelResourcePermission.contains(
				permissionChecker, objectEntry,
				attachmentObjectField.getAttachmentDownloadActionKey())) {

			return;
		}

		FileEntry fileEntry = _dlAppLocalService.getFileEntry(fileEntryId);

		if (!fileEntry.containsPermission(
				permissionChecker, ActionKeys.DOWNLOAD)) {

			return;
		}

		try (InputStream inputStream = fileEntry.getContentStream()) {
			if (inputStream == null) {
				return;
			}

			String fileName = _getUniqueFileName(
				fileNames, fileEntry.getFileName());

			zipOutputStream.putNextEntry(
				new ZipEntry(_getPath(path, fileName)));

			StreamUtil.transfer(inputStream, zipOutputStream, false);

			zipOutputStream.closeEntry();
		}
	}

	private void _zipObjectEntryFolder(
			long groupId, long objectEntryFolderId, String path,
			ThemeDisplay themeDisplay, ZipOutputStream zipOutputStream)
		throws PortalException {

		Set<String> fileNames = new HashSet<>();

		IntervalActionProcessor<Void> objectEntriesIntervalActionProcessor =
			new IntervalActionProcessor<>(
				_objectEntryLocalService.getObjectEntryFolderObjectEntriesCount(
					groupId, objectEntryFolderId),
				_BATCH_SIZE);

		objectEntriesIntervalActionProcessor.setPerformIntervalActionMethod(
			(start, end) -> {
				List<ObjectEntry> objectEntryFolderObjectEntries =
					_objectEntryLocalService.getObjectEntryFolderObjectEntries(
						groupId, objectEntryFolderId, start, end);

				try {
					for (ObjectEntry objectEntry :
							objectEntryFolderObjectEntries) {

						_zipObjectEntry(
							fileNames, objectEntry, path,
							themeDisplay.getPermissionChecker(),
							zipOutputStream);
					}
				}
				catch (IOException ioException) {
					throw new PortalException(ioException);
				}

				objectEntriesIntervalActionProcessor.incrementStart(
					objectEntryFolderObjectEntries.size());

				return null;
			});

		objectEntriesIntervalActionProcessor.performIntervalActions();

		IntervalActionProcessor<Void>
			objectEntryFoldersIntervalActionProcessor =
				new IntervalActionProcessor<>(
					_objectEntryFolderLocalService.getObjectEntryFoldersCount(
						groupId, themeDisplay.getCompanyId(),
						objectEntryFolderId),
					_BATCH_SIZE);

		objectEntryFoldersIntervalActionProcessor.
			setPerformIntervalActionMethod(
				(start, end) -> {
					List<ObjectEntryFolder> objectEntryFolders =
						_objectEntryFolderLocalService.getObjectEntryFolders(
							groupId, themeDisplay.getCompanyId(),
							objectEntryFolderId, start, end);

					for (ObjectEntryFolder objectEntryFolder :
							objectEntryFolders) {

						if (!_objectEntryFolderModelResourcePermission.contains(
								themeDisplay.getPermissionChecker(),
								objectEntryFolder, ActionKeys.VIEW)) {

							continue;
						}

						_zipObjectEntryFolder(
							groupId, objectEntryFolder.getObjectEntryFolderId(),
							_getPath(path, objectEntryFolder.getName()),
							themeDisplay, zipOutputStream);
					}

					objectEntryFoldersIntervalActionProcessor.incrementStart(
						objectEntryFolders.size());

					return null;
				});

		objectEntryFoldersIntervalActionProcessor.performIntervalActions();
	}

	private static final int _BATCH_SIZE = 1000;

	private static final Log _log = LogFactoryUtil.getLog(
		DownloadObjectEntryFolderCMSServlet.class);

	@Reference
	private DLAppLocalService _dlAppLocalService;

	@Reference
	private JSONFactory _jsonFactory;

	@Reference
	private ObjectEntryFolderLocalService _objectEntryFolderLocalService;

	@Reference(
		target = "(model.class.name=com.liferay.object.model.ObjectEntryFolder)"
	)
	private ModelResourcePermission<ObjectEntryFolder>
		_objectEntryFolderModelResourcePermission;

	@Reference
	private ObjectEntryFolderService _objectEntryFolderService;

	@Reference
	private ObjectEntryLocalService _objectEntryLocalService;

	@Reference
	private ObjectFieldLocalService _objectFieldLocalService;

}