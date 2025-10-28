/**
 * SPDX-FileCopyrightText: (c) 2025 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.headless.admin.site.client.dto.v1_0;

import com.liferay.headless.admin.site.client.function.UnsafeSupplier;
import com.liferay.headless.admin.site.client.serdes.v1_0.SelectFragmentConfigurationFieldValueSerDes;

import jakarta.annotation.Generated;

import java.io.Serializable;

import java.util.Objects;

/**
 * @author Rubén Pulido
 * @generated
 */
@Generated("")
public abstract class SelectFragmentConfigurationFieldValue
	implements Cloneable, Serializable {

	public static SelectFragmentConfigurationFieldValue toDTO(String json) {
		return SelectFragmentConfigurationFieldValueSerDes.toDTO(json);
	}

	public LocalizableType getLocalizableType() {
		return localizableType;
	}

	public String getLocalizableTypeAsString() {
		if (localizableType == null) {
			return null;
		}

		return localizableType.toString();
	}

	public void setLocalizableType(LocalizableType localizableType) {
		this.localizableType = localizableType;
	}

	public void setLocalizableType(
		UnsafeSupplier<LocalizableType, Exception>
			localizableTypeUnsafeSupplier) {

		try {
			localizableType = localizableTypeUnsafeSupplier.get();
		}
		catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	protected LocalizableType localizableType;

	@Override
	public SelectFragmentConfigurationFieldValue clone()
		throws CloneNotSupportedException {

		return (SelectFragmentConfigurationFieldValue)super.clone();
	}

	@Override
	public boolean equals(Object object) {
		if (this == object) {
			return true;
		}

		if (!(object instanceof SelectFragmentConfigurationFieldValue)) {
			return false;
		}

		SelectFragmentConfigurationFieldValue
			selectFragmentConfigurationFieldValue =
				(SelectFragmentConfigurationFieldValue)object;

		return Objects.equals(
			toString(), selectFragmentConfigurationFieldValue.toString());
	}

	@Override
	public int hashCode() {
		String string = toString();

		return string.hashCode();
	}

	public String toString() {
		return SelectFragmentConfigurationFieldValueSerDes.toJSON(this);
	}

	public static enum LocalizableType {

		LOCALIZABLE("Localizable"), NONLOCALIZABLE("Nonlocalizable");

		public static LocalizableType create(String value) {
			for (LocalizableType localizableType : values()) {
				if (Objects.equals(localizableType.getValue(), value) ||
					Objects.equals(localizableType.name(), value)) {

					return localizableType;
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

		private LocalizableType(String value) {
			_value = value;
		}

		private final String _value;

	}

}