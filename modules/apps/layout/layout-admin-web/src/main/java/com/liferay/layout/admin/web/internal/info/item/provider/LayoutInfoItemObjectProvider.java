/**
 * SPDX-FileCopyrightText: (c) 2000 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.layout.admin.web.internal.info.item.provider;

import com.liferay.info.exception.NoSuchInfoItemException;
import com.liferay.info.item.ClassPKInfoItemIdentifier;
import com.liferay.info.item.ERCInfoItemIdentifier;
import com.liferay.info.item.InfoItemIdentifier;
import com.liferay.info.item.provider.InfoItemObjectProvider;
import com.liferay.petra.string.StringBundler;
import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.model.Group;
import com.liferay.portal.kernel.model.Layout;
import com.liferay.portal.kernel.service.GroupLocalService;
import com.liferay.portal.kernel.service.LayoutLocalService;
import com.liferay.portal.kernel.service.ServiceContext;
import com.liferay.portal.kernel.service.ServiceContextThreadLocal;
import com.liferay.portal.kernel.util.Validator;

import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

/**
 * @author Adolfo Pérez
 */
@Component(
	property = "info.item.identifier=com.liferay.info.item.ClassPKInfoItemIdentifier",
	service = InfoItemObjectProvider.class
)
public class LayoutInfoItemObjectProvider
	implements InfoItemObjectProvider<Layout> {

	@Override
	public Layout getInfoItem(InfoItemIdentifier infoItemIdentifier)
		throws NoSuchInfoItemException {

		ServiceContext serviceContext =
			ServiceContextThreadLocal.getServiceContext();

		return getInfoItem(
			serviceContext.getScopeGroupId(), infoItemIdentifier);
	}

	@Override
	public Layout getInfoItem(
			long groupId, InfoItemIdentifier infoItemIdentifier)
		throws NoSuchInfoItemException {

		if (!(infoItemIdentifier instanceof ClassPKInfoItemIdentifier) &&
			!(infoItemIdentifier instanceof ERCInfoItemIdentifier)) {

			throw new NoSuchInfoItemException(
				"Unsupported info item identifier " + infoItemIdentifier);
		}

		if (infoItemIdentifier instanceof ClassPKInfoItemIdentifier) {
			ClassPKInfoItemIdentifier classPKInfoItemIdentifier =
				(ClassPKInfoItemIdentifier)infoItemIdentifier;

			try {
				return _layoutLocalService.getLayout(
					classPKInfoItemIdentifier.getClassPK());
			}
			catch (PortalException portalException) {
				throw new NoSuchInfoItemException(
					"No layout found with PLID " +
						classPKInfoItemIdentifier.getClassPK(),
					portalException);
			}
		}

		ERCInfoItemIdentifier ercInfoItemIdentifier =
			(ERCInfoItemIdentifier)infoItemIdentifier;

		long scopeGroupId = groupId;

		if (Validator.isNotNull(
				ercInfoItemIdentifier.getScopeExternalReferenceCode())) {

			ServiceContext serviceContext =
				ServiceContextThreadLocal.getServiceContext();

			try {
				Group group =
					_groupLocalService.getGroupByExternalReferenceCode(
						ercInfoItemIdentifier.getScopeExternalReferenceCode(),
						serviceContext.getCompanyId());

				scopeGroupId = group.getGroupId();
			}
			catch (PortalException portalException) {
				throw new NoSuchInfoItemException(
					StringBundler.concat(
						"No group found with external reference code ",
						ercInfoItemIdentifier.getScopeExternalReferenceCode(),
						", and company ID ", serviceContext.getCompanyId()),
					portalException);
			}
		}

		try {
			return _layoutLocalService.getLayoutByExternalReferenceCode(
				ercInfoItemIdentifier.getExternalReferenceCode(), scopeGroupId);
		}
		catch (PortalException portalException) {
			throw new NoSuchInfoItemException(
				StringBundler.concat(
					"No layout found with external reference code ",
					ercInfoItemIdentifier.getExternalReferenceCode(),
					", and group ID ", groupId),
				portalException);
		}
	}

	@Reference
	private GroupLocalService _groupLocalService;

	@Reference
	private LayoutLocalService _layoutLocalService;

}