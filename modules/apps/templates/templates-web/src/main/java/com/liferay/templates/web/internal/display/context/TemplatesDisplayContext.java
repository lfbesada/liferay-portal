/**
 * Copyright (c) 2000-present Liferay, Inc. All rights reserved.
 *
 * This library is free software; you can redistribute it and/or modify it under
 * the terms of the GNU Lesser General Public License as published by the Free
 * Software Foundation; either version 2.1 of the License, or (at your option)
 * any later version.
 *
 * This library is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU Lesser General Public License for more
 * details.
 */

package com.liferay.templates.web.internal.display.context;

import com.liferay.dynamic.data.mapping.configuration.DDMGroupServiceConfiguration;
import com.liferay.dynamic.data.mapping.configuration.DDMWebConfiguration;
import com.liferay.dynamic.data.mapping.constants.DDMPortletKeys;
import com.liferay.dynamic.data.mapping.constants.DDMTemplateConstants;
import com.liferay.dynamic.data.mapping.model.DDMStructure;
import com.liferay.dynamic.data.mapping.model.DDMTemplate;
import com.liferay.dynamic.data.mapping.service.DDMTemplateService;
import com.liferay.dynamic.data.mapping.storage.StorageAdapterRegistry;
import com.liferay.dynamic.data.mapping.util.DDMDisplay;
import com.liferay.dynamic.data.mapping.util.DDMDisplayRegistry;
import com.liferay.dynamic.data.mapping.util.DDMDisplayTabItem;
import com.liferay.dynamic.data.mapping.util.DDMTemplateHelper;
import com.liferay.dynamic.data.mapping.util.DDMUtil;
import com.liferay.frontend.taglib.clay.servlet.taglib.util.CreationMenu;
import com.liferay.frontend.taglib.clay.servlet.taglib.util.DropdownItem;
import com.liferay.frontend.taglib.clay.servlet.taglib.util.DropdownItemListBuilder;
import com.liferay.frontend.taglib.clay.servlet.taglib.util.NavigationItem;
import com.liferay.frontend.taglib.clay.servlet.taglib.util.NavigationItemList;
import com.liferay.frontend.taglib.clay.servlet.taglib.util.NavigationItemListBuilder;
import com.liferay.info.item.renderer.InfoItemRenderer;
import com.liferay.info.item.renderer.InfoItemRendererTracker;
import com.liferay.info.item.renderer.InfoItemTemplatedRenderer;
import com.liferay.info.item.renderer.template.InfoItemRendererTemplate;
import com.liferay.petra.function.UnsafeConsumer;
import com.liferay.petra.portlet.url.builder.PortletURLBuilder;
import com.liferay.petra.string.StringPool;
import com.liferay.portal.kernel.dao.search.SearchContainer;
import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.language.LanguageUtil;
import com.liferay.portal.kernel.portlet.LiferayPortletRequest;
import com.liferay.portal.kernel.portlet.LiferayPortletResponse;
import com.liferay.portal.kernel.portlet.PortalPreferences;
import com.liferay.portal.kernel.portlet.PortletPreferencesFactoryUtil;
import com.liferay.portal.kernel.portlet.PortletURLUtil;
import com.liferay.portal.kernel.security.permission.ActionKeys;
import com.liferay.portal.kernel.service.permission.PortletPermissionUtil;
import com.liferay.portal.kernel.template.TemplateHandler;
import com.liferay.portal.kernel.template.TemplateHandlerRegistryUtil;
import com.liferay.portal.kernel.template.comparator.TemplateHandlerComparator;
import com.liferay.portal.kernel.theme.ThemeDisplay;
import com.liferay.portal.kernel.util.GetterUtil;
import com.liferay.portal.kernel.util.ListUtil;
import com.liferay.portal.kernel.util.OrderByComparator;
import com.liferay.portal.kernel.util.ParamUtil;
import com.liferay.portal.kernel.util.PortalUtil;
import com.liferay.portal.kernel.util.Validator;
import com.liferay.portal.kernel.util.WebKeys;
import com.liferay.portlet.display.template.PortletDisplayTemplate;
import com.liferay.templates.web.internal.constants.TemplatesPortletKeys;
import com.liferay.templates.web.internal.context.util.TemplatesWebRequestHelper;
import com.liferay.templates.web.internal.search.TemplateSearch;
import com.liferay.templates.web.internal.search.TemplateSearchTerms;
import com.liferay.templates.web.internal.security.permissions.resource.DDMTemplatePermission;
import com.liferay.templates.web.internal.tab.item.BaseTemplatesTabItem;
import com.liferay.templates.web.internal.util.PortletDisplayTemplateUtil;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import java.util.Objects;
import java.util.Set;

import javax.portlet.PortletException;
import javax.portlet.PortletRequest;
import javax.portlet.PortletURL;
import javax.portlet.RenderRequest;
import javax.portlet.RenderResponse;

import javax.servlet.http.HttpServletRequest;

/**
 * @author Rafael Praxedes
 * @author Lourdes Fernández Besada
 */
public class TemplatesDisplayContext {

	public TemplatesDisplayContext(
		RenderRequest renderRequest, RenderResponse renderResponse,
		DDMDisplayRegistry ddmDisplayRegistry,
		DDMTemplateHelper ddmTemplateHelper,
		DDMTemplateService ddmTemplateService,
		DDMWebConfiguration ddmWebConfiguration,
		InfoItemRendererTracker infoItemRendererTracker,
		StorageAdapterRegistry storageAdapterRegistry) {

		_renderRequest = renderRequest;
		_renderResponse = renderResponse;
		_ddmDisplayRegistry = ddmDisplayRegistry;
		_ddmTemplateHelper = ddmTemplateHelper;
		_ddmTemplateService = ddmTemplateService;
		_ddmWebConfiguration = ddmWebConfiguration;
		_infoItemRendererTracker = infoItemRendererTracker;
		_storageAdapterRegistry = storageAdapterRegistry;

		_templatesWebRequestHelper = new TemplatesWebRequestHelper(
			PortalUtil.getHttpServletRequest(renderRequest));
	}

	public boolean autogenerateStructureKey() {
		return _ddmWebConfiguration.autogenerateStructureKey();
	}

	public boolean autogenerateTemplateKey() {
		return _ddmWebConfiguration.autogenerateTemplateKey();
	}

	public boolean changeableDefaultLanguage() {
		return _ddmWebConfiguration.changeableDefaultLanguage();
	}

	public boolean containsAddTemplatePermission(String actualTemplateTypeValue)
		throws PortalException {

		DDMDisplay ddmDisplay = getDDMDisplay();

		String expectedTemplateTypeValue = getTemplateTypeValue();

		long scopeClassNameId = PortalUtil.getClassNameId(
			ddmDisplay.getStructureType());

		if (DDMTemplatePermission.containsAddTemplatePermission(
				_templatesWebRequestHelper.getPermissionChecker(),
				_templatesWebRequestHelper.getScopeGroupId(), getClassNameId(),
				scopeClassNameId) &&
			(Validator.isNull(expectedTemplateTypeValue) ||
			 expectedTemplateTypeValue.equals(actualTemplateTypeValue))) {

			return true;
		}

		return false;
	}

	public DDMStructure fetchStructure(DDMTemplate template) {
		return _ddmTemplateHelper.fetchStructure(template);
	}

	public List<DropdownItem> getActionItemsDropdownItems(String action) {
		return DropdownItemListBuilder.add(
			dropdownItem -> {
				dropdownItem.putData("action", action);
				dropdownItem.setIcon("times-circle");
				dropdownItem.setLabel(
					LanguageUtil.get(
						_templatesWebRequestHelper.getRequest(), "delete"));
				dropdownItem.setQuickAction(true);
			}
		).build();
	}

	public String getAutocompleteJSON(
			HttpServletRequest httpServletRequest, String language)
		throws Exception {

		return _ddmTemplateHelper.getAutocompleteJSON(
			httpServletRequest, language);
	}

	public String getClearResultsURL() throws PortletException {
		return PortletURLBuilder.create(
			PortletURLUtil.clone(getPortletURL(), _renderResponse)
		).setKeywords(
			StringPool.BLANK
		).setParameter(
			TemplatesPortletKeys.CURRENT_TAB_ID_PARAMETER_NAME, getSelectedTab()
		).buildString();
	}

	public DDMDisplay getDDMDisplay() {
		return _ddmDisplayRegistry.getDDMDisplay(getRefererPortletName());
	}

	public List<DropdownItem> getFilterItemsDropdownItems() {
		return DropdownItemListBuilder.addGroup(
			dropdownGroupItem -> {
				dropdownGroupItem.setDropdownItems(
					getFilterNavigationDropdownItems());
				dropdownGroupItem.setLabel(
					LanguageUtil.get(
						_templatesWebRequestHelper.getRequest(),
						"filter-by-navigation"));
			}
		).addGroup(
			dropdownGroupItem -> {
				dropdownGroupItem.setDropdownItems(getOrderByDropdownItems());
				dropdownGroupItem.setLabel(
					LanguageUtil.get(
						_templatesWebRequestHelper.getRequest(), "order-by"));
			}
		).build();
	}

	public List<InfoItemRendererTemplate> getInfoItemRendererTemplatesList(
		PortletRequest portletRequest) {

		List<InfoItemRendererTemplate> infoItemRendererTemplatesList =
			new ArrayList<>();

		List<InfoItemRenderer<?>> infoItemRenderers =
			_infoItemRendererTracker.getInfoItemRenderers();

		ThemeDisplay themeDisplay = (ThemeDisplay)portletRequest.getAttribute(
			WebKeys.THEME_DISPLAY);

		for (InfoItemRenderer<?> infoItemRenderer : infoItemRenderers) {
			if (infoItemRenderer instanceof InfoItemTemplatedRenderer) {
				InfoItemTemplatedRenderer<Object> infoItemTemplatedRenderer =
					(InfoItemTemplatedRenderer<Object>)infoItemRenderer;

				List<InfoItemRendererTemplate> infoItemRendererTemplates =
					infoItemTemplatedRenderer.getInfoItemRendererTemplates(
						"com.liferay.journal.model.JournalArticle",
						StringPool.BLANK, themeDisplay.getLocale());

				infoItemRendererTemplatesList.addAll(infoItemRendererTemplates);
			}
		}

		Collections.sort(
			infoItemRendererTemplatesList,
			Comparator.comparing(InfoItemRendererTemplate::getLabel));

		return infoItemRendererTemplatesList;
	}

	public List<NavigationItem> getNavigationItem() {
		return NavigationItemListBuilder.add(
			navigationItem -> {
				navigationItem.setActive(true);
				navigationItem.setLabel(getScopedStructureLabel());
			}
		).build();
	}

	public List<NavigationItem> getNavigationItems(
			LiferayPortletRequest liferayPortletRequest,
			LiferayPortletResponse liferayPortletResponse)
		throws Exception {

		return new NavigationItemList() {
			{
				DDMDisplay ddmDisplay = getDDMDisplay();

				for (DDMDisplayTabItem ddmDisplayTabItem :
						ddmDisplay.getTabItems()) {

					if (!ddmDisplayTabItem.isShow(liferayPortletRequest)) {
						continue;
					}

					BaseTemplatesTabItem baseTemplatesTabItem =
						(BaseTemplatesTabItem)ddmDisplayTabItem;

					String ddmDisplayTabItemTitle = GetterUtil.getString(
						baseTemplatesTabItem.getTitle(
							liferayPortletRequest, liferayPortletResponse));

					String ddmDisplayTabItemHREF = GetterUtil.getString(
						baseTemplatesTabItem.getURL(
							liferayPortletRequest, liferayPortletResponse));

					boolean tabActive = Objects.equals(
						getSelectedTab(), baseTemplatesTabItem.getTabId());

					add(
						navigationItem -> {
							navigationItem.setActive(tabActive);
							navigationItem.setHref(ddmDisplayTabItemHREF);
							navigationItem.setLabel(ddmDisplayTabItemTitle);
						});
				}
			}
		};
	}

	public String getOrderByCol() {
		PortalPreferences portalPreferences =
			PortletPreferencesFactoryUtil.getPortalPreferences(_renderRequest);

		String orderByCol = ParamUtil.getString(_renderRequest, "orderByCol");

		if (Validator.isNull(orderByCol)) {
			orderByCol = portalPreferences.getValue(
				DDMPortletKeys.DYNAMIC_DATA_MAPPING, "entries-order-by-col",
				"modified-date");
		}
		else {
			portalPreferences.setValue(
				DDMPortletKeys.DYNAMIC_DATA_MAPPING, "entries-order-by-col",
				orderByCol);
		}

		return orderByCol;
	}

	public String getOrderByType() {
		PortalPreferences portalPreferences =
			PortletPreferencesFactoryUtil.getPortalPreferences(_renderRequest);

		String orderByType = ParamUtil.getString(_renderRequest, "orderByType");

		if (Validator.isNull(orderByType)) {
			orderByType = portalPreferences.getValue(
				DDMPortletKeys.DYNAMIC_DATA_MAPPING, "entries-order-by-type",
				"asc");
		}
		else {
			portalPreferences.setValue(
				DDMPortletKeys.DYNAMIC_DATA_MAPPING, "entries-order-by-type",
				orderByType);
		}

		return orderByType;
	}

	public String getRefererPortletName() {
		return ParamUtil.getString(
			_templatesWebRequestHelper.getRequest(), "refererPortletName",
			_templatesWebRequestHelper.getPortletName());
	}

	public String getScopedStructureLabel() {
		String scopeTitle = ParamUtil.getString(_renderRequest, "scopeTitle");

		if (Validator.isNull(scopeTitle)) {
			DDMDisplay ddmDisplay = getDDMDisplay();

			return ddmDisplay.getTitle(_templatesWebRequestHelper.getLocale());
		}

		return scopeTitle;
	}

	public String getSelectedTab() {
		if (_selectedTab != null) {
			return _selectedTab;
		}

		_selectedTab = ParamUtil.getString(
			_templatesWebRequestHelper.getLiferayPortletRequest(),
			TemplatesPortletKeys.CURRENT_TAB_ID_PARAMETER_NAME,
			TemplatesPortletKeys.INFO_TEMPLATE_TAB_ID);

		return _selectedTab;
	}

	public String getSelectStructureSearchActionURL() {
		return PortletURLBuilder.createRenderURL(
			_renderResponse
		).setMVCPath(
			"/select_structure.jsp"
		).setParameter(
			TemplatesPortletKeys.CURRENT_TAB_ID_PARAMETER_NAME, getSelectedTab()
		).setParameter(
			"classPK", ParamUtil.getLong(_renderRequest, "classPK")
		).setParameter(
			"eventName",
			ParamUtil.getString(_renderRequest, "eventName", "selectStructure")
		).buildString();
	}

	public String getSelectTemplateSearchActionURL() {
		return PortletURLBuilder.createRenderURL(
			_renderResponse
		).setMVCPath(
			"/select_template.jsp"
		).setParameter(
			TemplatesPortletKeys.CURRENT_TAB_ID_PARAMETER_NAME, getSelectedTab()
		).setParameter(
			"classNameId", getClassNameId()
		).setParameter(
			"classPK", ParamUtil.getLong(_renderRequest, "classPK")
		).setParameter(
			"eventName",
			ParamUtil.getString(_renderRequest, "eventName", "selectTemplate")
		).setParameter(
			"resourceClassNameId", getResourceClassNameId()
		).setParameter(
			"templateId", ParamUtil.getLong(_renderRequest, "templateId")
		).buildString();
	}

	public String getSortingURL() throws Exception {
		return PortletURLBuilder.create(
			PortletURLUtil.clone(getPortletURL(), _renderResponse)
		).setParameter(
			TemplatesPortletKeys.CURRENT_TAB_ID_PARAMETER_NAME, getSelectedTab()
		).setParameter(
			"orderByType",
			() -> {
				String orderByType = ParamUtil.getString(
					_renderRequest, "orderByType");

				if (orderByType.equals("asc")) {
					return "desc";
				}

				return "asc";
			}
		).buildString();
	}

	public Set<String> getStorageTypes() {
		return _storageAdapterRegistry.getStorageTypes();
	}

	public String getStructureSearchActionURL() {
		return PortletURLBuilder.createRenderURL(
			_renderResponse
		).setMVCPath(
			"/view.jsp"
		).setTabs1(
			ParamUtil.getString(_renderRequest, "tabs1", "structures")
		).setParameter(
			TemplatesPortletKeys.CURRENT_TAB_ID_PARAMETER_NAME, getSelectedTab()
		).setParameter(
			"groupId", _templatesWebRequestHelper.getScopeGroupId()
		).buildString();
	}

	public String getStructureSearchContainerId() {
		return "ddmStructures";
	}

	public CreationMenu getTemplateCreationMenu() throws PortalException {
		if (!isShowAddTemplateButton()) {
			return null;
		}

		return new CreationMenu() {
			{
				if (getClassNameId() == PortalUtil.getClassNameId(
						DDMStructure.class)) {

					PortletURL addTemplateURL =
						PortletURLBuilder.createRenderURL(
							_renderResponse
						).setMVCPath(
							"/edit_template.jsp"
						).setParameter(
							TemplatesPortletKeys.CURRENT_TAB_ID_PARAMETER_NAME,
							getSelectedTab()
						).setParameter(
							"classNameId", getClassNameId()
						).setParameter(
							"classPK", getClassPK()
						).setParameter(
							"groupId",
							_templatesWebRequestHelper.getScopeGroupId()
						).setParameter(
							"mode", getTemplateMode()
						).setParameter(
							"resourceClassNameId", getResourceClassNameId()
						).build();

					String message = "add";

					if (containsAddTemplatePermission(
							DDMTemplateConstants.TEMPLATE_TYPE_FORM)) {

						addTemplateURL.setParameter(
							"structureAvailableFields",
							_renderResponse.getNamespace() +
								"getAvailableFields");

						if (Validator.isNull(getTemplateTypeValue())) {
							message = "add-form-template";
						}

						addPrimaryDropdownItem(
							getCreationMenuDropdownItem(
								addTemplateURL, message));
					}

					if (containsAddTemplatePermission(
							DDMTemplateConstants.TEMPLATE_TYPE_DISPLAY)) {

						addTemplateURL.setParameter(
							"type", DDMTemplateConstants.TEMPLATE_TYPE_DISPLAY);

						if (Validator.isNull(getTemplateTypeValue())) {
							message = "add-display-template";
						}

						addPrimaryDropdownItem(
							getCreationMenuDropdownItem(
								addTemplateURL, message));
					}
				}
				else {
					List<TemplateHandler> templateHandlers =
						getTemplateHandlers();

					if (!templateHandlers.isEmpty()) {
						PortletURL addPortletDisplayTemplateURL =
							PortletURLBuilder.createRenderURL(
								_renderResponse
							).setMVCPath(
								"/edit_template.jsp"
							).setParameter(
								TemplatesPortletKeys.
									CURRENT_TAB_ID_PARAMETER_NAME,
								getSelectedTab()
							).setParameter(
								"groupId",
								_templatesWebRequestHelper.getScopeGroupId()
							).setParameter(
								"type",
								DDMTemplateConstants.TEMPLATE_TYPE_DISPLAY
							).build();

						for (TemplateHandler templateHandler :
								templateHandlers) {

							addPortletDisplayTemplateURL.setParameter(
								"classNameId",
								String.valueOf(
									PortalUtil.getClassNameId(
										templateHandler.getClassName())));
							addPortletDisplayTemplateURL.setParameter(
								"classPK", String.valueOf(0));
							addPortletDisplayTemplateURL.setParameter(
								"resourceClassNameId",
								String.valueOf(getResourceClassNameId()));

							addPrimaryDropdownItem(
								getCreationMenuDropdownItem(
									addPortletDisplayTemplateURL,
									templateHandler.getName(
										_templatesWebRequestHelper.
											getLocale())));
						}
					}
				}
			}
		};
	}

	public SearchContainer<DDMTemplate> getTemplateSearch() throws Exception {
		TemplateSearch templateSearch = new TemplateSearch(
			_renderRequest, getPortletURL());

		String orderByCol = getOrderByCol();
		String orderByType = getOrderByType();

		OrderByComparator<DDMTemplate> orderByComparator =
			DDMUtil.getTemplateOrderByComparator(
				getOrderByCol(), getOrderByType());

		templateSearch.setOrderByCol(orderByCol);
		templateSearch.setOrderByComparator(orderByComparator);
		templateSearch.setOrderByType(orderByType);

		if (templateSearch.isSearch()) {
			templateSearch.setEmptyResultsMessage("no-templates-were-found");
		}
		else {
			templateSearch.setEmptyResultsMessage("there-are-no-templates");
		}

		setDDMTemplateInstanceSearchResults(templateSearch);
		setDDMTemplateInstanceSearchTotal(templateSearch);

		return templateSearch;
	}

	public String getTemplateSearchActionURL() {
		return PortletURLBuilder.createRenderURL(
			_renderResponse
		).setMVCPath(
			"/view.jsp"
		).setTabs1(
			ParamUtil.getString(_renderRequest, "tabs1", "templates")
		).setParameter(
			TemplatesPortletKeys.CURRENT_TAB_ID_PARAMETER_NAME, getSelectedTab()
		).setParameter(
			"classNameId", getClassNameId()
		).setParameter(
			"classPK", getClassPK()
		).setParameter(
			"eventName",
			ParamUtil.getString(_renderRequest, "eventName", "selectTemplate")
		).setParameter(
			"groupId", _templatesWebRequestHelper.getScopeGroupId()
		).setParameter(
			"resourceClassNameId", getResourceClassNameId()
		).setParameter(
			"templateId", ParamUtil.getLong(_renderRequest, "templateId")
		).buildString();
	}

	public String getTemplateSearchContainerId() {
		return "ddmTemplates";
	}

	public int getTotalItems(String context) throws Exception {
		SearchContainer<?> searchContainer = getTemplateSearch();

		return searchContainer.getTotal();
	}

	public boolean isAutocompleteEnabled(String language) {
		return _ddmTemplateHelper.isAutocompleteEnabled(language);
	}

	public boolean isDisabledManagementBar(String context) throws Exception {
		if (hasResults(context) || isSearch()) {
			return false;
		}

		return true;
	}

	public boolean isSearch() {
		if (Validator.isNotNull(getKeywords())) {
			return true;
		}

		return false;
	}

	public boolean isSelectedInfoTemplatesTab() {
		return Objects.equals(
			TemplatesPortletKeys.INFO_TEMPLATE_TAB_ID, getSelectedTab());
	}

	public boolean isSelectedWidgetTemplatesTab() {
		return Objects.equals(
			TemplatesPortletKeys.WIDGET_TEMPLATE_TAB_ID, getSelectedTab());
	}

	public boolean isShowAddTemplateButton() throws PortalException {
		DDMDisplay ddmDisplay = getDDMDisplay();

		ThemeDisplay themeDisplay =
			_templatesWebRequestHelper.getThemeDisplay();

		if (_ddmWebConfiguration.enableTemplateCreation() &&
			ddmDisplay.isShowAddButton(themeDisplay.getScopeGroup())) {

			long classNameId = getClassNameId();
			long resourceClassNameId = PortalUtil.getClassNameId(
				ddmDisplay.getStructureType());

			if ((classNameId != 0) && (resourceClassNameId != 0)) {
				return DDMTemplatePermission.containsAddTemplatePermission(
					_templatesWebRequestHelper.getPermissionChecker(),
					_templatesWebRequestHelper.getScopeGroupId(), classNameId,
					resourceClassNameId);
			}

			return true;
		}

		return false;
	}

	public String[] smallImageExtensions() {
		DDMGroupServiceConfiguration ddmGroupServiceConfiguration =
			_templatesWebRequestHelper.getDDMGroupServiceConfiguration();

		return ddmGroupServiceConfiguration.smallImageExtensions();
	}

	public int smallImageMaxSize() {
		DDMGroupServiceConfiguration ddmGroupServiceConfiguration =
			_templatesWebRequestHelper.getDDMGroupServiceConfiguration();

		return ddmGroupServiceConfiguration.smallImageMaxSize();
	}

	protected boolean containsAddPortletDisplayTemplatePermission(
			String resourceName)
		throws PortalException {

		if (getClassNameId() > 0) {
			return PortletPermissionUtil.contains(
				_templatesWebRequestHelper.getPermissionChecker(),
				_templatesWebRequestHelper.getLayout(), resourceName,
				ActionKeys.ADD_PORTLET_DISPLAY_TEMPLATE);
		}

		return PortletPermissionUtil.contains(
			_templatesWebRequestHelper.getPermissionChecker(),
			_templatesWebRequestHelper.getScopeGroupId(),
			_templatesWebRequestHelper.getLayout(), resourceName,
			ActionKeys.ADD_PORTLET_DISPLAY_TEMPLATE, false, false);
	}

	protected long getClassNameId() {
		return ParamUtil.getLong(_renderRequest, "classNameId");
	}

	protected long getClassPK() {
		return ParamUtil.getLong(_renderRequest, "classPK");
	}

	protected UnsafeConsumer<DropdownItem, Exception>
		getCreationMenuDropdownItem(PortletURL url, String label) {

		return dropdownItem -> {
			dropdownItem.setHref(url);
			dropdownItem.setLabel(
				LanguageUtil.get(
					_templatesWebRequestHelper.getRequest(), label));
		};
	}

	protected List<DropdownItem> getFilterNavigationDropdownItems() {
		return DropdownItemListBuilder.add(
			dropdownItem -> {
				dropdownItem.setActive(true);
				dropdownItem.setHref(getPortletURL(), "navigation", "all");
				dropdownItem.setLabel(
					LanguageUtil.get(
						_templatesWebRequestHelper.getRequest(), "all"));
			}
		).build();
	}

	protected String getKeywords() {
		return ParamUtil.getString(_renderRequest, "keywords");
	}

	protected UnsafeConsumer<DropdownItem, Exception> getOrderByDropdownItem(
		String orderByCol) {

		return dropdownItem -> {
			dropdownItem.setActive(orderByCol.equals(getOrderByCol()));
			dropdownItem.setHref(getPortletURL(), "orderByCol", orderByCol);
			dropdownItem.setLabel(
				LanguageUtil.get(
					_templatesWebRequestHelper.getRequest(), orderByCol));
		};
	}

	protected List<DropdownItem> getOrderByDropdownItems() {
		return DropdownItemListBuilder.add(
			getOrderByDropdownItem("modified-date")
		).add(
			getOrderByDropdownItem("id")
		).build();
	}

	protected PortletURL getPortletURL() {
		PortletURL portletURL = _renderResponse.createRenderURL();

		String mvcPath = ParamUtil.getString(_renderRequest, "mvcPath");

		if (Validator.isNotNull(mvcPath)) {
			portletURL.setParameter("mvcPath", mvcPath);
		}

		String tabs1 = ParamUtil.getString(_renderRequest, "tabs1");

		if (Validator.isNotNull(tabs1)) {
			portletURL.setParameter("tabs1", tabs1);
		}

		long templateId = ParamUtil.getLong(_renderRequest, "templateId");

		if (templateId != 0) {
			portletURL.setParameter("templateId", String.valueOf(templateId));
		}

		long classNameId = getClassNameId();

		if (classNameId != 0) {
			portletURL.setParameter("classNameId", String.valueOf(classNameId));
		}

		if (classNameId != 0) {
			portletURL.setParameter("classPK", String.valueOf(getClassPK()));
		}

		long resourceClassNameId = getResourceClassNameId();

		if (resourceClassNameId != 0) {
			portletURL.setParameter(
				"resourceClassNameId", String.valueOf(resourceClassNameId));
		}

		String refererPortletName = getRefererPortletName();

		if (Validator.isNotNull(refererPortletName)) {
			portletURL.setParameter("refererPortletName", refererPortletName);
		}

		String delta = ParamUtil.getString(_renderRequest, "delta");

		if (Validator.isNotNull(delta)) {
			portletURL.setParameter("delta", delta);
		}

		String eventName = ParamUtil.getString(_renderRequest, "eventName");

		if (Validator.isNotNull(eventName)) {
			portletURL.setParameter("eventName", eventName);
		}

		String keywords = getKeywords();

		if (Validator.isNotNull(keywords)) {
			portletURL.setParameter("keywords", keywords);
		}

		String orderByCol = getOrderByCol();

		if (Validator.isNotNull(orderByCol)) {
			portletURL.setParameter("orderByCol", orderByCol);
		}

		String orderByType = getOrderByType();

		if (Validator.isNotNull(orderByType)) {
			portletURL.setParameter("orderByType", orderByType);
		}

		boolean showAncestorScopes = showAncestorScopes();

		if (showAncestorScopes) {
			portletURL.setParameter(
				"showAncestorScopes", String.valueOf(showAncestorScopes));
		}

		portletURL.setParameter(
			TemplatesPortletKeys.CURRENT_TAB_ID_PARAMETER_NAME,
			getSelectedTab());

		return portletURL;
	}

	protected long getResourceClassNameId() {
		long resourceClassNameId = ParamUtil.getLong(
			_renderRequest, "resourceClassNameId");

		if (resourceClassNameId == 0) {
			resourceClassNameId = PortalUtil.getClassNameId(
				PortletDisplayTemplate.class);
		}

		return resourceClassNameId;
	}

	protected long getSearchRestrictionClassNameId() {
		return ParamUtil.getLong(
			_templatesWebRequestHelper.getRequest(),
			"searchRestrictionClassNameId");
	}

	protected long getSearchRestrictionClassPK() {
		return ParamUtil.getLong(
			_templatesWebRequestHelper.getRequest(),
			"searchRestrictionClassPK");
	}

	protected long getStructureClassNameId() {
		DDMDisplay ddmDisplay = getDDMDisplay();

		return PortalUtil.getClassNameId(ddmDisplay.getStructureType());
	}

	protected long[] getTemplateClassNameIds() {
		DDMDisplay ddmDisplay = getDDMDisplay();

		return ddmDisplay.getTemplateClassNameIds(getClassNameId());
	}

	protected List<TemplateHandler> getTemplateHandlers()
		throws PortalException {

		List<TemplateHandler> templateHandlers = new ArrayList<>();

		if (getClassNameId() > 0) {
			TemplateHandler templateHandler =
				TemplateHandlerRegistryUtil.getTemplateHandler(
					getClassNameId());

			if (containsAddPortletDisplayTemplatePermission(
					templateHandler.getResourceName())) {

				templateHandlers.add(templateHandler);
			}
		}
		else {
			templateHandlers =
				PortletDisplayTemplateUtil.getPortletDisplayTemplateHandlers();

			Iterator<TemplateHandler> iterator = templateHandlers.iterator();

			while (iterator.hasNext()) {
				TemplateHandler templateHandler = iterator.next();

				if (!containsAddPortletDisplayTemplatePermission(
						templateHandler.getResourceName())) {

					iterator.remove();
				}
			}
		}

		ListUtil.sort(
			templateHandlers,
			new TemplateHandlerComparator(
				_templatesWebRequestHelper.getLocale()));

		return templateHandlers;
	}

	protected String getTemplateMode() {
		DDMDisplay ddmDisplay = getDDMDisplay();

		return ParamUtil.getString(
			_renderRequest, "mode", ddmDisplay.getTemplateMode());
	}

	protected String getTemplateTypeValue() {
		DDMDisplay ddmDisplay = getDDMDisplay();

		String scopeTemplateType = ddmDisplay.getTemplateType();

		String templateTypeValue = StringPool.BLANK;

		if (scopeTemplateType.equals(
				DDMTemplateConstants.TEMPLATE_TYPE_DISPLAY)) {

			templateTypeValue = DDMTemplateConstants.TEMPLATE_TYPE_DISPLAY;
		}
		else if (scopeTemplateType.equals(
					DDMTemplateConstants.TEMPLATE_TYPE_FORM)) {

			templateTypeValue = DDMTemplateConstants.TEMPLATE_TYPE_FORM;
		}

		return templateTypeValue;
	}

	protected boolean hasResults(String context) throws Exception {
		if (getTotalItems(context) > 0) {
			return true;
		}

		return false;
	}

	protected void setDDMTemplateInstanceSearchResults(
			TemplateSearch templateSearch)
		throws Exception {

		TemplateSearchTerms searchTerms =
			(TemplateSearchTerms)templateSearch.getSearchTerms();
		DDMDisplay ddmDisplay = getDDMDisplay();

		ThemeDisplay themeDisplay = (ThemeDisplay)_renderRequest.getAttribute(
			WebKeys.THEME_DISPLAY);

		long[] groupIds = ddmDisplay.getTemplateGroupIds(
			themeDisplay, showAncestorScopes());

		List<DDMTemplate> results = _ddmTemplateService.search(
			_templatesWebRequestHelper.getCompanyId(), groupIds,
			getTemplateClassNameIds(), _getDDMTemplateClassPKs(),
			getResourceClassNameId(), searchTerms.getKeywords(),
			searchTerms.getType(), getTemplateMode(), searchTerms.getStatus(),
			templateSearch.getStart(), templateSearch.getEnd(),
			templateSearch.getOrderByComparator());

		templateSearch.setResults(results);
	}

	protected void setDDMTemplateInstanceSearchTotal(
			TemplateSearch templateSearch)
		throws Exception {

		TemplateSearchTerms searchTerms =
			(TemplateSearchTerms)templateSearch.getSearchTerms();
		DDMDisplay ddmDisplay = getDDMDisplay();

		ThemeDisplay themeDisplay = (ThemeDisplay)_renderRequest.getAttribute(
			WebKeys.THEME_DISPLAY);

		long[] groupIds = ddmDisplay.getTemplateGroupIds(
			themeDisplay, showAncestorScopes());

		int total = _ddmTemplateService.searchCount(
			_templatesWebRequestHelper.getCompanyId(), groupIds,
			getTemplateClassNameIds(), _getDDMTemplateClassPKs(),
			getResourceClassNameId(), searchTerms.getKeywords(),
			searchTerms.getType(), getTemplateMode(), searchTerms.getStatus());

		templateSearch.setTotal(total);
	}

	protected boolean showAncestorScopes() {
		return ParamUtil.getBoolean(_renderRequest, "showAncestorScopes");
	}

	private long[] _getDDMTemplateClassPKs() {
		if (getClassPK() > 0) {
			return new long[] {getClassPK()};
		}

		return null;
	}

	private final DDMDisplayRegistry _ddmDisplayRegistry;
	private final DDMTemplateHelper _ddmTemplateHelper;
	private final DDMTemplateService _ddmTemplateService;
	private final DDMWebConfiguration _ddmWebConfiguration;
	private final InfoItemRendererTracker _infoItemRendererTracker;
	private final RenderRequest _renderRequest;
	private final RenderResponse _renderResponse;
	private String _selectedTab;
	private final StorageAdapterRegistry _storageAdapterRegistry;
	private final TemplatesWebRequestHelper _templatesWebRequestHelper;

}