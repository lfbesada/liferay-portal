/**
 * SPDX-FileCopyrightText: (c) 2025 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.headless.admin.site.client.dto.v1_0;

import com.liferay.headless.admin.site.client.function.UnsafeSupplier;
import com.liferay.headless.admin.site.client.serdes.v1_0.UrlConfigurationFieldValueSerDes;

import jakarta.annotation.Generated;

import java.io.Serializable;

import java.util.Objects;

/**
 * @author Rubén Pulido
 * @generated
 */
@Generated("")
public class UrlConfigurationFieldValue
	extends ConfigurationFieldValue implements Cloneable, Serializable {

	public static UrlConfigurationFieldValue toDTO(String json) {
		return UrlConfigurationFieldValueSerDes.toDTO(json);
	}

	public Object getValue() {
		return value;
	}

	public void setValue(Object value) {
		this.value = value;
	}

	public void setValue(
		UnsafeSupplier<Object, Exception> valueUnsafeSupplier) {

		try {
			value = valueUnsafeSupplier.get();
		}
		catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	protected Object value;

	public Object getValue_i18n() {
		return value_i18n;
	}

	public void setValue_i18n(Object value_i18n) {
		this.value_i18n = value_i18n;
	}

	public void setValue_i18n(
		UnsafeSupplier<Object, Exception> value_i18nUnsafeSupplier) {

		try {
			value_i18n = value_i18nUnsafeSupplier.get();
		}
		catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	protected Object value_i18n;

	@Override
	public UrlConfigurationFieldValue clone()
		throws CloneNotSupportedException {

		return (UrlConfigurationFieldValue)super.clone();
	}

	@Override
	public boolean equals(Object object) {
		if (this == object) {
			return true;
		}

		if (!(object instanceof UrlConfigurationFieldValue)) {
			return false;
		}

		UrlConfigurationFieldValue urlConfigurationFieldValue =
			(UrlConfigurationFieldValue)object;

		return Objects.equals(
			toString(), urlConfigurationFieldValue.toString());
	}

	@Override
	public int hashCode() {
		String string = toString();

		return string.hashCode();
	}

	public String toString() {
		return UrlConfigurationFieldValueSerDes.toJSON(this);
	}

}