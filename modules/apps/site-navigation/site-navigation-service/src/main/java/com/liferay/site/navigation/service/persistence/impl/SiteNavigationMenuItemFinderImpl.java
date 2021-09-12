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

package com.liferay.site.navigation.service.persistence.impl;

import com.liferay.petra.string.StringBundler;
import com.liferay.petra.string.StringPool;
import com.liferay.portal.dao.orm.custom.sql.CustomSQL;
import com.liferay.portal.kernel.dao.orm.QueryPos;
import com.liferay.portal.kernel.dao.orm.SQLQuery;
import com.liferay.portal.kernel.dao.orm.Session;
import com.liferay.portal.kernel.dao.orm.Type;
import com.liferay.portal.kernel.exception.SystemException;
import com.liferay.site.navigation.service.persistence.SiteNavigationMenuItemFinder;

import java.util.List;

import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

/**
 * @author Lourdes Fernández Besada
 */
@Component(service = SiteNavigationMenuItemFinder.class)
public class SiteNavigationMenuItemFinderImpl
	extends SiteNavigationMenuItemFinderBaseImpl
	implements SiteNavigationMenuItemFinder {

	public static final String FIND_PARENTID_BY_M_TS =
		SiteNavigationMenuItemFinder.class.getName() + ".findParentIdByM_TS";

	@Override
	public List<Long> findParentIdByM_TS(
		long siteNavigationMenuId, String typeSettings) {

		Session session = null;

		try {
			session = openSession();

			String sql = _customSQL.get(getClass(), FIND_PARENTID_BY_M_TS);

			SQLQuery sqlQuery = session.createSynchronizedSQLQuery(sql);

			sqlQuery.setCacheable(true);
			sqlQuery.addScalar("parentSiteNavigationMenuItemId", Type.LONG);

			QueryPos queryPos = QueryPos.getInstance(sqlQuery);

			queryPos.add(siteNavigationMenuId);
			queryPos.add(
				StringBundler.concat(
					StringPool.PERCENT, typeSettings, StringPool.PERCENT));

			return (List<Long>)sqlQuery.list();
		}
		catch (Exception exception) {
			throw new SystemException(exception);
		}
		finally {
			closeSession(session);
		}
	}

	@Reference
	private CustomSQL _customSQL;

}