/**
 * SPDX-FileCopyrightText: (c) 2025 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.headless.admin.site.client.dto.v1_0;

import com.liferay.headless.admin.site.client.function.UnsafeSupplier;
import com.liferay.headless.admin.site.client.serdes.v1_0.NavigationMenuValueSerDes;

import jakarta.annotation.Generated;

import java.io.Serializable;

import java.util.Objects;

/**
 * @author Rubén Pulido
 * @generated
 */
@Generated("")
public class NavigationMenuValue implements Cloneable, Serializable {

	public static NavigationMenuValue toDTO(String json) {
		return NavigationMenuValueSerDes.toDTO(json);
	}

	public ItemExternalReference getNavigationMenu() {
		return navigationMenu;
	}

	public void setNavigationMenu(ItemExternalReference navigationMenu) {
		this.navigationMenu = navigationMenu;
	}

	public void setNavigationMenu(
		UnsafeSupplier<ItemExternalReference, Exception>
			navigationMenuUnsafeSupplier) {

		try {
			navigationMenu = navigationMenuUnsafeSupplier.get();
		}
		catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	protected ItemExternalReference navigationMenu;

	public String getParentItemExternalReferenceCode() {
		return parentItemExternalReferenceCode;
	}

	public void setParentItemExternalReferenceCode(
		String parentItemExternalReferenceCode) {

		this.parentItemExternalReferenceCode = parentItemExternalReferenceCode;
	}

	public void setParentItemExternalReferenceCode(
		UnsafeSupplier<String, Exception>
			parentItemExternalReferenceCodeUnsafeSupplier) {

		try {
			parentItemExternalReferenceCode =
				parentItemExternalReferenceCodeUnsafeSupplier.get();
		}
		catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	protected String parentItemExternalReferenceCode;

	public Boolean getPrivatePages() {
		return privatePages;
	}

	public void setPrivatePages(Boolean privatePages) {
		this.privatePages = privatePages;
	}

	public void setPrivatePages(
		UnsafeSupplier<Boolean, Exception> privatePagesUnsafeSupplier) {

		try {
			privatePages = privatePagesUnsafeSupplier.get();
		}
		catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	protected Boolean privatePages;

	@Override
	public NavigationMenuValue clone() throws CloneNotSupportedException {
		return (NavigationMenuValue)super.clone();
	}

	@Override
	public boolean equals(Object object) {
		if (this == object) {
			return true;
		}

		if (!(object instanceof NavigationMenuValue)) {
			return false;
		}

		NavigationMenuValue navigationMenuValue = (NavigationMenuValue)object;

		return Objects.equals(toString(), navigationMenuValue.toString());
	}

	@Override
	public int hashCode() {
		String string = toString();

		return string.hashCode();
	}

	public String toString() {
		return NavigationMenuValueSerDes.toJSON(this);
	}

}