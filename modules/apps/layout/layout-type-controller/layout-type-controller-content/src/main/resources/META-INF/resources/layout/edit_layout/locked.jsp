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

<%@ include file="/layout/edit_layout/init.jsp" %>

<%
ContentLayoutLockedDisplayContext contentLayoutLockedDisplayContext = (ContentLayoutLockedDisplayContext)request.getAttribute(ContentLayoutLockedDisplayContext.class.getName());
%>

<clay:container-fluid>
	<clay:content-row
		cssClass="c-mt-5 text-info"
	>
		<clay:content-col>
			<img src="<%= contentLayoutLockedDisplayContext.getImagesPath() %>/blocked_page.png" />
		</clay:content-col>
	</clay:content-row>

	<clay:content-row
		cssClass="c-mt-3 text-info"
	>
		<clay:content-col
			expand="<%= true %>"
		>
			<h1><liferay-ui:message key="page-in-use" /></h1>
		</clay:content-col>
	</clay:content-row>

	<clay:content-row
		cssClass="c-mt-3"
	>
		<clay:content-col
			expand="<%= true %>"
		>
			<liferay-ui:message key="this-page-is-currently-being-edited-by-another-user.-if-you-need-to-take-control-over-this-page,-you-can-contact-your-admin-to-unlock-it" />
		</clay:content-col>
	</clay:content-row>

	<c:if test="<%= contentLayoutLockedDisplayContext.isShowGoBackButton() %>">
		<clay:content-row
			cssClass="c-mt-3"
		>
			<clay:content-col>
				<clay:button
					displayType="primary"
					label="go-back"
					onClick='<%= "location.href='" + HtmlUtil.escapeJS(contentLayoutLockedDisplayContext.getBackURL()) + "';" %>'
				/>
			</clay:content-col>
		</clay:content-row>
	</c:if>
</clay:container-fluid>