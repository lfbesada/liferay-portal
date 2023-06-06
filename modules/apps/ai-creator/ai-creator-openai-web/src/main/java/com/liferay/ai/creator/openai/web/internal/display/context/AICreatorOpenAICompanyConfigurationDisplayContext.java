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

package com.liferay.ai.creator.openai.web.internal.display.context;

import com.liferay.ai.creator.openai.configuration.manager.AICreatorOpenAIConfigurationManager;
import com.liferay.ai.creator.openai.web.internal.constants.AICreatorOpenAIPortletKeys;
import com.liferay.portal.kernel.exception.SystemException;
import com.liferay.portal.kernel.module.configuration.ConfigurationException;
import com.liferay.portal.kernel.portlet.LiferayWindowState;
import com.liferay.portal.kernel.portlet.RequestBackedPortletURLFactory;
import com.liferay.portal.kernel.portlet.RequestBackedPortletURLFactoryUtil;
import com.liferay.portal.kernel.theme.ThemeDisplay;

import javax.portlet.PortletMode;
import javax.portlet.PortletModeException;
import javax.portlet.PortletURL;
import javax.portlet.WindowStateException;

import javax.servlet.http.HttpServletRequest;

/**
 * @author Lourdes Fernández Besada
 */
public class AICreatorOpenAICompanyConfigurationDisplayContext {

	public AICreatorOpenAICompanyConfigurationDisplayContext(
		AICreatorOpenAIConfigurationManager aiCreatorOpenAIConfigurationManager,
		ThemeDisplay themeDisplay) {

		_aiCreatorOpenAIConfigurationManager =
			aiCreatorOpenAIConfigurationManager;
		_themeDisplay = themeDisplay;
	}

	public String getAICreatorOpenAIPortletURL(
			HttpServletRequest httpServletRequest)
		throws PortletModeException, WindowStateException {

		RequestBackedPortletURLFactory requestBackedPortletURLFactory =
			RequestBackedPortletURLFactoryUtil.create(httpServletRequest);

		PortletURL portletURL =
			requestBackedPortletURLFactory.createControlPanelRenderURL(
				AICreatorOpenAIPortletKeys.AI_CREATOR_OPENAI,
				_themeDisplay.getScopeGroup(),
				_themeDisplay.getRefererGroupId(), 0);

		try {
			portletURL.setPortletMode(PortletMode.VIEW);
		}
		catch (PortletModeException portletModeException) {
			throw new SystemException(portletModeException);
		}

		try {
			portletURL.setWindowState(LiferayWindowState.POP_UP);
		}
		catch (WindowStateException windowStateException) {
			throw new SystemException(windowStateException);
		}

		portletURL.setParameter("mvcPath", "/view.jsp");

		return portletURL.toString();
	}

	public String getAPIKey() throws ConfigurationException {
		return _aiCreatorOpenAIConfigurationManager.
			getAICreatorOpenAICompanyAPIKey(_themeDisplay.getCompanyId());
	}

	public boolean isEnabled() throws ConfigurationException {
		return _aiCreatorOpenAIConfigurationManager.
			isAICreatorOpenAICompanyEnabled(_themeDisplay.getCompanyId());
	}

	private final AICreatorOpenAIConfigurationManager
		_aiCreatorOpenAIConfigurationManager;
	private final ThemeDisplay _themeDisplay;

}