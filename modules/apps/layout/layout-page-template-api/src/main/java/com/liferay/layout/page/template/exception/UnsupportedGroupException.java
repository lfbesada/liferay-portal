/**
 * SPDX-FileCopyrightText: (c) 2024 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.layout.page.template.exception;

import com.liferay.portal.kernel.exception.PortalException;

/**
 * @author Brian Wing Shun Chan
 */
public class UnsupportedGroupException extends PortalException {

	public UnsupportedGroupException() {
	}

	public UnsupportedGroupException(String msg) {
		super(msg);
	}

	public UnsupportedGroupException(String msg, Throwable throwable) {
		super(msg, throwable);
	}

	public UnsupportedGroupException(Throwable throwable) {
		super(throwable);
	}

}