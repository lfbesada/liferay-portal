/**
 * SPDX-FileCopyrightText: (c) 2026 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.design.library.resource.type;

import com.liferay.depot.model.DepotEntry;
import com.liferay.frontend.data.set.model.FDSActionDropdownItem;
import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.security.permission.PermissionChecker;

import jakarta.servlet.http.HttpServletRequest;

import java.util.List;
import java.util.Locale;
import java.util.Map;

/**
 * Contributes a resource type to the Design Library Admin. Register one
 * component per type from the module that owns the type, and the type appears
 * without any change to the Design Library Admin itself.
 *
 * <p>
 * The listing is backed by a single search query. Every contributed type must
 * therefore be indexed with a <code>groupIds</code> field and must have a
 * <code>DTOConverter</code> registered under <code>dto.class.name</code> for
 * its entry class name, or its rows list with empty columns.
 * </p>
 *
 * @author Thiago Buarque
 */
public interface DesignLibraryResourceTypeContributor {

	/**
	 * Returns the name of the Clay palette custom property used to tint this
	 * type's sticker, such as <code>"purple"</code>. The Design Library Admin
	 * composes the CSS reference via <code>var(--${color})</code>, so return
	 * only the palette name without the CSS <code>--</code> prefix.
	 */
	public String getColor();

	/**
	 * Returns the ES import declaration for the JavaScript factory that
	 * produces this type's creation menu items, such as
	 * <code>"{getFooCreationItems} from foo-web"</code>, or <code>null</code>
	 * when the type cannot be created.
	 *
	 * <p>
	 * Name the module as it is written in an import statement and export the
	 * factory from the module's own <code>js/index.js</code>. The Design
	 * Library Admin resolves the declaration to an absolute URL before
	 * serializing it, because the browser loads it through a dynamic import at
	 * runtime and a bare name only resolves for modules that publish an import
	 * map entry, which web modules do not.
	 * </p>
	 *
	 * <p>
	 * The factory receives {@link #getCreationItemsProps} and returns an array
	 * of <code>{label, onClick}</code> items, so one type may contribute
	 * several entries to the menu. Fragments contribute three this way.
	 * </p>
	 */
	public String getCreationItemsModule();

	/**
	 * Returns the properties passed to the factory named by {@link
	 * #getCreationItemsModule}, such as the URLs and portlet namespace its
	 * modal needs.
	 *
	 * <p>
	 * Only called when that method returns a module and {@link
	 * #hasAddPermission} grants the current user. Every value must be
	 * JSON-serializable, because the map is serialized into the page. Use
	 * <code>backURL</code> as the redirect so the user returns to the Design
	 * Library after creating an entry.
	 * </p>
	 */
	public Map<String, Object> getCreationItemsProps(
			HttpServletRequest httpServletRequest, DepotEntry depotEntry,
			String backURL)
		throws PortalException;

	/**
	 * Returns the id of the action the row title links to, such as
	 * <code>"view"</code> or <code>"edit"</code>. The id must match one
	 * returned by {@link #getFDSActionDropdownItems}.
	 */
	public String getDefaultActionId();

	/**
	 * Returns the class name of the model this type lists, such as
	 * <code>StyleBookEntry.class.getName()</code>.
	 *
	 * <p>
	 * The name is added to the search query and, with {@link #getType},
	 * identifies which rows belong to this type. Several types may share one
	 * class name.
	 * </p>
	 */
	public String getEntryClassName();

	/**
	 * Returns this type's row actions.
	 *
	 * <p>
	 * Actions are declared once for the type rather than once per row, so build
	 * hrefs from templates such as
	 * <code>"{embedded.externalReferenceCode}"</code> that the data set expands
	 * per row. Do not set visibility filters; the Design Library Admin stamps
	 * them from {@link #getEntryClassName} and {@link #getType} so that one
	 * type's actions never appear on another's rows.
	 * </p>
	 *
	 * <p>
	 * An action cannot depend on the state of an individual entry through Java
	 * logic. A rule such as "the default style book cannot be deleted" is
	 * expressible only when the deciding fact is a field the search response
	 * returns, in which case add a visibility filter for that field.
	 * </p>
	 */
	public List<FDSActionDropdownItem> getFDSActionDropdownItems(
			HttpServletRequest httpServletRequest, DepotEntry depotEntry,
			String backURL)
		throws PortalException;

	/**
	 * Returns the Clay icon symbol shown on this type's rows, such as
	 * <code>"book"</code>.
	 */
	public String getIcon();

	/**
	 * Returns the unique and stable key of this type, such as
	 * <code>"style-book"</code>. Unlike {@link #getEntryClassName}, it
	 * identifies the type even when several types share a model class.
	 */
	public String getKey();

	/**
	 * Returns the localized name of this type, shown in the type column.
	 */
	public String getLabel(Locale locale);

	/**
	 * Returns the value that the <code>type</code> field of a row must hold for
	 * the row to belong to this type, or <code>null</code> when the entry class
	 * name alone identifies it.
	 *
	 * <p>
	 * Override only when more than one type shares an entry class name.
	 * Masters, display page templates, content page templates, and widget
	 * templates are all <code>LayoutPageTemplateEntry</code> and differ only by
	 * its <code>type</code> field, so each returns the same entry class name
	 * and a distinct value here. The value is compared as a string against the
	 * indexed <code>type</code> field, so
	 * <code>LayoutPageTemplateEntryTypeConstants.MASTER_LAYOUT</code> becomes
	 * <code>"3"</code>.
	 * </p>
	 */
	public default String getType() {
		return null;
	}

	/**
	 * Returns <code>true</code> when the user may add entries of this type to
	 * this Design Library. Gates the creation menu items.
	 */
	public boolean hasAddPermission(
		PermissionChecker permissionChecker, DepotEntry depotEntry);

	/**
	 * Returns <code>true</code> when the user may see entries of this type in
	 * this Design Library.
	 *
	 * <p>
	 * A type that returns <code>false</code> contributes no rows and no
	 * actions, and its class name is left out of the search query entirely.
	 * When no type returns <code>true</code>, the Design Library Admin shows
	 * the no-access message instead of the table.
	 * </p>
	 */
	public boolean hasViewPermission(
		PermissionChecker permissionChecker, DepotEntry depotEntry);

}