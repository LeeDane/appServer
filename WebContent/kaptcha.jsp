<%@ page language="java" contentType="text/html; charset=utf-8"
    pageEncoding="utf-8"%>
<%@ include file="/page/common/basePath.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<title>测试kaptcha验证码</title>
</head>
<body>                         
        <form method="POST">  
            <table>  
                <tr>  
                    <td>用户名：</td>  
                    <td><input type="text" name="userId" value=""></td>  
                </tr>  
                <tr>  
                    <td>密码：</td>  
                    <td><input type="password" name="passwd"></td>  
                </tr>  
                <tr>  
                    <td>验证码：</td>  
                    <td><input type="text" name="userId"></td>  
                    <td><img src="<%=basePath %>Kaptcha.jpg"></td>  
                </tr>  
                <tr>  
                    <td><input type="submit" name="submit"></td>  
                </tr>  
            </table>  
        </form>  

</body>
</html>