/**
 * SPDX-FileCopyrightText: (c) 2025 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.layout.admin.web.internal.info.item.provider;

import com.liferay.info.item.InfoItemClassDetails;
import com.liferay.info.item.InfoItemDetails;
import com.liferay.info.item.InfoItemReference;
import com.liferay.info.item.provider.InfoItemDetailsProvider;
import com.liferay.portal.kernel.model.Layout;

import org.osgi.service.component.annotations.Component;

/**
 * @author Lourdes Fernández Besada
 */
@Component(service = InfoItemDetailsProvider.class)
public class LayoutInfoItemDetailsProvider
	implements InfoItemDetailsProvider<Layout> {

	@Override
	public InfoItemClassDetails getInfoItemClassDetails() {
		return new InfoItemClassDetails(Layout.class.getName());
	}

	@Override
	public InfoItemDetails getInfoItemDetails(Layout layout) {
		return new InfoItemDetails(
			getInfoItemClassDetails(),
			new InfoItemReference(Layout.class.getName(), layout.getPlid()));
	}

}