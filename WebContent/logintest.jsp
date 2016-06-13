<%@ page language="java" contentType="text/html; charset=utf-8"
    pageEncoding="utf-8"%>
<%@ include file="/page/common/basePath.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<title>Insert title here</title>
</head>

<script type="text/javascript" src="js/others/jquery-1.9.1.min.js"></script>
<script type="text/javascript" src="js/others/jquery.md5.js"></script>
<script type="text/javascript" src="other/layer/layer.js"></script>

<script type="text/javascript">
	function login(){
		var basePath = $("#basePath").val();
		var url = basePath + "user_login.action";
		var userName = $("#userName").val();
		var userPassword = $("#userPassword").val();	
		var data = '{"account" : "'+userName+'", "password" : "'+$.md5(userPassword)+'"}';
		$.ajax({
			type:'post',
			data:{params : data},
			url: url,
			dataType:'json',
			success:function(data){
				var msg = JSON.parse(data);
				if(msg.isSuccess){
					alert(msg.message);
				}else{
					alert(msg.message);
				}
			}		
		});
	}
	
	function register(){
		var basePath = $("#basePath").val();
		//window.open("page/front/register.jsp");
		var loadi = layer.load('努力加载中…'); //需关闭加载层时，执行layer.close(loadi)即可
		var url = basePath +"user_registerUser.action";
		var userName = $("#userName").val();
		var userPassword = $("#userPassword").val();		
		var data = '{"account" : "'+userName+'", "password" : "'+$.md5(userPassword)+'"}';
		
		$.ajax({
			type:'post',
			data:{params : data},
			url: url,
			dataType:'json',
			success:function(data){
				layer.close(loadi);
				var msg = JSON.parse(data);
				if(msg.isSuccess){
					alert(msg.message);
				}else{
					if(msg.isAccount){
						layer.tips('账号已经被占用了', "#userName", {
						    style: ['background-color:#78BA32; color:#fff', '#78BA32'],
						    maxWidth:185,
						    guide: 1,
						    time: 2,
						    closeBtn:[0, false]
						});
					}else{
						layer.tips('邮箱已经被占用了!', '#email', {time: 2});
					}
				}
				//layer.msg('错误,请稍后重试...', 1, -1);
			}		
		});
	}
	
	function againSendRegisterEmail(){
		var basePath = $("#basePath").val();
		//window.open("page/front/register.jsp");
		var loadi = layer.load('邮件发送中…'); //需关闭加载层时，执行layer.close(loadi)即可
		var url = basePath +"user_againSendRegisterEmail.action";
		var userName = $("#userName").val();
		var userPassword = $("#userPassword").val();		
		var data = '{"account" : "'+userName+'", "password" : "'+$.md5(userPassword)+'"}';
		
		$.ajax({
			type:'post',
			data:{params:data},
			url: url,
			dataType:'json',
			success:function(data){
				layer.close(loadi);
				var msg = JSON.parse(data);
				alert(msg.message);
			}		
		});
	}
</script>

<body>
	<table border="1" style="text-align: center;">
		<tr>
			<td>
				用户名：<input type="text" id="userName" name="userName" value="Lee"/>
			</td>		
		</tr>
		<tr>
			<td>
				密码：<input type="text" id="userPassword" name="userPassword" value="123"/>
			</td>
		</tr>
		<tr>
			<td>
				<input type="button" value="Login" onclick="login();"/>
				<input type="button" value="register" onclick="register();"/>
				<input type="button" value="再次发送邮件" onclick="againSendRegisterEmail();"/>
				<a href = 'http://www.baidu.com'>点击百度</a>
			</td>
		</tr>
	</table>
</body>
</html>