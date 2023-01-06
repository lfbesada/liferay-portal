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

package com.liferay.user.service.test;

import com.liferay.arquillian.extension.junit.bridge.junit.Arquillian;
import com.liferay.petra.string.StringBundler;
import com.liferay.petra.string.StringPool;
import com.liferay.portal.kernel.model.Group;
import com.liferay.portal.kernel.model.GroupConstants;
import com.liferay.portal.kernel.model.User;
import com.liferay.portal.kernel.service.CompanyLocalService;
import com.liferay.portal.kernel.service.GroupLocalService;
import com.liferay.portal.kernel.service.UserLocalService;
import com.liferay.portal.kernel.test.rule.AggregateTestRule;
import com.liferay.portal.kernel.test.rule.DeleteAfterTestRun;
import com.liferay.portal.kernel.test.util.GroupTestUtil;
import com.liferay.portal.kernel.test.util.UserTestUtil;
import com.liferay.portal.kernel.util.ArrayUtil;
import com.liferay.portal.kernel.util.LocaleUtil;
import com.liferay.portal.kernel.util.PropsKeys;
import com.liferay.portal.kernel.util.UnicodePropertiesBuilder;
import com.liferay.portal.test.rule.Inject;
import com.liferay.portal.test.rule.LiferayIntegrationTestRule;

import org.junit.Assert;
import org.junit.Before;
import org.junit.ClassRule;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

/**
 * @author Huy Le Nguyen
 */
@RunWith(Arquillian.class)
public class UserServiceWhenAddingDefaultGroupTest {

	@ClassRule
	@Rule
	public static final AggregateTestRule aggregateTestRule =
		new LiferayIntegrationTestRule();

	@Before
	public void setUp() throws Exception {
		_parentGroup = GroupTestUtil.addGroup();

		_childGroup = GroupTestUtil.addGroup(_parentGroup.getGroupId());

		_childGroup.setMembershipRestriction(
			GroupConstants.MEMBERSHIP_RESTRICTION_TO_PARENT_SITE_MEMBERS);

		_groupLocalService.updateGroup(_childGroup);

		_grandChildGroup1 = GroupTestUtil.addGroup(_childGroup.getGroupId());

		_grandChildGroup1.setMembershipRestriction(
			GroupConstants.MEMBERSHIP_RESTRICTION_TO_PARENT_SITE_MEMBERS);

		_groupLocalService.updateGroup(_grandChildGroup1);

		_grandChildGroup2 = GroupTestUtil.addGroup(_childGroup.getGroupId());

		_user = UserTestUtil.addUser();

		_companyLocalService.updatePreferences(
			_user.getCompanyId(),
			UnicodePropertiesBuilder.put(
				PropsKeys.ADMIN_DEFAULT_GROUP_NAMES,
				StringBundler.concat(
					_parentGroup.getName(LocaleUtil.US), StringPool.NEW_LINE,
					_grandChildGroup2.getName(LocaleUtil.US),
					StringPool.NEW_LINE,
					_grandChildGroup1.getName(LocaleUtil.US))
			).build());
	}

	@Test
	public void testAddDefaultGroup() throws Exception {
		_userLocalService.addDefaultGroups(_user.getUserId());

		Assert.assertTrue(
			ArrayUtil.contains(_user.getGroupIds(), _parentGroup.getGroupId()));

		Assert.assertTrue(
			ArrayUtil.contains(
				_user.getGroupIds(), _grandChildGroup2.getGroupId()));

		Assert.assertFalse(
			ArrayUtil.contains(_user.getGroupIds(), _childGroup.getGroupId()));

		Assert.assertFalse(
			ArrayUtil.contains(
				_user.getGroupIds(), _grandChildGroup1.getGroupId()));
	}

	@DeleteAfterTestRun
	private Group _childGroup;

	@Inject
	private CompanyLocalService _companyLocalService;

	@DeleteAfterTestRun
	private Group _grandChildGroup1;

	@DeleteAfterTestRun
	private Group _grandChildGroup2;

	@Inject
	private GroupLocalService _groupLocalService;

	@DeleteAfterTestRun
	private Group _parentGroup;

	@DeleteAfterTestRun
	private User _user;

	@Inject
	private UserLocalService _userLocalService;

}