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
EditJournalArticleAICreatorConfigurationDisplayContext editJournalArticleAICreatorConfigurationDisplayContext = (EditJournalArticleAICreatorConfigurationDisplayContext)request.getAttribute(EditJournalArticleAICreatorConfigurationDisplayContext.class.getName());
%>

<p class="text-secondary">
	<liferay-ui:message key="journal-article-ai-creator-group-configuration-description" />
</p>

<liferay-ui:error exception="<%= ConfigurationException.class %>" message="there-was-an-error-processing-one-or-more-of-the-configurations" />

<clay:sheet-section>
	<clay:content-row
		containerElement="h3"
		cssClass="sheet-subtitle"
	>
		<clay:content-col
			expand="<%= true %>"
		>
			<span class="heading-text"><liferay-ui:message key="open-ai" /></span>
		</clay:content-col>
	</clay:content-row>

	<clay:content-row
		padded="<%= true %>"
	>
		<clay:content-col
			expand="<%= true %>"
		>
			<clay:checkbox
				checked="<%= editJournalArticleAICreatorConfigurationDisplayContext.isEnabled() %>"
				disabled="<%= editJournalArticleAICreatorConfigurationDisplayContext.disallowEnableOpenAI() %>"
				id='<%= liferayPortletResponse.getNamespace() + "enableOpenAI" %>'
				label='<%= LanguageUtil.get(request, "enable-openai-to-create-content-in-your-sites") %>'
				name='<%= liferayPortletResponse.getNamespace() + "enableOpenAI" %>'
			/>
		</clay:content-col>
	</clay:content-row>

	<clay:content-row
		padded="<%= true %>"
	>
		<clay:content-col
			expand="<%= true %>"
		>
			<aui:input label="api-key" name="apiKey" type="text" value="<%= editJournalArticleAICreatorConfigurationDisplayContext.getApiKey() %>" />
		</clay:content-col>
	</clay:content-row>

	<clay:content-row>
		<clay:content-col>
			<clay:link
				href="https://platform.openai.com/docs/api-reference/authentication"
				label="how-to-get-an-api-key"
				target="_blank"
			/>
		</clay:content-col>
	</clay:content-row>
</clay:sheet-section>