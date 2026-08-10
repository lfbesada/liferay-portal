/**
 * SPDX-FileCopyrightText: (c) 2026 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.style.book.web.internal.design.library;

import com.liferay.portal.kernel.json.JSONObject;
import com.liferay.portal.kernel.json.JSONUtil;
import com.liferay.portal.kernel.model.Group;
import com.liferay.portal.kernel.portlet.bridges.mvc.MVCRenderCommand;
import com.liferay.portal.kernel.portlet.url.builder.PortletURLBuilder;
import com.liferay.portal.kernel.theme.ThemeDisplay;
import com.liferay.portal.kernel.util.ParamUtil;
import com.liferay.portal.kernel.util.PortalUtil;
import com.liferay.portal.kernel.util.WebKeys;
import com.liferay.style.book.constants.StyleBookPortletKeys;
import com.liferay.style.book.util.StyleBookUtil;

import jakarta.portlet.PortletRequest;
import jakarta.portlet.RenderRequest;
import jakarta.portlet.RenderResponse;

import org.osgi.service.component.annotations.Component;

/**
 * @author Lourdes Fernández Besada
 */
@Component(
	property = {
		"jakarta.portlet.name=" + StyleBookPortletKeys.STYLE_BOOK,
		"mvc.command.name=/style_book/design_library/add_style_book_entry"
	},
	service = MVCRenderCommand.class
)
public class AddStyleBookEntryMVCRenderCommand implements MVCRenderCommand {

	public static final String PROPS_ATTRIBUTE =
		AddStyleBookEntryMVCRenderCommand.class.getName() + "#PROPS";

	@Override
	public String render(
		RenderRequest renderRequest, RenderResponse renderResponse) {

		renderRequest.setAttribute(PROPS_ATTRIBUTE, _getProps(renderRequest));

		return "/design_library/add_style_book_entry.jsp";
	}

	private JSONObject _getProps(RenderRequest renderRequest) {
		ThemeDisplay themeDisplay = (ThemeDisplay)renderRequest.getAttribute(
			WebKeys.THEME_DISPLAY);

		Group group = themeDisplay.getScopeGroup();

		String backURL = ParamUtil.getString(renderRequest, "backURL");

		return JSONUtil.put(
			"addStyleBookEntryURL",
			PortletURLBuilder.create(
				PortalUtil.getControlPanelPortletURL(
					PortalUtil.getHttpServletRequest(renderRequest), group,
					StyleBookPortletKeys.STYLE_BOOK, 0, 0,
					PortletRequest.ACTION_PHASE)
			).setActionName(
				"/style_book/add_style_book_entry"
			).setRedirect(
				backURL
			).setParameter(
				"backURLTitle", group.getName(themeDisplay.getLocale())
			).buildString()
		).put(
			"frontendTokenDefinitionProviders",
			StyleBookUtil.getFrontendTokenDefinitionProviders(
				themeDisplay.getCompanyId(), themeDisplay.getLocale())
		).put(
			"namespace",
			PortalUtil.getPortletNamespace(StyleBookPortletKeys.STYLE_BOOK)
		);
	}

}