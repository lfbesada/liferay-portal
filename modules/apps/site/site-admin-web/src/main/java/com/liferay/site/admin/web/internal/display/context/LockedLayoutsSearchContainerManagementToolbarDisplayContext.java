/**
 * SPDX-FileCopyrightText: (c) 2023 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.site.admin.web.internal.display.context;

import com.liferay.frontend.taglib.clay.servlet.taglib.display.context.SearchContainerManagementToolbarDisplayContext;
import com.liferay.portal.kernel.portlet.LiferayPortletRequest;
import com.liferay.portal.kernel.portlet.LiferayPortletResponse;
import com.liferay.portal.kernel.util.HttpComponentsUtil;

import javax.servlet.http.HttpServletRequest;

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