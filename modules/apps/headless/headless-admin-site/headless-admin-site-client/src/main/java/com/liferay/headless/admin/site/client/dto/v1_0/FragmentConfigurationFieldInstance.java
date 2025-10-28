/**
 * SPDX-FileCopyrightText: (c) 2025 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.headless.admin.site.client.dto.v1_0;

import com.liferay.headless.admin.site.client.function.UnsafeSupplier;
import com.liferay.headless.admin.site.client.serdes.v1_0.FragmentConfigurationFieldInstanceSerDes;

import jakarta.annotation.Generated;

import java.io.Serializable;

import java.util.Objects;

/**
 * @author Rubén Pulido
 * @generated
 */
@Generated("")
public abstract class FragmentConfigurationFieldInstance
	implements Cloneable, Serializable {

	public static FragmentConfigurationFieldInstance toDTO(String json) {
		return FragmentConfigurationFieldInstanceSerDes.toDTO(json);
	}

	public FragmentConfigurationFieldType getFragmentConfigurationFieldType() {
		return fragmentConfigurationFieldType;
	}

	public String getFragmentConfigurationFieldTypeAsString() {
		if (fragmentConfigurationFieldType == null) {
			return null;
		}

		return fragmentConfigurationFieldType.toString();
	}

	public void setFragmentConfigurationFieldType(
		FragmentConfigurationFieldType fragmentConfigurationFieldType) {

		this.fragmentConfigurationFieldType = fragmentConfigurationFieldType;
	}

	public void setFragmentConfigurationFieldType(
		UnsafeSupplier<FragmentConfigurationFieldType, Exception>
			fragmentConfigurationFieldTypeUnsafeSupplier) {

		try {
			fragmentConfigurationFieldType =
				fragmentConfigurationFieldTypeUnsafeSupplier.get();
		}
		catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	protected FragmentConfigurationFieldType fragmentConfigurationFieldType;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public void setName(UnsafeSupplier<String, Exception> nameUnsafeSupplier) {
		try {
			name = nameUnsafeSupplier.get();
		}
		catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	protected String name;

	@Override
	public FragmentConfigurationFieldInstance clone()
		throws CloneNotSupportedException {

		return (FragmentConfigurationFieldInstance)super.clone();
	}

	@Override
	public boolean equals(Object object) {
		if (this == object) {
			return true;
		}

		if (!(object instanceof FragmentConfigurationFieldInstance)) {
			return false;
		}

		FragmentConfigurationFieldInstance fragmentConfigurationFieldInstance =
			(FragmentConfigurationFieldInstance)object;

		return Objects.equals(
			toString(), fragmentConfigurationFieldInstance.toString());
	}

	@Override
	public int hashCode() {
		String string = toString();

		return string.hashCode();
	}

	public String toString() {
		return FragmentConfigurationFieldInstanceSerDes.toJSON(this);
	}

	public static enum FragmentConfigurationFieldType {

		SELECT("Select");

		public static FragmentConfigurationFieldType create(String value) {
			for (FragmentConfigurationFieldType fragmentConfigurationFieldType :
					values()) {

				if (Objects.equals(
						fragmentConfigurationFieldType.getValue(), value) ||
					Objects.equals(
						fragmentConfigurationFieldType.name(), value)) {

					return fragmentConfigurationFieldType;
				}
			}

			return null;
		}

		public String getValue() {
			return _value;
		}

		@Override
		public String toString() {
			return _value;
		}

		private FragmentConfigurationFieldType(String value) {
			_value = value;
		}

		private final String _value;

	}

}