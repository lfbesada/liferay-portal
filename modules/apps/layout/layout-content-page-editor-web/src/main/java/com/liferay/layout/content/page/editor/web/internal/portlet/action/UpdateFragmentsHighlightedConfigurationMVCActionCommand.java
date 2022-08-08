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

import com.liferay.fragment.constants.FragmentConstants;
import com.liferay.fragment.contributor.FragmentCollectionContributorTracker;
import com.liferay.fragment.model.FragmentComposition;
import com.liferay.fragment.model.FragmentEntry;
import com.liferay.fragment.renderer.FragmentRenderer;
import com.liferay.fragment.renderer.FragmentRendererTracker;
import com.liferay.fragment.service.FragmentCompositionService;
import com.liferay.fragment.service.FragmentEntryLocalService;
import com.liferay.layout.content.page.editor.constants.ContentPageEditorPortletKeys;
import com.liferay.layout.content.page.editor.web.internal.constants.ContentPageEditorConstants;
import com.liferay.layout.content.page.editor.web.internal.util.FragmentEntryLinkManager;
import com.liferay.layout.content.page.editor.web.internal.util.layout.structure.LayoutStructureUtil;
import com.liferay.layout.page.template.model.LayoutPageTemplateEntry;
import com.liferay.layout.page.template.service.LayoutPageTemplateEntryLocalService;
import com.liferay.layout.util.structure.DropZoneLayoutStructureItem;
import com.liferay.layout.util.structure.LayoutStructure;
import com.liferay.petra.string.StringPool;
import com.liferay.portal.kernel.json.JSONArray;
import com.liferay.portal.kernel.json.JSONObject;
import com.liferay.portal.kernel.json.JSONUtil;
import com.liferay.portal.kernel.language.Language;
import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.portal.kernel.model.Group;
import com.liferay.portal.kernel.model.Layout;
import com.liferay.portal.kernel.model.PortletConstants;
import com.liferay.portal.kernel.portlet.JSONPortletResponseUtil;
import com.liferay.portal.kernel.portlet.PortletIdCodec;
import com.liferay.portal.kernel.portlet.PortletPreferencesFactory;
import com.liferay.portal.kernel.portlet.bridges.mvc.BaseMVCActionCommand;
import com.liferay.portal.kernel.portlet.bridges.mvc.MVCActionCommand;
import com.liferay.portal.kernel.service.GroupLocalService;
import com.liferay.portal.kernel.theme.ThemeDisplay;
import com.liferay.portal.kernel.util.GetterUtil;
import com.liferay.portal.kernel.util.ListUtil;
import com.liferay.portal.kernel.util.ParamUtil;
import com.liferay.portal.kernel.util.Portal;
import com.liferay.portal.kernel.util.SetUtil;
import com.liferay.portal.kernel.util.Validator;
import com.liferay.portal.kernel.util.WebKeys;
import com.liferay.segments.constants.SegmentsExperienceConstants;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

import javax.portlet.ActionRequest;
import javax.portlet.ActionResponse;
import javax.portlet.PortletPreferences;
import javax.portlet.ValidatorException;

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
		"mvc.command.name=/layout_content_page_editor/update_fragments_highlighted_configuration"
	},
	service = MVCActionCommand.class
)
public class UpdateFragmentsHighlightedConfigurationMVCActionCommand
	extends BaseMVCActionCommand {

	@Override
	protected void doProcessAction(
			ActionRequest actionRequest, ActionResponse actionResponse)
		throws Exception {

		JSONPortletResponseUtil.writeJSON(
			actionRequest, actionResponse,
			_updateFragmentsHighlightedConfiguration(actionRequest));
	}

	private String _getFragmentEntryKey(ActionRequest actionRequest) {
		String fragmentEntryKey = ParamUtil.getString(
			actionRequest, "fragmentEntryKey");

		FragmentRenderer fragmentRenderer =
			_fragmentRendererTracker.getFragmentRenderer(fragmentEntryKey);

		if (fragmentRenderer != null) {
			return fragmentEntryKey;
		}

		FragmentEntry fragmentEntry =
			_fragmentCollectionContributorTracker.getFragmentEntry(
				fragmentEntryKey);

		if (fragmentEntry != null) {
			return fragmentEntryKey;
		}

		FragmentComposition fragmentComposition =
			_fragmentCollectionContributorTracker.getFragmentComposition(
				fragmentEntryKey);

		if (fragmentComposition != null) {
			return fragmentEntryKey;
		}

		long groupId = ParamUtil.getLong(actionRequest, "groupId");

		fragmentEntry = _fragmentEntryLocalService.fetchFragmentEntry(
			groupId, fragmentEntryKey);

		if (fragmentEntry != null) {
			return _getFragmentUniqueKey(fragmentEntryKey, groupId);
		}

		fragmentComposition =
			_fragmentCompositionService.fetchFragmentComposition(
				groupId, fragmentEntryKey);

		if (fragmentComposition != null) {
			return _getFragmentUniqueKey(fragmentEntryKey, groupId);
		}

		return null;
	}

	private String _getFragmentUniqueKey(
		String fragmentEntryKey, long groupId) {

		Group group = _groupLocalService.fetchGroup(groupId);

		if (group == null) {
			return fragmentEntryKey;
		}

		return fragmentEntryKey + StringPool.POUND + group.getGroupKey();
	}

	private JSONArray _getHighlightedFragmentsJSONArray(
		Set<String> highlightedFragmentEntryKeys,
		HttpServletRequest httpServletRequest, ThemeDisplay themeDisplay) {

		DropZoneLayoutStructureItem masterDropZoneLayoutStructureItem =
			_getMasterDropZoneLayoutStructureItem(themeDisplay);

		Map<String, JSONObject> map = new TreeMap<>();

		for (String key : highlightedFragmentEntryKeys) {
			int pos = key.indexOf(StringPool.POUND);

			long groupId = 0;

			if (pos > 0) {
				String groupKey = GetterUtil.getString(key.substring(pos + 1));

				Group group = _groupLocalService.fetchGroup(
					themeDisplay.getCompanyId(), groupKey);

				if (group != null) {
					groupId = group.getGroupId();
				}

				key = key.substring(0, pos);
			}

			if (!_isAllowedFragmentEntryKey(
					key, masterDropZoneLayoutStructureItem)) {

				continue;
			}

			FragmentRenderer fragmentRenderer =
				_fragmentRendererTracker.getFragmentRenderer(key);

			if (fragmentRenderer != null) {
				String label = fragmentRenderer.getLabel(
					themeDisplay.getLocale());

				map.put(
					label,
					JSONUtil.put(
						"fragmentEntryKey", fragmentRenderer.getKey()
					).put(
						"highlighted", true
					).put(
						"icon", fragmentRenderer.getIcon()
					).put(
						"imagePreviewURL",
						fragmentRenderer.getImagePreviewURL(httpServletRequest)
					).put(
						"name", label
					));

				continue;
			}

			FragmentEntry fragmentEntry =
				_fragmentEntryLinkManager.getFragmentEntry(
					groupId, key, themeDisplay.getLocale());

			if (fragmentEntry != null) {
				map.put(
					fragmentEntry.getName(),
					JSONUtil.put(
						"fragmentEntryKey", fragmentEntry.getFragmentEntryKey()
					).put(
						"groupId", fragmentEntry.getGroupId()
					).put(
						"icon", fragmentEntry.getIcon()
					).put(
						"imagePreviewURL",
						fragmentEntry.getImagePreviewURL(themeDisplay)
					).put(
						"name", fragmentEntry.getName()
					).put(
						"type",
						FragmentConstants.getTypeLabel(fragmentEntry.getType())
					));

				continue;
			}

			FragmentComposition fragmentComposition =
				_fragmentCollectionContributorTracker.getFragmentComposition(
					key);

			if (fragmentComposition == null) {
				fragmentComposition =
					_fragmentCompositionService.fetchFragmentComposition(
						groupId, key);
			}

			if (fragmentComposition == null) {
				continue;
			}

			map.put(
				fragmentEntry.getName(),
				JSONUtil.put(
					"fragmentEntryKey",
					fragmentComposition.getFragmentCompositionKey()
				).put(
					"groupId", fragmentComposition.getGroupId()
				).put(
					"icon", fragmentComposition.getIcon()
				).put(
					"imagePreviewURL",
					fragmentComposition.getImagePreviewURL(themeDisplay)
				).put(
					"name", fragmentComposition.getName()
				).put(
					"type", ContentPageEditorConstants.TYPE_COMPOSITION
				));
		}

		List<JSONObject> sortedHighlightedFragments = new ArrayList<>(
			map.values());

		return JSONUtil.putAll(
			sortedHighlightedFragments.toArray(new JSONObject[0]));
	}

	private DropZoneLayoutStructureItem _getMasterDropZoneLayoutStructureItem(
		ThemeDisplay themeDisplay) {

		Layout layout = themeDisplay.getLayout();

		if (layout.getMasterLayoutPlid() <= 0) {
			return null;
		}

		LayoutPageTemplateEntry masterLayoutPageTemplateEntry =
			_layoutPageTemplateEntryLocalService.
				fetchLayoutPageTemplateEntryByPlid(
					layout.getMasterLayoutPlid());

		if (masterLayoutPageTemplateEntry == null) {
			return null;
		}

		try {
			LayoutStructure masterLayoutStructure =
				LayoutStructureUtil.getLayoutStructure(
					masterLayoutPageTemplateEntry.getGroupId(),
					masterLayoutPageTemplateEntry.getPlid(),
					SegmentsExperienceConstants.KEY_DEFAULT);

			return (DropZoneLayoutStructureItem)
				masterLayoutStructure.getDropZoneLayoutStructureItem();
		}
		catch (Exception exception) {
			if (_log.isDebugEnabled()) {
				_log.debug("Unable to get master layout structure", exception);
			}
		}

		return null;
	}

	private boolean _isAllowedFragmentEntryKey(
		String fragmentEntryKey,
		DropZoneLayoutStructureItem masterDropZoneLayoutStructureItem) {

		if (masterDropZoneLayoutStructureItem == null) {
			return true;
		}

		List<String> fragmentEntryKeys =
			masterDropZoneLayoutStructureItem.getFragmentEntryKeys();

		if (masterDropZoneLayoutStructureItem.isAllowNewFragmentEntries()) {
			if (ListUtil.isEmpty(fragmentEntryKeys) ||
				!fragmentEntryKeys.contains(fragmentEntryKey)) {

				return true;
			}

			return false;
		}

		if (ListUtil.isNotEmpty(fragmentEntryKeys) &&
			fragmentEntryKeys.contains(fragmentEntryKey)) {

			return true;
		}

		return false;
	}

	private JSONObject _updateFragmentsHighlightedConfiguration(
			ActionRequest actionRequest)
		throws Exception {

		String fragmentEntryKey = _getFragmentEntryKey(actionRequest);

		HttpServletRequest httpServletRequest = _portal.getHttpServletRequest(
			actionRequest);

		if (Validator.isNull(fragmentEntryKey)) {
			hideDefaultSuccessMessage(actionRequest);

			return JSONUtil.put(
				"error",
				_language.get(
					httpServletRequest, "an-unexpected-error-occurred"));
		}

		boolean highlighted = ParamUtil.getBoolean(
			actionRequest, "highlighted");

		ThemeDisplay themeDisplay =
			(ThemeDisplay)httpServletRequest.getAttribute(
				WebKeys.THEME_DISPLAY);

		PortletPreferences portletPreferences =
			_portletPreferencesFactory.getPortletSetup(
				httpServletRequest,
				PortletIdCodec.encode(
					ContentPageEditorPortletKeys.CONTENT_PAGE_EDITOR_PORTLET,
					themeDisplay.getUserId()),
				PortletConstants.DEFAULT_PREFERENCES);

		Set<String> highlightedFragmentEntryKeys = SetUtil.fromArray(
			portletPreferences.getValues(
				"highlightedFragmentEntryKeys", new String[0]));

		if (highlighted) {
			highlightedFragmentEntryKeys.add(fragmentEntryKey);
		}
		else {
			highlightedFragmentEntryKeys.remove(fragmentEntryKey);
		}

		portletPreferences.setValues(
			"highlightedFragmentEntryKeys",
			highlightedFragmentEntryKeys.toArray(new String[0]));

		try {
			portletPreferences.store();
		}
		catch (ValidatorException validatorException) {
			hideDefaultSuccessMessage(actionRequest);

			return JSONUtil.put(
				"error",
				_language.get(
					httpServletRequest, "an-unexpected-error-occurred"));
		}

		return JSONUtil.put(
			"highlightedFragments",
			_getHighlightedFragmentsJSONArray(
				highlightedFragmentEntryKeys, httpServletRequest,
				themeDisplay));
	}

	private static final Log _log = LogFactoryUtil.getLog(
		UpdateFragmentsHighlightedConfigurationMVCActionCommand.class);

	@Reference
	private FragmentCollectionContributorTracker
		_fragmentCollectionContributorTracker;

	@Reference
	private FragmentCompositionService _fragmentCompositionService;

	@Reference
	private FragmentEntryLinkManager _fragmentEntryLinkManager;

	@Reference
	private FragmentEntryLocalService _fragmentEntryLocalService;

	@Reference
	private FragmentRendererTracker _fragmentRendererTracker;

	@Reference
	private GroupLocalService _groupLocalService;

	@Reference
	private Language _language;

	@Reference
	private LayoutPageTemplateEntryLocalService
		_layoutPageTemplateEntryLocalService;

	@Reference
	private Portal _portal;

	@Reference
	private PortletPreferencesFactory _portletPreferencesFactory;

}