<%@ page language="java" contentType="text/html; charset=utf-8"
    pageEncoding="utf-8"%>
<%@ include file="page/common/basePath.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html lang="en">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<title>404页面找不到</title>
</head>
<style type="text/css">  
	#ShowDiv{ font-size:18px;}  
</style> 
<body>
       
<script language='javascript' type='text/javascript'>     
var secs =5; //倒计时的秒数         
var URL ;         
function Load(url){         
	URL =url;         
	for(var i=secs;i>=0;i--){         
		window.setTimeout('doUpdate(' + i + ')', (secs-i) * 1000);         
	}         
}         
function doUpdate(num){         
	document.getElementById('ShowDiv').innerHTML = '哎呦！页面不存在或者已经失效,系统将在 <strong>'+num+'</strong> 秒后自动跳转到登录页面' ;         
	if(num == 0){ 
		window.location=URL; 
	}         
}          
</script>     
      
</head>         
<body>         
	
<div id="ShowDiv"></div>         
<script language="javascript">     
Load("<%=basePath %>login-admin.jsp"); //要跳转到的页面         
</script>     

</body>
</html>