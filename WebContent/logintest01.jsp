<%@ page language="java" contentType="text/html; charset=utf-8"
    pageEncoding="utf-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html lang="en">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<title>登录</title>
</head>
<link href="/leedane/other/bootstrap-flatui-master/dist/css/flat-ui.min.css" rel="stylesheet">

<script type="text/javascript">
	function login(){
		var url = "user_login.action";
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
		//window.open("page/front/register.jsp");
		var loadi = layer.load('努力加载中…'); //需关闭加载层时，执行layer.close(loadi)即可
		var url = "user_registerUser.action";
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
</script>

<body>
	<!-- <table border="1" style="text-align: center;">
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
				<a href = 'http://www.baidu.com'>点击百度</a>
			</td>
		</tr>
	</table> -->
	<!-- <div class="container">
		<div class="login">
	        <div class="login-screen">
	          <div class="container" style="margin:0 auto;">
					<div class="row">
						<div class="span12">
							<form class="form-horizontal">
								<div class="input-group" style="margin-top:20px;">
							         <span class="input-group-addon">账号</span>
							         <input type="text" class="form-control" placeholder="您的账号/邮箱" style="width: 300px;">
							     </div>
								<div class="input-group" style="margin-top:20px;">
							         <span class="input-group-addon">密码</span>
							         <input type="password" class="form-control" placeholder="您的密码" style="width: 300px;">
							     </div>
								<div class="control-group" style="margin-top:20px;">
									<div class="controls"  style="margin-top:20px;">
										 <label><input type="checkbox" />记住我一周</label> 
										 <label style="padding-top:10px;"><a href="#">忘记密码？</a></label>
									</div>
								</div>
								<div class="btn-group" data-toggle="buttons">
								   <label class="btn btn-primary active">
								      <input type="radio" name="options" id="option1" checked>用户
								   </label>
								   <label class="btn btn-primary">
								      <input type="radio" name="options" id="option2">管理员
								   </label>
								   <label class="btn btn-primary">
								      <input type="radio" name="options" id="option3">商家
								   </label>
								</div>	
								<div>
									<button type="button" class="btn btn-primary">立即登录</button>
								</div>
								
							</form>
						</div>
					</div>
				</div>
	        </div>
	      </div>
	 </div> -->
	 <span class="fui-info-circle"></span>
</body>
</html>