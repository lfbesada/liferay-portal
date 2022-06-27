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

const wrapper = fragmentElement;

const inputElement = document.getElementById(fragmentNamespace + '-file-upload');
const fileName = wrapper.querySelector('.forms-file-upload-file-name');
const removeButton = wrapper.querySelector("[type='button']");
const selectButton = wrapper.querySelector('.btn-secondary');

const selectFromDocumentLibrary = input.attributes.selectFromDocumentLibrary;
const selectFromDocumentLibraryURL = input.attributes.selectFromDocumentLibraryURL;

if (layoutMode === 'edit') {
	if (selectButton) {
		selectButton.classList.add('disabled');
	}
}

function onInputChange() {
	fileName.innerText = inputElement.files[0].name;

	removeButton.classList.remove('d-none');
	removeButton.addEventListener('click', onRemoveFile);
}

function onRemoveFile() {
	inputElement.value = '';
	fileName.innerText = '';

	removeButton.classList.add('d-none');
	removeButton.removeEventListener('click', onRemoveFile);
}

function onSelectFile(event) {
	if (selectFromDocumentLibrary) {
		event.preventDefault();

		Liferay.Util.openSelectionModal({
			customSelectEvent: true,
			multiple: true,
			onSelect(selectedItem) {
				console.log("onSelect selectedItem:");
				console.log(selectedItem);
			},
			selectEventName: 'fileSelected',
			title: 'Select Document',
			url: selectFromDocumentLibraryURL,
		});
	}
}

function fileSelected(selectedItem) {
	console.log("selectedItem:");
	console.log(selectedItem);
}

inputElement.addEventListener('change', onInputChange);
selectButton.addEventListener('click', onSelectFile);