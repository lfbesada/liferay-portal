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

import com.liferay.petra.string.StringPool;
import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.portal.kernel.templateparser.TemplateNode;
import com.liferay.portal.kernel.theme.ThemeDisplay;
import com.liferay.template.info.field.transformer.TemplateNodeTransformer;
import com.liferay.template.internal.transformer.TemplateNodeTransformerUtil;

import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;

/**
 * @author Lourdes Fernández Besada
 */
public class DefaultRepeatableFieldTemplateNodeTransformer
	implements TemplateNodeTransformer {

	@Override
	public String getClassName() {
		return Collections.class.getName();
	}

	public TemplateNode transform(
		String fieldName, String fieldType, Object value,
		ThemeDisplay themeDisplay) {

		if (!(value instanceof Collection)) {
			return new TemplateNode(
				themeDisplay, fieldName, String.valueOf(value), fieldType,
				Collections.emptyMap());
		}

		Collection<Object> collection = (Collection<Object>)value;

		try {
			return _createTemplateNode(
				fieldName, fieldType, themeDisplay, collection);
		}
		catch (Exception exception) {
			if (_log.isDebugEnabled()) {
				_log.debug(exception);
			}

			return new TemplateNode(
				themeDisplay, fieldName, String.valueOf(value), fieldType,
				Collections.emptyMap());
		}
	}

	private <T> TemplateNode _createTemplateNode(
		String fieldName, String fieldType, ThemeDisplay themeDisplay,
		Collection<Object> collection) {

		if (collection.isEmpty()) {
			return new TemplateNode(
				themeDisplay, fieldName, StringPool.BLANK, fieldType,
				Collections.emptyMap());
		}

		Iterator<Object> iterator = collection.iterator();

		Object firstItem = iterator.next();

		TemplateNodeTransformer templateNodeTransformer =
			TemplateNodeTransformerUtil.getTemplateNodeTransformer(firstItem);

		TemplateNode templateNode = templateNodeTransformer.transform(
			fieldName, fieldType, firstItem, themeDisplay);

		templateNode.appendSibling(templateNode);

		while (iterator.hasNext()) {
			templateNode.appendSibling(
				templateNodeTransformer.transform(
					fieldName, fieldType, iterator.next(), themeDisplay));
		}

		return templateNode;
	}

	private static final Log _log = LogFactoryUtil.getLog(
		DefaultRepeatableFieldTemplateNodeTransformer.class);

}