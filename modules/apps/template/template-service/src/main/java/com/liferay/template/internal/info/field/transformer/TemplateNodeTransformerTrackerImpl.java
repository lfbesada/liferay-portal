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

package com.liferay.template.internal.info.field.transformer;

import com.liferay.osgi.service.tracker.collections.map.PropertyServiceReferenceComparator;
import com.liferay.osgi.service.tracker.collections.map.ServiceTrackerMap;
import com.liferay.osgi.service.tracker.collections.map.ServiceTrackerMapFactory;
import com.liferay.template.info.field.transformer.TemplateNodeTransformer;

import java.util.ArrayList;
import java.util.List;

import org.osgi.framework.BundleContext;
import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;

/**
 * @author Lourdes Fernández Besada
 */
@Component(service = TemplateNodeTransformerTracker.class)
public class TemplateNodeTransformerTrackerImpl
	implements TemplateNodeTransformerTracker {

	@Override
	public TemplateNodeTransformer getTemplateNodeTransformer(
		String className) {

		return _templateNodeTransformerServiceTrackerMap.getService(className);
	}

	@Override
	public List<TemplateNodeTransformer> getTemplateNodeTransformers() {
		return new ArrayList(
			_templateNodeTransformerServiceTrackerMap.values());
	}

	@Activate
	protected void activate(BundleContext bundleContext) {
		_templateNodeTransformerServiceTrackerMap =
			ServiceTrackerMapFactory.openSingleValueMap(
				bundleContext,
				(Class<TemplateNodeTransformer>)
					(Class)TemplateNodeTransformer.class,
				null,
				(serviceReference, emitter) -> {
					TemplateNodeTransformer templateNodeTransformer =
						bundleContext.getService(serviceReference);

					try {
						emitter.emit(templateNodeTransformer.getClassName());
					}
					finally {
						bundleContext.ungetService(serviceReference);
					}
				},
				new PropertyServiceReferenceComparator<>("service.ranking"));
	}

	private ServiceTrackerMap<String, TemplateNodeTransformer>
		_templateNodeTransformerServiceTrackerMap;

}