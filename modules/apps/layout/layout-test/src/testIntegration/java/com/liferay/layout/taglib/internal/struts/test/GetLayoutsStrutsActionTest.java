/**
 * SPDX-FileCopyrightText: (c) 2023 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */


package com.liferay.layout.taglib.internal.struts.test;

import com.liferay.arquillian.extension.junit.bridge.junit.Arquillian;
import com.liferay.layout.test.util.ContentLayoutTestUtil;
import com.liferay.layout.test.util.LayoutTestUtil;
import com.liferay.petra.string.StringPool;
import com.liferay.portal.kernel.json.JSONArray;
import com.liferay.portal.kernel.json.JSONFactory;
import com.liferay.portal.kernel.json.JSONObject;
import com.liferay.portal.kernel.model.Group;
import com.liferay.portal.kernel.model.Layout;
import com.liferay.portal.kernel.model.LayoutConstants;
import com.liferay.portal.kernel.model.ResourceConstants;
import com.liferay.portal.kernel.model.role.RoleConstants;
import com.liferay.portal.kernel.security.permission.ActionKeys;
import com.liferay.portal.kernel.service.CompanyLocalService;
import com.liferay.portal.kernel.service.LayoutLocalService;
import com.liferay.portal.kernel.service.LayoutLocalServiceUtil;
import com.liferay.portal.kernel.struts.StrutsAction;
import com.liferay.portal.kernel.test.rule.AggregateTestRule;
import com.liferay.portal.kernel.test.rule.DeleteAfterTestRun;
import com.liferay.portal.kernel.test.util.GroupTestUtil;
import com.liferay.portal.kernel.test.util.RandomTestUtil;
import com.liferay.portal.kernel.test.util.RoleTestUtil;
import com.liferay.portal.kernel.test.util.ServiceContextTestUtil;
import com.liferay.portal.kernel.test.util.TestPropsValues;
import com.liferay.portal.kernel.util.GetterUtil;
import com.liferay.portal.kernel.util.ListUtil;
import com.liferay.portal.kernel.util.Portal;
import com.liferay.portal.test.rule.Inject;
import com.liferay.portal.test.rule.LiferayIntegrationTestRule;
import com.liferay.portal.test.rule.PermissionCheckerMethodTestRule;
import com.liferay.portal.util.PropsValues;
import org.junit.Assert;
import org.junit.Before;
import org.junit.ClassRule;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author Lourdes Fernández Besada
 */
@RunWith(Arquillian.class)
public class GetLayoutsStrutsActionTest {

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


	@Inject
	private CompanyLocalService _companyLocalService;

	@Inject
	private Portal _portal;

	private static final int _COUNT_ROOT_LAYOUTS = 5;

	private static final int _COUNT_CHILDREN_LAYOUTS = 5;

	private static final int _CHILDREN_PROBABILITY = 50;

	private static final int _DRAFT_LAYOUT_PROBABILITY = 10;

	private static final int _RESTRICTED_LAYOUT_PROBABILITY = 5;

	private boolean _getBooleanWithProbability(int probability) {
		if (RandomTestUtil.randomInt(1, 101) <= probability) {
			return true;
		}

		return false;
	}
	private Map<Long, List<Long>> _getLayouts(int count, long parentLayoutId)
		throws Exception {
		Map<Long, List<Long>> layoutIdsMap = new HashMap<>();

		for (int i = 0; i < count; i++) {
			Layout layout = LayoutLocalServiceUtil.addLayout(
				TestPropsValues.getUserId(), _group.getGroupId(), false,
				parentLayoutId,
				RandomTestUtil.randomString(), StringPool.BLANK, StringPool.BLANK,
				LayoutConstants.TYPE_CONTENT, false, StringPool.BLANK,
				ServiceContextTestUtil.getServiceContext(
					TestPropsValues.getGroupId(), TestPropsValues.getUserId()));

			if (_getBooleanWithProbability(_DRAFT_LAYOUT_PROBABILITY)) {
				continue;
			}

			ContentLayoutTestUtil.publishLayout(layout.fetchDraftLayout(), layout);

			if (_getBooleanWithProbability(_RESTRICTED_LAYOUT_PROBABILITY)) {
				RoleTestUtil.removeResourcePermission(
					RoleConstants.GUEST, Layout.class.getName(),
					ResourceConstants.SCOPE_INDIVIDUAL,
					String.valueOf(layout.getPlid()), ActionKeys.VIEW);
				RoleTestUtil.removeResourcePermission(
					RoleConstants.SITE_MEMBER, Layout.class.getName(),
					ResourceConstants.SCOPE_INDIVIDUAL,
					String.valueOf(layout.getPlid()), ActionKeys.VIEW);

				continue;
			}

			List<Long> childrenLayoutIds = new ArrayList<>();

			if (parentLayoutId == LayoutConstants.DEFAULT_PARENT_LAYOUT_ID &&
				_getBooleanWithProbability(_CHILDREN_PROBABILITY)) {

				Map<Long, List<Long>> childrenLayoutIdsMap =
					_getLayouts(_COUNT_CHILDREN_LAYOUTS, layout.getLayoutId());

				childrenLayoutIds.addAll(childrenLayoutIdsMap.keySet());
			}

			layoutIdsMap.put(layout.getLayoutId(), childrenLayoutIds);
		}

		return layoutIdsMap;
	}

	@Inject
	private JSONFactory _jsonFactory;

	@Test
	public void test () throws Exception {

		Map<Long, List<Long>> layoutIdsMap = _getLayouts(
			_COUNT_ROOT_LAYOUTS, LayoutConstants.DEFAULT_PARENT_LAYOUT_ID);

		int pageSize = GetterUtil.getInteger(
			PropsValues.LAYOUT_MANAGE_PAGES_INITIAL_CHILDREN);

		int count = layoutIdsMap.size();
		int completePagesCount = count/pageSize;
		int offset = 0;

		int lastPageIndex = completePagesCount - 1;

		if (count % pageSize > 0) {
			lastPageIndex++;
		}

		MockHttpServletRequest mockHttpServletRequest =
			ContentLayoutTestUtil.getMockHttpServletRequest(
				_companyLocalService.getCompany(_group.getCompanyId()), _group,
				_layoutLocalService.getLayout(_portal.getControlPanelPlid(_group.getCompanyId())));

		mockHttpServletRequest.addParameter("groupId", String.valueOf(_group.getGroupId()));


		for (int i = 0; i < completePagesCount; i++, offset = offset + pageSize) {
			mockHttpServletRequest.addParameter("start", String.valueOf(offset));
			mockHttpServletRequest.addParameter("end", String.valueOf(offset + pageSize));

			boolean hasMoreElements = true;

			if (i == lastPageIndex) {
				hasMoreElements = false;
			}

			_assertGetLayoutsStrutsAction(hasMoreElements , layoutIdsMap, mockHttpServletRequest, pageSize);
		}

		int lastPageItemCount = count % pageSize;

		if (lastPageItemCount > 0) {
			mockHttpServletRequest.setAttribute("start", offset);
			mockHttpServletRequest.setAttribute("end", offset + lastPageItemCount);

			_assertGetLayoutsStrutsAction(false, layoutIdsMap, mockHttpServletRequest, lastPageItemCount);

		}
	}

	private void _assertGetLayoutsStrutsAction(
		boolean hasMoreElements, Map<Long, List<Long>> layoutIdsMap,
		MockHttpServletRequest mockHttpServletRequest, int pageSize) throws Exception {
		MockHttpServletResponse httpServletResponse =
			new MockHttpServletResponse();

		_getLayoutsStrutsAction.execute(
			mockHttpServletRequest, httpServletResponse);

		JSONObject jsonObject = _jsonFactory.createJSONObject(
			httpServletResponse.getContentAsString());

		Assert.assertEquals(hasMoreElements, jsonObject.getBoolean("hasMoreElements"));

		JSONArray jsonArray = jsonObject.getJSONArray("items");

		Assert.assertEquals(pageSize, jsonArray.length());

		for (int j = 0; j < jsonArray.length(); j++) {
			JSONObject layoutJSONObject = jsonArray.getJSONObject(j);

			Assert.assertTrue(layoutJSONObject.has("hasChildren"));
			Assert.assertTrue(layoutJSONObject.has("paginated"));

			long layoutId = layoutJSONObject.getLong("layoutId");

			List<Long> childrenLayoutIds = layoutIdsMap.remove(layoutId);

			Assert.assertNotNull(childrenLayoutIds);

			Assert.assertEquals(ListUtil.isNotEmpty(childrenLayoutIds), layoutJSONObject.getBoolean("hasChildren"));
			Assert.assertEquals(childrenLayoutIds.size() > pageSize, layoutJSONObject.getBoolean("paginated"));

		}
	}

	@Inject
	private LayoutLocalService _layoutLocalService;

	@Inject(filter = "path=/portal/get_layouts")
	private StrutsAction _getLayoutsStrutsAction;

	@DeleteAfterTestRun
	private Group _group;
}
