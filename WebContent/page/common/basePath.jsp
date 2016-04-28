<%@ page language="java" contentType="text/html; charset=utf-8"
    pageEncoding="utf-8"%>
<%
	String basePath = request.getScheme()+"://"+request.getServerName()
					+":"+request.getServerPort()+request.getContextPath()+"/"; 

%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<script type="text/javascript" src="<%=basePath %>js/base.js"></script>
</head>
<body>
	<input type="hidden" value="<%=basePath%>" id="basePath"/>
</body>
</html>