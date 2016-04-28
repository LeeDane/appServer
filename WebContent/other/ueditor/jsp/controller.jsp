<%@ page language="java" contentType="text/html; charset=UTF-8"
	import="com.baidu.ueditor.ActionEnter"
    pageEncoding="UTF-8"%>
<%-- 页面执行到此打开报错，导致页面控制台提示上传功能不能使用，
注释掉就正常，这句话在此的作用：
是使jsp输出的html时去除多余的空行(jsp上使用EL和tag会产生大量的空格和空行)
那为什么在此会报错呢？是版本不兼容吗？
<%@ page trimDirectiveWhitespaces="true" %> 
--%>
<%

    request.setCharacterEncoding( "utf-8" );
	response.setHeader("Content-Type" , "text/html");
	
	String rootPath = application.getRealPath( "/" );
	System.out.println(rootPath);
	out.write( new ActionEnter( request, rootPath ).exec() );
	
%>