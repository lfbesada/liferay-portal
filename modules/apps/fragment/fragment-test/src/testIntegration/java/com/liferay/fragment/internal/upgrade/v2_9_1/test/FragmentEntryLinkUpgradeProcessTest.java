/**
 * SPDX-FileCopyrightText: (c) 2024 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.fragment.internal.upgrade.v2_9_1.test;

import com.liferay.arquillian.extension.junit.bridge.junit.Arquillian;
import com.liferay.change.tracking.configuration.CTSettingsConfiguration;
import com.liferay.change.tracking.model.CTCollection;
import com.liferay.change.tracking.service.CTCollectionLocalService;
import com.liferay.fragment.entry.processor.constants.FragmentEntryProcessorConstants;
import com.liferay.fragment.model.FragmentEntryLink;
import com.liferay.fragment.service.FragmentEntryLinkLocalService;
import com.liferay.layout.test.util.ContentLayoutTestUtil;
import com.liferay.layout.test.util.LayoutTestUtil;
import com.liferay.petra.lang.SafeCloseable;
import com.liferay.portal.configuration.test.util.CompanyConfigurationTemporarySwapper;
import com.liferay.portal.kernel.cache.MultiVMPool;
import com.liferay.portal.kernel.change.tracking.CTCollectionThreadLocal;
import com.liferay.portal.kernel.json.JSONFactoryUtil;
import com.liferay.portal.kernel.json.JSONObject;
import com.liferay.portal.kernel.json.JSONUtil;
import com.liferay.portal.kernel.model.Group;
import com.liferay.portal.kernel.model.Layout;
import com.liferay.portal.kernel.test.rule.AggregateTestRule;
import com.liferay.portal.kernel.test.rule.DeleteAfterTestRun;
import com.liferay.portal.kernel.test.util.GroupTestUtil;
import com.liferay.portal.kernel.test.util.RandomTestUtil;
import com.liferay.portal.kernel.test.util.TestPropsValues;
import com.liferay.portal.kernel.upgrade.UpgradeProcess;
import com.liferay.portal.kernel.util.HashMapDictionaryBuilder;
import com.liferay.portal.kernel.version.Version;
import com.liferay.portal.test.rule.Inject;
import com.liferay.portal.test.rule.LiferayIntegrationTestRule;
import com.liferay.portal.test.rule.PermissionCheckerMethodTestRule;
import com.liferay.portal.upgrade.registry.UpgradeStepRegistrator;
import com.liferay.portal.upgrade.test.util.UpgradeTestUtil;
import com.liferay.segments.service.SegmentsExperienceLocalService;

import java.util.List;

import org.junit.Assert;
import org.junit.Before;
import org.junit.ClassRule;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

/**
 * @author Rubén Pulido
 */
@RunWith(Arquillian.class)
public class FragmentEntryLinkUpgradeProcessTest {

	@ClassRule
	@Rule
	public static final AggregateTestRule aggregateTestRule =
		new AggregateTestRule(
			new LiferayIntegrationTestRule(),
			PermissionCheckerMethodTestRule.INSTANCE);

	@Before
	public void setUp() throws Exception {
		_group = GroupTestUtil.addGroup();
	}

	@Test
	public void testUpgrade() throws Exception {
		try (CompanyConfigurationTemporarySwapper
				companyConfigurationTemporarySwapper =
					new CompanyConfigurationTemporarySwapper(
						TestPropsValues.getCompanyId(),
						CTSettingsConfiguration.class.getName(),
						HashMapDictionaryBuilder.<String, Object>put(
							"enabled", true
						).build())) {

			Layout layout = LayoutTestUtil.addTypeContentLayout(_group);

			Layout draftLayout = layout.fetchDraftLayout();

			Assert.assertNotNull(draftLayout);

			long segmentsExperienceId =
				_segmentsExperienceLocalService.
					fetchDefaultSegmentsExperienceId(draftLayout.getPlid());

			JSONObject editableValuesJSONObject1 = JSONUtil.put(
				FragmentEntryProcessorConstants.
					KEY_BACKGROUND_IMAGE_FRAGMENT_ENTRY_PROCESSOR,
				JSONFactoryUtil.createJSONObject()
			).put(
				FragmentEntryProcessorConstants.
					KEY_EDITABLE_FRAGMENT_ENTRY_PROCESSOR,
				JSONFactoryUtil.createJSONObject()
			).put(
				FragmentEntryProcessorConstants.
					KEY_FREEMARKER_FRAGMENT_ENTRY_PROCESSOR,
				JSONFactoryUtil.createJSONObject()
			);

			FragmentEntryLink draftLayoutFragmentEntryLink =
				ContentLayoutTestUtil.addFragmentEntryLinkToLayout(
					editableValuesJSONObject1.toString(), draftLayout,
					segmentsExperienceId);

			Assert.assertTrue(
				draftLayoutFragmentEntryLink.getEditableValues(),
				JSONUtil.equals(
					editableValuesJSONObject1,
					JSONFactoryUtil.createJSONObject(
						draftLayoutFragmentEntryLink.getEditableValues())));

			ContentLayoutTestUtil.publishLayout(draftLayout, layout);

			List<FragmentEntryLink> fragmentEntryLinks =
				_fragmentEntryLinkLocalService.getFragmentEntryLinksByPlid(
					layout.getGroupId(), layout.getPlid());

			Assert.assertEquals(
				fragmentEntryLinks.toString(), 1, fragmentEntryLinks.size());

			FragmentEntryLink publishedLayoutFragmentEntryLink =
				fragmentEntryLinks.get(0);

			Assert.assertTrue(
				publishedLayoutFragmentEntryLink.getEditableValues(),
				JSONUtil.equals(
					editableValuesJSONObject1,
					JSONFactoryUtil.createJSONObject(
						publishedLayoutFragmentEntryLink.getEditableValues())));

			CTCollection ctCollection =
				_ctCollectionLocalService.addCTCollection(
					null, TestPropsValues.getCompanyId(),
					TestPropsValues.getUserId(), 0,
					RandomTestUtil.randomString(),
					RandomTestUtil.randomString());

			JSONObject editableValuesJSONObject2 = JSONUtil.put(
				FragmentEntryProcessorConstants.
					KEY_BACKGROUND_IMAGE_FRAGMENT_ENTRY_PROCESSOR,
				JSONUtil.put(
					RandomTestUtil.randomString(),
					RandomTestUtil.randomString())
			).put(
				FragmentEntryProcessorConstants.
					KEY_EDITABLE_FRAGMENT_ENTRY_PROCESSOR,
				JSONUtil.put(
					RandomTestUtil.randomString(),
					RandomTestUtil.randomString())
			).put(
				FragmentEntryProcessorConstants.
					KEY_FREEMARKER_FRAGMENT_ENTRY_PROCESSOR,
				JSONUtil.put(
					RandomTestUtil.randomString(),
					RandomTestUtil.randomString())
			);

			try (SafeCloseable safeCloseable =
					CTCollectionThreadLocal.setCTCollectionIdWithSafeCloseable(
						ctCollection.getCtCollectionId())) {

				_fragmentEntryLinkLocalService.updateFragmentEntryLink(
					TestPropsValues.getUserId(),
					draftLayoutFragmentEntryLink.getFragmentEntryLinkId(),
					editableValuesJSONObject2.toString());

//				ContentLayoutTestUtil.publishLayout(draftLayout, layout);
			}

			_runUpgrade();

			draftLayoutFragmentEntryLink =
				_fragmentEntryLinkLocalService.getFragmentEntryLink(
					draftLayoutFragmentEntryLink.getFragmentEntryLinkId());

			Assert.assertTrue(
				publishedLayoutFragmentEntryLink.getEditableValues(),
				JSONUtil.equals(
					editableValuesJSONObject1,
					JSONFactoryUtil.createJSONObject(
						publishedLayoutFragmentEntryLink.getEditableValues())));

			publishedLayoutFragmentEntryLink =
				_fragmentEntryLinkLocalService.getFragmentEntryLink(
					publishedLayoutFragmentEntryLink.getFragmentEntryLinkId());

			Assert.assertTrue(
				publishedLayoutFragmentEntryLink.getEditableValues(),
				JSONUtil.isEmpty(
					JSONFactoryUtil.createJSONObject(
						publishedLayoutFragmentEntryLink.getEditableValues())));

			try (SafeCloseable safeCloseable =
					CTCollectionThreadLocal.setCTCollectionIdWithSafeCloseable(
						ctCollection.getCtCollectionId())) {

				fragmentEntryLinks =
					_fragmentEntryLinkLocalService.getFragmentEntryLinksByPlid(
						layout.getGroupId(), layout.getPlid());

				Assert.assertEquals(
					fragmentEntryLinks.toString(), 1,
					fragmentEntryLinks.size());

				publishedLayoutFragmentEntryLink = fragmentEntryLinks.get(0);

				Assert.assertTrue(
					publishedLayoutFragmentEntryLink.getEditableValues(),
					JSONUtil.equals(
						editableValuesJSONObject2,
						JSONFactoryUtil.createJSONObject(
							publishedLayoutFragmentEntryLink.
								getEditableValues())));

				draftLayoutFragmentEntryLink =
					_fragmentEntryLinkLocalService.getFragmentEntryLink(
						draftLayoutFragmentEntryLink.getFragmentEntryLinkId());

				Assert.assertTrue(
					draftLayoutFragmentEntryLink.getEditableValues(),
					JSONUtil.equals(
						editableValuesJSONObject2,
						JSONFactoryUtil.createJSONObject(
							draftLayoutFragmentEntryLink.getEditableValues())));
			}
		}
	}

	private void _runUpgrade() throws Exception {
		UpgradeProcess[] upgradeProcesses = UpgradeTestUtil.getUpgradeSteps(
			_upgradeStepRegistrator, new Version(2, 9, 1));

		for (UpgradeProcess upgradeProcess : upgradeProcesses) {
			upgradeProcess.upgrade();
		}

		_multiVMPool.clear();
	}

	@Inject(
		filter = "(&(component.name=com.liferay.fragment.internal.upgrade.registry.FragmentServiceUpgradeStepRegistrator))"
	)
	private static UpgradeStepRegistrator _upgradeStepRegistrator;

	@Inject
	private CTCollectionLocalService _ctCollectionLocalService;

	@Inject
	private FragmentEntryLinkLocalService _fragmentEntryLinkLocalService;

	@DeleteAfterTestRun
	private Group _group;

	@Inject
	private MultiVMPool _multiVMPool;

	@Inject
	private SegmentsExperienceLocalService _segmentsExperienceLocalService;

}