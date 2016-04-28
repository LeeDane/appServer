<%@ page language="java" contentType="text/html; charset=utf-8"
    pageEncoding="utf-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<title>管理员中心</title>

<style type="text/css">
	*{
		padding:0px;
		margin: 0px;
	}
</style>
</head>
<body style="background-color: #999999">
	  <div id="admin">
		<%@ include file="topNav.jsp"%>
		<div id="main" style="clear: both;">
			<div id="left">			
				<%@ include file="leftNav.jsp"%>
			</div>		
			
			<div id="right" >
				<iframe src="welcome.jsp" minwidth="200" width="800" id="win" name="win" onload="Javascript:SetWinHeight(this)" frameborder="0" scrolling="no">
        		</iframe> 
			</div>
		</div>	
	</div>-
</body>
</html>