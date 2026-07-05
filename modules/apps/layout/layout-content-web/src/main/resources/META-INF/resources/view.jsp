<%--
/**
 * SPDX-FileCopyrightText: (c) 2026 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */
--%>

<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>

<%@ include file="/init.jsp" %>

<%
LayoutContentVersionDisplayContext layoutContentVersionDisplayContext = (LayoutContentVersionDisplayContext)request.getAttribute(LayoutContentVersionWebKeys.LAYOUT_CONTENT_VERSION_DISPLAY_CONTEXT);

Map<String, Object> context = layoutContentVersionDisplayContext.getContext();

Map<String, Object> configMap = (Map<String, Object>)context.get("config");

String listURL = (String)configMap.get("pageSpecificationVersionsURL");
%>

<%--
Temporarily disabled while the WIP smoke sidebar is in use; the React panel
occludes the sidebar on load. Restore the react:component when the WIP is
dropped.

<react:component
	module="{VersionHistory} from layout-content-web"
	props="<%= context %>"
/>
--%>

<aui:style type="text/css">
	.layout-content-versioning-sidebar {
		background: #fff;
		border: 1px solid #e7e7ed;
		border-radius: 4px;
		box-shadow: 0 4px 12px rgba(0, 0, 0, 0.15);
		max-height: calc(100vh - 8rem);
		overflow-y: auto;
		padding: 1rem;
		position: fixed;
		right: 1rem;
		top: 7rem;
		width: 360px;
		z-index: 1050;
	}

	.layout-content-versioning-sidebar h4 {
		margin: 0 0 0.25rem 0;
	}

	.layout-content-versioning-sidebar .layout-content-versioning-sidebar-tag {
		color: #6b6c7e;
		font-size: 0.7rem;
		font-weight: 400;
	}

	.layout-content-versioning-sidebar .layout-content-versioning-sidebar-url {
		color: #6b6c7e;
		font-size: 0.7rem;
		margin: 0 0 0.75rem 0;
		word-break: break-all;
	}

	.layout-content-versioning-sidebar .layout-content-versioning-sidebar-status {
		color: #6b6c7e;
		font-size: 0.75rem;
		margin-bottom: 0.5rem;
	}

	.layout-content-versioning-sidebar details {
		border: 1px solid #e7e7ed;
		border-radius: 4px;
		margin-bottom: 0.5rem;
	}

	.layout-content-versioning-sidebar summary {
		cursor: pointer;
		padding: 0.5rem 0.75rem;
	}

	.layout-content-versioning-sidebar .layout-content-versioning-sidebar-body {
		border-top: 1px solid #e7e7ed;
		padding: 0.5rem 0.75rem;
	}

	.layout-content-versioning-sidebar .layout-content-versioning-sidebar-meta {
		color: #6b6c7e;
		font-size: 0.75rem;
	}

	.layout-content-versioning-sidebar .layout-content-versioning-sidebar-erc {
		color: #aaa;
		font-size: 0.7rem;
		word-break: break-all;
	}

	.layout-content-versioning-sidebar .layout-content-versioning-sidebar-status-value {
		color: #6b6c7e;
		font-size: 0.75rem;
	}

	.layout-content-versioning-sidebar pre {
		background: #f7f8f9;
		border-radius: 4px;
		font-size: 0.7rem;
		margin: 0.5rem 0 0 0;
		max-height: 200px;
		overflow: auto;
		padding: 0.5rem;
		white-space: pre-wrap;
		word-break: break-all;
	}

	.layout-content-versioning-sidebar-error {
		color: #da1414;
	}
</aui:style>

<aside class="layout-content-versioning-sidebar">
	<h4>Page Version History <span class="layout-content-versioning-sidebar-tag">(WIP)</span></h4>

	<p class="layout-content-versioning-sidebar-url"><%= listURL %></p>

	<div class="layout-content-versioning-sidebar-status" id="<portlet:namespace />status">Loading...</div>

	<div id="<portlet:namespace />versions"></div>
</aside>

<aui:script>
	(async () => {
		const statusEl = document.getElementById('<portlet:namespace />status');
		const target = document.getElementById('<portlet:namespace />versions');

		try {
			const response = await Liferay.Util.fetch('<%= listURL %>');

			if (!response.ok) {
				statusEl.innerHTML =
					'<span class="layout-content-versioning-sidebar-error">HTTP ' +
					response.status +
					'</span>';

				return;
			}

			const data = await response.json();
			const items = data.items || [];

			statusEl.textContent =
				items.length +
				' version' +
				(items.length === 1 ? '' : 's') +
				' — totalCount: ' +
				(data.totalCount ?? '?');

			if (items.length === 0) {
				return;
			}

			target.innerHTML = items
				.map((item, idx) => {
					const fmt = (date) => {
						if (!date) return '';
						return new Date(date).toLocaleString();
					};

					const safeHTML = (text) =>
						String(text ?? '').replace(
							/[&<>"]/g,
							(char) =>
								({
									'&': '&amp;',
									'<': '&lt;',
									'>': '&gt;',
									'"': '&quot;',
								})[char]
						);

					const title =
						item.version !== undefined && item.version !== null
							? 'v' + item.version
							: '#' + (idx + 1);
					const name =
						typeof item.name === 'string' && item.name
							? item.name
							: '—';
					const status = item.status || '—';
					const dateCreated = fmt(item.dateCreated);
					const erc = item.externalReferenceCode || '';

					return (
						'<details>' +
						'<summary>' +
						'<strong>' +
						safeHTML(title) +
						'</strong> · ' +
						'<span>' +
						safeHTML(name) +
						'</span> · ' +
						'<span class="layout-content-versioning-sidebar-status-value">' +
						safeHTML(status) +
						'</span>' +
						'</summary>' +
						'<div class="layout-content-versioning-sidebar-body">' +
						'<div class="layout-content-versioning-sidebar-meta">created: ' +
						safeHTML(dateCreated) +
						'</div>' +
						'<div class="layout-content-versioning-sidebar-erc">erc: ' +
						safeHTML(erc) +
						'</div>' +
						'<pre>' +
						safeHTML(JSON.stringify(item, null, 2)) +
						'</pre>' +
						'</div>' +
						'</details>'
					);
				})
				.join('');
		}
		catch (error) {
			statusEl.innerHTML =
				'<span class="layout-content-versioning-sidebar-error">' +
				error.message +
				'</span>';
		}
	})();
</aui:script>