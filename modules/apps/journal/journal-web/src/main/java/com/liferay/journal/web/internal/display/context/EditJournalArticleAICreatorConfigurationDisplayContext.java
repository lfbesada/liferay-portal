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

package com.liferay.journal.web.internal.display.context;

import com.liferay.journal.web.internal.configuration.manager.JournalArticleAICreatorConfigurationManager;
import com.liferay.portal.kernel.module.configuration.ConfigurationException;
import com.liferay.portal.kernel.theme.ThemeDisplay;
import com.liferay.portal.kernel.util.WebKeys;

import javax.servlet.http.HttpServletRequest;

/**
 * @author Lourdes Fernández Besada
 */
public class EditJournalArticleAICreatorConfigurationDisplayContext {

	public EditJournalArticleAICreatorConfigurationDisplayContext(
		boolean company, HttpServletRequest httpServletRequest,
		JournalArticleAICreatorConfigurationManager
			journalArticleAICreatorConfigurationManager) {

		_company = company;
		_journalArticleAICreatorConfigurationManager =
			journalArticleAICreatorConfigurationManager;

		_themeDisplay = (ThemeDisplay)httpServletRequest.getAttribute(
			WebKeys.THEME_DISPLAY);
	}

	public boolean disallowEnableOpenAI() throws ConfigurationException {
		if (_company) {
			return false;
		}

		return !_journalArticleAICreatorConfigurationManager.
			isAICreatorCompanyEnabled(_themeDisplay.getCompanyId());
	}

	public String getApiKey() throws ConfigurationException {
		if (_company) {
			return _journalArticleAICreatorConfigurationManager.
				getAICreatorCompanyApiKey(_themeDisplay.getCompanyId());
		}

		return _journalArticleAICreatorConfigurationManager.
			getAICreatorGroupApiKey(
				_themeDisplay.getCompanyId(), _themeDisplay.getScopeGroupId(),
				true);
	}

	public boolean isEnabled() throws ConfigurationException {
		if (_company) {
			return _journalArticleAICreatorConfigurationManager.
				isAICreatorCompanyEnabled(_themeDisplay.getCompanyId());
		}

		return _journalArticleAICreatorConfigurationManager.
			isAICreatorGroupEnabled(
				_themeDisplay.getCompanyId(), _themeDisplay.getScopeGroupId());
	}

	private final boolean _company;
	private final JournalArticleAICreatorConfigurationManager
		_journalArticleAICreatorConfigurationManager;
	private final ThemeDisplay _themeDisplay;

}