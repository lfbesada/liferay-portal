/**
 * SPDX-FileCopyrightText: (c) 2026 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.layout.page.template.verify.test;

import com.liferay.arquillian.extension.junit.bridge.junit.Arquillian;
import com.liferay.dynamic.data.mapping.model.DDMStructure;
import com.liferay.dynamic.data.mapping.test.util.DDMStructureTestUtil;
import com.liferay.journal.model.JournalArticle;
import com.liferay.layout.page.template.model.LayoutPageTemplateEntry;
import com.liferay.layout.page.template.service.LayoutPageTemplateEntryLocalService;
import com.liferay.layout.page.template.test.util.DisplayPageTemplateTestUtil;
import com.liferay.portal.kernel.dao.jdbc.DataAccess;
import com.liferay.portal.kernel.dao.orm.EntityCache;
import com.liferay.portal.kernel.model.Group;
import com.liferay.portal.kernel.test.rule.AggregateTestRule;
import com.liferay.portal.kernel.test.rule.DeleteAfterTestRun;
import com.liferay.portal.kernel.test.util.GroupTestUtil;
import com.liferay.portal.kernel.util.Portal;
import com.liferay.portal.kernel.util.Validator;
import com.liferay.portal.kernel.workflow.WorkflowConstants;
import com.liferay.portal.test.rule.Inject;
import com.liferay.portal.test.rule.LiferayIntegrationTestRule;
import com.liferay.portal.verify.VerifyProcess;
import com.liferay.portal.verify.test.util.BaseVerifyProcessTestCase;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.ClassRule;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

/**
 * @author Mikel Lorza
 */
@RunWith(Arquillian.class)
public class LayoutPageTemplateEntryVerifyProcessTest
	extends BaseVerifyProcessTestCase {

	@ClassRule
	@Rule
	public static final AggregateTestRule aggregateTestRule =
		new LiferayIntegrationTestRule();

	@BeforeClass
	public static void setUpClass() throws Exception {
		_connection = DataAccess.getConnection();
	}

	@AfterClass
	public static void tearDownClass() throws Exception {
		DataAccess.cleanUp(_connection);
	}

	@Before
	@Override
	public void setUp() throws Exception {
		super.setUp();

		_group = GroupTestUtil.addGroup();
	}

	@Test
	public void testUpdateClassTypeKey() throws Exception {
		DDMStructure ddmStructure = DDMStructureTestUtil.addStructure(
			_group.getGroupId(), JournalArticle.class.getName());

		LayoutPageTemplateEntry layoutPageTemplateEntry =
			DisplayPageTemplateTestUtil.addDisplayPageTemplate(
				_group.getGroupId(),
				_portal.getClassNameId(JournalArticle.class.getName()), null,
				true, WorkflowConstants.STATUS_APPROVED);

		_updateLayoutPageTemplateEntry(
			ddmStructure.getStructureId(),
			layoutPageTemplateEntry.getLayoutPageTemplateEntryId());

		Assert.assertEquals(
			ddmStructure.getStructureId(),
			_getClassTypeId(
				layoutPageTemplateEntry.getLayoutPageTemplateEntryId()));
		Assert.assertTrue(
			Validator.isNull(
				_getClassTypeKey(
					layoutPageTemplateEntry.getLayoutPageTemplateEntryId())));

		doVerify();

		Assert.assertEquals(
			ddmStructure.getStructureId(),
			_getClassTypeId(
				layoutPageTemplateEntry.getLayoutPageTemplateEntryId()));
		Assert.assertEquals(
			ddmStructure.getStructureKey(),
			_getClassTypeKey(
				layoutPageTemplateEntry.getLayoutPageTemplateEntryId()));
	}

	@Override
	protected VerifyProcess getVerifyProcess() {
		return _verifyProcess;
	}

	private long _getClassTypeId(long layoutPageTemplateEntryId)
		throws Exception {

		try (PreparedStatement preparedStatement = _connection.prepareStatement(
				"select classTypeId from LayoutPageTemplateEntry where " +
					"layoutPageTemplateEntryId = ?")) {

			preparedStatement.setLong(1, layoutPageTemplateEntryId);

			try (ResultSet resultSet = preparedStatement.executeQuery()) {
				if (resultSet.next()) {
					return resultSet.getLong("classTypeId");
				}
			}
		}

		return 0;
	}

	private String _getClassTypeKey(long layoutPageTemplateEntryId)
		throws Exception {

		try (PreparedStatement preparedStatement = _connection.prepareStatement(
				"select classTypeKey from LayoutPageTemplateEntry where " +
					"layoutPageTemplateEntryId = ?")) {

			preparedStatement.setLong(1, layoutPageTemplateEntryId);

			try (ResultSet resultSet = preparedStatement.executeQuery()) {
				if (resultSet.next()) {
					return resultSet.getString("classTypeKey");
				}
			}
		}

		return null;
	}

	private void _updateLayoutPageTemplateEntry(
			long classTypeId, long layoutPageTemplateEntryId)
		throws Exception {

		try (PreparedStatement preparedStatement = _connection.prepareStatement(
				"update LayoutPageTemplateEntry set classTypeId = ?, " +
					"classTypeKey = ? where layoutPageTemplateEntryId = ?")) {

			preparedStatement.setLong(1, classTypeId);
			preparedStatement.setString(2, null);
			preparedStatement.setLong(3, layoutPageTemplateEntryId);

			preparedStatement.executeUpdate();
		}
	}

	private static Connection _connection;

	@Inject
	private EntityCache _entityCache;

	@DeleteAfterTestRun
	private Group _group;

	@Inject
	private LayoutPageTemplateEntryLocalService
		_layoutPageTemplateEntryLocalService;

	@Inject
	private Portal _portal;

	@Inject(
		filter = "component.name=com.liferay.layout.page.template.internal.verify.LayoutPageTemplateEntryVerifyProcess"
	)
	private VerifyProcess _verifyProcess;

}