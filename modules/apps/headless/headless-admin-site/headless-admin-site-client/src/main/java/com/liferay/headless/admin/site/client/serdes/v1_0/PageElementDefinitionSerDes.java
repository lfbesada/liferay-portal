/**
 * SPDX-FileCopyrightText: (c) 2025 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.headless.admin.site.client.serdes.v1_0;

import com.liferay.headless.admin.site.client.dto.v1_0.PageCollectionDefinition;
import com.liferay.headless.admin.site.client.dto.v1_0.PageCollectionItemDefinition;
import com.liferay.headless.admin.site.client.dto.v1_0.PageColumnDefinition;
import com.liferay.headless.admin.site.client.dto.v1_0.PageContainerDefinition;
import com.liferay.headless.admin.site.client.dto.v1_0.PageDropZoneDefinition;
import com.liferay.headless.admin.site.client.dto.v1_0.PageElementDefinition;
import com.liferay.headless.admin.site.client.dto.v1_0.PageFormDefinition;
import com.liferay.headless.admin.site.client.dto.v1_0.PageFormStepContainerDefinition;
import com.liferay.headless.admin.site.client.dto.v1_0.PageFormStepDefinition;
import com.liferay.headless.admin.site.client.dto.v1_0.PageFragmentCompositionInstanceDefinition;
import com.liferay.headless.admin.site.client.dto.v1_0.PageFragmentDropZoneDefinition;
import com.liferay.headless.admin.site.client.dto.v1_0.PageFragmentInstanceDefinition;
import com.liferay.headless.admin.site.client.dto.v1_0.PageRowDefinition;
import com.liferay.headless.admin.site.client.dto.v1_0.PageWidgetInstanceDefinition;
import com.liferay.headless.admin.site.client.json.BaseJSONParser;

import java.util.Iterator;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.TreeMap;

import javax.annotation.Generated;

/**
 * @author Rubén Pulido
 * @generated
 */
@Generated("")
public class PageElementDefinitionSerDes {

	public static PageElementDefinition toDTO(String json) {
		PageElementDefinitionJSONParser pageElementDefinitionJSONParser =
			new PageElementDefinitionJSONParser();

		return pageElementDefinitionJSONParser.parseToDTO(json);
	}

	public static PageElementDefinition[] toDTOs(String json) {
		PageElementDefinitionJSONParser pageElementDefinitionJSONParser =
			new PageElementDefinitionJSONParser();

		return pageElementDefinitionJSONParser.parseToDTOs(json);
	}

	public static String toJSON(PageElementDefinition pageElementDefinition) {
		if (pageElementDefinition == null) {
			return "null";
		}

		PageElementDefinition.Type type = pageElementDefinition.getType();

		if (type != null) {
			String typeString = type.toString();

			if (typeString.equals("CollectionDefinition")) {
				return PageCollectionDefinitionSerDes.toJSON(
					(PageCollectionDefinition)pageElementDefinition);
			}

			if (typeString.equals("CollectionItemDefinition")) {
				return PageCollectionItemDefinitionSerDes.toJSON(
					(PageCollectionItemDefinition)pageElementDefinition);
			}

			if (typeString.equals("ColumnDefinition")) {
				return PageColumnDefinitionSerDes.toJSON(
					(PageColumnDefinition)pageElementDefinition);
			}

			if (typeString.equals("ContainerDefinition")) {
				return PageContainerDefinitionSerDes.toJSON(
					(PageContainerDefinition)pageElementDefinition);
			}

			if (typeString.equals("DropZoneDefinition")) {
				return PageDropZoneDefinitionSerDes.toJSON(
					(PageDropZoneDefinition)pageElementDefinition);
			}

			if (typeString.equals("FormDefinition")) {
				return PageFormDefinitionSerDes.toJSON(
					(PageFormDefinition)pageElementDefinition);
			}

			if (typeString.equals("FormStepContainerDefinition")) {
				return PageFormStepContainerDefinitionSerDes.toJSON(
					(PageFormStepContainerDefinition)pageElementDefinition);
			}

			if (typeString.equals("FormStepDefinition")) {
				return PageFormStepDefinitionSerDes.toJSON(
					(PageFormStepDefinition)pageElementDefinition);
			}

			if (typeString.equals("FragmentCompositionDefinition")) {
				return PageFragmentCompositionInstanceDefinitionSerDes.toJSON(
					(PageFragmentCompositionInstanceDefinition)
						pageElementDefinition);
			}

			if (typeString.equals("FragmentDefinition")) {
				return PageFragmentInstanceDefinitionSerDes.toJSON(
					(PageFragmentInstanceDefinition)pageElementDefinition);
			}

			if (typeString.equals("FragmentDropZoneDefinition")) {
				return PageFragmentDropZoneDefinitionSerDes.toJSON(
					(PageFragmentDropZoneDefinition)pageElementDefinition);
			}

			if (typeString.equals("RowDefinition")) {
				return PageRowDefinitionSerDes.toJSON(
					(PageRowDefinition)pageElementDefinition);
			}

			if (typeString.equals("WidgetDefinition")) {
				return PageWidgetInstanceDefinitionSerDes.toJSON(
					(PageWidgetInstanceDefinition)pageElementDefinition);
			}

			throw new IllegalArgumentException("Unknown type " + typeString);
		}
		else {
			throw new IllegalArgumentException("Missing type parameter");
		}
	}

	public static Map<String, Object> toMap(String json) {
		PageElementDefinitionJSONParser pageElementDefinitionJSONParser =
			new PageElementDefinitionJSONParser();

		return pageElementDefinitionJSONParser.parseToMap(json);
	}

	public static Map<String, String> toMap(
		PageElementDefinition pageElementDefinition) {

		if (pageElementDefinition == null) {
			return null;
		}

		Map<String, String> map = new TreeMap<>();

		if (pageElementDefinition.getType() == null) {
			map.put("type", null);
		}
		else {
			map.put("type", String.valueOf(pageElementDefinition.getType()));
		}

		return map;
	}

	public static class PageElementDefinitionJSONParser
		extends BaseJSONParser<PageElementDefinition> {

		@Override
		protected PageElementDefinition createDTO() {
			return null;
		}

		@Override
		protected PageElementDefinition[] createDTOArray(int size) {
			return new PageElementDefinition[size];
		}

		@Override
		protected boolean parseMaps(String jsonParserFieldName) {
			if (Objects.equals(jsonParserFieldName, "type")) {
				return false;
			}

			return false;
		}

		@Override
		public PageElementDefinition parseToDTO(String json) {
			Map<String, Object> jsonMap = parseToMap(json);

			Object type = jsonMap.get("type");

			if (type != null) {
				String typeString = type.toString();

				if (typeString.equals("CollectionDefinition")) {
					return PageCollectionDefinition.toDTO(json);
				}

				if (typeString.equals("CollectionItemDefinition")) {
					return PageCollectionItemDefinition.toDTO(json);
				}

				if (typeString.equals("ColumnDefinition")) {
					return PageColumnDefinition.toDTO(json);
				}

				if (typeString.equals("ContainerDefinition")) {
					return PageContainerDefinition.toDTO(json);
				}

				if (typeString.equals("DropZoneDefinition")) {
					return PageDropZoneDefinition.toDTO(json);
				}

				if (typeString.equals("FormDefinition")) {
					return PageFormDefinition.toDTO(json);
				}

				if (typeString.equals("FormStepContainerDefinition")) {
					return PageFormStepContainerDefinition.toDTO(json);
				}

				if (typeString.equals("FormStepDefinition")) {
					return PageFormStepDefinition.toDTO(json);
				}

				if (typeString.equals("FragmentCompositionDefinition")) {
					return PageFragmentCompositionInstanceDefinition.toDTO(
						json);
				}

				if (typeString.equals("FragmentDefinition")) {
					return PageFragmentInstanceDefinition.toDTO(json);
				}

				if (typeString.equals("FragmentDropZoneDefinition")) {
					return PageFragmentDropZoneDefinition.toDTO(json);
				}

				if (typeString.equals("RowDefinition")) {
					return PageRowDefinition.toDTO(json);
				}

				if (typeString.equals("WidgetDefinition")) {
					return PageWidgetInstanceDefinition.toDTO(json);
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
			PageElementDefinition pageElementDefinition,
			String jsonParserFieldName, Object jsonParserFieldValue) {

			if (Objects.equals(jsonParserFieldName, "type")) {
				if (jsonParserFieldValue != null) {
					pageElementDefinition.setType(
						PageElementDefinition.Type.create(
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