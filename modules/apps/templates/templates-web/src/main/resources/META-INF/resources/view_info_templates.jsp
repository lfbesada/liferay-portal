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

<h1>Information templates view</h1>
<ul>

	<%
	for (InfoItemRendererTemplate infoItemRendererTemplate : templatesDisplayContext.getInfoItemRendererTemplatesList(renderRequest)) {
	%>

		<li>Label: <%= infoItemRendererTemplate.getLabel() %> [<%= infoItemRendererTemplate.getTemplateKey() %>]</li>

	<%
	}
	%>

</ul>

<div class="component-image overflow-hidden">
	<picture data-fileentryid="39385">
		<source media="(max-width:300px)" srcset="/o/templates-web/icons/work-in-progress.jpeg" />
		<source media="(max-width:1000px) and (min-width:300px)" srcset="/o/templates-web/icons/work-in-progress.jpeg" />

		<img alt="Responsive Image" class="w-100" data-lfr-editable-id="image-square" data-lfr-editable-type="image" src="/o/templates-web/icons/work-in-progress.jpeg" />
	</picture>
</div>