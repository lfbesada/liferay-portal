/**
 * SPDX-FileCopyrightText: (c) 2026 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

import {FragmentSetModalContent} from '@liferay/layout-js-components-web';
import {openToast} from 'frontend-js-components-web';
import {fetch} from 'frontend-js-web';
import React from 'react';

type FragmentCollection = {fragmentCollectionId: number; name: string};

interface Props {
	addFragmentCollectionURL: string;
	addFragmentEntryURL: string;
	backURL: string;
	fragmentCollections: Array<FragmentCollection>;
	fragmentType: number;
	mode: 'fragment' | 'set';
	namespace: string;
}

const closeParentModal = () => {
	Liferay.Util.getOpener()?.Liferay?.fire('closeModal');
};

const navigateOpener = (redirectURL: string, fallbackURL: string) => {
	const opener = Liferay.Util.getOpener();

	if (!opener?.location) {
		return;
	}

	const targetURL = redirectURL || fallbackURL;

	if (targetURL && targetURL !== opener.location.href) {
		opener.location.href = targetURL;
	}
	else {
		opener.location.reload();
	}
};

const DesignLibraryAddFragmentForm = ({
	addFragmentCollectionURL,
	addFragmentEntryURL,
	backURL,
	fragmentCollections,
	fragmentType,
	mode,
	namespace,
}: Props) => {
	const submitFragmentEntry = (
		fragmentCollectionId: number,
		fragmentName?: string
	) => {
		const formData = new FormData();

		formData.append(
			`${namespace}fragmentCollectionId`,
			String(fragmentCollectionId)
		);

		formData.append(`${namespace}name`, fragmentName ?? '');

		formData.append(`${namespace}type`, String(fragmentType));

		fetch(addFragmentEntryURL, {body: formData, method: 'POST'})
			.then((response) => response.json())
			.then(({redirectURL}: {redirectURL?: string}) => {
				navigateOpener(redirectURL ?? '', backURL);
			})
			.catch(() =>
				openToast({
					message: Liferay.Language.get(
						'an-unexpected-error-occurred'
					),
					type: 'danger',
				})
			);
	};

	const notifyFragmentSetCreated = () => {
		navigateOpener('', backURL);
	};

	return (
		<FragmentSetModalContent
			addFragmentCollectionURL={addFragmentCollectionURL}
			allowCustomName={mode === 'fragment'}
			closeModal={closeParentModal}
			fragmentCollections={
				mode === 'fragment' ? fragmentCollections : []
			}
			onSubmitFragmentCollection={
				mode === 'fragment'
					? submitFragmentEntry
					: notifyFragmentSetCreated
			}
			portletNamespace={namespace}
		/>
	);
};

export default DesignLibraryAddFragmentForm;
