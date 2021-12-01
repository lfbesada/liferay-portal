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

package com.liferay.site.navigation.menu.item.display.page.internal.portlet.action;

import com.liferay.info.item.InfoItemReference;
import com.liferay.info.item.InfoItemReferenceMetadata;
import com.liferay.layout.display.page.LayoutDisplayPageMultiSelectionProvider;
import com.liferay.layout.display.page.LayoutDisplayPageMultiSelectionProviderTracker;
import com.liferay.petra.string.StringBundler;
import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.json.JSONFactoryUtil;
import com.liferay.portal.kernel.json.JSONObject;
import com.liferay.portal.kernel.json.JSONUtil;
import com.liferay.portal.kernel.language.LanguageUtil;
import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.portal.kernel.portlet.JSONPortletResponseUtil;
import com.liferay.portal.kernel.portlet.bridges.mvc.BaseMVCActionCommand;
import com.liferay.portal.kernel.portlet.bridges.mvc.MVCActionCommand;
import com.liferay.portal.kernel.service.ServiceContext;
import com.liferay.portal.kernel.service.ServiceContextFactory;
import com.liferay.portal.kernel.theme.ThemeDisplay;
import com.liferay.portal.kernel.util.ParamUtil;
import com.liferay.portal.kernel.util.Portal;
import com.liferay.portal.kernel.util.UnicodeProperties;
import com.liferay.portal.kernel.util.Validator;
import com.liferay.portal.kernel.util.WebKeys;
import com.liferay.site.navigation.admin.constants.SiteNavigationAdminPortletKeys;
import com.liferay.site.navigation.model.SiteNavigationMenuItem;
import com.liferay.site.navigation.service.SiteNavigationMenuItemService;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import javax.portlet.ActionRequest;
import javax.portlet.ActionResponse;

import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

/**
 * @author Lourdes Fernández Besada
 */
@Component(
	immediate = true,
	property = {
		"javax.portlet.name=" + SiteNavigationAdminPortletKeys.SITE_NAVIGATION_ADMIN,
		"mvc.command.name=/navigation_menu/add_multiple_display_page_type_site_navigation_menu_item"
	},
	service = MVCActionCommand.class
)
public class AddMultipleDisplayPageTypeSiteNavigationMenuItemMVCActionCommand
	extends BaseMVCActionCommand {

	@Override
	protected void doProcessAction(
			ActionRequest actionRequest, ActionResponse actionResponse)
		throws Exception {

		JSONObject jsonObject = JSONFactoryUtil.createJSONObject();

		String siteNavigationMenuItemType = ParamUtil.getString(
			actionRequest, "siteNavigationMenuItemType");
		long siteNavigationMenuId = ParamUtil.getLong(
			actionRequest, "siteNavigationMenuId");

		if (Validator.isNotNull(siteNavigationMenuItemType) &&
			(siteNavigationMenuId > 0)) {

			ServiceContext serviceContext = ServiceContextFactory.getInstance(
				actionRequest);

			ThemeDisplay themeDisplay =
				(ThemeDisplay)actionRequest.getAttribute(WebKeys.THEME_DISPLAY);

			try {
				List<InfoItemReferenceMetadata> infoItemReferenceMetadatas =
					JSONUtil.toList(
						JSONFactoryUtil.createJSONArray(
							ParamUtil.getString(actionRequest, "items")),
						itemJSONObject -> {
							if (!Objects.equals(
									itemJSONObject.getString("className"),
									siteNavigationMenuItemType)) {

								return null;
							}

							InfoItemReference infoItemReference =
								new InfoItemReference(
									itemJSONObject.getString("className"),
									itemJSONObject.getLong("classPK"));

							Set<String> keys = itemJSONObject.keySet();

							Stream<String> stream = keys.stream();

							Map<String, Object> data = stream.collect(
								Collectors.toMap(
									Function.identity(),
									key -> itemJSONObject.get(key)));

							return new InfoItemReferenceMetadata(
								data, infoItemReference);
						});

				LayoutDisplayPageMultiSelectionProvider<?>
					layoutDisplayPageMultiSelectionProvider =
						_layoutDisplayPageMultiSelectionProviderTracker.
							getLayoutDisplayPageMultiSelectionProvider(
								siteNavigationMenuItemType);

				if (layoutDisplayPageMultiSelectionProvider != null) {
					infoItemReferenceMetadatas =
						layoutDisplayPageMultiSelectionProvider.process(
							infoItemReferenceMetadatas);
				}

				for (InfoItemReferenceMetadata infoItemReferenceMetadata :
						infoItemReferenceMetadatas) {

					_addSiteNavigationMenuItem(
						themeDisplay.getScopeGroupId(),
						infoItemReferenceMetadata, 0, serviceContext,
						siteNavigationMenuId, siteNavigationMenuItemType);
				}
			}
			catch (PortalException portalException) {
				if (_log.isDebugEnabled()) {
					_log.debug(portalException, portalException);
				}

				jsonObject.put(
					"errorMessage",
					LanguageUtil.get(
						_portal.getHttpServletRequest(actionRequest),
						"an-unexpected-error-occurred"));
			}
		}
		else {
			if (_log.isDebugEnabled()) {
				_log.debug(
					StringBundler.concat(
						"Unable to add multiple SiteNavigationMenuItem for ",
						"siteNavigationMenuId ", siteNavigationMenuId,
						" and type ", siteNavigationMenuItemType));
			}

			jsonObject.put(
				"errorMessage",
				LanguageUtil.get(
					_portal.getHttpServletRequest(actionRequest),
					"an-unexpected-error-occurred"));
		}

		JSONPortletResponseUtil.writeJSON(
			actionRequest, actionResponse, jsonObject);
	}

	private void _addSiteNavigationMenuItem(
			long groupId, InfoItemReferenceMetadata infoItemReferenceMetadata,
			long parentSiteNavigationMenuItemId, ServiceContext serviceContext,
			long siteNavigationMenuId, String siteNavigationMenuItemType)
		throws PortalException {

		UnicodeProperties typeSettingsUnicodeProperties = new UnicodeProperties(
			true);

		InfoItemReference infoItemReference =
			infoItemReferenceMetadata.getInfoItemReference();

		typeSettingsUnicodeProperties.setProperty(
			"className", infoItemReference.getClassName());

		Map<String, Object> data = infoItemReferenceMetadata.getData();

		typeSettingsUnicodeProperties.setProperty(
			"classNameId", String.valueOf(data.get("classNameId")));
		typeSettingsUnicodeProperties.setProperty(
			"classPK", String.valueOf(data.get("classPK")));
		typeSettingsUnicodeProperties.setProperty(
			"classTypeId", String.valueOf(data.get("classTypeId")));
		typeSettingsUnicodeProperties.setProperty(
			"type", String.valueOf(data.get("type")));
		typeSettingsUnicodeProperties.setProperty(
			"title", String.valueOf(data.get("title")));

		SiteNavigationMenuItem siteNavigationMenuItem =
			_siteNavigationMenuItemService.addSiteNavigationMenuItem(
				groupId, siteNavigationMenuId, parentSiteNavigationMenuItemId,
				siteNavigationMenuItemType,
				typeSettingsUnicodeProperties.toString(), serviceContext);

		if (!data.containsKey("children") ||
			!(data.get("children") instanceof List)) {

			return;
		}

		List<InfoItemReferenceMetadata> children =
			(List<InfoItemReferenceMetadata>)data.get("children");

		for (InfoItemReferenceMetadata child : children) {
			_addSiteNavigationMenuItem(
				groupId, child,
				siteNavigationMenuItem.getSiteNavigationMenuItemId(),
				serviceContext, siteNavigationMenuId,
				siteNavigationMenuItemType);
		}
	}

	private static final Log _log = LogFactoryUtil.getLog(
		AddMultipleDisplayPageTypeSiteNavigationMenuItemMVCActionCommand.class);

	@Reference
	private LayoutDisplayPageMultiSelectionProviderTracker
		_layoutDisplayPageMultiSelectionProviderTracker;

	@Reference
	private Portal _portal;

	@Reference
	private SiteNavigationMenuItemService _siteNavigationMenuItemService;

}