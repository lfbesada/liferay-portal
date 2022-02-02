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

package com.liferay.journal.internal.search.spi.model.query.contributor;

import com.liferay.journal.model.JournalArticle;
import com.liferay.portal.kernel.search.BooleanClauseOccur;
import com.liferay.portal.kernel.search.BooleanQuery;
import com.liferay.portal.kernel.search.ExpandoQueryContributor;
import com.liferay.portal.kernel.search.Field;
import com.liferay.portal.kernel.search.QueryConfig;
import com.liferay.portal.kernel.search.SearchContext;
import com.liferay.portal.kernel.search.generic.BooleanQueryImpl;
import com.liferay.portal.kernel.util.GetterUtil;
import com.liferay.portal.kernel.util.StringUtil;
import com.liferay.portal.kernel.util.Validator;
import com.liferay.portal.search.localization.SearchLocalizationHelper;
import com.liferay.portal.search.spi.model.query.contributor.KeywordQueryContributor;
import com.liferay.portal.search.spi.model.query.contributor.helper.KeywordQueryContributorHelper;

import java.io.Serializable;

import java.util.LinkedHashMap;

import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

/**
 * @author Lourdes Fernández Besada
 */
@Component(
	immediate = true,
	property = "indexer.class.name=com.liferay.journal.model.JournalArticle",
	service = KeywordQueryContributor.class
)
public class JournalArticleKeywordQueryContributor
	implements KeywordQueryContributor {

	@Override
	public void contribute(
		String keywords, BooleanQuery booleanQuery,
		KeywordQueryContributorHelper keywordQueryContributorHelper) {

		SearchContext searchContext =
			keywordQueryContributorHelper.getSearchContext();

		_addSearchTerm(booleanQuery, searchContext, Field.ARTICLE_ID, false);
		_addSearchTerm(booleanQuery, searchContext, Field.CLASS_PK, false);
		_addSearchLocalizedTerm(
			booleanQuery, searchContext, Field.CONTENT, false);
		_addSearchLocalizedTerm(
			booleanQuery, searchContext, Field.DESCRIPTION, false);
		_addSearchTerm(
			booleanQuery, searchContext, Field.ENTRY_CLASS_PK, false);
		_addSearchLocalizedTerm(
			booleanQuery, searchContext, Field.TITLE, false);
		_addSearchTerm(booleanQuery, searchContext, Field.USER_NAME, false);

		LinkedHashMap<String, Object> params =
			(LinkedHashMap<String, Object>)searchContext.getAttribute("params");

		if (params != null) {
			String expandoAttributes = (String)params.get("expandoAttributes");

			if (Validator.isNotNull(expandoAttributes)) {
				_addSearchExpando(
					booleanQuery, searchContext, expandoAttributes);
			}
		}

		QueryConfig queryConfig = searchContext.getQueryConfig();

		String[] localizedFieldNames =
			_searchLocalizationHelper.getLocalizedFieldNames(
				new String[] {Field.CONTENT, Field.DESCRIPTION, Field.TITLE},
				searchContext);

		queryConfig.addHighlightFieldNames(localizedFieldNames);
	}

	private void _addLocalizedFields(
			BooleanQuery booleanQuery, String field, String value, boolean like,
			SearchContext searchContext)
		throws Exception {

		String[] localizedFieldNames =
			_searchLocalizationHelper.getLocalizedFieldNames(
				new String[] {field}, searchContext);

		for (String localizedFieldName : localizedFieldNames) {
			booleanQuery.addTerm(localizedFieldName, value, like);
		}
	}

	private void _addLocalizedQuery(
			BooleanQuery booleanQuery, BooleanQuery localizedQuery,
			SearchContext searchContext)
		throws Exception {

		BooleanClauseOccur booleanClauseOccur = BooleanClauseOccur.SHOULD;

		if (searchContext.isAndSearch()) {
			booleanClauseOccur = BooleanClauseOccur.MUST;
		}

		booleanQuery.add(localizedQuery, booleanClauseOccur);
	}

	private void _addSearchExpando(
			BooleanQuery booleanQuery, SearchContext searchContext,
			String keywords)
		throws Exception {

		_expandoQueryContributor.contribute(
			keywords, booleanQuery,
			new String[] {JournalArticle.class.getName()}, searchContext);
	}

	private void _addSearchLocalizedTerm(
			BooleanQuery searchQuery, SearchContext searchContext, String field,
			boolean like)
		throws Exception {

		if (Validator.isBlank(field)) {
			return;
		}

		String value = GetterUtil.getString(searchContext.getAttribute(field));

		if (Validator.isBlank(value)) {
			value = searchContext.getKeywords();
		}

		if (Validator.isBlank(value)) {
			return;
		}

		if (Validator.isBlank(searchContext.getKeywords())) {
			BooleanQuery localizedQuery = new BooleanQueryImpl();

			_addLocalizedFields(
				localizedQuery, field, value, like, searchContext);

			_addLocalizedQuery(searchQuery, localizedQuery, searchContext);
		}
		else {
			_addLocalizedFields(searchQuery, field, value, like, searchContext);
		}
	}

	private void _addSearchTerm(
			BooleanQuery searchQuery, SearchContext searchContext, String field,
			boolean like)
		throws Exception {

		if (Validator.isNull(field)) {
			return;
		}

		String value = null;

		Serializable serializable = searchContext.getAttribute(field);

		if (serializable != null) {
			Class<?> clazz = serializable.getClass();

			if (clazz.isArray()) {
				value = StringUtil.merge((Object[])serializable);
			}
			else {
				value = GetterUtil.getString(serializable);
			}
		}
		else {
			value = GetterUtil.getString(serializable);
		}

		if (Validator.isNotNull(value) &&
			(searchContext.getFacet(field) != null)) {

			return;
		}

		if (Validator.isNull(value)) {
			value = searchContext.getKeywords();
		}

		if (Validator.isNull(value)) {
			return;
		}

		if (searchContext.isAndSearch()) {
			searchQuery.addRequiredTerm(field, value, like);
		}
		else {
			searchQuery.addTerm(field, value, like);
		}
	}

	@Reference
	private ExpandoQueryContributor _expandoQueryContributor;

	@Reference
	private SearchLocalizationHelper _searchLocalizationHelper;

}