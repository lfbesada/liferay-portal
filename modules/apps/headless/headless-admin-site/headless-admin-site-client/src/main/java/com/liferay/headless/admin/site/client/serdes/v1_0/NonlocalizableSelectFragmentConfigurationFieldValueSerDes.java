/**
 * SPDX-FileCopyrightText: (c) 2025 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.headless.admin.site.client.serdes.v1_0;

import com.liferay.headless.admin.site.client.dto.v1_0.NonlocalizableSelectFragmentConfigurationFieldValue;
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
public class NonlocalizableSelectFragmentConfigurationFieldValueSerDes {

	public static NonlocalizableSelectFragmentConfigurationFieldValue toDTO(
		String json) {

		NonlocalizableSelectFragmentConfigurationFieldValueJSONParser
			nonlocalizableSelectFragmentConfigurationFieldValueJSONParser =
				new NonlocalizableSelectFragmentConfigurationFieldValueJSONParser();

		return nonlocalizableSelectFragmentConfigurationFieldValueJSONParser.
			parseToDTO(json);
	}

	public static NonlocalizableSelectFragmentConfigurationFieldValue[] toDTOs(
		String json) {

		NonlocalizableSelectFragmentConfigurationFieldValueJSONParser
			nonlocalizableSelectFragmentConfigurationFieldValueJSONParser =
				new NonlocalizableSelectFragmentConfigurationFieldValueJSONParser();

		return nonlocalizableSelectFragmentConfigurationFieldValueJSONParser.
			parseToDTOs(json);
	}

	public static String toJSON(
		NonlocalizableSelectFragmentConfigurationFieldValue
			nonlocalizableSelectFragmentConfigurationFieldValue) {

		if (nonlocalizableSelectFragmentConfigurationFieldValue == null) {
			return "null";
		}

		StringBuilder sb = new StringBuilder();

		sb.append("{");

		if (nonlocalizableSelectFragmentConfigurationFieldValue.getValue() !=
				null) {

			if (sb.length() > 1) {
				sb.append(", ");
			}

			sb.append("\"value\": ");

			sb.append("\"");

			sb.append(
				_escape(
					nonlocalizableSelectFragmentConfigurationFieldValue.
						getValue()));

			sb.append("\"");
		}

		if (nonlocalizableSelectFragmentConfigurationFieldValue.
				getLocalizableType() != null) {

			if (sb.length() > 1) {
				sb.append(", ");
			}

			sb.append("\"localizableType\": ");

			sb.append("\"");

			sb.append(
				nonlocalizableSelectFragmentConfigurationFieldValue.
					getLocalizableType());

			sb.append("\"");
		}

		sb.append("}");

		return sb.toString();
	}

	public static Map<String, Object> toMap(String json) {
		NonlocalizableSelectFragmentConfigurationFieldValueJSONParser
			nonlocalizableSelectFragmentConfigurationFieldValueJSONParser =
				new NonlocalizableSelectFragmentConfigurationFieldValueJSONParser();

		return nonlocalizableSelectFragmentConfigurationFieldValueJSONParser.
			parseToMap(json);
	}

	public static Map<String, String> toMap(
		NonlocalizableSelectFragmentConfigurationFieldValue
			nonlocalizableSelectFragmentConfigurationFieldValue) {

		if (nonlocalizableSelectFragmentConfigurationFieldValue == null) {
			return null;
		}

		Map<String, String> map = new TreeMap<>();

		if (nonlocalizableSelectFragmentConfigurationFieldValue.getValue() ==
				null) {

			map.put("value", null);
		}
		else {
			map.put(
				"value",
				String.valueOf(
					nonlocalizableSelectFragmentConfigurationFieldValue.
						getValue()));
		}

		if (nonlocalizableSelectFragmentConfigurationFieldValue.
				getLocalizableType() == null) {

			map.put("localizableType", null);
		}
		else {
			map.put(
				"localizableType",
				String.valueOf(
					nonlocalizableSelectFragmentConfigurationFieldValue.
						getLocalizableType()));
		}

		return map;
	}

	public static class
		NonlocalizableSelectFragmentConfigurationFieldValueJSONParser
			extends BaseJSONParser
				<NonlocalizableSelectFragmentConfigurationFieldValue> {

		@Override
		protected NonlocalizableSelectFragmentConfigurationFieldValue
			createDTO() {

			return new NonlocalizableSelectFragmentConfigurationFieldValue();
		}

		@Override
		protected NonlocalizableSelectFragmentConfigurationFieldValue[]
			createDTOArray(int size) {

			return
				new NonlocalizableSelectFragmentConfigurationFieldValue[size];
		}

		@Override
		protected boolean parseMaps(String jsonParserFieldName) {
			if (Objects.equals(jsonParserFieldName, "value")) {
				return false;
			}
			else if (Objects.equals(jsonParserFieldName, "localizableType")) {
				return false;
			}

			return false;
		}

		@Override
		protected void setField(
			NonlocalizableSelectFragmentConfigurationFieldValue
				nonlocalizableSelectFragmentConfigurationFieldValue,
			String jsonParserFieldName, Object jsonParserFieldValue) {

			if (Objects.equals(jsonParserFieldName, "value")) {
				if (jsonParserFieldValue != null) {
					nonlocalizableSelectFragmentConfigurationFieldValue.
						setValue((String)jsonParserFieldValue);
				}
			}
			else if (Objects.equals(jsonParserFieldName, "localizableType")) {
				if (jsonParserFieldValue != null) {
					nonlocalizableSelectFragmentConfigurationFieldValue.
						setLocalizableType(
							NonlocalizableSelectFragmentConfigurationFieldValue.
								LocalizableType.create(
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