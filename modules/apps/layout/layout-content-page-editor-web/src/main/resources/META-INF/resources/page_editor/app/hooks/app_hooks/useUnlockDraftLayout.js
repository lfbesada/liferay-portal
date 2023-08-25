/**
 * SPDX-FileCopyrightText: (c) 2000 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

import {useEffect} from 'react';

import {useDispatch} from '../../contexts/StoreContext';
import unlockDraftLayout from '../../thunks/unlockDraftLayout';

export default function useUnlockDraftLayout() {
	const dispatch = useDispatch();

	useEffect(() => {
		const beforeUnloadHandle = () => {
			dispatch(unlockDraftLayout());
		};

		window.addEventListener('beforeunload', beforeUnloadHandle);

		return () => {
			window.removeEventListener('beforeunload', beforeUnloadHandle);
		};
	});
}
