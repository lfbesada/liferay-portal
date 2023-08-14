/**
 * SPDX-FileCopyrightText: (c) 2023 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.site.admin.web.internal.display.context;

import com.liferay.layout.page.template.constants.LayoutPageTemplateEntryTypeConstants;
import com.liferay.layout.page.template.model.LayoutPageTemplateEntry;
import com.liferay.layout.page.template.service.LayoutPageTemplateEntryLocalService;
import com.liferay.layout.utility.page.kernel.LayoutUtilityPageEntryViewRenderer;
import com.liferay.layout.utility.page.kernel.LayoutUtilityPageEntryViewRendererRegistryUtil;
import com.liferay.layout.utility.page.model.LayoutUtilityPageEntry;
import com.liferay.layout.utility.page.service.LayoutUtilityPageEntryLocalService;
import com.liferay.petra.sql.dsl.DSLFunctionFactoryUtil;
import com.liferay.petra.sql.dsl.DSLQueryFactoryUtil;
import com.liferay.petra.string.StringPool;
import com.liferay.portal.kernel.dao.search.EmptyOnClickRowChecker;
import com.liferay.portal.kernel.dao.search.SearchContainer;
import com.liferay.portal.kernel.language.Language;
import com.liferay.portal.kernel.model.LayoutConstants;
import com.liferay.portal.kernel.model.LayoutTable;
import com.liferay.portal.kernel.portlet.LiferayPortletRequest;
import com.liferay.portal.kernel.portlet.LiferayPortletResponse;
import com.liferay.portal.kernel.portlet.PortletURLUtil;
import com.liferay.portal.kernel.service.LayoutLocalService;
import com.liferay.portal.kernel.theme.ThemeDisplay;
import com.liferay.portal.kernel.util.GetterUtil;
import com.liferay.portal.kernel.util.HtmlUtil;
import com.liferay.portal.kernel.util.ListUtil;
import com.liferay.portal.kernel.util.LocalizationUtil;
import com.liferay.portal.kernel.util.WebKeys;
import com.liferay.portal.kernel.workflow.WorkflowConstants;
import com.liferay.portal.lock.model.LockTable;

import java.io.Serializable;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;

/**
 * @author Lourdes Fernández Besada
 */
public class LockedLayoutsDisplayContext {

	public LockedLayoutsDisplayContext(
		Language language, LayoutLocalService layoutLocalService,
		LayoutPageTemplateEntryLocalService layoutPageTemplateEntryLocalService,
		LayoutUtilityPageEntryLocalService layoutUtilityPageEntryLocalService,
		LiferayPortletRequest liferayPortletRequest,
		LiferayPortletResponse liferayPortletResponse) {

		_language = language;
		_layoutLocalService = layoutLocalService;
		_layoutPageTemplateEntryLocalService =
			layoutPageTemplateEntryLocalService;
		_layoutUtilityPageEntryLocalService =
			layoutUtilityPageEntryLocalService;
		_liferayPortletRequest = liferayPortletRequest;
		_liferayPortletResponse = liferayPortletResponse;

		_themeDisplay = (ThemeDisplay)liferayPortletRequest.getAttribute(
			WebKeys.THEME_DISPLAY);
	}

	public boolean existLockedLayouts() {
		if (ListUtil.isEmpty(_getLockedLayoutDTOs())) {
			return false;
		}

		return true;
	}

	public SearchContainer<LockedLayoutDTO> getSearchContainer() {
		if (_searchContainer != null) {
			return _searchContainer;
		}

		SearchContainer<LockedLayoutDTO> searchContainer = new SearchContainer(
			_liferayPortletRequest,
			PortletURLUtil.getCurrent(
				_liferayPortletRequest, _liferayPortletResponse),
			ListUtil.fromArray("name", "type", "current-user", "last-autosave"),
			"there-are-no-locked-pages");

		List<LockedLayoutDTO> lockedLayoutDTOs = _getLockedLayoutDTOs();

		searchContainer.setResultsAndTotal(
			() -> lockedLayoutDTOs, lockedLayoutDTOs.size());

		searchContainer.setRowChecker(
			new EmptyOnClickRowChecker(_liferayPortletResponse));

		_searchContainer = searchContainer;

		return _searchContainer;
	}

	public class LockedLayoutDTO implements Serializable {

		public LockedLayoutDTO(
			long classPK, Date lastAutoSaveDate, String name, long plid,
			String type, String userName) {

			_classPK = classPK;
			_lastAutoSaveDate = lastAutoSaveDate;
			_name = LocalizationUtil.getLocalization(
				name, _themeDisplay.getLanguageId());
			_plid = plid;
			_type = type;
			_userName = userName;
		}

		public String getLastAutoSave() throws Exception {
			return _language.format(
				_themeDisplay.getLocale(), "x-ago",
				_language.getTimeDescription(
					_themeDisplay.getLocale(),
					System.currentTimeMillis() - _lastAutoSaveDate.getTime(),
					true));
		}

		public String getLayoutType() {
			return _language.get(
				_themeDisplay.getLocale(), _getLayoutType(_classPK, _type));
		}

		public String getName() {
			return HtmlUtil.escape(_name);
		}

		public long getPlid() {
			return _plid;
		}

		public String getUserName() {
			return HtmlUtil.escape(_userName);
		}

		private final long _classPK;
		private final Date _lastAutoSaveDate;
		private final String _name;
		private final long _plid;
		private final String _type;
		private final String _userName;

	}

	private String _getLayoutPageTemplateEntryTypeLabel(
		LayoutPageTemplateEntry layoutPageTemplateEntry) {

		if (Objects.equals(
				layoutPageTemplateEntry.getType(),
				LayoutPageTemplateEntryTypeConstants.TYPE_BASIC)) {

			return "content-page-template";
		}

		if (Objects.equals(
				layoutPageTemplateEntry.getType(),
				LayoutPageTemplateEntryTypeConstants.TYPE_DISPLAY_PAGE)) {

			return "display-page-template";
		}

		if (Objects.equals(
				layoutPageTemplateEntry.getType(),
				LayoutPageTemplateEntryTypeConstants.TYPE_MASTER_LAYOUT)) {

			return "master";
		}

		return StringPool.BLANK;
	}

	private String _getLayoutType(long classPK, String type) {
		if (Objects.equals(type, LayoutConstants.TYPE_ASSET_DISPLAY)) {
			return "display-page-template";
		}

		if (Objects.equals(type, LayoutConstants.TYPE_COLLECTION)) {
			return "collection-page";
		}

		if (!Objects.equals(type, LayoutConstants.TYPE_CONTENT)) {
			return StringPool.BLANK;
		}

		LayoutPageTemplateEntry layoutPageTemplateEntry =
			_layoutPageTemplateEntryLocalService.
				fetchLayoutPageTemplateEntryByPlid(classPK);

		if (layoutPageTemplateEntry != null) {
			return _getLayoutPageTemplateEntryTypeLabel(
				layoutPageTemplateEntry);
		}

		LayoutUtilityPageEntry layoutUtilityPageEntry =
			_layoutUtilityPageEntryLocalService.
				fetchLayoutUtilityPageEntryByPlid(classPK);

		if (layoutUtilityPageEntry != null) {
			return _getLayoutUtilityPageEntryTypeLabel(layoutUtilityPageEntry);
		}

		return "content-page";
	}

	private String _getLayoutUtilityPageEntryTypeLabel(
		LayoutUtilityPageEntry layoutUtilityPageEntry) {

		LayoutUtilityPageEntryViewRenderer layoutUtilityPageEntryViewRenderer =
			LayoutUtilityPageEntryViewRendererRegistryUtil.
				getLayoutUtilityPageEntryViewRenderer(
					layoutUtilityPageEntry.getType());

		if (layoutUtilityPageEntryViewRenderer == null) {
			return StringPool.BLANK;
		}

		return layoutUtilityPageEntryViewRenderer.getLabel(
			_themeDisplay.getLocale());
	}

	private List<LockedLayoutDTO> _getLockedLayoutDTOs() {
		if (_lockedLayoutDTOs != null) {
			return _lockedLayoutDTOs;
		}

		List<Object[]> results = _layoutLocalService.dslQuery(
			DSLQueryFactoryUtil.select(
				LayoutTable.INSTANCE.classPK, LockTable.INSTANCE.createDate,
				LayoutTable.INSTANCE.name, LayoutTable.INSTANCE.plid,
				LayoutTable.INSTANCE.type, LockTable.INSTANCE.userName
			).from(
				LayoutTable.INSTANCE
			).innerJoinON(
				LockTable.INSTANCE,
				LockTable.INSTANCE.key.eq(
					DSLFunctionFactoryUtil.castText(LayoutTable.INSTANCE.plid))
			).where(
				LayoutTable.INSTANCE.groupId.eq(
					_themeDisplay.getScopeGroupId()
				).and(
					LayoutTable.INSTANCE.hidden.eq(true)
				).and(
					LayoutTable.INSTANCE.system.eq(true)
				).and(
					LayoutTable.INSTANCE.status.eq(
						WorkflowConstants.STATUS_DRAFT)
				).and(
					LayoutTable.INSTANCE.type.in(
						new String[] {
							LayoutConstants.TYPE_COLLECTION,
							LayoutConstants.TYPE_CONTENT
						})
				)
			).orderBy(
				orderByStep -> orderByStep.orderBy(
					LayoutTable.INSTANCE.modifiedDate.descending())
			));

		List<LockedLayoutDTO> lockedLayoutDTOs = new ArrayList<>(
			results.size());

		for (Object[] columns : results) {
			lockedLayoutDTOs.add(
				new LockedLayoutDTO(
					GetterUtil.getLong(columns[0]), (Date)columns[1],
					GetterUtil.getString(columns[2]),
					GetterUtil.getLong(columns[3]),
					GetterUtil.getString(columns[4]),
					GetterUtil.getString(columns[5])));
		}

		_lockedLayoutDTOs = lockedLayoutDTOs;

		return _lockedLayoutDTOs;
	}

	private final Language _language;
	private final LayoutLocalService _layoutLocalService;
	private final LayoutPageTemplateEntryLocalService
		_layoutPageTemplateEntryLocalService;
	private final LayoutUtilityPageEntryLocalService
		_layoutUtilityPageEntryLocalService;
	private final LiferayPortletRequest _liferayPortletRequest;
	private final LiferayPortletResponse _liferayPortletResponse;
	private List<LockedLayoutDTO> _lockedLayoutDTOs;
	private SearchContainer<LockedLayoutDTO> _searchContainer;
	private final ThemeDisplay _themeDisplay;

}