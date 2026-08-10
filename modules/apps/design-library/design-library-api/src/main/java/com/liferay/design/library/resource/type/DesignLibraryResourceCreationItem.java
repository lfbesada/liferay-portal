/**
 * SPDX-FileCopyrightText: (c) 2026 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.design.library.resource.type;

/**
 * @author Lourdes Fernández Besada
 */
public class DesignLibraryResourceCreationItem {

	public DesignLibraryResourceCreationItem(
		String id, String label, String url) {

		_id = id;
		_label = label;
		_url = url;
	}

	public String getId() {
		return _id;
	}

	public String getLabel() {
		return _label;
	}

	public String getURL() {
		return _url;
	}

	private final String _id;
	private final String _label;
	private final String _url;

}
