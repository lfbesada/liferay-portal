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

package com.liferay.journal.web.internal.configuration.provider;

import com.liferay.journal.web.internal.configuration.JournalArticleAICreatorCompanyConfiguration;
import com.liferay.journal.web.internal.configuration.JournalArticleAICreatorGroupConfiguration;
import com.liferay.portal.kernel.module.configuration.ConfigurationException;
import com.liferay.portal.kernel.module.configuration.ConfigurationProvider;
import com.liferay.portal.kernel.util.Validator;

import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

/**
 * @author Lourdes Fernández Besada
 */
@Component(service = JournalArticleAICreatorConfigurationProvider.class)
public class JournalArticleAICreatorConfigurationProvider {

	public String getApiKey(long companyId) throws ConfigurationException {
		JournalArticleAICreatorCompanyConfiguration
			journalArticleAICreatorCompanyConfiguration =
				_configurationProvider.getCompanyConfiguration(
					JournalArticleAICreatorCompanyConfiguration.class,
					companyId);

		return journalArticleAICreatorCompanyConfiguration.apiKey();
	}

	public String getApiKey(long companyId, long groupId)
		throws ConfigurationException {

		JournalArticleAICreatorGroupConfiguration
			journalArticleAICreatorGroupConfiguration =
				_configurationProvider.getGroupConfiguration(
					JournalArticleAICreatorGroupConfiguration.class, groupId);

		if (Validator.isNotNull(
				journalArticleAICreatorGroupConfiguration.apiKey())) {

			return journalArticleAICreatorGroupConfiguration.apiKey();
		}

		JournalArticleAICreatorCompanyConfiguration
			journalArticleAICreatorCompanyConfiguration =
				_configurationProvider.getCompanyConfiguration(
					JournalArticleAICreatorCompanyConfiguration.class,
					companyId);

		return journalArticleAICreatorCompanyConfiguration.apiKey();
	}

	public boolean isEnabled(long companyId) throws ConfigurationException {
		JournalArticleAICreatorCompanyConfiguration
			journalArticleAICreatorCompanyConfiguration =
				_configurationProvider.getCompanyConfiguration(
					JournalArticleAICreatorCompanyConfiguration.class,
					companyId);

		if (journalArticleAICreatorCompanyConfiguration.
				enableOpenAIToCreateContentInYourSites()) {

			return true;
		}

		return false;
	}

	public boolean isEnabled(long companyId, long groupId)
		throws ConfigurationException {

		if (!isEnabled(companyId)) {
			return false;
		}

		JournalArticleAICreatorGroupConfiguration
			journalArticleAICreatorGroupConfiguration =
				_configurationProvider.getGroupConfiguration(
					JournalArticleAICreatorGroupConfiguration.class, groupId);

		if (journalArticleAICreatorGroupConfiguration.
				enableOpenAIToCreateContentInYourSites()) {

			return true;
		}

		return false;
	}

	@Reference
	private ConfigurationProvider _configurationProvider;

}