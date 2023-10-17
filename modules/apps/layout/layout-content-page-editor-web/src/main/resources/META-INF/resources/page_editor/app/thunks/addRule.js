/**
 * SPDX-FileCopyrightText: (c) 2000 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

import addRuleAction from '../actions/addRule';
import RulesService from '../services/RulesService';

export default function addRule({actions, conditions, name}) {
	return (dispatch, getState) => {
		const {segmentsExperienceId} = getState();

		return RulesService.addRule({
			actions,
			conditions,
			name,
			onNetworkStatus: dispatch,
			segmentsExperienceId,
		}).then(({layoutData}) => {
			dispatch(addRuleAction({layoutData}));
		});
	};
}
