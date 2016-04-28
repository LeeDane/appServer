package com.cn.leedane.entity;

import javax.servlet.http.HttpServletRequest;

/**
 * 浏览器信息实体
 * @author LeeDane
 * 2015年5月29日 下午1:36:18
 * Version 1.0
 */
public class Browser {
	private String ip; //IP地址
	//private String browser;//操作的浏览器名称
	
	public Browser convertRequest(HttpServletRequest request){
		 //处理服务器的IP
    	ip = request.getHeader("X-Cluster-Client-Ip");
    	ip = ip != null && ip.length() > 0 ? ip : request.getRemoteAddr();
		return new Browser();
	}
	
	
	
}
