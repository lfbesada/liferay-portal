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

package com.liferay.layout.page.template.admin.web.internal.headless.delivery.dto.v1_0.structure.importer;

import com.liferay.portal.kernel.model.Layout;

/**
 * @author Lourdes Fernández Besada
 */
public class LayoutStructureItemImporterContext {

	public LayoutStructureItemImporterContext(
		Layout layout, String parentItemId, int position) {

		_parentItemId = parentItemId;
		_position = position;

		_companyId = layout.getCompanyId();
		_groupId = layout.getGroupId();
		_plid = layout.getPlid();
		_userId = layout.getUserId();
	}

	public LayoutStructureItemImporterContext(
		long companyId, long groupId, String parentItemId, long plid,
		int position, long userId) {

		_companyId = companyId;
		_groupId = groupId;
		_parentItemId = parentItemId;
		_plid = plid;
		_position = position;
		_userId = userId;
	}

	public long getCompanyId() {
		return _companyId;
	}

	public long getGroupId() {
		return _groupId;
	}

	public String getParentItemId() {
		return _parentItemId;
	}

	public long getPlid() {
		return _plid;
	}

	public int getPosition() {
		return _position;
	}

	public long getUserId() {
		return _userId;
	}

	private final long _companyId;
	private final long _groupId;
	private final String _parentItemId;
	private final long _plid;
	private final int _position;
	private final long _userId;

}