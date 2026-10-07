/**
 * SPDX-FileCopyrightText: (c) 2026 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.headless.admin.site.resource.v1_0.test.util;

import com.liferay.petra.function.UnsafeSupplier;
import com.liferay.portal.configuration.test.util.ConfigurationTemporarySwapper;

import java.util.Dictionary;

/**
 * @author Lourdes Fernández Besada
 */
public class ConfigurationTemporarySwapperUtil {

	public static <T> T swap(
			String pid, Dictionary<String, Object> properties,
			UnsafeSupplier<T, Exception> unsafeSupplier)
		throws Exception {

		try (ConfigurationTemporarySwapper configurationTemporarySwapper =
				new ConfigurationTemporarySwapper(pid, properties)) {

			return unsafeSupplier.get();
		}
	}

}