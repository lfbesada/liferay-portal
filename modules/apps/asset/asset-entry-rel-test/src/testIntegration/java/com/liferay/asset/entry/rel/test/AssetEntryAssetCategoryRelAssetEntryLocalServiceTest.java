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

package com.liferay.asset.entry.rel.test;

import com.liferay.arquillian.extension.junit.bridge.junit.Arquillian;
import com.liferay.asset.kernel.model.AssetCategory;
import com.liferay.asset.kernel.model.AssetVocabulary;
import com.liferay.asset.kernel.service.AssetCategoryServiceUtil;
import com.liferay.asset.kernel.service.AssetEntryLocalService;
import com.liferay.asset.test.util.AssetTestUtil;
import com.liferay.portal.kernel.model.Group;
import com.liferay.portal.kernel.model.ResourceConstants;
import com.liferay.portal.kernel.model.User;
import com.liferay.portal.kernel.model.role.RoleConstants;
import com.liferay.portal.kernel.security.permission.ActionKeys;
import com.liferay.portal.kernel.security.permission.PermissionCheckerFactoryUtil;
import com.liferay.portal.kernel.security.permission.PermissionThreadLocal;
import com.liferay.portal.kernel.test.rule.AggregateTestRule;
import com.liferay.portal.kernel.test.rule.DeleteAfterTestRun;
import com.liferay.portal.kernel.test.util.GroupTestUtil;
import com.liferay.portal.kernel.test.util.RoleTestUtil;
import com.liferay.portal.kernel.test.util.UserTestUtil;
import com.liferay.portal.kernel.util.ArrayUtil;
import com.liferay.portal.test.rule.Inject;
import com.liferay.portal.test.rule.LiferayIntegrationTestRule;
import com.liferay.portal.test.rule.PermissionCheckerMethodTestRule;

import java.util.ArrayList;
import java.util.List;

import org.junit.Assert;
import org.junit.Before;
import org.junit.ClassRule;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

/**
 * @author Samuel Trong Tran
 */
@RunWith(Arquillian.class)
public class AssetEntryAssetCategoryRelAssetEntryLocalServiceTest {

	@ClassRule
	@Rule
	public static final AggregateTestRule aggregateTestRule =
		new AggregateTestRule(
			new LiferayIntegrationTestRule(),
			PermissionCheckerMethodTestRule.INSTANCE);

	@Before
	public void setUp() throws Exception {
		_group = GroupTestUtil.addGroup();
		_user = UserTestUtil.addUser();

		_creatorUser = UserTestUtil.addGroupUser(
			_group, RoleConstants.SITE_MEMBER);

		_assetVocabulary = AssetTestUtil.addVocabulary(
			_group.getGroupId(), _creatorUser.getUserId());

		for (int i = 0; i < _assetCategoryIds.length; i++) {
			_assetCategoryIds[i] = _addAssetCategory(_group, _assetVocabulary);
		}
	}

	@Test
	public void testUserCategoryIds() throws Exception {
		_assertSize(1, ArrayUtil.subset(_assetCategoryIds, 0, 1));

		_assertSize(2, _assetCategoryIds);

		_assertSize(2, null);

		_assertSize(0, new long[0]);
	}

	@Test
	public void testUserCategoryIdsWithoutViewPermissionsOverCategory()
		throws Exception {

		_assertSize(2, _assetCategoryIds);

		AssetCategory assetCategory1 = _assetCategories.get(0);
		AssetCategory assetCategory2 = _assetCategories.get(1);

		RoleTestUtil.removeResourcePermission(
			RoleConstants.GUEST, AssetCategory.class.getName(),
			ResourceConstants.SCOPE_INDIVIDUAL,
			String.valueOf(assetCategory1.getCategoryId()), ActionKeys.VIEW);

		RoleTestUtil.removeResourcePermission(
			RoleConstants.SITE_MEMBER, AssetCategory.class.getName(),
			ResourceConstants.SCOPE_INDIVIDUAL,
			String.valueOf(assetCategory1.getCategoryId()), ActionKeys.VIEW);

		PermissionThreadLocal.setPermissionChecker(
			PermissionCheckerFactoryUtil.create(_user));

		_assertSize(2, new long[] {assetCategory2.getCategoryId()});
	}

	@Test
	public void testUserCategoryIdsWithoutViewPermissionsOverVocabulary()
		throws Exception {

		_assertSize(2, _assetCategoryIds);

		RoleTestUtil.removeResourcePermission(
			RoleConstants.GUEST, AssetVocabulary.class.getName(),
			ResourceConstants.SCOPE_INDIVIDUAL,
			String.valueOf(_assetVocabulary.getVocabularyId()),
			ActionKeys.VIEW);

		RoleTestUtil.removeResourcePermission(
			RoleConstants.SITE_MEMBER, AssetVocabulary.class.getName(),
			ResourceConstants.SCOPE_INDIVIDUAL,
			String.valueOf(_assetVocabulary.getVocabularyId()),
			ActionKeys.VIEW);

		PermissionThreadLocal.setPermissionChecker(
			PermissionCheckerFactoryUtil.create(_user));

		_assertSize(2, new long[0]);
	}

	@Inject
	protected static AssetEntryLocalService assetEntryLocalService;

	private long _addAssetCategory(Group group, AssetVocabulary assetVocabulary)
		throws Exception {

		AssetCategory assetCategory = AssetTestUtil.addCategory(
			group.getGroupId(), assetVocabulary.getVocabularyId(),
			_creatorUser.getUserId());

		_assetCategories.add(assetCategory);

		return assetCategory.getCategoryId();
	}

	private void _assertSize(int expectedSize, long[] assetCategoryIds)
		throws Exception {

		assetEntryLocalService.updateEntry(
			_user.getUserId(), _group.getGroupId(), _user.getCreateDate(),
			_user.getModifiedDate(), User.class.getName(), _user.getUserId(),
			_user.getUuid(), 0, assetCategoryIds, null, true, false, null, null,
			null, null, null, _user.getFullName(), null, null, null, null, 0, 0,
			null);

		List<AssetCategory> assetCategories =
			AssetCategoryServiceUtil.getCategories(
				User.class.getName(), _user.getUserId());

		Assert.assertEquals(
			assetCategories.toString(), expectedSize, assetCategories.size());
	}

	@DeleteAfterTestRun
	private final List<AssetCategory> _assetCategories = new ArrayList<>();

	private final long[] _assetCategoryIds = new long[2];

	@DeleteAfterTestRun
	private AssetVocabulary _assetVocabulary;

	@DeleteAfterTestRun
	private User _creatorUser;

	@DeleteAfterTestRun
	private Group _group;

	@DeleteAfterTestRun
	private User _user;

}