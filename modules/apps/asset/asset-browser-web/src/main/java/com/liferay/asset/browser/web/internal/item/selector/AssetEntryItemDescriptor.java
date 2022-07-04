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

import com.liferay.asset.kernel.model.AssetEntry;
import com.liferay.item.selector.ItemSelectorViewDescriptor;
import com.liferay.petra.string.StringPool;
import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.json.JSONUtil;
import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.portal.kernel.model.Group;
import com.liferay.portal.kernel.service.GroupLocalServiceUtil;
import com.liferay.portal.kernel.theme.ThemeDisplay;
import com.liferay.portal.kernel.util.StringUtil;
import com.liferay.portal.kernel.util.WebKeys;

import java.util.Date;
import java.util.Locale;

import javax.servlet.http.HttpServletRequest;

/**
 * @author Barbara Cabrera
 */

public class AssetEntryItemDescriptor
	implements ItemSelectorViewDescriptor.ItemDescriptor{


	public AssetEntryItemDescriptor(AssetEntry assetEntry,
									HttpServletRequest httpServletRequest) {

		_assetEntry = assetEntry;
		_httpServletRequest = httpServletRequest;

	}

	@Override
	public String getIcon() {
		return "pages-tree";
	}

	@Override
	public String getImageURL() {
		return null;
	}

	@Override
	public Date getModifiedDate() {
		return _assetEntry.getModifiedDate();
	}

	public String getPayload() {
		return JSONUtil.put(
			"className", _assetEntry.getClassName()
		).put(
			"classNameId", _assetEntry.getClassNameId()
		).put(
			"classPK", String.valueOf(_assetEntry.getClassPK())
		).put(
			"title", _assetEntry.getTitle()
		).toString();
	}

	@Override
	public String getSubtitle(Locale locale) {
		return StringPool.BLANK; //STATUS
	}

	@Override
	public String getTitle(Locale locale) {
		return _assetEntry.getTitle();
	}

	private final HttpServletRequest _httpServletRequest;
	private final AssetEntry _assetEntry;
}
