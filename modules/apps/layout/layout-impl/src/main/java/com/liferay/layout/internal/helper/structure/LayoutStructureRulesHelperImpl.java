/**
 * SPDX-FileCopyrightText: (c) 2023 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.layout.internal.helper.structure;

import com.liferay.dynamic.data.mapping.expression.CreateExpressionRequest;
import com.liferay.dynamic.data.mapping.expression.DDMExpression;
import com.liferay.dynamic.data.mapping.expression.DDMExpressionException;
import com.liferay.dynamic.data.mapping.expression.DDMExpressionFactory;
import com.liferay.dynamic.data.mapping.expression.DDMExpressionFieldAccessor;
import com.liferay.dynamic.data.mapping.expression.DDMExpressionParameterAccessor;
import com.liferay.dynamic.data.mapping.expression.GetFieldPropertyRequest;
import com.liferay.dynamic.data.mapping.expression.GetFieldPropertyResponse;
import com.liferay.layout.helper.structure.LayoutStructureRulesHelper;
import com.liferay.layout.util.structure.LayoutStructure;
import com.liferay.layout.util.structure.LayoutStructureRule;
import com.liferay.petra.string.StringPool;
import com.liferay.portal.kernel.json.JSONArray;
import com.liferay.portal.kernel.json.JSONFactoryUtil;
import com.liferay.portal.kernel.json.JSONObject;
import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.portal.kernel.model.User;
import com.liferay.portal.kernel.security.permission.PermissionChecker;
import com.liferay.portal.kernel.util.ArrayUtil;
import com.liferay.portal.kernel.util.HashMapBuilder;

import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

/**
 * @author Víctor Galán
 */
@Component(service = LayoutStructureRulesHelper.class)
public class LayoutStructureRulesHelperImpl
	implements LayoutStructureRulesHelper {

	@Override
	public LayoutStructureRulesResult processLayoutStructureRules(
		long groupId, LayoutStructure layoutStructure,
		PermissionChecker permissionChecker, long[] segmentsEntryIds) {

		Set<String> displayedItemIds = new HashSet<>();
		Set<String> hiddenItemIds = new HashSet<>();
		LayoutStructureRulesContext layoutStructureRulesContext =
			new LayoutStructureRulesContext(
				groupId, permissionChecker, segmentsEntryIds);

		for (LayoutStructureRule layoutStructureRule :
				layoutStructure.getLayoutStructureRules()) {

			if (!_isLayoutStructureRuleActive(
					layoutStructureRule, layoutStructureRulesContext)) {

				continue;
			}

			_processActions(
				layoutStructureRule.getActionsJSONArray(), displayedItemIds,
				hiddenItemIds);
		}

		return new LayoutStructureRulesResult(displayedItemIds, hiddenItemIds);
	}

	public class LayoutStructureRuleDDMExpressionFieldAccessor
		implements DDMExpressionFieldAccessor {

		public LayoutStructureRuleDDMExpressionFieldAccessor(
			long[] roleIds, long[] segmentsEntryIds, User user) {

			_values = HashMapBuilder.<String, Object>put(
				"createDate", user.getCreateDate()
			).put(
				"emailAddresses", user.getEmailAddresses()
			).put(
				"lastLoginDate", user.getLastLoginDate()
			).put(
				"modifiedDate", user.getModifiedDate()
			).put(
				"roleIds", roleIds
			).put(
				"screenName", user.getScreenName()
			).put(
				"segmentsEntryIds", segmentsEntryIds
			).put(
				"userId", user.getUserId()
			).build();
		}

		@Override
		public GetFieldPropertyResponse getFieldProperty(
			GetFieldPropertyRequest getFieldPropertyRequest) {

			Object value = _values.get(getFieldPropertyRequest.getField());

			if ((value == null) &&
				isField(getFieldPropertyRequest.getField())) {

				value = StringPool.BLANK;
			}

			GetFieldPropertyResponse.Builder builder =
				GetFieldPropertyResponse.Builder.newBuilder(value);

			return builder.build();
		}

		@Override
		public boolean isField(String parameter) {
			return _values.containsKey(parameter);
		}

		private final Map<String, Object> _values;

	}

	public class LayoutStructureRuleDDMExpressionParameterAccessor
		implements DDMExpressionParameterAccessor {

		public LayoutStructureRuleDDMExpressionParameterAccessor(
			long groupId, User user) {

			_groupId = groupId;

			_companyId = user.getCompanyId();

			_locale = user.getLocale();
			_timeZoneId = user.getTimeZoneId();
			_userId = user.getUserId();
		}

		@Override
		public long getCompanyId() {
			return _companyId;
		}

		@Override
		public String getGooglePlacesAPIKey() {
			return StringPool.BLANK;
		}

		@Override
		public long getGroupId() {
			return _groupId;
		}

		@Override
		public Locale getLocale() {
			return _locale;
		}

		@Override
		public JSONArray getObjectFieldsJSONArray() {
			return JSONFactoryUtil.createJSONArray();
		}

		@Override
		public String getTimeZoneId() {
			return _timeZoneId;
		}

		@Override
		public long getUserId() {
			return _userId;
		}

		private final long _companyId;
		private final long _groupId;
		private final Locale _locale;
		private final String _timeZoneId;
		private final long _userId;

	}

	private boolean _evaluateDDMExpression(
		String script,
		LayoutStructureRulesContext layoutStructureRulesContext) {

		try {
			DDMExpression<Boolean> ddmExpression =
				_ddmExpressionFactory.createExpression(
					CreateExpressionRequest.Builder.newBuilder(
						script
					).withDDMExpressionFieldAccessor(
						layoutStructureRulesContext.
							getDDMExpressionFieldAccessor()
					).withDDMExpressionParameterAccessor(
						layoutStructureRulesContext.
							getDDMExpressionParameterAccessor()
					).build());

			return ddmExpression.evaluate();
		}
		catch (DDMExpressionException ddmExpressionException) {
			_log.error(ddmExpressionException);
		}
		catch (Exception exception) {
			_log.error(exception);
		}

		return false;
	}

	private boolean _isConditionActive(
		JSONObject conditionJSONObject,
		LayoutStructureRulesContext layoutStructureRulesContext) {

		long value = conditionJSONObject.getLong("value");

		if (Objects.equals(
				conditionJSONObject.getString("condition"), "role")) {

			return ArrayUtil.contains(
				layoutStructureRulesContext.getRoleIds(), value);
		}

		if (Objects.equals(
				conditionJSONObject.getString("condition"), "segment")) {

			return ArrayUtil.contains(
				layoutStructureRulesContext.getSegmentsEntryIds(), value);
		}

		if (Objects.equals(
				conditionJSONObject.getString("condition"), "user") &&
			Objects.equals(layoutStructureRulesContext.getUserId(), value)) {

			return true;
		}

		return false;
	}

	private boolean _isLayoutStructureRuleActive(
		LayoutStructureRule layoutStructureRule,
		LayoutStructureRulesContext layoutStructureRulesContext) {

		if (layoutStructureRule.isAdvancedRule()) {
			return _evaluateDDMExpression(
				layoutStructureRule.getScript(), layoutStructureRulesContext);
		}

		JSONArray conditionsJSONArray =
			layoutStructureRule.getConditionsJSONArray();

		for (int i = 0; i < conditionsJSONArray.length(); i++) {
			JSONObject conditionJSONObject = conditionsJSONArray.getJSONObject(
				i);

			boolean conditionActive = _isConditionActive(
				conditionJSONObject, layoutStructureRulesContext);

			if (conditionActive) {
				if (Objects.equals(
						layoutStructureRule.getConditionType(), "any")) {

					return true;
				}
			}
			else if (Objects.equals(
						layoutStructureRule.getConditionType(), "all")) {

				return false;
			}
		}

		return true;
	}

	private void _processActions(
		JSONArray actionsJSONArray, Set<String> displayedItemIds,
		Set<String> hiddenItemIds) {

		for (int i = 0; i < actionsJSONArray.length(); i++) {
			JSONObject actionsJSONObject = actionsJSONArray.getJSONObject(i);

			if (Objects.equals(actionsJSONObject.getString("type"), "show")) {
				displayedItemIds.add(actionsJSONObject.getString("itemId"));
			}
			else {
				hiddenItemIds.add(actionsJSONObject.getString("itemId"));
			}
		}
	}

	private static final Log _log = LogFactoryUtil.getLog(
		LayoutStructureRulesHelperImpl.class);

	@Reference
	private DDMExpressionFactory _ddmExpressionFactory;

	private class LayoutStructureRulesContext {

		public DDMExpressionFieldAccessor getDDMExpressionFieldAccessor() {
			if (_ddmExpressionFieldAccessor != null) {
				return _ddmExpressionFieldAccessor;
			}

			_ddmExpressionFieldAccessor =
				new LayoutStructureRuleDDMExpressionFieldAccessor(
					getRoleIds(), getSegmentsEntryIds(),
					_permissionChecker.getUser());

			return _ddmExpressionFieldAccessor;
		}

		public DDMExpressionParameterAccessor
			getDDMExpressionParameterAccessor() {

			if (_ddmExpressionParameterAccessor != null) {
				return _ddmExpressionParameterAccessor;
			}

			_ddmExpressionParameterAccessor =
				new LayoutStructureRuleDDMExpressionParameterAccessor(
					_groupId, _permissionChecker.getUser());

			return _ddmExpressionParameterAccessor;
		}

		public long getGroupId() {
			return _groupId;
		}

		public PermissionChecker getPermissionChecker() {
			return _permissionChecker;
		}

		public long[] getRoleIds() {
			if (_roleIds != null) {
				return _roleIds;
			}

			_roleIds = _permissionChecker.getRoleIds(
				_permissionChecker.getUserId(), _groupId);

			return _roleIds;
		}

		public long[] getSegmentsEntryIds() {
			return _segmentsEntryIds;
		}

		public long getUserId() {
			return _permissionChecker.getUserId();
		}

		private LayoutStructureRulesContext(
			long groupId, PermissionChecker permissionChecker,
			long[] segmentsEntryIds) {

			_groupId = groupId;
			_permissionChecker = permissionChecker;
			_segmentsEntryIds = segmentsEntryIds;
		}

		private DDMExpressionFieldAccessor _ddmExpressionFieldAccessor;
		private DDMExpressionParameterAccessor _ddmExpressionParameterAccessor;
		private final long _groupId;
		private final PermissionChecker _permissionChecker;
		private long[] _roleIds;
		private final long[] _segmentsEntryIds;

	}

}