/**
 * SPDX-FileCopyrightText: (c) 2023 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.site.admin.web.internal.portal.settings.configuration.admin.display;

import com.liferay.portal.kernel.feature.flag.FeatureFlagManagerUtil;
import com.liferay.portal.kernel.language.Language;
import com.liferay.portal.kernel.model.Group;
import com.liferay.portal.kernel.service.LayoutLocalService;
import com.liferay.portal.kernel.util.JavaConstants;
import com.liferay.portal.kernel.util.Portal;
import com.liferay.site.admin.web.internal.display.context.LockedLayoutsDisplayContext;
import com.liferay.site.settings.configuration.admin.display.SiteSettingsConfigurationScreenContributor;

import java.util.Locale;

import javax.portlet.PortletRequest;
import javax.portlet.PortletResponse;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

/**
 * @author Lourdes Fernández Besada
 */
@Component(service = SiteSettingsConfigurationScreenContributor.class)
public class LockedLayoutsSiteSettingsConfigurationScreenContributor
	implements SiteSettingsConfigurationScreenContributor {

	@Override
	public String getCategoryKey() {
		return "pages";
	}

	@Override
	public String getJspPath() {
		return "/site_settings/locked_layouts.jsp";
	}

	@Override
	public String getKey() {
		return "site-configuration-locked-pages";
	}

	@Override
	public String getName(Locale locale) {
		return _language.get(locale, "locked-pages");
	}

	@Override
	public ServletContext getServletContext() {
		return _servletContext;
	}

	@Override
	public boolean isVisible(Group group) {
		if (FeatureFlagManagerUtil.isEnabled("LPS-180328")) {
			return true;
		}

		return false;
	}

	@Override
	public void setAttributes(
		HttpServletRequest httpServletRequest,
		HttpServletResponse httpServletResponse) {

		httpServletRequest.setAttribute(
			LockedLayoutsDisplayContext.class.getName(),
			new LockedLayoutsDisplayContext(
				_language, _layoutLocalService,
				_portal.getLiferayPortletRequest(
					(PortletRequest)httpServletRequest.getAttribute(
						JavaConstants.JAVAX_PORTLET_REQUEST)),
				_portal.getLiferayPortletResponse(
					(PortletResponse)httpServletRequest.getAttribute(
						JavaConstants.JAVAX_PORTLET_RESPONSE))));
	}

	@Reference
	private Language _language;

	@Reference
	private LayoutLocalService _layoutLocalService;

	@Reference
	private Portal _portal;

	@Reference(target = "(osgi.web.symbolicname=com.liferay.site.admin.web)")
	private ServletContext _servletContext;

}