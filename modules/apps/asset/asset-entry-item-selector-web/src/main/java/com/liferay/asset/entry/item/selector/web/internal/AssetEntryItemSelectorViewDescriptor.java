/**
 * Copyright (c) 2000-present Liferay, Inc. All rights reserved.
 *
 * This library is free software; you can redistribute it and/or modify it under
 * the terms of the GNU Lesser General Public License as published by the Free
 * Software Foundation; either version 2.1 of the License, or (at your option)
 * any later version.
 *
 * This library is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU Lesser General Public License for more
 * details.
 */

package com.liferay.asset.entry.item.selector.web.internal;

import com.liferay.asset.kernel.model.AssetEntry;
import com.liferay.asset.kernel.service.AssetEntryLocalServiceUtil;
import com.liferay.asset.util.AssetHelper;
import com.liferay.depot.model.DepotEntry;
import com.liferay.depot.service.DepotEntryServiceUtil;
import com.liferay.item.selector.ItemSelectorReturnType;
import com.liferay.item.selector.ItemSelectorViewDescriptor;
import com.liferay.item.selector.criteria.AssetEntryItemSelectorReturnType;
import com.liferay.item.selector.criteria.asset.criterion.AssetEntryItemSelectorCriterion;
import com.liferay.petra.string.StringPool;
import com.liferay.portal.kernel.dao.orm.QueryUtil;
import com.liferay.portal.kernel.dao.search.SearchContainer;
import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.search.Hits;
import com.liferay.portal.kernel.theme.ThemeDisplay;
import com.liferay.portal.kernel.util.ArrayUtil;
import com.liferay.portal.kernel.util.GetterUtil;
import com.liferay.portal.kernel.util.JavaConstants;
import com.liferay.portal.kernel.util.ListUtil;
import com.liferay.portal.kernel.util.ParamUtil;
import com.liferay.portal.kernel.util.PortalUtil;
import com.liferay.portal.kernel.util.PropsKeys;
import com.liferay.portal.kernel.util.PropsUtil;
import com.liferay.portal.kernel.util.Validator;
import com.liferay.portal.kernel.util.WebKeys;
import com.liferay.portal.kernel.workflow.WorkflowConstants;

import javax.portlet.PortletRequest;
import javax.portlet.PortletURL;

import javax.servlet.http.HttpServletRequest;

/**
 * @author Barbara Cabrera
 */
public class AssetEntryItemSelectorViewDescriptor
	implements ItemSelectorViewDescriptor<AssetEntry> {

	public AssetEntryItemSelectorViewDescriptor(
		AssetEntryItemSelectorCriterion assetEntryItemSelectorCriterion,
		AssetHelper assetHelper, HttpServletRequest httpServletRequest,
		PortletURL portletURL) {

		_assetEntryItemSelectorCriterion = assetEntryItemSelectorCriterion;
		_assetHelper = assetHelper;
		_httpServletRequest = httpServletRequest;
		_portletURL = portletURL;
	}

	@Override
	public String getDefaultDisplayStyle() {
		return "list";
	}

	@Override
	public ItemDescriptor getItemDescriptor(AssetEntry assetEntry) {
		return new AssetEntryItemDescriptor(assetEntry, _httpServletRequest);
	}

	@Override
	public ItemSelectorReturnType getItemSelectorReturnType() {
		return new AssetEntryItemSelectorReturnType();
	}

	@Override
	public SearchContainer<AssetEntry> getSearchContainer()
		throws PortalException {

		if (_searchContainer != null) {
			return _searchContainer;
		}

		SearchContainer<AssetEntry> searchContainer = new SearchContainer(
			(PortletRequest)_httpServletRequest.getAttribute(
				JavaConstants.JAVAX_PORTLET_REQUEST),
			_portletURL, null, "there-are-no-results");

		if (GetterUtil.getBoolean(
				PropsUtil.get(PropsKeys.ASSET_BROWSER_SEARCH_WITH_DATABASE))) {

			long[] classTypeIds = ArrayUtil.filter(
				new long[] {
					_assetEntryItemSelectorCriterion.getSubtypeSelectionId()
				},
				subtypeSelectionId -> subtypeSelectionId >= 0);

			searchContainer.setResultsAndTotal(
				() -> AssetEntryLocalServiceUtil.getEntries(
					_getFilterGroupIds(), _getClassNameIds(), classTypeIds,
					_getKeywords(), _getKeywords(), _getKeywords(),
					_getKeywords(), null, false, false, QueryUtil.ALL_POS,
					QueryUtil.ALL_POS, "modifiedDate", StringPool.BLANK,
					StringPool.BLANK, StringPool.BLANK),
				AssetEntryLocalServiceUtil.getEntriesCount(
					_getFilterGroupIds(), _getClassNameIds(), classTypeIds,
					_getKeywords(), _getKeywords(), _getKeywords(),
					_getKeywords(), null, false, false));
		}
		else {
			ThemeDisplay themeDisplay =
				(ThemeDisplay)_httpServletRequest.getAttribute(
					WebKeys.THEME_DISPLAY);

			Hits hits = AssetEntryLocalServiceUtil.search(
				themeDisplay.getCompanyId(), _getFilterGroupIds(),
				themeDisplay.getUserId(), _getClassNameIds(),
				_assetEntryItemSelectorCriterion.getSubtypeSelectionId(),
				_getKeywords(),
				_assetEntryItemSelectorCriterion.isShowNonindexable(),
				_getStatuses(), QueryUtil.ALL_POS, QueryUtil.ALL_POS, null);

			searchContainer.setResultsAndTotal(
				() -> _assetHelper.getAssetEntries(hits), hits.getLength());

			_searchContainer = searchContainer;
		}

		return _searchContainer;
	}

	@Override
	public boolean isShowSearch() {
		return true;
	}

	private long[] _getClassNameIds() {
		if (_classNameIds != null) {
			return _classNameIds;
		}

		if (Validator.isNotNull(
				_assetEntryItemSelectorCriterion.getTypeSelection())) {

			_classNameIds = new long[] {
				PortalUtil.getClassNameId(
					_assetEntryItemSelectorCriterion.getTypeSelection())
			};
		}

		return _classNameIds;
	}

	private long[] _getFilterGroupIds() throws PortalException {
		if (_filterGroupIds != null) {
			return _filterGroupIds;
		}

		if (_getGroupId() == 0) {
			_filterGroupIds =
				_assetEntryItemSelectorCriterion.getSelectedGroupIds();
		}
		else {
			_filterGroupIds = ArrayUtil.append(
				PortalUtil.getCurrentAndAncestorSiteGroupIds(_getGroupId()),
				ListUtil.toLongArray(
					DepotEntryServiceUtil.getGroupConnectedDepotEntries(
						_getGroupId(), QueryUtil.ALL_POS, QueryUtil.ALL_POS),
					DepotEntry::getGroupId));
		}

		return _filterGroupIds;
	}

	private long _getGroupId() {
		ThemeDisplay themeDisplay =
			(ThemeDisplay)_httpServletRequest.getAttribute(
				WebKeys.THEME_DISPLAY);

		if (_assetEntryItemSelectorCriterion.getGroupId() ==
				themeDisplay.getRefererGroupId()) {

			return themeDisplay.getScopeGroupId();
		}

		return _assetEntryItemSelectorCriterion.getGroupId();
	}

	private String _getKeywords() {
		if (_keywords != null) {
			return _keywords;
		}

		_keywords = ParamUtil.getString(_httpServletRequest, "keywords");

		return _keywords;
	}

	private int[] _getStatuses() {
		int[] statuses = {WorkflowConstants.STATUS_APPROVED};

		if (_assetEntryItemSelectorCriterion.isShowScheduled()) {
			statuses = new int[] {
				WorkflowConstants.STATUS_APPROVED,
				WorkflowConstants.STATUS_SCHEDULED
			};
		}

		return statuses;
	}

	private final AssetEntryItemSelectorCriterion
		_assetEntryItemSelectorCriterion;
	private final AssetHelper _assetHelper;
	private long[] _classNameIds;
	private long[] _filterGroupIds;
	private final HttpServletRequest _httpServletRequest;
	private String _keywords;
	private final PortletURL _portletURL;
	private SearchContainer<AssetEntry> _searchContainer;

}