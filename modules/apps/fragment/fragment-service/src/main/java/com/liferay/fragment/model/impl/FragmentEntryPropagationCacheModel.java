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

package com.liferay.fragment.model.impl;

import com.liferay.fragment.model.FragmentEntryPropagation;
import com.liferay.petra.lang.HashUtil;
import com.liferay.petra.string.StringBundler;
import com.liferay.portal.kernel.model.CacheModel;
import com.liferay.portal.kernel.model.MVCCModel;

import java.io.Externalizable;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;

import java.util.Date;

/**
 * The cache model class for representing FragmentEntryPropagation in entity cache.
 *
 * @author Brian Wing Shun Chan
 * @generated
 */
public class FragmentEntryPropagationCacheModel
	implements CacheModel<FragmentEntryPropagation>, Externalizable, MVCCModel {

	@Override
	public boolean equals(Object object) {
		if (this == object) {
			return true;
		}

		if (!(object instanceof FragmentEntryPropagationCacheModel)) {
			return false;
		}

		FragmentEntryPropagationCacheModel fragmentEntryPropagationCacheModel =
			(FragmentEntryPropagationCacheModel)object;

		if ((fragmentEntryPropagationId ==
				fragmentEntryPropagationCacheModel.
					fragmentEntryPropagationId) &&
			(mvccVersion == fragmentEntryPropagationCacheModel.mvccVersion)) {

			return true;
		}

		return false;
	}

	@Override
	public int hashCode() {
		int hashCode = HashUtil.hash(0, fragmentEntryPropagationId);

		return HashUtil.hash(hashCode, mvccVersion);
	}

	@Override
	public long getMvccVersion() {
		return mvccVersion;
	}

	@Override
	public void setMvccVersion(long mvccVersion) {
		this.mvccVersion = mvccVersion;
	}

	@Override
	public String toString() {
		StringBundler sb = new StringBundler(23);

		sb.append("{mvccVersion=");
		sb.append(mvccVersion);
		sb.append(", ctCollectionId=");
		sb.append(ctCollectionId);
		sb.append(", fragmentEntryPropagationId=");
		sb.append(fragmentEntryPropagationId);
		sb.append(", createDate=");
		sb.append(createDate);
		sb.append(", modifiedDate=");
		sb.append(modifiedDate);
		sb.append(", fragmentEntryKey=");
		sb.append(fragmentEntryKey);
		sb.append(", css=");
		sb.append(css);
		sb.append(", html=");
		sb.append(html);
		sb.append(", js=");
		sb.append(js);
		sb.append(", configuration=");
		sb.append(configuration);
		sb.append(", type=");
		sb.append(type);
		sb.append("}");

		return sb.toString();
	}

	@Override
	public FragmentEntryPropagation toEntityModel() {
		FragmentEntryPropagationImpl fragmentEntryPropagationImpl =
			new FragmentEntryPropagationImpl();

		fragmentEntryPropagationImpl.setMvccVersion(mvccVersion);
		fragmentEntryPropagationImpl.setCtCollectionId(ctCollectionId);
		fragmentEntryPropagationImpl.setFragmentEntryPropagationId(
			fragmentEntryPropagationId);

		if (createDate == Long.MIN_VALUE) {
			fragmentEntryPropagationImpl.setCreateDate(null);
		}
		else {
			fragmentEntryPropagationImpl.setCreateDate(new Date(createDate));
		}

		if (modifiedDate == Long.MIN_VALUE) {
			fragmentEntryPropagationImpl.setModifiedDate(null);
		}
		else {
			fragmentEntryPropagationImpl.setModifiedDate(
				new Date(modifiedDate));
		}

		if (fragmentEntryKey == null) {
			fragmentEntryPropagationImpl.setFragmentEntryKey("");
		}
		else {
			fragmentEntryPropagationImpl.setFragmentEntryKey(fragmentEntryKey);
		}

		if (css == null) {
			fragmentEntryPropagationImpl.setCss("");
		}
		else {
			fragmentEntryPropagationImpl.setCss(css);
		}

		if (html == null) {
			fragmentEntryPropagationImpl.setHtml("");
		}
		else {
			fragmentEntryPropagationImpl.setHtml(html);
		}

		if (js == null) {
			fragmentEntryPropagationImpl.setJs("");
		}
		else {
			fragmentEntryPropagationImpl.setJs(js);
		}

		if (configuration == null) {
			fragmentEntryPropagationImpl.setConfiguration("");
		}
		else {
			fragmentEntryPropagationImpl.setConfiguration(configuration);
		}

		fragmentEntryPropagationImpl.setType(type);

		fragmentEntryPropagationImpl.resetOriginalValues();

		return fragmentEntryPropagationImpl;
	}

	@Override
	public void readExternal(ObjectInput objectInput) throws IOException {
		mvccVersion = objectInput.readLong();

		ctCollectionId = objectInput.readLong();

		fragmentEntryPropagationId = objectInput.readLong();
		createDate = objectInput.readLong();
		modifiedDate = objectInput.readLong();
		fragmentEntryKey = objectInput.readUTF();
		css = objectInput.readUTF();
		html = objectInput.readUTF();
		js = objectInput.readUTF();
		configuration = objectInput.readUTF();

		type = objectInput.readInt();
	}

	@Override
	public void writeExternal(ObjectOutput objectOutput) throws IOException {
		objectOutput.writeLong(mvccVersion);

		objectOutput.writeLong(ctCollectionId);

		objectOutput.writeLong(fragmentEntryPropagationId);
		objectOutput.writeLong(createDate);
		objectOutput.writeLong(modifiedDate);

		if (fragmentEntryKey == null) {
			objectOutput.writeUTF("");
		}
		else {
			objectOutput.writeUTF(fragmentEntryKey);
		}

		if (css == null) {
			objectOutput.writeUTF("");
		}
		else {
			objectOutput.writeUTF(css);
		}

		if (html == null) {
			objectOutput.writeUTF("");
		}
		else {
			objectOutput.writeUTF(html);
		}

		if (js == null) {
			objectOutput.writeUTF("");
		}
		else {
			objectOutput.writeUTF(js);
		}

		if (configuration == null) {
			objectOutput.writeUTF("");
		}
		else {
			objectOutput.writeUTF(configuration);
		}

		objectOutput.writeInt(type);
	}

	public long mvccVersion;
	public long ctCollectionId;
	public long fragmentEntryPropagationId;
	public long createDate;
	public long modifiedDate;
	public String fragmentEntryKey;
	public String css;
	public String html;
	public String js;
	public String configuration;
	public int type;

}