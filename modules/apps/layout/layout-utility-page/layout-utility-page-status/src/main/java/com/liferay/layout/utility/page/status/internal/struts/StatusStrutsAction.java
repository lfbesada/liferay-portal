/**
 * SPDX-FileCopyrightText: (c) 2000 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.layout.utility.page.status.internal.struts;

import com.liferay.petra.io.unsync.UnsyncStringWriter;
import com.liferay.portal.kernel.model.LayoutSet;
import com.liferay.portal.kernel.model.Theme;
import com.liferay.portal.kernel.service.LayoutSetLocalService;
import com.liferay.portal.kernel.service.ThemeLocalService;
import com.liferay.portal.kernel.servlet.PipingServletResponse;
import com.liferay.portal.kernel.servlet.PortalMessages;
import com.liferay.portal.kernel.servlet.ServletResponseUtil;
import com.liferay.portal.kernel.servlet.SessionMessages;
import com.liferay.portal.kernel.struts.StrutsAction;
import com.liferay.portal.kernel.theme.ThemeDisplay;
import com.liferay.portal.kernel.theme.ThemeUtil;
import com.liferay.portal.kernel.util.ColorSchemeFactoryUtil;
import com.liferay.portal.kernel.util.WebKeys;
import com.liferay.portal.util.ThemeFactoryUtil;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

/**
 * @author Lourdes Fernández Besada
 */
@Component(property = "path=/portal/status", service = StrutsAction.class)
public class StatusStrutsAction implements StrutsAction {

	@Override
	public String execute(
			HttpServletRequest httpServletRequest,
			HttpServletResponse httpServletResponse)
		throws Exception {

		ThemeDisplay themeDisplay =
			(ThemeDisplay)httpServletRequest.getAttribute(
				WebKeys.THEME_DISPLAY);

		LayoutSet layoutSet = _layoutSetLocalService.getLayoutSet(
			themeDisplay.getScopeGroupId(), false);

		themeDisplay.setLayoutSet(layoutSet);

		Theme theme = layoutSet.getTheme();

		themeDisplay.setLookAndFeel(theme, layoutSet.getColorScheme());

		httpServletRequest.setAttribute(WebKeys.THEME_DISPLAY, themeDisplay);

		String html = _getHTML(httpServletRequest, httpServletResponse, theme);

		if (html == null) {
			theme = _themeLocalService.getTheme(
				themeDisplay.getCompanyId(),
				ThemeFactoryUtil.getDefaultRegularThemeId(
					themeDisplay.getCompanyId()));

			themeDisplay.setLookAndFeel(
				theme,
				_themeLocalService.getColorScheme(
					themeDisplay.getCompanyId(), theme.getThemeId(),
					ColorSchemeFactoryUtil.getDefaultRegularColorSchemeId()));

			html = _getHTML(httpServletRequest, httpServletResponse, theme);
		}

		if (html != null) {
			ServletResponseUtil.write(httpServletResponse, html);
		}

		return null;
	}

	private String _getHTML(
			HttpServletRequest httpServletRequest,
			HttpServletResponse httpServletResponse, Theme theme)
		throws Exception {

		RequestDispatcher requestDispatcher =
			_servletContext.getRequestDispatcher("/status.jsp");

		UnsyncStringWriter unsyncStringWriter = new UnsyncStringWriter();

		PipingServletResponse pipingServletResponse = new PipingServletResponse(
			httpServletResponse, unsyncStringWriter);

		requestDispatcher.include(httpServletRequest, pipingServletResponse);

		Document document = Jsoup.parse(
			ThemeUtil.include(
				httpServletRequest.getServletContext(), httpServletRequest,
				httpServletResponse, "portal_normal.ftl", theme, false));

		PortalMessages.clear(httpServletRequest);
		SessionMessages.clear(httpServletRequest);

		Element contentElement = document.getElementById("content");

		if (contentElement == null) {
			return null;
		}

		contentElement.html(unsyncStringWriter.toString());

		return document.html();
	}

	@Reference
	private LayoutSetLocalService _layoutSetLocalService;

	@Reference(
		target = "(osgi.web.symbolicname=com.liferay.layout.utility.page.status)"
	)
	private ServletContext _servletContext;

	@Reference
	private ThemeLocalService _themeLocalService;

}