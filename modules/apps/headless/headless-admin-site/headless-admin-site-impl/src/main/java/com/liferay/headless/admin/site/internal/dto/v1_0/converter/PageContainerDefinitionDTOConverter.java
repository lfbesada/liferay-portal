/**
 * SPDX-FileCopyrightText: (c) 2024 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.headless.admin.site.internal.dto.v1_0.converter;

import com.liferay.headless.admin.site.dto.v1_0.HTMLProperties;
import com.liferay.headless.admin.site.dto.v1_0.PageContainerDefinition;
import com.liferay.layout.util.structure.ContainerStyledLayoutStructureItem;
import com.liferay.portal.kernel.util.ArrayUtil;
import com.liferay.portal.kernel.util.HashMapBuilder;
import com.liferay.portal.kernel.util.SetUtil;
import com.liferay.portal.vulcan.dto.converter.DTOConverter;
import com.liferay.portal.vulcan.dto.converter.DTOConverterContext;

import java.util.Map;

import org.osgi.service.component.annotations.Component;

/**
 * @author Eudaldo Alonso
 */
@Component(
	property = "dto.class.name=com.liferay.layout.util.structure.ContainerStyledLayoutStructureItem",
	service = DTOConverter.class
)
public class PageContainerDefinitionDTOConverter
	implements DTOConverter
		<ContainerStyledLayoutStructureItem, PageContainerDefinition> {

	@Override
	public String getContentType() {
		return PageContainerDefinition.class.getSimpleName();
	}

	@Override
	public PageContainerDefinition toDTO(
			DTOConverterContext dtoConverterContext,
			ContainerStyledLayoutStructureItem
				containerStyledLayoutStructureItem)
		throws Exception {

		return new PageContainerDefinition() {
			{
				setContentVisibility(
					containerStyledLayoutStructureItem::getContentVisibility);
				setCssClasses(
					() -> {
						if (SetUtil.isEmpty(
								containerStyledLayoutStructureItem.
									getCssClasses())) {

							return null;
						}

						return ArrayUtil.toStringArray(
							containerStyledLayoutStructureItem.getCssClasses());
					});
				setCustomCSS(containerStyledLayoutStructureItem::getCustomCSS);
				setHtmlProperties(
					() -> _toHtmlProperties(
						containerStyledLayoutStructureItem));
				setIndexed(containerStyledLayoutStructureItem::isIndexed);
				setName(containerStyledLayoutStructureItem::getName);
			}
		};
	}

	private HTMLProperties _toHtmlProperties(
		ContainerStyledLayoutStructureItem containerStyledLayoutStructureItem) {

		return new HTMLProperties() {
			{
				setHtmlTag(
					() -> _internalToExternalValuesMap.get(
						containerStyledLayoutStructureItem.getHtmlTag()));
			}
		};
	}

	private static final Map<String, HTMLProperties.HtmlTag>
		_internalToExternalValuesMap = HashMapBuilder.put(
			"article", HTMLProperties.HtmlTag.ARTICLE
		).put(
			"aside", HTMLProperties.HtmlTag.ASIDE
		).put(
			"div", HTMLProperties.HtmlTag.DIV
		).put(
			"footer", HTMLProperties.HtmlTag.FOOTER
		).put(
			"header", HTMLProperties.HtmlTag.HEADER
		).put(
			"nav", HTMLProperties.HtmlTag.NAV
		).put(
			"section", HTMLProperties.HtmlTag.SECTION
		).build();

}