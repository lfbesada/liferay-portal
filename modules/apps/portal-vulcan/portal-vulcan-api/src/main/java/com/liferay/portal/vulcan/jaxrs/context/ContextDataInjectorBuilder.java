/**
 * SPDX-FileCopyrightText: (c) 2025 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.portal.vulcan.jaxrs.context;

import com.liferay.portal.kernel.model.Company;
import com.liferay.portal.kernel.model.User;
import com.liferay.portal.odata.filter.ExpressionConvert;
import com.liferay.portal.odata.filter.FilterParserProvider;
import com.liferay.portal.odata.sort.SortParserProvider;
import com.liferay.portal.vulcan.accept.language.AcceptLanguage;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import jakarta.ws.rs.core.UriInfo;

import java.util.function.Function;

/**
 * @author Carlos Correa
 */
public interface ContextDataInjectorBuilder {

	public ContextDataInjectorBuilder acceptLanguage(
		AcceptLanguage acceptLanguage);

	public ContextDataInjector build();

	public ContextDataInjectorBuilder company(Company company);

	public ContextDataInjectorBuilder expressionConvert(
		ExpressionConvert<?> expressionConvert);

	public ContextDataInjectorBuilder fallbackContextValueFunction(
		Function<Class<?>, Object> contextResolver);

	public ContextDataInjectorBuilder filterParserProvider(
		FilterParserProvider filterParserProvider);

	public ContextDataInjectorBuilder httpServletRequest(
		HttpServletRequest httpServletRequest);

	public ContextDataInjectorBuilder httpServletResponse(
		HttpServletResponse httpServletResponse);

	public ContextDataInjectorBuilder scopeChecker(Object scopeChecker);

	public ContextDataInjectorBuilder sortParserProvider(
		SortParserProvider sortParserProvider);

	public ContextDataInjectorBuilder uriInfo(UriInfo uriInfo);

	public ContextDataInjectorBuilder user(User user);

}