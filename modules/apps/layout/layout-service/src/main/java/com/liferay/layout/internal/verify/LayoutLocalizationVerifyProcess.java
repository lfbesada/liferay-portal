/**
 * SPDX-FileCopyrightText: (c) 2023 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.layout.internal.verify;

import com.liferay.counter.kernel.service.CounterLocalService;
import com.liferay.layout.content.LayoutContentProvider;
import com.liferay.layout.util.LayoutServiceContextHelper;
import com.liferay.petra.string.StringBundler;
import com.liferay.portal.dao.orm.common.SQLTransformer;
import com.liferay.portal.kernel.language.Language;
import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.portal.kernel.model.Layout;
import com.liferay.portal.kernel.model.Release;
import com.liferay.portal.kernel.service.ClassNameLocalService;
import com.liferay.portal.kernel.service.CompanyLocalService;
import com.liferay.portal.kernel.service.LayoutLocalService;
import com.liferay.portal.kernel.service.ServiceContext;
import com.liferay.portal.kernel.service.ServiceContextThreadLocal;
import com.liferay.portal.kernel.theme.ThemeDisplay;
import com.liferay.portal.kernel.util.LocaleUtil;
import com.liferay.portal.kernel.util.LoggingTimer;
import com.liferay.portal.kernel.uuid.PortalUUID;
import com.liferay.portal.verify.VerifyProcess;

import java.sql.PreparedStatement;
import java.sql.Timestamp;

import java.util.Locale;

import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

/**
 * @author Lourdes Fernández Besada
 */
@Component(
	property = "run.on.portal.upgrade=true", service = VerifyProcess.class
)
public class LayoutLocalizationVerifyProcess extends VerifyProcess {

	@Override
	protected void doVerify() throws Exception {
		try (LoggingTimer loggingTimer = new LoggingTimer()) {
			long classNameId = _classNameLocalService.getClassNameId(
				Layout.class.getName());

			_companyLocalService.forEachCompanyId(
				companyId -> _addLayoutLocalization(companyId, classNameId));
		}
	}

	private void _addLayoutLocalization(long companyId, long classNameId)
		throws Exception {

		String sql = StringBundler.concat(
			"Select distinct Layout.plid from Layout inner join ",
			"LayoutPageTemplateStructure on LayoutPageTemplateStructure.plid ",
			"= Layout.plid where Layout.companyId = ", companyId,
			" and Layout.classNameId = 0 and Layout.classPK = 0 and ",
			"(Layout.type_ = 'content' or Layout.type_ = 'collection') and ",
			"Layout.system_ = '0' and Layout.hidden_ = '0' and not exists ",
			"(select 1 from LayoutLocalization where LayoutLocalization.plid ",
			"= Layout.plid) and (not exists(select 1 from Layout as Layout1 ",
			"where Layout1.classNameId = ", classNameId,
			" and Layout1.classPK = Layout.plid) or exists (select 1 from ",
			"Layout as Layout1 where Layout1.classNameId = ", classNameId,
			" and Layout1.classPK = Layout.plid and Layout1.typeSettings like ",
			"'%published=true%'))");

		processConcurrently(
			SQLTransformer.transform(sql),
			StringBundler.concat(
				"insert into LayoutLocalization (uuid_, layoutLocalizationId, ",
				"groupId, companyId, createDate, modifiedDate, content, ",
				"languageId, plid) values (?, ?, ?, ?, ?, ?, ?, ?, ?)"),
			resultSet -> new Object[] {resultSet.getLong("plid")},
			(values, preparedStatement) -> {
				long plid = (Long)values[0];

				Layout layout = _layoutLocalService.getLayout(plid);

				try (AutoCloseable autoCloseable =
						_layoutServiceContextHelper.
							getServiceContextAutoCloseable(layout)) {

					ServiceContext serviceContext =
						ServiceContextThreadLocal.getServiceContext();

					ThemeDisplay themeDisplay =
						serviceContext.getThemeDisplay();

					for (Locale locale :
							_language.getAvailableLocales(
								layout.getGroupId())) {

						String layoutContent =
							_layoutContentProvider.getLayoutContent(
								themeDisplay.getRequest(),
								themeDisplay.getResponse(), layout, locale);

						_addLayoutLocalization(
							layout.getGroupId(), layout.getCompanyId(),
							layoutContent, LocaleUtil.toLanguageId(locale),
							layout.getPlid(), preparedStatement);
					}
				}
				catch (Exception exception) {
					exception.printStackTrace();

					if (_log.isWarnEnabled()) {
						_log.warn(
							"Unable to add LayoutLocalization for plid " + plid,
							exception);
					}
				}
			},
			"Unable to create layout localizations");
	}

	private void _addLayoutLocalization(
			long groupId, long companyId, String content, String languageId,
			long plid, PreparedStatement preparedStatement)
		throws Exception {

		preparedStatement.setString(1, _portalUUID.generate());
		preparedStatement.setLong(2, _counterLocalService.increment());
		preparedStatement.setLong(3, groupId);
		preparedStatement.setLong(4, companyId);

		Timestamp timestamp = new Timestamp(System.currentTimeMillis());

		preparedStatement.setTimestamp(5, timestamp);
		preparedStatement.setTimestamp(6, timestamp);

		preparedStatement.setString(7, content);
		preparedStatement.setString(8, languageId);
		preparedStatement.setLong(9, plid);
		preparedStatement.addBatch();
	}

	private static final Log _log = LogFactoryUtil.getLog(
		LayoutLocalizationVerifyProcess.class);

	@Reference
	private ClassNameLocalService _classNameLocalService;

	@Reference
	private CompanyLocalService _companyLocalService;

	@Reference
	private CounterLocalService _counterLocalService;

	@Reference
	private Language _language;

	@Reference
	private LayoutContentProvider _layoutContentProvider;

	@Reference
	private LayoutLocalService _layoutLocalService;

	@Reference
	private LayoutServiceContextHelper _layoutServiceContextHelper;

	@Reference
	private PortalUUID _portalUUID;

	@Reference(
		target = "(&(release.bundle.symbolic.name=com.liferay.layout.service)(release.schema.version>=1.4.0))"
	)
	private Release _release;

}