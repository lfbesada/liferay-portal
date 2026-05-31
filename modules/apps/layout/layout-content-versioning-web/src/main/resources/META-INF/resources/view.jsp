<%--
/**
 * SPDX-FileCopyrightText: (c) 2026 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */
--%>

<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>

<%@ include file="/init.jsp" %>

<%@ page import="com.liferay.portal.kernel.model.Layout" %><%@
page import="com.liferay.portal.kernel.service.LayoutLocalServiceUtil" %>

<%
Layout draftLayout = themeDisplay.getLayout();

Layout publishedLayout = LayoutLocalServiceUtil.getLayout(draftLayout.getClassPK());

String siteExternalReferenceCode = themeDisplay.getScopeGroup(
).getExternalReferenceCode();
String sitePageExternalReferenceCode = publishedLayout.getExternalReferenceCode();

String listURL = "/o/headless-admin-site/v1.0/sites/" + siteExternalReferenceCode + "/site-pages/" + sitePageExternalReferenceCode + "/page-specification-versions";
%>

<aside class="layout-content-versioning-sidebar" style="background: #fff; border-left: 1px solid #e7e7ed; bottom: 0; overflow-y: auto; padding: 1rem; position: fixed; right: 0; top: 56px; width: 320px; z-index: 100;">
	<h4 style="margin: 0 0 0.25rem 0;">Page Version History <span style="color: #6b6c7e; font-size: 0.7rem; font-weight: 400;">(WIP)</span></h4>

	<p style="color: #6b6c7e; font-size: 0.7rem; margin: 0 0 0.75rem 0; word-break: break-all;"><%= listURL %></p>

	<div id="<portlet:namespace />status" style="color: #6b6c7e; font-size: 0.75rem; margin-bottom: 0.5rem;">Loading...</div>

	<div id="<portlet:namespace />versions"></div>
</aside>

<script>
	(async () => {
		const statusEl = document.getElementById(
			'<portlet:namespace />status');
		const target = document.getElementById(
			'<portlet:namespace />versions');

		try {
			const response = await Liferay.Util.fetch('<%= listURL %>');

			if (!response.ok) {
				statusEl.innerHTML =
					'<span style="color:#da1414;">HTTP ' + response.status +
						'</span>';

				return;
			}

			const data = await response.json();
			const items = data.items || [];

			statusEl.textContent = items.length + ' version' +
				(items.length === 1 ? '' : 's') + ' — totalCount: ' +
					(data.totalCount ?? '?');

			if (items.length === 0) {
				return;
			}

			target.innerHTML = items.map((item, idx) => {
				const fmt = (date) => {
					if (!date) return '';
					return new Date(date).toLocaleString();
				};

				const safeHTML = (text) => String(text ?? '').replace(
					/[&<>"]/g,
					(char) => ({
						'&': '&amp;',
						'<': '&lt;',
						'>': '&gt;',
						'"': '&quot;'
					})[char]);

				const title = (item.version !== undefined && item.version !== null) ?
					'v' + item.version : '#' + (idx + 1);
				const name = (typeof item.name === 'string' && item.name) ?
					item.name : '—';
				const status = item.status || '—';
				const dateCreated = fmt(item.dateCreated);
				const erc = item.externalReferenceCode || '';

				return '<details style="border: 1px solid #e7e7ed; border-radius: 4px; margin-bottom: 0.5rem;">' +
					'<summary style="cursor: pointer; padding: 0.5rem 0.75rem;">' +
						'<strong>' + safeHTML(title) + '</strong> · ' +
						'<span>' + safeHTML(name) + '</span> · ' +
						'<span style="color: #6b6c7e; font-size: 0.75rem;">' + safeHTML(status) + '</span>' +
					'</summary>' +
					'<div style="border-top: 1px solid #e7e7ed; padding: 0.5rem 0.75rem;">' +
						'<div style="color: #6b6c7e; font-size: 0.75rem;">created: ' + safeHTML(dateCreated) + '</div>' +
						'<div style="color: #aaa; font-size: 0.7rem; word-break: break-all;">erc: ' + safeHTML(erc) + '</div>' +
						'<pre style="background: #f7f8f9; border-radius: 4px; font-size: 0.7rem; margin: 0.5rem 0 0 0; max-height: 200px; overflow: auto; padding: 0.5rem; white-space: pre-wrap; word-break: break-all;">' +
							safeHTML(JSON.stringify(item, null, 2)) +
						'</pre>' +
					'</div>' +
				'</details>';
			}).join('');
		}
		catch (error) {
			statusEl.innerHTML =
				'<span style="color:#da1414;">' + error.message + '</span>';
		}
	})();
</script>