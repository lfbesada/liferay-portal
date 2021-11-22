/**
 * Copyright (c) 2000-present Liferay, Inc. All rights reserved.
 *
 * This library is free software; you can redistribute it and/or modify it under
 * the terms of the GNU Lesser General Public License as published by the Free
 * Software Foundation; either version 2.1 of the License, or (at your option)
 * any later version.
 *
 * This library is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU Lesser General Public License for more
 * details.
 */

package com.liferay.site.navigation.menu.item.display.page.internal.portlet.action;

import com.liferay.asset.kernel.model.AssetCategory;
import com.liferay.asset.kernel.model.AssetVocabulary;
import com.liferay.asset.kernel.model.AssetVocabularyConstants;
import com.liferay.asset.kernel.service.AssetCategoryLocalService;
import com.liferay.asset.kernel.service.AssetVocabularyLocalService;
import com.liferay.portal.kernel.portlet.bridges.mvc.MVCActionCommand;
import com.liferay.portal.kernel.theme.ThemeDisplay;
import com.liferay.portal.kernel.util.ListUtil;
import com.liferay.portal.kernel.util.MapUtil;
import com.liferay.portal.kernel.util.Validator;
import com.liferay.portal.kernel.util.WebKeys;
import com.liferay.portlet.asset.util.comparator.AssetVocabularyGroupLocalizedTitleComparator;
import com.liferay.site.navigation.admin.constants.SiteNavigationAdminPortletKeys;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import javax.portlet.ActionRequest;

import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

/**
 * @author Lourdes Fernández Besada
 */
@Component(
	immediate = true,
	property = {
		"javax.portlet.name=" + SiteNavigationAdminPortletKeys.SITE_NAVIGATION_ADMIN,
		"mvc.command.name=/navigation_menu/add_multiple_asset_category_site_navigation_menu_item"
	},
	service = MVCActionCommand.class
)
public class AddMultipleAssetCategorySiteNavigationMenuItemsMVCActionCommand
	extends AddMultipleDisplayPageTypeSiteNavigationMenuItemMVCActionCommand {

	@Override
	protected List<InfoItemItemSelectorReturnItem>
			getInfoItemItemSelectorReturnItems(
				ActionRequest actionRequest, String siteNavigationMenuItemType)
		throws Exception {

		List<InfoItemItemSelectorReturnItem> infoItemItemSelectorReturnItems =
			super.getInfoItemItemSelectorReturnItems(
				actionRequest, siteNavigationMenuItemType);

		Stream<InfoItemItemSelectorReturnItem> stream =
			infoItemItemSelectorReturnItems.stream();

		Map<Long, Map<Long, InfoItemItemSelectorReturnItem>>
			itemsByVocabularyIdMap = stream.collect(
				Collectors.groupingBy(
					infoItemItemSelectorReturnItem -> {
						AssetCategory assetCategory =
							_assetCategoryLocalService.fetchAssetCategory(
								infoItemItemSelectorReturnItem.getClassPK());

						return assetCategory.getVocabularyId();
					},
					Collectors.toMap(
						InfoItemItemSelectorReturnItem::getClassPK,
						Function.identity())));

		ThemeDisplay themeDisplay = (ThemeDisplay)actionRequest.getAttribute(
			WebKeys.THEME_DISPLAY);

		List<InfoItemItemSelectorReturnItem> itemsHierarchy = new ArrayList<>();

		for (long vocabularyId : _getOrderedVocabularyIds(themeDisplay)) {
			Map<Long, InfoItemItemSelectorReturnItem> itemsByCategoryId =
				itemsByVocabularyIdMap.get(vocabularyId);

			if (MapUtil.isEmpty(itemsByCategoryId)) {
				continue;
			}

			Set<Long> categoryIds = itemsByCategoryId.keySet();

			Map<Long, List<InfoItemItemSelectorReturnItem>>
				itemsByParentCategoryIdMap = new HashMap<>();

			for (InfoItemItemSelectorReturnItem infoItemItemSelectorReturnItem :
					itemsByCategoryId.values()) {

				AssetCategory assetCategory =
					_assetCategoryLocalService.fetchAssetCategory(
						infoItemItemSelectorReturnItem.getClassPK());

				long parentCategoryId = _getClosestParentCategoryId(
					assetCategory, categoryIds);

				List<InfoItemItemSelectorReturnItem> children =
					itemsByParentCategoryIdMap.get(parentCategoryId);

				if (children == null) {
					children = new ArrayList<>();

					itemsByParentCategoryIdMap.put(parentCategoryId, children);
				}

				children.add(infoItemItemSelectorReturnItem);
			}

			itemsHierarchy.addAll(_getChildren(itemsByParentCategoryIdMap, 0L));
		}

		return itemsHierarchy;
	}

	private List<InfoItemItemSelectorReturnItem> _getChildren(
		Map<Long, List<InfoItemItemSelectorReturnItem>>
			itemsByParentCategoryIdMap,
		long parentCategoryId) {

		if (!itemsByParentCategoryIdMap.containsKey(parentCategoryId)) {
			return Collections.emptyList();
		}

		List<InfoItemItemSelectorReturnItem> children = 
			itemsByParentCategoryIdMap.get(parentCategoryId);

		for (InfoItemItemSelectorReturnItem infoItemItemSelectorReturnItem :
				children) {

			infoItemItemSelectorReturnItem.setChildren(
				_getChildren(
					itemsByParentCategoryIdMap,
					infoItemItemSelectorReturnItem.getClassPK()));
		}

		return children;
	}

	private long _getClosestParentCategoryId(
		AssetCategory assetCategory, Set<Long> availableCategoryIds) {

		String treePath = assetCategory.getTreePath();

		Stream<String> stream = Arrays.stream(treePath.split("/"));

		return stream.filter(
			s -> Validator.isNotNull(s)
		).mapToLong(
			Long::valueOf
		).filter(
			categoryId -> !Objects.equals(
				categoryId, assetCategory.getCategoryId())
		).boxed(
		).sorted(
			Collections.reverseOrder()
		).filter(
			parentCategoryId -> availableCategoryIds.contains(parentCategoryId)
		).findFirst(
		).orElse(
			0L
		);
	}

	private List<Long> _getOrderedVocabularyIds(ThemeDisplay themeDisplay) {
		List<AssetVocabulary> assetVocabularies =
			_assetVocabularyLocalService.getGroupVocabularies(
				new long[] {
					themeDisplay.getCompanyGroupId(),
					themeDisplay.getScopeGroupId()
				},
				new int[] {AssetVocabularyConstants.VISIBILITY_TYPE_PUBLIC});

		if (assetVocabularies.isEmpty()) {
			return Collections.emptyList();
		}

		ListUtil.sort(
			assetVocabularies,
			new AssetVocabularyGroupLocalizedTitleComparator(
				themeDisplay.getScopeGroupId(), themeDisplay.getLocale(),
				true));

		return ListUtil.toList(
			assetVocabularies, AssetVocabulary.VOCABULARY_ID_ACCESSOR);
	}

	@Reference
	private AssetCategoryLocalService _assetCategoryLocalService;

	@Reference
	private AssetVocabularyLocalService _assetVocabularyLocalService;

}