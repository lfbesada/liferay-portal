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
SelectAssetVocabularyItemSelectorDisplayContext selectAssetVocabularyItemSelectorDisplayContext = (SelectAssetVocabularyItemSelectorDisplayContext)request.getAttribute(AssetVocabularyItemSelectorWebKeys.SELECT_ASSET_VOCABULARY_ITEM_SELECTOR_DISPLAY_CONTEXT);
%>

<liferay-frontend:management-bar
	searchContainerId="vocabularies"
>
	<liferay-ui:input-search
		markupView="lexicon"
	/>
</liferay-frontend:management-bar>

<div class="container-fluid container-fluid-max-xl p-4">
	<liferay-ui:search-container
		cssClass="table-hover"
		id="vocabularies"
		searchContainer="<%= selectAssetVocabularyItemSelectorDisplayContext.getAssetVocabularySearchContainer() %>"
	>
		<liferay-ui:search-container-row
			className="com.liferay.asset.kernel.model.AssetVocabulary"
			keyProperty="assetVocabularyId"
			modelVar="assetVocabulary"
		>

			<%
			row.setData(
				HashMapBuilder.<String, Object>put(
					"group-id", String.valueOf(assetVocabulary.getGroupId())
				).put(
					"title", assetVocabulary.getTitle(locale)
				).put(
					"uuid", assetVocabulary.getUuid()
				).put(
					"vocabulary-id", String.valueOf(assetVocabulary.getVocabularyId())
				).build());
			%>

			<liferay-ui:search-container-column-text
				name="vocabularies"
			>
				<clay:sticker
					cssClass="bg-light mr-3"
					displayType="dark"
					icon="vocabulary"
				/>

				<b><%= HtmlUtil.escape(assetVocabulary.getTitle(locale)) %></b>
			</liferay-ui:search-container-column-text>

			<liferay-ui:search-container-column-text
				name="site"
				value="<%= selectAssetVocabularyItemSelectorDisplayContext.getVocabularyGroupDescriptiveName(assetVocabulary.getGroupId()) %>"
			/>

			<liferay-ui:search-container-column-text
				cssClass="table-column-text-center"
				name="categories"
				value="<%= String.valueOf(assetVocabulary.getCategoriesCount()) %>"
			/>

			<liferay-ui:search-container-column-date
				name="creation-date"
				property="createDate"
			/>
		</liferay-ui:search-container-row>

		<liferay-ui:search-iterator
			markupView="lexicon"
			searchResultCssClass="table table-autofit"
		/>
	</liferay-ui:search-container>
</div>