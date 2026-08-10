<%--
/**
 * SPDX-FileCopyrightText: (c) 2026 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */
--%>

<%@ include file="/init.jsp" %>

<%@ page import="com.liferay.portal.kernel.json.JSONObject" %><%@
page import="com.liferay.style.book.web.internal.design.library.AddStyleBookEntryMVCRenderCommand" %>

<%
JSONObject propsJSONObject = (JSONObject)request.getAttribute(AddStyleBookEntryMVCRenderCommand.PROPS_ATTRIBUTE);
%>

<react:component
	module="{DesignLibraryAddStyleBookForm} from style-book-web"
	props="<%= propsJSONObject.toString() %>"
/>
