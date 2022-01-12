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

package com.liferay.layout.display.page.internal;

import com.liferay.layout.display.page.LayoutDisplayPageDetailsProvider;
import com.liferay.layout.display.page.LayoutDisplayPageDetailsProviderTracker;
import com.liferay.osgi.service.tracker.collections.map.PropertyServiceReferenceComparator;
import com.liferay.osgi.service.tracker.collections.map.ServiceTrackerMap;
import com.liferay.osgi.service.tracker.collections.map.ServiceTrackerMapFactory;

import java.util.ArrayList;
import java.util.List;

import org.osgi.framework.BundleContext;
import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;

/**
 * @author Lourdes Fernández Besada
 */
@Component(service = LayoutDisplayPageDetailsProviderTracker.class)
public class LayoutDisplayPageDetailsProviderTrackerImpl
	implements LayoutDisplayPageDetailsProviderTracker {

	@Override
	public LayoutDisplayPageDetailsProvider<?>
		getLayoutDisplayPageDetailsProvider(String className) {

		return _layoutDisplayPageDetailsProviderServiceTrackerMap.getService(
			className);
	}

	@Override
	public List<LayoutDisplayPageDetailsProvider<?>>
		getLayoutDisplayPageDetailsProviders() {

		return new ArrayList(
			_layoutDisplayPageDetailsProviderServiceTrackerMap.values());
	}

	@Activate
	protected void activate(BundleContext bundleContext) {
		_layoutDisplayPageDetailsProviderServiceTrackerMap =
			ServiceTrackerMapFactory.openSingleValueMap(
				bundleContext,
				(Class<LayoutDisplayPageDetailsProvider<?>>)
					(Class<?>)LayoutDisplayPageDetailsProvider.class,
				null,
				(serviceReference, emitter) -> {
					LayoutDisplayPageDetailsProvider<?>
						layoutDisplayPageDetailsProvider =
							bundleContext.getService(serviceReference);

					try {
						emitter.emit(
							layoutDisplayPageDetailsProvider.getClassName());
					}
					finally {
						bundleContext.ungetService(serviceReference);
					}
				},
				new PropertyServiceReferenceComparator<>("service.ranking"));
	}

	private ServiceTrackerMap<String, LayoutDisplayPageDetailsProvider<?>>
		_layoutDisplayPageDetailsProviderServiceTrackerMap;

}