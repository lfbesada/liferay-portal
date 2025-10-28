/**
 * SPDX-FileCopyrightText: (c) 2025 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.headless.admin.site.client.serdes.v1_0;

import com.liferay.headless.admin.site.client.dto.v1_0.LocalizableSelectFragmentConfigurationFieldValue;
import com.liferay.headless.admin.site.client.dto.v1_0.NonlocalizableSelectFragmentConfigurationFieldValue;
import com.liferay.headless.admin.site.client.dto.v1_0.SelectFragmentConfigurationFieldValue;
import com.liferay.headless.admin.site.client.json.BaseJSONParser;

import jakarta.annotation.Generated;

import java.util.Iterator;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.TreeMap;

/**
 * @author Rubén Pulido
 * @generated
 */
@Generated("")
public class SelectFragmentConfigurationFieldValueSerDes {

	public static SelectFragmentConfigurationFieldValue toDTO(String json) {
		SelectFragmentConfigurationFieldValueJSONParser
			selectFragmentConfigurationFieldValueJSONParser =
				new SelectFragmentConfigurationFieldValueJSONParser();

		return selectFragmentConfigurationFieldValueJSONParser.parseToDTO(json);
	}

	public static SelectFragmentConfigurationFieldValue[] toDTOs(String json) {
		SelectFragmentConfigurationFieldValueJSONParser
			selectFragmentConfigurationFieldValueJSONParser =
				new SelectFragmentConfigurationFieldValueJSONParser();

		return selectFragmentConfigurationFieldValueJSONParser.parseToDTOs(
			json);
	}

	public static String toJSON(
		SelectFragmentConfigurationFieldValue
			selectFragmentConfigurationFieldValue) {

		if (selectFragmentConfigurationFieldValue == null) {
			return "null";
		}

		SelectFragmentConfigurationFieldValue.LocalizableType localizableType =
			selectFragmentConfigurationFieldValue.getLocalizableType();

		if (localizableType != null) {
			String localizableTypeString = localizableType.toString();

			if (localizableTypeString.equals("Localizable")) {
				return LocalizableSelectFragmentConfigurationFieldValueSerDes.
					toJSON(
						(LocalizableSelectFragmentConfigurationFieldValue)
							selectFragmentConfigurationFieldValue);
			}

			if (localizableTypeString.equals("Nonlocalizable")) {
				return NonlocalizableSelectFragmentConfigurationFieldValueSerDes.
					toJSON(
						(NonlocalizableSelectFragmentConfigurationFieldValue)
							selectFragmentConfigurationFieldValue);
			}

			throw new IllegalArgumentException(
				"Unknown localizableType " + localizableTypeString);
		}
		else {
			throw new IllegalArgumentException(
				"Missing localizableType parameter");
		}
	}

	public static Map<String, Object> toMap(String json) {
		SelectFragmentConfigurationFieldValueJSONParser
			selectFragmentConfigurationFieldValueJSONParser =
				new SelectFragmentConfigurationFieldValueJSONParser();

		return selectFragmentConfigurationFieldValueJSONParser.parseToMap(json);
	}

	public static Map<String, String> toMap(
		SelectFragmentConfigurationFieldValue
			selectFragmentConfigurationFieldValue) {

		if (selectFragmentConfigurationFieldValue == null) {
			return null;
		}

		Map<String, String> map = new TreeMap<>();

		if (selectFragmentConfigurationFieldValue.getLocalizableType() ==
				null) {

			map.put("localizableType", null);
		}
		else {
			map.put(
				"localizableType",
				String.valueOf(
					selectFragmentConfigurationFieldValue.
						getLocalizableType()));
		}

		return map;
	}

	public static class SelectFragmentConfigurationFieldValueJSONParser
		extends BaseJSONParser<SelectFragmentConfigurationFieldValue> {

		@Override
		protected SelectFragmentConfigurationFieldValue createDTO() {
			return null;
		}

		@Override
		protected SelectFragmentConfigurationFieldValue[] createDTOArray(
			int size) {

			return new SelectFragmentConfigurationFieldValue[size];
		}

		@Override
		protected boolean parseMaps(String jsonParserFieldName) {
			if (Objects.equals(jsonParserFieldName, "localizableType")) {
				return false;
			}

			return false;
		}

		@Override
		public SelectFragmentConfigurationFieldValue parseToDTO(String json) {
			Map<String, Object> jsonMap = parseToMap(json);

			Object localizableType = jsonMap.get("localizableType");

			if (localizableType != null) {
				String localizableTypeString = localizableType.toString();

				if (localizableTypeString.equals("Localizable")) {
					return LocalizableSelectFragmentConfigurationFieldValue.
						toDTO(json);
				}

				if (localizableTypeString.equals("Nonlocalizable")) {
					return NonlocalizableSelectFragmentConfigurationFieldValue.
						toDTO(json);
				}

				throw new IllegalArgumentException(
					"Unknown localizableType " + localizableTypeString);
			}
			else {
				throw new IllegalArgumentException(
					"Missing localizableType parameter");
			}
		}

		@Override
		protected void setField(
			SelectFragmentConfigurationFieldValue
				selectFragmentConfigurationFieldValue,
			String jsonParserFieldName, Object jsonParserFieldValue) {

			if (Objects.equals(jsonParserFieldName, "localizableType")) {
				if (jsonParserFieldValue != null) {
					selectFragmentConfigurationFieldValue.setLocalizableType(
						SelectFragmentConfigurationFieldValue.LocalizableType.
							create((String)jsonParserFieldValue));
				}
			}
		}

	}

	private static String _escape(Object object) {
		String string = String.valueOf(object);

		for (String[] strings : BaseJSONParser.JSON_ESCAPE_STRINGS) {
			string = string.replace(strings[0], strings[1]);
		}

		return string;
	}

	private static String _toJSON(Map<String, ?> map) {
		StringBuilder sb = new StringBuilder("{");

		@SuppressWarnings("unchecked")
		Set set = map.entrySet();

		@SuppressWarnings("unchecked")
		Iterator<Map.Entry<String, ?>> iterator = set.iterator();

		while (iterator.hasNext()) {
			Map.Entry<String, ?> entry = iterator.next();

			sb.append("\"");
			sb.append(entry.getKey());
			sb.append("\": ");

			Object value = entry.getValue();

			sb.append(_toJSON(value));

			if (iterator.hasNext()) {
				sb.append(", ");
			}
		}

		sb.append("}");

		return sb.toString();
	}

	private static String _toJSON(Object value) {
		if (value == null) {
			return "null";
		}

		if (value instanceof Map) {
			return _toJSON((Map)value);
		}

		Class<?> clazz = value.getClass();

		if (clazz.isArray()) {
			StringBuilder sb = new StringBuilder("[");

			Object[] values = (Object[])value;

			for (int i = 0; i < values.length; i++) {
				sb.append(_toJSON(values[i]));

				if ((i + 1) < values.length) {
					sb.append(", ");
				}
			}

			sb.append("]");

			return sb.toString();
		}

		if (value instanceof String) {
			return "\"" + _escape(value) + "\"";
		}

		return String.valueOf(value);
	}

}