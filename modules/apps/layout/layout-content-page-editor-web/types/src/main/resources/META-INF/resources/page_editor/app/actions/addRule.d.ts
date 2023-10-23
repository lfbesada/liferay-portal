/**
 * SPDX-FileCopyrightText: (c) 2000 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

import {LayoutData} from '../../types/layout_data/LayoutData';
declare type Props = {
	layoutData: LayoutData;
	name: string;
	ruleId: string;
};
export default function addRule({
	layoutData,
	name,
	ruleId,
}: Props): {
	readonly layoutData: LayoutData;
	readonly name: string;
	readonly ruleId: string;
	readonly type: 'ADD_RULE';
};
export {};
