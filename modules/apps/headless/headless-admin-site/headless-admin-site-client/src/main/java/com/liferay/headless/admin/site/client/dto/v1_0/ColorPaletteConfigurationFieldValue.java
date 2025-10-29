/**
 * SPDX-FileCopyrightText: (c) 2025 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.headless.admin.site.client.dto.v1_0;

import com.liferay.headless.admin.site.client.function.UnsafeSupplier;
import com.liferay.headless.admin.site.client.serdes.v1_0.ColorPaletteConfigurationFieldValueSerDes;

import jakarta.annotation.Generated;

import java.io.Serializable;

import java.util.Map;
import java.util.Objects;

/**
 * @author Rubén Pulido
 * @generated
 */
@Generated("")
public class ColorPaletteConfigurationFieldValue
	extends ConfigurationFieldValue implements Cloneable, Serializable {

	public static ColorPaletteConfigurationFieldValue toDTO(String json) {
		return ColorPaletteConfigurationFieldValueSerDes.toDTO(json);
	}

	public ColorPaletteValue getValue() {
		return value;
	}

	public void setValue(ColorPaletteValue value) {
		this.value = value;
	}

	public void setValue(
		UnsafeSupplier<ColorPaletteValue, Exception> valueUnsafeSupplier) {

		try {
			value = valueUnsafeSupplier.get();
		}
		catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	protected ColorPaletteValue value;

	public Map<String, ColorPaletteValue> getValue_i18n() {
		return value_i18n;
	}

	public void setValue_i18n(Map<String, ColorPaletteValue> value_i18n) {
		this.value_i18n = value_i18n;
	}

	public void setValue_i18n(
		UnsafeSupplier<Map<String, ColorPaletteValue>, Exception>
			value_i18nUnsafeSupplier) {

		try {
			value_i18n = value_i18nUnsafeSupplier.get();
		}
		catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	protected Map<String, ColorPaletteValue> value_i18n;

	@Override
	public ColorPaletteConfigurationFieldValue clone()
		throws CloneNotSupportedException {

		return (ColorPaletteConfigurationFieldValue)super.clone();
	}

	@Override
	public boolean equals(Object object) {
		if (this == object) {
			return true;
		}

		if (!(object instanceof ColorPaletteConfigurationFieldValue)) {
			return false;
		}

		ColorPaletteConfigurationFieldValue
			colorPaletteConfigurationFieldValue =
				(ColorPaletteConfigurationFieldValue)object;

		return Objects.equals(
			toString(), colorPaletteConfigurationFieldValue.toString());
	}

	@Override
	public int hashCode() {
		String string = toString();

		return string.hashCode();
	}

	public String toString() {
		return ColorPaletteConfigurationFieldValueSerDes.toJSON(this);
	}

}