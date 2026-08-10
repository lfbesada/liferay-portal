<%--
/**
 * SPDX-FileCopyrightText: (c) 2026 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */
--%>

<%@ include file="/init.jsp" %>

<%@ page import="com.liferay.fragment.web.internal.design.library.AddFragmentEntryMVCRenderCommand" %><%@
page import="com.liferay.portal.kernel.json.JSONObject" %>

<%
JSONObject propsJSONObject = (JSONObject)request.getAttribute(AddFragmentEntryMVCRenderCommand.PROPS_ATTRIBUTE);
%>

<react:component
	module="{DesignLibraryAddFragmentForm} from fragment-web"
	props="<%= propsJSONObject.toString() %>"
/>
