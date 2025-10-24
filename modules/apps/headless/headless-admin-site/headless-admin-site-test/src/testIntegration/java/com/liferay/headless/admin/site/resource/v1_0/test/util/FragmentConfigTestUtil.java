/**
 * SPDX-FileCopyrightText: (c) 2025 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.headless.admin.site.resource.v1_0.test.util;

import com.liferay.fragment.util.configuration.FragmentConfigurationField;
import com.liferay.fragment.util.configuration.FragmentEntryConfigurationParserUtil;
import com.liferay.headless.admin.site.client.dto.v1_0.CategoryConfigurationFieldValue;
import com.liferay.headless.admin.site.client.dto.v1_0.CheckboxConfigurationFieldValue;
import com.liferay.headless.admin.site.client.dto.v1_0.CollectionConfigurationFieldValue;
import com.liferay.headless.admin.site.client.dto.v1_0.CollectionReference;
import com.liferay.headless.admin.site.client.dto.v1_0.ColorPaletteConfigurationFieldValue;
import com.liferay.headless.admin.site.client.dto.v1_0.ColorPaletteValue;
import com.liferay.headless.admin.site.client.dto.v1_0.ColorPickerConfigurationFieldValue;
import com.liferay.headless.admin.site.client.dto.v1_0.ConfigurationFieldValue;
import com.liferay.headless.admin.site.client.dto.v1_0.ItemConfigurationFieldValue;
import com.liferay.headless.admin.site.client.dto.v1_0.ItemExternalReference;
import com.liferay.headless.admin.site.client.dto.v1_0.ItemValue;
import com.liferay.headless.admin.site.client.dto.v1_0.SelectConfigurationFieldValue;
import com.liferay.headless.admin.site.client.dto.v1_0.TemplateReference;
import com.liferay.portal.kernel.json.JSONObject;
import com.liferay.portal.kernel.json.JSONUtil;
import com.liferay.portal.kernel.util.GetterUtil;
import com.liferay.portal.kernel.util.HashMapBuilder;
import com.liferay.portal.kernel.util.LocaleUtil;
import com.liferay.portal.kernel.util.MapUtil;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

/**
 * @author Lourdes Fernández Besada
 */
public class FragmentConfigTestUtil {

	public static ConfigurationFieldValue getCategoryConfigurationFieldValue(
		boolean localizable, Object object, long scopeGroupId) {

		ItemExternalReference itemExternalReference =
			ReferencesTestUtil.getItemExternalReference(object, scopeGroupId);

		if (itemExternalReference == null) {
			return null;
		}

		CategoryConfigurationFieldValue categoryConfigurationFieldValue =
			new CategoryConfigurationFieldValue() {
				{
					setType(Type.CATEGORY);
				}
			};

		if (localizable) {
			categoryConfigurationFieldValue.setValue_i18n(
				HashMapBuilder.put(
					LocaleUtil.toLanguageId(LocaleUtil.getDefault()),
					itemExternalReference
				).build());
		}
		else {
			categoryConfigurationFieldValue.setValue(itemExternalReference);
		}

		return categoryConfigurationFieldValue;
	}

	public static ConfigurationFieldValue getCheckboxConfigurationFieldValue(
		boolean localizable, Object object) {

		CheckboxConfigurationFieldValue checkboxConfigurationFieldValue =
			new CheckboxConfigurationFieldValue() {
				{
					setType(Type.CHECKBOX);
				}
			};

		if (localizable) {
			checkboxConfigurationFieldValue.setValue_i18n(
				HashMapBuilder.put(
					LocaleUtil.toLanguageId(LocaleUtil.getDefault()),
					GetterUtil.getBoolean(object)
				).build());
		}
		else {
			checkboxConfigurationFieldValue.setValue(
				GetterUtil.getBoolean(object));
		}

		return checkboxConfigurationFieldValue;
	}

	public static ConfigurationFieldValue getCollectionConfigurationFieldValue(
		boolean localizable, Object object, long scopeGroupId) {

		CollectionReference collectionReference =
			ReferencesTestUtil.getCollectionReference(object, scopeGroupId);

		if (collectionReference == null) {
			return null;
		}

		CollectionConfigurationFieldValue collectionConfigurationFieldValue =
			new CollectionConfigurationFieldValue() {
				{
					setType(Type.COLLECTION);
				}
			};

		if (localizable) {
			collectionConfigurationFieldValue.setValue_i18n(
				HashMapBuilder.put(
					LocaleUtil.toLanguageId(LocaleUtil.getDefault()),
					collectionReference
				).build());
		}
		else {
			collectionConfigurationFieldValue.setValue(collectionReference);
		}

		return collectionConfigurationFieldValue;
	}

	public static ConfigurationFieldValue
		getColorPaletteConfigurationFieldValue(
			boolean localizable, Object object) {

		ColorPaletteConfigurationFieldValue
			colorPaletteConfigurationFieldValue =
				new ColorPaletteConfigurationFieldValue() {
					{
						setType(Type.COLOR_PALETTE);
					}
				};

		if (localizable) {
			colorPaletteConfigurationFieldValue.setValue_i18n(
				HashMapBuilder.put(
					LocaleUtil.toLanguageId(LocaleUtil.getDefault()),
					getColorPaletteValue((JSONObject)object)
				).build());
		}
		else {
			colorPaletteConfigurationFieldValue.setValue(
				getColorPaletteValue((JSONObject)object));
		}

		return colorPaletteConfigurationFieldValue;
	}

	public static ColorPaletteValue getColorPaletteValue(
		JSONObject jsonObject) {

		if (JSONUtil.isEmpty(jsonObject)) {
			return null;
		}

		return new ColorPaletteValue() {
			{
				setColor(() -> jsonObject.getString("color"));
				setCssClass(() -> jsonObject.getString("cssClass"));
				setRgbValue(() -> jsonObject.getString("rgbValue"));
			}
		};
	}

	public static ConfigurationFieldValue getColorPickerConfigurationFieldValue(
		boolean localizable, Object object) {

		ColorPickerConfigurationFieldValue colorPickerConfigurationFieldValue =
			new ColorPickerConfigurationFieldValue() {
				{
					setType(Type.COLOR_PICKER);
				}
			};

		if (localizable) {
			colorPickerConfigurationFieldValue.setValue_i18n(
				HashMapBuilder.put(
					LocaleUtil.toLanguageId(LocaleUtil.getDefault()),
					GetterUtil.getString(object)
				).build());
		}
		else {
			colorPickerConfigurationFieldValue.setValue(
				GetterUtil.getString(object));
		}

		return colorPickerConfigurationFieldValue;
	}

	public static ConfigurationFieldValue getConfigurationFieldValue(
		FragmentConfigurationField fragmentConfigurationField, Object value,
		long scopeGroupId) {

		String type = fragmentConfigurationField.getType();

		if (Objects.equals(type, "categoryTreeNodeSelector")) {
			return getCategoryConfigurationFieldValue(
				fragmentConfigurationField.isLocalizable(), value,
				scopeGroupId);
		}

		if (Objects.equals(type, "checkbox")) {
			return getCheckboxConfigurationFieldValue(
				fragmentConfigurationField.isLocalizable(), value);
		}

		if (Objects.equals(type, "collectionSelector")) {
			return getCollectionConfigurationFieldValue(
				fragmentConfigurationField.isLocalizable(), value,
				scopeGroupId);
		}

		if (Objects.equals(type, "colorPalette")) {
			return getColorPaletteConfigurationFieldValue(
				fragmentConfigurationField.isLocalizable(), value);
		}

		if (Objects.equals(type, "colorPicker")) {
			return getColorPickerConfigurationFieldValue(
				fragmentConfigurationField.isLocalizable(), value);
		}

		if (Objects.equals(type, "itemSelector")) {
			return getItemConfigurationFieldValue(
				fragmentConfigurationField.isLocalizable(), value,
				scopeGroupId);
		}

		if (Objects.equals(type, "select")) {
			return getSelectConfigurationFieldValue(
				fragmentConfigurationField.isLocalizable(), value);
		}

		return null;
	}

	public static Map<String, ConfigurationFieldValue> getFragmentConfigMap(
		JSONObject configurationJSONObject, Map<String, Object> objectsMap,
		long scopeGroupId) {

		Map<String, ConfigurationFieldValue> map = new HashMap<>();

		for (FragmentConfigurationField fragmentConfigurationField :
				FragmentEntryConfigurationParserUtil.
					getFragmentConfigurationFields(configurationJSONObject)) {

			Object object = objectsMap.get(
				fragmentConfigurationField.getName());

			if (object == null) {
				continue;
			}

			map.put(
				fragmentConfigurationField.getName(),
				getConfigurationFieldValue(
					fragmentConfigurationField, object, scopeGroupId));
		}

		return map;
	}

	public static ConfigurationFieldValue getItemConfigurationFieldValue(
		boolean localizable, Object object, long scopeGroupId) {

		Map<String, Object> objectMap = (Map<String, Object>)object;

		if (MapUtil.isEmpty(objectMap)) {
			return null;
		}

		ItemExternalReference itemExternalReference =
			ReferencesTestUtil.getItemExternalReference(
				objectMap.get("item"), scopeGroupId);

		if (itemExternalReference == null) {
			return null;
		}

		ItemConfigurationFieldValue itemConfigurationFieldValue =
			new ItemConfigurationFieldValue() {
				{
					setType(Type.ITEM);
				}
			};

		ItemValue itemValue = new ItemValue() {
			{
				setItem(itemExternalReference);
				setTemplate(
					() -> new TemplateReference() {
						{
							setRendererKey(
								() -> GetterUtil.getString(
									objectMap.get("infoItemRendererKey")));
							setTemplateKey(
								() -> GetterUtil.getString(
									objectMap.get("templateKey")));
						}
					});
			}
		};

		if (localizable) {
			itemConfigurationFieldValue.setValue_i18n(
				HashMapBuilder.put(
					LocaleUtil.toLanguageId(LocaleUtil.getDefault()), itemValue
				).build());
		}
		else {
			itemConfigurationFieldValue.setValue(itemValue);
		}

		return itemConfigurationFieldValue;
	}

	public static ConfigurationFieldValue getSelectConfigurationFieldValue(
		boolean localizable, Object object) {

		SelectConfigurationFieldValue selectConfigurationFieldValue =
			new SelectConfigurationFieldValue() {
				{
					setType(Type.SELECT);
				}
			};

		if (localizable) {
			selectConfigurationFieldValue.setValue_i18n(
				HashMapBuilder.put(
					LocaleUtil.toLanguageId(LocaleUtil.getDefault()),
					GetterUtil.getString(object)
				).build());
		}
		else {
			selectConfigurationFieldValue.setValue(
				GetterUtil.getString(object));
		}

		return selectConfigurationFieldValue;
	}

}