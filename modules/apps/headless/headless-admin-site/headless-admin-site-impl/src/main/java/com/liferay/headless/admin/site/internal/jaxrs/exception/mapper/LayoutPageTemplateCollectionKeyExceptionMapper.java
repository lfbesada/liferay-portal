/**
 * SPDX-FileCopyrightText: (c) 2025 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.headless.admin.site.internal.jaxrs.exception.mapper;

import com.liferay.layout.page.template.constants.LayoutPageTemplateCollectionTypeConstants;
import com.liferay.layout.page.template.exception.LayoutPageTemplateCollectionLayoutPageTemplateCollectionKeyException;
import com.liferay.portal.kernel.util.StringUtil;
import com.liferay.portal.vulcan.jaxrs.exception.mapper.BaseExceptionMapper;
import com.liferay.portal.vulcan.jaxrs.exception.mapper.Problem;

import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

import org.osgi.service.component.annotations.Component;

/**
 * @author Lourdes Fernández Besada
 */
@Component(
	property = {
		"osgi.jaxrs.application.select=(osgi.jaxrs.name=Liferay.Headless.Admin.Site)",
		"osgi.jaxrs.extension=true",
		"osgi.jaxrs.name=Liferay.Headless.Admin.Site.LayoutPageTemplateCollectionKeyExceptionMapper"
	},
	service = ExceptionMapper.class
)
@Provider
public class LayoutPageTemplateCollectionKeyExceptionMapper
	extends BaseExceptionMapper
		<LayoutPageTemplateCollectionLayoutPageTemplateCollectionKeyException> {

	@Override
	protected Problem getProblem(
		LayoutPageTemplateCollectionLayoutPageTemplateCollectionKeyException
			layoutPageTemplateCollectionLayoutPageTemplateCollectionKeyException) {

		if (layoutPageTemplateCollectionLayoutPageTemplateCollectionKeyException.
				getLayoutPageTemplateCollectionType() ==
					LayoutPageTemplateCollectionTypeConstants.DISPLAY_PAGE) {

			return new Problem(
				Response.Status.CONFLICT,
				StringUtil.replace(
					StringUtil.replace(
						layoutPageTemplateCollectionLayoutPageTemplateCollectionKeyException.
							getMessage(),
						"layout page template collection",
						"display page template folder"),
					"Layout page template collection",
					"Display page template folder"));
		}

		return new Problem(
			Response.Status.CONFLICT,
			StringUtil.replace(
				StringUtil.replace(
					layoutPageTemplateCollectionLayoutPageTemplateCollectionKeyException.
						getMessage(),
					"layout page template collection", "page template set"),
				"Layout page template collection", "Page template set"));
	}

}