/**
 * SPDX-FileCopyrightText: (c) 2026 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.launch.internal.entry.manager;

import com.liferay.fragment.service.FragmentEntryLinkLocalService;
import com.liferay.item.selector.ItemSelectorCriterion;
import com.liferay.launch.entry.manager.LaunchEntryManager;
import com.liferay.layout.constants.LayoutTypeSettingsConstants;
import com.liferay.layout.content.creator.LayoutContentVersionCreator;
import com.liferay.layout.item.selector.LayoutItemSelectorCriterion;
import com.liferay.layout.item.selector.LayoutItemSelectorReturnType;
import com.liferay.layout.page.template.model.LayoutPageTemplateStructure;
import com.liferay.layout.page.template.model.LayoutPageTemplateStructureRel;
import com.liferay.layout.page.template.service.LayoutPageTemplateStructureLocalService;
import com.liferay.layout.page.template.service.LayoutPageTemplateStructureRelLocalService;
import com.liferay.layout.util.structure.DeletedLayoutStructureItem;
import com.liferay.layout.util.structure.LayoutStructure;
import com.liferay.petra.string.StringBundler;
import com.liferay.portal.kernel.exception.NoSuchLayoutException;
import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.exception.SystemException;
import com.liferay.portal.kernel.language.Language;
import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.portal.kernel.model.Layout;
import com.liferay.portal.kernel.module.service.Snapshot;
import com.liferay.portal.kernel.portlet.RequestBackedPortletURLFactory;
import com.liferay.portal.kernel.search.IndexStatusManagerThreadLocal;
import com.liferay.portal.kernel.service.LayoutLocalService;
import com.liferay.portal.kernel.service.ServiceContext;
import com.liferay.portal.kernel.service.ServiceContextThreadLocal;
import com.liferay.portal.kernel.theme.ThemeDisplay;
import com.liferay.portal.kernel.util.Constants;
import com.liferay.portal.kernel.util.GetterUtil;
import com.liferay.portal.kernel.util.HttpComponentsUtil;
import com.liferay.portal.kernel.util.Portal;
import com.liferay.portal.kernel.util.UnicodeProperties;
import com.liferay.portal.kernel.workflow.WorkflowConstants;

import java.io.Serializable;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

/**
 * @author Alejandro Tardín
 * @author Lourdes Fernández Besada
 */
@Component(
	property = "launch.entry.manager.class.name=com.liferay.portal.kernel.model.Layout",
	service = LaunchEntryManager.class
)
public class LayoutLaunchEntryManager implements LaunchEntryManager {

	@Override
	public Draft addDraft(long classPK, long launchSetId, String versionKey)
		throws PortalException {

		Layout layout = _layoutLocalService.getLayout(classPK);

		Layout baseLayout = _fetchLayout(classPK, versionKey);

		if (baseLayout == null) {
			throw new NoSuchLayoutException(
				StringBundler.concat(
					"No version exists with the key {plid=", classPK,
					", versionKey=", versionKey, "}"));
		}

		Layout launchDraftLayout = _layoutLocalService.fetchDraftLayout(
			classPK, launchSetId);

		if (launchDraftLayout == null) {
			launchDraftLayout = _addLaunchDraftLayout(
				layout, baseLayout, launchSetId);
		}

		return _toDraft(_copyLayoutContent(baseLayout, launchDraftLayout));
	}

	@Override
	public void deleteDraft(long classPK, long draftId) throws PortalException {
		Layout launchDraftLayout = _fetchLaunchDraftLayout(classPK, draftId);

		if (launchDraftLayout == null) {
			return;
		}

		_layoutLocalService.deleteLayout(launchDraftLayout);
	}

	@Override
	public Draft fetchDraft(long classPK, long draftId) {
		Layout launchDraftLayout = _fetchLaunchDraftLayout(classPK, draftId);

		if (launchDraftLayout == null) {
			return null;
		}

		return _toDraft(launchDraftLayout);
	}

	@Override
	public Item fetchItem(long classPK) {
		Layout layout = _layoutLocalService.fetchLayout(classPK);

		if (layout == null) {
			return null;
		}

		return new Item() {

			@Override
			public long getGroupId() {
				return layout.getGroupId();
			}

			@Override
			public Serializable getPrimaryKey() {
				return layout.getPlid();
			}

			@Override
			public String getTitle(Locale locale) {
				return layout.getName(locale);
			}

			@Override
			public String getTypeName(Locale locale) {
				return _language.get(locale, "content-page");
			}

		};
	}

	@Override
	public Version fetchPublishedVersion(long classPK) {
		Layout layout = _layoutLocalService.fetchLayout(classPK);

		if (layout == null) {
			return null;
		}

		return _toVersion(layout);
	}

	@Override
	public Version fetchVersion(long classPK, String versionKey) {
		Layout layout = _fetchLayout(classPK, versionKey);

		if (layout == null) {
			return null;
		}

		return _toVersion(layout);
	}

	@Override
	public long getClassPK(Map<String, String> selectedItemData) {
		Layout layout = _layoutLocalService.fetchLayout(
			GetterUtil.getLong(selectedItemData.get("plid")));

		if ((layout == null) || !layout.isTypeContent() ||
			layout.isDraftLayout()) {

			return 0;
		}

		return layout.getPlid();
	}

	@Override
	public ItemSelectorCriterion getItemSelectorCriterion() {
		LayoutItemSelectorCriterion layoutItemSelectorCriterion =
			new LayoutItemSelectorCriterion();

		layoutItemSelectorCriterion.setDesiredItemSelectorReturnTypes(
			new LayoutItemSelectorReturnType());

		return layoutItemSelectorCriterion;
	}

	@Override
	public String getLabel(Locale locale) {
		return _language.get(locale, "page");
	}

	@Override
	public Class<?> getModelClass() {
		return Layout.class;
	}

	@Override
	public List<Version> getVersions(long classPK) {
		List<Version> versions = new ArrayList<>();

		Layout layout = _layoutLocalService.fetchLayout(classPK);

		if (layout == null) {
			return versions;
		}

		if (layout.isPublished()) {
			versions.add(_toVersion(layout));
		}

		Layout draftLayout = layout.fetchDraftLayout();

		if ((draftLayout != null) && draftLayout.isDraft()) {
			versions.add(_toVersion(draftLayout));
		}

		return versions;
	}

	@Override
	public Version publishDraft(long classPK, long draftId)
		throws PortalException {

		Layout launchDraftLayout = _fetchLaunchDraftLayout(classPK, draftId);

		if (launchDraftLayout == null) {
			throw new NoSuchLayoutException(
				StringBundler.concat(
					"No launch draft exists with the key {plid=", classPK,
					", draftId=", draftId, "}"));
		}

		Layout layout = _layoutLocalService.getLayout(classPK);

		if (!layout.isTypeContent() || layout.isDraftLayout()) {
			throw new PortalException(
				"The layout must be the public layout of a content page");
		}

		Layout draftLayout = layout.fetchDraftLayout();

		if (draftLayout.isDraft()) {
			_createLayoutContentVersion(draftLayout);
		}

		draftLayout = _copyLayoutContent(launchDraftLayout, draftLayout);

		_layoutLocalService.deleteLayout(launchDraftLayout);

		return _toVersion(_publishLayout(draftLayout, layout));
	}

	private Layout _addLaunchDraftLayout(
			Layout layout, Layout baseLayout, long launchSetId)
		throws PortalException {

		ServiceContext serviceContext =
			ServiceContextThreadLocal.getServiceContext();

		Layout launchDraftLayout = _layoutLocalService.addLayout(
			StringBundler.concat(
				layout.getExternalReferenceCode(),
				_EXTERNAL_REFERENCE_CODE_INFIX_LAUNCH_DRAFT, launchSetId),
			serviceContext.getUserId(), layout.getGroupId(),
			layout.isPrivateLayout(), layout.getParentLayoutId(),
			_portal.getClassNameId(Layout.class), layout.getPlid(),
			baseLayout.getNameMap(), baseLayout.getTitleMap(),
			baseLayout.getDescriptionMap(), baseLayout.getKeywordsMap(),
			baseLayout.getRobotsMap(), layout.getType(),
			baseLayout.getTypeSettings(), true, true, Collections.emptyMap(),
			baseLayout.getMasterLayoutPageTemplateEntryERC(), serviceContext);

		launchDraftLayout.setLaunchSetId(launchSetId);

		return _layoutLocalService.updateLayout(launchDraftLayout);
	}

	private Layout _copyLayoutContent(Layout sourceLayout, Layout targetLayout)
		throws PortalException {

		try {
			_layoutLocalService.copyLayoutContent(sourceLayout, targetLayout);
		}
		catch (Exception exception) {
			throw new PortalException(exception);
		}

		return _layoutLocalService.getLayout(targetLayout.getPlid());
	}

	/**
	 * Keeps the pending changes of the page's own draft as a version before a
	 * launch draft overwrites them, so that they can still be restored.
	 */
	private void _createLayoutContentVersion(Layout draftLayout) {
		LayoutContentVersionCreator layoutContentVersionCreator =
			_layoutContentVersionCreatorSnapshot.get();

		if (layoutContentVersionCreator == null) {
			if (_log.isDebugEnabled()) {
				_log.debug("Layout content version creator is unavailable");
			}

			return;
		}

		layoutContentVersionCreator.createLayoutContentVersion(
			draftLayout, WorkflowConstants.STATUS_DRAFT);
	}

	private void _deleteMarkedForDeletionItems(
			long groupId, long plid, long userId)
		throws PortalException {

		LayoutPageTemplateStructure layoutPageTemplateStructure =
			_layoutPageTemplateStructureLocalService.
				fetchLayoutPageTemplateStructure(groupId, plid);

		if (layoutPageTemplateStructure == null) {
			return;
		}

		_fragmentEntryLinkLocalService.deleteFragmentEntryLinks(
			groupId, plid, true);

		for (LayoutPageTemplateStructureRel layoutPageTemplateStructureRel :
				_layoutPageTemplateStructureRelLocalService.
					getLayoutPageTemplateStructureRels(
						layoutPageTemplateStructure.
							getLayoutPageTemplateStructureId())) {

			LayoutStructure layoutStructure = LayoutStructure.of(
				layoutPageTemplateStructureRel.getData());

			for (DeletedLayoutStructureItem deletedLayoutStructureItem :
					layoutStructure.getDeletedLayoutStructureItems()) {

				layoutStructure.deleteLayoutStructureItem(
					deletedLayoutStructureItem.getItemId());
			}

			_layoutPageTemplateStructureLocalService.
				updateLayoutPageTemplateStructureData(
					userId, groupId, plid,
					layoutPageTemplateStructureRel.getSegmentsExperienceId(),
					layoutStructure.toString());
		}
	}

	private Layout _fetchLaunchDraftLayout(long classPK, long draftId) {
		Layout launchDraftLayout = _layoutLocalService.fetchLayout(draftId);

		if ((launchDraftLayout == null) ||
			(launchDraftLayout.getClassPK() != classPK) ||
			(launchDraftLayout.getLaunchSetId() == 0)) {

			return null;
		}

		return launchDraftLayout;
	}

	private Layout _fetchLayout(long classPK, String versionKey) {
		Layout layout = _layoutLocalService.fetchLayout(
			GetterUtil.getLong(versionKey));

		if ((layout == null) || (layout.getLaunchSetId() > 0)) {
			return null;
		}

		if ((layout.getPlid() == classPK) ||
			(layout.isDraftLayout() && (layout.getClassPK() == classPK))) {

			return layout;
		}

		return null;
	}

	private String _getEditURL(Layout layout, String redirect) {
		ServiceContext serviceContext =
			ServiceContextThreadLocal.getServiceContext();

		ThemeDisplay themeDisplay = serviceContext.getThemeDisplay();

		try {
			return HttpComponentsUtil.addParameters(
				_portal.getLayoutFullURL(layout, themeDisplay), "p_l_back_url",
				redirect, "p_l_back_url_title",
				_language.get(themeDisplay.getLocale(), "launches"), "p_l_mode",
				Constants.EDIT);
		}
		catch (PortalException portalException) {
			throw new SystemException(portalException);
		}
	}

	private String _getLabel(Layout layout, Locale locale) {
		if (!layout.isDraftLayout()) {
			return _language.get(locale, "published");
		}

		return _language.get(locale, "current-draft");
	}

	/**
	 * Publishes the page's own draft the way the page editor does, once a
	 * launch draft has been copied into it.
	 */
	private Layout _publishLayout(Layout draftLayout, Layout layout)
		throws PortalException {

		ServiceContext serviceContext =
			ServiceContextThreadLocal.getServiceContext();

		boolean indexReadOnly = IndexStatusManagerThreadLocal.isIndexReadOnly();

		IndexStatusManagerThreadLocal.setIndexReadOnly(true);

		try {
			layout = _copyLayoutContent(draftLayout, layout);
		}
		finally {
			IndexStatusManagerThreadLocal.setIndexReadOnly(indexReadOnly);
		}

		_deleteMarkedForDeletionItems(
			draftLayout.getGroupId(), draftLayout.getPlid(),
			serviceContext.getUserId());

		draftLayout = _layoutLocalService.getLayout(draftLayout.getPlid());

		UnicodeProperties typeSettingsUnicodeProperties =
			draftLayout.getTypeSettingsProperties();

		typeSettingsUnicodeProperties.remove(
			LayoutTypeSettingsConstants.KEY_DESIGN_CONFIGURATION_MODIFIED);
		typeSettingsUnicodeProperties.put(
			LayoutTypeSettingsConstants.KEY_PUBLISHED, Boolean.TRUE.toString());

		draftLayout.setStatus(WorkflowConstants.STATUS_APPROVED);

		_layoutLocalService.updateLayout(draftLayout);

		layout.setStatus(WorkflowConstants.STATUS_APPROVED);

		return _layoutLocalService.updateLayout(layout);
	}

	private Draft _toDraft(Layout launchDraftLayout) {
		return new Draft() {

			@Override
			public long getDraftId() {
				return launchDraftLayout.getPlid();
			}

			@Override
			public String getEditURL(
				String redirect,
				RequestBackedPortletURLFactory requestBackedPortletURLFactory) {

				return _getEditURL(launchDraftLayout, redirect);
			}

			@Override
			public Date getModifiedDate() {
				return launchDraftLayout.getModifiedDate();
			}

			@Override
			public Serializable getPreviewPrimaryKey() {
				return launchDraftLayout.getPlid();
			}

			@Override
			public int getStatus() {
				return WorkflowConstants.STATUS_DRAFT;
			}

			@Override
			public String getUserName() {
				return launchDraftLayout.getUserName();
			}

		};
	}

	private Version _toVersion(Layout layout) {
		return new Version() {

			@Override
			public String getLabel(Locale locale) {
				return _getLabel(layout, locale);
			}

			@Override
			public Date getModifiedDate() {
				return layout.getModifiedDate();
			}

			@Override
			public Serializable getPreviewPrimaryKey() {
				return layout.getPlid();
			}

			@Override
			public int getStatus() {
				if (layout.isDraftLayout()) {
					return WorkflowConstants.STATUS_DRAFT;
				}

				return layout.getStatus();
			}

			@Override
			public String getUserName() {
				return layout.getUserName();
			}

			@Override
			public String getVersionKey() {
				return String.valueOf(layout.getPlid());
			}

		};
	}

	private static final String _EXTERNAL_REFERENCE_CODE_INFIX_LAUNCH_DRAFT =
		"-launch-";

	private static final Log _log = LogFactoryUtil.getLog(
		LayoutLaunchEntryManager.class);

	private static final Snapshot<LayoutContentVersionCreator>
		_layoutContentVersionCreatorSnapshot = new Snapshot<>(
			LayoutLaunchEntryManager.class, LayoutContentVersionCreator.class,
			null, true);

	@Reference
	private FragmentEntryLinkLocalService _fragmentEntryLinkLocalService;

	@Reference
	private Language _language;

	@Reference
	private LayoutLocalService _layoutLocalService;

	@Reference
	private LayoutPageTemplateStructureLocalService
		_layoutPageTemplateStructureLocalService;

	@Reference
	private LayoutPageTemplateStructureRelLocalService
		_layoutPageTemplateStructureRelLocalService;

	@Reference
	private Portal _portal;

}