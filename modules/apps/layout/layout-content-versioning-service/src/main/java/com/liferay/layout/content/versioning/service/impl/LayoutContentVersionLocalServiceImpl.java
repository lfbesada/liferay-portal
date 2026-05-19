/**
 * SPDX-FileCopyrightText: (c) 2026 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.layout.content.versioning.service.impl;

import com.liferay.layout.content.versioning.exception.RequiredLayoutContentVersionException;
import com.liferay.layout.content.versioning.model.LayoutContentVersion;
import com.liferay.layout.content.versioning.service.base.LayoutContentVersionLocalServiceBaseImpl;
import com.liferay.layout.content.versioning.util.comparator.LayoutContentVersionVersionComparator;
import com.liferay.portal.aop.AopService;
import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.feature.flag.FeatureFlagManagerUtil;
import com.liferay.portal.kernel.model.Layout;
import com.liferay.portal.kernel.model.User;
import com.liferay.portal.kernel.service.LayoutLocalService;
import com.liferay.portal.kernel.service.UserLocalService;
import com.liferay.portal.kernel.util.DigesterUtil;
import com.liferay.portal.kernel.util.GetterUtil;
import com.liferay.portal.kernel.util.HashMapBuilder;
import com.liferay.portal.kernel.util.LocaleUtil;
import com.liferay.portal.kernel.util.Validator;
import com.liferay.portal.kernel.workflow.WorkflowConstants;

import java.util.Date;
import java.util.List;

import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

/**
 * @author Lourdes Fernández Besada
 */
@Component(
	property = "model.class.name=com.liferay.layout.content.versioning.model.LayoutContentVersion",
	service = AopService.class
)
public class LayoutContentVersionLocalServiceImpl
	extends LayoutContentVersionLocalServiceBaseImpl {

	public LayoutContentVersion addLayoutContentVersion(
			String externalReferenceCode, long userId, long plid, String name,
			String data, int status, boolean skipIfUnchanged)
		throws PortalException {

		Layout layout = _layoutLocalService.getLayout(plid);

		_checkFeatureFlag(layout.getCompanyId());

		if (!layout.isDraftLayout()) {
			throw new UnsupportedOperationException();
		}

		String dataHash = DigesterUtil.digestHex(
			"SHA-256", GetterUtil.getString(data));

		if (skipIfUnchanged) {
			LayoutContentVersion lastLayoutContentVersion =
				layoutContentVersionPersistence.fetchByPlid_First(plid, null);

			if ((lastLayoutContentVersion != null) &&
				dataHash.equals(lastLayoutContentVersion.getDataHash())) {

				return lastLayoutContentVersion;
			}
		}

		LayoutContentVersion layoutContentVersion =
			layoutContentVersionPersistence.create(
				counterLocalService.increment(
					LayoutContentVersion.class.getName()));

		int version = _generateVersion(plid);

		if (externalReferenceCode == null) {
			externalReferenceCode =
				layout.getExternalReferenceCode() + "_v_" + version;
		}

		layoutContentVersion.setExternalReferenceCode(externalReferenceCode);

		layoutContentVersion.setGroupId(layout.getGroupId());
		layoutContentVersion.setCompanyId(layout.getCompanyId());
		layoutContentVersion.setUserId(userId);

		User user = _userLocalService.getUser(userId);

		layoutContentVersion.setUserName(user.getFullName());

		Date date = new Date();

		layoutContentVersion.setCreateDate(date);
		layoutContentVersion.setModifiedDate(date);

		layoutContentVersion.setPlid(plid);
		layoutContentVersion.setVersion(version);

		if (Validator.isNotNull(name)) {
			layoutContentVersion.setNameMap(
				HashMapBuilder.put(
					LocaleUtil.getSiteDefault(), name
				).build());
		}

		layoutContentVersion.setStatus(status);
		layoutContentVersion.setStatusByUserId(userId);
		layoutContentVersion.setStatusByUserName(user.getFullName());
		layoutContentVersion.setStatusDate(date);

		layoutContentVersion.setSpecSchemaVersion(_SPEC_SCHEMA_VERSION);
		layoutContentVersion.setData(data);
		layoutContentVersion.setDataHash(dataHash);

		return layoutContentVersionPersistence.update(layoutContentVersion);
	}

	@Override
	public LayoutContentVersion deleteLayoutContentVersion(
			long layoutContentVersionId)
		throws PortalException {

		LayoutContentVersion layoutContentVersion =
			layoutContentVersionPersistence.findByPrimaryKey(
				layoutContentVersionId);

		_checkFeatureFlag(layoutContentVersion.getCompanyId());

		if (layoutContentVersion.getStatus() ==
				WorkflowConstants.STATUS_APPROVED) {

			List<LayoutContentVersion> approvedLayoutContentVersions =
				layoutContentVersionPersistence.findByP_S(
					layoutContentVersion.getPlid(),
					WorkflowConstants.STATUS_APPROVED);

			for (LayoutContentVersion approvedLayoutContentVersion :
					approvedLayoutContentVersions) {

				if (approvedLayoutContentVersion.getVersion() >
						layoutContentVersion.getVersion()) {

					return layoutContentVersionPersistence.remove(
						layoutContentVersionId);
				}
			}

			throw new RequiredLayoutContentVersionException();
		}

		return layoutContentVersionPersistence.remove(layoutContentVersionId);
	}

	public List<LayoutContentVersion> getLayoutContentVersions(long plid)
		throws PortalException {

		Layout layout = _layoutLocalService.getLayout(plid);

		_checkFeatureFlag(layout.getCompanyId());

		return layoutContentVersionPersistence.findByPlid(plid);
	}

	public LayoutContentVersion updateLayoutContentVersion(
			long layoutContentVersionId, String name)
		throws PortalException {

		LayoutContentVersion layoutContentVersion =
			layoutContentVersionPersistence.findByPrimaryKey(
				layoutContentVersionId);

		_checkFeatureFlag(layoutContentVersion.getCompanyId());

		layoutContentVersion.setModifiedDate(new Date());
		layoutContentVersion.setNameMap(
			HashMapBuilder.put(
				LocaleUtil.getSiteDefault(), name
			).build());

		return layoutContentVersionPersistence.update(layoutContentVersion);
	}

	private void _checkFeatureFlag(long companyId) {
		if (!FeatureFlagManagerUtil.isEnabled(companyId, "LPD-10622")) {
			throw new UnsupportedOperationException();
		}
	}

	private int _generateVersion(long plid) {
		LayoutContentVersion layoutContentVersion =
			layoutContentVersionPersistence.fetchByPlid_First(
				plid, LayoutContentVersionVersionComparator.getInstance(false));

		if (layoutContentVersion == null) {
			return 1;
		}

		return layoutContentVersion.getVersion() + 1;
	}

	private static final String _SPEC_SCHEMA_VERSION = "v1.0";

	@Reference
	private LayoutLocalService _layoutLocalService;

	@Reference
	private UserLocalService _userLocalService;

}