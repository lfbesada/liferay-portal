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

import com.liferay.journal.web.internal.configuration.provider.JournalArticleAICreatorConfigurationProvider;
import com.liferay.portal.kernel.module.configuration.ConfigurationException;
import com.liferay.portal.kernel.theme.ThemeDisplay;
import com.liferay.portal.kernel.util.WebKeys;

import javax.servlet.http.HttpServletRequest;

/**
 * @author Lourdes Fernández Besada
 */
public class EditJournalArticleAICreatorConfigurationDisplayContext {

	public EditJournalArticleAICreatorConfigurationDisplayContext(
		HttpServletRequest httpServletRequest,
		JournalArticleAICreatorConfigurationProvider
			journalArticleAICreatorConfigurationProvider) {

		_journalArticleAICreatorConfigurationProvider =
			journalArticleAICreatorConfigurationProvider;

		_themeDisplay = (ThemeDisplay)httpServletRequest.getAttribute(
			WebKeys.THEME_DISPLAY);
	}

	public String getApiKey() throws ConfigurationException {
		return _journalArticleAICreatorConfigurationProvider.getApiKey(
			_themeDisplay.getCompanyId());
	}

	public boolean isEnabled() throws ConfigurationException {
		return _journalArticleAICreatorConfigurationProvider.isEnabled(
			_themeDisplay.getCompanyId());
	}

	private final JournalArticleAICreatorConfigurationProvider
		_journalArticleAICreatorConfigurationProvider;
	private final ThemeDisplay _themeDisplay;

}