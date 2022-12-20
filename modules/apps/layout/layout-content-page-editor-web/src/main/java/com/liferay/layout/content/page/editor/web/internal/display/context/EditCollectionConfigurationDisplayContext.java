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

package com.liferay.layout.content.page.editor.web.internal.display.context;

import com.liferay.asset.kernel.model.AssetCategory;
import com.liferay.info.collection.provider.ConfigurableInfoCollectionProvider;
import com.liferay.info.collection.provider.InfoCollectionProvider;
import com.liferay.info.collection.provider.RelatedInfoItemCollectionProvider;
import com.liferay.info.item.InfoItemServiceRegistry;
import com.liferay.item.selector.ItemSelector;
import com.liferay.item.selector.criteria.InfoItemItemSelectorReturnType;
import com.liferay.item.selector.criteria.info.item.criterion.InfoItemItemSelectorCriterion;
import com.liferay.layout.content.page.editor.web.internal.util.InfoFormUtil;
import com.liferay.portal.kernel.json.JSONFactoryUtil;
import com.liferay.portal.kernel.json.JSONObject;
import com.liferay.portal.kernel.portlet.RequestBackedPortletURLFactoryUtil;
import com.liferay.portal.kernel.portlet.url.builder.PortletURLBuilder;
import com.liferay.portal.kernel.theme.ThemeDisplay;
import com.liferay.portal.kernel.util.HashMapBuilder;
import com.liferay.portal.kernel.util.ParamUtil;
import com.liferay.portal.kernel.util.Validator;
import com.liferay.portal.kernel.util.WebKeys;

import java.util.Map;

import javax.portlet.ActionURL;
import javax.portlet.RenderResponse;

import javax.servlet.http.HttpServletRequest;

/**
 * @author Lourdes Fernández Besada
 */
public class EditCollectionConfigurationDisplayContext {

	public EditCollectionConfigurationDisplayContext(
		HttpServletRequest httpServletRequest,
		InfoItemServiceRegistry infoItemServiceRegistry,
		ItemSelector itemSelector, RenderResponse renderResponse) {

		_httpServletRequest = httpServletRequest;
		_infoItemServiceRegistry = infoItemServiceRegistry;
		_itemSelector = itemSelector;
		_renderResponse = renderResponse;

		_themeDisplay = (ThemeDisplay)httpServletRequest.getAttribute(
			WebKeys.THEME_DISPLAY);
	}

	public ActionURL getActionURL() {
		return PortletURLBuilder.createActionURL(
			_renderResponse
		).setActionName(
			"/layout_content_page_editor/update_collection_configuration"
		).buildActionURL();
	}

	public String getCollectionKey() {
		if (_collectionKey != null) {
			return _collectionKey;
		}

		_collectionKey = ParamUtil.getString(
			_httpServletRequest, "collectionKey");

		return _collectionKey;
	}

	public Map<String, Object> getData() throws Exception {
		return HashMapBuilder.<String, Object>put(
			"collectionConfiguration", _getCollectionConfigurationJSONObject()
		).put(
			"collectionItemClassName", _getCollectionItemClassName()
		).put(
			"collectionKey", getCollectionKey()
		).put(
			"collectionLabel", _getCollectionLabel()
		).put(
			"selectAssetCategoryURL",
			() -> {
				InfoItemItemSelectorCriterion itemSelectorCriterion =
					new InfoItemItemSelectorCriterion();

				itemSelectorCriterion.setDesiredItemSelectorReturnTypes(
					new InfoItemItemSelectorReturnType());
				itemSelectorCriterion.setItemType(
					AssetCategory.class.getName());

				return PortletURLBuilder.create(
					_itemSelector.getItemSelectorURL(
						RequestBackedPortletURLFactoryUtil.create(
							_httpServletRequest),
						_renderResponse.getNamespace() + "selectItem",
						itemSelectorCriterion)
				).buildString();
			}
		).build();
	}

	public String getItemId() {
		if (_itemId != null) {
			return _itemId;
		}

		_itemId = ParamUtil.getString(_httpServletRequest, "itemId");

		return _itemId;
	}

	public String getRedirect() {
		if (Validator.isNotNull(_redirect)) {
			return _redirect;
		}

		_redirect = ParamUtil.getString(_httpServletRequest, "redirect");

		return _redirect;
	}

	public long getSegmentsExperienceId() {
		if (_segmentsExperienceId != null) {
			return _segmentsExperienceId;
		}

		_segmentsExperienceId = ParamUtil.getLong(
			_httpServletRequest, "segmentsExperienceId");

		return _segmentsExperienceId;
	}

	private JSONObject _getCollectionConfigurationJSONObject() {
		InfoCollectionProvider<?> infoCollectionProvider =
			_getInfoCollectionProvider();

		if (!(infoCollectionProvider instanceof
				ConfigurableInfoCollectionProvider)) {

			return JSONFactoryUtil.createJSONObject();
		}

		ConfigurableInfoCollectionProvider<?>
			configurableInfoCollectionProvider =
				(ConfigurableInfoCollectionProvider<?>)infoCollectionProvider;

		return InfoFormUtil.getConfigurationJSONObject(
			configurableInfoCollectionProvider.getConfigurationInfoForm(),
			_themeDisplay.getLocale());
	}

	private String _getCollectionItemClassName() {
		if (_collectionItemClassName != null) {
			return _collectionItemClassName;
		}

		InfoCollectionProvider<?> infoCollectionProvider =
			_getInfoCollectionProvider();

		if (infoCollectionProvider == null) {
			return null;
		}

		_collectionItemClassName =
			infoCollectionProvider.getCollectionItemClassName();

		return _collectionItemClassName;
	}

	private String _getCollectionLabel() {
		if (_collectionLabel != null) {
			return _collectionLabel;
		}

		InfoCollectionProvider<?> infoCollectionProvider =
			_getInfoCollectionProvider();

		if (infoCollectionProvider == null) {
			return null;
		}

		_collectionLabel = infoCollectionProvider.getLabel(
			_themeDisplay.getLocale());

		return _collectionLabel;
	}

	private InfoCollectionProvider _getInfoCollectionProvider() {
		String collectionKey = getCollectionKey();

		if (Validator.isBlank(collectionKey)) {
			return null;
		}

		InfoCollectionProvider<?> infoCollectionProvider =
			_infoItemServiceRegistry.getInfoItemService(
				InfoCollectionProvider.class, collectionKey);

		if (infoCollectionProvider != null) {
			return infoCollectionProvider;
		}

		return _infoItemServiceRegistry.getInfoItemService(
			RelatedInfoItemCollectionProvider.class, collectionKey);
	}

	private String _collectionItemClassName;
	private String _collectionKey;
	private String _collectionLabel;
	private final HttpServletRequest _httpServletRequest;
	private final InfoItemServiceRegistry _infoItemServiceRegistry;
	private String _itemId;
	private final ItemSelector _itemSelector;
	private String _redirect;
	private final RenderResponse _renderResponse;
	private Long _segmentsExperienceId;
	private final ThemeDisplay _themeDisplay;

}