/**
 * SPDX-FileCopyrightText: (c) 2024 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.asset.publisher.web.internal.util;

import com.liferay.asset.list.model.AssetListEntry;
import com.liferay.asset.list.service.AssetListEntryServiceUtil;
import com.liferay.asset.publisher.web.internal.configuration.AssetPublisherSelectionStyleConfigurationUtil;
import com.liferay.asset.publisher.web.internal.constants.AssetPublisherSelectionStyleConstants;
import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.feature.flag.FeatureFlagManagerUtil;
import com.liferay.portal.kernel.model.Group;
import com.liferay.portal.kernel.service.GroupServiceUtil;
import com.liferay.portal.kernel.util.GetterUtil;
import com.liferay.portal.kernel.util.Validator;

import javax.portlet.PortletPreferences;

/**
 * @author Lourdes Fernández Besada
 */
public class AssetPublisherUtil {

	public static AssetListEntry getAssetListEntry(
			long companyId, long groupId, PortletPreferences portletPreferences)
		throws PortalException {

		String selectionStyle = GetterUtil.getString(
			portletPreferences.getValue("selectionStyle", null),
			AssetPublisherSelectionStyleConfigurationUtil.
				defaultSelectionStyle());

		if (!selectionStyle.equals(
				AssetPublisherSelectionStyleConstants.TYPE_ASSET_LIST)) {

			return null;
		}

		if (!FeatureFlagManagerUtil.isEnabled("LPD-22837")) {
			return AssetListEntryServiceUtil.fetchAssetListEntry(
				GetterUtil.getLong(
					portletPreferences.getValue("assetListEntryId", null)));
		}

		String assetListEntryExternalReferenceCode = GetterUtil.getString(
			portletPreferences.getValue(
				"assetListEntryExternalReferenceCode", null));

		if (Validator.isNull(assetListEntryExternalReferenceCode)) {
			return null;
		}

		String assetListEntryScopeExternalReferenceCode = GetterUtil.getString(
			portletPreferences.getValue(
				"assetListEntryScopeExternalReferenceCode", null));

		if (Validator.isNull(assetListEntryScopeExternalReferenceCode)) {
			return AssetListEntryServiceUtil.
				fetchAssetListEntryByExternalReferenceCode(
					assetListEntryScopeExternalReferenceCode, groupId);
		}

		Group group = GroupServiceUtil.fetchGroupByExternalReferenceCode(
			assetListEntryScopeExternalReferenceCode, companyId);

		if (group == null) {
			return null;
		}

		return AssetListEntryServiceUtil.
			fetchAssetListEntryByExternalReferenceCode(
				assetListEntryScopeExternalReferenceCode, group.getGroupId());
	}

}