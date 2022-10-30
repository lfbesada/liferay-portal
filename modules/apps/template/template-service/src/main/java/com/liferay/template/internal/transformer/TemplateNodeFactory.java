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

import java.util.Collections;

/**
 * @author Lourdes Fernández Besada
 */
public class TemplateNodeFactory {

	public static TemplateNode createTemplateNode(
		String fieldName, String fieldType, Object value,
		ThemeDisplay themeDisplay) {

		if (Validator.isNull(value)) {
			return new TemplateNode(
				themeDisplay, fieldName, StringPool.BLANK, fieldType,
				Collections.emptyMap());
		}

		TemplateNodeTransformer templateNodeTransformer =
			TemplateNodeTransformerUtil.getTemplateNodeTransformer(value);

		return templateNodeTransformer.transform(
			fieldName, fieldType, value, themeDisplay);
	}

}