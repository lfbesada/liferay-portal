/**
 * SPDX-FileCopyrightText: (c) 2025 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.headless.admin.site.internal.resource.v1_0.layout.structure.item.importer.util;

import com.liferay.asset.kernel.model.AssetCategory;
import com.liferay.asset.kernel.model.AssetVocabulary;
import com.liferay.asset.kernel.service.AssetCategoryLocalServiceUtil;
import com.liferay.asset.kernel.service.AssetVocabularyLocalServiceUtil;
import com.liferay.asset.list.model.AssetListEntry;
import com.liferay.asset.list.service.AssetListEntryLocalServiceUtil;
import com.liferay.fragment.util.configuration.FragmentConfigurationField;
import com.liferay.fragment.util.configuration.FragmentEntryConfigurationParserUtil;
import com.liferay.headless.admin.site.dto.v1_0.CategoryConfigurationFieldValue;
import com.liferay.headless.admin.site.dto.v1_0.CheckboxConfigurationFieldValue;
import com.liferay.headless.admin.site.dto.v1_0.ClassNameReference;
import com.liferay.headless.admin.site.dto.v1_0.CollectionConfigurationFieldValue;
import com.liferay.headless.admin.site.dto.v1_0.CollectionItemExternalReference;
import com.liferay.headless.admin.site.dto.v1_0.CollectionReference;
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
import com.liferay.headless.admin.site.internal.dto.v1_0.util.ConfigurationFieldValueTypeUtil;
import com.liferay.headless.admin.site.internal.dto.v1_0.util.InfoItemUtil;
import com.liferay.headless.admin.site.internal.dto.v1_0.util.ItemScopeUtil;
import com.liferay.headless.admin.site.internal.dto.v1_0.util.LocalizedValueUtil;
import com.liferay.headless.admin.site.internal.resource.v1_0.layout.structure.item.importer.context.LayoutStructureItemImporterContext;
import com.liferay.headless.admin.site.internal.util.LogUtil;
import com.liferay.info.collection.provider.InfoCollectionProvider;
import com.liferay.info.collection.provider.SingleFormVariationInfoCollectionProvider;
import com.liferay.info.item.InfoItemServiceRegistry;
import com.liferay.info.list.provider.item.selector.criterion.InfoListProviderItemSelectorReturnType;
import com.liferay.item.selector.criteria.InfoListItemSelectorReturnType;
import com.liferay.petra.function.UnsafeFunction;
import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.json.JSONFactoryUtil;
import com.liferay.portal.kernel.json.JSONObject;
import com.liferay.portal.kernel.json.JSONUtil;
import com.liferay.portal.kernel.language.LanguageUtil;
import com.liferay.portal.kernel.model.Layout;
import com.liferay.portal.kernel.service.LayoutLocalServiceUtil;
import com.liferay.portal.kernel.util.GetterUtil;
import com.liferay.portal.kernel.util.LocaleUtil;
import com.liferay.portal.kernel.util.PortalUtil;
import com.liferay.portal.kernel.util.Validator;
import com.liferay.site.navigation.item.selector.SiteNavigationMenuItemSelectorReturnType;
import com.liferay.site.navigation.model.SiteNavigationMenu;
import com.liferay.site.navigation.model.SiteNavigationMenuItem;
import com.liferay.site.navigation.service.SiteNavigationMenuItemLocalServiceUtil;
import com.liferay.site.navigation.service.SiteNavigationMenuLocalServiceUtil;

import java.util.Map;
import java.util.Objects;

/**
 * @author Lourdes Fernández Besada
 */
public class FragmentInstanceConfigurationFieldValuesUtil {

	public static JSONObject getFreeMarkerFragmentEntryProcessorJSONObject(
			String configuration,
			Map<String, ConfigurationFieldValue> configurationFieldValuesMap,
			LayoutStructureItemImporterContext
				layoutStructureItemImporterContext)
		throws Exception {

		JSONObject jsonObject = JSONFactoryUtil.createJSONObject();

		if (configurationFieldValuesMap == null) {
			return jsonObject;
		}

		JSONObject configurationJSONObject = JSONFactoryUtil.createJSONObject(
			configuration);

		for (FragmentConfigurationField fragmentConfigurationField :
				FragmentEntryConfigurationParserUtil.
					getFragmentConfigurationFields(configurationJSONObject)) {

			ConfigurationFieldValue configurationFieldValue =
				configurationFieldValuesMap.get(
					fragmentConfigurationField.getName());

			if (configurationFieldValue == null) {
				continue;
			}

			if (!Objects.equals(
					configurationFieldValue.getType(),
					ConfigurationFieldValueTypeUtil.toExternalType(
						fragmentConfigurationField.getType()))) {

				throw new UnsupportedOperationException();
			}

			jsonObject.put(
				fragmentConfigurationField.getName(),
				_fromConfigurationFieldValue(
					configurationFieldValue, fragmentConfigurationField,
					layoutStructureItemImporterContext));
		}

		return jsonObject;
	}

	private static Object _fromConfigurationFieldValue(
			ConfigurationFieldValue configurationFieldValue,
			FragmentConfigurationField fragmentConfigurationField,
			LayoutStructureItemImporterContext
				layoutStructureItemImporterContext)
		throws Exception {

		if (Objects.equals(
				configurationFieldValue.getType(),
				ConfigurationFieldValue.Type.CATEGORY)) {

			CategoryConfigurationFieldValue categoryConfigurationFieldValue =
				(CategoryConfigurationFieldValue)configurationFieldValue;

			return _getConfigurationJSONObject(
				fragmentConfigurationField.isLocalizable(),
				itemExternalReference -> _getCategoryTreeNodeJSONObject(
					itemExternalReference, layoutStructureItemImporterContext),
				categoryConfigurationFieldValue.getValue(),
				categoryConfigurationFieldValue.getValue_i18n());
		}

		if (Objects.equals(
				configurationFieldValue.getType(),
				ConfigurationFieldValue.Type.CHECKBOX)) {

			CheckboxConfigurationFieldValue checkboxConfigurationFieldValue =
				(CheckboxConfigurationFieldValue)configurationFieldValue;

			return _getConfigurationObject(
				fragmentConfigurationField.isLocalizable(),
				checkboxConfigurationFieldValue.getValue(),
				checkboxConfigurationFieldValue.getValue_i18n());
		}

		if (Objects.equals(
				configurationFieldValue.getType(),
				ConfigurationFieldValue.Type.COLLECTION)) {

			CollectionConfigurationFieldValue
				collectionConfigurationFieldValue =
					(CollectionConfigurationFieldValue)configurationFieldValue;

			return _getConfigurationJSONObject(
				fragmentConfigurationField.isLocalizable(),
				collectionReference -> _getCollectionJSONObject(
					collectionReference, layoutStructureItemImporterContext),
				collectionConfigurationFieldValue.getValue(),
				collectionConfigurationFieldValue.getValue_i18n());
		}

		if (Objects.equals(
				configurationFieldValue.getType(),
				ConfigurationFieldValue.Type.COLOR_PALETTE)) {

			ColorPaletteConfigurationFieldValue
				colorPaletteConfigurationFieldValue =
					(ColorPaletteConfigurationFieldValue)
						configurationFieldValue;

			return _getConfigurationJSONObject(
				fragmentConfigurationField.isLocalizable(),
				colorPaletteValue -> _getColorPaletteJSONObject(
					colorPaletteValue),
				colorPaletteConfigurationFieldValue.getValue(),
				colorPaletteConfigurationFieldValue.getValue_i18n());
		}

		if (Objects.equals(
				configurationFieldValue.getType(),
				ConfigurationFieldValue.Type.COLOR_PICKER)) {

			ColorPickerConfigurationFieldValue
				colorPickerConfigurationFieldValue =
					(ColorPickerConfigurationFieldValue)configurationFieldValue;

			return _getConfigurationObject(
				fragmentConfigurationField.isLocalizable(),
				colorPickerConfigurationFieldValue.getValue(),
				colorPickerConfigurationFieldValue.getValue_i18n());
		}

		if (Objects.equals(
				configurationFieldValue.getType(),
				ConfigurationFieldValue.Type.ITEM)) {

			ItemConfigurationFieldValue itemConfigurationFieldValue =
				(ItemConfigurationFieldValue)configurationFieldValue;

			return _getConfigurationJSONObject(
				fragmentConfigurationField.isLocalizable(),
				itemValue -> _getItemJSONObject(
					itemValue, layoutStructureItemImporterContext),
				itemConfigurationFieldValue.getValue(),
				itemConfigurationFieldValue.getValue_i18n());
		}

		if (Objects.equals(
				configurationFieldValue.getType(),
				ConfigurationFieldValue.Type.LENGTH)) {

			LengthConfigurationFieldValue lengthConfigurationFieldValue =
				(LengthConfigurationFieldValue)configurationFieldValue;

			return _getConfigurationObject(
				fragmentConfigurationField.isLocalizable(),
				lengthConfigurationFieldValue.getValue(),
				lengthConfigurationFieldValue.getValue_i18n());
		}

		if (Objects.equals(
				configurationFieldValue.getType(),
				ConfigurationFieldValue.Type.NAVIGATION_MENU)) {

			NavigationMenuConfigurationFieldValue
				navigationMenuConfigurationFieldValue =
					(NavigationMenuConfigurationFieldValue)
						configurationFieldValue;

			return _getConfigurationJSONObject(
				fragmentConfigurationField.isLocalizable(),
				navigationMenuValue -> _getNavigationMenuJSONObject(
					layoutStructureItemImporterContext, navigationMenuValue),
				navigationMenuConfigurationFieldValue.getValue(),
				navigationMenuConfigurationFieldValue.getValue_i18n());
		}

		if (Objects.equals(
				configurationFieldValue.getType(),
				ConfigurationFieldValue.Type.SELECT)) {

			SelectConfigurationFieldValue selectConfigurationFieldValue =
				(SelectConfigurationFieldValue)configurationFieldValue;

			return _getConfigurationObject(
				fragmentConfigurationField.isLocalizable(),
				selectConfigurationFieldValue.getValue(),
				selectConfigurationFieldValue.getValue_i18n());
		}

		if (Objects.equals(
				configurationFieldValue.getType(),
				ConfigurationFieldValue.Type.TEXT)) {

			TextConfigurationFieldValue textConfigurationFieldValue =
				(TextConfigurationFieldValue)configurationFieldValue;

			return _getConfigurationObject(
				fragmentConfigurationField.isLocalizable(),
				textConfigurationFieldValue.getValue(),
				textConfigurationFieldValue.getValue_i18n());
		}

		if (Objects.equals(
				configurationFieldValue.getType(),
				ConfigurationFieldValue.Type.URL)) {

			// TODO

		}

		if (Objects.equals(
				configurationFieldValue.getType(),
				ConfigurationFieldValue.Type.VIDEO)) {

			// TODO

		}

		throw new UnsupportedOperationException();
	}

	private static JSONObject _getCategoryTreeNodeJSONObject(
			ItemExternalReference itemExternalReference,
			LayoutStructureItemImporterContext
				layoutStructureItemImporterContext)
		throws PortalException {

		if ((itemExternalReference == null) ||
			Validator.isNull(
				itemExternalReference.getExternalReferenceCode())) {

			return null;
		}

		Long groupId = ItemScopeUtil.getGroupId(
			layoutStructureItemImporterContext.getCompanyId(),
			itemExternalReference.getScope(),
			layoutStructureItemImporterContext.getGroupId());

		if (groupId == null) {
			if (Objects.equals(
					itemExternalReference.getClassName(),
					AssetCategory.class.getName())) {

				return _getCategoryTreeNodeMissingReferenceJSONObject(
					"Category", layoutStructureItemImporterContext.getGroupId(),
					itemExternalReference);
			}

			return _getCategoryTreeNodeMissingReferenceJSONObject(
				"Vocabulary", layoutStructureItemImporterContext.getGroupId(),
				itemExternalReference);
		}

		if (Objects.equals(
				itemExternalReference.getClassName(),
				AssetCategory.class.getName())) {

			AssetCategory assetCategory =
				AssetCategoryLocalServiceUtil.
					fetchAssetCategoryByExternalReferenceCode(
						itemExternalReference.getExternalReferenceCode(),
						groupId);

			if (assetCategory != null) {
				return JSONUtil.put(
					"categoryTreeNodeId",
					String.valueOf(assetCategory.getCategoryId())
				).put(
					"categoryTreeNodeType", "Category"
				).put(
					"title", assetCategory.getName()
				);
			}

			return _getCategoryTreeNodeMissingReferenceJSONObject(
				"Category", layoutStructureItemImporterContext.getGroupId(),
				itemExternalReference);
		}

		if (Objects.equals(
				itemExternalReference.getClassName(),
				AssetVocabulary.class.getName())) {

			AssetVocabulary assetVocabulary =
				AssetVocabularyLocalServiceUtil.
					fetchAssetVocabularyByExternalReferenceCode(
						itemExternalReference.getExternalReferenceCode(),
						groupId);

			if (assetVocabulary != null) {
				return JSONUtil.put(
					"categoryTreeNodeId",
					String.valueOf(assetVocabulary.getVocabularyId())
				).put(
					"categoryTreeNodeType", "Vocabulary"
				).put(
					"title", assetVocabulary.getName()
				);
			}

			return _getCategoryTreeNodeMissingReferenceJSONObject(
				"Vocabulary", layoutStructureItemImporterContext.getGroupId(),
				itemExternalReference);
		}

		throw new UnsupportedOperationException();
	}

	private static JSONObject _getCategoryTreeNodeMissingReferenceJSONObject(
			String categoryTreeNodeType, long groupId,
			ItemExternalReference itemExternalReference)
		throws PortalException {

		LogUtil.logOptionalReference(itemExternalReference, groupId);

		return JSONUtil.put(
			"categoryTreeNodeExternalReferenceCode",
			itemExternalReference.getExternalReferenceCode()
		).put(
			"categoryTreeNodeScopeExternalReferenceCode",
			ItemScopeUtil.getItemScopeExternalReferenceCode(
				itemExternalReference.getScope(), groupId)
		).put(
			"categoryTreeNodeType", categoryTreeNodeType
		);
	}

	private static JSONObject _getClassNameReferenceJSONObject(
		ClassNameReference classNameReference,
		LayoutStructureItemImporterContext layoutStructureItemImporterContext) {

		if (Validator.isNull(classNameReference.getClassName())) {
			return null;
		}

		JSONObject jsonObject = JSONUtil.put(
			"key", classNameReference.getClassName()
		).put(
			"type", InfoListProviderItemSelectorReturnType.class.getName()
		);

		InfoItemServiceRegistry infoItemServiceRegistry =
			layoutStructureItemImporterContext.getInfoItemServiceRegistry();

		if (infoItemServiceRegistry == null) {
			return jsonObject;
		}

		InfoCollectionProvider infoCollectionProvider =
			infoItemServiceRegistry.getInfoItemService(
				InfoCollectionProvider.class,
				classNameReference.getClassName());

		if (infoCollectionProvider == null) {
			LogUtil.logOptionalReference(
				InfoCollectionProvider.class, classNameReference.getClassName(),
				layoutStructureItemImporterContext.getCompanyId());

			return jsonObject;
		}

		return jsonObject.put(
			"itemSubtype",
			() -> {
				if (!(infoCollectionProvider instanceof
						SingleFormVariationInfoCollectionProvider)) {

					return null;
				}

				SingleFormVariationInfoCollectionProvider<?>
					singleFormVariationInfoCollectionProvider =
						(SingleFormVariationInfoCollectionProvider<?>)
							infoCollectionProvider;

				return singleFormVariationInfoCollectionProvider.
					getFormVariationKey();
			}
		).put(
			"itemType", infoCollectionProvider.getCollectionItemClassName()
		).put(
			"key", infoCollectionProvider.getKey()
		).put(
			"title",
			() -> infoCollectionProvider.getLabel(LocaleUtil.getDefault())
		);
	}

	private static JSONObject _getCollectionItemExternalReferenceJSONObject(
			CollectionItemExternalReference collectionItemExternalReference,
			LayoutStructureItemImporterContext
				layoutStructureItemImporterContext)
		throws Exception {

		if (Validator.isNull(
				collectionItemExternalReference.getExternalReferenceCode())) {

			return null;
		}

		Long groupId = ItemScopeUtil.getGroupId(
			layoutStructureItemImporterContext.getCompanyId(),
			collectionItemExternalReference.getScope(),
			layoutStructureItemImporterContext.getGroupId());

		if (groupId == null) {
			return _getCollectionItemExternalReferenceMissingReferenceJSONObject(
				collectionItemExternalReference,
				layoutStructureItemImporterContext.getGroupId());
		}

		AssetListEntry assetListEntry =
			AssetListEntryLocalServiceUtil.
				fetchAssetListEntryByExternalReferenceCode(
					collectionItemExternalReference.getExternalReferenceCode(),
					groupId);

		if (assetListEntry == null) {
			return _getCollectionItemExternalReferenceMissingReferenceJSONObject(
				collectionItemExternalReference,
				layoutStructureItemImporterContext.getGroupId());
		}

		return JSONUtil.put(
			"classNameId",
			String.valueOf(PortalUtil.getClassNameId(AssetListEntry.class))
		).put(
			"classPK", assetListEntry.getAssetListEntryId()
		).put(
			"itemSubtype", assetListEntry.getAssetEntrySubtype()
		).put(
			"itemType", assetListEntry.getAssetEntryType()
		).put(
			"title", assetListEntry.getTitle()
		).put(
			"type", InfoListItemSelectorReturnType.class.getName()
		);
	}

	private static JSONObject
			_getCollectionItemExternalReferenceMissingReferenceJSONObject(
				CollectionItemExternalReference collectionItemExternalReference,
				long groupId)
		throws Exception {

		LogUtil.logOptionalReference(
			collectionItemExternalReference.getClassName(),
			collectionItemExternalReference.getExternalReferenceCode(),
			collectionItemExternalReference.getScope(), groupId);

		return JSONUtil.put(
			"externalReferenceCode",
			collectionItemExternalReference.getExternalReferenceCode()
		).put(
			"scopeExternalReferenceCode",
			ItemScopeUtil.getScopeExternalReferenceCode(
				collectionItemExternalReference.getScope(), groupId)
		).put(
			"type", InfoListItemSelectorReturnType.class.getName()
		);
	}

	private static JSONObject _getCollectionJSONObject(
			CollectionReference collectionReference,
			LayoutStructureItemImporterContext
				layoutStructureItemImporterContext)
		throws Exception {

		if (collectionReference == null) {
			return null;
		}

		if (collectionReference instanceof ClassNameReference) {
			return _getClassNameReferenceJSONObject(
				(ClassNameReference)collectionReference,
				layoutStructureItemImporterContext);
		}

		return _getCollectionItemExternalReferenceJSONObject(
			(CollectionItemExternalReference)collectionReference,
			layoutStructureItemImporterContext);
	}

	private static JSONObject _getColorPaletteJSONObject(
		ColorPaletteValue colorPaletteValue) {

		if (colorPaletteValue == null) {
			return null;
		}

		return JSONUtil.put(
			"color", colorPaletteValue.getColor()
		).put(
			"cssClass", colorPaletteValue.getCssClass()
		).put(
			"rgbValue", colorPaletteValue.getRgbValue()
		);
	}

	private static <T> JSONObject _getConfigurationJSONObject(
			boolean localizable,
			UnsafeFunction<T, JSONObject, Exception> unsafeFunction, T value,
			Map<String, T> valuesMap)
		throws Exception {

		if (!localizable) {
			return unsafeFunction.apply(value);
		}

		return LocalizedValueUtil.toJSONObject(
			valuesMap, curValue -> unsafeFunction.apply(curValue));
	}

	private static <T> Object _getConfigurationObject(
		boolean localizable, T value, Map<String, T> valuesMap) {

		if (!localizable) {
			return value;
		}

		return LocalizedValueUtil.toJSONObject(valuesMap, curValue -> curValue);
	}

	private static JSONObject _getItemJSONObject(
		ItemValue itemValue,
		LayoutStructureItemImporterContext layoutStructureItemImporterContext) {

		if ((itemValue == null) || (itemValue.getItem() == null)) {
			return null;
		}

		ItemExternalReference itemExternalReference = itemValue.getItem();

		JSONObject jsonObject = InfoItemUtil.getMappedItemJSONObject(
			itemExternalReference.getClassName(),
			itemExternalReference.getExternalReferenceCode(), null,
			layoutStructureItemImporterContext.getInfoItemServiceRegistry(),
			itemExternalReference.getScope(),
			layoutStructureItemImporterContext.getGroupId());

		return jsonObject.put(
			"template",
			() -> {
				TemplateReference templateReference = itemValue.getTemplate();

				if (templateReference == null) {
					return null;
				}

				// TODO: Info Template's templateKey is an ID!!!

				return JSONUtil.put(
					"infoItemRendererKey", templateReference.getRendererKey()
				).put(
					"templateKey", templateReference.getTemplateKey()
				);
			});
	}

	private static Layout _getLayout(
		NavigationMenuValue navigationMenuValue, long scopeGroupId) {

		if ((navigationMenuValue.getNavigationMenu() != null) ||
			Validator.isNull(
				navigationMenuValue.getParentItemExternalReferenceCode())) {

			return null;
		}

		Layout layout =
			LayoutLocalServiceUtil.fetchLayoutByExternalReferenceCode(
				navigationMenuValue.getParentItemExternalReferenceCode(),
				scopeGroupId);

		if (layout == null) {
			LogUtil.logOptionalReference(
				Layout.class.getName(),
				navigationMenuValue.getParentItemExternalReferenceCode(), null,
				scopeGroupId);
		}

		return layout;
	}

	private static JSONObject _getNavigationMenuJSONObject(
		LayoutStructureItemImporterContext layoutStructureItemImporterContext,
		NavigationMenuValue navigationMenuValue) {

		if (navigationMenuValue == null) {
			return null;
		}

		ItemExternalReference siteNavigationMenuItemExternalReference =
			navigationMenuValue.getNavigationMenu();

		SiteNavigationMenu siteNavigationMenu = _getSiteNavigationMenu(
			layoutStructureItemImporterContext.getCompanyId(),
			siteNavigationMenuItemExternalReference,
			layoutStructureItemImporterContext.getGroupId());

		SiteNavigationMenuItem siteNavigationMenuItem =
			_getSiteNavigationMenuItem(
				navigationMenuValue,
				layoutStructureItemImporterContext.getGroupId(),
				siteNavigationMenu);

		Layout layout = _getLayout(
			navigationMenuValue,
			layoutStructureItemImporterContext.getGroupId());

		return JSONUtil.put(
			"parentSiteNavigationMenuItemExternalReferenceCode",
			() -> {
				if (layout != null) {
					return layout.getExternalReferenceCode();
				}

				if (siteNavigationMenuItem == null) {
					return null;
				}

				return siteNavigationMenuItem.getExternalReferenceCode();
			}
		).put(
			"parentSiteNavigationMenuItemId",
			() -> {
				if (layout != null) {
					return String.valueOf(layout.getPlid());
				}

				if (siteNavigationMenuItem == null) {
					return null;
				}

				return String.valueOf(
					siteNavigationMenuItem.getSiteNavigationMenuItemId());
			}
		).put(
			"privateLayout", navigationMenuValue::getPrivatePages
		).put(
			"siteNavigationMenuExternalReferenceCode",
			() -> {
				if (siteNavigationMenuItem != null) {
					return siteNavigationMenuItem.getExternalReferenceCode();
				}

				if (siteNavigationMenuItemExternalReference == null) {
					return null;
				}

				return siteNavigationMenuItemExternalReference.
					getExternalReferenceCode();
			}
		).put(
			"siteNavigationMenuItemId",
			() -> {
				if (siteNavigationMenu == null) {
					return null;
				}

				return String.valueOf(
					siteNavigationMenu.getSiteNavigationMenuId());
			}
		).put(
			"siteNavigationMenuScopeExternalReferenceCode",
			() -> {
				if ((siteNavigationMenuItemExternalReference == null) ||
					(siteNavigationMenuItemExternalReference.getScope() ==
						null)) {

					return null;
				}

				Scope scope =
					siteNavigationMenuItemExternalReference.getScope();

				return scope.getExternalReferenceCode();
			}
		).put(
			"title",
			() -> {
				if (siteNavigationMenu != null) {
					return siteNavigationMenu.getName();
				}

				if (siteNavigationMenuItemExternalReference != null) {
					return null;
				}

				if (GetterUtil.getBoolean(
						navigationMenuValue.getPrivatePages())) {

					return LanguageUtil.get(
						LocaleUtil.getDefault(), "private-pages");
				}

				return LanguageUtil.get(
					LocaleUtil.getDefault(), "public-pages");
			}
		).put(
			"type",
			() -> SiteNavigationMenuItemSelectorReturnType.class.getName()
		);
	}

	private static SiteNavigationMenu _getSiteNavigationMenu(
		long companyId, ItemExternalReference itemExternalReference,
		long scopeGroupId) {

		if (itemExternalReference == null) {
			return null;
		}

		Long groupId = ItemScopeUtil.getGroupId(
			companyId, itemExternalReference.getScope(), scopeGroupId);

		if (groupId == null) {
			LogUtil.logOptionalReference(itemExternalReference, scopeGroupId);

			return null;
		}

		SiteNavigationMenu siteNavigationMenu =
			SiteNavigationMenuLocalServiceUtil.
				fetchSiteNavigationMenuByExternalReferenceCode(
					itemExternalReference.getExternalReferenceCode(), groupId);

		if (siteNavigationMenu == null) {
			LogUtil.logOptionalReference(itemExternalReference, scopeGroupId);
		}

		return siteNavigationMenu;
	}

	private static SiteNavigationMenuItem _getSiteNavigationMenuItem(
		NavigationMenuValue navigationMenuValue, long scopeGroupId,
		SiteNavigationMenu siteNavigationMenu) {

		if ((siteNavigationMenu == null) ||
			Validator.isNull(
				navigationMenuValue.getParentItemExternalReferenceCode())) {

			return null;
		}

		SiteNavigationMenuItem siteNavigationMenuItem =
			SiteNavigationMenuItemLocalServiceUtil.
				fetchSiteNavigationMenuItemByExternalReferenceCode(
					navigationMenuValue.getParentItemExternalReferenceCode(),
					siteNavigationMenu.getGroupId());

		if (siteNavigationMenuItem == null) {
			ItemExternalReference siteNavigationMenuItemExternalReference =
				navigationMenuValue.getNavigationMenu();

			LogUtil.logOptionalReference(
				SiteNavigationMenuItem.class.getName(),
				navigationMenuValue.getParentItemExternalReferenceCode(),
				siteNavigationMenuItemExternalReference.getScope(),
				scopeGroupId);
		}

		return siteNavigationMenuItem;
	}

}