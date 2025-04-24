/**
 * SPDX-FileCopyrightText: (c) 2025 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.fragment.collection.contributor.hhs.internal.fragment.renderer;

import com.liferay.fragment.renderer.FragmentRenderer;
import com.liferay.fragment.renderer.FragmentRendererContext;
import com.liferay.frontend.taglib.react.servlet.taglib.ComponentTag;
import com.liferay.info.item.ClassPKInfoItemIdentifier;
import com.liferay.info.item.ERCInfoItemIdentifier;
import com.liferay.info.item.InfoItemReference;
import com.liferay.object.admin.rest.resource.v1_0.ObjectDefinitionResource;
import com.liferay.object.model.ObjectEntry;
import com.liferay.object.service.ObjectEntryLocalService;
import com.liferay.petra.string.StringPool;
import com.liferay.portal.kernel.json.JSONFactory;
import com.liferay.portal.kernel.language.Language;
import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.portal.kernel.util.HashMapBuilder;
import com.liferay.taglib.servlet.PageContextFactoryUtil;

import java.io.PrintWriter;

import java.util.Locale;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

/**
 * @author Sandro Chinea
 */
@Component(service = FragmentRenderer.class)
public class MemberTableFragmentRenderer implements FragmentRenderer {

	@Override
	public String getCollectionKey() {
		return "HHS";
	}

	@Override
	public String getLabel(Locale locale) {
		return _language.get(locale, "Member Table");
	}

	@Override
	public void render(
		FragmentRendererContext fragmentRendererContext,
		HttpServletRequest httpServletRequest,
		HttpServletResponse httpServletResponse) {

		try {
			PrintWriter printWriter = httpServletResponse.getWriter();

			printWriter.write("<div><span aria-hidden=\"true\" class=\"");
			printWriter.write("loading-animation\"></span>");

			ComponentTag componentTag = new ComponentTag();

			componentTag.setModule(
				"{MemberTable} from fragment-collection-contributor-hhs");
			componentTag.setPageContext(
				PageContextFactoryUtil.create(
					httpServletRequest, httpServletResponse));
			componentTag.setProps(
				HashMapBuilder.<String, Object>put(
					"externalReferenceCode",
					_getExternalReferenceCode(
						fragmentRendererContext.getContextInfoItemReference())
				).build());

			componentTag.setServletContext(_servletContext);

			componentTag.doStartTag();

			componentTag.doEndTag();

			printWriter.write("</div>");
		}
		catch (Exception exception) {
			if (_log.isDebugEnabled()) {
				_log.debug(exception);
			}
		}
	}

	private String _getExternalReferenceCode(
		InfoItemReference infoItemReference) {

		if (infoItemReference == null) {
			return StringPool.BLANK;
		}

		if (infoItemReference.getInfoItemIdentifier() instanceof
				ERCInfoItemIdentifier) {

			ERCInfoItemIdentifier ercInfoItemIdentifier =
				(ERCInfoItemIdentifier)
					infoItemReference.getInfoItemIdentifier();

			return ercInfoItemIdentifier.getExternalReferenceCode();
		}

		if (!(infoItemReference.getInfoItemIdentifier() instanceof
				ClassPKInfoItemIdentifier)) {

			return StringPool.BLANK;
		}

		ClassPKInfoItemIdentifier classPKInfoItemIdentifier =
			(ClassPKInfoItemIdentifier)
				infoItemReference.getInfoItemIdentifier();

		ObjectEntry objectEntry = _objectEntryLocalService.fetchObjectEntry(
			classPKInfoItemIdentifier.getClassPK());

		if (objectEntry == null) {
			return StringPool.BLANK;
		}

		return objectEntry.getExternalReferenceCode();
	}

	private static final Log _log = LogFactoryUtil.getLog(
		MemberTableFragmentRenderer.class);

	@Reference
	private JSONFactory _jsonFactory;

	@Reference
	private Language _language;

	@Reference
	private ObjectDefinitionResource.Factory _objectDefinitionResourceFactory;

	@Reference
	private ObjectEntryLocalService _objectEntryLocalService;

	@Reference(
		target = "(osgi.web.symbolicname=com.liferay.fragment.collection.contributor.hhs)"
	)
	private ServletContext _servletContext;

}