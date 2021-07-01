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

package com.liferay.templates.web.internal.tab.item;

import com.liferay.dynamic.data.mapping.util.DDMDisplayTabItem;
import com.liferay.templates.web.internal.constants.TemplatesPortletKeys;

import org.osgi.service.component.annotations.Component;

/**
 * @author Lino Alves
 * @author Lourdes Fernández Besada
 */
@Component(
	property = "javax.portlet.name=" + TemplatesPortletKeys.TEMPLATES_PORTLET,
	service = {DDMDisplayTabItem.class, InfoTemplatesTabItem.class}
)
public class InfoTemplatesTabItem extends BaseTemplatesTabItem {

	@Override
	public String getTabId() {
		return TemplatesPortletKeys.INFO_TEMPLATE_TAB_ID;
	}

	@Override
	public String getTitleKey() {
		return TemplatesPortletKeys.INFO_TEMPLATE_TAB_ID;
	}

}