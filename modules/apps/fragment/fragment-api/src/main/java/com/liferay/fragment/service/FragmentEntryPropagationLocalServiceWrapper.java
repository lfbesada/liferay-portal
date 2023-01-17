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

package com.liferay.fragment.service;

import com.liferay.fragment.model.FragmentEntryPropagation;
import com.liferay.petra.function.UnsafeFunction;
import com.liferay.portal.kernel.service.ServiceWrapper;
import com.liferay.portal.kernel.service.persistence.change.tracking.CTPersistence;

/**
 * Provides a wrapper for {@link FragmentEntryPropagationLocalService}.
 *
 * @author Brian Wing Shun Chan
 * @see FragmentEntryPropagationLocalService
 * @generated
 */
public class FragmentEntryPropagationLocalServiceWrapper
	implements FragmentEntryPropagationLocalService,
			   ServiceWrapper<FragmentEntryPropagationLocalService> {

	public FragmentEntryPropagationLocalServiceWrapper() {
		this(null);
	}

	public FragmentEntryPropagationLocalServiceWrapper(
		FragmentEntryPropagationLocalService
			fragmentEntryPropagationLocalService) {

		_fragmentEntryPropagationLocalService =
			fragmentEntryPropagationLocalService;
	}

	/**
	 * Adds the fragment entry propagation to the database. Also notifies the appropriate model listeners.
	 *
	 * <p>
	 * <strong>Important:</strong> Inspect FragmentEntryPropagationLocalServiceImpl for overloaded versions of the method. If provided, use these entry points to the API, as the implementation logic may require the additional parameters defined there.
	 * </p>
	 *
	 * @param fragmentEntryPropagation the fragment entry propagation
	 * @return the fragment entry propagation that was added
	 */
	@Override
	public FragmentEntryPropagation addFragmentEntryPropagation(
		FragmentEntryPropagation fragmentEntryPropagation) {

		return _fragmentEntryPropagationLocalService.
			addFragmentEntryPropagation(fragmentEntryPropagation);
	}

	@Override
	public FragmentEntryPropagation addOrUpdateFragmentEntryPropagation(
		String fragmentEntryKey, String css, String html, String js,
		String configuration, int type) {

		return _fragmentEntryPropagationLocalService.
			addOrUpdateFragmentEntryPropagation(
				fragmentEntryKey, css, html, js, configuration, type);
	}

	/**
	 * Creates a new fragment entry propagation with the primary key. Does not add the fragment entry propagation to the database.
	 *
	 * @param fragmentEntryPropagationId the primary key for the new fragment entry propagation
	 * @return the new fragment entry propagation
	 */
	@Override
	public FragmentEntryPropagation createFragmentEntryPropagation(
		long fragmentEntryPropagationId) {

		return _fragmentEntryPropagationLocalService.
			createFragmentEntryPropagation(fragmentEntryPropagationId);
	}

	/**
	 * @throws PortalException
	 */
	@Override
	public com.liferay.portal.kernel.model.PersistedModel createPersistedModel(
			java.io.Serializable primaryKeyObj)
		throws com.liferay.portal.kernel.exception.PortalException {

		return _fragmentEntryPropagationLocalService.createPersistedModel(
			primaryKeyObj);
	}

	/**
	 * Deletes the fragment entry propagation from the database. Also notifies the appropriate model listeners.
	 *
	 * <p>
	 * <strong>Important:</strong> Inspect FragmentEntryPropagationLocalServiceImpl for overloaded versions of the method. If provided, use these entry points to the API, as the implementation logic may require the additional parameters defined there.
	 * </p>
	 *
	 * @param fragmentEntryPropagation the fragment entry propagation
	 * @return the fragment entry propagation that was removed
	 */
	@Override
	public FragmentEntryPropagation deleteFragmentEntryPropagation(
		FragmentEntryPropagation fragmentEntryPropagation) {

		return _fragmentEntryPropagationLocalService.
			deleteFragmentEntryPropagation(fragmentEntryPropagation);
	}

	/**
	 * Deletes the fragment entry propagation with the primary key from the database. Also notifies the appropriate model listeners.
	 *
	 * <p>
	 * <strong>Important:</strong> Inspect FragmentEntryPropagationLocalServiceImpl for overloaded versions of the method. If provided, use these entry points to the API, as the implementation logic may require the additional parameters defined there.
	 * </p>
	 *
	 * @param fragmentEntryPropagationId the primary key of the fragment entry propagation
	 * @return the fragment entry propagation that was removed
	 * @throws PortalException if a fragment entry propagation with the primary key could not be found
	 */
	@Override
	public FragmentEntryPropagation deleteFragmentEntryPropagation(
			long fragmentEntryPropagationId)
		throws com.liferay.portal.kernel.exception.PortalException {

		return _fragmentEntryPropagationLocalService.
			deleteFragmentEntryPropagation(fragmentEntryPropagationId);
	}

	/**
	 * @throws PortalException
	 */
	@Override
	public com.liferay.portal.kernel.model.PersistedModel deletePersistedModel(
			com.liferay.portal.kernel.model.PersistedModel persistedModel)
		throws com.liferay.portal.kernel.exception.PortalException {

		return _fragmentEntryPropagationLocalService.deletePersistedModel(
			persistedModel);
	}

	@Override
	public <T> T dslQuery(com.liferay.petra.sql.dsl.query.DSLQuery dslQuery) {
		return _fragmentEntryPropagationLocalService.dslQuery(dslQuery);
	}

	@Override
	public int dslQueryCount(
		com.liferay.petra.sql.dsl.query.DSLQuery dslQuery) {

		return _fragmentEntryPropagationLocalService.dslQueryCount(dslQuery);
	}

	@Override
	public com.liferay.portal.kernel.dao.orm.DynamicQuery dynamicQuery() {
		return _fragmentEntryPropagationLocalService.dynamicQuery();
	}

	/**
	 * Performs a dynamic query on the database and returns the matching rows.
	 *
	 * @param dynamicQuery the dynamic query
	 * @return the matching rows
	 */
	@Override
	public <T> java.util.List<T> dynamicQuery(
		com.liferay.portal.kernel.dao.orm.DynamicQuery dynamicQuery) {

		return _fragmentEntryPropagationLocalService.dynamicQuery(dynamicQuery);
	}

	/**
	 * Performs a dynamic query on the database and returns a range of the matching rows.
	 *
	 * <p>
	 * Useful when paginating results. Returns a maximum of <code>end - start</code> instances. <code>start</code> and <code>end</code> are not primary keys, they are indexes in the result set. Thus, <code>0</code> refers to the first result in the set. Setting both <code>start</code> and <code>end</code> to <code>com.liferay.portal.kernel.dao.orm.QueryUtil#ALL_POS</code> will return the full result set. If <code>orderByComparator</code> is specified, then the query will include the given ORDER BY logic. If <code>orderByComparator</code> is absent, then the query will include the default ORDER BY logic from <code>com.liferay.fragment.model.impl.FragmentEntryPropagationModelImpl</code>.
	 * </p>
	 *
	 * @param dynamicQuery the dynamic query
	 * @param start the lower bound of the range of model instances
	 * @param end the upper bound of the range of model instances (not inclusive)
	 * @return the range of matching rows
	 */
	@Override
	public <T> java.util.List<T> dynamicQuery(
		com.liferay.portal.kernel.dao.orm.DynamicQuery dynamicQuery, int start,
		int end) {

		return _fragmentEntryPropagationLocalService.dynamicQuery(
			dynamicQuery, start, end);
	}

	/**
	 * Performs a dynamic query on the database and returns an ordered range of the matching rows.
	 *
	 * <p>
	 * Useful when paginating results. Returns a maximum of <code>end - start</code> instances. <code>start</code> and <code>end</code> are not primary keys, they are indexes in the result set. Thus, <code>0</code> refers to the first result in the set. Setting both <code>start</code> and <code>end</code> to <code>com.liferay.portal.kernel.dao.orm.QueryUtil#ALL_POS</code> will return the full result set. If <code>orderByComparator</code> is specified, then the query will include the given ORDER BY logic. If <code>orderByComparator</code> is absent, then the query will include the default ORDER BY logic from <code>com.liferay.fragment.model.impl.FragmentEntryPropagationModelImpl</code>.
	 * </p>
	 *
	 * @param dynamicQuery the dynamic query
	 * @param start the lower bound of the range of model instances
	 * @param end the upper bound of the range of model instances (not inclusive)
	 * @param orderByComparator the comparator to order the results by (optionally <code>null</code>)
	 * @return the ordered range of matching rows
	 */
	@Override
	public <T> java.util.List<T> dynamicQuery(
		com.liferay.portal.kernel.dao.orm.DynamicQuery dynamicQuery, int start,
		int end,
		com.liferay.portal.kernel.util.OrderByComparator<T> orderByComparator) {

		return _fragmentEntryPropagationLocalService.dynamicQuery(
			dynamicQuery, start, end, orderByComparator);
	}

	/**
	 * Returns the number of rows matching the dynamic query.
	 *
	 * @param dynamicQuery the dynamic query
	 * @return the number of rows matching the dynamic query
	 */
	@Override
	public long dynamicQueryCount(
		com.liferay.portal.kernel.dao.orm.DynamicQuery dynamicQuery) {

		return _fragmentEntryPropagationLocalService.dynamicQueryCount(
			dynamicQuery);
	}

	/**
	 * Returns the number of rows matching the dynamic query.
	 *
	 * @param dynamicQuery the dynamic query
	 * @param projection the projection to apply to the query
	 * @return the number of rows matching the dynamic query
	 */
	@Override
	public long dynamicQueryCount(
		com.liferay.portal.kernel.dao.orm.DynamicQuery dynamicQuery,
		com.liferay.portal.kernel.dao.orm.Projection projection) {

		return _fragmentEntryPropagationLocalService.dynamicQueryCount(
			dynamicQuery, projection);
	}

	@Override
	public FragmentEntryPropagation fetchByFragmentEntryKey(
		String fragmentEntryKey) {

		return _fragmentEntryPropagationLocalService.fetchByFragmentEntryKey(
			fragmentEntryKey);
	}

	@Override
	public FragmentEntryPropagation fetchFragmentEntryPropagation(
		long fragmentEntryPropagationId) {

		return _fragmentEntryPropagationLocalService.
			fetchFragmentEntryPropagation(fragmentEntryPropagationId);
	}

	@Override
	public com.liferay.portal.kernel.dao.orm.ActionableDynamicQuery
		getActionableDynamicQuery() {

		return _fragmentEntryPropagationLocalService.
			getActionableDynamicQuery();
	}

	/**
	 * Returns the fragment entry propagation with the primary key.
	 *
	 * @param fragmentEntryPropagationId the primary key of the fragment entry propagation
	 * @return the fragment entry propagation
	 * @throws PortalException if a fragment entry propagation with the primary key could not be found
	 */
	@Override
	public FragmentEntryPropagation getFragmentEntryPropagation(
			long fragmentEntryPropagationId)
		throws com.liferay.portal.kernel.exception.PortalException {

		return _fragmentEntryPropagationLocalService.
			getFragmentEntryPropagation(fragmentEntryPropagationId);
	}

	/**
	 * Returns a range of all the fragment entry propagations.
	 *
	 * <p>
	 * Useful when paginating results. Returns a maximum of <code>end - start</code> instances. <code>start</code> and <code>end</code> are not primary keys, they are indexes in the result set. Thus, <code>0</code> refers to the first result in the set. Setting both <code>start</code> and <code>end</code> to <code>com.liferay.portal.kernel.dao.orm.QueryUtil#ALL_POS</code> will return the full result set. If <code>orderByComparator</code> is specified, then the query will include the given ORDER BY logic. If <code>orderByComparator</code> is absent, then the query will include the default ORDER BY logic from <code>com.liferay.fragment.model.impl.FragmentEntryPropagationModelImpl</code>.
	 * </p>
	 *
	 * @param start the lower bound of the range of fragment entry propagations
	 * @param end the upper bound of the range of fragment entry propagations (not inclusive)
	 * @return the range of fragment entry propagations
	 */
	@Override
	public java.util.List<FragmentEntryPropagation>
		getFragmentEntryPropagations(int start, int end) {

		return _fragmentEntryPropagationLocalService.
			getFragmentEntryPropagations(start, end);
	}

	/**
	 * Returns the number of fragment entry propagations.
	 *
	 * @return the number of fragment entry propagations
	 */
	@Override
	public int getFragmentEntryPropagationsCount() {
		return _fragmentEntryPropagationLocalService.
			getFragmentEntryPropagationsCount();
	}

	@Override
	public com.liferay.portal.kernel.dao.orm.IndexableActionableDynamicQuery
		getIndexableActionableDynamicQuery() {

		return _fragmentEntryPropagationLocalService.
			getIndexableActionableDynamicQuery();
	}

	/**
	 * Returns the OSGi service identifier.
	 *
	 * @return the OSGi service identifier
	 */
	@Override
	public String getOSGiServiceIdentifier() {
		return _fragmentEntryPropagationLocalService.getOSGiServiceIdentifier();
	}

	/**
	 * @throws PortalException
	 */
	@Override
	public com.liferay.portal.kernel.model.PersistedModel getPersistedModel(
			java.io.Serializable primaryKeyObj)
		throws com.liferay.portal.kernel.exception.PortalException {

		return _fragmentEntryPropagationLocalService.getPersistedModel(
			primaryKeyObj);
	}

	/**
	 * Updates the fragment entry propagation in the database or adds it if it does not yet exist. Also notifies the appropriate model listeners.
	 *
	 * <p>
	 * <strong>Important:</strong> Inspect FragmentEntryPropagationLocalServiceImpl for overloaded versions of the method. If provided, use these entry points to the API, as the implementation logic may require the additional parameters defined there.
	 * </p>
	 *
	 * @param fragmentEntryPropagation the fragment entry propagation
	 * @return the fragment entry propagation that was updated
	 */
	@Override
	public FragmentEntryPropagation updateFragmentEntryPropagation(
		FragmentEntryPropagation fragmentEntryPropagation) {

		return _fragmentEntryPropagationLocalService.
			updateFragmentEntryPropagation(fragmentEntryPropagation);
	}

	@Override
	public CTPersistence<FragmentEntryPropagation> getCTPersistence() {
		return _fragmentEntryPropagationLocalService.getCTPersistence();
	}

	@Override
	public Class<FragmentEntryPropagation> getModelClass() {
		return _fragmentEntryPropagationLocalService.getModelClass();
	}

	@Override
	public <R, E extends Throwable> R updateWithUnsafeFunction(
			UnsafeFunction<CTPersistence<FragmentEntryPropagation>, R, E>
				updateUnsafeFunction)
		throws E {

		return _fragmentEntryPropagationLocalService.updateWithUnsafeFunction(
			updateUnsafeFunction);
	}

	@Override
	public FragmentEntryPropagationLocalService getWrappedService() {
		return _fragmentEntryPropagationLocalService;
	}

	@Override
	public void setWrappedService(
		FragmentEntryPropagationLocalService
			fragmentEntryPropagationLocalService) {

		_fragmentEntryPropagationLocalService =
			fragmentEntryPropagationLocalService;
	}

	private FragmentEntryPropagationLocalService
		_fragmentEntryPropagationLocalService;

}