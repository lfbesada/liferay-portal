/**
 * SPDX-FileCopyrightText: (c) 2000 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

import {Action} from '../../plugins/page_rules/components/Action';
import {Condition} from '../../plugins/page_rules/components/Condition';
import {LayoutData} from '../../types/layout_data/LayoutData';

/**
 * Add a rule
 */
declare type AddRuleProps = {
	actions: Action[];
	conditions: Condition[];
	name: string;
	onNetworkStatus: () => {};
	segmentsExperienceId: string;
};
declare function addRule({
	actions,
	conditions,
	name,
	onNetworkStatus,
	segmentsExperienceId,
}: AddRuleProps): Promise<
	Array<{
		layoutData: LayoutData;
	}>
>;

/**
 * Delete a rule
 */
declare type DeleteRuleProps = {
	onNetworkStatus: () => {};
	ruleId: string;
	segmentsExperienceId: string;
};
declare function deleteRule({
	onNetworkStatus,
	ruleId,
	segmentsExperienceId,
}: DeleteRuleProps): Promise<
	Array<{
		layoutData: LayoutData;
	}>
>;

/**
 * Get users
 */
declare function getUsers(): Promise<
	Array<{
		screenName: string;
		userId: string;
	}>
>;

/**
 * Update a rule with new name, actions and conditions
 */
declare type UpdateRuleProps = {
	actions: Action[];
	conditions: Condition[];
	name: string;
	onNetworkStatus: () => {};
	ruleId: string;
	segmentsExperienceId: string;
};
declare function updateRule({
	actions,
	conditions,
	name,
	onNetworkStatus,
	ruleId,
	segmentsExperienceId,
}: UpdateRuleProps): Promise<
	Array<{
		layoutData: LayoutData;
	}>
>;
declare const _default: {
	addRule: typeof addRule;
	deleteRule: typeof deleteRule;
	getUsers: typeof getUsers;
	updateRule: typeof updateRule;
};
export default _default;
