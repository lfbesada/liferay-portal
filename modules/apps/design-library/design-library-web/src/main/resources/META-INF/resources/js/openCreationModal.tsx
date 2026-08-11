/**
 * SPDX-FileCopyrightText: (c) 2026 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

import {openModal} from 'frontend-js-components-web';
import {loadModule} from 'frontend-js-web';
import React, {useEffect, useState} from 'react';

import {DesignLibraryResourceCreationItem} from './types';

function LazyModal({
	closeModal,
	module,
	moduleProps,
}: {
	closeModal: () => void;
	module: string;
	moduleProps: Record<string, unknown>;
}) {
	const [Component, setComponent] = useState<React.ComponentType<any> | null>(
		null
	);

	useEffect(() => {
		let cancelled = false;

		loadModule(module)
			.then((loaded: any) => {
				if (!cancelled) {
					setComponent(() => loaded.default ?? loaded);
				}
			})
			.catch((error: Error) => {
				console.error(
					`Unable to load creation modal from ${module}`,
					error
				);
			});

		return () => {
			cancelled = true;
		};
	}, [module]);

	if (!Component) {
		return null;
	}

	return <Component {...moduleProps} closeModal={closeModal} />;
}

export default function openCreationModal(
	designLibraryResourceCreationItem: DesignLibraryResourceCreationItem
) {
	openModal({
		contentComponent: ({closeModal}: {closeModal: () => void}) => (
			<LazyModal
				closeModal={closeModal}
				module={designLibraryResourceCreationItem.module}
				moduleProps={designLibraryResourceCreationItem.moduleProps}
			/>
		),
	});
}
