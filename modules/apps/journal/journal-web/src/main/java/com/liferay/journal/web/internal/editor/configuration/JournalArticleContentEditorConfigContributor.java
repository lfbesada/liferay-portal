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

package com.liferay.journal.web.internal.editor.configuration;

import com.liferay.journal.constants.JournalPortletKeys;
import com.liferay.portal.kernel.editor.configuration.BaseEditorConfigContributor;
import com.liferay.portal.kernel.editor.configuration.EditorConfigContributor;
import com.liferay.portal.kernel.feature.flag.FeatureFlagManagerUtil;
import com.liferay.portal.kernel.json.JSONArray;
import com.liferay.portal.kernel.json.JSONObject;
import com.liferay.portal.kernel.json.JSONUtil;
import com.liferay.portal.kernel.portlet.RequestBackedPortletURLFactory;
import com.liferay.portal.kernel.portlet.url.builder.PortletURLBuilder;
import com.liferay.portal.kernel.theme.PortletDisplay;
import com.liferay.portal.kernel.theme.ThemeDisplay;
import com.liferay.portal.kernel.util.HtmlUtil;
import com.liferay.portal.kernel.util.Portal;
import com.liferay.portal.kernel.util.Validator;

import java.util.Map;

import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

/**
 * @author Fortunato Maldonado
 */
@Component(
	property = {
		"editor.config.key=rich_text",
		"javax.portlet.name=" + JournalPortletKeys.JOURNAL
	},
	service = EditorConfigContributor.class
)
public class JournalArticleContentEditorConfigContributor
	extends BaseEditorConfigContributor {

	@Override
	public void populateConfigJSONObject(
		JSONObject jsonObject, Map<String, Object> inputEditorTaglibAttributes,
		ThemeDisplay themeDisplay,
		RequestBackedPortletURLFactory requestBackedPortletURLFactory) {

		jsonObject.put(
			"bodyClass", jsonObject.getString("bodyClass") + " h-100");

		JSONArray contentsCSSJSONArray = jsonObject.getJSONArray("contentsCss");

		contentsCSSJSONArray.put(
			HtmlUtil.escape(
				_portal.getStaticResourceURL(
					themeDisplay.getRequest(),
					_portal.getPathContext() +
						"/o/journal-web/css/ckeditor.css")));

		jsonObject.put(
			"contentsCss", contentsCSSJSONArray
		).put(
			"resize_enabled", true
		).put(
			"toolbar", _getToolbarJSONArray()
		);

		if (FeatureFlagManagerUtil.isEnabled("LPS-179483")) {
			String extraPlugins = jsonObject.getString("extraPlugins");

			if (Validator.isNotNull(extraPlugins)) {
				extraPlugins = extraPlugins + ",aicreator";
			}
			else {
				extraPlugins = "aicreator";
			}

			jsonObject.put("extraPlugins", extraPlugins);
		}

		String removePlugins = jsonObject.getString("removePlugins");

		if (Validator.isNotNull(removePlugins)) {
			removePlugins = removePlugins + ",autogrow";
		}
		else {
			removePlugins = "autogrow";
		}

		jsonObject.put("removePlugins", removePlugins);

		PortletDisplay portletDisplay = themeDisplay.getPortletDisplay();

		if (Validator.isNotNull(portletDisplay.getId())) {
			jsonObject.put(
				"uploadUrl",
				PortletURLBuilder.create(
					requestBackedPortletURLFactory.createActionURL(
						JournalPortletKeys.JOURNAL)
				).setActionName(
					"/journal/upload_image"
				).buildString());
		}
	}

	private JSONArray _getToolbarJSONArray() {
		JSONArray toolbarJSONArray = JSONUtil.putAll(
			JSONUtil.putAll("Undo", "Redo"), JSONUtil.putAll("Styles"),
			JSONUtil.putAll("Bold", "Italic", "Underline"),
			JSONUtil.putAll("NumberedList", "BulletedList"),
			JSONUtil.putAll("Link", "Unlink"),
			JSONUtil.putAll("Table", "ImageSelector", "VideoSelector"),
			JSONUtil.putAll("Source"));

		if (FeatureFlagManagerUtil.isEnabled("LPS-179483")) {
			toolbarJSONArray.put(JSONUtil.putAll("AICreator"));
		}

		return toolbarJSONArray;
	}

	@Reference
	private Portal _portal;

}