/**
 * SPDX-FileCopyrightText: (c) 2024 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.headless.admin.site.internal.dto.v1_0.converter;

import com.liferay.fragment.entry.processor.constants.FragmentEntryProcessorConstants;
import com.liferay.fragment.model.FragmentEntry;
import com.liferay.fragment.model.FragmentEntryLink;
import com.liferay.fragment.service.FragmentEntryLinkLocalService;
import com.liferay.fragment.service.FragmentEntryLocalService;
import com.liferay.fragment.util.configuration.FragmentConfigurationField;
import com.liferay.fragment.util.configuration.FragmentEntryConfigurationParser;
import com.liferay.headless.admin.site.dto.v1_0.ConfigurationFieldValue;
import com.liferay.headless.admin.site.dto.v1_0.DefaultFragmentReference;
import com.liferay.headless.admin.site.dto.v1_0.FragmentInstancePageElementDefinition;
import com.liferay.headless.admin.site.dto.v1_0.FragmentItemExternalReference;
import com.liferay.headless.admin.site.dto.v1_0.PageElementDefinition;
import com.liferay.headless.admin.site.dto.v1_0.Scope;
import com.liferay.layout.util.structure.FragmentStyledLayoutStructureItem;
import com.liferay.portal.kernel.json.JSONObject;
import com.liferay.portal.kernel.model.Group;
import com.liferay.portal.kernel.service.GroupLocalService;
import com.liferay.portal.kernel.util.ArrayUtil;
import com.liferay.portal.kernel.util.SetUtil;
import com.liferay.portal.kernel.util.Validator;
import com.liferay.portal.vulcan.dto.converter.DTOConverter;
import com.liferay.portal.vulcan.dto.converter.DTOConverterContext;
import com.liferay.portal.vulcan.dto.converter.DefaultDTOConverterContext;

import java.util.HashMap;
import java.util.Map;

import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

/**
 * @author Eudaldo Alonso
 */
@Component(
	property = "dto.class.name=com.liferay.layout.util.structure.FragmentStyledLayoutStructureItem",
	service = DTOConverter.class
)
public class FragmentInstancePageElementDefinitionDTOConverter
	implements DTOConverter
		<FragmentStyledLayoutStructureItem,
		 FragmentInstancePageElementDefinition> {

	@Override
	public String getContentType() {
		return FragmentInstancePageElementDefinition.class.getSimpleName();
	}

	@Override
	public FragmentInstancePageElementDefinition toDTO(
			DTOConverterContext dtoConverterContext,
			FragmentStyledLayoutStructureItem fragmentStyledLayoutStructureItem)
		throws Exception {

		FragmentEntryLink fragmentEntryLink =
			_fragmentEntryLinkLocalService.fetchFragmentEntryLink(
				fragmentStyledLayoutStructureItem.getFragmentEntryLinkId());

		if (fragmentEntryLink == null) {
			throw new UnsupportedOperationException();
		}

		return new FragmentInstancePageElementDefinition() {
			{
				setConfiguration(fragmentEntryLink::getConfiguration);
				setCss(fragmentEntryLink::getCss);
				setCssClasses(
					() -> {
						if (SetUtil.isEmpty(
								fragmentStyledLayoutStructureItem.
									getCssClasses())) {

							return null;
						}

						return ArrayUtil.toStringArray(
							fragmentStyledLayoutStructureItem.getCssClasses());
					});
				setCustomCSS(fragmentStyledLayoutStructureItem::getCustomCSS);
				setDatePropagated(fragmentEntryLink::getLastPropagationDate);
				setDraftFragmentInstanceExternalReferenceCode(
					() -> _getDraftFragmentInstanceExternalReferenceCode(
						fragmentEntryLink));
				setFragmentConfig(
					() -> _getFragmentConfigMap(fragmentEntryLink));
				setFragmentInstanceExternalReferenceCode(
					fragmentEntryLink::getExternalReferenceCode);
				setFragmentReference(
					() -> {
						if ((fragmentEntryLink.getFragmentEntryId() == 0) &&
							Validator.isNull(
								fragmentEntryLink.getRendererKey())) {

							return null;
						}

						FragmentEntry fragmentEntry =
							_fragmentEntryLocalService.fetchFragmentEntry(
								fragmentEntryLink.getFragmentEntryId());

						if (fragmentEntry != null) {
							return new FragmentItemExternalReference() {
								{
									setExternalReferenceCode(
										fragmentEntry::
											getExternalReferenceCode);
									setFragmentReferenceType(
										() ->
											FragmentReferenceType.
												FRAGMENT_ITEM_EXTERNAL_REFERENCE);
									setScope(
										() -> {
											if (fragmentEntry.getGroupId() ==
													fragmentEntryLink.
														getGroupId()) {

												return null;
											}

											Group group =
												_groupLocalService.getGroup(
													fragmentEntry.getGroupId());

											return new Scope() {
												{
													setExternalReferenceCode(
														group::
															getExternalReferenceCode);
													setType(() -> Type.SITE);
												}
											};
										});
								}
							};
						}

						if (fragmentEntryLink.getFragmentEntryId() > 0) {
							return null;
						}

						return new DefaultFragmentReference() {
							{
								setDefaultFragmentKey(
									fragmentEntryLink::getRendererKey);
								setFragmentReferenceType(
									() ->
										FragmentReferenceType.
											DEFAULT_FRAGMENT_REFERENCE);
							}
						};
					});
				setFragmentType(
					() -> {
						if (fragmentEntryLink.isTypeComponent()) {
							return FragmentType.BASIC;
						}

						return FragmentType.FORM;
					});
				setHtml(fragmentEntryLink::getHtml);
				setIndexed(fragmentStyledLayoutStructureItem::isIndexed);
				setJs(fragmentEntryLink::getJs);
				setName(fragmentStyledLayoutStructureItem::getName);
				setNamespace(fragmentEntryLink::getNamespace);
				setType(PageElementDefinition.Type.FRAGMENT);
				setUuid(fragmentEntryLink::getUuid);
			}
		};
	}

	private String _getDraftFragmentInstanceExternalReferenceCode(
		FragmentEntryLink fragmentEntryLink) {

		long originalFragmentEntryLinkId =
			fragmentEntryLink.getOriginalFragmentEntryLinkId();

		if (originalFragmentEntryLinkId == 0) {
			return null;
		}

		FragmentEntryLink originalFragmentEntryLink =
			_fragmentEntryLinkLocalService.fetchFragmentEntryLink(
				originalFragmentEntryLinkId);

		if (originalFragmentEntryLink == null) {
			return null;
		}

		return originalFragmentEntryLink.getExternalReferenceCode();
	}

	private DTOConverterContext _getDTOConverterContext(
		long companyId, long scopeGroupId) {

		DTOConverterContext dtoConverterContext =
			new DefaultDTOConverterContext(null, null, null, null, null);

		dtoConverterContext.setAttribute("companyId", companyId);
		dtoConverterContext.setAttribute("scopeGroupId", scopeGroupId);

		return dtoConverterContext;
	}

	private Map<String, ConfigurationFieldValue> _getFragmentConfigMap(
			FragmentEntryLink fragmentEntryLink)
		throws Exception {

		JSONObject editableValuesJSONObject =
			fragmentEntryLink.getEditableValuesJSONObject();

		JSONObject freeMarkerJSONObject =
			editableValuesJSONObject.getJSONObject(
				FragmentEntryProcessorConstants.
					KEY_FREEMARKER_FRAGMENT_ENTRY_PROCESSOR);

		if (freeMarkerJSONObject == null) {
			return null;
		}

		JSONObject configurationJSONObject =
			fragmentEntryLink.getConfigurationJSONObject();

		if (configurationJSONObject == null) {
			return null;
		}

		Map<String, ConfigurationFieldValue> map = new HashMap<>();

		DTOConverterContext dtoConverterContext = _getDTOConverterContext(
			fragmentEntryLink.getCompanyId(), fragmentEntryLink.getGroupId());

		for (FragmentConfigurationField fragmentConfigurationField :
				_fragmentEntryConfigurationParser.
					getFragmentConfigurationFields(
						fragmentEntryLink.getConfigurationJSONObject())) {

			if (!freeMarkerJSONObject.has(
					fragmentConfigurationField.getName())) {

				continue;
			}

			dtoConverterContext.setAttribute(
				"fragmentConfigurationFieldValue",
				freeMarkerJSONObject.get(fragmentConfigurationField.getName()));

			map.put(
				fragmentConfigurationField.getName(),
				_configurationFieldValueDTOConverter.toDTO(
					dtoConverterContext, fragmentConfigurationField));
		}

		return map;
	}

	@Reference(
		target = "(component.name=com.liferay.headless.admin.site.internal.dto.v1_0.converter.ConfigurationFieldValueDTOConverter)"
	)
	private DTOConverter<FragmentConfigurationField, ConfigurationFieldValue>
		_configurationFieldValueDTOConverter;

	@Reference
	private FragmentEntryConfigurationParser _fragmentEntryConfigurationParser;

	@Reference
	private FragmentEntryLinkLocalService _fragmentEntryLinkLocalService;

	@Reference
	private FragmentEntryLocalService _fragmentEntryLocalService;

	@Reference
	private GroupLocalService _groupLocalService;

}