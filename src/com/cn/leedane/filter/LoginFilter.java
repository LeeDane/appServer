package com.cn.leedane.filter;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;

import org.apache.struts2.dispatcher.ng.filter.StrutsPrepareAndExecuteFilter;

/**
 * 系统是否登录的过滤器
 * @author LeeDane
 * 2015年9月2日 下午3:20:56
 * Version 1.0
 */
public class LoginFilter extends StrutsPrepareAndExecuteFilter{

	@Override
	public void doFilter(ServletRequest request, ServletResponse response,
			FilterChain chain) throws IOException, ServletException {
		//System.out.println("过滤器");
		super.doFilter(request, response, chain);
	}

}
