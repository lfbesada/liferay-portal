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

package com.liferay.fragment.service.persistence.impl;

import com.liferay.fragment.exception.NoSuchEntryPropagationException;
import com.liferay.fragment.model.FragmentEntryPropagation;
import com.liferay.fragment.model.FragmentEntryPropagationTable;
import com.liferay.fragment.model.impl.FragmentEntryPropagationImpl;
import com.liferay.fragment.model.impl.FragmentEntryPropagationModelImpl;
import com.liferay.fragment.service.persistence.FragmentEntryPropagationPersistence;
import com.liferay.fragment.service.persistence.FragmentEntryPropagationUtil;
import com.liferay.fragment.service.persistence.impl.constants.FragmentPersistenceConstants;
import com.liferay.petra.string.StringBundler;
import com.liferay.portal.kernel.change.tracking.CTColumnResolutionType;
import com.liferay.portal.kernel.configuration.Configuration;
import com.liferay.portal.kernel.dao.orm.EntityCache;
import com.liferay.portal.kernel.dao.orm.FinderCache;
import com.liferay.portal.kernel.dao.orm.FinderPath;
import com.liferay.portal.kernel.dao.orm.Query;
import com.liferay.portal.kernel.dao.orm.QueryPos;
import com.liferay.portal.kernel.dao.orm.QueryUtil;
import com.liferay.portal.kernel.dao.orm.Session;
import com.liferay.portal.kernel.dao.orm.SessionFactory;
import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.portal.kernel.service.ServiceContext;
import com.liferay.portal.kernel.service.ServiceContextThreadLocal;
import com.liferay.portal.kernel.service.persistence.change.tracking.helper.CTPersistenceHelper;
import com.liferay.portal.kernel.service.persistence.impl.BasePersistenceImpl;
import com.liferay.portal.kernel.util.GetterUtil;
import com.liferay.portal.kernel.util.OrderByComparator;
import com.liferay.portal.kernel.util.PropsKeys;
import com.liferay.portal.kernel.util.PropsUtil;
import com.liferay.portal.kernel.util.ProxyUtil;
import com.liferay.portal.kernel.util.SetUtil;

import java.io.Serializable;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationHandler;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.EnumMap;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

import javax.sql.DataSource;

import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Deactivate;
import org.osgi.service.component.annotations.Reference;

/**
 * The persistence implementation for the fragment entry propagation service.
 *
 * <p>
 * Caching information and settings can be found in <code>portal.properties</code>
 * </p>
 *
 * @author Brian Wing Shun Chan
 * @generated
 */
@Component(service = FragmentEntryPropagationPersistence.class)
public class FragmentEntryPropagationPersistenceImpl
	extends BasePersistenceImpl<FragmentEntryPropagation>
	implements FragmentEntryPropagationPersistence {

	/*
	 * NOTE FOR DEVELOPERS:
	 *
	 * Never modify or reference this class directly. Always use <code>FragmentEntryPropagationUtil</code> to access the fragment entry propagation persistence. Modify <code>service.xml</code> and rerun ServiceBuilder to regenerate this class.
	 */
	public static final String FINDER_CLASS_NAME_ENTITY =
		FragmentEntryPropagationImpl.class.getName();

	public static final String FINDER_CLASS_NAME_LIST_WITH_PAGINATION =
		FINDER_CLASS_NAME_ENTITY + ".List1";

	public static final String FINDER_CLASS_NAME_LIST_WITHOUT_PAGINATION =
		FINDER_CLASS_NAME_ENTITY + ".List2";

	private FinderPath _finderPathWithPaginationFindAll;
	private FinderPath _finderPathWithoutPaginationFindAll;
	private FinderPath _finderPathCountAll;
	private FinderPath _finderPathFetchByFragmentEntryKey;
	private FinderPath _finderPathCountByFragmentEntryKey;

	/**
	 * Returns the fragment entry propagation where fragmentEntryKey = &#63; or throws a <code>NoSuchEntryPropagationException</code> if it could not be found.
	 *
	 * @param fragmentEntryKey the fragment entry key
	 * @return the matching fragment entry propagation
	 * @throws NoSuchEntryPropagationException if a matching fragment entry propagation could not be found
	 */
	@Override
	public FragmentEntryPropagation findByFragmentEntryKey(
			String fragmentEntryKey)
		throws NoSuchEntryPropagationException {

		FragmentEntryPropagation fragmentEntryPropagation =
			fetchByFragmentEntryKey(fragmentEntryKey);

		if (fragmentEntryPropagation == null) {
			StringBundler sb = new StringBundler(4);

			sb.append(_NO_SUCH_ENTITY_WITH_KEY);

			sb.append("fragmentEntryKey=");
			sb.append(fragmentEntryKey);

			sb.append("}");

			if (_log.isDebugEnabled()) {
				_log.debug(sb.toString());
			}

			throw new NoSuchEntryPropagationException(sb.toString());
		}

		return fragmentEntryPropagation;
	}

	/**
	 * Returns the fragment entry propagation where fragmentEntryKey = &#63; or returns <code>null</code> if it could not be found. Uses the finder cache.
	 *
	 * @param fragmentEntryKey the fragment entry key
	 * @return the matching fragment entry propagation, or <code>null</code> if a matching fragment entry propagation could not be found
	 */
	@Override
	public FragmentEntryPropagation fetchByFragmentEntryKey(
		String fragmentEntryKey) {

		return fetchByFragmentEntryKey(fragmentEntryKey, true);
	}

	/**
	 * Returns the fragment entry propagation where fragmentEntryKey = &#63; or returns <code>null</code> if it could not be found, optionally using the finder cache.
	 *
	 * @param fragmentEntryKey the fragment entry key
	 * @param useFinderCache whether to use the finder cache
	 * @return the matching fragment entry propagation, or <code>null</code> if a matching fragment entry propagation could not be found
	 */
	@Override
	public FragmentEntryPropagation fetchByFragmentEntryKey(
		String fragmentEntryKey, boolean useFinderCache) {

		fragmentEntryKey = Objects.toString(fragmentEntryKey, "");

		boolean productionMode = ctPersistenceHelper.isProductionMode(
			FragmentEntryPropagation.class);

		Object[] finderArgs = null;

		if (useFinderCache && productionMode) {
			finderArgs = new Object[] {fragmentEntryKey};
		}

		Object result = null;

		if (useFinderCache && productionMode) {
			result = finderCache.getResult(
				_finderPathFetchByFragmentEntryKey, finderArgs, this);
		}

		if (result instanceof FragmentEntryPropagation) {
			FragmentEntryPropagation fragmentEntryPropagation =
				(FragmentEntryPropagation)result;

			if (!Objects.equals(
					fragmentEntryKey,
					fragmentEntryPropagation.getFragmentEntryKey())) {

				result = null;
			}
		}

		if (result == null) {
			StringBundler sb = new StringBundler(3);

			sb.append(_SQL_SELECT_FRAGMENTENTRYPROPAGATION_WHERE);

			boolean bindFragmentEntryKey = false;

			if (fragmentEntryKey.isEmpty()) {
				sb.append(_FINDER_COLUMN_FRAGMENTENTRYKEY_FRAGMENTENTRYKEY_3);
			}
			else {
				bindFragmentEntryKey = true;

				sb.append(_FINDER_COLUMN_FRAGMENTENTRYKEY_FRAGMENTENTRYKEY_2);
			}

			String sql = sb.toString();

			Session session = null;

			try {
				session = openSession();

				Query query = session.createQuery(sql);

				QueryPos queryPos = QueryPos.getInstance(query);

				if (bindFragmentEntryKey) {
					queryPos.add(fragmentEntryKey);
				}

				List<FragmentEntryPropagation> list = query.list();

				if (list.isEmpty()) {
					if (useFinderCache && productionMode) {
						finderCache.putResult(
							_finderPathFetchByFragmentEntryKey, finderArgs,
							list);
					}
				}
				else {
					FragmentEntryPropagation fragmentEntryPropagation =
						list.get(0);

					result = fragmentEntryPropagation;

					cacheResult(fragmentEntryPropagation);
				}
			}
			catch (Exception exception) {
				throw processException(exception);
			}
			finally {
				closeSession(session);
			}
		}

		if (result instanceof List<?>) {
			return null;
		}
		else {
			return (FragmentEntryPropagation)result;
		}
	}

	/**
	 * Removes the fragment entry propagation where fragmentEntryKey = &#63; from the database.
	 *
	 * @param fragmentEntryKey the fragment entry key
	 * @return the fragment entry propagation that was removed
	 */
	@Override
	public FragmentEntryPropagation removeByFragmentEntryKey(
			String fragmentEntryKey)
		throws NoSuchEntryPropagationException {

		FragmentEntryPropagation fragmentEntryPropagation =
			findByFragmentEntryKey(fragmentEntryKey);

		return remove(fragmentEntryPropagation);
	}

	/**
	 * Returns the number of fragment entry propagations where fragmentEntryKey = &#63;.
	 *
	 * @param fragmentEntryKey the fragment entry key
	 * @return the number of matching fragment entry propagations
	 */
	@Override
	public int countByFragmentEntryKey(String fragmentEntryKey) {
		fragmentEntryKey = Objects.toString(fragmentEntryKey, "");

		boolean productionMode = ctPersistenceHelper.isProductionMode(
			FragmentEntryPropagation.class);

		FinderPath finderPath = null;
		Object[] finderArgs = null;

		Long count = null;

		if (productionMode) {
			finderPath = _finderPathCountByFragmentEntryKey;

			finderArgs = new Object[] {fragmentEntryKey};

			count = (Long)finderCache.getResult(finderPath, finderArgs, this);
		}

		if (count == null) {
			StringBundler sb = new StringBundler(2);

			sb.append(_SQL_COUNT_FRAGMENTENTRYPROPAGATION_WHERE);

			boolean bindFragmentEntryKey = false;

			if (fragmentEntryKey.isEmpty()) {
				sb.append(_FINDER_COLUMN_FRAGMENTENTRYKEY_FRAGMENTENTRYKEY_3);
			}
			else {
				bindFragmentEntryKey = true;

				sb.append(_FINDER_COLUMN_FRAGMENTENTRYKEY_FRAGMENTENTRYKEY_2);
			}

			String sql = sb.toString();

			Session session = null;

			try {
				session = openSession();

				Query query = session.createQuery(sql);

				QueryPos queryPos = QueryPos.getInstance(query);

				if (bindFragmentEntryKey) {
					queryPos.add(fragmentEntryKey);
				}

				count = (Long)query.uniqueResult();

				if (productionMode) {
					finderCache.putResult(finderPath, finderArgs, count);
				}
			}
			catch (Exception exception) {
				throw processException(exception);
			}
			finally {
				closeSession(session);
			}
		}

		return count.intValue();
	}

	private static final String
		_FINDER_COLUMN_FRAGMENTENTRYKEY_FRAGMENTENTRYKEY_2 =
			"fragmentEntryPropagation.fragmentEntryKey = ?";

	private static final String
		_FINDER_COLUMN_FRAGMENTENTRYKEY_FRAGMENTENTRYKEY_3 =
			"(fragmentEntryPropagation.fragmentEntryKey IS NULL OR fragmentEntryPropagation.fragmentEntryKey = '')";

	public FragmentEntryPropagationPersistenceImpl() {
		Map<String, String> dbColumnNames = new HashMap<String, String>();

		dbColumnNames.put("type", "type_");

		setDBColumnNames(dbColumnNames);

		setModelClass(FragmentEntryPropagation.class);

		setModelImplClass(FragmentEntryPropagationImpl.class);
		setModelPKClass(long.class);

		setTable(FragmentEntryPropagationTable.INSTANCE);
	}

	/**
	 * Caches the fragment entry propagation in the entity cache if it is enabled.
	 *
	 * @param fragmentEntryPropagation the fragment entry propagation
	 */
	@Override
	public void cacheResult(FragmentEntryPropagation fragmentEntryPropagation) {
		if (fragmentEntryPropagation.getCtCollectionId() != 0) {
			return;
		}

		entityCache.putResult(
			FragmentEntryPropagationImpl.class,
			fragmentEntryPropagation.getPrimaryKey(), fragmentEntryPropagation);

		finderCache.putResult(
			_finderPathFetchByFragmentEntryKey,
			new Object[] {fragmentEntryPropagation.getFragmentEntryKey()},
			fragmentEntryPropagation);
	}

	private int _valueObjectFinderCacheListThreshold;

	/**
	 * Caches the fragment entry propagations in the entity cache if it is enabled.
	 *
	 * @param fragmentEntryPropagations the fragment entry propagations
	 */
	@Override
	public void cacheResult(
		List<FragmentEntryPropagation> fragmentEntryPropagations) {

		if ((_valueObjectFinderCacheListThreshold == 0) ||
			((_valueObjectFinderCacheListThreshold > 0) &&
			 (fragmentEntryPropagations.size() >
				 _valueObjectFinderCacheListThreshold))) {

			return;
		}

		for (FragmentEntryPropagation fragmentEntryPropagation :
				fragmentEntryPropagations) {

			if (fragmentEntryPropagation.getCtCollectionId() != 0) {
				continue;
			}

			if (entityCache.getResult(
					FragmentEntryPropagationImpl.class,
					fragmentEntryPropagation.getPrimaryKey()) == null) {

				cacheResult(fragmentEntryPropagation);
			}
		}
	}

	/**
	 * Clears the cache for all fragment entry propagations.
	 *
	 * <p>
	 * The <code>EntityCache</code> and <code>FinderCache</code> are both cleared by this method.
	 * </p>
	 */
	@Override
	public void clearCache() {
		entityCache.clearCache(FragmentEntryPropagationImpl.class);

		finderCache.clearCache(FragmentEntryPropagationImpl.class);
	}

	/**
	 * Clears the cache for the fragment entry propagation.
	 *
	 * <p>
	 * The <code>EntityCache</code> and <code>FinderCache</code> are both cleared by this method.
	 * </p>
	 */
	@Override
	public void clearCache(FragmentEntryPropagation fragmentEntryPropagation) {
		entityCache.removeResult(
			FragmentEntryPropagationImpl.class, fragmentEntryPropagation);
	}

	@Override
	public void clearCache(
		List<FragmentEntryPropagation> fragmentEntryPropagations) {

		for (FragmentEntryPropagation fragmentEntryPropagation :
				fragmentEntryPropagations) {

			entityCache.removeResult(
				FragmentEntryPropagationImpl.class, fragmentEntryPropagation);
		}
	}

	@Override
	public void clearCache(Set<Serializable> primaryKeys) {
		finderCache.clearCache(FragmentEntryPropagationImpl.class);

		for (Serializable primaryKey : primaryKeys) {
			entityCache.removeResult(
				FragmentEntryPropagationImpl.class, primaryKey);
		}
	}

	protected void cacheUniqueFindersCache(
		FragmentEntryPropagationModelImpl fragmentEntryPropagationModelImpl) {

		Object[] args = new Object[] {
			fragmentEntryPropagationModelImpl.getFragmentEntryKey()
		};

		finderCache.putResult(
			_finderPathCountByFragmentEntryKey, args, Long.valueOf(1));
		finderCache.putResult(
			_finderPathFetchByFragmentEntryKey, args,
			fragmentEntryPropagationModelImpl);
	}

	/**
	 * Creates a new fragment entry propagation with the primary key. Does not add the fragment entry propagation to the database.
	 *
	 * @param fragmentEntryPropagationId the primary key for the new fragment entry propagation
	 * @return the new fragment entry propagation
	 */
	@Override
	public FragmentEntryPropagation create(long fragmentEntryPropagationId) {
		FragmentEntryPropagation fragmentEntryPropagation =
			new FragmentEntryPropagationImpl();

		fragmentEntryPropagation.setNew(true);
		fragmentEntryPropagation.setPrimaryKey(fragmentEntryPropagationId);

		return fragmentEntryPropagation;
	}

	/**
	 * Removes the fragment entry propagation with the primary key from the database. Also notifies the appropriate model listeners.
	 *
	 * @param fragmentEntryPropagationId the primary key of the fragment entry propagation
	 * @return the fragment entry propagation that was removed
	 * @throws NoSuchEntryPropagationException if a fragment entry propagation with the primary key could not be found
	 */
	@Override
	public FragmentEntryPropagation remove(long fragmentEntryPropagationId)
		throws NoSuchEntryPropagationException {

		return remove((Serializable)fragmentEntryPropagationId);
	}

	/**
	 * Removes the fragment entry propagation with the primary key from the database. Also notifies the appropriate model listeners.
	 *
	 * @param primaryKey the primary key of the fragment entry propagation
	 * @return the fragment entry propagation that was removed
	 * @throws NoSuchEntryPropagationException if a fragment entry propagation with the primary key could not be found
	 */
	@Override
	public FragmentEntryPropagation remove(Serializable primaryKey)
		throws NoSuchEntryPropagationException {

		Session session = null;

		try {
			session = openSession();

			FragmentEntryPropagation fragmentEntryPropagation =
				(FragmentEntryPropagation)session.get(
					FragmentEntryPropagationImpl.class, primaryKey);

			if (fragmentEntryPropagation == null) {
				if (_log.isDebugEnabled()) {
					_log.debug(_NO_SUCH_ENTITY_WITH_PRIMARY_KEY + primaryKey);
				}

				throw new NoSuchEntryPropagationException(
					_NO_SUCH_ENTITY_WITH_PRIMARY_KEY + primaryKey);
			}

			return remove(fragmentEntryPropagation);
		}
		catch (NoSuchEntryPropagationException noSuchEntityException) {
			throw noSuchEntityException;
		}
		catch (Exception exception) {
			throw processException(exception);
		}
		finally {
			closeSession(session);
		}
	}

	@Override
	protected FragmentEntryPropagation removeImpl(
		FragmentEntryPropagation fragmentEntryPropagation) {

		Session session = null;

		try {
			session = openSession();

			if (!session.contains(fragmentEntryPropagation)) {
				fragmentEntryPropagation =
					(FragmentEntryPropagation)session.get(
						FragmentEntryPropagationImpl.class,
						fragmentEntryPropagation.getPrimaryKeyObj());
			}

			if ((fragmentEntryPropagation != null) &&
				ctPersistenceHelper.isRemove(fragmentEntryPropagation)) {

				session.delete(fragmentEntryPropagation);
			}
		}
		catch (Exception exception) {
			throw processException(exception);
		}
		finally {
			closeSession(session);
		}

		if (fragmentEntryPropagation != null) {
			clearCache(fragmentEntryPropagation);
		}

		return fragmentEntryPropagation;
	}

	@Override
	public FragmentEntryPropagation updateImpl(
		FragmentEntryPropagation fragmentEntryPropagation) {

		boolean isNew = fragmentEntryPropagation.isNew();

		if (!(fragmentEntryPropagation instanceof
				FragmentEntryPropagationModelImpl)) {

			InvocationHandler invocationHandler = null;

			if (ProxyUtil.isProxyClass(fragmentEntryPropagation.getClass())) {
				invocationHandler = ProxyUtil.getInvocationHandler(
					fragmentEntryPropagation);

				throw new IllegalArgumentException(
					"Implement ModelWrapper in fragmentEntryPropagation proxy " +
						invocationHandler.getClass());
			}

			throw new IllegalArgumentException(
				"Implement ModelWrapper in custom FragmentEntryPropagation implementation " +
					fragmentEntryPropagation.getClass());
		}

		FragmentEntryPropagationModelImpl fragmentEntryPropagationModelImpl =
			(FragmentEntryPropagationModelImpl)fragmentEntryPropagation;

		ServiceContext serviceContext =
			ServiceContextThreadLocal.getServiceContext();

		Date date = new Date();

		if (isNew && (fragmentEntryPropagation.getCreateDate() == null)) {
			if (serviceContext == null) {
				fragmentEntryPropagation.setCreateDate(date);
			}
			else {
				fragmentEntryPropagation.setCreateDate(
					serviceContext.getCreateDate(date));
			}
		}

		if (!fragmentEntryPropagationModelImpl.hasSetModifiedDate()) {
			if (serviceContext == null) {
				fragmentEntryPropagation.setModifiedDate(date);
			}
			else {
				fragmentEntryPropagation.setModifiedDate(
					serviceContext.getModifiedDate(date));
			}
		}

		Session session = null;

		try {
			session = openSession();

			if (ctPersistenceHelper.isInsert(fragmentEntryPropagation)) {
				if (!isNew) {
					session.evict(
						FragmentEntryPropagationImpl.class,
						fragmentEntryPropagation.getPrimaryKeyObj());
				}

				session.save(fragmentEntryPropagation);
			}
			else {
				fragmentEntryPropagation =
					(FragmentEntryPropagation)session.merge(
						fragmentEntryPropagation);
			}
		}
		catch (Exception exception) {
			throw processException(exception);
		}
		finally {
			closeSession(session);
		}

		if (fragmentEntryPropagation.getCtCollectionId() != 0) {
			if (isNew) {
				fragmentEntryPropagation.setNew(false);
			}

			fragmentEntryPropagation.resetOriginalValues();

			return fragmentEntryPropagation;
		}

		entityCache.putResult(
			FragmentEntryPropagationImpl.class,
			fragmentEntryPropagationModelImpl, false, true);

		cacheUniqueFindersCache(fragmentEntryPropagationModelImpl);

		if (isNew) {
			fragmentEntryPropagation.setNew(false);
		}

		fragmentEntryPropagation.resetOriginalValues();

		return fragmentEntryPropagation;
	}

	/**
	 * Returns the fragment entry propagation with the primary key or throws a <code>com.liferay.portal.kernel.exception.NoSuchModelException</code> if it could not be found.
	 *
	 * @param primaryKey the primary key of the fragment entry propagation
	 * @return the fragment entry propagation
	 * @throws NoSuchEntryPropagationException if a fragment entry propagation with the primary key could not be found
	 */
	@Override
	public FragmentEntryPropagation findByPrimaryKey(Serializable primaryKey)
		throws NoSuchEntryPropagationException {

		FragmentEntryPropagation fragmentEntryPropagation = fetchByPrimaryKey(
			primaryKey);

		if (fragmentEntryPropagation == null) {
			if (_log.isDebugEnabled()) {
				_log.debug(_NO_SUCH_ENTITY_WITH_PRIMARY_KEY + primaryKey);
			}

			throw new NoSuchEntryPropagationException(
				_NO_SUCH_ENTITY_WITH_PRIMARY_KEY + primaryKey);
		}

		return fragmentEntryPropagation;
	}

	/**
	 * Returns the fragment entry propagation with the primary key or throws a <code>NoSuchEntryPropagationException</code> if it could not be found.
	 *
	 * @param fragmentEntryPropagationId the primary key of the fragment entry propagation
	 * @return the fragment entry propagation
	 * @throws NoSuchEntryPropagationException if a fragment entry propagation with the primary key could not be found
	 */
	@Override
	public FragmentEntryPropagation findByPrimaryKey(
			long fragmentEntryPropagationId)
		throws NoSuchEntryPropagationException {

		return findByPrimaryKey((Serializable)fragmentEntryPropagationId);
	}

	/**
	 * Returns the fragment entry propagation with the primary key or returns <code>null</code> if it could not be found.
	 *
	 * @param primaryKey the primary key of the fragment entry propagation
	 * @return the fragment entry propagation, or <code>null</code> if a fragment entry propagation with the primary key could not be found
	 */
	@Override
	public FragmentEntryPropagation fetchByPrimaryKey(Serializable primaryKey) {
		if (ctPersistenceHelper.isProductionMode(
				FragmentEntryPropagation.class, primaryKey)) {

			return super.fetchByPrimaryKey(primaryKey);
		}

		FragmentEntryPropagation fragmentEntryPropagation = null;

		Session session = null;

		try {
			session = openSession();

			fragmentEntryPropagation = (FragmentEntryPropagation)session.get(
				FragmentEntryPropagationImpl.class, primaryKey);

			if (fragmentEntryPropagation != null) {
				cacheResult(fragmentEntryPropagation);
			}
		}
		catch (Exception exception) {
			throw processException(exception);
		}
		finally {
			closeSession(session);
		}

		return fragmentEntryPropagation;
	}

	/**
	 * Returns the fragment entry propagation with the primary key or returns <code>null</code> if it could not be found.
	 *
	 * @param fragmentEntryPropagationId the primary key of the fragment entry propagation
	 * @return the fragment entry propagation, or <code>null</code> if a fragment entry propagation with the primary key could not be found
	 */
	@Override
	public FragmentEntryPropagation fetchByPrimaryKey(
		long fragmentEntryPropagationId) {

		return fetchByPrimaryKey((Serializable)fragmentEntryPropagationId);
	}

	@Override
	public Map<Serializable, FragmentEntryPropagation> fetchByPrimaryKeys(
		Set<Serializable> primaryKeys) {

		if (ctPersistenceHelper.isProductionMode(
				FragmentEntryPropagation.class)) {

			return super.fetchByPrimaryKeys(primaryKeys);
		}

		if (primaryKeys.isEmpty()) {
			return Collections.emptyMap();
		}

		Map<Serializable, FragmentEntryPropagation> map =
			new HashMap<Serializable, FragmentEntryPropagation>();

		if (primaryKeys.size() == 1) {
			Iterator<Serializable> iterator = primaryKeys.iterator();

			Serializable primaryKey = iterator.next();

			FragmentEntryPropagation fragmentEntryPropagation =
				fetchByPrimaryKey(primaryKey);

			if (fragmentEntryPropagation != null) {
				map.put(primaryKey, fragmentEntryPropagation);
			}

			return map;
		}

		if ((databaseInMaxParameters > 0) &&
			(primaryKeys.size() > databaseInMaxParameters)) {

			Iterator<Serializable> iterator = primaryKeys.iterator();

			while (iterator.hasNext()) {
				Set<Serializable> page = new HashSet<>();

				for (int i = 0;
					 (i < databaseInMaxParameters) && iterator.hasNext(); i++) {

					page.add(iterator.next());
				}

				map.putAll(fetchByPrimaryKeys(page));
			}

			return map;
		}

		StringBundler sb = new StringBundler((primaryKeys.size() * 2) + 1);

		sb.append(getSelectSQL());
		sb.append(" WHERE ");
		sb.append(getPKDBName());
		sb.append(" IN (");

		for (Serializable primaryKey : primaryKeys) {
			sb.append((long)primaryKey);

			sb.append(",");
		}

		sb.setIndex(sb.index() - 1);

		sb.append(")");

		String sql = sb.toString();

		Session session = null;

		try {
			session = openSession();

			Query query = session.createQuery(sql);

			for (FragmentEntryPropagation fragmentEntryPropagation :
					(List<FragmentEntryPropagation>)query.list()) {

				map.put(
					fragmentEntryPropagation.getPrimaryKeyObj(),
					fragmentEntryPropagation);

				cacheResult(fragmentEntryPropagation);
			}
		}
		catch (Exception exception) {
			throw processException(exception);
		}
		finally {
			closeSession(session);
		}

		return map;
	}

	/**
	 * Returns all the fragment entry propagations.
	 *
	 * @return the fragment entry propagations
	 */
	@Override
	public List<FragmentEntryPropagation> findAll() {
		return findAll(QueryUtil.ALL_POS, QueryUtil.ALL_POS, null);
	}

	/**
	 * Returns a range of all the fragment entry propagations.
	 *
	 * <p>
	 * Useful when paginating results. Returns a maximum of <code>end - start</code> instances. <code>start</code> and <code>end</code> are not primary keys, they are indexes in the result set. Thus, <code>0</code> refers to the first result in the set. Setting both <code>start</code> and <code>end</code> to <code>QueryUtil#ALL_POS</code> will return the full result set. If <code>orderByComparator</code> is specified, then the query will include the given ORDER BY logic. If <code>orderByComparator</code> is absent, then the query will include the default ORDER BY logic from <code>FragmentEntryPropagationModelImpl</code>.
	 * </p>
	 *
	 * @param start the lower bound of the range of fragment entry propagations
	 * @param end the upper bound of the range of fragment entry propagations (not inclusive)
	 * @return the range of fragment entry propagations
	 */
	@Override
	public List<FragmentEntryPropagation> findAll(int start, int end) {
		return findAll(start, end, null);
	}

	/**
	 * Returns an ordered range of all the fragment entry propagations.
	 *
	 * <p>
	 * Useful when paginating results. Returns a maximum of <code>end - start</code> instances. <code>start</code> and <code>end</code> are not primary keys, they are indexes in the result set. Thus, <code>0</code> refers to the first result in the set. Setting both <code>start</code> and <code>end</code> to <code>QueryUtil#ALL_POS</code> will return the full result set. If <code>orderByComparator</code> is specified, then the query will include the given ORDER BY logic. If <code>orderByComparator</code> is absent, then the query will include the default ORDER BY logic from <code>FragmentEntryPropagationModelImpl</code>.
	 * </p>
	 *
	 * @param start the lower bound of the range of fragment entry propagations
	 * @param end the upper bound of the range of fragment entry propagations (not inclusive)
	 * @param orderByComparator the comparator to order the results by (optionally <code>null</code>)
	 * @return the ordered range of fragment entry propagations
	 */
	@Override
	public List<FragmentEntryPropagation> findAll(
		int start, int end,
		OrderByComparator<FragmentEntryPropagation> orderByComparator) {

		return findAll(start, end, orderByComparator, true);
	}

	/**
	 * Returns an ordered range of all the fragment entry propagations.
	 *
	 * <p>
	 * Useful when paginating results. Returns a maximum of <code>end - start</code> instances. <code>start</code> and <code>end</code> are not primary keys, they are indexes in the result set. Thus, <code>0</code> refers to the first result in the set. Setting both <code>start</code> and <code>end</code> to <code>QueryUtil#ALL_POS</code> will return the full result set. If <code>orderByComparator</code> is specified, then the query will include the given ORDER BY logic. If <code>orderByComparator</code> is absent, then the query will include the default ORDER BY logic from <code>FragmentEntryPropagationModelImpl</code>.
	 * </p>
	 *
	 * @param start the lower bound of the range of fragment entry propagations
	 * @param end the upper bound of the range of fragment entry propagations (not inclusive)
	 * @param orderByComparator the comparator to order the results by (optionally <code>null</code>)
	 * @param useFinderCache whether to use the finder cache
	 * @return the ordered range of fragment entry propagations
	 */
	@Override
	public List<FragmentEntryPropagation> findAll(
		int start, int end,
		OrderByComparator<FragmentEntryPropagation> orderByComparator,
		boolean useFinderCache) {

		boolean productionMode = ctPersistenceHelper.isProductionMode(
			FragmentEntryPropagation.class);

		FinderPath finderPath = null;
		Object[] finderArgs = null;

		if ((start == QueryUtil.ALL_POS) && (end == QueryUtil.ALL_POS) &&
			(orderByComparator == null)) {

			if (useFinderCache && productionMode) {
				finderPath = _finderPathWithoutPaginationFindAll;
				finderArgs = FINDER_ARGS_EMPTY;
			}
		}
		else if (useFinderCache && productionMode) {
			finderPath = _finderPathWithPaginationFindAll;
			finderArgs = new Object[] {start, end, orderByComparator};
		}

		List<FragmentEntryPropagation> list = null;

		if (useFinderCache && productionMode) {
			list = (List<FragmentEntryPropagation>)finderCache.getResult(
				finderPath, finderArgs, this);
		}

		if (list == null) {
			StringBundler sb = null;
			String sql = null;

			if (orderByComparator != null) {
				sb = new StringBundler(
					2 + (orderByComparator.getOrderByFields().length * 2));

				sb.append(_SQL_SELECT_FRAGMENTENTRYPROPAGATION);

				appendOrderByComparator(
					sb, _ORDER_BY_ENTITY_ALIAS, orderByComparator);

				sql = sb.toString();
			}
			else {
				sql = _SQL_SELECT_FRAGMENTENTRYPROPAGATION;

				sql = sql.concat(
					FragmentEntryPropagationModelImpl.ORDER_BY_JPQL);
			}

			Session session = null;

			try {
				session = openSession();

				Query query = session.createQuery(sql);

				list = (List<FragmentEntryPropagation>)QueryUtil.list(
					query, getDialect(), start, end);

				cacheResult(list);

				if (useFinderCache && productionMode) {
					finderCache.putResult(finderPath, finderArgs, list);
				}
			}
			catch (Exception exception) {
				throw processException(exception);
			}
			finally {
				closeSession(session);
			}
		}

		return list;
	}

	/**
	 * Removes all the fragment entry propagations from the database.
	 *
	 */
	@Override
	public void removeAll() {
		for (FragmentEntryPropagation fragmentEntryPropagation : findAll()) {
			remove(fragmentEntryPropagation);
		}
	}

	/**
	 * Returns the number of fragment entry propagations.
	 *
	 * @return the number of fragment entry propagations
	 */
	@Override
	public int countAll() {
		boolean productionMode = ctPersistenceHelper.isProductionMode(
			FragmentEntryPropagation.class);

		Long count = null;

		if (productionMode) {
			count = (Long)finderCache.getResult(
				_finderPathCountAll, FINDER_ARGS_EMPTY, this);
		}

		if (count == null) {
			Session session = null;

			try {
				session = openSession();

				Query query = session.createQuery(
					_SQL_COUNT_FRAGMENTENTRYPROPAGATION);

				count = (Long)query.uniqueResult();

				if (productionMode) {
					finderCache.putResult(
						_finderPathCountAll, FINDER_ARGS_EMPTY, count);
				}
			}
			catch (Exception exception) {
				throw processException(exception);
			}
			finally {
				closeSession(session);
			}
		}

		return count.intValue();
	}

	@Override
	public Set<String> getBadColumnNames() {
		return _badColumnNames;
	}

	@Override
	protected EntityCache getEntityCache() {
		return entityCache;
	}

	@Override
	protected String getPKDBName() {
		return "fragmentEntryPropagationId";
	}

	@Override
	protected String getSelectSQL() {
		return _SQL_SELECT_FRAGMENTENTRYPROPAGATION;
	}

	@Override
	public Set<String> getCTColumnNames(
		CTColumnResolutionType ctColumnResolutionType) {

		return _ctColumnNamesMap.getOrDefault(
			ctColumnResolutionType, Collections.emptySet());
	}

	@Override
	public List<String> getMappingTableNames() {
		return _mappingTableNames;
	}

	@Override
	public Map<String, Integer> getTableColumnsMap() {
		return FragmentEntryPropagationModelImpl.TABLE_COLUMNS_MAP;
	}

	@Override
	public String getTableName() {
		return "FragmentEntryPropagation";
	}

	@Override
	public List<String[]> getUniqueIndexColumnNames() {
		return _uniqueIndexColumnNames;
	}

	private static final Map<CTColumnResolutionType, Set<String>>
		_ctColumnNamesMap = new EnumMap<CTColumnResolutionType, Set<String>>(
			CTColumnResolutionType.class);
	private static final List<String> _mappingTableNames =
		new ArrayList<String>();
	private static final List<String[]> _uniqueIndexColumnNames =
		new ArrayList<String[]>();

	static {
		Set<String> ctControlColumnNames = new HashSet<String>();
		Set<String> ctIgnoreColumnNames = new HashSet<String>();
		Set<String> ctStrictColumnNames = new HashSet<String>();

		ctControlColumnNames.add("mvccVersion");
		ctControlColumnNames.add("ctCollectionId");
		ctStrictColumnNames.add("createDate");
		ctIgnoreColumnNames.add("modifiedDate");
		ctStrictColumnNames.add("fragmentEntryKey");
		ctStrictColumnNames.add("css");
		ctStrictColumnNames.add("html");
		ctStrictColumnNames.add("js");
		ctStrictColumnNames.add("configuration");
		ctStrictColumnNames.add("type_");

		_ctColumnNamesMap.put(
			CTColumnResolutionType.CONTROL, ctControlColumnNames);
		_ctColumnNamesMap.put(
			CTColumnResolutionType.IGNORE, ctIgnoreColumnNames);
		_ctColumnNamesMap.put(
			CTColumnResolutionType.PK,
			Collections.singleton("fragmentEntryPropagationId"));
		_ctColumnNamesMap.put(
			CTColumnResolutionType.STRICT, ctStrictColumnNames);

		_uniqueIndexColumnNames.add(new String[] {"fragmentEntryKey"});
	}

	/**
	 * Initializes the fragment entry propagation persistence.
	 */
	@Activate
	public void activate() {
		_valueObjectFinderCacheListThreshold = GetterUtil.getInteger(
			PropsUtil.get(PropsKeys.VALUE_OBJECT_FINDER_CACHE_LIST_THRESHOLD));

		_finderPathWithPaginationFindAll = new FinderPath(
			FINDER_CLASS_NAME_LIST_WITH_PAGINATION, "findAll", new String[0],
			new String[0], true);

		_finderPathWithoutPaginationFindAll = new FinderPath(
			FINDER_CLASS_NAME_LIST_WITHOUT_PAGINATION, "findAll", new String[0],
			new String[0], true);

		_finderPathCountAll = new FinderPath(
			FINDER_CLASS_NAME_LIST_WITHOUT_PAGINATION, "countAll",
			new String[0], new String[0], false);

		_finderPathFetchByFragmentEntryKey = new FinderPath(
			FINDER_CLASS_NAME_ENTITY, "fetchByFragmentEntryKey",
			new String[] {String.class.getName()},
			new String[] {"fragmentEntryKey"}, true);

		_finderPathCountByFragmentEntryKey = new FinderPath(
			FINDER_CLASS_NAME_LIST_WITHOUT_PAGINATION,
			"countByFragmentEntryKey", new String[] {String.class.getName()},
			new String[] {"fragmentEntryKey"}, false);

		_setFragmentEntryPropagationUtilPersistence(this);
	}

	@Deactivate
	public void deactivate() {
		_setFragmentEntryPropagationUtilPersistence(null);

		entityCache.removeCache(FragmentEntryPropagationImpl.class.getName());
	}

	private void _setFragmentEntryPropagationUtilPersistence(
		FragmentEntryPropagationPersistence
			fragmentEntryPropagationPersistence) {

		try {
			Field field = FragmentEntryPropagationUtil.class.getDeclaredField(
				"_persistence");

			field.setAccessible(true);

			field.set(null, fragmentEntryPropagationPersistence);
		}
		catch (ReflectiveOperationException reflectiveOperationException) {
			throw new RuntimeException(reflectiveOperationException);
		}
	}

	@Override
	@Reference(
		target = FragmentPersistenceConstants.SERVICE_CONFIGURATION_FILTER,
		unbind = "-"
	)
	public void setConfiguration(Configuration configuration) {
	}

	@Override
	@Reference(
		target = FragmentPersistenceConstants.ORIGIN_BUNDLE_SYMBOLIC_NAME_FILTER,
		unbind = "-"
	)
	public void setDataSource(DataSource dataSource) {
		super.setDataSource(dataSource);
	}

	@Override
	@Reference(
		target = FragmentPersistenceConstants.ORIGIN_BUNDLE_SYMBOLIC_NAME_FILTER,
		unbind = "-"
	)
	public void setSessionFactory(SessionFactory sessionFactory) {
		super.setSessionFactory(sessionFactory);
	}

	@Reference
	protected CTPersistenceHelper ctPersistenceHelper;

	@Reference
	protected EntityCache entityCache;

	@Reference
	protected FinderCache finderCache;

	private static final String _SQL_SELECT_FRAGMENTENTRYPROPAGATION =
		"SELECT fragmentEntryPropagation FROM FragmentEntryPropagation fragmentEntryPropagation";

	private static final String _SQL_SELECT_FRAGMENTENTRYPROPAGATION_WHERE =
		"SELECT fragmentEntryPropagation FROM FragmentEntryPropagation fragmentEntryPropagation WHERE ";

	private static final String _SQL_COUNT_FRAGMENTENTRYPROPAGATION =
		"SELECT COUNT(fragmentEntryPropagation) FROM FragmentEntryPropagation fragmentEntryPropagation";

	private static final String _SQL_COUNT_FRAGMENTENTRYPROPAGATION_WHERE =
		"SELECT COUNT(fragmentEntryPropagation) FROM FragmentEntryPropagation fragmentEntryPropagation WHERE ";

	private static final String _ORDER_BY_ENTITY_ALIAS =
		"fragmentEntryPropagation.";

	private static final String _NO_SUCH_ENTITY_WITH_PRIMARY_KEY =
		"No FragmentEntryPropagation exists with the primary key ";

	private static final String _NO_SUCH_ENTITY_WITH_KEY =
		"No FragmentEntryPropagation exists with the key {";

	private static final Log _log = LogFactoryUtil.getLog(
		FragmentEntryPropagationPersistenceImpl.class);

	private static final Set<String> _badColumnNames = SetUtil.fromArray(
		new String[] {"type"});

	@Override
	protected FinderCache getFinderCache() {
		return finderCache;
	}

}