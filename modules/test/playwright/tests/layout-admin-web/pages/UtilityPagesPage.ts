/**
 * SPDX-FileCopyrightText: (c) 2000 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

// @ts-ignore

import {Locator, Page} from '@playwright/test';
import {clickAndExpectToBeVisible} from '../../../utils/clickAndExpectToBeVisible';
import {StaticPagesPage} from "../../layout-admin-web/pages/StaticPagesPage";

export class UtilityPagesPage {
	readonly page: Page;

	readonly staticPagesPage: StaticPagesPage;

	constructor(page: Page) {
		this.page = page;

		this.staticPagesPage = new StaticPagesPage(page);
	}

	async clickOnAction (action: string, title: string) {
		await clickAndExpectToBeVisible({
			autoClick: true,
			target: this.page.locator('a.dropdown-item', { has: this.page.getByLabel(action)}),
			trigger: this.page
				.locator('div.card-row', { has: this.page.getByTitle(title)})
				.getByRole('button'),
		});
	}

	async goTo() {
		await this.staticPagesPage.goToUtilityPages();

		// Do it twice so we decrease flakiness

		await this.staticPagesPage.goToUtilityPages();
	}

	async goToEdit(title: string) {
		await this.goTo();
		await this.clickOnAction("Edit", title);
	}
}
