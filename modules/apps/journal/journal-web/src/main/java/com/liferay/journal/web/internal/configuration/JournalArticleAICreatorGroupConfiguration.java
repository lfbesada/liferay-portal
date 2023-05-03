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

package com.liferay.journal.web.internal.configuration;

import aQute.bnd.annotation.metatype.Meta;

import com.liferay.portal.configuration.metatype.annotations.ExtendedObjectClassDefinition;

/**
 * @author Lourdes Fernández Besada
 */
@ExtendedObjectClassDefinition(
	category = "ai-creator", generateUI = false,
	scope = ExtendedObjectClassDefinition.Scope.GROUP
)
@Meta.OCD(
	description = "journal-article-ai-creator-group-configuration-description",
	id = "com.liferay.journal.web.internal.configuration.JournalArticleAICreatorGroupConfiguration",
	localization = "content/Language",
	name = "journal-article-ai-creator-group-configuration-name"
)
public interface JournalArticleAICreatorGroupConfiguration {

	@Meta.AD(
		deflt = "true", name = "enable-openai-to-create-content-in-your-sites",
		required = false
	)
	public boolean enableOpenAIToCreateContentInYourSites();

	@Meta.AD(deflt = "", name = "api-key", required = false)
	public String apiKey();

}