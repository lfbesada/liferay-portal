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

import com.liferay.template.info.field.transformer.TemplateNodeTransformer;
import com.liferay.template.internal.info.field.transformer.DefaultRepeatableFieldTemplateNodeTransformer;
import com.liferay.template.internal.info.field.transformer.DefaultTemplateNodeTransformer;
import com.liferay.template.internal.info.field.transformer.TemplateNodeTransformerTracker;

import java.util.Collection;

import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

/**
 * @author Lourdes Fernández Besada
 */
@Component(service = {})
public class TemplateNodeTransformerUtil {

	public static TemplateNodeTransformer getTemplateNodeTransformer(
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
			return _DEFAULT_REPEATABLE_FIELD_TEMPLATE_NODE_TRANSFORMER;
		}

		return _DEFAULT_TEMPLATE_NODE_TRANSFORMER;
	}

	@Reference(unbind = "-")
	protected void setTemplateNodeTransformerTracker(
		TemplateNodeTransformerTracker templateNodeTransformerTracker) {

		_templateNodeTransformerTracker = templateNodeTransformerTracker;
	}

	private static final TemplateNodeTransformer
		_DEFAULT_REPEATABLE_FIELD_TEMPLATE_NODE_TRANSFORMER =
			new DefaultRepeatableFieldTemplateNodeTransformer();

	private static final TemplateNodeTransformer
		_DEFAULT_TEMPLATE_NODE_TRANSFORMER =
			new DefaultTemplateNodeTransformer();

	private static TemplateNodeTransformerTracker
		_templateNodeTransformerTracker;

}