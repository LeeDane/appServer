package com.cn.leedane.file.upload;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts2.dispatcher.multipart.JakartaMultiPartRequest;

public class RequestParseWrapper extends JakartaMultiPartRequest {

	 public void parse(HttpServletRequest servletRequest, String saveDir)throws IOException {
		 System.out.println("执行RequestParseWrapper的parse()方法");
    }
	 
	 
}
