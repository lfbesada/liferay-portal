/**
 * SPDX-FileCopyrightText: (c) 2024 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.asset.publisher.web.internal.util;

import com.liferay.asset.list.model.AssetListEntry;
import com.liferay.asset.list.service.AssetListEntryLocalServiceUtil;
import com.liferay.asset.list.service.AssetListEntryServiceUtil;
import com.liferay.asset.publisher.web.internal.configuration.AssetPublisherSelectionStyleConfigurationUtil;
import com.liferay.asset.publisher.web.internal.constants.AssetPublisherSelectionStyleConstants;
import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.feature.flag.FeatureFlagManagerUtil;
import com.liferay.portal.kernel.test.util.RandomTestUtil;
import com.liferay.portal.kernel.upgrade.MockPortletPreferences;
import com.liferay.portal.kernel.util.HashMapBuilder;
import com.liferay.portal.kernel.util.Props;
import com.liferay.portal.kernel.util.PropsUtil;
import com.liferay.portal.test.rule.LiferayUnitTestRule;

import java.util.Collections;
import java.util.Map;

import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.ClassRule;
import org.junit.Rule;
import org.junit.Test;

import org.mockito.MockedStatic;
import org.mockito.Mockito;

/**
 * @author Lourdes Fernández Besada
 */
public class AssetPublisherUtilTest {

	@ClassRule
	@Rule
	public static final LiferayUnitTestRule liferayUnitTestRule =
		LiferayUnitTestRule.INSTANCE;

	@BeforeClass
	public static void setUpClass() {
		PropsUtil propsUtil = new PropsUtil();

		propsUtil.setProps(Mockito.mock(Props.class));

		_featureFlagManagerUtilMockedStatic = Mockito.mockStatic(
			FeatureFlagManagerUtil.class);
	}

	@AfterClass
	public static void tearDownClass() {
		_assetListEntryLocalServiceUtilMockedStatic.close();
		_assetListEntryServiceUtilMockedStatic.close();
		_assetPublisherSelectionStyleConfigurationUtilMockedStatic.close();
		_featureFlagManagerUtilMockedStatic.close();
	}

	@Before
	public void setUp() {
		_assetListEntryLocalServiceUtilMockedStatic.reset();
		_assetListEntryServiceUtilMockedStatic.reset();
		_featureFlagManagerUtilMockedStatic.reset();

		_assetPublisherSelectionStyleConfigurationUtilMockedStatic.when(
			AssetPublisherSelectionStyleConfigurationUtil::defaultSelectionStyle
		).thenReturn(
			AssetPublisherSelectionStyleConstants.TYPE_ASSET_LIST
		);
	}

	@Test
	public void testGetAssetListEntryWithFeatureFlagDisabled()
		throws PortalException {

		AssetListEntry assetListEntry = _getAssetListEntry();

		_setUpFetchAssetListEntry(assetListEntry, true);

		_assertGetAssetListEntry(
			null, true, Collections.emptyMap());
		_assertGetAssetListEntry(
			null, true,
			HashMapBuilder.put(
				"assetListEntryId", String.valueOf(RandomTestUtil.randomLong())
			).build());

		Map<String, String> portletPreferencesMap = HashMapBuilder.put(
			"assetListEntryId",
			String.valueOf(assetListEntry.getAssetListEntryId())
		).build();

		_assertGetAssetListEntry(null, false, portletPreferencesMap);
		_assertGetAssetListEntry(assetListEntry, true, portletPreferencesMap);

		_setUpFetchAssetListEntry(assetListEntry, false);

		_assertGetAssetListEntry(assetListEntry, false, portletPreferencesMap);
		_assertGetAssetListEntry(null, true, portletPreferencesMap);

		_assetPublisherSelectionStyleConfigurationUtilMockedStatic.when(
			AssetPublisherSelectionStyleConfigurationUtil::defaultSelectionStyle
		).thenReturn(
			AssetPublisherSelectionStyleConstants.TYPE_DYNAMIC
		);

		_assertGetAssetListEntry(null, false, portletPreferencesMap);
		_assertGetAssetListEntry(null, true, portletPreferencesMap);
	}

	private static void _setUpFetchAssetListEntry(
		AssetListEntry assetListEntry, boolean checkPermissions) {

		if (checkPermissions) {
			_assetListEntryServiceUtilMockedStatic.when(
				() -> AssetListEntryServiceUtil.fetchAssetListEntry(
					assetListEntry.getAssetListEntryId())
			).thenReturn(
				assetListEntry
			);
			_assetListEntryLocalServiceUtilMockedStatic.when(
				() -> AssetListEntryLocalServiceUtil.fetchAssetListEntry(
					assetListEntry.getAssetListEntryId())
			).thenReturn(
				null
			);

			return;
		}

		_assetListEntryServiceUtilMockedStatic.when(
			() -> AssetListEntryServiceUtil.fetchAssetListEntry(
				assetListEntry.getAssetListEntryId())
		).thenReturn(
			null
		);
		_assetListEntryLocalServiceUtilMockedStatic.when(
			() -> AssetListEntryLocalServiceUtil.fetchAssetListEntry(
				assetListEntry.getAssetListEntryId())
		).thenReturn(
			assetListEntry
		);
	}

	private static AssetListEntry _getAssetListEntry() {
		AssetListEntry assetListEntry = Mockito.mock(AssetListEntry.class);

		Mockito.when(
			assetListEntry.getAssetListEntryId()
		).thenReturn(
			RandomTestUtil.randomLong()
		);

		return assetListEntry;
	}

	private void _assertGetAssetListEntry(
			AssetListEntry assetListEntry, boolean checkPermissions,
			Map<String, String> portletPreferencesMap)
		throws PortalException {

		Assert.assertEquals(
			assetListEntry,
			AssetPublisherUtil.getAssetListEntry(
				checkPermissions, _COMPANY_ID, _GROUP_ID,
				_getMockPortletPreferences(portletPreferencesMap)));
	}

	private MockPortletPreferences _getMockPortletPreferences(
		Map<String, String> portletPreferencesMap) {

		MockPortletPreferences mockPortletPreferences =
			new MockPortletPreferences();

		for (Map.Entry<String, String> entry :
				portletPreferencesMap.entrySet()) {

			mockPortletPreferences.setValue(entry.getKey(), entry.getValue());
		}

		return mockPortletPreferences;
	}

	private static final long _COMPANY_ID = RandomTestUtil.randomLong();

	private static final long _GROUP_ID = RandomTestUtil.randomLong();

	private static final MockedStatic<AssetListEntryLocalServiceUtil>
		_assetListEntryLocalServiceUtilMockedStatic = Mockito.mockStatic(
			AssetListEntryLocalServiceUtil.class);
	private static final MockedStatic<AssetListEntryServiceUtil>
		_assetListEntryServiceUtilMockedStatic = Mockito.mockStatic(
			AssetListEntryServiceUtil.class);
	private static final MockedStatic
		<AssetPublisherSelectionStyleConfigurationUtil>
			_assetPublisherSelectionStyleConfigurationUtilMockedStatic =
				Mockito.mockStatic(
					AssetPublisherSelectionStyleConfigurationUtil.class);
	private static MockedStatic<FeatureFlagManagerUtil>
		_featureFlagManagerUtilMockedStatic;

}