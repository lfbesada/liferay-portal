/**
 * SPDX-FileCopyrightText: (c) 2026 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.headless.admin.site.internal.util;

import com.liferay.exportimport.kernel.empty.model.EmptyModelManagerUtil;
import com.liferay.exportimport.kernel.staging.MergeLayoutPrototypesThreadLocal;
import com.liferay.fragment.model.FragmentEntry;
import com.liferay.portal.kernel.test.TestInfo;
import com.liferay.portal.kernel.test.util.RandomTestUtil;
import com.liferay.portal.test.log.LogCapture;
import com.liferay.portal.test.log.LoggerTestUtil;
import com.liferay.portal.test.rule.LiferayUnitTestRule;

import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.ClassRule;
import org.junit.Rule;
import org.junit.Test;

import org.mockito.MockedStatic;
import org.mockito.Mockito;

/**
 * @author Lourdes Fernández Besada
 */
public class LogUtilTest {

	@ClassRule
	@Rule
	public static final LiferayUnitTestRule liferayUnitTestRule =
		LiferayUnitTestRule.INSTANCE;

	@AfterClass
	public static void tearDownClass() {
		_emptyModelManagerUtilMockedStatic.close();
	}

	@Test
	@TestInfo("LPD-108594")
	public void testLogOptionalReference() {
		_testLogOptionalReference(1, false);
		_testLogOptionalReference(0, true);
	}

	private void _testLogOptionalReference(
		int expectedLogEntriesSize, boolean mergeLayoutPrototypesInProgress) {

		MergeLayoutPrototypesThreadLocal.setInProgress(
			mergeLayoutPrototypesInProgress);

		try (LogCapture logCapture = LoggerTestUtil.configureLog4JLogger(
				LogUtil.class.getName(), LoggerTestUtil.WARN)) {

			LogUtil.logOptionalReference(
				FragmentEntry.class.getName(), RandomTestUtil.randomString(),
				null, RandomTestUtil.randomLong());

			Assert.assertEquals(
				expectedLogEntriesSize,
				logCapture.getLogEntries(
				).size());
		}
		finally {
			MergeLayoutPrototypesThreadLocal.setInProgress(false);
		}
	}

	private static final MockedStatic<EmptyModelManagerUtil>
		_emptyModelManagerUtilMockedStatic = Mockito.mockStatic(
			EmptyModelManagerUtil.class);

}