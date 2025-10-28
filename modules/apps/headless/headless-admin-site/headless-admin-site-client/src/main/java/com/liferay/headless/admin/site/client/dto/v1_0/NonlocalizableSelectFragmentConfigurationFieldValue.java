/**
 * SPDX-FileCopyrightText: (c) 2025 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.headless.admin.site.client.dto.v1_0;

import com.liferay.headless.admin.site.client.function.UnsafeSupplier;
import com.liferay.headless.admin.site.client.serdes.v1_0.NonlocalizableSelectFragmentConfigurationFieldValueSerDes;

import jakarta.annotation.Generated;

import java.io.Serializable;

import java.util.Objects;

/**
 * @author Rubén Pulido
 * @generated
 */
@Generated("")
public class NonlocalizableSelectFragmentConfigurationFieldValue
	extends SelectFragmentConfigurationFieldValue
	implements Cloneable, Serializable {

	public static NonlocalizableSelectFragmentConfigurationFieldValue toDTO(
		String json) {

		return NonlocalizableSelectFragmentConfigurationFieldValueSerDes.toDTO(
			json);
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	public void setValue(
		UnsafeSupplier<String, Exception> valueUnsafeSupplier) {

		try {
			value = valueUnsafeSupplier.get();
		}
		catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	protected String value;

	@Override
	public NonlocalizableSelectFragmentConfigurationFieldValue clone()
		throws CloneNotSupportedException {

		return (NonlocalizableSelectFragmentConfigurationFieldValue)
			super.clone();
	}

	@Override
	public boolean equals(Object object) {
		if (this == object) {
			return true;
		}

		if (!(object instanceof
				NonlocalizableSelectFragmentConfigurationFieldValue)) {

			return false;
		}

		NonlocalizableSelectFragmentConfigurationFieldValue
			nonlocalizableSelectFragmentConfigurationFieldValue =
				(NonlocalizableSelectFragmentConfigurationFieldValue)object;

		return Objects.equals(
			toString(),
			nonlocalizableSelectFragmentConfigurationFieldValue.toString());
	}

	@Override
	public int hashCode() {
		String string = toString();

		return string.hashCode();
	}

	public String toString() {
		return NonlocalizableSelectFragmentConfigurationFieldValueSerDes.toJSON(
			this);
	}

}