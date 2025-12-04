/**
 * SPDX-FileCopyrightText: (c) 2025 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.portal.vulcan.crud;

import com.liferay.portal.kernel.model.User;
import com.liferay.portal.vulcan.accept.language.AcceptLanguage;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import jakarta.ws.rs.core.UriInfo;

import org.osgi.annotation.versioning.ProviderType;

/**
 * @author Carlos Correa
 */
@ProviderType
public interface VulcanCRUDItemDelegateBuilder {

	public HttpServletRequestStepVulcanCRUDItemDelegateBuilder acceptLanguage(
		AcceptLanguage acceptLanguage);

	@ProviderType
	public interface BuildStepVulcanCRUDItemDelegateBuilder {

		public VulcanCRUDItemDelegate build() throws Exception;

	}

	@ProviderType
	public interface HttpServletRequestStepVulcanCRUDItemDelegateBuilder {

		public HttpServletResponseStepVulcanCRUDItemDelegateBuilder
			httpServletRequest(HttpServletRequest httpServletRequest);

	}

	@ProviderType
	public interface HttpServletResponseStepVulcanCRUDItemDelegateBuilder {

		public ScopeCheckerStepVulcanCRUDItemDelegateBuilder
			httpServletResponse(HttpServletResponse httpServletResponse);

	}

	@ProviderType
	public interface ScopeCheckerStepVulcanCRUDItemDelegateBuilder {

		public UriInfoStepVulcanCRUDItemDelegateBuilder scopeChecker(
			Object scopeChecker);

	}

	@ProviderType
	public interface UriInfoStepVulcanCRUDItemDelegateBuilder {

		public UserStepVulcanCRUDItemDelegateBuilder uriInfo(UriInfo uriInfo);

	}

	@ProviderType
	public interface UserStepVulcanCRUDItemDelegateBuilder {

		public BuildStepVulcanCRUDItemDelegateBuilder user(User user);

	}

}