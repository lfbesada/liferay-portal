/**
 * SPDX-FileCopyrightText: (c) 2000 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.fragment.entry.processor.editable.parser;

import com.liferay.fragment.constants.FragmentEntryLinkConstants;
import com.liferay.fragment.exception.FragmentEntryContentException;
import com.liferay.petra.string.StringBundler;
import com.liferay.petra.string.StringPool;
import com.liferay.portal.kernel.json.JSONFactoryUtil;
import com.liferay.portal.kernel.json.JSONObject;
import com.liferay.portal.kernel.language.LanguageUtil;
import com.liferay.portal.kernel.util.GetterUtil;

import java.util.Locale;
import java.util.Objects;

import org.jsoup.nodes.Element;

/**
 * Provides a utility to replace an editable element value.
 *
 * @author Pavel Savinov
 */
public interface EditableElementParser {

	public default JSONObject getAttributes(Element element) {
		return JSONFactoryUtil.createJSONObject();
	}

	public default JSONObject getFieldTemplateConfigJSONObject(
		String fieldName, Locale locale, Object fieldValue) {

		return JSONFactoryUtil.createJSONObject();
	}

	public default String getRestrictedContentFieldValue(
		Locale locale, String mode) {

		if (!Objects.equals(mode, FragmentEntryLinkConstants.EDIT)) {
			return StringPool.BLANK;
		}

		return StringBundler.concat(
			"<span class=\"clearfix page-editor__editable\" ",
			"data-lfr-editable-id=\"02-title\">",
			LanguageUtil.get(locale, "restricted-content"),
			"&nbsp;<svg class=\"lexicon-icon lexicon-icon-password-policies",
			"\" role=\"presentation\" viewBox=\"0 0 512 512\">",
			"<use xlink:href=\"/o/classic-theme/images/clay",
			"/icons.svg#password-policies\"></use></svg></span>");
	}

	public String getValue(Element element);

	public default String parseFieldValue(Object fieldValue) {
		return GetterUtil.get(fieldValue.toString(), StringPool.BLANK);
	}

	/**
	 * Replaces the editable element value with a new one.
	 *
	 * @param element the editable element to replace
	 * @param value the new element value
	 */
	public void replace(Element element, String value);

	/**
	 * Replaces the editable element value with a new one and applies the
	 * configuration values.
	 *
	 * @param element the editable element to replace
	 * @param value the new element value
	 * @param configJSONObject the configuration values
	 */
	public default void replace(
		Element element, String value, JSONObject configJSONObject) {

		replace(element, value);
	}

	/**
	 * Validates the editable element.
	 *
	 * @param  element the editable element to validate
	 * @throws FragmentEntryContentException if an invalid editable element is
	 *         detected
	 */
	public default void validate(Element element)
		throws FragmentEntryContentException {
	}

}