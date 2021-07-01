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

package com.liferay.templates.web.internal.util;

import com.liferay.dynamic.data.mapping.constants.DDMTemplateConstants;
import com.liferay.dynamic.data.mapping.model.DDMStructure;
import com.liferay.dynamic.data.mapping.model.DDMTemplate;
import com.liferay.dynamic.data.mapping.util.BaseDDMDisplay;
import com.liferay.dynamic.data.mapping.util.DDMDisplay;
import com.liferay.dynamic.data.mapping.util.DDMDisplayTabItem;
import com.liferay.petra.portlet.url.builder.PortletURLBuilder;
import com.liferay.petra.string.StringPool;
import com.liferay.portal.kernel.language.LanguageUtil;
import com.liferay.portal.kernel.portlet.LiferayPortletRequest;
import com.liferay.portal.kernel.portlet.LiferayPortletResponse;
import com.liferay.portal.kernel.portlet.LiferayWindowState;
import com.liferay.portal.kernel.template.TemplateHandler;
import com.liferay.portal.kernel.template.TemplateHandlerRegistryUtil;
import com.liferay.portal.kernel.theme.ThemeDisplay;
import com.liferay.portal.kernel.util.ParamUtil;
import com.liferay.portal.kernel.util.Portal;
import com.liferay.portal.kernel.util.SetUtil;
import com.liferay.portlet.display.template.PortletDisplayTemplate;
import com.liferay.templates.web.internal.constants.TemplatesPortletKeys;
import com.liferay.templates.web.internal.tab.item.InfoTemplatesTabItem;
import com.liferay.templates.web.internal.tab.item.WidgetTemplatesTabItem;

import java.util.Arrays;
import java.util.List;
import java.util.Locale;
import java.util.Set;

import javax.portlet.PortletRequest;
import javax.portlet.PortletURL;

import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

/**
 * @author Eduardo García
 * @author Lourdes Fernández Besada
 */
@Component(
	property = "javax.portlet.name=" + TemplatesPortletKeys.TEMPLATES_PORTLET,
	service = DDMDisplay.class
)
public class TemplatesPortletDDMDisplay extends BaseDDMDisplay {

	@Override
	public DDMDisplayTabItem getDefaultTabItem() {
		return _infoTemplatesTabItem;
	}

	@Override
	public String getEditTemplateBackURL(
			LiferayPortletRequest liferayPortletRequest,
			LiferayPortletResponse liferayPortletResponse, long classNameId,
			long classPK, long resourceClassNameId, String portletResource)
		throws Exception {

		return getViewTemplatesURL(
			liferayPortletRequest, liferayPortletResponse, classNameId, classPK,
			resourceClassNameId);
	}

	@Override
	public String getPortletId() {
		return TemplatesPortletKeys.TEMPLATES_PORTLET;
	}

	@Override
	public List<DDMDisplayTabItem> getTabItems() {
		return Arrays.asList(_infoTemplatesTabItem, _widgetTemplatesTabItem);
	}

	@Override
	public long[] getTemplateClassPKs(
			long companyId, long classNameId, long classPK)
		throws Exception {

		return null;
	}

	@Override
	public long[] getTemplateGroupIds(
			ThemeDisplay themeDisplay, boolean includeAncestorTemplates)
		throws Exception {

		if (includeAncestorTemplates) {
			return _portal.getCurrentAndAncestorSiteGroupIds(
				themeDisplay.getScopeGroupId());
		}

		return new long[] {
			portletDisplayTemplate.getDDMTemplateGroupId(
				themeDisplay.getScopeGroupId())
		};
	}

	@Override
	public String getTemplateType() {
		return DDMTemplateConstants.TEMPLATE_TYPE_DISPLAY;
	}

	@Override
	public String getTemplateType(DDMTemplate template, Locale locale) {
		String type = template.getType();

		if (!type.equals(DDMTemplateConstants.TEMPLATE_TYPE_DISPLAY)) {
			return type;
		}

		TemplateHandler templateHandler =
			TemplateHandlerRegistryUtil.getTemplateHandler(
				template.getClassNameId());

		return templateHandler.getName(locale);
	}

	@Override
	public String getTitle(Locale locale) {
		return LanguageUtil.get(locale, "templates");
	}

	@Override
	public String getViewTemplatesBackURL(
			LiferayPortletRequest liferayPortletRequest,
			LiferayPortletResponse liferayPortletResponse, long classPK)
		throws Exception {

		return StringPool.BLANK;
	}

	@Override
	public Set<String> getViewTemplatesExcludedColumnNames() {
		return _viewTemplateExcludedColumnNames;
	}

	@Override
	public String getViewTemplatesTitle(
		DDMStructure structure, boolean controlPanel, boolean search,
		Locale locale) {

		if (search) {
			return LanguageUtil.get(locale, "templates");
		}

		if (controlPanel) {
			return StringPool.BLANK;
		}

		return super.getViewTemplatesTitle(
			structure, controlPanel, search, locale);
	}

	@Override
	public boolean isShowBackURLInTitleBar() {
		return true;
	}

	@Override
	protected String getDefaultEditTemplateTitle(Locale locale) {
		return LanguageUtil.get(locale, "new-widget-template");
	}

	@Override
	protected String getDefaultViewTemplateTitle(Locale locale) {
		return LanguageUtil.get(locale, "widget-templates");
	}

	@Override
	protected String getViewTemplatesURL(
			LiferayPortletRequest liferayPortletRequest,
			LiferayPortletResponse liferayPortletResponse, long classNameId,
			long classPK, long resourceClassNameId)
		throws Exception {

		String portletName = liferayPortletRequest.getPortletName();

		PortletURL portletURL = null;

		if (portletName.equals(TemplatesPortletKeys.TEMPLATES_PORTLET)) {
			portletURL = _portal.getControlPanelPortletURL(
				liferayPortletRequest, TemplatesPortletKeys.TEMPLATES_PORTLET,
				PortletRequest.RENDER_PHASE);
		}
		else {
			long groupId = _portal.getScopeGroupId(liferayPortletRequest);

			portletURL = PortletURLBuilder.createRenderURL(
				liferayPortletResponse
			).setMVCPath(
				"/view.jsp"
			).setParameter(
				"classNameId", classNameId
			).setParameter(
				"classPK", classPK
			).setParameter(
				"groupId", groupId
			).setWindowState(
				LiferayWindowState.POP_UP
			).build();
		}

		return PortletURLBuilder.create(
			portletURL
		).setParameter(
			TemplatesPortletKeys.CURRENT_TAB_ID_PARAMETER_NAME,
			ParamUtil.getString(
				liferayPortletRequest,
				TemplatesPortletKeys.CURRENT_TAB_ID_PARAMETER_NAME,
				TemplatesPortletKeys.INFO_TEMPLATE_TAB_ID)
		).buildString();
	}

	@Reference(unbind = "-")
	protected void setPortletDisplayTemplate(
		PortletDisplayTemplate portletDisplayTemplate) {

		this.portletDisplayTemplate = portletDisplayTemplate;
	}

	protected PortletDisplayTemplate portletDisplayTemplate;

	private static final Set<String> _viewTemplateExcludedColumnNames =
		SetUtil.fromArray(new String[] {"language", "mode", "structure"});

	@Reference
	private InfoTemplatesTabItem _infoTemplatesTabItem;

	@Reference
	private Portal _portal;

	@Reference
	private WidgetTemplatesTabItem _widgetTemplatesTabItem;

}