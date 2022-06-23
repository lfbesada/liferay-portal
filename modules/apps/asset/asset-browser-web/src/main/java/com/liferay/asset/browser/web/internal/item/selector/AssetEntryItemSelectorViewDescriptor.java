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

package com.liferay.asset.browser.web.internal.item.selector;

import com.liferay.asset.browser.web.internal.display.context.AssetBrowserDisplayContext;
import com.liferay.asset.kernel.model.AssetEntry;
import com.liferay.asset.kernel.model.AssetVocabulary;
import com.liferay.item.selector.ItemSelectorReturnType;
import com.liferay.item.selector.ItemSelectorViewDescriptor;
import com.liferay.item.selector.criteria.AssetEntryItemSelectorReturnType;
import com.liferay.item.selector.criteria.asset.criterion.AssetEntryItemSelectorCriterion;
import com.liferay.portal.kernel.dao.search.SearchContainer;
import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.theme.ThemeDisplay;
import com.liferay.portal.kernel.util.JavaConstants;
import com.liferay.portal.kernel.util.WebKeys;

import javax.portlet.PortletException;
import javax.portlet.PortletRequest;
import javax.portlet.PortletURL;

import javax.servlet.http.HttpServletRequest;

/**
 * @author Barbara Cabrera
 */
public class AssetEntryItemSelectorViewDescriptor implements
	ItemSelectorViewDescriptor<AssetEntry> {

	public AssetEntryItemSelectorViewDescriptor(
		AssetEntryItemSelectorCriterion
			assetEntryItemSelectorCriterion,
		HttpServletRequest httpServletRequest, PortletURL portletURL,
		AssetBrowserDisplayContext assetBrowserDisplayContext) {

		_assetEntryItemSelectorCriterion =
			assetEntryItemSelectorCriterion;
		_httpServletRequest = httpServletRequest;
		_portletURL = portletURL;
		_assetBrowserDisplayContext = assetBrowserDisplayContext;

		_themeDisplay = (ThemeDisplay)_httpServletRequest.getAttribute(
			WebKeys.THEME_DISPLAY);
	}

	@Override
	public String getDefaultDisplayStyle() {
		return "list";
	}

	@Override
	public ItemDescriptor getItemDescriptor(AssetEntry assetEntry) {
		return new AssetEntryItemDescriptor(
			assetEntry, _httpServletRequest);
	}

	@Override
	public ItemSelectorReturnType getItemSelectorReturnType() {
		return new AssetEntryItemSelectorReturnType();
	}

	@Override
	public SearchContainer<AssetEntry> getSearchContainer()
		throws PortalException{

		try {
			return _assetBrowserDisplayContext.getAssetBrowserSearch();
		}
		catch (PortletException portletException) {
			throw new PortalException(portletException);
		}

	}

	private final AssetEntryItemSelectorCriterion
		_assetEntryItemSelectorCriterion;
	private final HttpServletRequest _httpServletRequest;
	private final PortletURL _portletURL;
	private final ThemeDisplay _themeDisplay;
	private final AssetBrowserDisplayContext _assetBrowserDisplayContext;
}
