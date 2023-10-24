/**
 * SPDX-FileCopyrightText: (c) 2023 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.layout.util.structure;

import com.liferay.petra.lang.HashUtil;
import com.liferay.portal.kernel.json.JSONObject;
import com.liferay.portal.kernel.json.JSONUtil;

import java.util.Objects;

/**
 * @author Lourdes Fernández Besada
 */
public class LayoutStructureRule {

	public static LayoutStructureRule of(JSONObject jsonObject) {
		return new LayoutStructureRule(
			jsonObject.getString("name"), jsonObject.getString("ruleId"));
	}

	public LayoutStructureRule(String name, String ruleId) {
		_name = name;
		_ruleId = ruleId;
	}

	@Override
	public boolean equals(Object object) {
		if (this == object) {
			return true;
		}

		if (!(object instanceof LayoutStructureRule)) {
			return false;
		}

		LayoutStructureRule layoutStructureRule = (LayoutStructureRule)object;

		if (Objects.equals(_name, layoutStructureRule._name) &&
			Objects.equals(_ruleId, layoutStructureRule._ruleId)) {

			return true;
		}

		return false;
	}

	public String getName() {
		return _name;
	}

	public String getRuleId() {
		return _ruleId;
	}

	@Override
	public int hashCode() {
		return HashUtil.hash(0, getRuleId());
	}

	public void setName(String name) {
		_name = name;
	}

	public void setRuleId(String ruleId) {
		_ruleId = ruleId;
	}

	public JSONObject toJSONObject() {
		return JSONUtil.put(
			"name", getName()
		).put(
			"ruleId", getRuleId()
		);
	}

	@Override
	public String toString() {
		JSONObject jsonObject = toJSONObject();

		return jsonObject.toString();
	}

	private String _name;
	private String _ruleId;

}