/**
 * SPDX-FileCopyrightText: (c) 2025 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.headless.admin.site.client.serdes.v1_0;

import com.liferay.headless.admin.site.client.dto.v1_0.SelectFragmentConfigurationFieldInstance;
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
public class SelectFragmentConfigurationFieldInstanceSerDes {

	public static SelectFragmentConfigurationFieldInstance toDTO(String json) {
		SelectFragmentConfigurationFieldInstanceJSONParser
			selectFragmentConfigurationFieldInstanceJSONParser =
				new SelectFragmentConfigurationFieldInstanceJSONParser();

		return selectFragmentConfigurationFieldInstanceJSONParser.parseToDTO(
			json);
	}

	public static SelectFragmentConfigurationFieldInstance[] toDTOs(
		String json) {

		SelectFragmentConfigurationFieldInstanceJSONParser
			selectFragmentConfigurationFieldInstanceJSONParser =
				new SelectFragmentConfigurationFieldInstanceJSONParser();

		return selectFragmentConfigurationFieldInstanceJSONParser.parseToDTOs(
			json);
	}

	public static String toJSON(
		SelectFragmentConfigurationFieldInstance
			selectFragmentConfigurationFieldInstance) {

		if (selectFragmentConfigurationFieldInstance == null) {
			return "null";
		}

		StringBuilder sb = new StringBuilder();

		sb.append("{");

		if (selectFragmentConfigurationFieldInstance.
				getSelectFragmentConfigurationFieldValue() != null) {

			if (sb.length() > 1) {
				sb.append(", ");
			}

			sb.append("\"selectFragmentConfigurationFieldValue\": ");

			sb.append(
				String.valueOf(
					selectFragmentConfigurationFieldInstance.
						getSelectFragmentConfigurationFieldValue()));
		}

		if (selectFragmentConfigurationFieldInstance.
				getFragmentConfigurationFieldType() != null) {

			if (sb.length() > 1) {
				sb.append(", ");
			}

			sb.append("\"fragmentConfigurationFieldType\": ");

			sb.append("\"");

			sb.append(
				selectFragmentConfigurationFieldInstance.
					getFragmentConfigurationFieldType());

			sb.append("\"");
		}

		if (selectFragmentConfigurationFieldInstance.getName() != null) {
			if (sb.length() > 1) {
				sb.append(", ");
			}

			sb.append("\"name\": ");

			sb.append("\"");

			sb.append(
				_escape(selectFragmentConfigurationFieldInstance.getName()));

			sb.append("\"");
		}

		sb.append("}");

		return sb.toString();
	}

	public static Map<String, Object> toMap(String json) {
		SelectFragmentConfigurationFieldInstanceJSONParser
			selectFragmentConfigurationFieldInstanceJSONParser =
				new SelectFragmentConfigurationFieldInstanceJSONParser();

		return selectFragmentConfigurationFieldInstanceJSONParser.parseToMap(
			json);
	}

	public static Map<String, String> toMap(
		SelectFragmentConfigurationFieldInstance
			selectFragmentConfigurationFieldInstance) {

		if (selectFragmentConfigurationFieldInstance == null) {
			return null;
		}

		Map<String, String> map = new TreeMap<>();

		if (selectFragmentConfigurationFieldInstance.
				getSelectFragmentConfigurationFieldValue() == null) {

			map.put("selectFragmentConfigurationFieldValue", null);
		}
		else {
			map.put(
				"selectFragmentConfigurationFieldValue",
				String.valueOf(
					selectFragmentConfigurationFieldInstance.
						getSelectFragmentConfigurationFieldValue()));
		}

		if (selectFragmentConfigurationFieldInstance.
				getFragmentConfigurationFieldType() == null) {

			map.put("fragmentConfigurationFieldType", null);
		}
		else {
			map.put(
				"fragmentConfigurationFieldType",
				String.valueOf(
					selectFragmentConfigurationFieldInstance.
						getFragmentConfigurationFieldType()));
		}

		if (selectFragmentConfigurationFieldInstance.getName() == null) {
			map.put("name", null);
		}
		else {
			map.put(
				"name",
				String.valueOf(
					selectFragmentConfigurationFieldInstance.getName()));
		}

		return map;
	}

	public static class SelectFragmentConfigurationFieldInstanceJSONParser
		extends BaseJSONParser<SelectFragmentConfigurationFieldInstance> {

		@Override
		protected SelectFragmentConfigurationFieldInstance createDTO() {
			return new SelectFragmentConfigurationFieldInstance();
		}

		@Override
		protected SelectFragmentConfigurationFieldInstance[] createDTOArray(
			int size) {

			return new SelectFragmentConfigurationFieldInstance[size];
		}

		@Override
		protected boolean parseMaps(String jsonParserFieldName) {
			if (Objects.equals(
					jsonParserFieldName,
					"selectFragmentConfigurationFieldValue")) {

				return false;
			}
			else if (Objects.equals(
						jsonParserFieldName,
						"fragmentConfigurationFieldType")) {

				return false;
			}
			else if (Objects.equals(jsonParserFieldName, "name")) {
				return false;
			}

			return false;
		}

		@Override
		protected void setField(
			SelectFragmentConfigurationFieldInstance
				selectFragmentConfigurationFieldInstance,
			String jsonParserFieldName, Object jsonParserFieldValue) {

			if (Objects.equals(
					jsonParserFieldName,
					"selectFragmentConfigurationFieldValue")) {

				if (jsonParserFieldValue != null) {
					selectFragmentConfigurationFieldInstance.
						setSelectFragmentConfigurationFieldValue(
							SelectFragmentConfigurationFieldValueSerDes.toDTO(
								(String)jsonParserFieldValue));
				}
			}
			else if (Objects.equals(
						jsonParserFieldName,
						"fragmentConfigurationFieldType")) {

				if (jsonParserFieldValue != null) {
					selectFragmentConfigurationFieldInstance.
						setFragmentConfigurationFieldType(
							SelectFragmentConfigurationFieldInstance.
								FragmentConfigurationFieldType.create(
									(String)jsonParserFieldValue));
				}
			}
			else if (Objects.equals(jsonParserFieldName, "name")) {
				if (jsonParserFieldValue != null) {
					selectFragmentConfigurationFieldInstance.setName(
						(String)jsonParserFieldValue);
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