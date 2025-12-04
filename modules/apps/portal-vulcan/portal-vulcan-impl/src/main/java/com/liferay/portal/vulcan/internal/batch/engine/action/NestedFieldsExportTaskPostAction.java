/**
 * SPDX-FileCopyrightText: (c) 2025 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.portal.vulcan.internal.batch.engine.action;

import com.liferay.batch.engine.BatchEngineTaskItemDelegate;
import com.liferay.batch.engine.action.ExportTaskPostAction;
import com.liferay.batch.engine.jaxrs.uri.BatchEngineUriInfo;
import com.liferay.batch.engine.model.BatchEngineExportTask;
import com.liferay.portal.kernel.model.User;
import com.liferay.portal.kernel.service.CompanyLocalService;
import com.liferay.portal.kernel.service.UserLocalService;
import com.liferay.portal.kernel.util.LocaleUtil;
import com.liferay.portal.vulcan.accept.language.AcceptLanguage;
import com.liferay.portal.vulcan.internal.fields.NestedFieldsSetterUtil;
import com.liferay.portal.vulcan.jaxrs.context.ContextDataInjectorBuilderFactory;
import com.liferay.portal.vulcan.pagination.Pagination;

import jakarta.ws.rs.core.UriInfo;

import java.io.Serializable;

import java.util.List;
import java.util.Locale;
import java.util.Map;

import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

/**
 * @author Alejandro Tardín
 */
@Component(service = ExportTaskPostAction.class)
public class NestedFieldsExportTaskPostAction implements ExportTaskPostAction {

	@Override
	public void run(
			BatchEngineExportTask batchEngineExportTask,
			BatchEngineTaskItemDelegate<?> batchEngineTaskItemDelegate,
			Object item)
		throws Exception {

		User user = _userLocalService.fetchUser(
			batchEngineExportTask.getUserId());

		NestedFieldsSetterUtil.setNestedFields(
			item,
			_contextDataInjectorBuilderFactory.builder(
			).acceptLanguage(
				new AcceptLanguage() {

					@Override
					public List<Locale> getLocales() {
						return null;
					}

					@Override
					public String getPreferredLanguageId() {
						return user.getLanguageId();
					}

					@Override
					public Locale getPreferredLocale() {
						return LocaleUtil.fromLanguageId(user.getLanguageId());
					}

				}
			).company(
				_companyLocalService.fetchCompany(
					batchEngineExportTask.getCompanyId())
			).fallbackContextValueFunction(
				contextClass -> {
					if (contextClass == Pagination.class) {
						return Pagination.of(-1, -1);
					}

					return null;
				}
			).uriInfo(
				_getUriInfo(batchEngineExportTask)
			).user(
				user
			).build());
	}

	private UriInfo _getUriInfo(BatchEngineExportTask batchEngineExportTask) {
		BatchEngineUriInfo.Builder batchEngineUriInfoBuilder =
			new BatchEngineUriInfo.Builder();

		Map<String, Serializable> parameters =
			batchEngineExportTask.getParameters();

		for (Map.Entry<String, Serializable> entry : parameters.entrySet()) {
			batchEngineUriInfoBuilder.queryParameter(
				entry.getKey(), String.valueOf(entry.getValue()));
		}

		return batchEngineUriInfoBuilder.build();
	}

	@Reference
	private CompanyLocalService _companyLocalService;

	@Reference
	private ContextDataInjectorBuilderFactory
		_contextDataInjectorBuilderFactory;

	@Reference
	private UserLocalService _userLocalService;

}