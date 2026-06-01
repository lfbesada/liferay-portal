/**
 * SPDX-FileCopyrightText: (c) 2026 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.layout.content.versioning.web.internal.display.context;

import com.liferay.petra.string.StringBundler;
import com.liferay.portal.kernel.model.Group;
import com.liferay.portal.kernel.model.Layout;
import com.liferay.portal.kernel.service.LayoutLocalService;
import com.liferay.portal.kernel.theme.ThemeDisplay;
import com.liferay.portal.kernel.util.HashMapBuilder;
import com.liferay.portal.kernel.util.WebKeys;

import jakarta.servlet.http.HttpServletRequest;

import java.util.Map;

/**
 * @author Lourdes Fernández Besada
 */
public class LayoutContentVersioningDisplayContext {

	public LayoutContentVersioningDisplayContext(
		HttpServletRequest httpServletRequest,
		LayoutLocalService layoutLocalService) {

		_layoutLocalService = layoutLocalService;

		_themeDisplay = (ThemeDisplay)httpServletRequest.getAttribute(
			WebKeys.THEME_DISPLAY);
	}

	public Map<String, Object> getContext() throws Exception {
		return HashMapBuilder.<String, Object>put(
			"config",
			HashMapBuilder.<String, Object>put(
				"pageSpecificationVersionsURL",
				_getPageSpecificationVersionsURL()
			).build()
		).build();
	}

	private String _getPageSpecificationVersionsURL() throws Exception {
		Layout draftLayout = _themeDisplay.getLayout();

		Layout publishedLayout = _layoutLocalService.getLayout(
			draftLayout.getClassPK());

		Group scopeGroup = _themeDisplay.getScopeGroup();

		return StringBundler.concat(
			"/o/headless-admin-site/v1.0/sites/",
			scopeGroup.getExternalReferenceCode(), "/site-pages/",
			publishedLayout.getExternalReferenceCode(),
			"/page-specification-versions");
	}

	private final LayoutLocalService _layoutLocalService;
	private final ThemeDisplay _themeDisplay;

}