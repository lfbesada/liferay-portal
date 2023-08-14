<%--
/**
 * SPDX-FileCopyrightText: (c) 2023 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */
--%>

<%@ include file="/init.jsp" %>

<%
LockedLayoutsDisplayContext lockedLayoutsDisplayContext = (LockedLayoutsDisplayContext)request.getAttribute(LockedLayoutsDisplayContext.class.getName());
%>

<clay:sheet-section>
	<clay:content-row
		containerElement="h3"
		cssClass="sheet-subtitle"
	>
		<clay:content-col
			expand="<%= true %>"
		>
			<span class="heading-text"><liferay-ui:message key="manual-unlocking" /></span>
		</clay:content-col>
	</clay:content-row>

	<c:if test="<%= lockedLayoutsDisplayContext.existLockedLayouts() %>">
		<clay:content-row>
			<clay:content-col
				expand="<%= true %>"
			>
				<p class="text-secondary"><liferay-ui:message key="select-the-pages-that-you-want-to-manually-unlock-and-save.-please-note-that-the-current-user-may-lose-control-over-the-edition" /></p>
			</clay:content-col>
		</clay:content-row>
	</c:if>

	<clay:content-row>
		<clay:content-col
			expand="<%= true %>"
		>
			<c:if test="<%= lockedLayoutsDisplayContext.existLockedLayouts() %>">
				<clay:management-toolbar
					managementToolbarDisplayContext="<%= new LockedLayoutsSearchContainerManagementToolbarDisplayContext(request, liferayPortletRequest, liferayPortletResponse, lockedLayoutsDisplayContext) %>"
				/>
			</c:if>

			<liferay-ui:search-container
				id="lockedLayoutsSearchContainer"
				searchContainer="<%= lockedLayoutsDisplayContext.getSearchContainer() %>"
			>
				<liferay-ui:search-container-row
					className="com.liferay.site.admin.web.internal.display.context.LockedLayoutsDisplayContext.LockedLayoutDTO"
					escapedModel="<%= true %>"
					keyProperty="plid"
					modelVar="lockedLayoutDTO"
				>
					<liferay-ui:search-container-column-text
						cssClass="modify-text"
						name="name"
						value="<%= lockedLayoutDTO.getName() %>"
					/>

					<liferay-ui:search-container-column-text
						cssClass="modify-text"
						name="type"
						value="<%= lockedLayoutDTO.getLayoutType() %>"
					/>

					<liferay-ui:search-container-column-text
						cssClass="modify-text"
						name="current-user"
						value="<%= lockedLayoutDTO.getUserName() %>"
					/>

					<liferay-ui:search-container-column-text
						cssClass="modify-text"
						name="last-autosave"
						value="<%= lockedLayoutDTO.getLastAutoSave() %>"
					/>
				</liferay-ui:search-container-row>

				<liferay-ui:search-iterator
					markupView="lexicon"
					paginate="<%= false %>"
				/>
			</liferay-ui:search-container>
		</clay:content-col>
	</clay:content-row>
</clay:sheet-section>