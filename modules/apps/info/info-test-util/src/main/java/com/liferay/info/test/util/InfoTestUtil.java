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

package com.liferay.info.test.util;

import com.liferay.info.field.InfoFieldSet;
import com.liferay.info.item.capability.InfoItemCapability;
import com.liferay.info.item.creator.InfoItemCreator;
import com.liferay.info.item.provider.InfoItemCapabilitiesProvider;
import com.liferay.info.item.provider.InfoItemDetailsProvider;
import com.liferay.info.item.provider.InfoItemFieldValuesProvider;
import com.liferay.info.item.provider.InfoItemFormProvider;
import com.liferay.info.test.util.info.item.creator.MockInfoItemCreator;
import com.liferay.info.test.util.info.item.provider.MockInfoItemCapabilitiesProvider;
import com.liferay.info.test.util.info.item.provider.MockInfoItemDetailsProvider;
import com.liferay.info.test.util.info.item.provider.MockInfoItemFieldValuesProvider;
import com.liferay.info.test.util.info.item.provider.MockInfoItemFormProvider;
import com.liferay.info.test.util.model.MockObject;
import com.liferay.portal.kernel.util.HashMapDictionaryBuilder;
import com.liferay.portal.kernel.util.ListUtil;

import java.util.List;

import org.osgi.framework.Bundle;
import org.osgi.framework.BundleContext;
import org.osgi.framework.FrameworkUtil;
import org.osgi.framework.ServiceRegistration;

/**
 * @author Lourdes Fernández Besada
 */
public class InfoTestUtil {

	public static List<ServiceRegistration<?>>
		registerMockInfoFrameworkImplementation(
			InfoFieldSet infoFieldSet,
			InfoItemCapability... infoItemCapabilities) {

		Bundle bundle = FrameworkUtil.getBundle(InfoTestUtil.class);

		BundleContext bundleContext = bundle.getBundleContext();

		return ListUtil.fromArray(
			bundleContext.registerService(
				InfoItemCapabilitiesProvider.class,
				new MockInfoItemCapabilitiesProvider(infoItemCapabilities),
				HashMapDictionaryBuilder.<String, Object>put(
					"item.class.name", MockObject.class.getName()
				).build()),
			bundleContext.registerService(
				InfoItemCreator.class, new MockInfoItemCreator(),
				HashMapDictionaryBuilder.<String, Object>put(
					"item.class.name", MockObject.class.getName()
				).build()),
			bundleContext.registerService(
				InfoItemDetailsProvider.class,
				new MockInfoItemDetailsProvider(),
				HashMapDictionaryBuilder.<String, Object>put(
					"item.class.name", MockObject.class.getName()
				).build()),
			bundleContext.registerService(
				InfoItemFieldValuesProvider.class,
				new MockInfoItemFieldValuesProvider(),
				HashMapDictionaryBuilder.<String, Object>put(
					"item.class.name", MockObject.class.getName()
				).build()),
			bundleContext.registerService(
				InfoItemFormProvider.class,
				new MockInfoItemFormProvider(infoFieldSet),
				HashMapDictionaryBuilder.<String, Object>put(
					"item.class.name", MockObject.class.getName()
				).build()));
	}

}