/**
 * SPDX-FileCopyrightText: (c) 2026 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.fragment.internal.model.listener;

import com.liferay.asset.kernel.model.AssetEntry;
import com.liferay.fragment.cache.FragmentEntryLinkCache;
import com.liferay.fragment.model.FragmentEntryLink;
import com.liferay.info.search.InfoSearchClassMapperRegistry;
import com.liferay.layout.model.LayoutClassedModelUsage;
import com.liferay.layout.service.LayoutClassedModelUsageLocalService;
import com.liferay.portal.kernel.exception.ModelListenerException;
import com.liferay.portal.kernel.model.BaseModelListener;
import com.liferay.portal.kernel.model.ModelListener;
import com.liferay.portal.kernel.transaction.TransactionCallbackUtil;
import com.liferay.portal.kernel.util.GetterUtil;
import com.liferay.portal.kernel.util.Portal;

import java.util.List;

import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

/**
 * @author Lourdes Fernández Besada
 */
@Component(service = ModelListener.class)
public class AssetEntryFragmentEntryLinkCacheModelListener
	extends BaseModelListener<AssetEntry> {

	@Override
	public void onAfterCreate(AssetEntry assetEntry)
		throws ModelListenerException {

		_invalidateFragmentEntryLinkCache(assetEntry);
	}

	@Override
	public void onAfterRemove(AssetEntry assetEntry)
		throws ModelListenerException {

		_invalidateFragmentEntryLinkCache(assetEntry);
	}

	@Override
	public void onAfterUpdate(
			AssetEntry originalAssetEntry, AssetEntry assetEntry)
		throws ModelListenerException {

		_invalidateFragmentEntryLinkCache(assetEntry);
	}

	private void _invalidateFragmentEntryLinkCache(AssetEntry assetEntry) {
		long classNameId = _portal.getClassNameId(
			_infoSearchClassMapperRegistry.getClassName(
				assetEntry.getClassName()));
		long classPK = assetEntry.getClassPK();

		TransactionCallbackUtil.registerCommitCallback(
			() -> {
				long fragmentEntryLinkClassNameId = _portal.getClassNameId(
					FragmentEntryLink.class.getName());

				List<LayoutClassedModelUsage> layoutClassedModelUsages =
					_layoutClassedModelUsageLocalService.
						getLayoutClassedModelUsages(classNameId, classPK);

				for (LayoutClassedModelUsage layoutClassedModelUsage :
						layoutClassedModelUsages) {

					if (layoutClassedModelUsage.getContainerType() !=
							fragmentEntryLinkClassNameId) {

						continue;
					}

					_fragmentEntryLinkCache.removeFragmentEntryLinkCache(
						GetterUtil.getLong(
							layoutClassedModelUsage.getContainerKey()));
				}

				return null;
			});
	}

	@Reference
	private FragmentEntryLinkCache _fragmentEntryLinkCache;

	@Reference
	private InfoSearchClassMapperRegistry _infoSearchClassMapperRegistry;

	@Reference
	private LayoutClassedModelUsageLocalService
		_layoutClassedModelUsageLocalService;

	@Reference
	private Portal _portal;

}