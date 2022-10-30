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

import com.liferay.info.type.KeyLocalizedLabelPair;
import com.liferay.portal.kernel.templateparser.TemplateNode;
import com.liferay.portal.kernel.theme.ThemeDisplay;
import com.liferay.portal.kernel.util.HashMapBuilder;
import com.liferay.template.info.field.transformer.BaseTemplateNodeTransformer;
import com.liferay.template.info.field.transformer.TemplateNodeTransformer;

import org.osgi.service.component.annotations.Component;

/**
 * @author Lourdes Fernández Besada
 */
@Component(immediate = true, service = TemplateNodeTransformer.class)
public class KeyLocalizedLabelPairTemplateNodeTransformer
	extends BaseTemplateNodeTransformer {

	@Override
	public String getClassName() {
		return KeyLocalizedLabelPair.class.getName();
	}

	@Override
	public TemplateNode transform(
		String fieldName, String fieldType, Object value,
		ThemeDisplay themeDisplay) {

		KeyLocalizedLabelPair keyLocalizedLabelPair =
			(KeyLocalizedLabelPair)value;

		return new TemplateNode(
			themeDisplay, fieldName,
			keyLocalizedLabelPair.getLabel(themeDisplay.getLocale()), fieldType,
			HashMapBuilder.put(
				"key", keyLocalizedLabelPair.getKey()
			).put(
				"label",
				keyLocalizedLabelPair.getLabel(themeDisplay.getLocale())
			).build());
	}

}