/**
 * SPDX-FileCopyrightText: (c) 2024 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.headless.admin.site.internal.dto.v1_0.util;

import com.liferay.headless.admin.site.dto.v1_0.PageElement;
import com.liferay.layout.util.constants.LayoutDataItemTypeConstants;
import com.liferay.portal.kernel.util.HashMapBuilder;

import java.util.Map;
import java.util.Objects;
import java.util.Set;

/**
 * @author Lourdes Fernández Besada
 */
public class PageElementTypeUtil {

	public static PageElement.Type toExternalType(String internalType) {
		if (_internalToExternalValuesMap.containsKey(internalType)) {
			return _internalToExternalValuesMap.get(internalType);
		}

		throw new UnsupportedOperationException();
	}

	public static String toInternalType(PageElement.Type externalType) {
		Set<String> internalTypes = _internalToExternalValuesMap.keySet();

		for (String internalType : internalTypes) {
			if (Objects.equals(
					_internalToExternalValuesMap.get(internalType),
					externalType)) {

				return internalType;
			}
		}

		throw new UnsupportedOperationException();
	}

	private static final Map<String, PageElement.Type>
		_internalToExternalValuesMap = HashMapBuilder.put(
			LayoutDataItemTypeConstants.TYPE_COLLECTION,
			PageElement.Type.COLLECTION
		).put(
			LayoutDataItemTypeConstants.TYPE_COLLECTION_ITEM,
			PageElement.Type.COLLECTION_ITEM
		).put(
			LayoutDataItemTypeConstants.TYPE_COLUMN, PageElement.Type.COLUMN
		).put(
			LayoutDataItemTypeConstants.TYPE_CONTAINER,
			PageElement.Type.CONTAINER
		).put(
			LayoutDataItemTypeConstants.TYPE_DROP_ZONE,
			PageElement.Type.DROP_ZONE
		).put(
			LayoutDataItemTypeConstants.TYPE_FORM, PageElement.Type.FORM
		).put(
			LayoutDataItemTypeConstants.TYPE_FORM_STEP,
			PageElement.Type.FORM_STEP
		).put(
			LayoutDataItemTypeConstants.TYPE_FORM_STEP_CONTAINER,
			PageElement.Type.FORM_STEP_CONTAINER
		).put(
			LayoutDataItemTypeConstants.TYPE_FRAGMENT, PageElement.Type.FRAGMENT
		).put(
			LayoutDataItemTypeConstants.TYPE_FRAGMENT_DROP_ZONE,
			PageElement.Type.FRAGMENT_DROP_ZONE
		).put(
			LayoutDataItemTypeConstants.TYPE_ROW, PageElement.Type.ROW
		).build();

}