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

package com.liferay.templates.web.internal.display.context;

import com.liferay.dynamic.data.mapping.model.DDMTemplate;
import com.liferay.dynamic.data.mapping.service.DDMTemplateServiceUtil;
import com.liferay.frontend.taglib.clay.servlet.taglib.util.DropdownItem;
import com.liferay.frontend.taglib.clay.servlet.taglib.util.NavigationItem;
import com.liferay.frontend.taglib.clay.servlet.taglib.util.NavigationItemListBuilder;
import com.liferay.petra.portlet.url.builder.PortletURLBuilder;
import com.liferay.portal.kernel.dao.search.SearchContainer;
import com.liferay.portal.kernel.language.LanguageUtil;
import com.liferay.portal.kernel.portlet.LiferayPortletRequest;
import com.liferay.portal.kernel.portlet.LiferayPortletResponse;
import com.liferay.portal.kernel.util.ParamUtil;
import com.liferay.portal.kernel.util.PortalUtil;
import com.liferay.templates.web.internal.constants.TemplatesWebKeys;
import com.liferay.templates.web.internal.util.TemplateActionDropdownItemsProvider;

import java.util.List;
import java.util.Objects;

import javax.portlet.PortletURL;

import javax.servlet.http.HttpServletRequest;

/**
 * @author Lourdes Fernández Besada
 */
public class TemplatesDisplayContext {

	public TemplatesDisplayContext(
		LiferayPortletRequest liferayPortletRequest,
		LiferayPortletResponse liferayPortletResponse) {

		_liferayPortletRequest = liferayPortletRequest;
		_liferayPortletResponse = liferayPortletResponse;

		_httpServletRequest = PortalUtil.getHttpServletRequest(
			_liferayPortletRequest);
	}

	public List<DropdownItem> getDDMTemplateActionDropdownItems(
			DDMTemplate ddmTemplate)
		throws Exception {

		TemplateActionDropdownItemsProvider ddmTemplateActionDropdownItems =
			new TemplateActionDropdownItemsProvider(
				ddmTemplate, _httpServletRequest, _liferayPortletResponse);

		return ddmTemplateActionDropdownItems.getActionDropdownItems();
	}

	public String getEditURL(DDMTemplate ddmTemplate) {
		return PortletURLBuilder.createRenderURL(
			_liferayPortletResponse
		).setMVCPath(
			"/edit_template.jsp"
		).setParameter(
			"classNameId", ddmTemplate.getClassNameId()
		).setParameter(
			"classPK", ddmTemplate.getClassPK()
		).setParameter(
			"groupId", ddmTemplate.getGroupId()
		).setParameter(
			"templateId", ddmTemplate.getTemplateId()
		).setParameter(
			"type", ddmTemplate.getType()
		).buildString();
	}

	public List<NavigationItem> getNavigationItems() {
		return NavigationItemListBuilder.add(
			navigationItem -> {
				navigationItem.setActive(
					Objects.equals(
						getTabs1(), TemplatesWebKeys.INFORMATION_TEMPLATES));
				navigationItem.setHref(
					_liferayPortletResponse.createRenderURL(), "tabs1",
					TemplatesWebKeys.INFORMATION_TEMPLATES);
				navigationItem.setLabel(
					LanguageUtil.get(
						_httpServletRequest,
						TemplatesWebKeys.INFORMATION_TEMPLATES));
			}
		).add(
			navigationItem -> {
				navigationItem.setActive(
					Objects.equals(
						getTabs1(), TemplatesWebKeys.WIDGET_TEMPLATES));
				navigationItem.setHref(
					_liferayPortletResponse.createRenderURL(), "tabs1",
					TemplatesWebKeys.WIDGET_TEMPLATES);
				navigationItem.setLabel(
					LanguageUtil.get(
						_httpServletRequest,
						TemplatesWebKeys.WIDGET_TEMPLATES));
			}
		).build();
	}

	public String getTabs1() {
		if (_tabs1 != null) {
			return _tabs1;
		}

		_tabs1 = ParamUtil.getString(
			_liferayPortletRequest, "tabs1",
			TemplatesWebKeys.INFORMATION_TEMPLATES);

		return _tabs1;
	}

	public SearchContainer<DDMTemplate> getTemplatesSearchContainer() {
		if (_ddmTemplatesSearchContainer != null) {
			return _ddmTemplatesSearchContainer;
		}

		SearchContainer<DDMTemplate> ddmTemplatesSearchContainer =
			new SearchContainer(
				_liferayPortletRequest, _getPortletURL(), null,
				"there-are-no-templates");

		ddmTemplatesSearchContainer.setResults(Collections.emptyList());
		ddmTemplatesSearchContainer.setTotal(0);

		_ddmTemplatesSearchContainer = ddmTemplatesSearchContainer;

		return _ddmTemplatesSearchContainer;
	}

	private PortletURL _getPortletURL() {
		return PortletURLBuilder.createRenderURL(
			_liferayPortletResponse
		).setTabs1(
			getTabs1()
		).build();
	}

	private SearchContainer<DDMTemplate> _ddmTemplatesSearchContainer;
	private final HttpServletRequest _httpServletRequest;
	private final LiferayPortletRequest _liferayPortletRequest;
	private final LiferayPortletResponse _liferayPortletResponse;
	private String _tabs1;

}