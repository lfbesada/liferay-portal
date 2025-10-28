/**
 * SPDX-FileCopyrightText: (c) 2025 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.headless.admin.site.client.dto.v1_0;

import com.liferay.headless.admin.site.client.function.UnsafeSupplier;
import com.liferay.headless.admin.site.client.serdes.v1_0.SelectFragmentConfigurationFieldInstanceSerDes;

import jakarta.annotation.Generated;

import java.io.Serializable;

import java.util.Objects;

/**
 * @author Rubén Pulido
 * @generated
 */
@Generated("")
public class SelectFragmentConfigurationFieldInstance
	extends FragmentConfigurationFieldInstance
	implements Cloneable, Serializable {

	public static SelectFragmentConfigurationFieldInstance toDTO(String json) {
		return SelectFragmentConfigurationFieldInstanceSerDes.toDTO(json);
	}

	public SelectFragmentConfigurationFieldValue
		getSelectFragmentConfigurationFieldValue() {

		return selectFragmentConfigurationFieldValue;
	}

	public void setSelectFragmentConfigurationFieldValue(
		SelectFragmentConfigurationFieldValue
			selectFragmentConfigurationFieldValue) {

		this.selectFragmentConfigurationFieldValue =
			selectFragmentConfigurationFieldValue;
	}

	public void setSelectFragmentConfigurationFieldValue(
		UnsafeSupplier<SelectFragmentConfigurationFieldValue, Exception>
			selectFragmentConfigurationFieldValueUnsafeSupplier) {

		try {
			selectFragmentConfigurationFieldValue =
				selectFragmentConfigurationFieldValueUnsafeSupplier.get();
		}
		catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	protected SelectFragmentConfigurationFieldValue
		selectFragmentConfigurationFieldValue;

	@Override
	public SelectFragmentConfigurationFieldInstance clone()
		throws CloneNotSupportedException {

		return (SelectFragmentConfigurationFieldInstance)super.clone();
	}

	@Override
	public boolean equals(Object object) {
		if (this == object) {
			return true;
		}

		if (!(object instanceof SelectFragmentConfigurationFieldInstance)) {
			return false;
		}

		SelectFragmentConfigurationFieldInstance
			selectFragmentConfigurationFieldInstance =
				(SelectFragmentConfigurationFieldInstance)object;

		return Objects.equals(
			toString(), selectFragmentConfigurationFieldInstance.toString());
	}

	@Override
	public int hashCode() {
		String string = toString();

		return string.hashCode();
	}

	public String toString() {
		return SelectFragmentConfigurationFieldInstanceSerDes.toJSON(this);
	}

}