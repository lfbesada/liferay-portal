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

package com.liferay.template.internal.transformer;

import com.liferay.petra.string.StringPool;
import com.liferay.portal.kernel.templateparser.TemplateNode;
import com.liferay.portal.kernel.theme.ThemeDisplay;
import com.liferay.portal.kernel.util.Validator;
import com.liferay.template.info.field.transformer.TemplateNodeTransformer;
import com.liferay.template.internal.info.field.transformer.TemplateNodeTransformerTracker;

import java.util.Collection;
import java.util.Collections;

import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

/**
 * @author Lourdes Fernández Besada
 */
@Component(immediate = true, service = TemplateNodeFactory.class)
public class TemplateNodeFactory {

	public TemplateNode createTemplateNode(
		String fieldName, String fieldType, Object value,
		ThemeDisplay themeDisplay) {

		if (Validator.isNull(value)) {
			return new TemplateNode(
				themeDisplay, fieldName, StringPool.BLANK, fieldType,
				Collections.emptyMap());
		}

		TemplateNodeTransformer templateNodeTransformer =
			_getTemplateNodeTransformer(value);

		return templateNodeTransformer.transform(
			fieldName, fieldType, value, themeDisplay);
	}

	private TemplateNodeTransformer _getTemplateNodeTransformer(
		Object value) {

		Class<?> fieldValueClass = value.getClass();

		String fieldValueClassName = fieldValueClass.getName();

		TemplateNodeTransformer templateNodeTransformer =
			_templateNodeTransformerTracker.getTemplateNodeTransformer(
				fieldValueClassName);

		if (templateNodeTransformer != null) {
			return templateNodeTransformer;
		}

		if (value instanceof Collection) {
			templateNodeTransformer =
				_templateNodeTransformerTracker.getTemplateNodeTransformer(
					Collection.class.getName());
		}

		if (templateNodeTransformer != null) {
			return templateNodeTransformer;
		}

		return _templateNodeTransformerTracker.getTemplateNodeTransformer(
			"<ANY>");
	}

	@Reference
	private TemplateNodeTransformerTracker _templateNodeTransformerTracker;

}