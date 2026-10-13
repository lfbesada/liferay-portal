/**
 * SPDX-FileCopyrightText: (c) 2026 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.launch.internal.entry.type;

import com.liferay.item.selector.ItemSelectorCriterion;
import com.liferay.launch.entry.type.LaunchEntryType;
import com.liferay.layout.content.creator.LayoutContentVersionCreator;
import com.liferay.layout.item.selector.LayoutItemSelectorCriterion;
import com.liferay.layout.item.selector.LayoutItemSelectorReturnType;
import com.liferay.petra.string.StringBundler;
import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.language.Language;
import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.portal.kernel.model.BaseModel;
import com.liferay.portal.kernel.model.Layout;
import com.liferay.portal.kernel.model.LayoutConstants;
import com.liferay.portal.kernel.module.service.Snapshot;
import com.liferay.portal.kernel.portlet.RequestBackedPortletURLFactory;
import com.liferay.portal.kernel.service.LayoutLocalService;
import com.liferay.portal.kernel.service.ServiceContext;
import com.liferay.portal.kernel.service.ServiceContextThreadLocal;
import com.liferay.portal.kernel.theme.ThemeDisplay;
import com.liferay.portal.kernel.util.Constants;
import com.liferay.portal.kernel.util.GetterUtil;
import com.liferay.portal.kernel.util.HttpComponentsUtil;
import com.liferay.portal.kernel.util.Portal;
import com.liferay.portal.kernel.workflow.WorkflowConstants;

import java.io.Serializable;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

/**
 * @author Alejandro Tardín
 * @author Lourdes Fernández Besada
 */
@Component(
	property = "launch.entry.type.class.name=com.liferay.portal.kernel.model.Layout",
	service = LaunchEntryType.class
)
public class LayoutLaunchEntryType implements LaunchEntryType {

	@Override
	public Version addPublishedVersion(long classPK, String classVersion)
		throws PortalException {

		Layout layout = _layoutLocalService.getLayout(classPK);

		if (!layout.isTypeContent() || layout.isDraftLayout()) {
			throw new PortalException(
				"The layout must be the public layout of a content page");
		}

		Layout launchDraftLayout = _fetchLayout(classPK, classVersion);

		if ((launchDraftLayout == null) ||
			(launchDraftLayout.getPlid() == classPK)) {

			return _toVersion(layout);
		}

		Layout draftLayout = layout.fetchDraftLayout();

		if (draftLayout.isDraft()) {
			_createLayoutContentVersion(draftLayout);
		}

		_copyLayoutContent(launchDraftLayout, layout);

		if (draftLayout.getPlid() != launchDraftLayout.getPlid()) {
			_copyLayoutContent(launchDraftLayout, draftLayout);
		}

		if (launchDraftLayout.getLaunchSetId() > 0) {
			_layoutLocalService.deleteLayout(launchDraftLayout);
		}

		return fetchPublishedVersion(classPK);
	}

	@Override
	public Version addVersion(
			long classPK, String classVersion, long launchSetId)
		throws PortalException {

		Layout layout = _layoutLocalService.getLayout(classPK);

		Layout baseLayout = _fetchLayout(classPK, classVersion);

		if (baseLayout == null) {
			baseLayout = layout;
		}

		return _toVersion(
			_copyLayoutContent(
				baseLayout,
				_addLaunchDraftLayout(layout, baseLayout, launchSetId)));
	}

	@Override
	public Version fetchPublishedVersion(long classPK) throws PortalException {
		return _toVersion(_layoutLocalService.getLayout(classPK));
	}

	@Override
	public Version fetchVersion(long classPK, String classVersion)
		throws PortalException {

		Layout layout = _fetchLayout(classPK, classVersion);

		if (layout == null) {
			return null;
		}

		return _toVersion(layout);
	}

	@Override
	public long getClassPK(Map<String, String> selectedItemData) {
		Layout layout = _layoutLocalService.fetchLayout(
			GetterUtil.getLong(selectedItemData.get("plid")));

		if ((layout == null) || !layout.isTypeContent() ||
			layout.isDraftLayout()) {

			return 0;
		}

		return layout.getPlid();
	}

	@Override
	public ItemSelectorCriterion getItemSelectorCriterion() {
		LayoutItemSelectorCriterion layoutItemSelectorCriterion =
			new LayoutItemSelectorCriterion();

		layoutItemSelectorCriterion.setDesiredItemSelectorReturnTypes(
			new LayoutItemSelectorReturnType());

		return layoutItemSelectorCriterion;
	}

	@Override
	public String getLabel(Locale locale) {
		return _language.get(locale, "page");
	}

	@Override
	public Class<?> getModelClass() {
		return Layout.class;
	}

	@Override
	public List<Version> getVersions(long classPK, Locale locale)
		throws PortalException {

		List<Version> versions = new ArrayList<>();

		Layout layout = _layoutLocalService.getLayout(classPK);

		versions.add(_toVersion(layout));

		Layout draftLayout = layout.fetchDraftLayout();

		if (draftLayout != null) {
			versions.add(_toVersion(draftLayout));
		}

		return versions;
	}

	@Override
	public Version publishVersion(long classPK, String classVersion)
		throws PortalException {

		return addPublishedVersion(classPK, classVersion);
	}

	private Layout _addLaunchDraftLayout(
			Layout layout, Layout baseLayout, long launchSetId)
		throws PortalException {

		ServiceContext serviceContext =
			ServiceContextThreadLocal.getServiceContext();

		String externalReferenceCode = StringBundler.concat(
			layout.getExternalReferenceCode(),
			LayoutConstants.EXTERNAL_REFERENCE_CODE_INFIX_LAUNCH_DRAFT,
			launchSetId);

		Layout launchDraftLayout = _layoutLocalService.addLayout(
			externalReferenceCode, serviceContext.getUserId(),
			layout.getGroupId(), layout.isPrivateLayout(),
			layout.getParentLayoutId(), _portal.getClassNameId(Layout.class),
			layout.getPlid(), baseLayout.getNameMap(), baseLayout.getTitleMap(),
			baseLayout.getDescriptionMap(), baseLayout.getKeywordsMap(),
			baseLayout.getRobotsMap(), layout.getType(),
			baseLayout.getTypeSettings(), true, true, Collections.emptyMap(),
			baseLayout.getMasterLayoutPageTemplateEntryERC(), serviceContext);

		launchDraftLayout.setLaunchSetId(launchSetId);

		return _layoutLocalService.updateLayout(launchDraftLayout);
	}

	private Layout _copyLayoutContent(Layout sourceLayout, Layout targetLayout)
		throws PortalException {

		try {
			_layoutLocalService.copyLayoutContent(sourceLayout, targetLayout);
		}
		catch (Exception exception) {
			throw new PortalException(exception);
		}

		return _layoutLocalService.getLayout(targetLayout.getPlid());
	}

	private void _createLayoutContentVersion(Layout sourceLayout) {
		LayoutContentVersionCreator layoutContentVersionCreator =
			_layoutContentVersionCreatorSnapshot.get();

		if (layoutContentVersionCreator == null) {
			if (_log.isDebugEnabled()) {
				_log.debug("Layout content version creator is null");
			}

			return;
		}

		layoutContentVersionCreator.createLayoutContentVersion(
			sourceLayout, WorkflowConstants.STATUS_DRAFT);
	}

	private Layout _fetchLayout(long classPK, String classVersion) {
		Layout layout = _layoutLocalService.fetchLayout(
			GetterUtil.getLong(classVersion));

		if (layout == null) {
			return null;
		}

		if ((layout.getPlid() == classPK) ||
			(layout.isDraftLayout() && (layout.getClassPK() == classPK))) {

			return layout;
		}

		return null;
	}

	private String _getEditURL(Layout layout, String redirect)
		throws PortalException {

		ServiceContext serviceContext =
			ServiceContextThreadLocal.getServiceContext();

		ThemeDisplay themeDisplay = serviceContext.getThemeDisplay();

		return HttpComponentsUtil.addParameters(
			_portal.getLayoutFullURL(layout, themeDisplay), "p_l_back_url",
			redirect, "p_l_back_url_title",
			_language.get(themeDisplay.getLocale(), "launches"), "p_l_mode",
			Constants.EDIT);
	}

	private String _getLabel(Layout layout, Locale locale) {
		if (!layout.isDraftLayout()) {
			return _language.get(locale, "published");
		}

		if (layout.getLaunchSetId() == 0) {
			return _language.get(locale, "current-draft");
		}

		return _language.get(locale, "draft");
	}

	private Version _toVersion(Layout layout) {
		return new Version() {

			@Override
			public String getClassVersion() {
				return String.valueOf(layout.getPlid());
			}

			@Override
			public String getEditURL(
					String redirect,
					RequestBackedPortletURLFactory
						requestBackedPortletURLFactory)
				throws PortalException {

				return _getEditURL(layout, redirect);
			}

			@Override
			public long getGroupId() {
				return layout.getGroupId();
			}

			@Override
			public String getLabel(Locale locale) {
				return _getLabel(layout, locale);
			}

			@Override
			public Date getModifiedDate() {
				return layout.getModifiedDate();
			}

			@Override
			public BaseModel<?> getPreviewBaseModel() {
				return layout;
			}

			@Override
			public Serializable getPrimaryKey() {
				return layout.getPlid();
			}

			@Override
			public int getStatus() {
				if (layout.isDraftLayout()) {
					return WorkflowConstants.STATUS_DRAFT;
				}

				return layout.getStatus();
			}

			@Override
			public String getTitle(Locale locale) {
				return layout.getName(locale);
			}

			@Override
			public String getTypeName(Locale locale) {
				return _language.get(locale, "content-page");
			}

			@Override
			public String getUserName() {
				return layout.getUserName();
			}

		};
	}

	private static final Log _log = LogFactoryUtil.getLog(
		LayoutLaunchEntryType.class);

	private static final Snapshot<LayoutContentVersionCreator>
		_layoutContentVersionCreatorSnapshot = new Snapshot<>(
			LayoutLaunchEntryType.class, LayoutContentVersionCreator.class,
			null, true);

	@Reference
	private Language _language;

	@Reference
	private LayoutLocalService _layoutLocalService;

	@Reference
	private Portal _portal;

}