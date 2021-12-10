/**
 * Copyright (c) 2000-present Liferay, Inc. All rights reserved.
 *
 * This library is free software; you can redistribute it and/or modify it under
 * the terms of the GNU Lesser General Public License as published by the Free
 * Software Foundation; either version 2.1 of the License, or (at your option)
 * any later version.
 *
 * This library is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU Lesser General Public License for more
 * details.
 */

package com.liferay.info.item;

import com.liferay.petra.string.StringBundler;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

/**
 * @author Lourdes Fernández Besada
 */
public class InfoItemReferenceMetadata {

	public InfoItemReferenceMetadata(InfoItemReference infoItemReference) {
		_infoItemReference = infoItemReference;

		_data = new HashMap<>();
	}

	public InfoItemReferenceMetadata(
		Map<String, Object> data, InfoItemReference infoItemReference) {

		_data = data;
		_infoItemReference = infoItemReference;
	}

	@Override
	public boolean equals(Object object) {
		if (this == object) {
			return true;
		}

		if (!(object instanceof InfoItemReferenceMetadata)) {
			return false;
		}

		InfoItemReferenceMetadata infoItemReferenceMetadata =
			(InfoItemReferenceMetadata)object;

		if (Objects.equals(_data, infoItemReferenceMetadata._data) &&
			Objects.equals(
				_infoItemReference,
				infoItemReferenceMetadata._infoItemReference)) {

			return true;
		}

		return false;
	}

	public Map<String, Object> getData() {
		return _data;
	}

	public InfoItemReference getInfoItemReference() {
		return _infoItemReference;
	}

	@Override
	public int hashCode() {
		return Objects.hash(_data, _infoItemReference);
	}

	public void setData(Map<String, Object> data) {
		_data = data;
	}

	@Override
	public String toString() {
		return StringBundler.concat(
			"{data=", _data, ", _infoItemReference=", _infoItemReference, "}");
	}

	private Map<String, Object> _data;
	private final InfoItemReference _infoItemReference;

}