/**
 * SPDX-FileCopyrightText: (c) 2024 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.asset.list.internal.upgrade.v2_1_1;

import com.liferay.portal.kernel.upgrade.UpgradeProcess;

/**
 * @author Lourdes Fernández Besada
 */
public class AssetListEntryAssetEntryRelUpgradeProcess extends UpgradeProcess {

	@Override
	protected void doUpgrade() throws Exception {
		runSQL(
			"delete from AssetListEntryAssetEntryRel where not exists " +
				"(select 1 from AssetEntry where entryId = assetEntryId)");
	}

}