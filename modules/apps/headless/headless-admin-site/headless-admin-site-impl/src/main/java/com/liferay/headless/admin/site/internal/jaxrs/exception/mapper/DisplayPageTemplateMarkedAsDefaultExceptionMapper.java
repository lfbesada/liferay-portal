package com.liferay.headless.admin.site.internal.jaxrs.exception.mapper;

import com.liferay.layout.page.template.exception.LayoutPageTemplateEntryDefaultTemplateException;
import com.liferay.layout.utility.page.exception.DefaultLayoutUtilityPageEntryException;
import com.liferay.portal.vulcan.jaxrs.exception.mapper.BaseExceptionMapper;
import com.liferay.portal.vulcan.jaxrs.exception.mapper.Problem;
import org.osgi.service.component.annotations.Component;

import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

@Component(
	property = {
		"osgi.jaxrs.application.select=(osgi.jaxrs.name=Liferay.Headless.Admin.Site)",
		"osgi.jaxrs.extension=true",
		"osgi.jaxrs.name=Liferay.Headless.Admin.Site.DisplayPageTemplateMarkedAsDefaultExceptionMapper"
	},
	service = ExceptionMapper.class
)
@Provider
public class DisplayPageTemplateMarkedAsDefaultExceptionMapper
	extends BaseExceptionMapper<LayoutPageTemplateEntryDefaultTemplateException> {
	@Override
	protected Problem getProblem(
		LayoutPageTemplateEntryDefaultTemplateException layoutPageTemplateEntryDefaultTemplateException) {

		return new Problem(layoutPageTemplateEntryDefaultTemplateException);
	}
}
