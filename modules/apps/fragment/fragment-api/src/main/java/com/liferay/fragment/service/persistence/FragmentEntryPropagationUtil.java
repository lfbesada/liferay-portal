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

package com.liferay.fragment.service.persistence;

import com.liferay.fragment.model.FragmentEntryPropagation;
import com.liferay.portal.kernel.dao.orm.DynamicQuery;
import com.liferay.portal.kernel.service.ServiceContext;
import com.liferay.portal.kernel.util.OrderByComparator;

import java.io.Serializable;

import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * The persistence utility for the fragment entry propagation service. This utility wraps <code>com.liferay.fragment.service.persistence.impl.FragmentEntryPropagationPersistenceImpl</code> and provides direct access to the database for CRUD operations. This utility should only be used by the service layer, as it must operate within a transaction. Never access this utility in a JSP, controller, model, or other front-end class.
 *
 * <p>
 * Caching information and settings can be found in <code>portal.properties</code>
 * </p>
 *
 * @author Brian Wing Shun Chan
 * @see FragmentEntryPropagationPersistence
 * @generated
 */
public class FragmentEntryPropagationUtil {

	/*
	 * NOTE FOR DEVELOPERS:
	 *
	 * Never modify this class directly. Modify <code>service.xml</code> and rerun ServiceBuilder to regenerate this class.
	 */

	/**
	 * @see com.liferay.portal.kernel.service.persistence.BasePersistence#clearCache()
	 */
	public static void clearCache() {
		getPersistence().clearCache();
	}

	/**
	 * @see com.liferay.portal.kernel.service.persistence.BasePersistence#clearCache(com.liferay.portal.kernel.model.BaseModel)
	 */
	public static void clearCache(
		FragmentEntryPropagation fragmentEntryPropagation) {

		getPersistence().clearCache(fragmentEntryPropagation);
	}

	/**
	 * @see com.liferay.portal.kernel.service.persistence.BasePersistence#countWithDynamicQuery(DynamicQuery)
	 */
	public static long countWithDynamicQuery(DynamicQuery dynamicQuery) {
		return getPersistence().countWithDynamicQuery(dynamicQuery);
	}

	/**
	 * @see com.liferay.portal.kernel.service.persistence.BasePersistence#fetchByPrimaryKeys(Set)
	 */
	public static Map<Serializable, FragmentEntryPropagation>
		fetchByPrimaryKeys(Set<Serializable> primaryKeys) {

		return getPersistence().fetchByPrimaryKeys(primaryKeys);
	}

	/**
	 * @see com.liferay.portal.kernel.service.persistence.BasePersistence#findWithDynamicQuery(DynamicQuery)
	 */
	public static List<FragmentEntryPropagation> findWithDynamicQuery(
		DynamicQuery dynamicQuery) {

		return getPersistence().findWithDynamicQuery(dynamicQuery);
	}

	/**
	 * @see com.liferay.portal.kernel.service.persistence.BasePersistence#findWithDynamicQuery(DynamicQuery, int, int)
	 */
	public static List<FragmentEntryPropagation> findWithDynamicQuery(
		DynamicQuery dynamicQuery, int start, int end) {

		return getPersistence().findWithDynamicQuery(dynamicQuery, start, end);
	}

	/**
	 * @see com.liferay.portal.kernel.service.persistence.BasePersistence#findWithDynamicQuery(DynamicQuery, int, int, OrderByComparator)
	 */
	public static List<FragmentEntryPropagation> findWithDynamicQuery(
		DynamicQuery dynamicQuery, int start, int end,
		OrderByComparator<FragmentEntryPropagation> orderByComparator) {

		return getPersistence().findWithDynamicQuery(
			dynamicQuery, start, end, orderByComparator);
	}

	/**
	 * @see com.liferay.portal.kernel.service.persistence.BasePersistence#update(com.liferay.portal.kernel.model.BaseModel)
	 */
	public static FragmentEntryPropagation update(
		FragmentEntryPropagation fragmentEntryPropagation) {

		return getPersistence().update(fragmentEntryPropagation);
	}

	/**
	 * @see com.liferay.portal.kernel.service.persistence.BasePersistence#update(com.liferay.portal.kernel.model.BaseModel, ServiceContext)
	 */
	public static FragmentEntryPropagation update(
		FragmentEntryPropagation fragmentEntryPropagation,
		ServiceContext serviceContext) {

		return getPersistence().update(
			fragmentEntryPropagation, serviceContext);
	}

	/**
	 * Returns the fragment entry propagation where fragmentEntryKey = &#63; or throws a <code>NoSuchEntryPropagationException</code> if it could not be found.
	 *
	 * @param fragmentEntryKey the fragment entry key
	 * @return the matching fragment entry propagation
	 * @throws NoSuchEntryPropagationException if a matching fragment entry propagation could not be found
	 */
	public static FragmentEntryPropagation findByFragmentEntryKey(
			String fragmentEntryKey)
		throws com.liferay.fragment.exception.NoSuchEntryPropagationException {

		return getPersistence().findByFragmentEntryKey(fragmentEntryKey);
	}

	/**
	 * Returns the fragment entry propagation where fragmentEntryKey = &#63; or returns <code>null</code> if it could not be found. Uses the finder cache.
	 *
	 * @param fragmentEntryKey the fragment entry key
	 * @return the matching fragment entry propagation, or <code>null</code> if a matching fragment entry propagation could not be found
	 */
	public static FragmentEntryPropagation fetchByFragmentEntryKey(
		String fragmentEntryKey) {

		return getPersistence().fetchByFragmentEntryKey(fragmentEntryKey);
	}

	/**
	 * Returns the fragment entry propagation where fragmentEntryKey = &#63; or returns <code>null</code> if it could not be found, optionally using the finder cache.
	 *
	 * @param fragmentEntryKey the fragment entry key
	 * @param useFinderCache whether to use the finder cache
	 * @return the matching fragment entry propagation, or <code>null</code> if a matching fragment entry propagation could not be found
	 */
	public static FragmentEntryPropagation fetchByFragmentEntryKey(
		String fragmentEntryKey, boolean useFinderCache) {

		return getPersistence().fetchByFragmentEntryKey(
			fragmentEntryKey, useFinderCache);
	}

	/**
	 * Removes the fragment entry propagation where fragmentEntryKey = &#63; from the database.
	 *
	 * @param fragmentEntryKey the fragment entry key
	 * @return the fragment entry propagation that was removed
	 */
	public static FragmentEntryPropagation removeByFragmentEntryKey(
			String fragmentEntryKey)
		throws com.liferay.fragment.exception.NoSuchEntryPropagationException {

		return getPersistence().removeByFragmentEntryKey(fragmentEntryKey);
	}

	/**
	 * Returns the number of fragment entry propagations where fragmentEntryKey = &#63;.
	 *
	 * @param fragmentEntryKey the fragment entry key
	 * @return the number of matching fragment entry propagations
	 */
	public static int countByFragmentEntryKey(String fragmentEntryKey) {
		return getPersistence().countByFragmentEntryKey(fragmentEntryKey);
	}

	/**
	 * Caches the fragment entry propagation in the entity cache if it is enabled.
	 *
	 * @param fragmentEntryPropagation the fragment entry propagation
	 */
	public static void cacheResult(
		FragmentEntryPropagation fragmentEntryPropagation) {

		getPersistence().cacheResult(fragmentEntryPropagation);
	}

	/**
	 * Caches the fragment entry propagations in the entity cache if it is enabled.
	 *
	 * @param fragmentEntryPropagations the fragment entry propagations
	 */
	public static void cacheResult(
		List<FragmentEntryPropagation> fragmentEntryPropagations) {

		getPersistence().cacheResult(fragmentEntryPropagations);
	}

	/**
	 * Creates a new fragment entry propagation with the primary key. Does not add the fragment entry propagation to the database.
	 *
	 * @param fragmentEntryPropagationId the primary key for the new fragment entry propagation
	 * @return the new fragment entry propagation
	 */
	public static FragmentEntryPropagation create(
		long fragmentEntryPropagationId) {

		return getPersistence().create(fragmentEntryPropagationId);
	}

	/**
	 * Removes the fragment entry propagation with the primary key from the database. Also notifies the appropriate model listeners.
	 *
	 * @param fragmentEntryPropagationId the primary key of the fragment entry propagation
	 * @return the fragment entry propagation that was removed
	 * @throws NoSuchEntryPropagationException if a fragment entry propagation with the primary key could not be found
	 */
	public static FragmentEntryPropagation remove(
			long fragmentEntryPropagationId)
		throws com.liferay.fragment.exception.NoSuchEntryPropagationException {

		return getPersistence().remove(fragmentEntryPropagationId);
	}

	public static FragmentEntryPropagation updateImpl(
		FragmentEntryPropagation fragmentEntryPropagation) {

		return getPersistence().updateImpl(fragmentEntryPropagation);
	}

	/**
	 * Returns the fragment entry propagation with the primary key or throws a <code>NoSuchEntryPropagationException</code> if it could not be found.
	 *
	 * @param fragmentEntryPropagationId the primary key of the fragment entry propagation
	 * @return the fragment entry propagation
	 * @throws NoSuchEntryPropagationException if a fragment entry propagation with the primary key could not be found
	 */
	public static FragmentEntryPropagation findByPrimaryKey(
			long fragmentEntryPropagationId)
		throws com.liferay.fragment.exception.NoSuchEntryPropagationException {

		return getPersistence().findByPrimaryKey(fragmentEntryPropagationId);
	}

	/**
	 * Returns the fragment entry propagation with the primary key or returns <code>null</code> if it could not be found.
	 *
	 * @param fragmentEntryPropagationId the primary key of the fragment entry propagation
	 * @return the fragment entry propagation, or <code>null</code> if a fragment entry propagation with the primary key could not be found
	 */
	public static FragmentEntryPropagation fetchByPrimaryKey(
		long fragmentEntryPropagationId) {

		return getPersistence().fetchByPrimaryKey(fragmentEntryPropagationId);
	}

	/**
	 * Returns all the fragment entry propagations.
	 *
	 * @return the fragment entry propagations
	 */
	public static List<FragmentEntryPropagation> findAll() {
		return getPersistence().findAll();
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
	public static List<FragmentEntryPropagation> findAll(int start, int end) {
		return getPersistence().findAll(start, end);
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
	public static List<FragmentEntryPropagation> findAll(
		int start, int end,
		OrderByComparator<FragmentEntryPropagation> orderByComparator) {

		return getPersistence().findAll(start, end, orderByComparator);
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
	public static List<FragmentEntryPropagation> findAll(
		int start, int end,
		OrderByComparator<FragmentEntryPropagation> orderByComparator,
		boolean useFinderCache) {

		return getPersistence().findAll(
			start, end, orderByComparator, useFinderCache);
	}

	/**
	 * Removes all the fragment entry propagations from the database.
	 */
	public static void removeAll() {
		getPersistence().removeAll();
	}

	/**
	 * Returns the number of fragment entry propagations.
	 *
	 * @return the number of fragment entry propagations
	 */
	public static int countAll() {
		return getPersistence().countAll();
	}

	public static FragmentEntryPropagationPersistence getPersistence() {
		return _persistence;
	}

	private static volatile FragmentEntryPropagationPersistence _persistence;

}