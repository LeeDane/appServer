package com.cn.leedane.filter;

import java.io.IOException;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.struts2.dispatcher.ng.filter.StrutsPrepareAndExecuteFilter;

public class FileUploadFilter extends StrutsPrepareAndExecuteFilter {

	//Dispatcher dispatcher;
	//private ServletContext servletContext;
	@Override
	public void destroy() {
		

	}

	@Override
	public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse,
			FilterChain filterChain) throws IOException, ServletException {
		//prepareDispatcherAndWrapRequest((HttpServletRequest)servletRequest,(HttpServletResponse)servletResponse);
		HttpServletRequest request = (HttpServletRequest) servletRequest;
		HttpServletResponse response = (HttpServletResponse) servletResponse;
		request.setCharacterEncoding("utf-8");
		response.setCharacterEncoding("utf-8");
		  String contentType = request.getContentType();
		  	System.out.println("进入过滤器");
		  try {

		   if (contentType != null && contentType.equals("multipart/form-data")) {
			   filterChain.doFilter(servletRequest, servletResponse);
		   } else {
			   //使用默认的过滤器
			   super.doFilter(servletRequest, servletResponse, filterChain);  
			   //filterChain.doFilter(servletRequest, servletResponse);
		   }
		  } catch (Exception e) {
		   e.printStackTrace();
		  }

	}

	@Override
	public void init(FilterConfig config) throws ServletException {
		//servletContext = config.getServletContext();

	}
	
	protected HttpServletRequest prepareDispatcherAndWrapRequest(HttpServletRequest request, HttpServletResponse response) throws ServletException {  
		  
		      /*  Dispatcher du = Dispatcher.getInstance();// (1)  
		  
		        if (du == null) {  
		            Dispatcher.setInstance(dispatcher);  
		            dispatcher.prepare(request, response);// (2)  
		        } else {  
		            dispatcher = du;  
		        }  
		          
		        try {  
		            request = dispatcher.wrapRequest(request, getServletContext());// (3)  
		        } catch (IOException e) {  
		            String message = "Could not wrap servlet request with MultipartRequestWrapper!";  
		            throw new ServletException(message, e);  
		        }  
		  
		        return request;  */
		return null;
		    }

	/*private ServletContext getServletContext() {
		return servletContext;
	}*/


}
