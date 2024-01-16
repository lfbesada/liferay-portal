/**
 * SPDX-FileCopyrightText: (c) 2000 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

import {useEffect, useState} from 'react';

import updatePageContents from '../actions/updatePageContents';
import {useDispatch, useSelector} from '../contexts/StoreContext';
import {selectPageContents} from '../selectors/selectPageContents';
import selectSegmentsExperienceId from '../selectors/selectSegmentsExperienceId';
import InfoItemService from '../services/InfoItemService';

export default function usePageContents() {
	const dispatch = useDispatch();
	const pageContents = useSelector(selectPageContents);
	const segmentsExperienceId = useSelector(selectSegmentsExperienceId);

	const [contents, setContents] = useState(pageContents);

	useEffect(() => {
		if (!pageContents) {
			InfoItemService.getPageContents({
				segmentsExperienceId,
			}).then((pageContents) => {
				dispatch(
					updatePageContents({
						pageContents,
					})
				);

				setContents(pageContents);
			});
		}
	}, [contents, dispatch, pageContents, segmentsExperienceId]);

	return contents || [];
}
