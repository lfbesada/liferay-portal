/**
 * SPDX-FileCopyrightText: (c) 2000 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.style.book.util;

import com.liferay.exportimport.kernel.staging.StagingUtil;
import com.liferay.frontend.token.definition.FrontendTokenDefinition;
import com.liferay.frontend.token.definition.FrontendTokenDefinitionRegistry;
import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.feature.flag.FeatureFlagManagerUtil;
import com.liferay.portal.kernel.language.LanguageUtil;
import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.portal.kernel.model.Layout;
import com.liferay.portal.kernel.module.service.Snapshot;
import com.liferay.portal.kernel.service.LayoutLocalServiceUtil;
import com.liferay.portal.kernel.util.Validator;
import com.liferay.style.book.model.StyleBookEntry;
import com.liferay.style.book.service.StyleBookEntryLocalServiceUtil;
import com.liferay.style.book.service.StyleBookEntryServiceUtil;

import java.util.Locale;
import java.util.Objects;

/**
 * @author Víctor Galán
 */
public class DefaultStyleBookEntryUtil {

	public static StyleBookEntry getDefaultMasterStyleBookEntry(Layout layout) {
		StyleBookEntry styleBookEntry = _getMasterLayoutStyleBookEntry(layout);

		if (styleBookEntry != null) {
			return styleBookEntry;
		}

		FrontendTokenDefinitionRegistry frontendTokenDefinitionRegistry =
			_frontendTokenDefinitionRegistrySnapshot.get();

		FrontendTokenDefinition frontendTokenDefinition =
			frontendTokenDefinitionRegistry.getFrontendTokenDefinition(layout);

		if (frontendTokenDefinition != null) {
			return StyleBookEntryLocalServiceUtil.fetchDefaultStyleBookEntry(
				StagingUtil.getLiveGroupId(layout.getGroupId()),
				frontendTokenDefinition.getThemeId());
		}

		return null;
	}

	public static StyleBookEntry getDefaultStyleBookEntry(Layout layout) {
		StyleBookEntry styleBookEntry = _getStyleBookEntry(layout);

		if ((styleBookEntry == null) ||
			!_isStyleBookEntryApplicable(layout, styleBookEntry)) {

			return getDefaultMasterStyleBookEntry(layout);
		}

		return styleBookEntry;
	}

	public static String getStyleBookEntryName(
		Layout layout, Locale locale, StyleBookEntry styleBookEntry) {

		if ((styleBookEntry != null) &&
			(styleBookEntry.getStyleBookEntryId() > 0)) {

			return styleBookEntry.getName();
		}

		StyleBookEntry defaultStyleBookEntry = getDefaultMasterStyleBookEntry(
			layout);

		if (defaultStyleBookEntry == null) {
			if (FeatureFlagManagerUtil.isEnabled(
					layout.getCompanyId(), "LPD-30204")) {

				return LanguageUtil.format(
					locale, "styles-from-x",
					StyleBookUtil.getThemeName(layout, locale));
			}

			return LanguageUtil.get(locale, "styles-from-theme");
		}

		StyleBookEntry masterLayoutStyleBookEntry =
			_getMasterLayoutStyleBookEntry(layout);

		if (masterLayoutStyleBookEntry != null) {
			return LanguageUtil.get(locale, "styles-from-master");
		}

		return LanguageUtil.get(locale, "styles-by-default");
	}

	private static StyleBookEntry _getMasterLayoutStyleBookEntry(
		Layout layout) {

		StyleBookEntry styleBookEntry = null;

		if (layout.getMasterLayoutPlid() > 0) {
			Layout masterLayout = LayoutLocalServiceUtil.fetchLayout(
				layout.getMasterLayoutPlid());

			if (masterLayout != null) {
				styleBookEntry = _getStyleBookEntry(masterLayout);
			}
		}

		return styleBookEntry;
	}

	private static StyleBookEntry _getStyleBookEntry(Layout layout) {
		if (Validator.isNull(layout.getStyleBookEntryERC())) {
			return null;
		}

		try {
			return StyleBookEntryServiceUtil.
				getStyleBookEntryByExternalReferenceCode(
					layout.getStyleBookEntryERC(), layout.getGroupId());
		}
		catch (PortalException portalException) {
			if (_log.isDebugEnabled()) {
				_log.debug(portalException);
			}
		}

		return null;
	}

	private static boolean _isStyleBookEntryApplicable(
		Layout layout, StyleBookEntry styleBookEntry) {

		if (!FeatureFlagManagerUtil.isEnabled(
				layout.getCompanyId(), "LPD-30204")) {

			return true;
		}

		FrontendTokenDefinitionRegistry frontendTokenDefinitionRegistry =
			_frontendTokenDefinitionRegistrySnapshot.get();

		FrontendTokenDefinition frontendTokenDefinition =
			frontendTokenDefinitionRegistry.getFrontendTokenDefinition(layout);

		if ((frontendTokenDefinition != null) &&
			Objects.equals(
				frontendTokenDefinition.getThemeId(),
				styleBookEntry.getThemeId())) {

			return true;
		}

		return false;
	}

	private static final Log _log = LogFactoryUtil.getLog(
		DefaultStyleBookEntryUtil.class);

	private static final Snapshot<FrontendTokenDefinitionRegistry>
		_frontendTokenDefinitionRegistrySnapshot = new Snapshot<>(
			DefaultStyleBookEntryUtil.class,
			FrontendTokenDefinitionRegistry.class);

}