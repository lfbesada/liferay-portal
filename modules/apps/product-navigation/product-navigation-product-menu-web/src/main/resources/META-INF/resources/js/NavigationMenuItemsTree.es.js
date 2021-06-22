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

import {Treeview} from 'frontend-js-components-web';
import React from 'react';

import NavigationMenuItemsTreeNode from './NavigationMenuItemsTreeNode';

const INITIAL_NODES = [
	{
		children: [
			{
				id: '1.1',
				name: 'Pablictor',
			},
			{
				children: [
					{
						id: '1.2.1',
						name: 'Eudaldo',
					},
				],
				id: '1.2',
				name: 'Pabla',
			},
		],
		id: '1',
		name: 'Sandro',
	},
	{
		id: '2',
		name: 'Victor',
	},
	{
		children: [
			{
				id: '3.1',
				name: 'Straight line',
			},
		],
		expanded: true,
		id: '3',
		name: 'Juan',
	},
	{
		children: [
			{
				expanded: true,
				id: '4.1',
				name: 'Victor Son',
			},
		],
		id: '4',
		name: 'Victor Father',
	},
];

export default function NavigationMenuItemsTree({nodes = INITIAL_NODES}) {
	return (
		<div className="m-3 navigation-menu-items-tree">
			<Treeview
				NodeComponent={NavigationMenuItemsTreeNode}
				nodes={nodes}
			/>
		</div>
	);
}
