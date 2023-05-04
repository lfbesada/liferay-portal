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

package com.liferay.journal.web.internal.configuration.manager;

import com.liferay.journal.web.internal.configuration.JournalArticleAICreatorCompanyConfiguration;
import com.liferay.journal.web.internal.configuration.JournalArticleAICreatorGroupConfiguration;
import com.liferay.portal.kernel.module.configuration.ConfigurationException;
import com.liferay.portal.kernel.module.configuration.ConfigurationProvider;
import com.liferay.portal.kernel.util.HashMapDictionaryBuilder;
import com.liferay.portal.kernel.util.Validator;

import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

/**
 * @author Lourdes Fernández Besada
 */
@Component(service = JournalArticleAICreatorConfigurationManager.class)
public class JournalArticleAICreatorConfigurationManager {

	public String getAICreatorCompanyApiKey(long companyId)
		throws ConfigurationException {

		JournalArticleAICreatorCompanyConfiguration
			journalArticleAICreatorCompanyConfiguration =
				_configurationProvider.getCompanyConfiguration(
					JournalArticleAICreatorCompanyConfiguration.class,
					companyId);

		return journalArticleAICreatorCompanyConfiguration.apiKey();
	}

	public String getAICreatorGroupApiKey(
			long companyId, long groupId, boolean strict)
		throws ConfigurationException {

		JournalArticleAICreatorGroupConfiguration
			journalArticleAICreatorGroupConfiguration =
				_configurationProvider.getGroupConfiguration(
					JournalArticleAICreatorGroupConfiguration.class, groupId);

		if (strict ||
			Validator.isNotNull(
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

	public boolean isAICreatorCompanyEnabled(long companyId)
		throws ConfigurationException {

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

	public boolean isAICreatorGroupEnabled(long companyId, long groupId)
		throws ConfigurationException {

		if (!isAICreatorCompanyEnabled(companyId)) {
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

	public void saveCompanyConfiguration(
			long companyId, String apiKey, boolean enabled)
		throws ConfigurationException {

		_configurationProvider.saveCompanyConfiguration(
			JournalArticleAICreatorCompanyConfiguration.class, companyId,
			HashMapDictionaryBuilder.<String, Object>put(
				"apiKey", apiKey
			).put(
				"enableOpenAIToCreateContentInYourSites", enabled
			).build());
	}

	public void saveGroupConfiguration(
			long groupId, String apiKey, boolean enabled)
		throws ConfigurationException {

		_configurationProvider.saveGroupConfiguration(
			JournalArticleAICreatorGroupConfiguration.class, groupId,
			HashMapDictionaryBuilder.<String, Object>put(
				"apiKey", apiKey
			).put(
				"enableOpenAIToCreateContentInYourSites", enabled
			).build());
	}

	@Reference
	private ConfigurationProvider _configurationProvider;

}