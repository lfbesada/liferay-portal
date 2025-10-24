/**
 * SPDX-FileCopyrightText: (c) 2025 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.headless.admin.site.client.serdes.v1_0;

import com.liferay.headless.admin.site.client.dto.v1_0.CategoryConfigurationFieldValue;
import com.liferay.headless.admin.site.client.dto.v1_0.CheckboxConfigurationFieldValue;
import com.liferay.headless.admin.site.client.dto.v1_0.CollectionConfigurationFieldValue;
import com.liferay.headless.admin.site.client.dto.v1_0.ColorPaletteConfigurationFieldValue;
import com.liferay.headless.admin.site.client.dto.v1_0.ColorPickerConfigurationFieldValue;
import com.liferay.headless.admin.site.client.dto.v1_0.ConfigurationFieldValue;
import com.liferay.headless.admin.site.client.dto.v1_0.ItemConfigurationFieldValue;
import com.liferay.headless.admin.site.client.dto.v1_0.LengthConfigurationFieldValue;
import com.liferay.headless.admin.site.client.dto.v1_0.NavigationMenuConfigurationFieldValue;
import com.liferay.headless.admin.site.client.dto.v1_0.SelectConfigurationFieldValue;
import com.liferay.headless.admin.site.client.dto.v1_0.TextConfigurationFieldValue;
import com.liferay.headless.admin.site.client.dto.v1_0.UrlConfigurationFieldValue;
import com.liferay.headless.admin.site.client.dto.v1_0.VideoConfigurationFieldValue;
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
public class ConfigurationFieldValueSerDes {

	public static ConfigurationFieldValue toDTO(String json) {
		ConfigurationFieldValueJSONParser configurationFieldValueJSONParser =
			new ConfigurationFieldValueJSONParser();

		return configurationFieldValueJSONParser.parseToDTO(json);
	}

	public static ConfigurationFieldValue[] toDTOs(String json) {
		ConfigurationFieldValueJSONParser configurationFieldValueJSONParser =
			new ConfigurationFieldValueJSONParser();

		return configurationFieldValueJSONParser.parseToDTOs(json);
	}

	public static String toJSON(
		ConfigurationFieldValue configurationFieldValue) {

		if (configurationFieldValue == null) {
			return "null";
		}

		ConfigurationFieldValue.Type type = configurationFieldValue.getType();

		if (type != null) {
			String typeString = type.toString();

			if (typeString.equals("Category")) {
				return CategoryConfigurationFieldValueSerDes.toJSON(
					(CategoryConfigurationFieldValue)configurationFieldValue);
			}

			if (typeString.equals("Checkbox")) {
				return CheckboxConfigurationFieldValueSerDes.toJSON(
					(CheckboxConfigurationFieldValue)configurationFieldValue);
			}

			if (typeString.equals("Collection")) {
				return CollectionConfigurationFieldValueSerDes.toJSON(
					(CollectionConfigurationFieldValue)configurationFieldValue);
			}

			if (typeString.equals("ColorPalette")) {
				return ColorPaletteConfigurationFieldValueSerDes.toJSON(
					(ColorPaletteConfigurationFieldValue)
						configurationFieldValue);
			}

			if (typeString.equals("ColorPicker")) {
				return ColorPickerConfigurationFieldValueSerDes.toJSON(
					(ColorPickerConfigurationFieldValue)
						configurationFieldValue);
			}

			if (typeString.equals("Item")) {
				return ItemConfigurationFieldValueSerDes.toJSON(
					(ItemConfigurationFieldValue)configurationFieldValue);
			}

			if (typeString.equals("Length")) {
				return LengthConfigurationFieldValueSerDes.toJSON(
					(LengthConfigurationFieldValue)configurationFieldValue);
			}

			if (typeString.equals("NavigationMenu")) {
				return NavigationMenuConfigurationFieldValueSerDes.toJSON(
					(NavigationMenuConfigurationFieldValue)
						configurationFieldValue);
			}

			if (typeString.equals("Select")) {
				return SelectConfigurationFieldValueSerDes.toJSON(
					(SelectConfigurationFieldValue)configurationFieldValue);
			}

			if (typeString.equals("Text")) {
				return TextConfigurationFieldValueSerDes.toJSON(
					(TextConfigurationFieldValue)configurationFieldValue);
			}

			if (typeString.equals("Url")) {
				return UrlConfigurationFieldValueSerDes.toJSON(
					(UrlConfigurationFieldValue)configurationFieldValue);
			}

			if (typeString.equals("Video")) {
				return VideoConfigurationFieldValueSerDes.toJSON(
					(VideoConfigurationFieldValue)configurationFieldValue);
			}

			throw new IllegalArgumentException("Unknown type " + typeString);
		}
		else {
			throw new IllegalArgumentException("Missing type parameter");
		}
	}

	public static Map<String, Object> toMap(String json) {
		ConfigurationFieldValueJSONParser configurationFieldValueJSONParser =
			new ConfigurationFieldValueJSONParser();

		return configurationFieldValueJSONParser.parseToMap(json);
	}

	public static Map<String, String> toMap(
		ConfigurationFieldValue configurationFieldValue) {

		if (configurationFieldValue == null) {
			return null;
		}

		Map<String, String> map = new TreeMap<>();

		if (configurationFieldValue.getType() == null) {
			map.put("type", null);
		}
		else {
			map.put("type", String.valueOf(configurationFieldValue.getType()));
		}

		return map;
	}

	public static class ConfigurationFieldValueJSONParser
		extends BaseJSONParser<ConfigurationFieldValue> {

		@Override
		protected ConfigurationFieldValue createDTO() {
			return null;
		}

		@Override
		protected ConfigurationFieldValue[] createDTOArray(int size) {
			return new ConfigurationFieldValue[size];
		}

		@Override
		protected boolean parseMaps(String jsonParserFieldName) {
			if (Objects.equals(jsonParserFieldName, "type")) {
				return false;
			}

			return false;
		}

		@Override
		public ConfigurationFieldValue parseToDTO(String json) {
			Map<String, Object> jsonMap = parseToMap(json);

			Object type = jsonMap.get("type");

			if (type != null) {
				String typeString = type.toString();

				if (typeString.equals("Category")) {
					return CategoryConfigurationFieldValue.toDTO(json);
				}

				if (typeString.equals("Checkbox")) {
					return CheckboxConfigurationFieldValue.toDTO(json);
				}

				if (typeString.equals("Collection")) {
					return CollectionConfigurationFieldValue.toDTO(json);
				}

				if (typeString.equals("ColorPalette")) {
					return ColorPaletteConfigurationFieldValue.toDTO(json);
				}

				if (typeString.equals("ColorPicker")) {
					return ColorPickerConfigurationFieldValue.toDTO(json);
				}

				if (typeString.equals("Item")) {
					return ItemConfigurationFieldValue.toDTO(json);
				}

				if (typeString.equals("Length")) {
					return LengthConfigurationFieldValue.toDTO(json);
				}

				if (typeString.equals("NavigationMenu")) {
					return NavigationMenuConfigurationFieldValue.toDTO(json);
				}

				if (typeString.equals("Select")) {
					return SelectConfigurationFieldValue.toDTO(json);
				}

				if (typeString.equals("Text")) {
					return TextConfigurationFieldValue.toDTO(json);
				}

				if (typeString.equals("Url")) {
					return UrlConfigurationFieldValue.toDTO(json);
				}

				if (typeString.equals("Video")) {
					return VideoConfigurationFieldValue.toDTO(json);
				}

				throw new IllegalArgumentException(
					"Unknown type " + typeString);
			}
			else {
				throw new IllegalArgumentException("Missing type parameter");
			}
		}

		@Override
		protected void setField(
			ConfigurationFieldValue configurationFieldValue,
			String jsonParserFieldName, Object jsonParserFieldValue) {

			if (Objects.equals(jsonParserFieldName, "type")) {
				if (jsonParserFieldValue != null) {
					configurationFieldValue.setType(
						ConfigurationFieldValue.Type.create(
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