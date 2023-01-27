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

package com.liferay.layout.content.page.editor.web.internal.layout.list;

import com.liferay.layout.list.permission.provider.LayoutListPermissionProviderRegistry;
import com.liferay.layout.list.retriever.LayoutListRetrieverRegistry;
import com.liferay.layout.list.retriever.ListObjectReferenceFactoryRegistry;

import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

/**
 * @author Lourdes Fernández Besada
 */
@Component(service = {})
public class LayoutListRegistryUtil {

	public static LayoutListPermissionProviderRegistry
		getLayoutListPermissionProviderRegistry() {

		return _layoutListPermissionProviderRegistry;
	}

	public static LayoutListRetrieverRegistry getLayoutListRetrieverRegistry() {
		return _layoutListRetrieverRegistry;
	}

	public static ListObjectReferenceFactoryRegistry
		getListObjectReferenceFactoryRegistry() {

		return _listObjectReferenceFactoryRegistry;
	}

	@Reference(unbind = "-")
	protected void setLayoutListPermissionProviderRegistry(
		LayoutListPermissionProviderRegistry
			layoutListPermissionProviderRegistry) {

		_layoutListPermissionProviderRegistry =
			layoutListPermissionProviderRegistry;
	}

	@Reference(unbind = "-")
	protected void setLayoutListRetrieverRegistry(
		LayoutListRetrieverRegistry layoutListRetrieverRegistry) {

		_layoutListRetrieverRegistry = layoutListRetrieverRegistry;
	}

	@Reference(unbind = "-")
	protected void setListObjectReferenceFactoryRegistry(
		ListObjectReferenceFactoryRegistry listObjectReferenceFactoryRegistry) {

		_listObjectReferenceFactoryRegistry =
			listObjectReferenceFactoryRegistry;
	}

	private static LayoutListPermissionProviderRegistry
		_layoutListPermissionProviderRegistry;
	private static LayoutListRetrieverRegistry _layoutListRetrieverRegistry;
	private static ListObjectReferenceFactoryRegistry
		_listObjectReferenceFactoryRegistry;

}