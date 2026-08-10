/**
 * SPDX-FileCopyrightText: (c) 2000 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.fragment.web.internal.design.library;

import com.liferay.depot.model.DepotEntry;
import com.liferay.design.library.resource.type.DesignLibraryResourceCreationItem;
import com.liferay.design.library.resource.type.DesignLibraryResourceTypeContributor;
import com.liferay.fragment.constants.FragmentActionKeys;
import com.liferay.fragment.constants.FragmentConstants;
import com.liferay.fragment.constants.FragmentPortletKeys;
import com.liferay.fragment.model.FragmentCollection;
import com.liferay.frontend.data.set.model.FDSActionDropdownItem;
import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.language.LanguageUtil;
import com.liferay.portal.kernel.model.Group;
import com.liferay.portal.kernel.portlet.url.builder.PortletURLBuilder;
import com.liferay.portal.kernel.security.permission.PermissionChecker;
import com.liferay.portal.kernel.security.permission.resource.PortletResourcePermission;
import com.liferay.portal.kernel.util.ListUtil;
import com.liferay.portal.kernel.util.PortalUtil;

import jakarta.portlet.PortletRequest;

import jakarta.servlet.http.HttpServletRequest;

import java.util.List;
import java.util.Locale;

import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

/**
 * @author Thiago Buarque
 */
@Component(
	property = "service.ranking:Integer=200",
	service = DesignLibraryResourceTypeContributor.class
)
public class FragmentDesignLibraryResourceTypeContributor
	implements DesignLibraryResourceTypeContributor {

	@Override
	public String getColor() {
		return "--pink";
	}

	@Override
	public List<DesignLibraryResourceCreationItem> getCreationItems(
			HttpServletRequest httpServletRequest, DepotEntry depotEntry,
			String backURL)
		throws PortalException {

		return ListUtil.fromArray(
			_newCreationItem(
				httpServletRequest, depotEntry, backURL, "add-basic-fragment",
				"new-basic-fragment", "fragment",
				FragmentConstants.TYPE_COMPONENT),
			_newCreationItem(
				httpServletRequest, depotEntry, backURL, "add-form-fragment",
				"new-form-fragment", "fragment",
				FragmentConstants.TYPE_INPUT),
			_newCreationItem(
				httpServletRequest, depotEntry, backURL, "add-fragment-set",
				"new-fragment-set", "set", 0));
	}

	@Override
	public String getDefaultActionId() {
		return "view";
	}

	@Override
	public String getEntryClassName() {
		return FragmentCollection.class.getName();
	}

	@Override
	public List<FDSActionDropdownItem> getFDSActionDropdownItems(
			HttpServletRequest httpServletRequest, DepotEntry depotEntry,
			String backURL)
		throws PortalException {

		Group depotGroup = depotEntry.getGroup();

		return ListUtil.fromArray(
			new FDSActionDropdownItem(
				PortletURLBuilder.create(
					PortalUtil.getControlPanelPortletURL(
						httpServletRequest, depotGroup,
						FragmentPortletKeys.FRAGMENT, 0, 0,
						PortletRequest.RENDER_PHASE)
				).setBackURL(
					backURL
				).setParameter(
					"fragmentCollectionExternalReferenceCode",
					"{embedded.externalReferenceCode}"
				).buildString(),
				"view", "view", LanguageUtil.get(httpServletRequest, "view"),
				null, null, "link"),
			new FDSActionDropdownItem(
				PortletURLBuilder.create(
					PortalUtil.getControlPanelPortletURL(
						httpServletRequest, depotGroup,
						FragmentPortletKeys.FRAGMENT, 0, 0,
						PortletRequest.RENDER_PHASE)
				).setMVCRenderCommandName(
					"/fragment/edit_fragment_collection"
				).setRedirect(
					backURL
				).setParameter(
					"fragmentCollectionExternalReferenceCode",
					"{embedded.externalReferenceCode}"
				).buildString(),
				"pencil", "edit", LanguageUtil.get(httpServletRequest, "edit"),
				null, null, "link"),
			new FDSActionDropdownItem(
				"{actions.delete.href}", "trash", "delete",
				LanguageUtil.get(httpServletRequest, "delete"), "delete",
				"delete", "async"));
	}

	@Override
	public String getIcon() {
		return "squares";
	}

	@Override
	public String getKey() {
		return "fragment-set";
	}

	@Override
	public String getLabel(Locale locale) {
		return LanguageUtil.get(locale, "fragment-set");
	}

	@Override
	public boolean hasAddPermission(
		PermissionChecker permissionChecker, DepotEntry depotEntry) {

		return _portletResourcePermission.contains(
			permissionChecker, depotEntry.getGroupId(),
			FragmentActionKeys.MANAGE_FRAGMENT_ENTRIES);
	}

	@Override
	public boolean hasViewPermission(
		PermissionChecker permissionChecker, DepotEntry depotEntry) {

		return hasAddPermission(permissionChecker, depotEntry);
	}

	private DesignLibraryResourceCreationItem _newCreationItem(
			HttpServletRequest httpServletRequest, DepotEntry depotEntry,
			String backURL, String id, String languageKey, String mode,
			int fragmentType)
		throws PortalException {

		return new DesignLibraryResourceCreationItem(
			id, LanguageUtil.get(httpServletRequest, languageKey),
			PortletURLBuilder.create(
				PortalUtil.getControlPanelPortletURL(
					httpServletRequest, depotEntry.getGroup(),
					FragmentPortletKeys.FRAGMENT, 0, 0,
					PortletRequest.RENDER_PHASE)
			).setMVCRenderCommandName(
				"/fragment/design_library/add_fragment_entry"
			).setParameter(
				"backURL", backURL
			).setParameter(
				"fragmentType", fragmentType
			).setParameter(
				"mode", mode
			).buildString());
	}

	@Reference(
		target = "(resource.name=" + FragmentConstants.RESOURCE_NAME + ")"
	)
	private PortletResourcePermission _portletResourcePermission;

}