/**
 * SPDX-FileCopyrightText: (c) 2026 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.fragment.web.internal.design.library;

import com.liferay.fragment.constants.FragmentPortletKeys;
import com.liferay.fragment.model.FragmentCollection;
import com.liferay.fragment.service.FragmentCollectionLocalService;
import com.liferay.portal.kernel.dao.orm.QueryUtil;
import com.liferay.portal.kernel.json.JSONArray;
import com.liferay.portal.kernel.json.JSONFactory;
import com.liferay.portal.kernel.json.JSONObject;
import com.liferay.portal.kernel.json.JSONUtil;
import com.liferay.portal.kernel.model.Group;
import com.liferay.portal.kernel.portlet.LiferayPortletURL;
import com.liferay.portal.kernel.portlet.bridges.mvc.MVCRenderCommand;
import com.liferay.portal.kernel.portlet.url.builder.PortletURLBuilder;
import com.liferay.portal.kernel.theme.ThemeDisplay;
import com.liferay.portal.kernel.util.ParamUtil;
import com.liferay.portal.kernel.util.PortalUtil;
import com.liferay.portal.kernel.util.WebKeys;

import jakarta.portlet.PortletRequest;
import jakarta.portlet.RenderRequest;
import jakarta.portlet.RenderResponse;

import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

/**
 * @author Lourdes Fernández Besada
 */
@Component(
	property = {
		"jakarta.portlet.name=" + FragmentPortletKeys.FRAGMENT,
		"mvc.command.name=/fragment/design_library/add_fragment_entry"
	},
	service = MVCRenderCommand.class
)
public class AddFragmentEntryMVCRenderCommand implements MVCRenderCommand {

	public static final String PROPS_ATTRIBUTE =
		AddFragmentEntryMVCRenderCommand.class.getName() + "#PROPS";

	@Override
	public String render(
		RenderRequest renderRequest, RenderResponse renderResponse) {

		renderRequest.setAttribute(PROPS_ATTRIBUTE, _getProps(renderRequest));

		return "/design_library/add_fragment_entry.jsp";
	}

	private JSONObject _getProps(RenderRequest renderRequest) {
		ThemeDisplay themeDisplay = (ThemeDisplay)renderRequest.getAttribute(
			WebKeys.THEME_DISPLAY);

		Group group = themeDisplay.getScopeGroup();

		String backURL = ParamUtil.getString(renderRequest, "backURL");
		int fragmentType = ParamUtil.getInteger(renderRequest, "fragmentType");
		String mode = ParamUtil.getString(renderRequest, "mode");

		LiferayPortletURL addFragmentCollectionPortletURL =
			(LiferayPortletURL)PortalUtil.getControlPanelPortletURL(
				PortalUtil.getHttpServletRequest(renderRequest), group,
				FragmentPortletKeys.FRAGMENT, 0, 0,
				PortletRequest.RESOURCE_PHASE);

		addFragmentCollectionPortletURL.setResourceID(
			"/fragment/add_fragment_collection");

		return JSONUtil.put(
			"addFragmentCollectionURL",
			addFragmentCollectionPortletURL.toString()
		).put(
			"addFragmentEntryURL",
			PortletURLBuilder.create(
				PortalUtil.getControlPanelPortletURL(
					PortalUtil.getHttpServletRequest(renderRequest), group,
					FragmentPortletKeys.FRAGMENT, 0, 0,
					PortletRequest.ACTION_PHASE)
			).setActionName(
				"/fragment/add_fragment_entry"
			).setRedirect(
				backURL
			).setParameter(
				"type", fragmentType
			).buildString()
		).put(
			"backURL", backURL
		).put(
			"fragmentCollections", _getFragmentCollectionsJSONArray(group)
		).put(
			"fragmentType", fragmentType
		).put(
			"mode", mode
		).put(
			"namespace",
			PortalUtil.getPortletNamespace(FragmentPortletKeys.FRAGMENT)
		);
	}

	private JSONArray _getFragmentCollectionsJSONArray(Group depotGroup) {
		JSONArray jsonArray = _jsonFactory.createJSONArray();

		for (FragmentCollection fragmentCollection :
				_fragmentCollectionLocalService.getFragmentCollections(
					depotGroup.getGroupId(), QueryUtil.ALL_POS,
					QueryUtil.ALL_POS)) {

			jsonArray.put(
				JSONUtil.put(
					"fragmentCollectionId",
					fragmentCollection.getFragmentCollectionId()
				).put(
					"name", fragmentCollection.getName()
				));
		}

		return jsonArray;
	}

	@Reference
	private FragmentCollectionLocalService _fragmentCollectionLocalService;

	@Reference
	private JSONFactory _jsonFactory;

}