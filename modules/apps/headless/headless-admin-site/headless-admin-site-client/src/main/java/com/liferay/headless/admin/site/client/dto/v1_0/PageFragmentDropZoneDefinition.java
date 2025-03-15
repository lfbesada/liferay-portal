/**
 * SPDX-FileCopyrightText: (c) 2024 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.headless.admin.site.client.dto.v1_0;

import com.liferay.headless.admin.site.client.function.UnsafeSupplier;
import com.liferay.headless.admin.site.client.serdes.v1_0.PageFragmentDropZoneDefinitionSerDes;

import java.io.Serializable;

import java.util.Objects;

import javax.annotation.Generated;

/**
 * @author Rubén Pulido
 * @generated
 */
@Generated("")
public class PageFragmentDropZoneDefinition implements Cloneable, Serializable {

	public static PageFragmentDropZoneDefinition toDTO(String json) {
		return PageFragmentDropZoneDefinitionSerDes.toDTO(json);
	}

	public String getFragmentDropZoneId() {
		return fragmentDropZoneId;
	}

	public void setFragmentDropZoneId(String fragmentDropZoneId) {
		this.fragmentDropZoneId = fragmentDropZoneId;
	}

	public void setFragmentDropZoneId(
		UnsafeSupplier<String, Exception> fragmentDropZoneIdUnsafeSupplier) {

		try {
			fragmentDropZoneId = fragmentDropZoneIdUnsafeSupplier.get();
		}
		catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	protected String fragmentDropZoneId;

	public Type getType() {
		return type;
	}

	public String getTypeAsString() {
		if (type == null) {
			return null;
		}

		return type.toString();
	}

	public void setType(Type type) {
		this.type = type;
	}

	public void setType(UnsafeSupplier<Type, Exception> typeUnsafeSupplier) {
		try {
			type = typeUnsafeSupplier.get();
		}
		catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	protected Type type;

	@Override
	public PageFragmentDropZoneDefinition clone()
		throws CloneNotSupportedException {

		return (PageFragmentDropZoneDefinition)super.clone();
	}

	@Override
	public boolean equals(Object object) {
		if (this == object) {
			return true;
		}

		if (!(object instanceof PageFragmentDropZoneDefinition)) {
			return false;
		}

		PageFragmentDropZoneDefinition pageFragmentDropZoneDefinition =
			(PageFragmentDropZoneDefinition)object;

		return Objects.equals(
			toString(), pageFragmentDropZoneDefinition.toString());
	}

	@Override
	public int hashCode() {
		String string = toString();

		return string.hashCode();
	}

	public String toString() {
		return PageFragmentDropZoneDefinitionSerDes.toJSON(this);
	}

	public static enum Type {

		FRAGMENT_DROP_ZONE_DEFINITION("FragmentDropZoneDefinition");

		public static Type create(String value) {
			for (Type type : values()) {
				if (Objects.equals(type.getValue(), value) ||
					Objects.equals(type.name(), value)) {

					return type;
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

		private Type(String value) {
			_value = value;
		}

		private final String _value;

	}

}