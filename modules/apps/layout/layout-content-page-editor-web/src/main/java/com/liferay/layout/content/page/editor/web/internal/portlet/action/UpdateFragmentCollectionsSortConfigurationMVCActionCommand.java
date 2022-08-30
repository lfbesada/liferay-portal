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

package com.liferay.layout.content.page.editor.web.internal.portlet.action;

import com.liferay.layout.content.page.editor.constants.ContentPageEditorPortletKeys;
import com.liferay.portal.kernel.json.JSONArray;
import com.liferay.portal.kernel.json.JSONException;
import com.liferay.portal.kernel.json.JSONFactoryUtil;
import com.liferay.portal.kernel.json.JSONObject;
import com.liferay.portal.kernel.json.JSONUtil;
import com.liferay.portal.kernel.language.Language;
import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.portal.kernel.portlet.JSONPortletResponseUtil;
import com.liferay.portal.kernel.portlet.PortalPreferences;
import com.liferay.portal.kernel.portlet.PortletPreferencesFactory;
import com.liferay.portal.kernel.portlet.bridges.mvc.BaseMVCActionCommand;
import com.liferay.portal.kernel.portlet.bridges.mvc.MVCActionCommand;
import com.liferay.portal.kernel.util.ListUtil;
import com.liferay.portal.kernel.util.ParamUtil;
import com.liferay.portal.kernel.util.Portal;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import javax.portlet.ActionRequest;
import javax.portlet.ActionResponse;

import javax.servlet.http.HttpServletRequest;

import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

/**
 * @author Lourdes Fernández Besada
 */
@Component(
	immediate = true,
	property = {
		"javax.portlet.name=" + ContentPageEditorPortletKeys.CONTENT_PAGE_EDITOR_PORTLET,
		"mvc.command.name=/layout_content_page_editor/update_fragment_collections_sort_configuration"
	},
	service = MVCActionCommand.class
)
public class UpdateFragmentCollectionsSortConfigurationMVCActionCommand
	extends BaseMVCActionCommand {

	@Override
	protected void doProcessAction(
			ActionRequest actionRequest, ActionResponse actionResponse)
		throws Exception {

		JSONPortletResponseUtil.writeJSON(
			actionRequest, actionResponse,
			_updateFragmentCollectionsSortConfiguration(actionRequest));
	}

	private String[] _mergeFragmentCollectionKeys(
		List<String> newFragmentCollectionKeys,
		List<String> oldFragmentCollectionKeys) {

		if (ListUtil.isEmpty(oldFragmentCollectionKeys)) {
			return newFragmentCollectionKeys.toArray(new String[0]);
		}

		List<String> oldVisibleFragmentCollectionKeys = new ArrayList<>();

		for (String fragmentCollectionKey : oldFragmentCollectionKeys) {
			if (newFragmentCollectionKeys.contains(fragmentCollectionKey)) {
				oldVisibleFragmentCollectionKeys.add(fragmentCollectionKey);
			}
		}

		Map<String, String> swapCollectionKeysMap = new LinkedHashMap<>();

		for (int i = 0;
			 (i < newFragmentCollectionKeys.size()) &&
			 (i < oldVisibleFragmentCollectionKeys.size()); i++) {

			String fragmentCollectionKey = newFragmentCollectionKeys.get(i);
			String samePosOldVisibleFragmentCollectionKey =
				oldVisibleFragmentCollectionKeys.get(i);

			if (Objects.equals(
					fragmentCollectionKey,
					samePosOldVisibleFragmentCollectionKey)) {

				continue;
			}

			swapCollectionKeysMap.put(
				samePosOldVisibleFragmentCollectionKey, fragmentCollectionKey);
		}

		List<String> fragmentCollectionKeys = new LinkedList<>();

		for (String fragmentCollectionKey : oldFragmentCollectionKeys) {
			String swapFragmentCollectionKey = swapCollectionKeysMap.remove(
				fragmentCollectionKey);

			if (swapFragmentCollectionKey == null) {
				fragmentCollectionKeys.add(fragmentCollectionKey);

				continue;
			}

			fragmentCollectionKeys.add(swapFragmentCollectionKey);
		}

		if (newFragmentCollectionKeys.size() >
				oldVisibleFragmentCollectionKeys.size()) {

			fragmentCollectionKeys.addAll(
				ListUtil.subList(
					newFragmentCollectionKeys,
					oldVisibleFragmentCollectionKeys.size() - 1,
					newFragmentCollectionKeys.size() - 1));
		}

		return fragmentCollectionKeys.toArray(new String[0]);
	}

	private JSONObject _updateFragmentCollectionsSortConfiguration(
		ActionRequest actionRequest) {

		String fragmentCollections = ParamUtil.getString(
			actionRequest, "fragmentCollections");

		HttpServletRequest httpServletRequest = _portal.getHttpServletRequest(
			actionRequest);

		JSONArray fragmentCollectionsJSONArray;

		try {
			fragmentCollectionsJSONArray = JSONFactoryUtil.createJSONArray(
				fragmentCollections);
		}
		catch (JSONException jsonException) {
			if (_log.isDebugEnabled()) {
				_log.debug(jsonException);
			}

			hideDefaultSuccessMessage(actionRequest);

			return JSONUtil.put(
				"error",
				_language.get(
					httpServletRequest, "an-unexpected-error-occurred"));
		}

		List<String> sortedFragmentCollectionKeys = JSONUtil.toStringList(
			fragmentCollectionsJSONArray);

		PortalPreferences portalPreferences =
			_portletPreferencesFactory.getPortalPreferences(httpServletRequest);

		List<String> oldSortedFragmentCollectionKeys = ListUtil.fromArray(
			portalPreferences.getValues(
				ContentPageEditorPortletKeys.CONTENT_PAGE_EDITOR_PORTLET,
				"sortedFragmentCollectionKeys", new String[0]));

		portalPreferences.setValues(
			ContentPageEditorPortletKeys.CONTENT_PAGE_EDITOR_PORTLET,
			"sortedFragmentCollectionKeys",
			_mergeFragmentCollectionKeys(
				sortedFragmentCollectionKeys, oldSortedFragmentCollectionKeys));

		return JSONUtil.put(
			"fragmentCollections", JSONFactoryUtil.createJSONArray());
	}

	private static final Log _log = LogFactoryUtil.getLog(
		UpdateFragmentCollectionsSortConfigurationMVCActionCommand.class);

	@Reference
	private Language _language;

	@Reference
	private Portal _portal;

	@Reference
	private PortletPreferencesFactory _portletPreferencesFactory;

}