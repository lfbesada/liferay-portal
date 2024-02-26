/**
 * SPDX-FileCopyrightText: (c) 2000 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

import {expect, mergeTests} from '@playwright/test';

import {apiHelpersTest} from '../../fixtures/apiHelpersTest';
import {applicationsMenuPageTest} from '../../fixtures/applicationsMenuPageTest';
import {loginTest} from '../../fixtures/loginTest';
import getRandomString from '../../utils/getRandomString';
import {pageEditorPagesTest} from '../layout-content-page-editor-web/fixtures/pageEditorPagesTest';
import {pagesPagesTest} from './fixtures/pagesPagesTest';

export const test = mergeTests(
	apiHelpersTest,
	applicationsMenuPageTest,
	loginTest(),
	pageEditorPagesTest,
	pagesPagesTest
);

test('LPD-4459: The configuration action of an Utility Page should be accesible from the card dropdown actions', async ({
	apiHelpers,
	page,
	pageEditorPage,
	utilityPageConfigurationPage,
	utilityPagesPage,
}) => {
	await page.goto('/');

	// Create a site

	const site = await apiHelpers.headlessSite.createSite(getRandomString());

	await utilityPageConfigurationPage.setUtilityPageConfiguration(
		getRandomString(),
		getRandomString(),
		'404 Error'
	);

	await expect(
		page.getByText('The page was updated successfully.')
	).toBeVisible();

	await utilityPagesPage.goToEdit('404 Error');

	await pageEditorPage.goToSidebar('Page Design Options');

	await expect(
		page.getByRole('button', {exact: true, name: 'Master'})
	).toBeVisible();

	expect(
		await page
			.locator('.page-editor__sidebar__panel-header', {
				has: page.getByTitle('Page Design Options'),
			})
			.getByRole('link')
			.count()
	).toEqual(0);

	await apiHelpers.headlessSite.deleteSite(site.id);
});
