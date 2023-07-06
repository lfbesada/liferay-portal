/**
 * Copyright (c) 2000-present Liferay, Inc. All rights reserved.
 *
 * This library is free software; you can redistribute it and/or modify it under
 * the terms of the GNU Lesser General Public License as published by the Free
 * Software Foundation; either version 2.1 of the License, or (at your option)
 * any later version.
 *
 * This library is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU Lesser General Public License for more
 * details.
 */

package com.liferay.layout.service.test;

import com.liferay.arquillian.extension.junit.bridge.junit.Arquillian;
import com.liferay.asset.kernel.model.AssetEntry;
import com.liferay.asset.kernel.service.AssetEntryLocalService;
import com.liferay.asset.publisher.constants.AssetPublisherPortletKeys;
import com.liferay.journal.constants.JournalFolderConstants;
import com.liferay.journal.model.JournalArticle;
import com.liferay.journal.test.util.JournalTestUtil;
import com.liferay.layout.model.LayoutClassedModelUsage;
import com.liferay.layout.service.LayoutClassedModelUsageLocalService;
import com.liferay.layout.test.util.ContentLayoutTestUtil;
import com.liferay.layout.test.util.LayoutTestUtil;
import com.liferay.petra.string.StringPool;
import com.liferay.portal.kernel.model.Group;
import com.liferay.portal.kernel.model.Layout;
import com.liferay.portal.kernel.portlet.PortletLayoutListener;
import com.liferay.portal.kernel.service.ClassNameLocalService;
import com.liferay.portal.kernel.service.CompanyLocalService;
import com.liferay.portal.kernel.service.ServiceContext;
import com.liferay.portal.kernel.service.ServiceContextThreadLocal;
import com.liferay.portal.kernel.test.rule.AggregateTestRule;
import com.liferay.portal.kernel.test.rule.DeleteAfterTestRun;
import com.liferay.portal.kernel.test.util.GroupTestUtil;
import com.liferay.portal.kernel.test.util.ServiceContextTestUtil;
import com.liferay.portal.kernel.test.util.TestPropsValues;
import com.liferay.portal.kernel.theme.ThemeDisplay;
import com.liferay.portal.kernel.util.HashMapBuilder;
import com.liferay.portal.kernel.util.ListUtil;
import com.liferay.portal.kernel.util.WebKeys;
import com.liferay.portal.kernel.xml.Document;
import com.liferay.portal.kernel.xml.Element;
import com.liferay.portal.kernel.xml.SAXReaderUtil;
import com.liferay.portal.test.rule.Inject;
import com.liferay.portal.test.rule.LiferayIntegrationTestRule;
import com.liferay.portal.test.rule.PermissionCheckerMethodTestRule;

import java.util.List;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.ClassRule;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import org.springframework.mock.web.MockHttpServletRequest;

/**
 * @author Yurena Cabrera
 */
@RunWith(Arquillian.class)
public class LayoutClassedModelUsageLocalServiceTest {

	@ClassRule
	@Rule
	public static final AggregateTestRule aggregateTestRule =
		new AggregateTestRule(
			new LiferayIntegrationTestRule(),
			PermissionCheckerMethodTestRule.INSTANCE);

	@Before
	public void setUp() throws Exception {
		_group = GroupTestUtil.addGroup();

		_layout = LayoutTestUtil.addTypePortletLayout(_group);

		_pushServiceContext();
	}

	@After
	public void tearDown() throws Exception {
		ServiceContextThreadLocal.popServiceContext();
	}

	@Test
	public void testGetUniqueLayoutClassedModelUsagesCount() throws Exception {
		JournalArticle journalArticle = JournalTestUtil.addArticle(
			_group.getGroupId(),
			JournalFolderConstants.DEFAULT_PARENT_FOLDER_ID);

		_addLayoutClassedModelUsage(journalArticle.getResourcePrimKey());
		_addLayoutClassedModelUsage(journalArticle.getResourcePrimKey());
		_addLayoutClassedModelUsage(journalArticle.getResourcePrimKey());

		Assert.assertEquals(
			3,
			_layoutClassedModelUsageLocalService.
				getUniqueLayoutClassedModelUsagesCount(
					_classNameLocalService.getClassNameId(
						JournalArticle.class.getName()),
					journalArticle.getResourcePrimKey(),
					journalArticle.getExternalReferenceCode()));
	}

	private void _addLayoutClassedModelUsage(long resourcePrimKey)
		throws Exception {

		Layout layout = LayoutTestUtil.addTypePortletLayout(_group);

		AssetEntry assetEntry = _assetEntryLocalService.getEntry(
			JournalArticle.class.getName(), resourcePrimKey);

		Document document = SAXReaderUtil.createDocument(StringPool.UTF8);

		Element assetEntryElement = document.addElement("asset-entry");

		assetEntryElement.addElement("asset-entry-type");

		Element assetEntryUuidElement = assetEntryElement.addElement(
			"asset-entry-uuid");

		assetEntryUuidElement.addText(assetEntry.getClassUuid());

		List<LayoutClassedModelUsage> layoutClassedModelUsages1 =
			_layoutClassedModelUsageLocalService.
				getLayoutClassedModelUsagesByPlid(layout.getPlid());

		String portletId = LayoutTestUtil.addPortletToLayout(
			layout, AssetPublisherPortletKeys.ASSET_PUBLISHER,
			HashMapBuilder.put(
				"assetEntryXml",
				new String[] {document.formattedString(StringPool.BLANK)}
			).put(
				"selectionStyle", new String[] {"manual"}
			).build());

		_portletLayoutListener.onSetup(portletId, layout.getPlid());

		List<LayoutClassedModelUsage> layoutClassedModelUsages2 =
			ListUtil.remove(
				_layoutClassedModelUsageLocalService.
					getLayoutClassedModelUsagesByPlid(layout.getPlid()),
				layoutClassedModelUsages1);

		Assert.assertEquals(
			layoutClassedModelUsages2.toString(), 1,
			layoutClassedModelUsages2.size());

		LayoutClassedModelUsage layoutClassedModelUsage =
			layoutClassedModelUsages2.get(0);

		Assert.assertEquals(
			resourcePrimKey, layoutClassedModelUsage.getClassPK());
	}

	private void _pushServiceContext() throws Exception {
		ServiceContext serviceContext =
			ServiceContextTestUtil.getServiceContext(
				_group.getGroupId(), TestPropsValues.getUserId());

		MockHttpServletRequest mockHttpServletRequest =
			new MockHttpServletRequest();

		mockHttpServletRequest.setAttribute(WebKeys.LAYOUT, _layout);

		ThemeDisplay themeDisplay = ContentLayoutTestUtil.getThemeDisplay(
			_companyLocalService.getCompany(_group.getCompanyId()), _group,
			_layout);

		themeDisplay.setRequest(mockHttpServletRequest);

		mockHttpServletRequest.setAttribute(
			WebKeys.THEME_DISPLAY, themeDisplay);

		serviceContext.setRequest(mockHttpServletRequest);

		ServiceContextThreadLocal.pushServiceContext(serviceContext);
	}

	@Inject
	private AssetEntryLocalService _assetEntryLocalService;

	@Inject
	private ClassNameLocalService _classNameLocalService;

	@Inject
	private CompanyLocalService _companyLocalService;

	@DeleteAfterTestRun
	private Group _group;

	private Layout _layout;

	@Inject
	private LayoutClassedModelUsageLocalService
		_layoutClassedModelUsageLocalService;

	@Inject(
		filter = "(&(component.name=com.liferay.asset.publisher.web.internal.portlet.layout.listener.AssetPublisherPortletLayoutListener))"
	)
	private PortletLayoutListener _portletLayoutListener;

}