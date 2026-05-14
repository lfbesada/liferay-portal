/**
 * SPDX-FileCopyrightText: (c) 2026 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.layout.content.versioning.service.test;

import com.liferay.arquillian.extension.junit.bridge.junit.Arquillian;
import com.liferay.layout.content.versioning.exception.RequiredLayoutContentVersionException;
import com.liferay.layout.content.versioning.model.LayoutContentVersion;
import com.liferay.layout.content.versioning.service.LayoutContentVersionLocalService;
import com.liferay.layout.test.util.LayoutTestUtil;
import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.portal.kernel.model.Group;
import com.liferay.portal.kernel.model.Layout;
import com.liferay.portal.kernel.test.rule.AggregateTestRule;
import com.liferay.portal.kernel.test.rule.DeleteAfterTestRun;
import com.liferay.portal.kernel.test.util.GroupTestUtil;
import com.liferay.portal.kernel.test.util.RandomTestUtil;
import com.liferay.portal.kernel.test.util.TestPropsValues;
import com.liferay.portal.kernel.workflow.WorkflowConstants;
import com.liferay.portal.test.rule.FeatureFlag;
import com.liferay.portal.test.rule.Inject;
import com.liferay.portal.test.rule.LiferayIntegrationTestRule;
import com.liferay.portal.test.rule.PermissionCheckerMethodTestRule;

import org.junit.Assert;
import org.junit.Before;
import org.junit.ClassRule;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

/**
 * @author Lourdes Fernández Besada
 */
@FeatureFlag("LPD-10622")
@RunWith(Arquillian.class)
public class LayoutContentVersionLocalServiceTest {

	@ClassRule
	@Rule
	public static final AggregateTestRule aggregateTestRule =
		new AggregateTestRule(
			new LiferayIntegrationTestRule(),
			PermissionCheckerMethodTestRule.INSTANCE);

	@Before
	public void setUp() throws Exception {
		_group = GroupTestUtil.addGroup();

		Layout publishedLayout = LayoutTestUtil.addTypeContentLayout(_group);

		_draftLayout = publishedLayout.fetchDraftLayout();
	}

	@Test
	public void testAddLayoutContentVersion() throws Exception {
		LayoutContentVersion draftLayoutContentVersion =
			_layoutContentVersionLocalService.addLayoutContentVersion(
				RandomTestUtil.randomString(), TestPropsValues.getUserId(),
				_draftLayout.getPlid(), RandomTestUtil.randomString(),
				RandomTestUtil.randomString(), WorkflowConstants.STATUS_DRAFT,
				false);

		Assert.assertEquals(
			_draftLayout.getPlid(), draftLayoutContentVersion.getPlid());
		Assert.assertEquals(1, draftLayoutContentVersion.getVersion());
		Assert.assertEquals(
			WorkflowConstants.STATUS_DRAFT,
			draftLayoutContentVersion.getStatus());
		Assert.assertNotNull(draftLayoutContentVersion.getDataHash());

		LayoutContentVersion approvedLayoutContentVersion =
			_layoutContentVersionLocalService.addLayoutContentVersion(
				RandomTestUtil.randomString(), TestPropsValues.getUserId(),
				_draftLayout.getPlid(), RandomTestUtil.randomString(),
				RandomTestUtil.randomString(),
				WorkflowConstants.STATUS_APPROVED, false);

		Assert.assertEquals(
			WorkflowConstants.STATUS_APPROVED,
			approvedLayoutContentVersion.getStatus());

		_testAddLayoutContentVersionWithNullExternalReferenceCode();
		_testAddLayoutContentVersionWithSkipIfUnchanged();
	}

	@Test
	public void testDeleteLayoutContentVersion() throws Exception {
		LayoutContentVersion approvedLayoutContentVersion =
			_layoutContentVersionLocalService.addLayoutContentVersion(
				RandomTestUtil.randomString(), TestPropsValues.getUserId(),
				_draftLayout.getPlid(), RandomTestUtil.randomString(),
				RandomTestUtil.randomString(),
				WorkflowConstants.STATUS_APPROVED, false);

		LayoutContentVersion latestApprovedLayoutContentVersion =
			_layoutContentVersionLocalService.addLayoutContentVersion(
				RandomTestUtil.randomString(), TestPropsValues.getUserId(),
				_draftLayout.getPlid(), RandomTestUtil.randomString(),
				RandomTestUtil.randomString(),
				WorkflowConstants.STATUS_APPROVED, false);

		LayoutContentVersion draftLayoutContentVersion =
			_layoutContentVersionLocalService.addLayoutContentVersion(
				RandomTestUtil.randomString(), TestPropsValues.getUserId(),
				_draftLayout.getPlid(), RandomTestUtil.randomString(),
				RandomTestUtil.randomString(), WorkflowConstants.STATUS_DRAFT,
				false);

		_layoutContentVersionLocalService.deleteLayoutContentVersion(
			approvedLayoutContentVersion.getLayoutContentVersionId());

		Assert.assertNull(
			_layoutContentVersionLocalService.fetchLayoutContentVersion(
				approvedLayoutContentVersion.getLayoutContentVersionId()));

		try {
			_layoutContentVersionLocalService.deleteLayoutContentVersion(
				latestApprovedLayoutContentVersion.getLayoutContentVersionId());

			Assert.fail();
		}
		catch (RequiredLayoutContentVersionException
					requiredLayoutContentVersionException) {

			if (_log.isDebugEnabled()) {
				_log.debug(requiredLayoutContentVersionException);
			}
		}

		Assert.assertNotNull(
			_layoutContentVersionLocalService.fetchLayoutContentVersion(
				latestApprovedLayoutContentVersion.
					getLayoutContentVersionId()));

		_layoutContentVersionLocalService.deleteLayoutContentVersion(
			draftLayoutContentVersion.getLayoutContentVersionId());

		Assert.assertNull(
			_layoutContentVersionLocalService.fetchLayoutContentVersion(
				draftLayoutContentVersion.getLayoutContentVersionId()));
	}

	private void _testAddLayoutContentVersionWithNullExternalReferenceCode()
		throws Exception {

		LayoutContentVersion layoutContentVersion =
			_layoutContentVersionLocalService.addLayoutContentVersion(
				null, TestPropsValues.getUserId(), _draftLayout.getPlid(),
				RandomTestUtil.randomString(), RandomTestUtil.randomString(),
				WorkflowConstants.STATUS_DRAFT, false);

		Assert.assertEquals(
			_draftLayout.getExternalReferenceCode() + "_v_" +
				layoutContentVersion.getVersion(),
			layoutContentVersion.getExternalReferenceCode());
	}

	private void _testAddLayoutContentVersionWithSkipIfUnchanged()
		throws Exception {

		String data = RandomTestUtil.randomString();

		LayoutContentVersion layoutContentVersion1 =
			_layoutContentVersionLocalService.addLayoutContentVersion(
				RandomTestUtil.randomString(), TestPropsValues.getUserId(),
				_draftLayout.getPlid(), RandomTestUtil.randomString(), data,
				WorkflowConstants.STATUS_DRAFT, false);
		LayoutContentVersion layoutContentVersion2 =
			_layoutContentVersionLocalService.addLayoutContentVersion(
				RandomTestUtil.randomString(), TestPropsValues.getUserId(),
				_draftLayout.getPlid(), RandomTestUtil.randomString(), data,
				WorkflowConstants.STATUS_DRAFT, false);

		Assert.assertNotEquals(
			layoutContentVersion1.getLayoutContentVersionId(),
			layoutContentVersion2.getLayoutContentVersionId());

		data = RandomTestUtil.randomString();

		layoutContentVersion1 =
			_layoutContentVersionLocalService.addLayoutContentVersion(
				RandomTestUtil.randomString(), TestPropsValues.getUserId(),
				_draftLayout.getPlid(), RandomTestUtil.randomString(), data,
				WorkflowConstants.STATUS_APPROVED, true);
		layoutContentVersion2 =
			_layoutContentVersionLocalService.addLayoutContentVersion(
				RandomTestUtil.randomString(), TestPropsValues.getUserId(),
				_draftLayout.getPlid(), RandomTestUtil.randomString(), data,
				WorkflowConstants.STATUS_APPROVED, true);

		Assert.assertEquals(
			layoutContentVersion1.getLayoutContentVersionId(),
			layoutContentVersion2.getLayoutContentVersionId());
	}

	private static final Log _log = LogFactoryUtil.getLog(
		LayoutContentVersionLocalServiceTest.class);

	private Layout _draftLayout;

	@DeleteAfterTestRun
	private Group _group;

	@Inject
	private LayoutContentVersionLocalService _layoutContentVersionLocalService;

}