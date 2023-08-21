/**
 * SPDX-FileCopyrightText: (c) 2023 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.layout.internal.verify.test;

import com.liferay.arquillian.extension.junit.bridge.junit.Arquillian;
import com.liferay.fragment.contributor.FragmentCollectionContributorRegistry;
import com.liferay.fragment.entry.processor.constants.FragmentEntryProcessorConstants;
import com.liferay.fragment.model.FragmentEntry;
import com.liferay.layout.model.LayoutLocalization;
import com.liferay.layout.service.LayoutLocalizationLocalService;
import com.liferay.layout.test.util.ContentLayoutTestUtil;
import com.liferay.layout.test.util.LayoutTestUtil;
import com.liferay.portal.kernel.cache.MultiVMPool;
import com.liferay.portal.kernel.json.JSONUtil;
import com.liferay.portal.kernel.model.Group;
import com.liferay.portal.kernel.model.Layout;
import com.liferay.portal.kernel.service.LayoutLocalService;
import com.liferay.portal.kernel.test.rule.AggregateTestRule;
import com.liferay.portal.kernel.test.rule.DeleteAfterTestRun;
import com.liferay.portal.kernel.test.util.GroupTestUtil;
import com.liferay.portal.kernel.test.util.RandomTestUtil;
import com.liferay.portal.kernel.util.LocaleUtil;
import com.liferay.portal.kernel.util.Portal;
import com.liferay.portal.kernel.util.StringUtil;
import com.liferay.portal.test.log.LogCapture;
import com.liferay.portal.test.log.LogEntry;
import com.liferay.portal.test.log.LoggerTestUtil;
import com.liferay.portal.test.rule.Inject;
import com.liferay.portal.test.rule.LiferayIntegrationTestRule;
import com.liferay.portal.test.rule.PermissionCheckerMethodTestRule;
import com.liferay.portal.verify.VerifyProcess;
import com.liferay.portal.verify.test.util.BaseVerifyProcessTestCase;
import com.liferay.segments.service.SegmentsExperienceLocalService;

import java.util.List;

import org.junit.Assert;
import org.junit.Before;
import org.junit.ClassRule;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

/**
 * @author Lourdes Fernández Besada
 */
@RunWith(Arquillian.class)
public class LayoutLocalizationVerifyProcessTest
	extends BaseVerifyProcessTestCase {

	@ClassRule
	@Rule
	public static final AggregateTestRule aggregateTestRule =
		new AggregateTestRule(
			new LiferayIntegrationTestRule(),
			PermissionCheckerMethodTestRule.INSTANCE);

	@Before
	@Override
	public void setUp() throws Exception {
		super.setUp();

		_group = GroupTestUtil.addGroup();
	}

	@Test
	public void testDoVerify() throws Exception {
		String headingText = RandomTestUtil.randomString();

		String languageId = LocaleUtil.toLanguageId(
			_portal.getSiteDefaultLocale(_group));

		Layout layout = _addTypeContentLayout(languageId, true, headingText);

		int expectedLayoutLocalizationsSize = _deleteLayoutLocalizations(
			layout.getPlid());

		List<LogEntry> logEntries = _getDoVerifyLogEntries();

		Assert.assertEquals(logEntries.toString(), 0, logEntries.size());

		List<LayoutLocalization> layoutLocalizations =
			_layoutLocalizationLocalService.getLayoutLocalizations(
				layout.getPlid());

		Assert.assertEquals(
			layoutLocalizations.toString(), expectedLayoutLocalizationsSize,
			layoutLocalizations.size());

		LayoutLocalization layoutLocalization =
			_layoutLocalizationLocalService.fetchLayoutLocalization(
				layout.getGroupId(), languageId, layout.getPlid());

		Assert.assertNotNull(layoutLocalization);

		Assert.assertTrue(
			layoutLocalization.getContent(),
			StringUtil.contains(layoutLocalization.getContent(), headingText));
	}

	@Test
	public void testDoVerifyExistingLayoutLocalizations() throws Exception {
		String languageId = LocaleUtil.toLanguageId(
			_portal.getSiteDefaultLocale(_group));

		Layout layout = _addTypeContentLayout(
			languageId, true, RandomTestUtil.randomString());

		List<LayoutLocalization> layoutLocalizations1 =
			_layoutLocalizationLocalService.getLayoutLocalizations(
				layout.getPlid());

		Assert.assertFalse(
			layoutLocalizations1.toString(), layoutLocalizations1.isEmpty());

		List<LogEntry> logEntries = _getDoVerifyLogEntries();

		Assert.assertEquals(logEntries.toString(), 0, logEntries.size());

		List<LayoutLocalization> layoutLocalizations2 =
			_layoutLocalizationLocalService.getLayoutLocalizations(
				layout.getPlid());

		Assert.assertEquals(
			layoutLocalizations2.toString(), layoutLocalizations1,
			layoutLocalizations2);
	}

	@Test
	public void testDoVerifyPublishedDraftLayout() throws Exception {
		String headingText1 = RandomTestUtil.randomString();
		String headingText2 = RandomTestUtil.randomString();

		String languageId = LocaleUtil.toLanguageId(
			_portal.getSiteDefaultLocale(_group));

		Layout layout = _addTypeContentLayout(languageId, true, headingText1);

		_addHeadingFragmentToLayout(languageId, headingText2, layout);

		int expectedLayoutLocalizationsSize = _deleteLayoutLocalizations(
			layout.getPlid());

		List<LogEntry> logEntries = _getDoVerifyLogEntries();

		Assert.assertEquals(logEntries.toString(), 0, logEntries.size());

		List<LayoutLocalization> layoutLocalizations =
			_layoutLocalizationLocalService.getLayoutLocalizations(
				layout.getPlid());

		Assert.assertEquals(
			layoutLocalizations.toString(), expectedLayoutLocalizationsSize,
			layoutLocalizations.size());

		LayoutLocalization layoutLocalization =
			_layoutLocalizationLocalService.fetchLayoutLocalization(
				layout.getGroupId(), languageId, layout.getPlid());

		Assert.assertNotNull(layoutLocalization);

		Assert.assertTrue(
			layoutLocalization.getContent(),
			StringUtil.contains(layoutLocalization.getContent(), headingText1));

		Assert.assertFalse(
			layoutLocalization.getContent(),
			StringUtil.contains(layoutLocalization.getContent(), headingText2));
	}

	@Test
	public void testDoVerifyUnpublishedDraftLayout() throws Exception {
		String headingText = RandomTestUtil.randomString();

		String languageId = LocaleUtil.toLanguageId(
			_portal.getSiteDefaultLocale(_group));

		Layout layout = _addTypeContentLayout(languageId, false, headingText);

		List<LayoutLocalization> layoutLocalizations =
			_layoutLocalizationLocalService.getLayoutLocalizations(
				layout.getPlid());

		Assert.assertTrue(
			layoutLocalizations.toString(), layoutLocalizations.isEmpty());

		List<LogEntry> logEntries = _getDoVerifyLogEntries();

		Assert.assertEquals(logEntries.toString(), 0, logEntries.size());

		layoutLocalizations =
			_layoutLocalizationLocalService.getLayoutLocalizations(
				layout.getPlid());

		Assert.assertTrue(
			layoutLocalizations.toString(), layoutLocalizations.isEmpty());
	}

	@Override
	protected VerifyProcess getVerifyProcess() {
		return _verifyProcess;
	}

	private Layout _addHeadingFragmentToLayout(
			String languageId, String headingText, Layout layout)
		throws Exception {

		Layout draftLayout = layout.fetchDraftLayout();

		Assert.assertNotNull(draftLayout);

		FragmentEntry fragmentEntry =
			_fragmentCollectionContributorRegistry.getFragmentEntry(
				"BASIC_COMPONENT-heading");

		Assert.assertNotNull(fragmentEntry);

		ContentLayoutTestUtil.addFragmentEntryLinkToLayout(
			JSONUtil.put(
				FragmentEntryProcessorConstants.
					KEY_EDITABLE_FRAGMENT_ENTRY_PROCESSOR,
				JSONUtil.put(
					"element-text", JSONUtil.put(languageId, headingText))
			).put(
				FragmentEntryProcessorConstants.
					KEY_FREEMARKER_FRAGMENT_ENTRY_PROCESSOR,
				JSONUtil.put("headingLevel", "h1")
			).toString(),
			fragmentEntry.getCss(), fragmentEntry.getConfiguration(),
			fragmentEntry.getFragmentEntryId(), fragmentEntry.getHtml(),
			fragmentEntry.getJs(), draftLayout,
			fragmentEntry.getFragmentEntryKey(), fragmentEntry.getType(), null,
			0,
			_segmentsExperienceLocalService.fetchDefaultSegmentsExperienceId(
				draftLayout.getPlid()));

		return draftLayout;
	}

	private Layout _addTypeContentLayout(
			String languageId, boolean publish, String headingText)
		throws Exception {

		Layout layout = LayoutTestUtil.addTypeContentLayout(_group);

		Layout draftLayout = _addHeadingFragmentToLayout(
			languageId, headingText, layout);

		if (publish) {
			ContentLayoutTestUtil.publishLayout(draftLayout, layout);

			layout = _layoutLocalService.getLayout(layout.getPlid());
		}

		return layout;
	}

	private int _deleteLayoutLocalizations(long plid) {
		List<LayoutLocalization> layoutLocalizations =
			_layoutLocalizationLocalService.getLayoutLocalizations(plid);

		Assert.assertFalse(
			layoutLocalizations.toString(), layoutLocalizations.isEmpty());

		for (LayoutLocalization layoutLocalization : layoutLocalizations) {
			_layoutLocalizationLocalService.deleteLayoutLocalization(
				layoutLocalization);
		}

		int originalLayoutLocalizationsSize = layoutLocalizations.size();

		layoutLocalizations =
			_layoutLocalizationLocalService.getLayoutLocalizations(plid);

		Assert.assertTrue(
			layoutLocalizations.toString(), layoutLocalizations.isEmpty());

		return originalLayoutLocalizationsSize;
	}

	private List<LogEntry> _getDoVerifyLogEntries() throws Exception {
		try (LogCapture logCapture = LoggerTestUtil.configureLog4JLogger(
				_CLASS_NAME, LoggerTestUtil.WARN)) {

			doVerify();

			_multiVMPool.clear();

			return logCapture.getLogEntries();
		}
	}

	private static final String _CLASS_NAME =
		"com.liferay.layout.internal.verify.LayoutLocalizationVerifyProcess";

	@Inject
	private FragmentCollectionContributorRegistry
		_fragmentCollectionContributorRegistry;

	@DeleteAfterTestRun
	private Group _group;

	@Inject
	private LayoutLocalizationLocalService _layoutLocalizationLocalService;

	@Inject
	private LayoutLocalService _layoutLocalService;

	@Inject
	private MultiVMPool _multiVMPool;

	@Inject
	private Portal _portal;

	@Inject
	private SegmentsExperienceLocalService _segmentsExperienceLocalService;

	@Inject(
		filter = "component.name=com.liferay.layout.internal.verify.LayoutLocalizationVerifyProcess"
	)
	private VerifyProcess _verifyProcess;

}