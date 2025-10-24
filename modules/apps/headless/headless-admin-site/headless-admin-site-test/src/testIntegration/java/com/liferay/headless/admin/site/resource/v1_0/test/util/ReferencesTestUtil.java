/**
 * SPDX-FileCopyrightText: (c) 2025 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.headless.admin.site.resource.v1_0.test.util;

import com.liferay.asset.kernel.model.AssetCategory;
import com.liferay.asset.kernel.model.AssetVocabulary;
import com.liferay.asset.list.model.AssetListEntry;
import com.liferay.headless.admin.site.client.dto.v1_0.ClassNameReference;
import com.liferay.headless.admin.site.client.dto.v1_0.CollectionItemExternalReference;
import com.liferay.headless.admin.site.client.dto.v1_0.CollectionReference;
import com.liferay.headless.admin.site.client.dto.v1_0.ItemExternalReference;
import com.liferay.journal.model.JournalArticle;
import com.liferay.portal.kernel.repository.model.FileEntry;
import com.liferay.portal.kernel.util.GetterUtil;
import com.liferay.site.navigation.model.SiteNavigationMenu;

/**
 * @author Lourdes Fernández Besada
 */
public class ReferencesTestUtil {

	public static CollectionReference getCollectionReference(
		Object object, long scopeGroupId) {

		if (object == null) {
			return null;
		}

		if (object instanceof String) {
			return new ClassNameReference() {
				{
					setClassName(() -> GetterUtil.getString(object));
					setCollectionType(() -> CollectionType.COLLECTION_PROVIDER);
				}
			};
		}

		if (!(object instanceof AssetListEntry)) {
			return null;
		}

		AssetListEntry assetListEntry = (AssetListEntry)object;

		return new CollectionItemExternalReference() {
			{
				setClassName(() -> AssetListEntry.class.getName());
				setCollectionType(() -> CollectionType.COLLECTION);
				setExternalReferenceCode(
					assetListEntry::getExternalReferenceCode);
				setScope(
					() -> ScopeTestUtil.getItemScope(
						assetListEntry.getGroupId(), scopeGroupId));
			}
		};
	}

	public static ItemExternalReference getItemExternalReference(
		Object object, long scopeGroupId) {

		if (object == null) {
			return null;
		}

		if (object instanceof AssetCategory) {
			AssetCategory assetCategory = (AssetCategory)object;

			return getItemExternalReference(
				AssetCategory.class.getName(),
				assetCategory.getExternalReferenceCode(),
				assetCategory.getGroupId(), scopeGroupId);
		}

		if (object instanceof AssetVocabulary) {
			AssetVocabulary assetVocabulary = (AssetVocabulary)object;

			return getItemExternalReference(
				AssetVocabulary.class.getName(),
				assetVocabulary.getExternalReferenceCode(),
				assetVocabulary.getGroupId(), scopeGroupId);
		}

		if (object instanceof FileEntry) {
			FileEntry fileEntry = (FileEntry)object;

			return getItemExternalReference(
				FileEntry.class.getName(), fileEntry.getExternalReferenceCode(),
				fileEntry.getGroupId(), scopeGroupId);
		}

		if (object instanceof JournalArticle) {
			JournalArticle journalArticle = (JournalArticle)object;

			return getItemExternalReference(
				JournalArticle.class.getName(),
				journalArticle.getExternalReferenceCode(),
				journalArticle.getGroupId(), scopeGroupId);
		}

		if (object instanceof SiteNavigationMenu) {
			SiteNavigationMenu siteNavigationMenu = (SiteNavigationMenu)object;

			return getItemExternalReference(
				SiteNavigationMenu.class.getName(),
				siteNavigationMenu.getExternalReferenceCode(),
				siteNavigationMenu.getGroupId(), scopeGroupId);
		}

		return null;
	}

	public static ItemExternalReference getItemExternalReference(
		String className, String externalReferenceCode, long itemGroupId,
		long scopeGroupId) {

		ItemExternalReference itemExternalReference =
			new ItemExternalReference();

		itemExternalReference.setClassName(className);
		itemExternalReference.setExternalReferenceCode(externalReferenceCode);
		itemExternalReference.setScope(
			() -> ScopeTestUtil.getItemScope(itemGroupId, scopeGroupId));

		return itemExternalReference;
	}

}