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

package com.liferay.layout.admin.web.internal.servlet.taglib.clay;

import com.liferay.frontend.taglib.clay.servlet.taglib.soy.VerticalCard;
import com.liferay.portal.kernel.repository.model.FileEntry;
import com.liferay.portal.kernel.util.HashMapBuilder;
import com.liferay.portal.kernel.util.ParamUtil;

import java.util.Map;
import java.util.Objects;

import javax.portlet.RenderRequest;

/**
 * @author Rubén Pulido
 */
public class SelectFaviconVerticalCard implements VerticalCard {

	public SelectFaviconVerticalCard(
		FileEntry fileEntry, RenderRequest renderRequest) {

		_fileEntry = fileEntry;
		_renderRequest = renderRequest;
	}

	@Override
	public String getCssClass() {
		String cssClass =
			"select-favicon-option card-interactive " +
				"card-interactive-secondary";

		long faviconFileEntryId = ParamUtil.getLong(
			_renderRequest, "faviconFileEntryId");

		if (Objects.equals(_fileEntry.getFileEntryId(), faviconFileEntryId)) {
			cssClass += " active";
		}

		return cssClass;
	}

	@Override
	public Map<String, String> getDynamicAttributes() {
		return HashMapBuilder.put(
			"data-faviconfileentryid",
			String.valueOf(_fileEntry.getFileEntryId())
		).put(
			"data-name", _fileEntry.getTitle()
		).put(
			"role", "button"
		).put(
			"tabIndex", "0"
		).build();
	}

	@Override
	public String getIcon() {
		return "documents-and-media";
	}

	@Override
	public String getStickerCssClass() {
		return "select-favicon-option-sticker sticker-primary";
	}

	@Override
	public String getStickerIcon() {
		return "check-circle";
	}

	@Override
	public String getTitle() {
		return _fileEntry.getTitle();
	}

	@Override
	public boolean isSelectable() {
		return false;
	}

	private final FileEntry _fileEntry;
	private final RenderRequest _renderRequest;

}