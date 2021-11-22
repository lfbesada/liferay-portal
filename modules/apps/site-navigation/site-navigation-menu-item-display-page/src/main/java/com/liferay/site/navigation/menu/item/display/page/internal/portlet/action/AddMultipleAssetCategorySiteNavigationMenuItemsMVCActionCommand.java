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
import com.liferay.asset.kernel.service.AssetCategoryLocalService;
import com.liferay.portal.kernel.portlet.bridges.mvc.MVCActionCommand;
import com.liferay.site.navigation.admin.constants.SiteNavigationAdminPortletKeys;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
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

		Map<Long, List<InfoItemItemSelectorReturnItem>>
			itemsByVocabularyIdMap = stream.collect(
				Collectors.groupingBy(
					infoItemItemSelectorReturnItem -> {
						AssetCategory assetCategory =
							_assetCategoryLocalService.fetchAssetCategory(
								infoItemItemSelectorReturnItem.getClassPK());

						return assetCategory.getVocabularyId();
					},
					Collectors.toList()));

		List<InfoItemItemSelectorReturnItem> itemsHierarchy = new ArrayList<>();

		for(List<InfoItemItemSelectorReturnItem> vocabularyItems
			: itemsByVocabularyIdMap.values()) {
			itemsHierarchy.addAll(vocabularyItems);
		}

		return itemsHierarchy;
	}
	

	@Reference
	private AssetCategoryLocalService _assetCategoryLocalService;

}