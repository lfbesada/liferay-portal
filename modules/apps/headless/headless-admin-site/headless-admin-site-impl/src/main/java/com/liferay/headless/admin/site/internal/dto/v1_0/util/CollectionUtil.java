/**
 * SPDX-FileCopyrightText: (c) 2025 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.headless.admin.site.internal.dto.v1_0.util;

import com.liferay.asset.list.model.AssetListEntry;
import com.liferay.asset.list.service.AssetListEntryLocalServiceUtil;
import com.liferay.headless.admin.site.dto.v1_0.ClassNameReference;
import com.liferay.headless.admin.site.dto.v1_0.CollectionItemExternalReference;
import com.liferay.headless.admin.site.dto.v1_0.CollectionReference;
import com.liferay.info.list.provider.item.selector.criterion.InfoListProviderItemSelectorReturnType;
import com.liferay.item.selector.criteria.InfoListItemSelectorReturnType;
import com.liferay.portal.kernel.json.JSONObject;
import com.liferay.portal.kernel.util.Validator;

import java.util.Objects;

/**
 * @author Lourdes Fernández Besada
 */
public class CollectionUtil {

	public static CollectionReference getCollectionReference(
		long companyId, JSONObject jsonObject, long scopeGroupId) {

		if (jsonObject == null) {
			return null;
		}

		String type = jsonObject.getString("type");

		if (Validator.isNull(type)) {
			return null;
		}

		if (Objects.equals(
				type, InfoListItemSelectorReturnType.class.getName())) {

			AssetListEntry assetListEntry =
				AssetListEntryLocalServiceUtil.fetchAssetListEntry(
					jsonObject.getLong("classPK"));

			if (assetListEntry != null) {
				return new CollectionItemExternalReference() {
					{
						setCollectionType(CollectionType.COLLECTION);
						setExternalReferenceCode(
							assetListEntry::getExternalReferenceCode);
					}
				};
			}

			if (Validator.isNull(
					jsonObject.getString("externalReferenceCode"))) {

				return null;
			}

			return new CollectionItemExternalReference() {
				{
					setCollectionType(CollectionType.COLLECTION);
					setExternalReferenceCode(
						() -> jsonObject.getString("externalReferenceCode"));
					setScope(
						() -> ItemScopeUtil.getItemScope(
							companyId,
							jsonObject.getString("scopeExternalReferenceCode"),
							scopeGroupId));
				}
			};
		}
		else if (Objects.equals(
					type,
					InfoListProviderItemSelectorReturnType.class.getName())) {

			return new ClassNameReference() {
				{
					setClassName(() -> jsonObject.getString("key"));
					setCollectionType(CollectionType.COLLECTION_PROVIDER);
				}
			};
		}

		return null;
	}

}