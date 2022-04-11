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

package com.liferay.layout.internal.search.spi.model.index.contributor;

import com.liferay.exportimport.kernel.lar.ExportImportThreadLocal;
import com.liferay.fragment.constants.FragmentEntryLinkConstants;
import com.liferay.fragment.renderer.FragmentRendererController;
import com.liferay.layout.adaptive.media.LayoutAdaptiveMediaProcessor;
import com.liferay.layout.crawler.LayoutCrawler;
import com.liferay.layout.internal.search.util.LayoutPageTemplateStructureRenderUtil;
import com.liferay.layout.page.template.model.LayoutPageTemplateStructure;
import com.liferay.layout.page.template.service.LayoutPageTemplateStructureLocalService;
import com.liferay.petra.string.StringPool;
import com.liferay.portal.kernel.language.LanguageUtil;
import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.portal.kernel.model.Group;
import com.liferay.portal.kernel.model.Layout;
import com.liferay.portal.kernel.search.BooleanClause;
import com.liferay.portal.kernel.search.BooleanClauseFactoryUtil;
import com.liferay.portal.kernel.search.BooleanClauseOccur;
import com.liferay.portal.kernel.search.Document;
import com.liferay.portal.kernel.search.Field;
import com.liferay.portal.kernel.search.Hits;
import com.liferay.portal.kernel.search.Indexer;
import com.liferay.portal.kernel.search.IndexerRegistryUtil;
import com.liferay.portal.kernel.search.Query;
import com.liferay.portal.kernel.search.SearchContext;
import com.liferay.portal.kernel.security.auth.CompanyThreadLocal;
import com.liferay.portal.kernel.service.GroupLocalService;
import com.liferay.portal.kernel.service.LayoutLocalService;
import com.liferay.portal.kernel.service.ServiceContext;
import com.liferay.portal.kernel.service.ServiceContextThreadLocal;
import com.liferay.portal.kernel.servlet.DynamicServletRequest;
import com.liferay.portal.kernel.util.GetterUtil;
import com.liferay.portal.kernel.util.Html;
import com.liferay.portal.kernel.util.LocaleUtil;
import com.liferay.portal.kernel.util.Validator;
import com.liferay.portal.kernel.workflow.WorkflowConstants;
import com.liferay.portal.search.spi.model.index.contributor.ModelDocumentContributor;
import com.liferay.segments.constants.SegmentsExperienceConstants;
import com.liferay.staging.StagingGroupHelper;

import java.util.Locale;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

/**
 * @author Vagner B.C
 */
@Component(
	immediate = true,
	property = "indexer.class.name=com.liferay.portal.kernel.model.Layout",
	service = ModelDocumentContributor.class
)
public class LayoutModelDocumentContributor
	implements ModelDocumentContributor<Layout> {

	public static final String CLASS_NAME = Layout.class.getName();

	@Override
	public void contribute(Document document, Layout layout) {
		if (layout.isSystem() ||
			(layout.getStatus() != WorkflowConstants.STATUS_APPROVED)) {

			return;
		}

		document.addText(
			Field.DEFAULT_LANGUAGE_ID, layout.getDefaultLanguageId());
		document.addLocalizedText(Field.NAME, layout.getNameMap());
		document.addText(
			"privateLayout", String.valueOf(layout.isPrivateLayout()));
		document.addKeyword(Field.STATUS, _getStatus(layout));
		document.addText(Field.TYPE, layout.getType());

		for (String languageId : layout.getAvailableLanguageIds()) {
			Locale locale = LocaleUtil.fromLanguageId(languageId);

			document.addText(
				Field.getLocalizedName(locale, Field.TITLE),
				layout.getName(locale));
		}

		LayoutPageTemplateStructure layoutPageTemplateStructure =
			_layoutPageTemplateStructureLocalService.
				fetchLayoutPageTemplateStructure(
					layout.getGroupId(), layout.getPlid());

		if (layoutPageTemplateStructure == null) {
			return;
		}

		Layout draftLayout = layout.fetchDraftLayout();

		if ((draftLayout == null) ||
			!GetterUtil.getBoolean(
				draftLayout.getTypeSettingsProperty("published"))) {

			return;
		}

		HttpServletRequest httpServletRequest = null;
		HttpServletResponse httpServletResponse = null;

		Group group = layout.getGroup();

		if (layout.isPrivateLayout() || group.isStagingGroup()) {
			ServiceContext serviceContext =
				ServiceContextThreadLocal.getServiceContext();

			if ((serviceContext != null) &&
				(serviceContext.getRequest() != null)) {

				httpServletRequest = DynamicServletRequest.addQueryString(
					serviceContext.getRequest(), "p_l_id=" + layout.getPlid(),
					false);

				httpServletResponse = serviceContext.getResponse();
			}
		}

		Set<Locale> locales = LanguageUtil.getAvailableLocales(
			layout.getGroupId());

		for (Locale locale : locales) {
			String content = StringPool.BLANK;

			try {
				content = _getLayoutContent(
					httpServletRequest, httpServletResponse, layout,
					layoutPageTemplateStructure, locale);
			}
			catch (Exception exception) {
				if (_log.isWarnEnabled()) {
					_log.warn("Unable to get layout content", exception);
				}
			}

			if (Validator.isNull(content)) {
				continue;
			}

			document.addText(
				Field.getLocalizedName(locale, Field.CONTENT), content);
		}
	}

	private String _getLayoutContent(
			HttpServletRequest httpServletRequest,
			HttpServletResponse httpServletResponse, Layout layout,
			LayoutPageTemplateStructure layoutPageTemplateStructure,
			Locale locale)
		throws Exception {

		Group group = layout.getGroup();

		if (!layout.isPrivateLayout() && !group.isStagingGroup()) {
			String content = _layoutCrawler.getLayoutContent(layout, locale);

			if (Validator.isNull(content)) {
				return content;
			}

			return _html.stripHtml(_getWrapper(content));
		}

		if ((httpServletRequest == null) || (httpServletResponse == null)) {
			return _getStagedContent(layout, locale);
		}

		return _html.stripHtml(
			LayoutPageTemplateStructureRenderUtil.renderLayoutContent(
				_fragmentRendererController, httpServletRequest,
				httpServletResponse, layoutPageTemplateStructure,
				FragmentEntryLinkConstants.VIEW, locale,
				SegmentsExperienceConstants.ID_DEFAULT));
	}

	private String _getStagedContent(Layout layout, Locale locale)
		throws Exception {

		Group group = _groupLocalService.getGroup(layout.getGroupId());

		Group stagingGroup = null;

		if (ExportImportThreadLocal.isInitialLayoutStagingInProcess()) {
			stagingGroup = _stagingGroupHelper.fetchLiveGroup(group);
		}
		else if (!group.isStaged() || group.isStagingGroup()) {
			stagingGroup = group;
		}
		else {
			stagingGroup = group.getStagingGroup();
		}

		if (stagingGroup == null) {
			return StringPool.BLANK;
		}

		Layout stagingLayout = _layoutLocalService.fetchLayoutByUuidAndGroupId(
			layout.getUuid(), stagingGroup.getGroupId(),
			layout.isPrivateLayout());

		SearchContext searchContext = new SearchContext();

		if ((CompanyThreadLocal.getCompanyId() == 0) ||
			ExportImportThreadLocal.isStagingInProcess()) {

			searchContext.setCompanyId(stagingLayout.getCompanyId());
		}

		searchContext.setGroupIds(new long[] {stagingGroup.getGroupId()});

		searchContext.setEntryClassNames(new String[] {Layout.class.getName()});

		BooleanClause<Query> booleanClause = BooleanClauseFactoryUtil.create(
			Field.ENTRY_CLASS_PK, String.valueOf(stagingLayout.getPlid()),
			BooleanClauseOccur.MUST.getName());

		searchContext.setBooleanClauses(new BooleanClause[] {booleanClause});

		Indexer<Layout> indexer = IndexerRegistryUtil.getIndexer(
			Layout.class.getName());

		Hits hits = indexer.search(searchContext);

		Document[] documents = hits.getDocs();

		if (documents.length != 1) {
			return StringPool.BLANK;
		}

		Document document = documents[0];

		return document.get(Field.getLocalizedName(locale, Field.CONTENT));
	}

	private int _getStatus(Layout layout) {
		if (!layout.isTypeContent()) {
			return WorkflowConstants.STATUS_APPROVED;
		}

		Layout draftLayout = layout.fetchDraftLayout();

		boolean published = false;

		if (draftLayout != null) {
			published = GetterUtil.getBoolean(
				draftLayout.getTypeSettingsProperty("published"));
		}

		if (published) {
			return WorkflowConstants.STATUS_APPROVED;
		}

		return WorkflowConstants.STATUS_DRAFT;
	}

	private String _getWrapper(String layoutContent) {
		int wrapperIndex = layoutContent.indexOf(_WRAPPER_ELEMENT);

		if (wrapperIndex == -1) {
			return layoutContent;
		}

		return layoutContent.substring(
			wrapperIndex + _WRAPPER_ELEMENT.length());
	}

	private static final String _WRAPPER_ELEMENT = "id=\"wrapper\">";

	private static final Log _log = LogFactoryUtil.getLog(
		LayoutModelDocumentContributor.class);

	@Reference
	private FragmentRendererController _fragmentRendererController;

	@Reference
	private GroupLocalService _groupLocalService;

	@Reference
	private Html _html;

	@Reference
	private LayoutAdaptiveMediaProcessor _layoutAdaptiveMediaProcessor;

	@Reference
	private LayoutCrawler _layoutCrawler;

	@Reference
	private LayoutLocalService _layoutLocalService;

	@Reference
	private LayoutPageTemplateStructureLocalService
		_layoutPageTemplateStructureLocalService;

	@Reference
	private StagingGroupHelper _stagingGroupHelper;

}