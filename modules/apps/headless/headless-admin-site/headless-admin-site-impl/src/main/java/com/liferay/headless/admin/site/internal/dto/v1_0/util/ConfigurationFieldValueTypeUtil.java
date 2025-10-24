/**
 * SPDX-FileCopyrightText: (c) 2025 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.headless.admin.site.internal.dto.v1_0.util;

import com.liferay.headless.admin.site.dto.v1_0.ConfigurationFieldValue;
import com.liferay.portal.kernel.util.HashMapBuilder;

import java.util.Map;
import java.util.Objects;

/**
 * @author Lourdes Fernández Besada
 */
public class ConfigurationFieldValueTypeUtil {

	public static ConfigurationFieldValue.Type toExternalType(
		String internalType) {

		if (_internalToExternalValuesMap.containsKey(internalType)) {
			return _internalToExternalValuesMap.get(internalType);
		}

		throw new UnsupportedOperationException();
	}

	public static String toInternalType(
		ConfigurationFieldValue.Type externalType) {

		for (Map.Entry<String, ConfigurationFieldValue.Type> entry :
				_internalToExternalValuesMap.entrySet()) {

			if (Objects.equals(externalType, entry.getValue())) {
				return entry.getKey();
			}
		}

		throw new UnsupportedOperationException();
	}

	private static final Map<String, ConfigurationFieldValue.Type>
		_internalToExternalValuesMap = HashMapBuilder.put(
			"categoryTreeNodeSelector", ConfigurationFieldValue.Type.CATEGORY
		).put(
			"checkbox", ConfigurationFieldValue.Type.CHECKBOX
		).put(
			"collectionSelector", ConfigurationFieldValue.Type.COLLECTION
		).put(
			"colorPalette", ConfigurationFieldValue.Type.COLOR_PALETTE
		).put(
			"colorPicker", ConfigurationFieldValue.Type.COLOR_PICKER
		).put(
			"itemSelector", ConfigurationFieldValue.Type.ITEM
		).put(
			"length", ConfigurationFieldValue.Type.LENGTH
		).put(
			"navigationMenuSelector",
			ConfigurationFieldValue.Type.NAVIGATION_MENU
		).put(
			"select", ConfigurationFieldValue.Type.SELECT
		).put(
			"text", ConfigurationFieldValue.Type.TEXT
		).put(
			"url", ConfigurationFieldValue.Type.URL
		).put(
			"videoSelector", ConfigurationFieldValue.Type.VIDEO
		).build();

}