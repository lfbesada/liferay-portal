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

import com.liferay.fragment.exception.NoSuchEntryPropagationException;
import com.liferay.fragment.model.FragmentEntryPropagation;
import com.liferay.portal.kernel.service.persistence.BasePersistence;
import com.liferay.portal.kernel.service.persistence.change.tracking.CTPersistence;

import org.osgi.annotation.versioning.ProviderType;

/**
 * The persistence interface for the fragment entry propagation service.
 *
 * <p>
 * Caching information and settings can be found in <code>portal.properties</code>
 * </p>
 *
 * @author Brian Wing Shun Chan
 * @see FragmentEntryPropagationUtil
 * @generated
 */
@ProviderType
public interface FragmentEntryPropagationPersistence
	extends BasePersistence<FragmentEntryPropagation>,
			CTPersistence<FragmentEntryPropagation> {

	/*
	 * NOTE FOR DEVELOPERS:
	 *
	 * Never modify or reference this interface directly. Always use {@link FragmentEntryPropagationUtil} to access the fragment entry propagation persistence. Modify <code>service.xml</code> and rerun ServiceBuilder to regenerate this interface.
	 */

	/**
	 * Returns the fragment entry propagation where fragmentEntryKey = &#63; or throws a <code>NoSuchEntryPropagationException</code> if it could not be found.
	 *
	 * @param fragmentEntryKey the fragment entry key
	 * @return the matching fragment entry propagation
	 * @throws NoSuchEntryPropagationException if a matching fragment entry propagation could not be found
	 */
	public FragmentEntryPropagation findByFragmentEntryKey(
			String fragmentEntryKey)
		throws NoSuchEntryPropagationException;

	/**
	 * Returns the fragment entry propagation where fragmentEntryKey = &#63; or returns <code>null</code> if it could not be found. Uses the finder cache.
	 *
	 * @param fragmentEntryKey the fragment entry key
	 * @return the matching fragment entry propagation, or <code>null</code> if a matching fragment entry propagation could not be found
	 */
	public FragmentEntryPropagation fetchByFragmentEntryKey(
		String fragmentEntryKey);

	/**
	 * Returns the fragment entry propagation where fragmentEntryKey = &#63; or returns <code>null</code> if it could not be found, optionally using the finder cache.
	 *
	 * @param fragmentEntryKey the fragment entry key
	 * @param useFinderCache whether to use the finder cache
	 * @return the matching fragment entry propagation, or <code>null</code> if a matching fragment entry propagation could not be found
	 */
	public FragmentEntryPropagation fetchByFragmentEntryKey(
		String fragmentEntryKey, boolean useFinderCache);

	/**
	 * Removes the fragment entry propagation where fragmentEntryKey = &#63; from the database.
	 *
	 * @param fragmentEntryKey the fragment entry key
	 * @return the fragment entry propagation that was removed
	 */
	public FragmentEntryPropagation removeByFragmentEntryKey(
			String fragmentEntryKey)
		throws NoSuchEntryPropagationException;

	/**
	 * Returns the number of fragment entry propagations where fragmentEntryKey = &#63;.
	 *
	 * @param fragmentEntryKey the fragment entry key
	 * @return the number of matching fragment entry propagations
	 */
	public int countByFragmentEntryKey(String fragmentEntryKey);

	/**
	 * Caches the fragment entry propagation in the entity cache if it is enabled.
	 *
	 * @param fragmentEntryPropagation the fragment entry propagation
	 */
	public void cacheResult(FragmentEntryPropagation fragmentEntryPropagation);

	/**
	 * Caches the fragment entry propagations in the entity cache if it is enabled.
	 *
	 * @param fragmentEntryPropagations the fragment entry propagations
	 */
	public void cacheResult(
		java.util.List<FragmentEntryPropagation> fragmentEntryPropagations);

	/**
	 * Creates a new fragment entry propagation with the primary key. Does not add the fragment entry propagation to the database.
	 *
	 * @param fragmentEntryPropagationId the primary key for the new fragment entry propagation
	 * @return the new fragment entry propagation
	 */
	public FragmentEntryPropagation create(long fragmentEntryPropagationId);

	/**
	 * Removes the fragment entry propagation with the primary key from the database. Also notifies the appropriate model listeners.
	 *
	 * @param fragmentEntryPropagationId the primary key of the fragment entry propagation
	 * @return the fragment entry propagation that was removed
	 * @throws NoSuchEntryPropagationException if a fragment entry propagation with the primary key could not be found
	 */
	public FragmentEntryPropagation remove(long fragmentEntryPropagationId)
		throws NoSuchEntryPropagationException;

	public FragmentEntryPropagation updateImpl(
		FragmentEntryPropagation fragmentEntryPropagation);

	/**
	 * Returns the fragment entry propagation with the primary key or throws a <code>NoSuchEntryPropagationException</code> if it could not be found.
	 *
	 * @param fragmentEntryPropagationId the primary key of the fragment entry propagation
	 * @return the fragment entry propagation
	 * @throws NoSuchEntryPropagationException if a fragment entry propagation with the primary key could not be found
	 */
	public FragmentEntryPropagation findByPrimaryKey(
			long fragmentEntryPropagationId)
		throws NoSuchEntryPropagationException;

	/**
	 * Returns the fragment entry propagation with the primary key or returns <code>null</code> if it could not be found.
	 *
	 * @param fragmentEntryPropagationId the primary key of the fragment entry propagation
	 * @return the fragment entry propagation, or <code>null</code> if a fragment entry propagation with the primary key could not be found
	 */
	public FragmentEntryPropagation fetchByPrimaryKey(
		long fragmentEntryPropagationId);

	/**
	 * Returns all the fragment entry propagations.
	 *
	 * @return the fragment entry propagations
	 */
	public java.util.List<FragmentEntryPropagation> findAll();

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
	public java.util.List<FragmentEntryPropagation> findAll(int start, int end);

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
	public java.util.List<FragmentEntryPropagation> findAll(
		int start, int end,
		com.liferay.portal.kernel.util.OrderByComparator
			<FragmentEntryPropagation> orderByComparator);

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
	public java.util.List<FragmentEntryPropagation> findAll(
		int start, int end,
		com.liferay.portal.kernel.util.OrderByComparator
			<FragmentEntryPropagation> orderByComparator,
		boolean useFinderCache);

	/**
	 * Removes all the fragment entry propagations from the database.
	 */
	public void removeAll();

	/**
	 * Returns the number of fragment entry propagations.
	 *
	 * @return the number of fragment entry propagations
	 */
	public int countAll();

}