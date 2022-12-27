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

import ClayButton from '@clayui/button';
import ClayLayout from '@clayui/layout';
import ClayToolbar from '@clayui/toolbar';
import {useIsMounted} from '@liferay/frontend-js-react-web';
import classNames from 'classnames';
import {fetch, objectToFormData, sub} from 'frontend-js-web';
import React, {useEffect, useRef, useState} from 'react';

import {setIn} from '../../app/utils/setIn';
import Collapse from '../../common/components/Collapse';
import {FieldSetContent} from '../browser/components/page-structure/components/item-configuration-panels/FieldSet';

export default function CollectionConfiguration({
	collection,
	collectionItemTypeLabel,
	collectionLabel,
	configurationDefinition,
	getCollectionItemCountURL,
	languageId,
	portletNamespace: namespace,
}) {
	const initialConfig = collection.config || {};

	const [itemConfig, setItemConfig] = useState(initialConfig);

	const handleFieldValueSelect = (fieldSet, name, value) => {
		const field = fieldSet.fields.find((field) => field.name === name);
		let nextConfig;

		if (field.localizable) {
			nextConfig = setIn(itemConfig, [name, languageId], value);
		}
		else {
			nextConfig = setIn(itemConfig, [name], value);
		}

		setItemConfig((previousItemConfig) => ({
			...previousItemConfig,
			...nextConfig,
		}));
	};

	return (
		<div className="cadmin">
			<div className="collection-configuration">
				<h1 className="sheet-title">
					{Liferay.Language.get('filter-collection')}
				</h1>

				<FilterInformationToolbar
					configurationDefinition={configurationDefinition}
					getCollectionItemCountURL={getCollectionItemCountURL}
					initialItemConfig={initialConfig}
					itemConfig={itemConfig}
					namespace={namespace}
					setItemConfig={setItemConfig}
				/>

				<p className="mb-1 text-secondary">
					<span className="font-weight-bold mr-1">
						{Liferay.Language.get('collection')}:
					</span>

					{collectionLabel}
				</p>

				<p className="text-secondary">
					<span className="font-weight-bold mr-1">
						{Liferay.Language.get('content-type')}:
					</span>

					{collectionItemTypeLabel}
				</p>

				<div className="collection-configuration__configuration-fieldsets">
					{configurationDefinition.fieldSets
						.filter((fieldSet) => fieldSet.fields.length)
						.map((fieldSet, index) => (
							<FieldSet
								fields={fieldSet.fields}
								key={`${fieldSet.label || ''}-${index}`}
								label={fieldSet.label}
								languageId={languageId}
								onValueSelect={(name, value) =>
									handleFieldValueSelect(
										fieldSet,
										name,
										value
									)
								}
								values={itemConfig}
							/>
						))}
				</div>
			</div>
		</div>
	);
}

function FieldSet({fields, label, languageId, onValueSelect, values}) {
	return label ? (
		<Collapse label={label} open>
			<FieldSetContent
				fields={fields}
				isCustomStylesFieldSet={false}
				item={{}}
				languageId={languageId}
				onValueSelect={onValueSelect}
				values={values}
			/>
		</Collapse>
	) : (
		<FieldSetContent
			fields={fields}
			isCustomStylesFieldSet={false}
			item={{}}
			languageId={languageId}
			onValueSelect={onValueSelect}
			values={values}
		/>
	);
}

const FilterInformationToolbar = ({
	configurationDefinition,
	getCollectionItemCountURL,
	initialItemConfig,
	itemConfig,
	namespace,
	setItemConfig,
}) => {
	const isMounted = useIsMounted();
	const [totalNumberOfItems, setTotalNumberOfItems] = useState(null);
	const [showAll, setShowAll] = useState(false);
	const [enableShowAll, setEnableShowAll] = useState(false);
	const filterInformationMessageElementRef = useRef();

	const hasConfigurationValues = !!Object.values(itemConfig).filter(
		(value) => !!value
	).length;

	const filterInformationMessage = getFilterInformationMessage(
		configurationDefinition,
		itemConfig
	);

	useEffect(() => {
		if (hasConfigurationValues) {
			fetch(getCollectionItemCountURL, {
				body: objectToFormData({
					[`${namespace}layoutObjectReference`]: JSON.stringify(
						itemConfig
					),
				}),
				method: 'POST',
			})
				.then((response) => response.json())
				.then(({totalNumberOfItems}) => {
					if (isMounted()) {
						setTotalNumberOfItems(totalNumberOfItems || 0);
					}
				});
		}
	}, [
		getCollectionItemCountURL,
		hasConfigurationValues,
		isMounted,
		itemConfig,
		namespace,
	]);

	useEffect(() => {
		const element = filterInformationMessageElementRef.current;
		if (element && element.offsetWidth < element.scrollWidth) {
			setEnableShowAll(true);
		}
		else {
			setEnableShowAll(false);
		}
	}, [filterInformationMessage]);

	return hasConfigurationValues && totalNumberOfItems !== null ? (
		<ClayToolbar className="mb-3" subnav={{displayType: 'primary'}}>
			<ClayLayout.ContainerFluid>
				<ClayToolbar.Nav>
					<ClayToolbar.Item className="pl-2 text-left" expand>
						<ClayToolbar.Section>
							<span
								className={classNames('component-text', {
									'mb-0': showAll,
									'text-truncate': !showAll,
								})}
								ref={filterInformationMessageElementRef}
							>
								{totalNumberOfItems === 1
									? sub(
											Liferay.Language.get(
												'there-is-1-result-for-x'
											),
											filterInformationMessage
									  )
									: sub(
											Liferay.Language.get(
												'there-are-x-results-for-x'
											),
											totalNumberOfItems,
											filterInformationMessage
									  )}
							</span>
						</ClayToolbar.Section>

						{enableShowAll && (
							<ClayToolbar.Section>
								<ClayButton
									className="btn-link font-weight-semi-bold pl-0 tbar-link"
									displayType="unstyled"
									onClick={() =>
										setShowAll(
											(previousShowAll) =>
												!previousShowAll
										)
									}
								>
									<span
										className="c-inner ml-0"
										tabIndex="-1"
									>
										{showAll
											? Liferay.Language.get('show-less')
											: Liferay.Language.get('show-all')}
									</span>
								</ClayButton>
							</ClayToolbar.Section>
						)}
					</ClayToolbar.Item>

					<ClayToolbar.Item>
						<ClayButton
							className="component-link tbar-link"
							displayType="unstyled"
							onClick={() => {
								setItemConfig(initialItemConfig);
							}}
						>
							{Liferay.Language.get('clear')}
						</ClayButton>
					</ClayToolbar.Item>
				</ClayToolbar.Nav>
			</ClayLayout.ContainerFluid>
		</ClayToolbar>
	) : null;
};

function getFilterInformationMessage(
	configurationDefinition,
	configurationValues
) {
	if (!configurationDefinition || !configurationValues) {
		return null;
	}

	const fields = configurationDefinition.fieldSets[0].fields;

	return Object.entries(configurationValues)
		.filter(([_name, value]) => !!value)
		.map(([name, value]) => {
			const field = fields.find((field) => field.name === name);

			if (field?.type === 'select') {
				return Array.isArray(value)
					? value.map((v) => getFieldLabel(field, v)).join(', ')
					: value;
			}

			return value;
		})
		.join(', ');
}

function getFieldLabel(field, value) {
	return (
		field.typeOptions?.validValues.find(
			(validValue) => validValue.value === value
		)?.label ?? value
	);
}
