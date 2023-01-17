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

package com.liferay.fragment.service.impl;

import com.liferay.fragment.model.FragmentEntryPropagation;
import com.liferay.fragment.service.base.FragmentEntryPropagationLocalServiceBaseImpl;
import com.liferay.portal.aop.AopService;

import java.util.Date;

import org.osgi.service.component.annotations.Component;

/**
 * @author Brian Wing Shun Chan
 */
@Component(
	property = "model.class.name=com.liferay.fragment.model.FragmentEntryPropagation",
	service = AopService.class
)
public class FragmentEntryPropagationLocalServiceImpl
	extends FragmentEntryPropagationLocalServiceBaseImpl {

	@Override
	public FragmentEntryPropagation addOrUpdateFragmentEntryPropagation(
		String fragmentEntryKey, String css, String html, String js,
		String configuration, int type) {

		FragmentEntryPropagation fragmentEntryPropagation =
			fragmentEntryPropagationPersistence.fetchByFragmentEntryKey(
				fragmentEntryKey);

		if (fragmentEntryPropagation == null) {
			fragmentEntryPropagation =
				fragmentEntryPropagationPersistence.create(
					counterLocalService.increment());

			fragmentEntryPropagation.setCreateDate(new Date());
			fragmentEntryPropagation.setFragmentEntryKey(fragmentEntryKey);
		}

		fragmentEntryPropagation.setModifiedDate(new Date());
		fragmentEntryPropagation.setFragmentEntryKey(fragmentEntryKey);
		fragmentEntryPropagation.setCss(css);
		fragmentEntryPropagation.setHtml(html);
		fragmentEntryPropagation.setJs(js);
		fragmentEntryPropagation.setConfiguration(configuration);
		fragmentEntryPropagation.setType(type);

		return fragmentEntryPropagationPersistence.update(
			fragmentEntryPropagation);
	}

	@Override
	public FragmentEntryPropagation fetchByFragmentEntryKey(
		String fragmentEntryKey) {

		return fragmentEntryPropagationPersistence.fetchByFragmentEntryKey(
			fragmentEntryKey);
	}

}