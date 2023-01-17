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

package com.liferay.fragment.service.persistence.test;

import com.liferay.arquillian.extension.junit.bridge.junit.Arquillian;
import com.liferay.fragment.exception.NoSuchEntryPropagationException;
import com.liferay.fragment.model.FragmentEntryPropagation;
import com.liferay.fragment.service.FragmentEntryPropagationLocalServiceUtil;
import com.liferay.fragment.service.persistence.FragmentEntryPropagationPersistence;
import com.liferay.fragment.service.persistence.FragmentEntryPropagationUtil;
import com.liferay.portal.kernel.dao.orm.ActionableDynamicQuery;
import com.liferay.portal.kernel.dao.orm.DynamicQuery;
import com.liferay.portal.kernel.dao.orm.DynamicQueryFactoryUtil;
import com.liferay.portal.kernel.dao.orm.ProjectionFactoryUtil;
import com.liferay.portal.kernel.dao.orm.QueryUtil;
import com.liferay.portal.kernel.dao.orm.RestrictionsFactoryUtil;
import com.liferay.portal.kernel.dao.orm.Session;
import com.liferay.portal.kernel.test.ReflectionTestUtil;
import com.liferay.portal.kernel.test.rule.AggregateTestRule;
import com.liferay.portal.kernel.test.util.RandomTestUtil;
import com.liferay.portal.kernel.transaction.Propagation;
import com.liferay.portal.kernel.util.IntegerWrapper;
import com.liferay.portal.kernel.util.OrderByComparator;
import com.liferay.portal.kernel.util.OrderByComparatorFactoryUtil;
import com.liferay.portal.kernel.util.Time;
import com.liferay.portal.test.rule.LiferayIntegrationTestRule;
import com.liferay.portal.test.rule.PersistenceTestRule;
import com.liferay.portal.test.rule.TransactionalTestRule;

import java.io.Serializable;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.ClassRule;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

/**
 * @generated
 */
@RunWith(Arquillian.class)
public class FragmentEntryPropagationPersistenceTest {

	@ClassRule
	@Rule
	public static final AggregateTestRule aggregateTestRule =
		new AggregateTestRule(
			new LiferayIntegrationTestRule(), PersistenceTestRule.INSTANCE,
			new TransactionalTestRule(
				Propagation.REQUIRED, "com.liferay.fragment.service"));

	@Before
	public void setUp() {
		_persistence = FragmentEntryPropagationUtil.getPersistence();

		Class<?> clazz = _persistence.getClass();

		_dynamicQueryClassLoader = clazz.getClassLoader();
	}

	@After
	public void tearDown() throws Exception {
		Iterator<FragmentEntryPropagation> iterator =
			_fragmentEntryPropagations.iterator();

		while (iterator.hasNext()) {
			_persistence.remove(iterator.next());

			iterator.remove();
		}
	}

	@Test
	public void testCreate() throws Exception {
		long pk = RandomTestUtil.nextLong();

		FragmentEntryPropagation fragmentEntryPropagation = _persistence.create(
			pk);

		Assert.assertNotNull(fragmentEntryPropagation);

		Assert.assertEquals(fragmentEntryPropagation.getPrimaryKey(), pk);
	}

	@Test
	public void testRemove() throws Exception {
		FragmentEntryPropagation newFragmentEntryPropagation =
			addFragmentEntryPropagation();

		_persistence.remove(newFragmentEntryPropagation);

		FragmentEntryPropagation existingFragmentEntryPropagation =
			_persistence.fetchByPrimaryKey(
				newFragmentEntryPropagation.getPrimaryKey());

		Assert.assertNull(existingFragmentEntryPropagation);
	}

	@Test
	public void testUpdateNew() throws Exception {
		addFragmentEntryPropagation();
	}

	@Test
	public void testUpdateExisting() throws Exception {
		long pk = RandomTestUtil.nextLong();

		FragmentEntryPropagation newFragmentEntryPropagation =
			_persistence.create(pk);

		newFragmentEntryPropagation.setMvccVersion(RandomTestUtil.nextLong());

		newFragmentEntryPropagation.setCtCollectionId(
			RandomTestUtil.nextLong());

		newFragmentEntryPropagation.setCreateDate(RandomTestUtil.nextDate());

		newFragmentEntryPropagation.setModifiedDate(RandomTestUtil.nextDate());

		newFragmentEntryPropagation.setFragmentEntryKey(
			RandomTestUtil.randomString());

		newFragmentEntryPropagation.setCss(RandomTestUtil.randomString());

		newFragmentEntryPropagation.setHtml(RandomTestUtil.randomString());

		newFragmentEntryPropagation.setJs(RandomTestUtil.randomString());

		newFragmentEntryPropagation.setConfiguration(
			RandomTestUtil.randomString());

		newFragmentEntryPropagation.setType(RandomTestUtil.nextInt());

		_fragmentEntryPropagations.add(
			_persistence.update(newFragmentEntryPropagation));

		FragmentEntryPropagation existingFragmentEntryPropagation =
			_persistence.findByPrimaryKey(
				newFragmentEntryPropagation.getPrimaryKey());

		Assert.assertEquals(
			existingFragmentEntryPropagation.getMvccVersion(),
			newFragmentEntryPropagation.getMvccVersion());
		Assert.assertEquals(
			existingFragmentEntryPropagation.getCtCollectionId(),
			newFragmentEntryPropagation.getCtCollectionId());
		Assert.assertEquals(
			existingFragmentEntryPropagation.getFragmentEntryPropagationId(),
			newFragmentEntryPropagation.getFragmentEntryPropagationId());
		Assert.assertEquals(
			Time.getShortTimestamp(
				existingFragmentEntryPropagation.getCreateDate()),
			Time.getShortTimestamp(
				newFragmentEntryPropagation.getCreateDate()));
		Assert.assertEquals(
			Time.getShortTimestamp(
				existingFragmentEntryPropagation.getModifiedDate()),
			Time.getShortTimestamp(
				newFragmentEntryPropagation.getModifiedDate()));
		Assert.assertEquals(
			existingFragmentEntryPropagation.getFragmentEntryKey(),
			newFragmentEntryPropagation.getFragmentEntryKey());
		Assert.assertEquals(
			existingFragmentEntryPropagation.getCss(),
			newFragmentEntryPropagation.getCss());
		Assert.assertEquals(
			existingFragmentEntryPropagation.getHtml(),
			newFragmentEntryPropagation.getHtml());
		Assert.assertEquals(
			existingFragmentEntryPropagation.getJs(),
			newFragmentEntryPropagation.getJs());
		Assert.assertEquals(
			existingFragmentEntryPropagation.getConfiguration(),
			newFragmentEntryPropagation.getConfiguration());
		Assert.assertEquals(
			existingFragmentEntryPropagation.getType(),
			newFragmentEntryPropagation.getType());
	}

	@Test
	public void testCountByFragmentEntryKey() throws Exception {
		_persistence.countByFragmentEntryKey("");

		_persistence.countByFragmentEntryKey("null");

		_persistence.countByFragmentEntryKey((String)null);
	}

	@Test
	public void testFindByPrimaryKeyExisting() throws Exception {
		FragmentEntryPropagation newFragmentEntryPropagation =
			addFragmentEntryPropagation();

		FragmentEntryPropagation existingFragmentEntryPropagation =
			_persistence.findByPrimaryKey(
				newFragmentEntryPropagation.getPrimaryKey());

		Assert.assertEquals(
			existingFragmentEntryPropagation, newFragmentEntryPropagation);
	}

	@Test(expected = NoSuchEntryPropagationException.class)
	public void testFindByPrimaryKeyMissing() throws Exception {
		long pk = RandomTestUtil.nextLong();

		_persistence.findByPrimaryKey(pk);
	}

	@Test
	public void testFindAll() throws Exception {
		_persistence.findAll(
			QueryUtil.ALL_POS, QueryUtil.ALL_POS, getOrderByComparator());
	}

	protected OrderByComparator<FragmentEntryPropagation>
		getOrderByComparator() {

		return OrderByComparatorFactoryUtil.create(
			"FragmentEntryPropagation", "mvccVersion", true, "ctCollectionId",
			true, "fragmentEntryPropagationId", true, "createDate", true,
			"modifiedDate", true, "fragmentEntryKey", true, "css", true, "html",
			true, "js", true, "configuration", true, "type", true);
	}

	@Test
	public void testFetchByPrimaryKeyExisting() throws Exception {
		FragmentEntryPropagation newFragmentEntryPropagation =
			addFragmentEntryPropagation();

		FragmentEntryPropagation existingFragmentEntryPropagation =
			_persistence.fetchByPrimaryKey(
				newFragmentEntryPropagation.getPrimaryKey());

		Assert.assertEquals(
			existingFragmentEntryPropagation, newFragmentEntryPropagation);
	}

	@Test
	public void testFetchByPrimaryKeyMissing() throws Exception {
		long pk = RandomTestUtil.nextLong();

		FragmentEntryPropagation missingFragmentEntryPropagation =
			_persistence.fetchByPrimaryKey(pk);

		Assert.assertNull(missingFragmentEntryPropagation);
	}

	@Test
	public void testFetchByPrimaryKeysWithMultiplePrimaryKeysWhereAllPrimaryKeysExist()
		throws Exception {

		FragmentEntryPropagation newFragmentEntryPropagation1 =
			addFragmentEntryPropagation();
		FragmentEntryPropagation newFragmentEntryPropagation2 =
			addFragmentEntryPropagation();

		Set<Serializable> primaryKeys = new HashSet<Serializable>();

		primaryKeys.add(newFragmentEntryPropagation1.getPrimaryKey());
		primaryKeys.add(newFragmentEntryPropagation2.getPrimaryKey());

		Map<Serializable, FragmentEntryPropagation> fragmentEntryPropagations =
			_persistence.fetchByPrimaryKeys(primaryKeys);

		Assert.assertEquals(2, fragmentEntryPropagations.size());
		Assert.assertEquals(
			newFragmentEntryPropagation1,
			fragmentEntryPropagations.get(
				newFragmentEntryPropagation1.getPrimaryKey()));
		Assert.assertEquals(
			newFragmentEntryPropagation2,
			fragmentEntryPropagations.get(
				newFragmentEntryPropagation2.getPrimaryKey()));
	}

	@Test
	public void testFetchByPrimaryKeysWithMultiplePrimaryKeysWhereNoPrimaryKeysExist()
		throws Exception {

		long pk1 = RandomTestUtil.nextLong();

		long pk2 = RandomTestUtil.nextLong();

		Set<Serializable> primaryKeys = new HashSet<Serializable>();

		primaryKeys.add(pk1);
		primaryKeys.add(pk2);

		Map<Serializable, FragmentEntryPropagation> fragmentEntryPropagations =
			_persistence.fetchByPrimaryKeys(primaryKeys);

		Assert.assertTrue(fragmentEntryPropagations.isEmpty());
	}

	@Test
	public void testFetchByPrimaryKeysWithMultiplePrimaryKeysWhereSomePrimaryKeysExist()
		throws Exception {

		FragmentEntryPropagation newFragmentEntryPropagation =
			addFragmentEntryPropagation();

		long pk = RandomTestUtil.nextLong();

		Set<Serializable> primaryKeys = new HashSet<Serializable>();

		primaryKeys.add(newFragmentEntryPropagation.getPrimaryKey());
		primaryKeys.add(pk);

		Map<Serializable, FragmentEntryPropagation> fragmentEntryPropagations =
			_persistence.fetchByPrimaryKeys(primaryKeys);

		Assert.assertEquals(1, fragmentEntryPropagations.size());
		Assert.assertEquals(
			newFragmentEntryPropagation,
			fragmentEntryPropagations.get(
				newFragmentEntryPropagation.getPrimaryKey()));
	}

	@Test
	public void testFetchByPrimaryKeysWithNoPrimaryKeys() throws Exception {
		Set<Serializable> primaryKeys = new HashSet<Serializable>();

		Map<Serializable, FragmentEntryPropagation> fragmentEntryPropagations =
			_persistence.fetchByPrimaryKeys(primaryKeys);

		Assert.assertTrue(fragmentEntryPropagations.isEmpty());
	}

	@Test
	public void testFetchByPrimaryKeysWithOnePrimaryKey() throws Exception {
		FragmentEntryPropagation newFragmentEntryPropagation =
			addFragmentEntryPropagation();

		Set<Serializable> primaryKeys = new HashSet<Serializable>();

		primaryKeys.add(newFragmentEntryPropagation.getPrimaryKey());

		Map<Serializable, FragmentEntryPropagation> fragmentEntryPropagations =
			_persistence.fetchByPrimaryKeys(primaryKeys);

		Assert.assertEquals(1, fragmentEntryPropagations.size());
		Assert.assertEquals(
			newFragmentEntryPropagation,
			fragmentEntryPropagations.get(
				newFragmentEntryPropagation.getPrimaryKey()));
	}

	@Test
	public void testActionableDynamicQuery() throws Exception {
		final IntegerWrapper count = new IntegerWrapper();

		ActionableDynamicQuery actionableDynamicQuery =
			FragmentEntryPropagationLocalServiceUtil.
				getActionableDynamicQuery();

		actionableDynamicQuery.setPerformActionMethod(
			new ActionableDynamicQuery.PerformActionMethod
				<FragmentEntryPropagation>() {

				@Override
				public void performAction(
					FragmentEntryPropagation fragmentEntryPropagation) {

					Assert.assertNotNull(fragmentEntryPropagation);

					count.increment();
				}

			});

		actionableDynamicQuery.performActions();

		Assert.assertEquals(count.getValue(), _persistence.countAll());
	}

	@Test
	public void testDynamicQueryByPrimaryKeyExisting() throws Exception {
		FragmentEntryPropagation newFragmentEntryPropagation =
			addFragmentEntryPropagation();

		DynamicQuery dynamicQuery = DynamicQueryFactoryUtil.forClass(
			FragmentEntryPropagation.class, _dynamicQueryClassLoader);

		dynamicQuery.add(
			RestrictionsFactoryUtil.eq(
				"fragmentEntryPropagationId",
				newFragmentEntryPropagation.getFragmentEntryPropagationId()));

		List<FragmentEntryPropagation> result =
			_persistence.findWithDynamicQuery(dynamicQuery);

		Assert.assertEquals(1, result.size());

		FragmentEntryPropagation existingFragmentEntryPropagation = result.get(
			0);

		Assert.assertEquals(
			existingFragmentEntryPropagation, newFragmentEntryPropagation);
	}

	@Test
	public void testDynamicQueryByPrimaryKeyMissing() throws Exception {
		DynamicQuery dynamicQuery = DynamicQueryFactoryUtil.forClass(
			FragmentEntryPropagation.class, _dynamicQueryClassLoader);

		dynamicQuery.add(
			RestrictionsFactoryUtil.eq(
				"fragmentEntryPropagationId", RandomTestUtil.nextLong()));

		List<FragmentEntryPropagation> result =
			_persistence.findWithDynamicQuery(dynamicQuery);

		Assert.assertEquals(0, result.size());
	}

	@Test
	public void testDynamicQueryByProjectionExisting() throws Exception {
		FragmentEntryPropagation newFragmentEntryPropagation =
			addFragmentEntryPropagation();

		DynamicQuery dynamicQuery = DynamicQueryFactoryUtil.forClass(
			FragmentEntryPropagation.class, _dynamicQueryClassLoader);

		dynamicQuery.setProjection(
			ProjectionFactoryUtil.property("fragmentEntryPropagationId"));

		Object newFragmentEntryPropagationId =
			newFragmentEntryPropagation.getFragmentEntryPropagationId();

		dynamicQuery.add(
			RestrictionsFactoryUtil.in(
				"fragmentEntryPropagationId",
				new Object[] {newFragmentEntryPropagationId}));

		List<Object> result = _persistence.findWithDynamicQuery(dynamicQuery);

		Assert.assertEquals(1, result.size());

		Object existingFragmentEntryPropagationId = result.get(0);

		Assert.assertEquals(
			existingFragmentEntryPropagationId, newFragmentEntryPropagationId);
	}

	@Test
	public void testDynamicQueryByProjectionMissing() throws Exception {
		DynamicQuery dynamicQuery = DynamicQueryFactoryUtil.forClass(
			FragmentEntryPropagation.class, _dynamicQueryClassLoader);

		dynamicQuery.setProjection(
			ProjectionFactoryUtil.property("fragmentEntryPropagationId"));

		dynamicQuery.add(
			RestrictionsFactoryUtil.in(
				"fragmentEntryPropagationId",
				new Object[] {RandomTestUtil.nextLong()}));

		List<Object> result = _persistence.findWithDynamicQuery(dynamicQuery);

		Assert.assertEquals(0, result.size());
	}

	@Test
	public void testResetOriginalValues() throws Exception {
		FragmentEntryPropagation newFragmentEntryPropagation =
			addFragmentEntryPropagation();

		_persistence.clearCache();

		_assertOriginalValues(
			_persistence.findByPrimaryKey(
				newFragmentEntryPropagation.getPrimaryKey()));
	}

	@Test
	public void testResetOriginalValuesWithDynamicQueryLoadFromDatabase()
		throws Exception {

		_testResetOriginalValuesWithDynamicQuery(true);
	}

	@Test
	public void testResetOriginalValuesWithDynamicQueryLoadFromSession()
		throws Exception {

		_testResetOriginalValuesWithDynamicQuery(false);
	}

	private void _testResetOriginalValuesWithDynamicQuery(boolean clearSession)
		throws Exception {

		FragmentEntryPropagation newFragmentEntryPropagation =
			addFragmentEntryPropagation();

		if (clearSession) {
			Session session = _persistence.openSession();

			session.flush();

			session.clear();
		}

		DynamicQuery dynamicQuery = DynamicQueryFactoryUtil.forClass(
			FragmentEntryPropagation.class, _dynamicQueryClassLoader);

		dynamicQuery.add(
			RestrictionsFactoryUtil.eq(
				"fragmentEntryPropagationId",
				newFragmentEntryPropagation.getFragmentEntryPropagationId()));

		List<FragmentEntryPropagation> result =
			_persistence.findWithDynamicQuery(dynamicQuery);

		_assertOriginalValues(result.get(0));
	}

	private void _assertOriginalValues(
		FragmentEntryPropagation fragmentEntryPropagation) {

		Assert.assertEquals(
			fragmentEntryPropagation.getFragmentEntryKey(),
			ReflectionTestUtil.invoke(
				fragmentEntryPropagation, "getColumnOriginalValue",
				new Class<?>[] {String.class}, "fragmentEntryKey"));
	}

	protected FragmentEntryPropagation addFragmentEntryPropagation()
		throws Exception {

		long pk = RandomTestUtil.nextLong();

		FragmentEntryPropagation fragmentEntryPropagation = _persistence.create(
			pk);

		fragmentEntryPropagation.setMvccVersion(RandomTestUtil.nextLong());

		fragmentEntryPropagation.setCtCollectionId(RandomTestUtil.nextLong());

		fragmentEntryPropagation.setCreateDate(RandomTestUtil.nextDate());

		fragmentEntryPropagation.setModifiedDate(RandomTestUtil.nextDate());

		fragmentEntryPropagation.setFragmentEntryKey(
			RandomTestUtil.randomString());

		fragmentEntryPropagation.setCss(RandomTestUtil.randomString());

		fragmentEntryPropagation.setHtml(RandomTestUtil.randomString());

		fragmentEntryPropagation.setJs(RandomTestUtil.randomString());

		fragmentEntryPropagation.setConfiguration(
			RandomTestUtil.randomString());

		fragmentEntryPropagation.setType(RandomTestUtil.nextInt());

		_fragmentEntryPropagations.add(
			_persistence.update(fragmentEntryPropagation));

		return fragmentEntryPropagation;
	}

	private List<FragmentEntryPropagation> _fragmentEntryPropagations =
		new ArrayList<FragmentEntryPropagation>();
	private FragmentEntryPropagationPersistence _persistence;
	private ClassLoader _dynamicQueryClassLoader;

}