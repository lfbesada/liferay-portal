/**
 * SPDX-FileCopyrightText: (c) 2025 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.headless.admin.site.client.dto.v1_0;

import com.liferay.headless.admin.site.client.function.UnsafeSupplier;
import com.liferay.headless.admin.site.client.serdes.v1_0.ItemConfigurationFieldValueSerDes;

import jakarta.annotation.Generated;

import java.io.Serializable;

import java.util.Map;
import java.util.Objects;

/**
 * @author Rubén Pulido
 * @generated
 */
@Generated("")
public class ItemConfigurationFieldValue
	extends ConfigurationFieldValue implements Cloneable, Serializable {

	public static ItemConfigurationFieldValue toDTO(String json) {
		return ItemConfigurationFieldValueSerDes.toDTO(json);
	}

	public ItemValue getValue() {
		return value;
	}

	public void setValue(ItemValue value) {
		this.value = value;
	}

	public void setValue(
		UnsafeSupplier<ItemValue, Exception> valueUnsafeSupplier) {

		try {
			value = valueUnsafeSupplier.get();
		}
		catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	protected ItemValue value;

	public Map<String, ItemValue> getValue_i18n() {
		return value_i18n;
	}

	public void setValue_i18n(Map<String, ItemValue> value_i18n) {
		this.value_i18n = value_i18n;
	}

	public void setValue_i18n(
		UnsafeSupplier<Map<String, ItemValue>, Exception>
			value_i18nUnsafeSupplier) {

		try {
			value_i18n = value_i18nUnsafeSupplier.get();
		}
		catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	protected Map<String, ItemValue> value_i18n;

	@Override
	public ItemConfigurationFieldValue clone()
		throws CloneNotSupportedException {

		return (ItemConfigurationFieldValue)super.clone();
	}

	@Override
	public boolean equals(Object object) {
		if (this == object) {
			return true;
		}

		if (!(object instanceof ItemConfigurationFieldValue)) {
			return false;
		}

		ItemConfigurationFieldValue itemConfigurationFieldValue =
			(ItemConfigurationFieldValue)object;

		return Objects.equals(
			toString(), itemConfigurationFieldValue.toString());
	}

	@Override
	public int hashCode() {
		String string = toString();

		return string.hashCode();
	}

	public String toString() {
		return ItemConfigurationFieldValueSerDes.toJSON(this);
	}

}