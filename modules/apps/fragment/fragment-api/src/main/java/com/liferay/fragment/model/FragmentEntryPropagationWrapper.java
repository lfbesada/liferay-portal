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

package com.liferay.fragment.model;

import com.liferay.portal.kernel.model.ModelWrapper;
import com.liferay.portal.kernel.model.wrapper.BaseModelWrapper;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.BiConsumer;
import java.util.function.Function;

/**
 * <p>
 * This class is a wrapper for {@link FragmentEntryPropagation}.
 * </p>
 *
 * @author Brian Wing Shun Chan
 * @see FragmentEntryPropagation
 * @generated
 */
public class FragmentEntryPropagationWrapper
	extends BaseModelWrapper<FragmentEntryPropagation>
	implements FragmentEntryPropagation,
			   ModelWrapper<FragmentEntryPropagation> {

	public FragmentEntryPropagationWrapper(
		FragmentEntryPropagation fragmentEntryPropagation) {

		super(fragmentEntryPropagation);
	}

	@Override
	public Map<String, Object> getModelAttributes() {
		Map<String, Object> attributes = new HashMap<String, Object>();

		attributes.put("mvccVersion", getMvccVersion());
		attributes.put("ctCollectionId", getCtCollectionId());
		attributes.put(
			"fragmentEntryPropagationId", getFragmentEntryPropagationId());
		attributes.put("createDate", getCreateDate());
		attributes.put("modifiedDate", getModifiedDate());
		attributes.put("fragmentEntryKey", getFragmentEntryKey());
		attributes.put("css", getCss());
		attributes.put("html", getHtml());
		attributes.put("js", getJs());
		attributes.put("configuration", getConfiguration());
		attributes.put("type", getType());

		return attributes;
	}

	@Override
	public void setModelAttributes(Map<String, Object> attributes) {
		Long mvccVersion = (Long)attributes.get("mvccVersion");

		if (mvccVersion != null) {
			setMvccVersion(mvccVersion);
		}

		Long ctCollectionId = (Long)attributes.get("ctCollectionId");

		if (ctCollectionId != null) {
			setCtCollectionId(ctCollectionId);
		}

		Long fragmentEntryPropagationId = (Long)attributes.get(
			"fragmentEntryPropagationId");

		if (fragmentEntryPropagationId != null) {
			setFragmentEntryPropagationId(fragmentEntryPropagationId);
		}

		Date createDate = (Date)attributes.get("createDate");

		if (createDate != null) {
			setCreateDate(createDate);
		}

		Date modifiedDate = (Date)attributes.get("modifiedDate");

		if (modifiedDate != null) {
			setModifiedDate(modifiedDate);
		}

		String fragmentEntryKey = (String)attributes.get("fragmentEntryKey");

		if (fragmentEntryKey != null) {
			setFragmentEntryKey(fragmentEntryKey);
		}

		String css = (String)attributes.get("css");

		if (css != null) {
			setCss(css);
		}

		String html = (String)attributes.get("html");

		if (html != null) {
			setHtml(html);
		}

		String js = (String)attributes.get("js");

		if (js != null) {
			setJs(js);
		}

		String configuration = (String)attributes.get("configuration");

		if (configuration != null) {
			setConfiguration(configuration);
		}

		Integer type = (Integer)attributes.get("type");

		if (type != null) {
			setType(type);
		}
	}

	@Override
	public FragmentEntryPropagation cloneWithOriginalValues() {
		return wrap(model.cloneWithOriginalValues());
	}

	/**
	 * Returns the configuration of this fragment entry propagation.
	 *
	 * @return the configuration of this fragment entry propagation
	 */
	@Override
	public String getConfiguration() {
		return model.getConfiguration();
	}

	/**
	 * Returns the create date of this fragment entry propagation.
	 *
	 * @return the create date of this fragment entry propagation
	 */
	@Override
	public Date getCreateDate() {
		return model.getCreateDate();
	}

	/**
	 * Returns the css of this fragment entry propagation.
	 *
	 * @return the css of this fragment entry propagation
	 */
	@Override
	public String getCss() {
		return model.getCss();
	}

	/**
	 * Returns the ct collection ID of this fragment entry propagation.
	 *
	 * @return the ct collection ID of this fragment entry propagation
	 */
	@Override
	public long getCtCollectionId() {
		return model.getCtCollectionId();
	}

	/**
	 * Returns the fragment entry key of this fragment entry propagation.
	 *
	 * @return the fragment entry key of this fragment entry propagation
	 */
	@Override
	public String getFragmentEntryKey() {
		return model.getFragmentEntryKey();
	}

	/**
	 * Returns the fragment entry propagation ID of this fragment entry propagation.
	 *
	 * @return the fragment entry propagation ID of this fragment entry propagation
	 */
	@Override
	public long getFragmentEntryPropagationId() {
		return model.getFragmentEntryPropagationId();
	}

	/**
	 * Returns the html of this fragment entry propagation.
	 *
	 * @return the html of this fragment entry propagation
	 */
	@Override
	public String getHtml() {
		return model.getHtml();
	}

	/**
	 * Returns the js of this fragment entry propagation.
	 *
	 * @return the js of this fragment entry propagation
	 */
	@Override
	public String getJs() {
		return model.getJs();
	}

	/**
	 * Returns the modified date of this fragment entry propagation.
	 *
	 * @return the modified date of this fragment entry propagation
	 */
	@Override
	public Date getModifiedDate() {
		return model.getModifiedDate();
	}

	/**
	 * Returns the mvcc version of this fragment entry propagation.
	 *
	 * @return the mvcc version of this fragment entry propagation
	 */
	@Override
	public long getMvccVersion() {
		return model.getMvccVersion();
	}

	/**
	 * Returns the primary key of this fragment entry propagation.
	 *
	 * @return the primary key of this fragment entry propagation
	 */
	@Override
	public long getPrimaryKey() {
		return model.getPrimaryKey();
	}

	/**
	 * Returns the type of this fragment entry propagation.
	 *
	 * @return the type of this fragment entry propagation
	 */
	@Override
	public int getType() {
		return model.getType();
	}

	@Override
	public void persist() {
		model.persist();
	}

	/**
	 * Sets the configuration of this fragment entry propagation.
	 *
	 * @param configuration the configuration of this fragment entry propagation
	 */
	@Override
	public void setConfiguration(String configuration) {
		model.setConfiguration(configuration);
	}

	/**
	 * Sets the create date of this fragment entry propagation.
	 *
	 * @param createDate the create date of this fragment entry propagation
	 */
	@Override
	public void setCreateDate(Date createDate) {
		model.setCreateDate(createDate);
	}

	/**
	 * Sets the css of this fragment entry propagation.
	 *
	 * @param css the css of this fragment entry propagation
	 */
	@Override
	public void setCss(String css) {
		model.setCss(css);
	}

	/**
	 * Sets the ct collection ID of this fragment entry propagation.
	 *
	 * @param ctCollectionId the ct collection ID of this fragment entry propagation
	 */
	@Override
	public void setCtCollectionId(long ctCollectionId) {
		model.setCtCollectionId(ctCollectionId);
	}

	/**
	 * Sets the fragment entry key of this fragment entry propagation.
	 *
	 * @param fragmentEntryKey the fragment entry key of this fragment entry propagation
	 */
	@Override
	public void setFragmentEntryKey(String fragmentEntryKey) {
		model.setFragmentEntryKey(fragmentEntryKey);
	}

	/**
	 * Sets the fragment entry propagation ID of this fragment entry propagation.
	 *
	 * @param fragmentEntryPropagationId the fragment entry propagation ID of this fragment entry propagation
	 */
	@Override
	public void setFragmentEntryPropagationId(long fragmentEntryPropagationId) {
		model.setFragmentEntryPropagationId(fragmentEntryPropagationId);
	}

	/**
	 * Sets the html of this fragment entry propagation.
	 *
	 * @param html the html of this fragment entry propagation
	 */
	@Override
	public void setHtml(String html) {
		model.setHtml(html);
	}

	/**
	 * Sets the js of this fragment entry propagation.
	 *
	 * @param js the js of this fragment entry propagation
	 */
	@Override
	public void setJs(String js) {
		model.setJs(js);
	}

	/**
	 * Sets the modified date of this fragment entry propagation.
	 *
	 * @param modifiedDate the modified date of this fragment entry propagation
	 */
	@Override
	public void setModifiedDate(Date modifiedDate) {
		model.setModifiedDate(modifiedDate);
	}

	/**
	 * Sets the mvcc version of this fragment entry propagation.
	 *
	 * @param mvccVersion the mvcc version of this fragment entry propagation
	 */
	@Override
	public void setMvccVersion(long mvccVersion) {
		model.setMvccVersion(mvccVersion);
	}

	/**
	 * Sets the primary key of this fragment entry propagation.
	 *
	 * @param primaryKey the primary key of this fragment entry propagation
	 */
	@Override
	public void setPrimaryKey(long primaryKey) {
		model.setPrimaryKey(primaryKey);
	}

	/**
	 * Sets the type of this fragment entry propagation.
	 *
	 * @param type the type of this fragment entry propagation
	 */
	@Override
	public void setType(int type) {
		model.setType(type);
	}

	@Override
	public String toXmlString() {
		return model.toXmlString();
	}

	@Override
	public Map<String, Function<FragmentEntryPropagation, Object>>
		getAttributeGetterFunctions() {

		return model.getAttributeGetterFunctions();
	}

	@Override
	public Map<String, BiConsumer<FragmentEntryPropagation, Object>>
		getAttributeSetterBiConsumers() {

		return model.getAttributeSetterBiConsumers();
	}

	@Override
	protected FragmentEntryPropagationWrapper wrap(
		FragmentEntryPropagation fragmentEntryPropagation) {

		return new FragmentEntryPropagationWrapper(fragmentEntryPropagation);
	}

}