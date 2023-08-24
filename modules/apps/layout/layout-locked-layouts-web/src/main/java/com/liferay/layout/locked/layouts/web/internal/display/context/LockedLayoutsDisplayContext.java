/**
 * SPDX-FileCopyrightText: (c) 2023 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.layout.locked.layouts.web.internal.display.context;

import com.liferay.frontend.taglib.clay.servlet.taglib.util.DropdownItem;
import com.liferay.frontend.taglib.clay.servlet.taglib.util.DropdownItemListBuilder;
import com.liferay.layout.model.LockedLayout;
import com.liferay.layout.manager.LayoutLockManager;
import com.liferay.petra.string.StringPool;
import com.liferay.portal.kernel.dao.search.EmptyOnClickRowChecker;
import com.liferay.portal.kernel.dao.search.SearchContainer;
import com.liferay.portal.kernel.language.Language;
import com.liferay.portal.kernel.language.LanguageUtil;
import com.liferay.portal.kernel.portlet.LiferayPortletRequest;
import com.liferay.portal.kernel.portlet.LiferayPortletResponse;
import com.liferay.portal.kernel.portlet.PortletURLUtil;
import com.liferay.portal.kernel.portlet.url.builder.PortletURLBuilder;
import com.liferay.portal.kernel.theme.ThemeDisplay;
import com.liferay.portal.kernel.util.ListUtil;
import com.liferay.portal.kernel.util.LocalizationUtil;
import com.liferay.portal.kernel.util.ParamUtil;
import com.liferay.portal.kernel.util.StringUtil;
import com.liferay.portal.kernel.util.Validator;
import com.liferay.portal.kernel.util.WebKeys;

import java.util.Date;
import java.util.List;

/**
 * @author Lourdes Fernández Besada
 */
public class LockedLayoutsDisplayContext {

	public LockedLayoutsDisplayContext(
		Language language, LayoutLockManager layoutLockManager,
		LiferayPortletRequest liferayPortletRequest,
		LiferayPortletResponse liferayPortletResponse) {

		_language = language;
		_layoutLockManager = layoutLockManager;
		_liferayPortletRequest = liferayPortletRequest;
		_liferayPortletResponse = liferayPortletResponse;

		_themeDisplay = (ThemeDisplay)liferayPortletRequest.getAttribute(
			WebKeys.THEME_DISPLAY);
	}

	public boolean existLockedLayouts() {
		if (ListUtil.isEmpty(_getLockedLayouts())) {
			return false;
		}

		return true;
	}

	public String getLastAutoSave(LockedLayout lockedLayout) throws Exception {
		Date lastAutoSaveDate = lockedLayout.getLastAutoSaveDate();

		return _language.format(
			_themeDisplay.getLocale(), "x-ago",
			_language.getTimeDescription(
				_themeDisplay.getLocale(),
				System.currentTimeMillis() - lastAutoSaveDate.getTime(), true));
	}

	public String getLayoutType(LockedLayout lockedLayout) {
		return _layoutLockManager.getLayoutType(
			lockedLayout.getClassPK(), _themeDisplay.getLocale(),
			lockedLayout.getType());
	}

	public String getName(LockedLayout lockedLayout) {
		return LocalizationUtil.getLocalization(
			lockedLayout.getName(), _themeDisplay.getLanguageId());
	}

	public List<DropdownItem> getLockedLayoutDropdownItems(LockedLayout lockedLayout) {
		return DropdownItemListBuilder.add(
			dropdownItem -> {
				dropdownItem.setIcon("unlock");
				dropdownItem.setLabel(
					LanguageUtil.get(_themeDisplay.getLocale(), "unlock"));
				dropdownItem.setHref(
					PortletURLBuilder.createActionURL(
						_liferayPortletResponse
					).setActionName(
						"/layout_locked_layouts/unlock_layouts"
					).setRedirect(
						_themeDisplay.getURLCurrent()
					).build(), "plid", lockedLayout.getPlid()
				);
			}
		).build();
	}

	public SearchContainer<LockedLayout> getSearchContainer() {
		if (_searchContainer != null) {
			return _searchContainer;
		}

		SearchContainer<LockedLayout> searchContainer = new SearchContainer(
			_liferayPortletRequest,
			PortletURLUtil.getCurrent(
				_liferayPortletRequest, _liferayPortletResponse),
			ListUtil.fromArray("name", "type", "current-user", "last-autosave"),
			"no-locked-pages-were-found");

		List<LockedLayout> lockedLayouts = _getFilteredLockedLayouts();

		searchContainer.setResultsAndTotal(
			() -> lockedLayouts, lockedLayouts.size());

		searchContainer.setRowChecker(
			new EmptyOnClickRowChecker(_liferayPortletResponse));

		_searchContainer = searchContainer;

		return _searchContainer;
	}

	private List<LockedLayout> _getFilteredLockedLayouts() {
		if (_filteredLockedLayouts != null) {
			return _filteredLockedLayouts;
		}

		if (Validator.isNull(_getKeywords())) {
			_filteredLockedLayouts = _getLockedLayouts();

			return _getLockedLayouts();
		}

		String keywords = StringUtil.toLowerCase(_getKeywords());

		_filteredLockedLayouts = ListUtil.filter(
			_getLockedLayouts(),
			lockedLayout -> _hasKeywords(keywords, lockedLayout));

		return _filteredLockedLayouts;
	}

	private String _getKeywords() {
		if (_keywords != null) {
			return _keywords;
		}

		_keywords = ParamUtil.getString(_liferayPortletRequest, "keywords");

		return _keywords;
	}

	private List<LockedLayout> _getLockedLayouts() {
		if (_lockedLayouts != null) {
			return _lockedLayouts;
		}

		_lockedLayouts = _layoutLockManager.getLockedLayouts(
			_themeDisplay.getScopeGroupId());

		return _lockedLayouts;
	}

	private boolean _hasKeywords(String keywords, LockedLayout lockedLayout) {
		if (StringUtil.contains(
				StringUtil.toLowerCase(lockedLayout.getUserName()), keywords,
				StringPool.BLANK) ||
			StringUtil.contains(
				StringUtil.toLowerCase(getName(lockedLayout)), keywords,
				StringPool.BLANK)) {

			return true;
		}

		return false;
	}

	private List<LockedLayout> _filteredLockedLayouts;
	private String _keywords;
	private final Language _language;
	private final LayoutLockManager _layoutLockManager;
	private final LiferayPortletRequest _liferayPortletRequest;
	private final LiferayPortletResponse _liferayPortletResponse;
	private List<LockedLayout> _lockedLayouts;
	private SearchContainer<LockedLayout> _searchContainer;
	private final ThemeDisplay _themeDisplay;

}