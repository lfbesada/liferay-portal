/**
 * SPDX-FileCopyrightText: (c) 2026 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.headless.admin.site.resource.v1_0.test;

import com.liferay.arquillian.extension.junit.bridge.junit.Arquillian;
import com.liferay.headless.admin.site.client.dto.v1_0.PageSpecificationVersion;
import com.liferay.layout.content.versioning.service.LayoutContentVersionLocalService;
import com.liferay.layout.test.util.LayoutTestUtil;
import com.liferay.petra.string.StringPool;
import com.liferay.portal.kernel.model.Group;
import com.liferay.portal.kernel.model.Layout;
import com.liferay.portal.kernel.service.GroupLocalService;
import com.liferay.portal.kernel.service.LayoutLocalService;
import com.liferay.portal.kernel.test.rule.AggregateTestRule;
import com.liferay.portal.kernel.test.util.TestPropsValues;
import com.liferay.portal.kernel.workflow.WorkflowConstants;
import com.liferay.portal.test.rule.FeatureFlag;
import com.liferay.portal.test.rule.Inject;
import com.liferay.portal.test.rule.LiferayIntegrationTestRule;
import com.liferay.portal.test.rule.PermissionCheckerMethodTestRule;

import org.junit.Before;
import org.junit.ClassRule;
import org.junit.Ignore;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

/**
 * @author Lourdes Fernández Besada
 */
@FeatureFlag("LPD-10622")
@RunWith(Arquillian.class)
public class PageSpecificationVersionResourceTest
	extends BasePageSpecificationVersionResourceTestCase {

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

		_testGroupLayout = LayoutTestUtil.addTypeContentLayout(testGroup);
		_irrelevantGroupLayout = LayoutTestUtil.addTypeContentLayout(
			irrelevantGroup);
	}

	@Ignore
	@Override
	@Test
	public void testBatchEngineDeleteImportTask() throws Exception {
		super.testBatchEngineDeleteImportTask();
	}

	@Override
	protected String[] getAdditionalAssertFieldNames() {
		return new String[] {"externalReferenceCode", "name"};
	}

	@Override
	protected PageSpecificationVersion
			testGetSiteSitePagePageSpecificationVersion_addPageSpecificationVersion()
		throws Exception {

		return _addPageSpecificationVersion(
			testGroup, _testGroupLayout, randomPageSpecificationVersion());
	}

	@Override
	protected String
			testGetSiteSitePagePageSpecificationVersion_getSitePageExternalReferenceCode()
		throws Exception {

		return _testGroupLayout.getExternalReferenceCode();
	}

	@Override
	protected PageSpecificationVersion
			testGetSiteSitePagePageSpecificationVersionsPage_addPageSpecificationVersion(
				String siteExternalReferenceCode,
				String sitePageExternalReferenceCode,
				PageSpecificationVersion pageSpecificationVersion)
		throws Exception {

		return _addPageSpecificationVersion(
			siteExternalReferenceCode, sitePageExternalReferenceCode,
			pageSpecificationVersion);
	}

	@Override
	protected String
			testGetSiteSitePagePageSpecificationVersionsPage_getIrrelevantSitePageExternalReferenceCode()
		throws Exception {

		return _irrelevantGroupLayout.getExternalReferenceCode();
	}

	@Override
	protected String
			testGetSiteSitePagePageSpecificationVersionsPage_getSitePageExternalReferenceCode()
		throws Exception {

		return _testGroupLayout.getExternalReferenceCode();
	}

	private PageSpecificationVersion _addPageSpecificationVersion(
			Group group, Layout layout,
			PageSpecificationVersion pageSpecificationVersion)
		throws Exception {

		Layout draftLayout = layout.fetchDraftLayout();

		_layoutContentVersionLocalService.addLayoutContentVersion(
			pageSpecificationVersion.getExternalReferenceCode(),
			TestPropsValues.getUserId(), draftLayout.getPlid(),
			pageSpecificationVersion.getName(), StringPool.BLANK,
			WorkflowConstants.STATUS_APPROVED, false);

		return pageSpecificationVersionResource.
			getSiteSitePagePageSpecificationVersion(
				group.getExternalReferenceCode(),
				layout.getExternalReferenceCode(),
				pageSpecificationVersion.getExternalReferenceCode());
	}

	private PageSpecificationVersion _addPageSpecificationVersion(
			String siteExternalReferenceCode,
			String sitePageExternalReferenceCode,
			PageSpecificationVersion pageSpecificationVersion)
		throws Exception {

		Group group = _groupLocalService.getGroupByExternalReferenceCode(
			siteExternalReferenceCode, testCompany.getCompanyId());

		return _addPageSpecificationVersion(
			group,
			_layoutLocalService.getLayoutByExternalReferenceCode(
				sitePageExternalReferenceCode, group.getGroupId()),
			pageSpecificationVersion);
	}

	@Inject
	private GroupLocalService _groupLocalService;

	private Layout _irrelevantGroupLayout;

	@Inject
	private LayoutContentVersionLocalService _layoutContentVersionLocalService;

	@Inject
	private LayoutLocalService _layoutLocalService;

	private Layout _testGroupLayout;

}