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

package com.liferay.layout.type.controller.content.internal.display.context;

import com.liferay.petra.string.StringBundler;
import com.liferay.portal.kernel.util.ParamUtil;
import com.liferay.portal.kernel.util.Portal;
import com.liferay.portal.kernel.util.Validator;

import javax.servlet.http.HttpServletRequest;

/**
 * @author Lourdes Fernández Besada
 */
public class ContentLayoutLockedDisplayContext {

	public ContentLayoutLockedDisplayContext(
		String contextPath, HttpServletRequest httpServletRequest,
		Portal portal) {

		_contextPath = contextPath;
		_httpServletRequest = httpServletRequest;
		_portal = portal;
	}

	public String getBackURL() {
		if (_backURL != null) {
			return _backURL;
		}

		HttpServletRequest originalHttpServletRequest =
			_portal.getOriginalServletRequest(_httpServletRequest);

		_backURL = ParamUtil.getString(
			originalHttpServletRequest, "p_l_back_url");

		return _backURL;
	}

	public String getImagesPath() {
		return StringBundler.concat(
			_portal.getPathProxy(), _contextPath, "/images");
	}

	public boolean isShowGoBackButton() {
		if (Validator.isNotNull(getBackURL())) {
			return true;
		}

		return false;
	}

	private String _backURL;
	private final String _contextPath;
	private final HttpServletRequest _httpServletRequest;
	private final Portal _portal;

}