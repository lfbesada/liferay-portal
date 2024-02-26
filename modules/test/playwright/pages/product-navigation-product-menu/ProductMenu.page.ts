/**
 * SPDX-FileCopyrightText: (c) 2000 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

import {Locator, Page} from '@playwright/test';

export class ProductMenuPage {
	readonly page: Page;

	readonly closeProductMenuButton: Locator;
	readonly configurationMenuItem: Locator;
	readonly contentAndDataMenuItem: Locator;
	readonly documentsAndMediaMenuItem: Locator;
	readonly journalMenuItem: Locator;
	readonly knowledgeBaseMenuItem: Locator;
	readonly openProductMenuButton: Locator;
	readonly pagesMenuItem: Locator;
	readonly siteBuilderMenuItem: Locator;

	constructor(page: Page) {
		this.page = page;

		this.closeProductMenuButton = page.getByLabel('Close Product Menu');
		this.configurationMenuItem = page.getByRole('menuitem', {
			exact: true,
			name: 'Configuration',
		});
		this.contentAndDataMenuItem = page.getByRole('menuitem', {
			exact: true,
			name: 'Content & Data',
		});
		this.documentsAndMediaMenuItem = page.getByRole('menuitem', {
			name: 'Documents and Media',
		});
		this.journalMenuItem = page.getByRole('menuitem', {
			name: 'Web Content',
		});
		this.knowledgeBaseMenuItem = page.getByRole('menuitem', {
			exact: true,
			name: 'Knowledge Base',
		});
		this.openProductMenuButton = page.getByLabel('Open Product Menu');
		this.pagesMenuItem = page.getByRole('menuitem', {
			name: 'Pages',
		});
		this.siteBuilderMenuItem = page.getByRole('menuitem', {
			exact: true,
			name: 'Site Builder',
		});
	}

	async closeProductMenu(siteFriendlyUrlPath: string = '/guest') {
		await this.goto(siteFriendlyUrlPath);

		if (await this.closeProductMenuButton.isVisible()) {
			await this.closeProductMenuButton.click();
		}
	}

	async goto(siteFriendlyUrlPath: string = '/guest') {
		await this.page.goto(`/group${siteFriendlyUrlPath}`);
	}

	async goToConfiguration(siteFriendlyUrlPath: string = '/guest') {
		await this.openProductMenu(siteFriendlyUrlPath);
		const isClosed =
			(await this.configurationMenuItem.getAttribute('aria-expanded')) ===
			'false';

		if (isClosed) {
			await this.configurationMenuItem.click();
		}
	}

	async goToContentAndData(siteFriendlyUrlPath: string = '/guest') {
		await this.openProductMenu(siteFriendlyUrlPath);
		const isClosed =
			(await this.contentAndDataMenuItem.getAttribute(
				'aria-expanded'
			)) === 'false';

		if (isClosed) {
			await this.contentAndDataMenuItem.click();
		}
	}

	async goToDocumentsAndMediaMenuItem(siteFriendlyUrlPath: string = '/guest') {
		await this.goToContentAndData(siteFriendlyUrlPath);
		await this.documentsAndMediaMenuItem.click();
	}

	async goToJournalMenuItem(siteFriendlyUrlPath: string = '/guest') {
		await this.goToContentAndData(siteFriendlyUrlPath);
		await this.journalMenuItem.click();
	}

	async goToKnowledgeBaseMenuItem(siteFriendlyUrlPath: string = '/guest') {
		await this.goToContentAndData(siteFriendlyUrlPath);
		await this.knowledgeBaseMenuItem.click();
	}

	async goToPagesMenuItem(siteFriendlyUrlPath: string = '/guest') {
		await this.goToSiteBuilder(siteFriendlyUrlPath);
		await this.pagesMenuItem.click();
	}

	async goToSiteBuilder(siteFriendlyUrlPath: string = '/guest') {
		await this.openProductMenu(siteFriendlyUrlPath);
		const isClosed =
			(await this.siteBuilderMenuItem.getAttribute(
				'aria-expanded'
			)) === 'false';

		if (isClosed) {
			await this.siteBuilderMenuItem.click();
		}
	}

	async openProductMenu(siteFriendlyUrlPath: string = '/guest') {
		await this.goto(siteFriendlyUrlPath);

		if (await this.openProductMenuButton.isVisible()) {
			await this.openProductMenuButton.click();
		}
	}
}
