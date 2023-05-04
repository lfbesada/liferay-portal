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

package com.liferay.journal.web.internal.portal.settings.configuration.admin.display;

import com.liferay.journal.web.internal.configuration.manager.JournalArticleAICreatorConfigurationManager;
import com.liferay.journal.web.internal.display.context.EditJournalArticleAICreatorConfigurationDisplayContext;
import com.liferay.portal.kernel.feature.flag.FeatureFlagManagerUtil;
import com.liferay.portal.kernel.language.Language;
import com.liferay.portal.kernel.model.Group;
import com.liferay.site.settings.configuration.admin.display.SiteSettingsConfigurationScreenContributor;

import java.util.Locale;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

/**
 * @author Lourdes Fernández Besada
 */
@Component(service = SiteSettingsConfigurationScreenContributor.class)
public class JournalArticleAICreatorSiteSettingsConfigurationScreenContributor
	implements SiteSettingsConfigurationScreenContributor {

	@Override
	public String getCategoryKey() {
		return "ai-creator";
	}

	@Override
	public String getJspPath() {
		return "/configuration/article/ai_creator.jsp";
	}

	@Override
	public String getKey() {
		return "journal-article-ai-creator-group-configuration";
	}

	@Override
	public String getName(Locale locale) {
		return _language.get(locale, "web-content");
	}

	@Override
	public String getSaveMVCActionCommandName() {
		return "/journal/save_journal_article_ai_creator_group_configuration";
	}

	@Override
	public ServletContext getServletContext() {
		return _servletContext;
	}

	@Override
	public boolean isVisible(Group group) {
		if (!FeatureFlagManagerUtil.isEnabled("LPS-179483")) {
			return false;
		}

		return true;
	}

	@Override
	public void setAttributes(
		HttpServletRequest httpServletRequest,
		HttpServletResponse httpServletResponse) {

		httpServletRequest.setAttribute(
			EditJournalArticleAICreatorConfigurationDisplayContext.class.
				getName(),
			new EditJournalArticleAICreatorConfigurationDisplayContext(
				false, httpServletRequest,
				_journalArticleAICreatorConfigurationManager));
	}

	@Reference
	private JournalArticleAICreatorConfigurationManager
		_journalArticleAICreatorConfigurationManager;

	@Reference
	private Language _language;

	@Reference(target = "(osgi.web.symbolicname=com.liferay.journal.web)")
	private ServletContext _servletContext;

}