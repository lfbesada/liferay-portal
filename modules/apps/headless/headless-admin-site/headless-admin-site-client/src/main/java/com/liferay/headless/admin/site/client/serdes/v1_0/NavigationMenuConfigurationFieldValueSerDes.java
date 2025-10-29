/**
 * SPDX-FileCopyrightText: (c) 2025 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.headless.admin.site.client.serdes.v1_0;

import com.liferay.headless.admin.site.client.dto.v1_0.NavigationMenuConfigurationFieldValue;
import com.liferay.headless.admin.site.client.dto.v1_0.NavigationMenuValue;
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
public class NavigationMenuConfigurationFieldValueSerDes {

	public static NavigationMenuConfigurationFieldValue toDTO(String json) {
		NavigationMenuConfigurationFieldValueJSONParser
			navigationMenuConfigurationFieldValueJSONParser =
				new NavigationMenuConfigurationFieldValueJSONParser();

		return navigationMenuConfigurationFieldValueJSONParser.parseToDTO(json);
	}

	public static NavigationMenuConfigurationFieldValue[] toDTOs(String json) {
		NavigationMenuConfigurationFieldValueJSONParser
			navigationMenuConfigurationFieldValueJSONParser =
				new NavigationMenuConfigurationFieldValueJSONParser();

		return navigationMenuConfigurationFieldValueJSONParser.parseToDTOs(
			json);
	}

	public static String toJSON(
		NavigationMenuConfigurationFieldValue
			navigationMenuConfigurationFieldValue) {

		if (navigationMenuConfigurationFieldValue == null) {
			return "null";
		}

		StringBuilder sb = new StringBuilder();

		sb.append("{");

		if (navigationMenuConfigurationFieldValue.getValue() != null) {
			if (sb.length() > 1) {
				sb.append(", ");
			}

			sb.append("\"value\": ");

			sb.append(
				String.valueOf(
					navigationMenuConfigurationFieldValue.getValue()));
		}

		if (navigationMenuConfigurationFieldValue.getValue_i18n() != null) {
			if (sb.length() > 1) {
				sb.append(", ");
			}

			sb.append("\"value_i18n\": ");

			sb.append(
				_toJSON(navigationMenuConfigurationFieldValue.getValue_i18n()));
		}

		if (navigationMenuConfigurationFieldValue.getType() != null) {
			if (sb.length() > 1) {
				sb.append(", ");
			}

			sb.append("\"type\": ");

			sb.append("\"");

			sb.append(navigationMenuConfigurationFieldValue.getType());

			sb.append("\"");
		}

		sb.append("}");

		return sb.toString();
	}

	public static Map<String, Object> toMap(String json) {
		NavigationMenuConfigurationFieldValueJSONParser
			navigationMenuConfigurationFieldValueJSONParser =
				new NavigationMenuConfigurationFieldValueJSONParser();

		return navigationMenuConfigurationFieldValueJSONParser.parseToMap(json);
	}

	public static Map<String, String> toMap(
		NavigationMenuConfigurationFieldValue
			navigationMenuConfigurationFieldValue) {

		if (navigationMenuConfigurationFieldValue == null) {
			return null;
		}

		Map<String, String> map = new TreeMap<>();

		if (navigationMenuConfigurationFieldValue.getValue() == null) {
			map.put("value", null);
		}
		else {
			map.put(
				"value",
				String.valueOf(
					navigationMenuConfigurationFieldValue.getValue()));
		}

		if (navigationMenuConfigurationFieldValue.getValue_i18n() == null) {
			map.put("value_i18n", null);
		}
		else {
			map.put(
				"value_i18n",
				String.valueOf(
					navigationMenuConfigurationFieldValue.getValue_i18n()));
		}

		if (navigationMenuConfigurationFieldValue.getType() == null) {
			map.put("type", null);
		}
		else {
			map.put(
				"type",
				String.valueOf(
					navigationMenuConfigurationFieldValue.getType()));
		}

		return map;
	}

	public static class NavigationMenuConfigurationFieldValueJSONParser
		extends BaseJSONParser<NavigationMenuConfigurationFieldValue> {

		@Override
		protected NavigationMenuConfigurationFieldValue createDTO() {
			return new NavigationMenuConfigurationFieldValue();
		}

		@Override
		protected NavigationMenuConfigurationFieldValue[] createDTOArray(
			int size) {

			return new NavigationMenuConfigurationFieldValue[size];
		}

		@Override
		protected boolean parseMaps(String jsonParserFieldName) {
			if (Objects.equals(jsonParserFieldName, "value")) {
				return false;
			}
			else if (Objects.equals(jsonParserFieldName, "value_i18n")) {
				return true;
			}
			else if (Objects.equals(jsonParserFieldName, "type")) {
				return false;
			}

			return false;
		}

		@Override
		protected void setField(
			NavigationMenuConfigurationFieldValue
				navigationMenuConfigurationFieldValue,
			String jsonParserFieldName, Object jsonParserFieldValue) {

			if (Objects.equals(jsonParserFieldName, "value")) {
				if (jsonParserFieldValue != null) {
					navigationMenuConfigurationFieldValue.setValue(
						NavigationMenuValueSerDes.toDTO(
							(String)jsonParserFieldValue));
				}
			}
			else if (Objects.equals(jsonParserFieldName, "value_i18n")) {
				if (jsonParserFieldValue != null) {
					navigationMenuConfigurationFieldValue.setValue_i18n(
						(Map<String, NavigationMenuValue>)jsonParserFieldValue);
				}
			}
			else if (Objects.equals(jsonParserFieldName, "type")) {
				if (jsonParserFieldValue != null) {
					navigationMenuConfigurationFieldValue.setType(
						NavigationMenuConfigurationFieldValue.Type.create(
							(String)jsonParserFieldValue));
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