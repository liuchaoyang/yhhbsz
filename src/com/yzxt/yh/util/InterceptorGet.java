package com.yzxt.yh.util;

import org.apache.struts2.ServletActionContext;

import com.opensymphony.xwork2.ActionInvocation;
import com.opensymphony.xwork2.interceptor.AbstractInterceptor;

public class InterceptorGet extends AbstractInterceptor{
	private static final long serialVersionUID = 1L;

	@Override
	public String intercept(ActionInvocation arg0) throws Exception {
		String method = ServletActionContext.getRequest().getMethod();
		if(method.equalsIgnoreCase("POST")){
			return arg0.invoke();
		}
		return "header.jsp";
	}

}
