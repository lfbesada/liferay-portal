/**
 * SPDX-FileCopyrightText: (c) 2026 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.launch.internal.entry.type.test;

import com.liferay.arquillian.extension.junit.bridge.junit.Arquillian;
import com.liferay.launch.entry.type.LaunchEntryType;
import com.liferay.layout.test.util.LayoutTestUtil;
import com.liferay.petra.function.transform.TransformUtil;
import com.liferay.petra.lang.SafeCloseable;
import com.liferay.petra.string.StringPool;
import com.liferay.portal.kernel.model.Layout;
import com.liferay.portal.kernel.model.LayoutConstants;
import com.liferay.portal.kernel.service.LayoutLocalService;
import com.liferay.portal.kernel.test.util.RandomTestUtil;
import com.liferay.portal.kernel.util.GetterUtil;
import com.liferay.portal.kernel.util.ListUtil;
import com.liferay.portal.kernel.util.LocaleUtil;
import com.liferay.portal.kernel.util.TreeMapBuilder;
import com.liferay.portal.kernel.util.UnicodeProperties;
import com.liferay.portal.kernel.workflow.WorkflowConstants;
import com.liferay.portal.test.rule.FeatureFlag;
import com.liferay.portal.test.rule.FeatureFlags;
import com.liferay.portal.test.rule.Inject;

import java.io.Serializable;

import java.util.List;
import java.util.Map;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;

/**
 * @author Alejandro Tardín
 */
@FeatureFlags(
	featureFlags = {@FeatureFlag("LPD-10622"), @FeatureFlag("LPD-72278")}
)
@RunWith(Arquillian.class)
public class LayoutLaunchEntryTypeTest extends BaseLaunchEntryTypeTestCase {

	@Test
	public void testGetPreviewBaseModel() throws Exception {
		long classPK = addEntry();

		LaunchEntryType launchEntryType = getLaunchEntryType();

		LaunchEntryType.Version version = addVersion(classPK);

		Layout launchDraftLayout = (Layout)version.getPreviewBaseModel();

		Assert.assertEquals(
			GetterUtil.getLong(version.getClassVersion()),
			launchDraftLayout.getPlid());

		LaunchEntryType.Version publishedVersion =
			launchEntryType.publishVersion(classPK, version.getClassVersion());

		Layout previewLayout = (Layout)publishedVersion.getPreviewBaseModel();

		Assert.assertEquals(classPK, previewLayout.getPlid());
	}

	@Override
	@Test
	public void testGetPrimaryKey() throws Exception {
		long classPK = addEntry();

		String entryTitle = getEntryTitle(classPK);

		String title1 = RandomTestUtil.randomString();

		LaunchEntryType.Version version1 = addVersion(classPK);

		updateEntry(title1, version1);

		String title2 = RandomTestUtil.randomString();

		LaunchEntryType.Version version2 = addVersion(classPK);

		updateEntry(title2, version2);

		try (SafeCloseable safeCloseable = setPreviewIdWithSafeCloseable(
				classPK, version1)) {

			Assert.assertEquals(title1, getEntryTitle(classPK));
		}

		try (SafeCloseable safeCloseable = setPreviewIdWithSafeCloseable(
				classPK, version2)) {

			Assert.assertEquals(title2, getEntryTitle(classPK));
		}

		Assert.assertEquals(entryTitle, getEntryTitle(classPK));
	}

	@Override
	@Test
	public void testGetVersions() throws Exception {
		long classPK = addEntry();

		LaunchEntryType launchEntryType = getLaunchEntryType();

		List<LaunchEntryType.Version> versions = launchEntryType.getVersions(
			classPK, LocaleUtil.getSiteDefault());

		Assert.assertEquals(versions.toString(), 1, versions.size());
		Assert.assertEquals(
			String.valueOf(classPK),
			versions.get(
				0
			).getClassVersion());
	}

	@Override
	protected long addEntry() throws Exception {
		Layout layout = LayoutTestUtil.addTypeContentPublishedLayout(
			group, RandomTestUtil.randomString(),
			WorkflowConstants.STATUS_APPROVED);

		return layout.getPlid();
	}

	@Override
	protected void assertLiveEntryIsUnchanged(long classPK) throws Exception {
		Layout layout = _layoutLocalService.getLayout(classPK);

		Assert.assertTrue(layout.isPublished());

		Layout draftLayout = _layoutLocalService.fetchDraftLayout(classPK);

		String externalReferenceCode = draftLayout.getExternalReferenceCode();

		Assert.assertFalse(
			externalReferenceCode,
			externalReferenceCode.contains(
				LayoutConstants.EXTERNAL_REFERENCE_CODE_INFIX_LAUNCH_DRAFT));
	}

	@Override
	protected String getBackURLParameterName() {
		return "p_l_back_url";
	}

	@Override
	protected String getEntryTitle(long classPK) throws Exception {
		Layout layout = _layoutLocalService.getLayout(classPK);

		UnicodeProperties typeSettingsUnicodeProperties =
			layout.getTypeSettingsProperties();

		return typeSettingsUnicodeProperties.getProperty(
			_DRAFTABLE_TITLE_KEY, StringPool.BLANK);
	}

	@Override
	protected List<Serializable> getListedPrimaryKeys(long classPK)
		throws Exception {

		Layout layout = _layoutLocalService.getLayout(classPK);

		return TransformUtil.transform(
			_layoutLocalService.getLayouts(
				layout.getGroupId(), layout.isPrivateLayout()),
			Layout::getPlid);
	}

	@Override
	protected Class<?> getModelClass() {
		return Layout.class;
	}

	@Override
	protected Map<String, String> getSelectedItemData(long classPK) {
		return TreeMapBuilder.<String, String>create(
			String.CASE_INSENSITIVE_ORDER
		).put(
			"plid", String.valueOf(classPK)
		).build();
	}

	@Override
	protected List<Map<String, String>> getUnbranchableSelectedItemsData(
			long classPK)
		throws Exception {

		Layout draftLayout = _layoutLocalService.fetchDraftLayout(classPK);

		Layout typePortletLayout = LayoutTestUtil.addTypePortletLayout(group);

		return ListUtil.fromArray(
			getSelectedItemData(draftLayout.getPlid()),
			getSelectedItemData(typePortletLayout.getPlid()));
	}

	@Override
	protected String getVersionTitle(LaunchEntryType.Version version)
		throws Exception {

		Layout layout = (Layout)version.getPreviewBaseModel();

		UnicodeProperties typeSettingsUnicodeProperties =
			layout.getTypeSettingsProperties();

		return typeSettingsUnicodeProperties.getProperty(
			_DRAFTABLE_TITLE_KEY, StringPool.BLANK);
	}

	@Override
	protected void updateEntry(String title, LaunchEntryType.Version version)
		throws Exception {

		_updateDraftableTitle(
			GetterUtil.getLong(version.getPrimaryKey()), title);
	}

	@Override
	protected void updateLiveEntry(String title, long classPK)
		throws Exception {

		_updateDraftableTitle(classPK, title);
	}

	private void _updateDraftableTitle(long plid, String title)
		throws Exception {

		Layout layout = _layoutLocalService.getLayout(plid);

		UnicodeProperties typeSettingsUnicodeProperties =
			layout.getTypeSettingsProperties();

		typeSettingsUnicodeProperties.setProperty(_DRAFTABLE_TITLE_KEY, title);

		layout.setTypeSettingsProperties(typeSettingsUnicodeProperties);

		_layoutLocalService.updateLayout(layout);
	}

	private static final String _DRAFTABLE_TITLE_KEY = "test-draftable-title";

	@Inject
	private LayoutLocalService _layoutLocalService;

}