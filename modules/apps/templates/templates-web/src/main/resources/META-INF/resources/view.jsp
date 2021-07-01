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
if (ddmDisplay.getDescription(locale) != null) {
	portletDisplay.setDescription(ddmDisplay.getDescription(locale));
}

if (ddmDisplay.getTitle(locale) != null) {
	renderResponse.setTitle(ddmDisplay.getTitle(locale));
}

// List<DDMDisplayTabItem> ddmDisplayTabItems = ddmDisplay.getTabItems();

%>

<liferay-ui:success key='<%= TemplatesPortletKeys.TEMPLATES_PORTLET + "requestProcessed" %>' message="your-request-completed-successfully" />

<liferay-ui:error exception="<%= RequiredTemplateException.MustNotDeleteTemplateReferencedByTemplateLinks.class %>" message="the-template-cannot-be-deleted-because-it-is-required-by-one-or-more-template-links" />

<liferay-util:include page="/navigation_bar.jsp" servletContext="<%= application %>" />

<c:if test="<%= templatesDisplayContext.isSelectedWidgetTemplatesTab() %>">
	<liferay-util:include page="/view_widget_templates.jsp" servletContext="<%= application %>" />
</c:if>

<c:if test="<%= templatesDisplayContext.isSelectedInfoTemplatesTab() %>">
	<liferay-util:include page="/view_info_templates.jsp" servletContext="<%= application %>" />
</c:if>