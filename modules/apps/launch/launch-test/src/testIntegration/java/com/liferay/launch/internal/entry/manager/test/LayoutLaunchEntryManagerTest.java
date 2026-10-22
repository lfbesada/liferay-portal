/**
 * SPDX-FileCopyrightText: (c) 2026 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.launch.internal.entry.manager.test;

import com.liferay.arquillian.extension.junit.bridge.junit.Arquillian;
import com.liferay.launch.entry.manager.LaunchEntryManager;
import com.liferay.layout.test.util.LayoutTestUtil;
import com.liferay.petra.function.transform.TransformUtil;
import com.liferay.portal.kernel.model.Layout;
import com.liferay.portal.kernel.service.LayoutLocalService;
import com.liferay.portal.kernel.test.util.RandomTestUtil;
import com.liferay.portal.kernel.util.HashMapBuilder;
import com.liferay.portal.kernel.util.LocaleUtil;
import com.liferay.portal.kernel.util.MapUtil;
import com.liferay.portal.kernel.util.TreeMapBuilder;
import com.liferay.portal.kernel.workflow.WorkflowConstants;
import com.liferay.portal.test.rule.FeatureFlag;
import com.liferay.portal.test.rule.Inject;

import java.io.Serializable;

import java.util.List;
import java.util.Map;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;

/**
 * @author Lourdes Fernández Besada
 */
@FeatureFlag("LPD-10622")
@RunWith(Arquillian.class)
public class LayoutLaunchEntryManagerTest
	extends BaseLaunchEntryManagerTestCase {

	@Override
	@Test
	public void testAddDraft() throws Exception {
		super.testAddDraft();

		_testAddDraftWithExistingLaunchDraft();
	}

	@Override
	@Test
	public void testFetchDraft() throws Exception {
		super.testFetchDraft();

		_testFetchDraftLayoutByLaunchSetId();
	}

	@Override
	@Test
	public void testGetPreviewBaseModel() throws Exception {
		super.testGetPreviewBaseModel();

		_testGetPreviewBaseModelPreservesPublishedFriendlyURL();
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
	protected Map<String, Object> getDraftValuesMap(
			LaunchEntryManager.Draft draft)
		throws Exception {

		return _getDraftableValuesMap(
			_layoutLocalService.getLayout(draft.getDraftId()));
	}

	@Override
	protected Map<String, Object> getEntryValuesMap(long classPK)
		throws Exception {

		return _getDraftableValuesMap(_layoutLocalService.getLayout(classPK));
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
	protected Map<String, Object> randomValuesMap() {
		return HashMapBuilder.<String, Object>put(
			"masterLayoutPageTemplateEntryERC", RandomTestUtil.randomString()
		).put(
			"styleBookEntryERC", RandomTestUtil.randomString()
		).build();
	}

	@Override
	protected void updateDraft(
			LaunchEntryManager.Draft draft, Map<String, Object> valuesMap)
		throws Exception {

		_updateLayout(draft.getDraftId(), valuesMap);
	}

	@Override
	protected void updateLiveEntry(long classPK, Map<String, Object> valuesMap)
		throws Exception {

		_updateLayout(classPK, valuesMap);
	}

	private Map<String, Object> _getDraftableValuesMap(Layout layout) {
		return HashMapBuilder.<String, Object>put(
			"masterLayoutPageTemplateEntryERC",
			layout.getMasterLayoutPageTemplateEntryERC()
		).put(
			"styleBookEntryERC", layout.getStyleBookEntryERC()
		).build();
	}

	private void _testAddDraftWithExistingLaunchDraft() throws Exception {
		long classPK = addEntry();

		Map<String, Object> entryValuesMap = getEntryValuesMap(classPK);

		long launchSetId = RandomTestUtil.randomLong();

		LaunchEntryManager launchEntryManager = getLaunchEntryManager();

		LaunchEntryManager.Draft draft1 = launchEntryManager.addDraft(
			classPK, launchSetId, String.valueOf(classPK));

		Assert.assertEquals(entryValuesMap, getDraftValuesMap(draft1));

		Map<String, Object> valuesMap = randomValuesMap();

		updateDraft(draft1, valuesMap);

		Assert.assertEquals(valuesMap, getDraftValuesMap(draft1));

		LaunchEntryManager.Draft draft2 = launchEntryManager.addDraft(
			classPK, launchSetId, String.valueOf(classPK));

		Assert.assertEquals(draft1.getDraftId(), draft2.getDraftId());
		Assert.assertEquals(entryValuesMap, getDraftValuesMap(draft2));

		Layout launchDraftLayout = _layoutLocalService.getLayout(
			draft2.getDraftId());

		Assert.assertEquals(launchSetId, launchDraftLayout.getLaunchSetId());

		Layout draftLayout = _layoutLocalService.fetchDraftLayout(classPK);

		Assert.assertEquals(0, draftLayout.getLaunchSetId());
	}

	private void _testFetchDraftLayoutByLaunchSetId() throws Exception {
		long classPK = addEntry();

		Layout mainDraftLayout = _layoutLocalService.fetchDraftLayout(classPK);

		long launchSetId = RandomTestUtil.randomLong();

		LaunchEntryManager launchEntryManager = getLaunchEntryManager();

		LaunchEntryManager.Draft launchDraft = launchEntryManager.addDraft(
			classPK, launchSetId, String.valueOf(classPK));

		Layout launchDraftLayout = _layoutLocalService.fetchDraftLayout(
			classPK, launchSetId);

		Assert.assertEquals(
			launchDraft.getDraftId(), launchDraftLayout.getPlid());
		Assert.assertEquals(launchSetId, launchDraftLayout.getLaunchSetId());

		Assert.assertEquals(
			mainDraftLayout, _layoutLocalService.fetchDraftLayout(classPK, 0));
	}

	private void _testGetPreviewBaseModelPreservesPublishedFriendlyURL()
		throws Exception {

		long classPK = addEntry();

		Layout publishedLayout = _layoutLocalService.getLayout(classPK);

		LaunchEntryManager.Draft draft = addDraft(classPK);

		Layout previewBaseModel = (Layout)draft.getPreviewBaseModel();

		Assert.assertEquals(draft.getDraftId(), previewBaseModel.getPlid());
		Assert.assertEquals(
			publishedLayout.getFriendlyURL(),
			previewBaseModel.getFriendlyURL());
		Assert.assertEquals(
			publishedLayout.getFriendlyURL(LocaleUtil.getSiteDefault()),
			previewBaseModel.getFriendlyURL(LocaleUtil.getSiteDefault()));
		Assert.assertEquals(
			publishedLayout.getFriendlyURLMap(),
			previewBaseModel.getFriendlyURLMap());
	}

	private void _updateLayout(long plid, Map<String, Object> valuesMap)
		throws Exception {

		Layout layout = _layoutLocalService.getLayout(plid);

		layout.setStyleBookEntryERC(
			MapUtil.getString(valuesMap, "styleBookEntryERC"));
		layout.setMasterLayoutPageTemplateEntryERC(
			MapUtil.getString(valuesMap, "masterLayoutPageTemplateEntryERC"));

		_layoutLocalService.updateLayout(layout);
	}

	@Inject
	private LayoutLocalService _layoutLocalService;

}