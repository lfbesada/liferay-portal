<%--
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
--%>

<%@ include file="/init.jsp" %>

<%
boolean includeCheckBox = ParamUtil.getBoolean(request, "includeCheckBox", true);
%>

<portlet:actionURL name="/templates/delete_template" var="deleteTemplatesURL">
	<portlet:param name="mvcPath" value="/view_widget_templates.jsp" />
</portlet:actionURL>

<clay:management-toolbar
	actionDropdownItems='<%= templatesDisplayContext.getActionItemsDropdownItems("deleteTemplates") %>'
	additionalProps='<%=
		HashMapBuilder.<String, Object>put(
			"deleteTemplatesURL", deleteTemplatesURL.toString()
		).build()
	%>'
	clearResultsURL="<%= templatesDisplayContext.getClearResultsURL() %>"
	creationMenu="<%= templatesDisplayContext.getTemplateCreationMenu() %>"
	disabled="<%= templatesDisplayContext.isDisabledManagementBar(TemplatesPortletKeys.TEMPLATES_WIDGET_TEMPLATE) %>"
	filterDropdownItems="<%= templatesDisplayContext.getFilterItemsDropdownItems() %>"
	itemsTotal="<%= templatesDisplayContext.getTotalItems(TemplatesPortletKeys.TEMPLATES_WIDGET_TEMPLATE) %>"
	propsTransformer="js/DDMTemplateManagementToolbarPropsTransformer"
	searchActionURL="<%= templatesDisplayContext.getTemplateSearchActionURL() %>"
	searchContainerId="<%= templatesDisplayContext.getTemplateSearchContainerId() %>"
	searchFormName="fm1"
	selectable="<%= includeCheckBox && !user.isDefaultUser() %>"
	sortingOrder="<%= templatesDisplayContext.getOrderByType() %>"
	sortingURL="<%= templatesDisplayContext.getSortingURL() %>"
/>