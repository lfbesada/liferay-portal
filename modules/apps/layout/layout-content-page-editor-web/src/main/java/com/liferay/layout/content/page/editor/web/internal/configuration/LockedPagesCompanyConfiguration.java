/**
 * SPDX-FileCopyrightText: (c) 2023 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.layout.content.page.editor.web.internal.configuration;

import aQute.bnd.annotation.metatype.Meta;
import com.liferay.portal.configuration.metatype.annotations.ExtendedObjectClassDefinition;

/**
 * @author Lourdes Fernández Besada
 */
@ExtendedObjectClassDefinition(
	category = "pages", generateUI = false,
	scope = ExtendedObjectClassDefinition.Scope.COMPANY
)
@Meta.OCD(
	id = "com.liferay.layout.content.page.editor.web.internal.LockedPagesCompanyConfiguration",
	localization = "content/Language",
	name = "locked-pages"
)
public interface LockedPagesCompanyConfiguration {

	@Meta.AD(
		deflt = "true", name = "allow-automatic-unlocking-process",
		required = false
	)
	public boolean allowAutomaticUnlockingProcess();

	@Meta.AD(
		deflt = "15", description = "set-in-minutes.-valid-values-between-1-and-99.999",
		max = "99999", min = "1", name = "lock-review-frequency",
		required = true)
	public int lockReviewFrequency();

	@Meta.AD(
		deflt = "5", description = "set-in-minutes.-valid-values-between-1-and-99.999",
		max = "99999", min = "1", name = "time-without-autosave",
		required = true)
	public int timeWithoutAutosave();

}
