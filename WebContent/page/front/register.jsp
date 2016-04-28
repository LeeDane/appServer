<%@ page language="java" contentType="text/html; charset=utf-8"
    pageEncoding="utf-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<title>Insert title here</title>
</head>

<script type="text/javascript" src="../../js/others/jquery-1.9.1.min.js"></script>
<script type="text/javascript" src="../../js/front/loginAndRegister.js"></script>

<body>
	<table border="1" style="text-align: center;">
		<tr>
			<td>
				用户名：<input type="text" id="userName" name="userName" value="Lee"/>
			</td>		
		</tr>
		<tr>
			<td>
				邮箱：<input type="text" id="userEmail" name="userEmail" value="123@qq.com"/>
			</td>		
		</tr>
		<tr>
			<td>
				密码：<input type="text" id="userPassword" name="userPassword" value="123"/>
			</td>
		</tr>
		<tr>
			<td>
				<input type="button" value="Register" onclick="register();"/>
			</td>
		</tr>
	</table>
</body>
</html>