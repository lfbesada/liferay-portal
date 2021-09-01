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

package com.liferay.template.web.internal.info.item.renderer;

import com.liferay.dynamic.data.mapping.model.DDMTemplate;
import com.liferay.dynamic.data.mapping.service.DDMTemplateLocalServiceUtil;
import com.liferay.info.item.InfoItemFieldValues;
import com.liferay.info.item.InfoItemServiceTracker;
import com.liferay.info.item.provider.InfoItemFieldValuesProvider;
import com.liferay.info.item.provider.InfoItemFormProvider;
import com.liferay.info.item.provider.InfoItemFormVariationsProvider;
import com.liferay.info.item.renderer.InfoItemTemplatedRenderer;
import com.liferay.info.item.renderer.template.InfoItemRendererTemplate;
import com.liferay.petra.string.StringPool;
import com.liferay.portal.kernel.dao.orm.QueryUtil;
import com.liferay.portal.kernel.language.LanguageUtil;
import com.liferay.portal.kernel.service.ServiceContext;
import com.liferay.portal.kernel.service.ServiceContextThreadLocal;
import com.liferay.portal.kernel.theme.ThemeDisplay;
import com.liferay.portal.kernel.util.ArrayUtil;
import com.liferay.portal.kernel.util.GetterUtil;
import com.liferay.portal.kernel.util.PortalUtil;
import com.liferay.portal.kernel.util.ResourceBundleUtil;
import com.liferay.portal.kernel.util.Validator;
import com.liferay.staging.StagingGroupHelper;
import com.liferay.template.web.internal.portlet.template.TemplateDisplayTemplateTransformer;

import java.io.Writer;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Locale;
import java.util.Optional;
import java.util.ResourceBundle;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author Lourdes Fernández Besada
 */
public abstract class BaseTemplateInfoItemTemplatedRenderer<T>
	implements InfoItemTemplatedRenderer<T> {

	@Override
	public List<InfoItemRendererTemplate> getInfoItemRendererTemplates(
		String className, String classTypeKey, Locale locale) {

		List<InfoItemRendererTemplate> infoItemRendererTemplates =
			new ArrayList<>();

		for (DDMTemplate ddmTemplate :
				_getDDMTemplates(className, GetterUtil.getLong(classTypeKey))) {

			if (getStagingGroupHelper().isLiveGroup(ddmTemplate.getGroupId())) {
				continue;
			}

			infoItemRendererTemplates.add(
				new InfoItemRendererTemplate(
					ddmTemplate.getName(locale), ddmTemplate.getTemplateKey()));
		}

		return infoItemRendererTemplates;
	}

	@Override
	public String getInfoItemRendererTemplatesGroupLabel(
		String className, String classTypeKey, Locale locale) {

		ServiceContext serviceContext =
			ServiceContextThreadLocal.getServiceContext();

		if (serviceContext == null) {
			return StringPool.BLANK;
		}

		ThemeDisplay themeDisplay = serviceContext.getThemeDisplay();

		return Optional.ofNullable(
			getInfoItemServiceTracker().getFirstInfoItemService(
				InfoItemFormVariationsProvider.class, className)
		).map(
			infoItemFormVariationsProvider ->
				infoItemFormVariationsProvider.getInfoItemFormVariation(
					themeDisplay.getScopeGroupId(), classTypeKey)
		).map(
			infoItemFormVariation -> infoItemFormVariation.getLabel(locale)
		).orElse(
			getLabel(themeDisplay.getLocale())
		);
	}

	@Override
	public String getLabel(Locale locale) {
		ResourceBundle resourceBundle = ResourceBundleUtil.getBundle(
			locale, getClass());

		return LanguageUtil.get(resourceBundle, "information-templates");
	}

	protected List<InfoItemRendererTemplate> getInfoItemRendererTemplates(
		String className, Locale locale) {

		return getInfoItemRendererTemplates(className, "0", locale);
	}

	protected String getInfoItemRendererTemplatesGroupLabel(
		String className, Locale locale) {

		return getInfoItemRendererTemplatesGroupLabel(className, "0", locale);
	}

	protected abstract InfoItemServiceTracker getInfoItemServiceTracker();

	protected abstract StagingGroupHelper getStagingGroupHelper();

	protected void renderTemplate(
		String className, T itemObject, String templateKey,
		HttpServletRequest httpServletRequest,
		HttpServletResponse httpServletResponse) {

		if (Validator.isNull(templateKey)) {
			render(itemObject, httpServletRequest, httpServletResponse);

			return;
		}

		ServiceContext serviceContext =
			ServiceContextThreadLocal.getServiceContext();

		if (serviceContext == null) {
			return;
		}

		ThemeDisplay themeDisplay = serviceContext.getThemeDisplay();

		DDMTemplate ddmTemplate = DDMTemplateLocalServiceUtil.fetchTemplate(
			themeDisplay.getScopeGroupId(),
			PortalUtil.getClassNameId(className), templateKey, true);

		if (ddmTemplate == null) {
			return;
		}

		try {
			InfoItemFieldValues infoItemFieldValues =
				InfoItemFieldValues.builder(
				).build();

			InfoItemFieldValuesProvider<Object> infoItemFieldValuesProvider =
				getInfoItemServiceTracker().getFirstInfoItemService(
					InfoItemFieldValuesProvider.class, className);

			if (infoItemFieldValuesProvider != null) {
				infoItemFieldValues =
					infoItemFieldValuesProvider.getTemplateInfoItemFieldValues(
						itemObject);
			}

			TemplateDisplayTemplateTransformer
				templateDisplayTemplateTransformer =
					new TemplateDisplayTemplateTransformer(
						ddmTemplate, infoItemFieldValues);

			String content = templateDisplayTemplateTransformer.transform(
				themeDisplay.getLocale());

			Writer writer = httpServletResponse.getWriter();

			writer.write(content);
		}
		catch (Exception exception) {
			throw new RuntimeException(exception);
		}
	}

	private List<DDMTemplate> _getDDMTemplates(String className, long classPK) {
		ServiceContext serviceContext =
			ServiceContextThreadLocal.getServiceContext();

		if (serviceContext == null) {
			return Collections.emptyList();
		}

		return DDMTemplateLocalServiceUtil.getTemplates(
			serviceContext.getCompanyId(),
			ArrayUtil.append(
				PortalUtil.getAncestorSiteGroupIds(
					serviceContext.getScopeGroupId()),
				new long[] {serviceContext.getScopeGroupId()}),
			new long[] {PortalUtil.getClassNameId(className)},
			new long[] {classPK},
			PortalUtil.getClassNameId(InfoItemFormProvider.class.getName()),
			QueryUtil.ALL_POS, QueryUtil.ALL_POS, null);
	}

}