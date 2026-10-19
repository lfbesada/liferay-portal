/**
 * SPDX-FileCopyrightText: (c) 2026 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.launch.internal.entry.manager.test;

import com.liferay.arquillian.extension.junit.bridge.junit.Arquillian;
import com.liferay.launch.entry.manager.LaunchEntryManager;
import com.liferay.layout.content.service.LayoutContentVersionLocalService;
import com.liferay.layout.test.util.LayoutTestUtil;
import com.liferay.petra.function.transform.TransformUtil;
import com.liferay.petra.string.StringPool;
import com.liferay.portal.kernel.model.Layout;
import com.liferay.portal.kernel.service.LayoutLocalService;
import com.liferay.portal.kernel.test.util.RandomTestUtil;
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
 * @author Lourdes Fernández Besada
 */
@FeatureFlags(
	featureFlags = {@FeatureFlag("LPD-10622"), @FeatureFlag("LPD-72278")}
)
@RunWith(Arquillian.class)
public class LayoutLaunchEntryManagerTest
	extends BaseLaunchEntryManagerTestCase {

	@Test
	public void testAddDraftWhenLaunchSetDraftExists() throws Exception {
		long classPK = addEntry();

		long launchSetId = RandomTestUtil.randomLong();

		LaunchEntryManager launchEntryManager = getLaunchEntryManager();

		LaunchEntryManager.Draft draft1 = launchEntryManager.addDraft(
			classPK, launchSetId, String.valueOf(classPK));

		updateDraft(RandomTestUtil.randomString(), draft1);

		LaunchEntryManager.Draft draft2 = launchEntryManager.addDraft(
			classPK, launchSetId, String.valueOf(classPK));

		Assert.assertEquals(draft1.getDraftId(), draft2.getDraftId());
		Assert.assertEquals(getEntryTitle(classPK), getDraftTitle(draft2));

		Layout launchDraftLayout = _layoutLocalService.getLayout(
			draft2.getDraftId());

		Assert.assertEquals(launchSetId, launchDraftLayout.getLaunchSetId());

		Layout draftLayout = _layoutLocalService.fetchDraftLayout(classPK);

		Assert.assertEquals(0, draftLayout.getLaunchSetId());
	}

	@Test
	public void testGetVersionsWhenDraftLayoutIsPending() throws Exception {
		long classPK = addEntry();

		Layout draftLayout = _updateDraftLayoutStatus(
			classPK, WorkflowConstants.STATUS_DRAFT);

		LaunchEntryManager launchEntryManager = getLaunchEntryManager();

		Assert.assertEquals(
			List.of(
				String.valueOf(classPK), String.valueOf(draftLayout.getPlid())),
			TransformUtil.transform(
				launchEntryManager.getVersions(classPK),
				LaunchEntryManager.Version::getVersionKey));
	}

	@Test
	public void testPublishDraftWhenDraftLayoutIsPending() throws Exception {
		long classPK = addEntry();

		String title = RandomTestUtil.randomString();

		LaunchEntryManager.Draft draft = addDraft(classPK);

		updateDraft(title, draft);

		Layout draftLayout = _updateDraftLayoutStatus(
			classPK, WorkflowConstants.STATUS_DRAFT);

		Assert.assertFalse(_hasDraftLayoutContentVersion(draftLayout));

		LaunchEntryManager launchEntryManager = getLaunchEntryManager();

		launchEntryManager.publishDraft(classPK, draft.getDraftId());

		Assert.assertTrue(_hasDraftLayoutContentVersion(draftLayout));

		draftLayout = _layoutLocalService.getLayout(draftLayout.getPlid());

		Assert.assertEquals(title, _getDraftableTitle(draftLayout));
		Assert.assertEquals(
			WorkflowConstants.STATUS_APPROVED, draftLayout.getStatus());
	}

	@Override
	protected long addEntry() throws Exception {
		Layout layout = LayoutTestUtil.addTypeContentPublishedLayout(
			group, RandomTestUtil.randomString(),
			WorkflowConstants.STATUS_APPROVED);

		return layout.getPlid();
	}

	@Override
	protected String getBackURLParameterName() {
		return "p_l_back_url";
	}

	@Override
	protected String getDraftTitle(LaunchEntryManager.Draft draft)
		throws Exception {

		return _getDraftableTitle(
			_layoutLocalService.getLayout(draft.getDraftId()));
	}

	@Override
	protected String getEntryTitle(long classPK) throws Exception {
		return _getDraftableTitle(_layoutLocalService.getLayout(classPK));
	}

	@Override
	protected String getItemTitle(long classPK) throws Exception {
		Layout layout = _layoutLocalService.getLayout(classPK);

		return layout.getName(LocaleUtil.getSiteDefault());
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
	protected boolean hasVersionHistory() {
		return false;
	}

	@Override
	protected void updateDraft(String title, LaunchEntryManager.Draft draft)
		throws Exception {

		_updateDraftableTitle(draft.getDraftId(), title);
	}

	@Override
	protected void updateLiveEntry(String title, long classPK)
		throws Exception {

		_updateDraftableTitle(classPK, title);
	}

	private String _getDraftableTitle(Layout layout) {
		UnicodeProperties typeSettingsUnicodeProperties =
			layout.getTypeSettingsProperties();

		return typeSettingsUnicodeProperties.getProperty(
			_DRAFTABLE_TITLE_KEY, StringPool.BLANK);
	}

	private boolean _hasDraftLayoutContentVersion(Layout draftLayout)
		throws Exception {

		if (ListUtil.exists(
				_layoutContentVersionLocalService.getLayoutContentVersions(
					draftLayout.getPlid()),
				layoutContentVersion ->
					layoutContentVersion.getStatus() ==
						WorkflowConstants.STATUS_DRAFT)) {

			return true;
		}

		return false;
	}

	private Layout _updateDraftLayoutStatus(long classPK, int status)
		throws Exception {

		Layout draftLayout = _layoutLocalService.fetchDraftLayout(classPK);

		draftLayout.setStatus(status);

		return _layoutLocalService.updateLayout(draftLayout);
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
	private LayoutContentVersionLocalService _layoutContentVersionLocalService;

	@Inject
	private LayoutLocalService _layoutLocalService;

}