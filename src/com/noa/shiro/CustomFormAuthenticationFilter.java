package com.noa.shiro;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;

import org.apache.shiro.web.filter.authc.FormAuthenticationFilter;
import org.apache.shiro.web.util.WebUtils;

public class CustomFormAuthenticationFilter extends FormAuthenticationFilter {
	// �Զ������֤�ɹ����ҳ����ת
	@Override
	protected void issueSuccessRedirect(ServletRequest request, ServletResponse response) throws Exception {

		WebUtils.issueRedirect(request, response, "/home", null, true);
	}

}
