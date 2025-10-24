/**
 * SPDX-FileCopyrightText: (c) 2025 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.headless.admin.site.internal.dto.v1_0.converter;

import com.liferay.asset.kernel.model.AssetCategory;
import com.liferay.asset.kernel.model.AssetVocabulary;
import com.liferay.asset.kernel.service.AssetCategoryLocalService;
import com.liferay.asset.kernel.service.AssetVocabularyLocalService;
import com.liferay.fragment.util.configuration.FragmentConfigurationField;
import com.liferay.headless.admin.site.dto.v1_0.CategoryConfigurationFieldValue;
import com.liferay.headless.admin.site.dto.v1_0.CheckboxConfigurationFieldValue;
import com.liferay.headless.admin.site.dto.v1_0.CollectionConfigurationFieldValue;
import com.liferay.headless.admin.site.dto.v1_0.ColorPaletteConfigurationFieldValue;
import com.liferay.headless.admin.site.dto.v1_0.ColorPaletteValue;
import com.liferay.headless.admin.site.dto.v1_0.ColorPickerConfigurationFieldValue;
import com.liferay.headless.admin.site.dto.v1_0.ConfigurationFieldValue;
import com.liferay.headless.admin.site.dto.v1_0.ItemConfigurationFieldValue;
import com.liferay.headless.admin.site.dto.v1_0.ItemExternalReference;
import com.liferay.headless.admin.site.dto.v1_0.ItemValue;
import com.liferay.headless.admin.site.dto.v1_0.LengthConfigurationFieldValue;
import com.liferay.headless.admin.site.dto.v1_0.NavigationMenuConfigurationFieldValue;
import com.liferay.headless.admin.site.dto.v1_0.NavigationMenuValue;
import com.liferay.headless.admin.site.dto.v1_0.Scope;
import com.liferay.headless.admin.site.dto.v1_0.SelectConfigurationFieldValue;
import com.liferay.headless.admin.site.dto.v1_0.TemplateReference;
import com.liferay.headless.admin.site.dto.v1_0.TextConfigurationFieldValue;
import com.liferay.headless.admin.site.dto.v1_0.UrlConfigurationFieldValue;
import com.liferay.headless.admin.site.dto.v1_0.VideoConfigurationFieldValue;
import com.liferay.headless.admin.site.dto.v1_0.VideoValue;
import com.liferay.headless.admin.site.internal.dto.v1_0.util.CollectionUtil;
import com.liferay.headless.admin.site.internal.dto.v1_0.util.ConfigurationFieldValueTypeUtil;
import com.liferay.headless.admin.site.internal.dto.v1_0.util.InfoItemUtil;
import com.liferay.headless.admin.site.internal.dto.v1_0.util.ItemScopeUtil;
import com.liferay.headless.admin.site.internal.dto.v1_0.util.LocalizedValueUtil;
import com.liferay.info.item.ERCInfoItemIdentifier;
import com.liferay.info.item.InfoItemServiceRegistry;
import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.json.JSONObject;
import com.liferay.portal.kernel.json.JSONUtil;
import com.liferay.portal.kernel.model.Layout;
import com.liferay.portal.kernel.service.LayoutLocalService;
import com.liferay.portal.kernel.util.GetterUtil;
import com.liferay.portal.kernel.util.Validator;
import com.liferay.portal.vulcan.dto.converter.DTOConverter;
import com.liferay.portal.vulcan.dto.converter.DTOConverterContext;
import com.liferay.site.navigation.model.SiteNavigationMenu;
import com.liferay.site.navigation.model.SiteNavigationMenuItem;
import com.liferay.site.navigation.service.SiteNavigationMenuItemLocalService;
import com.liferay.site.navigation.service.SiteNavigationMenuLocalService;

import java.util.Objects;

import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

/**
 * @author Lourdes Fernández Besada
 */
@Component(
	property = "dto.class.name=com.liferay.fragment.util.configuration.FragmentConfigurationField",
	service = DTOConverter.class
)
public class ConfigurationFieldValueDTOConverter
	implements DTOConverter
		<FragmentConfigurationField, ConfigurationFieldValue> {

	@Override
	public String getContentType() {
		return ConfigurationFieldValue.class.getSimpleName();
	}

	@Override
	public ConfigurationFieldValue toDTO(
			DTOConverterContext dtoConverterContext,
			FragmentConfigurationField fragmentConfigurationField)
		throws Exception {

		if (dtoConverterContext == null) {
			return null;
		}

		Object fragmentConfigurationFieldValue =
			dtoConverterContext.getAttribute("fragmentConfigurationFieldValue");

		if (fragmentConfigurationFieldValue == null) {
			return null;
		}

		ConfigurationFieldValue.Type type =
			ConfigurationFieldValueTypeUtil.toExternalType(
				fragmentConfigurationField.getType());

		if (Objects.equals(type, ConfigurationFieldValue.Type.CATEGORY)) {
			return _getCategoryConfigurationFieldValue(
				dtoConverterContext, fragmentConfigurationField,
				(JSONObject)fragmentConfigurationFieldValue);
		}

		if (Objects.equals(type, ConfigurationFieldValue.Type.CHECKBOX)) {
			return _getCheckboxConfigurationFieldValue(
				fragmentConfigurationField, fragmentConfigurationFieldValue);
		}

		if (Objects.equals(type, ConfigurationFieldValue.Type.COLLECTION)) {
			return _getCollectionConfigurationFieldValue(
				dtoConverterContext, fragmentConfigurationField,
				(JSONObject)fragmentConfigurationFieldValue);
		}

		if (Objects.equals(type, ConfigurationFieldValue.Type.COLOR_PALETTE)) {
			return _getColorPaletteConfigurationFieldValue(
				fragmentConfigurationField,
				(JSONObject)fragmentConfigurationFieldValue);
		}

		if (Objects.equals(type, ConfigurationFieldValue.Type.COLOR_PICKER)) {
			return _getColorPickerConfigurationFieldValue(
				fragmentConfigurationField, fragmentConfigurationFieldValue);
		}

		if (Objects.equals(type, ConfigurationFieldValue.Type.ITEM)) {
			return _getItemConfigurationFieldValue(
				dtoConverterContext, fragmentConfigurationField,
				(JSONObject)fragmentConfigurationFieldValue);
		}

		if (Objects.equals(type, ConfigurationFieldValue.Type.LENGTH)) {
			return _getLengthConfigurationFieldValue(
				fragmentConfigurationField, fragmentConfigurationFieldValue);
		}

		if (Objects.equals(
				type, ConfigurationFieldValue.Type.NAVIGATION_MENU)) {

			return _getNavigationMenuConfigurationFieldValue(
				dtoConverterContext, fragmentConfigurationField,
				(JSONObject)fragmentConfigurationFieldValue);
		}

		if (Objects.equals(type, ConfigurationFieldValue.Type.TEXT)) {
			return _getTextConfigurationFieldValue(
				fragmentConfigurationField, fragmentConfigurationFieldValue);
		}

		if (Objects.equals(type, ConfigurationFieldValue.Type.SELECT)) {
			return _getSelectConfigurationFieldValue(
				fragmentConfigurationField, fragmentConfigurationFieldValue);
		}

		if (Objects.equals(type, ConfigurationFieldValue.Type.URL)) {
			return _getUrlConfigurationFieldValue(
				dtoConverterContext, fragmentConfigurationField,
				(JSONObject)fragmentConfigurationFieldValue);
		}

		if (Objects.equals(type, ConfigurationFieldValue.Type.VIDEO)) {
			return _getVideoConfigurationFieldValue(
				fragmentConfigurationField,
				(JSONObject)fragmentConfigurationFieldValue);
		}

		return null;
	}

	private ConfigurationFieldValue _getCategoryConfigurationFieldValue(
			DTOConverterContext dtoConverterContext,
			FragmentConfigurationField fragmentConfigurationField,
			JSONObject jsonObject)
		throws Exception {

		Long companyId = (Long)dtoConverterContext.getAttribute("companyId");
		Long scopeGroupId = (Long)dtoConverterContext.getAttribute(
			"scopeGroupId");

		if ((companyId == null) || (scopeGroupId == null)) {
			throw new UnsupportedOperationException();
		}

		CategoryConfigurationFieldValue categoryConfigurationFieldValue =
			new CategoryConfigurationFieldValue() {
				{
					setType(ConfigurationFieldValue.Type.CATEGORY);
				}
			};

		if (fragmentConfigurationField.isLocalizable()) {
			categoryConfigurationFieldValue.setValue_i18n(
				() -> LocalizedValueUtil.toLocalizedValues(
					jsonObject,
					key -> _getCategoryTreeNodeItemExternalReference(
						companyId, jsonObject.getJSONObject(key),
						scopeGroupId)));
		}
		else {
			categoryConfigurationFieldValue.setValue(
				() -> _getCategoryTreeNodeItemExternalReference(
					companyId, jsonObject, scopeGroupId));
		}

		return categoryConfigurationFieldValue;
	}

	private ItemExternalReference _getCategoryTreeNodeItemExternalReference(
			long companyId, JSONObject jsonObject, long scopeGroupId)
		throws Exception {

		if (JSONUtil.isEmpty(jsonObject)) {
			return null;
		}

		long categoryTreeNodeId = jsonObject.getLong("categoryTreeNodeId");
		String externalReferenceCode = jsonObject.getString(
			"categoryTreeNodeExternalReferenceCode");
		String type = jsonObject.getString("categoryTreeNodeType");

		if (((categoryTreeNodeId == 0) &&
			 Validator.isNull(externalReferenceCode)) ||
			(!Objects.equals(type, "Category") &&
			 !Objects.equals(type, "Vocabulary"))) {

			return null;
		}

		if (categoryTreeNodeId == 0) {
			String className = AssetCategory.class.getName();

			if (Objects.equals(type, "Vocabulary")) {
				className = AssetVocabulary.class.getName();
			}

			return _getItemExternalReference(
				className, externalReferenceCode,
				ItemScopeUtil.getItemScope(
					companyId,
					jsonObject.getString(
						"categoryTreeNodeScopeExternalReferenceCode"),
					scopeGroupId));
		}

		if (Objects.equals(type, "Category")) {
			AssetCategory assetCategory =
				_assetCategoryLocalService.fetchAssetCategory(
					categoryTreeNodeId);

			if (assetCategory == null) {
				return _getItemExternalReference(
					AssetCategory.class.getName(), externalReferenceCode,
					ItemScopeUtil.getItemScope(
						companyId,
						jsonObject.getString(
							"categoryTreeNodeScopeExternalReferenceCode"),
						scopeGroupId));
			}

			return _getItemExternalReference(
				AssetCategory.class.getName(),
				assetCategory.getExternalReferenceCode(),
				ItemScopeUtil.getItemScope(
					assetCategory.getGroupId(), scopeGroupId));
		}

		AssetVocabulary assetVocabulary =
			_assetVocabularyLocalService.fetchAssetVocabulary(
				categoryTreeNodeId);

		if (assetVocabulary == null) {
			return _getItemExternalReference(
				AssetVocabulary.class.getName(), externalReferenceCode,
				ItemScopeUtil.getItemScope(
					companyId,
					jsonObject.getString(
						"categoryTreeNodeScopeExternalReferenceCode"),
					scopeGroupId));
		}

		return _getItemExternalReference(
			AssetVocabulary.class.getName(),
			assetVocabulary.getExternalReferenceCode(),
			ItemScopeUtil.getItemScope(
				assetVocabulary.getGroupId(), scopeGroupId));
	}

	private ConfigurationFieldValue _getCheckboxConfigurationFieldValue(
		FragmentConfigurationField fragmentConfigurationField,
		Object fragmentConfigurationFieldValue) {

		CheckboxConfigurationFieldValue checkboxConfigurationFieldValue =
			new CheckboxConfigurationFieldValue() {
				{
					setType(ConfigurationFieldValue.Type.CHECKBOX);
				}
			};

		if (fragmentConfigurationField.isLocalizable()) {
			JSONObject jsonObject = (JSONObject)fragmentConfigurationFieldValue;

			checkboxConfigurationFieldValue.setValue_i18n(
				() -> LocalizedValueUtil.toLocalizedValues(
					jsonObject, key -> jsonObject.getBoolean(key)));
		}
		else {
			checkboxConfigurationFieldValue.setValue(
				() -> GetterUtil.getBoolean(fragmentConfigurationFieldValue));
		}

		return checkboxConfigurationFieldValue;
	}

	private ConfigurationFieldValue _getCollectionConfigurationFieldValue(
		DTOConverterContext dtoConverterContext,
		FragmentConfigurationField fragmentConfigurationField,
		JSONObject jsonObject) {

		Long companyId = (Long)dtoConverterContext.getAttribute("companyId");
		Long scopeGroupId = (Long)dtoConverterContext.getAttribute(
			"scopeGroupId");

		if ((companyId == null) || (scopeGroupId == null)) {
			throw new UnsupportedOperationException();
		}

		CollectionConfigurationFieldValue collectionConfigurationFieldValue =
			new CollectionConfigurationFieldValue() {
				{
					setType(Type.COLLECTION);
				}
			};

		if (fragmentConfigurationField.isLocalizable()) {
			collectionConfigurationFieldValue.setValue_i18n(
				() -> LocalizedValueUtil.toLocalizedValues(
					jsonObject,
					key -> CollectionUtil.getCollectionReference(
						companyId, jsonObject.getJSONObject(key),
						scopeGroupId)));
		}
		else {
			collectionConfigurationFieldValue.setValue(
				() -> CollectionUtil.getCollectionReference(
					companyId, jsonObject, scopeGroupId));
		}

		return collectionConfigurationFieldValue;
	}

	private ConfigurationFieldValue _getColorPaletteConfigurationFieldValue(
		FragmentConfigurationField fragmentConfigurationField,
		JSONObject jsonObject) {

		ColorPaletteConfigurationFieldValue
			colorPaletteConfigurationFieldValue =
				new ColorPaletteConfigurationFieldValue() {
					{
						setType(Type.COLOR_PALETTE);
					}
				};

		if (fragmentConfigurationField.isLocalizable()) {
			colorPaletteConfigurationFieldValue.setValue_i18n(
				() -> LocalizedValueUtil.toLocalizedValues(
					jsonObject,
					key -> _getColorPaletteValue(
						jsonObject.getJSONObject(key))));
		}
		else {
			colorPaletteConfigurationFieldValue.setValue(
				() -> _getColorPaletteValue(jsonObject));
		}

		return colorPaletteConfigurationFieldValue;
	}

	private ColorPaletteValue _getColorPaletteValue(JSONObject jsonObject) {
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

	private ConfigurationFieldValue _getColorPickerConfigurationFieldValue(
		FragmentConfigurationField fragmentConfigurationField,
		Object fragmentConfigurationFieldValue) {

		ColorPickerConfigurationFieldValue colorPickerConfigurationFieldValue =
			new ColorPickerConfigurationFieldValue() {
				{
					setType(Type.COLOR_PICKER);
				}
			};

		if (fragmentConfigurationField.isLocalizable()) {
			JSONObject jsonObject = (JSONObject)fragmentConfigurationFieldValue;

			colorPickerConfigurationFieldValue.setValue_i18n(
				() -> LocalizedValueUtil.toLocalizedValues(jsonObject));
		}
		else {
			colorPickerConfigurationFieldValue.setValue(
				() -> GetterUtil.getString(fragmentConfigurationFieldValue));
		}

		return colorPickerConfigurationFieldValue;
	}

	private ItemExternalReference _getInfoItemExternalReference(
			long companyId, JSONObject jsonObject, long scopeGroupId) {

		if (JSONUtil.isEmpty(jsonObject)) {
			return null;
		}

		String className = jsonObject.getString("className");
		long classPK = jsonObject.getLong("classPK");
		String externalReferenceCode = jsonObject.getString(
			"externalReferenceCode");

		if (Validator.isNull(className) ||
			((classPK == 0) && Validator.isNull(externalReferenceCode))) {

			return null;
		}

		ERCInfoItemIdentifier ercInfoItemIdentifier =
			InfoItemUtil.getERCInfoItemIdentifier(
				className, classPK, _infoItemServiceRegistry, scopeGroupId);

		if (ercInfoItemIdentifier != null) {
			return _getItemExternalReference(
				className, ercInfoItemIdentifier.getExternalReferenceCode(),
				ItemScopeUtil.getItemScope(
					companyId,
					ercInfoItemIdentifier.getScopeExternalReferenceCode(),
					scopeGroupId));
		}

		return _getItemExternalReference(
			className, externalReferenceCode,
			ItemScopeUtil.getItemScope(
				companyId, jsonObject.getString("externalReferenceCode"),
				scopeGroupId));
	}

	private ConfigurationFieldValue _getItemConfigurationFieldValue(
			DTOConverterContext dtoConverterContext,
			FragmentConfigurationField fragmentConfigurationField,
			JSONObject jsonObject)
		throws Exception {

		Long companyId = (Long)dtoConverterContext.getAttribute("companyId");
		Long scopeGroupId = (Long)dtoConverterContext.getAttribute(
			"scopeGroupId");

		if ((companyId == null) || (scopeGroupId == null)) {
			throw new UnsupportedOperationException();
		}

		ItemConfigurationFieldValue itemConfigurationFieldValue =
			new ItemConfigurationFieldValue() {
				{
					setType(Type.ITEM);
				}
			};

		if (fragmentConfigurationField.isLocalizable()) {
			itemConfigurationFieldValue.setValue_i18n(
				() -> LocalizedValueUtil.toLocalizedValues(
					jsonObject,
					key -> _getItemValue(
						companyId, jsonObject.getJSONObject(key),
						scopeGroupId)));
		}
		else {
			itemConfigurationFieldValue.setValue(
				() -> _getItemValue(companyId, jsonObject, scopeGroupId));
		}

		return itemConfigurationFieldValue;
	}

	private ItemExternalReference _getItemExternalReference(
		String className, String externalReferenceCode, Scope scope) {

		ItemExternalReference itemExternalReference =
			new ItemExternalReference();

		itemExternalReference.setClassName(className);
		itemExternalReference.setExternalReferenceCode(externalReferenceCode);
		itemExternalReference.setScope(scope);

		return itemExternalReference;
	}

	private ItemValue _getItemValue(
			long companyId, JSONObject jsonObject, long scopeGroupId) {

		ItemExternalReference infoItemExternalReference =
			_getInfoItemExternalReference(companyId, jsonObject, scopeGroupId);

		if (infoItemExternalReference == null) {
			return null;
		}

		ItemValue itemValue = new ItemValue();

		itemValue.setItem(() -> infoItemExternalReference);
		itemValue.setTemplate(
			() -> {
				JSONObject templateJSONObject = jsonObject.getJSONObject(
					"template");

				if (JSONUtil.isEmpty(templateJSONObject)) {
					return null;
				}

				return new TemplateReference() {
					{
						setRendererKey(
							() -> templateJSONObject.getString(
								"infoItemRendererKey"));
						setTemplateKey(
							() -> templateJSONObject.getString("templateKey"));
					}
				};
			});

		return itemValue;
	}

	private ConfigurationFieldValue _getLengthConfigurationFieldValue(
		FragmentConfigurationField fragmentConfigurationField,
		Object fragmentConfigurationFieldValue) {

		LengthConfigurationFieldValue lengthConfigurationFieldValue =
			new LengthConfigurationFieldValue() {
				{
					setType(Type.LENGTH);
				}
			};

		if (fragmentConfigurationField.isLocalizable()) {
			JSONObject jsonObject = (JSONObject)fragmentConfigurationFieldValue;

			lengthConfigurationFieldValue.setValue_i18n(
				() -> LocalizedValueUtil.toLocalizedValues(jsonObject));
		}
		else {
			lengthConfigurationFieldValue.setValue(
				() -> GetterUtil.getString(fragmentConfigurationFieldValue));
		}

		return lengthConfigurationFieldValue;
	}

	private ConfigurationFieldValue _getNavigationMenuConfigurationFieldValue(
			DTOConverterContext dtoConverterContext,
			FragmentConfigurationField fragmentConfigurationField,
			JSONObject jsonObject)
		throws Exception {

		Long companyId = (Long)dtoConverterContext.getAttribute("companyId");
		Long scopeGroupId = (Long)dtoConverterContext.getAttribute(
			"scopeGroupId");

		if ((companyId == null) || (scopeGroupId == null)) {
			throw new UnsupportedOperationException();
		}

		NavigationMenuConfigurationFieldValue
			navigationMenuConfigurationFieldValue =
				new NavigationMenuConfigurationFieldValue() {
					{
						setType(Type.NAVIGATION_MENU);
					}
				};

		if (fragmentConfigurationField.isLocalizable()) {
			navigationMenuConfigurationFieldValue.setValue_i18n(
				() -> LocalizedValueUtil.toLocalizedValues(
					jsonObject,
					key -> _getNavigationMenuValue(
						companyId, jsonObject.getJSONObject(key),
						scopeGroupId)));
		}
		else {
			navigationMenuConfigurationFieldValue.setValue(
				() -> _getNavigationMenuValue(
					companyId, jsonObject, scopeGroupId));
		}

		return navigationMenuConfigurationFieldValue;
	}

	private NavigationMenuValue _getNavigationMenuValue(
			long companyId, JSONObject jsonObject, long scopeGroupId)
		throws Exception {

		if (JSONUtil.isEmpty(jsonObject)) {
			return null;
		}

		Boolean privateLayout = jsonObject.getBoolean("privateLayout");
		long siteNavigationMenuId = jsonObject.getLong("siteNavigationMenuId");
		String siteNavigationMenuExternalReferenceCode = jsonObject.getString(
			"siteNavigationMenuExternalReferenceCode");
		long parentSiteNavigationMenuItemId = jsonObject.getLong(
			"parentSiteNavigationMenuItemId");
		String parentSiteNavigationMenuItemExternalReferenceCode =
			jsonObject.getString(
				"parentSiteNavigationMenuItemExternalReferenceCode");

		if ((privateLayout == null) &&
			Validator.isNull(siteNavigationMenuExternalReferenceCode) &&
			(siteNavigationMenuId == 0) &&
			Validator.isNull(
				parentSiteNavigationMenuItemExternalReferenceCode) &&
			(parentSiteNavigationMenuItemId == 0)) {

			return null;
		}

		NavigationMenuValue navigationMenuValue = new NavigationMenuValue() {
			{
				setPrivatePages(() -> privateLayout);
			}
		};

		navigationMenuValue.setNavigationMenu(
			() -> _getSiteNavigationMenuItemExternalReference(
				companyId, siteNavigationMenuExternalReferenceCode,
				jsonObject.getString(
					"siteNavigationMenuScopeExternalReferenceCode"),
				siteNavigationMenuId, scopeGroupId));

		if (Validator.isNull(
				parentSiteNavigationMenuItemExternalReferenceCode) &&
			(parentSiteNavigationMenuItemId == 0)) {

			return navigationMenuValue;
		}

		navigationMenuValue.setParentItemExternalReferenceCode(
			() -> _getParentItemExternalReferenceCode(
				parentSiteNavigationMenuItemExternalReferenceCode,
				parentSiteNavigationMenuItemId,
				siteNavigationMenuExternalReferenceCode, siteNavigationMenuId));

		return navigationMenuValue;
	}

	private String _getParentItemExternalReferenceCode(
		String parentItemExternalReferenceCode, long parentItemId,
		String siteNavigationMenuExternalReferenceCode,
		long siteNavigationMenuId) {

		if (parentItemId == 0) {
			return parentItemExternalReferenceCode;
		}

		if (Validator.isNull(siteNavigationMenuExternalReferenceCode) &&
			(siteNavigationMenuId == 0)) {

			Layout layout = _layoutLocalService.fetchLayout(parentItemId);

			if (layout != null) {
				return layout.getExternalReferenceCode();
			}

			return parentItemExternalReferenceCode;
		}

		SiteNavigationMenuItem siteNavigationMenuItem =
			_siteNavigationMenuItemLocalService.fetchSiteNavigationMenuItem(
				siteNavigationMenuId);

		if (siteNavigationMenuItem != null) {
			return siteNavigationMenuItem.getExternalReferenceCode();
		}

		return parentItemExternalReferenceCode;
	}

	private ConfigurationFieldValue _getSelectConfigurationFieldValue(
		FragmentConfigurationField fragmentConfigurationField,
		Object fragmentConfigurationFieldValue) {

		SelectConfigurationFieldValue selectConfigurationFieldValue =
			new SelectConfigurationFieldValue() {
				{
					setType(Type.SELECT);
				}
			};

		if (fragmentConfigurationField.isLocalizable()) {
			JSONObject jsonObject = (JSONObject)fragmentConfigurationFieldValue;

			selectConfigurationFieldValue.setValue_i18n(
				() -> LocalizedValueUtil.toLocalizedValues(jsonObject));
		}
		else {
			selectConfigurationFieldValue.setValue(
				() -> GetterUtil.getString(fragmentConfigurationFieldValue));
		}

		return selectConfigurationFieldValue;
	}

	private ItemExternalReference _getSiteNavigationMenuItemExternalReference(
			long companyId, String externalReferenceCode,
			String scopeExternalReferenceCode, long siteNavigationMenuId,
			long scopeGroupId)
		throws Exception {

		if (siteNavigationMenuId > 0) {
			SiteNavigationMenu siteNavigationMenu =
				_siteNavigationMenuLocalService.fetchSiteNavigationMenu(
					siteNavigationMenuId);

			if (siteNavigationMenu != null) {
				return _getItemExternalReference(
					SiteNavigationMenu.class.getName(),
					siteNavigationMenu.getExternalReferenceCode(),
					ItemScopeUtil.getItemScope(
						siteNavigationMenu.getGroupId(), scopeGroupId));
			}
		}

		if (Validator.isNotNull(externalReferenceCode)) {
			return _getItemExternalReference(
				SiteNavigationMenu.class.getName(), externalReferenceCode,
				ItemScopeUtil.getItemScope(
					companyId, scopeExternalReferenceCode, scopeGroupId));
		}

		return null;
	}

	private ConfigurationFieldValue _getTextConfigurationFieldValue(
		FragmentConfigurationField fragmentConfigurationField,
		Object fragmentConfigurationFieldValue) {

		TextConfigurationFieldValue textConfigurationFieldValue =
			new TextConfigurationFieldValue() {
				{
					setType(Type.TEXT);
				}
			};

		if (fragmentConfigurationField.isLocalizable()) {
			JSONObject jsonObject = (JSONObject)fragmentConfigurationFieldValue;

			textConfigurationFieldValue.setValue_i18n(
				() -> LocalizedValueUtil.toLocalizedValues(jsonObject));
		}
		else {
			textConfigurationFieldValue.setValue(
				() -> GetterUtil.getString(fragmentConfigurationFieldValue));
		}

		return textConfigurationFieldValue;
	}

	private ConfigurationFieldValue _getUrlConfigurationFieldValue(
		DTOConverterContext dtoConverterContext,
		FragmentConfigurationField fragmentConfigurationField,
		JSONObject jsonObject) {

		Long companyId = (Long)dtoConverterContext.getAttribute("companyId");
		Long scopeGroupId = (Long)dtoConverterContext.getAttribute(
			"scopeGroupId");

		if ((companyId == null) || (scopeGroupId == null)) {
			throw new UnsupportedOperationException();
		}

		UrlConfigurationFieldValue urlConfigurationFieldValue =
			new UrlConfigurationFieldValue() {
				{
					setType(Type.URL);
				}
			};

		if (fragmentConfigurationField.isLocalizable()) {
			urlConfigurationFieldValue.setValue_i18n(
				() -> LocalizedValueUtil.toLocalizedValues(
					jsonObject,
					key -> _getUrlJSONObject(
						companyId, jsonObject.getJSONObject(key),
						scopeGroupId)));
		}
		else {
			urlConfigurationFieldValue.setValue(
				() -> _getUrlJSONObject(companyId, jsonObject, scopeGroupId));
		}

		return urlConfigurationFieldValue;
	}

	private Object _getUrlJSONObject(
		long companyId, JSONObject jsonObject, long scopeGroupId) {

		// TODO

		return null;
	}

	private ConfigurationFieldValue _getVideoConfigurationFieldValue(
		FragmentConfigurationField fragmentConfigurationField,
		JSONObject jsonObject) {

		VideoConfigurationFieldValue videoConfigurationFieldValue =
			new VideoConfigurationFieldValue() {
				{
					setType(Type.VIDEO);
				}
			};

		if (fragmentConfigurationField.isLocalizable()) {
			videoConfigurationFieldValue.setValue_i18n(
				() -> LocalizedValueUtil.toLocalizedValues(
					jsonObject,
					key -> _getVideoValue(jsonObject.getJSONObject(key))));
		}
		else {
			videoConfigurationFieldValue.setValue(
				() -> _getVideoValue(jsonObject));
		}

		return videoConfigurationFieldValue;
	}

	private VideoValue _getVideoValue(JSONObject jsonObject) {
		return new VideoValue() {
			{
				setHtml(() -> jsonObject.getString("html"));
				setTitle(() -> jsonObject.getString("title"));
			}
		};
	}

	@Reference
	private AssetCategoryLocalService _assetCategoryLocalService;

	@Reference
	private AssetVocabularyLocalService _assetVocabularyLocalService;

	@Reference
	private InfoItemServiceRegistry _infoItemServiceRegistry;

	@Reference
	private LayoutLocalService _layoutLocalService;

	@Reference
	private SiteNavigationMenuItemLocalService
		_siteNavigationMenuItemLocalService;

	@Reference
	private SiteNavigationMenuLocalService _siteNavigationMenuLocalService;

}