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

import {createRenderURL, openSelectionModal} from 'frontend-js-web';

export default function propsTransformer({
	additionalProps,
	portletNamespace,
	...props
}) {
	return {
		...props,
		onClick() {
			const {url} = additionalProps;

			const renderURL = createRenderURL(url, {
				faviconFileEntryId: getSelectedFaviconFileEntryId(
					portletNamespace
				),
			});

			openSelectionModal({
				buttonAddLabel: Liferay.Language.get('done'),
				iframeBodyCssClass: '',
				multiple: true,
				onSelect(selectedItem) {
					if (selectedItem) {
						const faviconFileEntryName = document.getElementById(
							`${portletNamespace}faviconFileEntryName`
						);

						faviconFileEntryName.innerHTML = selectedItem.name;

						const faviconFileEntryId = document.getElementById(
							`${portletNamespace}faviconFileEntryId`
						);

						faviconFileEntryId.value =
							selectedItem.faviconfileentryid;
					}
				},
				selectEventName: `${portletNamespace}selectFavicon`,
				title: Liferay.Language.get('select-favicon'),
				url: renderURL.toString(),
			});
		},
	};
}

function getSelectedFaviconFileEntryId(portletNamespace) {
	const faviconFileEntryIdInput = document.getElementById(
		`${portletNamespace}faviconFileEntryId`
	);

	return faviconFileEntryIdInput ? faviconFileEntryIdInput.value : 0;
}
