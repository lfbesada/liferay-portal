/**
 * SPDX-FileCopyrightText: (c) 2025 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.headless.admin.site.client.serdes.v1_0;

import com.liferay.headless.admin.site.client.dto.v1_0.FragmentConfigurationFieldInstance;
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
public class FragmentConfigurationFieldInstanceSerDes {

	public static FragmentConfigurationFieldInstance toDTO(String json) {
		FragmentConfigurationFieldInstanceJSONParser
			fragmentConfigurationFieldInstanceJSONParser =
				new FragmentConfigurationFieldInstanceJSONParser();

		return fragmentConfigurationFieldInstanceJSONParser.parseToDTO(json);
	}

	public static FragmentConfigurationFieldInstance[] toDTOs(String json) {
		FragmentConfigurationFieldInstanceJSONParser
			fragmentConfigurationFieldInstanceJSONParser =
				new FragmentConfigurationFieldInstanceJSONParser();

		return fragmentConfigurationFieldInstanceJSONParser.parseToDTOs(json);
	}

	public static String toJSON(
		FragmentConfigurationFieldInstance fragmentConfigurationFieldInstance) {

		if (fragmentConfigurationFieldInstance == null) {
			return "null";
		}

		FragmentConfigurationFieldInstance.FragmentConfigurationFieldType
			fragmentConfigurationFieldType =
				fragmentConfigurationFieldInstance.
					getFragmentConfigurationFieldType();

		if (fragmentConfigurationFieldType != null) {
			String fragmentConfigurationFieldTypeString =
				fragmentConfigurationFieldType.toString();

			if (fragmentConfigurationFieldTypeString.equals("Select")) {
				return SelectFragmentConfigurationFieldValueSerDes.toJSON(
					(SelectFragmentConfigurationFieldValue)
						fragmentConfigurationFieldInstance);
			}

			throw new IllegalArgumentException(
				"Unknown fragmentConfigurationFieldType " +
					fragmentConfigurationFieldTypeString);
		}
		else {
			throw new IllegalArgumentException(
				"Missing fragmentConfigurationFieldType parameter");
		}
	}

	public static Map<String, Object> toMap(String json) {
		FragmentConfigurationFieldInstanceJSONParser
			fragmentConfigurationFieldInstanceJSONParser =
				new FragmentConfigurationFieldInstanceJSONParser();

		return fragmentConfigurationFieldInstanceJSONParser.parseToMap(json);
	}

	public static Map<String, String> toMap(
		FragmentConfigurationFieldInstance fragmentConfigurationFieldInstance) {

		if (fragmentConfigurationFieldInstance == null) {
			return null;
		}

		Map<String, String> map = new TreeMap<>();

		if (fragmentConfigurationFieldInstance.
				getFragmentConfigurationFieldType() == null) {

			map.put("fragmentConfigurationFieldType", null);
		}
		else {
			map.put(
				"fragmentConfigurationFieldType",
				String.valueOf(
					fragmentConfigurationFieldInstance.
						getFragmentConfigurationFieldType()));
		}

		if (fragmentConfigurationFieldInstance.getName() == null) {
			map.put("name", null);
		}
		else {
			map.put(
				"name",
				String.valueOf(fragmentConfigurationFieldInstance.getName()));
		}

		return map;
	}

	public static class FragmentConfigurationFieldInstanceJSONParser
		extends BaseJSONParser<FragmentConfigurationFieldInstance> {

		@Override
		protected FragmentConfigurationFieldInstance createDTO() {
			return null;
		}

		@Override
		protected FragmentConfigurationFieldInstance[] createDTOArray(
			int size) {

			return new FragmentConfigurationFieldInstance[size];
		}

		@Override
		protected boolean parseMaps(String jsonParserFieldName) {
			if (Objects.equals(
					jsonParserFieldName, "fragmentConfigurationFieldType")) {

				return false;
			}
			else if (Objects.equals(jsonParserFieldName, "name")) {
				return false;
			}

			return false;
		}

		@Override
		public FragmentConfigurationFieldInstance parseToDTO(String json) {
			Map<String, Object> jsonMap = parseToMap(json);

			Object fragmentConfigurationFieldType = jsonMap.get(
				"fragmentConfigurationFieldType");

			if (fragmentConfigurationFieldType != null) {
				String fragmentConfigurationFieldTypeString =
					fragmentConfigurationFieldType.toString();

				if (fragmentConfigurationFieldTypeString.equals("Select")) {
					return SelectFragmentConfigurationFieldValue.toDTO(json);
				}

				throw new IllegalArgumentException(
					"Unknown fragmentConfigurationFieldType " +
						fragmentConfigurationFieldTypeString);
			}
			else {
				throw new IllegalArgumentException(
					"Missing fragmentConfigurationFieldType parameter");
			}
		}

		@Override
		protected void setField(
			FragmentConfigurationFieldInstance
				fragmentConfigurationFieldInstance,
			String jsonParserFieldName, Object jsonParserFieldValue) {

			if (Objects.equals(
					jsonParserFieldName, "fragmentConfigurationFieldType")) {

				if (jsonParserFieldValue != null) {
					fragmentConfigurationFieldInstance.
						setFragmentConfigurationFieldType(
							FragmentConfigurationFieldInstance.
								FragmentConfigurationFieldType.create(
									(String)jsonParserFieldValue));
				}
			}
			else if (Objects.equals(jsonParserFieldName, "name")) {
				if (jsonParserFieldValue != null) {
					fragmentConfigurationFieldInstance.setName(
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