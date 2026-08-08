/**
 * SPDX-FileCopyrightText: (c) 2026 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

import {DesignLibraryItemData, DesignLibraryResourceType} from '../types';

/**
 * Matches a listed row to the resource type that contributed it. Several types
 * may share an entry class name, so a type that declares a type only matches
 * rows whose own type field holds that value.
 */
export default function findResourceType(
	resourceTypes: DesignLibraryResourceType[],
	itemData?: DesignLibraryItemData
): DesignLibraryResourceType | undefined {
	if (!itemData?.entryClassName) {
		return undefined;
	}

	return resourceTypes.find((resourceType) => {
		if (resourceType.entryClassName !== itemData.entryClassName) {
			return false;
		}

		if (resourceType.type === undefined || resourceType.type === null) {
			return true;
		}

		return String(resourceType.type) === String(itemData.type);
	});
}
