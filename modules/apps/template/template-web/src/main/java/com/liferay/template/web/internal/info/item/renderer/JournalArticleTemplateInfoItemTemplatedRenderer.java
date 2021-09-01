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

package com.liferay.template.web.internal.info.item.renderer;

import com.liferay.dynamic.data.mapping.model.DDMStructure;
import com.liferay.info.item.InfoItemServiceTracker;
import com.liferay.info.item.renderer.InfoItemRenderer;
import com.liferay.info.item.renderer.InfoItemTemplatedRenderer;
import com.liferay.info.item.renderer.template.InfoItemRendererTemplate;
import com.liferay.journal.model.JournalArticle;
import com.liferay.staging.StagingGroupHelper;

import java.util.List;
import java.util.Locale;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

/**
 * @author Lourdes Fernández Besada
 */
@Component(
	property = "service.ranking:Integer=50", service = InfoItemRenderer.class
)
public class JournalArticleTemplateInfoItemTemplatedRenderer
	extends BaseTemplateInfoItemTemplatedRenderer<JournalArticle>
	implements InfoItemTemplatedRenderer<JournalArticle> {

	@Override
	public List<InfoItemRendererTemplate> getInfoItemRendererTemplates(
		JournalArticle journalArticle, Locale locale) {

		DDMStructure ddmStructure = journalArticle.getDDMStructure();

		return getInfoItemRendererTemplates(
			JournalArticle.class.getName(),
			String.valueOf(ddmStructure.getStructureId()), locale);
	}

	@Override
	public String getInfoItemRendererTemplatesGroupLabel(
		JournalArticle journalArticle, Locale locale) {

		DDMStructure ddmStructure = journalArticle.getDDMStructure();

		return getInfoItemRendererTemplatesGroupLabel(
			JournalArticle.class.getName(),
			String.valueOf(ddmStructure.getStructureId()), locale);
	}

	@Override
	public void render(
		JournalArticle journalArticle, String templateKey,
		HttpServletRequest httpServletRequest,
		HttpServletResponse httpServletResponse) {

		renderTemplate(
			JournalArticle.class.getName(), journalArticle, templateKey,
			httpServletRequest, httpServletResponse);
	}

	@Override
	protected InfoItemServiceTracker getInfoItemServiceTracker() {
		return _infoItemServiceTracker;
	}

	@Override
	protected StagingGroupHelper getStagingGroupHelper() {
		return _stagingGroupHelper;
	}

	@Reference
	private InfoItemServiceTracker _infoItemServiceTracker;

	@Reference
	private StagingGroupHelper _stagingGroupHelper;

}