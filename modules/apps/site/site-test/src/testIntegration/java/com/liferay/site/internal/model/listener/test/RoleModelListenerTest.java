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

package com.liferay.site.internal.model.listener.test;

import com.liferay.arquillian.extension.junit.bridge.junit.Arquillian;
import com.liferay.portal.kernel.model.Group;
import com.liferay.portal.kernel.model.Role;
import com.liferay.portal.kernel.model.role.RoleConstants;
import com.liferay.portal.kernel.module.configuration.ConfigurationProvider;
import com.liferay.portal.kernel.service.RoleLocalService;
import com.liferay.portal.kernel.test.rule.AggregateTestRule;
import com.liferay.portal.kernel.test.rule.DeleteAfterTestRun;
import com.liferay.portal.kernel.test.util.GroupTestUtil;
import com.liferay.portal.kernel.test.util.ServiceContextTestUtil;
import com.liferay.portal.kernel.test.util.TestPropsValues;
import com.liferay.portal.kernel.util.HashMapDictionaryBuilder;
import com.liferay.portal.kernel.util.StringUtil;
import com.liferay.portal.test.rule.FeatureFlags;
import com.liferay.portal.test.rule.Inject;
import com.liferay.portal.test.rule.LiferayIntegrationTestRule;
import com.liferay.portal.test.rule.PermissionCheckerMethodTestRule;
import com.liferay.site.configuration.MenuAccessConfiguration;
import com.liferay.site.configuration.MenuAccessConfigurationProvider;

import org.junit.Assert;
import org.junit.Before;
import org.junit.ClassRule;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

/**
 * @author Lourdes Fernández Besada
 */
@FeatureFlags("LPS-176136")
@RunWith(Arquillian.class)
public class RoleModelListenerTest {

	@ClassRule
	@Rule
	public static final AggregateTestRule aggregateTestRule =
		new AggregateTestRule(
			new LiferayIntegrationTestRule(),
			PermissionCheckerMethodTestRule.INSTANCE);

	@Before
	public void setUp() throws Exception {
		_group = GroupTestUtil.addGroup();

		_configurationProvider.saveGroupConfiguration(
			MenuAccessConfiguration.class, _group.getGroupId(),
			HashMapDictionaryBuilder.<String, Object>put(
				"rolesCanSeeControlMenu", new String[0]
			).put(
				"showControlMenuByRole", true
			).build());
	}

	@Test
	public void testAddRole() throws Exception {
		Role role = _roleLocalService.addRole(
			TestPropsValues.getUserId(), null, 0, StringUtil.randomString(),
			null, null, RoleConstants.TYPE_SITE, null,
			ServiceContextTestUtil.getServiceContext(_group.getGroupId()));

		Assert.assertArrayEquals(
			new String[] {String.valueOf(role.getRoleId())},
			_menuAccessConfigurationProvider.getRolesCanSeeControlMenu(
				_group.getGroupId()));
	}

	@Test
	public void testDeleteRole() throws Exception {
		Role role1 = _roleLocalService.addRole(
			TestPropsValues.getUserId(), null, 0, StringUtil.randomString(),
			null, null, RoleConstants.TYPE_SITE, null,
			ServiceContextTestUtil.getServiceContext(_group.getGroupId()));

		Role role2 = _roleLocalService.addRole(
			TestPropsValues.getUserId(), null, 0, StringUtil.randomString(),
			null, null, RoleConstants.TYPE_SITE, null,
			ServiceContextTestUtil.getServiceContext(_group.getGroupId()));

		Assert.assertArrayEquals(
			new String[] {
				String.valueOf(role1.getRoleId()),
				String.valueOf(role2.getRoleId())
			},
			_menuAccessConfigurationProvider.getRolesCanSeeControlMenu(
				_group.getGroupId()));

		_roleLocalService.deleteRole(role1);

		Assert.assertArrayEquals(
			new String[] {String.valueOf(role2.getRoleId())},
			_menuAccessConfigurationProvider.getRolesCanSeeControlMenu(
				_group.getGroupId()));
	}

	@Inject
	private ConfigurationProvider _configurationProvider;

	@DeleteAfterTestRun
	private Group _group;

	@Inject
	private MenuAccessConfigurationProvider _menuAccessConfigurationProvider;

	@Inject
	private RoleLocalService _roleLocalService;

}