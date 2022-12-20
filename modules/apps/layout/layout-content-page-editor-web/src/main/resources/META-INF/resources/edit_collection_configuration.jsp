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
EditCollectionConfigurationDisplayContext editCollectionConfigurationDisplayContext = (EditCollectionConfigurationDisplayContext)request.getAttribute(EditCollectionConfigurationDisplayContext.class.getName());
%>

<liferay-frontend:edit-form
	action="<%= editCollectionConfigurationDisplayContext.getActionURL() %>"
	method="post"
	name="fm"
>
	<aui:input name="collectionKey" type="hidden" value="<%= editCollectionConfigurationDisplayContext.getCollectionKey() %>" />
	<aui:input name="itemId" type="hidden" value="<%= editCollectionConfigurationDisplayContext.getItemId() %>" />
	<aui:input name="redirect" type="hidden" value="<%= editCollectionConfigurationDisplayContext.getRedirect() %>" />
	<aui:input name="segmentsExperienceId" type="hidden" value="<%= editCollectionConfigurationDisplayContext.getSegmentsExperienceId() %>" />

	<liferay-frontend:edit-form-body>
		<liferay-frontend:fieldset>
			<react:component
				module="page_editor/plugins/browser/components/page-structure/components/CollectionFilterConfigurationModal.js"
				props="<%= editCollectionConfigurationDisplayContext.getData() %>"
			/>
		</liferay-frontend:fieldset>
	</liferay-frontend:edit-form-body>

	<liferay-frontend:edit-form-footer>
		<liferay-frontend:edit-form-buttons />
	</liferay-frontend:edit-form-footer>
</liferay-frontend:edit-form>