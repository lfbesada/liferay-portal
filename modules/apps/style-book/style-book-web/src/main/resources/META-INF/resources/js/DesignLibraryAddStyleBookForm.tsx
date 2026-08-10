/**
 * SPDX-FileCopyrightText: (c) 2026 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

import React from 'react';

import AddStyleBookModalContent from './AddStyleBookModalContent';

type FrontendTokenDefinitionProvider = {name: string; themeId: string};

interface Props {
	addStyleBookEntryURL: string;
	frontendTokenDefinitionProviders?: Array<FrontendTokenDefinitionProvider>;
	namespace: string;
}

const closeParentModal = () => {
	Liferay.Util.getOpener()?.Liferay?.fire('closeModal');
};

const navigateOpener = (redirectURL: string, closeModal: () => void) => {
	const opener = Liferay.Util.getOpener();

	closeModal();

	if (opener?.location) {
		if (opener.location.href === redirectURL) {
			opener.location.reload();
		}
		else {
			opener.location.href = redirectURL;
		}
	}
};

const DesignLibraryAddStyleBookForm = (props: Props) => (
	<AddStyleBookModalContent
		{...props}
		closeModal={closeParentModal}
		onSuccess={navigateOpener}
	/>
);

export default DesignLibraryAddStyleBookForm;
