/**
 * SPDX-FileCopyrightText: (c) 2026 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.layout.content.versioning.internal.service;

import com.liferay.layout.content.versioning.provider.LayoutContentVersionDataProvider;
import com.liferay.layout.content.versioning.service.LayoutContentVersionLocalService;
import com.liferay.layout.page.template.model.LayoutPageTemplateEntry;
import com.liferay.layout.page.template.service.LayoutPageTemplateEntryLocalService;
import com.liferay.layout.utility.page.model.LayoutUtilityPageEntry;
import com.liferay.layout.utility.page.service.LayoutUtilityPageEntryLocalService;
import com.liferay.portal.kernel.feature.flag.FeatureFlagManagerUtil;
import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.portal.kernel.model.Layout;
import com.liferay.portal.kernel.service.ServiceContext;
import com.liferay.portal.kernel.service.ServiceContextThreadLocal;
import com.liferay.portal.kernel.service.ServiceWrapper;
import com.liferay.portal.kernel.util.LocaleUtil;
import com.liferay.portal.kernel.workflow.WorkflowConstants;

import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

/**
 * @author Lourdes Fernández Besada
 */
@Component(service = ServiceWrapper.class)
public class LayoutLocalServiceWrapper
	extends com.liferay.portal.kernel.service.LayoutLocalServiceWrapper {

	@Override
	public Layout copyLayoutContent(Layout sourceLayout, Layout targetLayout)
		throws Exception {

		Layout layout = super.copyLayoutContent(sourceLayout, targetLayout);

		_addLayoutContentVersion(sourceLayout, targetLayout);

		return layout;
	}

	private void _addLayoutContentVersion(
		Layout sourceLayout, Layout targetLayout) {

		try {
			if (!FeatureFlagManagerUtil.isEnabled(
					sourceLayout.getCompanyId(), "LPD-10622") ||
				(sourceLayout.getClassPK() != targetLayout.getPlid()) ||
				!targetLayout.isTypeContent()) {

				return;
			}

			LayoutPageTemplateEntry layoutPageTemplateEntry =
				_layoutPageTemplateEntryLocalService.
					fetchLayoutPageTemplateEntryByPlid(targetLayout.getPlid());

			if (layoutPageTemplateEntry != null) {
				return;
			}

			LayoutUtilityPageEntry layoutUtilityPageEntry =
				_layoutUtilityPageEntryLocalService.
					fetchLayoutUtilityPageEntryByPlid(targetLayout.getPlid());

			if (layoutUtilityPageEntry != null) {
				return;
			}

			ServiceContext serviceContext =
				ServiceContextThreadLocal.getServiceContext();

			if (serviceContext == null) {
				serviceContext = new ServiceContext();

				serviceContext.setCompanyId(sourceLayout.getCompanyId());
				serviceContext.setUserId(sourceLayout.getUserId());
			}

			_layoutContentVersionLocalService.addLayoutContentVersion(
				null, sourceLayout.getUserId(), sourceLayout.getPlid(),
				targetLayout.getName(LocaleUtil.getSiteDefault()),
				_layoutContentVersionDataProvider.getLayoutContentVersionData(
					sourceLayout, serviceContext),
				WorkflowConstants.STATUS_APPROVED, true);
		}
		catch (Exception exception) {
			_log.error(
				"Unable to add LayoutContentVersion on publish for plid " +
					sourceLayout.getPlid(),
				exception);
		}
	}

	private static final Log _log = LogFactoryUtil.getLog(
		LayoutLocalServiceWrapper.class);

	@Reference
	private LayoutContentVersionDataProvider _layoutContentVersionDataProvider;

	@Reference
	private LayoutContentVersionLocalService _layoutContentVersionLocalService;

	@Reference
	private LayoutPageTemplateEntryLocalService
		_layoutPageTemplateEntryLocalService;

	@Reference
	private LayoutUtilityPageEntryLocalService
		_layoutUtilityPageEntryLocalService;

}