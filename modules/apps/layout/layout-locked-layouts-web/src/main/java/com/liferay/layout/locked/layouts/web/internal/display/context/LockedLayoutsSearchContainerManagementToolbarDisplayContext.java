/**
 * SPDX-FileCopyrightText: (c) 2023 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.layout.locked.layouts.web.internal.display.context;

import com.liferay.frontend.taglib.clay.servlet.taglib.display.context.SearchContainerManagementToolbarDisplayContext;
import com.liferay.frontend.taglib.clay.servlet.taglib.util.DropdownItem;
import com.liferay.frontend.taglib.clay.servlet.taglib.util.DropdownItemListBuilder;
import com.liferay.portal.kernel.language.LanguageUtil;
import com.liferay.portal.kernel.portlet.LiferayPortletRequest;
import com.liferay.portal.kernel.portlet.LiferayPortletResponse;
import com.liferay.portal.kernel.portlet.url.builder.PortletURLBuilder;
import com.liferay.portal.kernel.theme.ThemeDisplay;
import com.liferay.portal.kernel.util.HttpComponentsUtil;
import com.liferay.portal.kernel.util.WebKeys;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * @author Lourdes Fernández Besada
 */
public class LockedLayoutsSearchContainerManagementToolbarDisplayContext
	extends SearchContainerManagementToolbarDisplayContext {

	public LockedLayoutsSearchContainerManagementToolbarDisplayContext(
		HttpServletRequest httpServletRequest,
		LiferayPortletRequest liferayPortletRequest,
		LiferayPortletResponse liferayPortletResponse,
		LockedLayoutsDisplayContext lockedLayoutsDisplayContext) {

		super(
			httpServletRequest, liferayPortletRequest, liferayPortletResponse,
			lockedLayoutsDisplayContext.getSearchContainer());
	}

	@Override
	public List<DropdownItem> getActionDropdownItems() {
		return DropdownItemListBuilder.add(
			dropdownItem -> {
				dropdownItem.setIcon("unlock");
				dropdownItem.setLabel(
					LanguageUtil.get(httpServletRequest, "unlock"));
				dropdownItem.setHref(
					PortletURLBuilder.createActionURL(
						liferayPortletResponse
					).setActionName("/layout_locked_layouts/unlock_layouts"
					).setRedirect(
						() -> {
							ThemeDisplay themeDisplay = (ThemeDisplay)httpServletRequest.getAttribute(
								WebKeys.THEME_DISPLAY);

							return themeDisplay.getURLCurrent();

						}
					).buildString()
				);
				dropdownItem.setQuickAction(true);
			}
		).build();
	}

	@Override
	public String getClearResultsURL() {
		return HttpComponentsUtil.removeParameter(
			String.valueOf(getPortletURL()), getNamespace() + "keywords");
	}

	@Override
	public String getSearchContainerId() {
		return "lockedLayoutsSearchContainer";
	}

	@Override
	public String getSortingURL() {
		return null;
	}

}