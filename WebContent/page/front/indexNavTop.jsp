<%@ page language="java" contentType="text/html; charset=utf-8"
    pageEncoding="utf-8"%>
<%@ include file="../common/basePath.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html lang="zh-CN">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">

<link type="text/css" rel="stylesheet" href="<%=basePath %>other/bootstrap-3.3.5/css/bootstrap.min.css">
<link type="text/css" rel="stylesheet" href="<%=basePath %>css/common.css">
<!-- <link type="text/css" rel="stylesheet" href="other/bootstrap-3.2.0/css/bootstrap-switch.min.css">
<link type="text/css" rel="stylesheet" href="bootstrap-3.2.0/css/highlight.css">
<link type="text/css" rel="stylesheet" href="bootstrap-3.2.0/css/main.css"> -->
<script type="text/javascript" src="<%=basePath %>js/others/jquery-1.9.1.min.js"></script>
<script type="text/javascript" src="<%=basePath %>other/bootstrap-3.3.5/js/bootstrap.min.js"></script>
<!--<script type="text/javascript" src="<%=basePath %>other/bootstrap-3.2.0/js/bootstrap-switch.min.js"></script>
 <script type="text/javascript" src="<%=basePath %>other/bootstrap-3.2.0/js/highlight.js"></script>
<script type="text/javascript" src="<%=basePath %>other/bootstrap-3.2.0/js/main.js"></script> -->
<script type="text/javascript" src="<%=basePath %>js/front/loginAndRegister.js"></script>
<script type="text/javascript" src="<%=basePath %>other/bootstrap-stickup-master/stickUp.min.js"></script>
<script type="text/javascript" src="<%=basePath %>other/layer/layer.js"></script>

</head>
<body>
	<!-- <div style="background-color: white;width: 100%;height: 51px;position: fixed;margin-top: -50px;"> -->
	
		<nav class="navbar navbar-default" role="navigation" style="width: 100%; margin:0 auto;">
		   <div class="navbar-header">
		      <a class="navbar-brand" href="#">LeeDane</a>
		   </div>
		   <div>
		      <ul class="nav navbar-nav">
		         <li class="active"><a href="<%=basePath %>page/front/index.jsp">首页</a></li>
		         <li class=""><a href="#">个人中心</a></li>
		         <li class="dropdown">
		            <a href="#" class="dropdown-toggle" data-toggle="dropdown">更多 
		               <b class="caret"></b>
		            </a>
		            <ul class="dropdown-menu">
		               <li><a href="#">生活助手</a></li>
		               <li><a href="#">公告/新闻</a></li>
		               <li><a href="#">Jasper Report</a></li>
		               <li class="divider"></li>
		               <li><a href="#">分离的链接</a></li>
		               <li class="divider"></li>
		               <li><a href="#">另一个分离的链接</a></li>
		            </ul>
		         </li>
		      </ul>
		   </div>
		    <form class="navbar-form navbar-left" role="search">
		         <div class="form-group">
		            <input type="text" class="form-control" style="width:300px;" placeholder="Search">
		         </div>
		         <div class="btn-group" role="group">
				    <button type="button" class="btn btn-default">全文检索</button>
				    <button type="submit" class="btn btn-default">标题检索</button>
				 </div>
		         
		    </form>   
			<label class="navbar-text navbar-right" style="margin-right: 10px;margin-top: 0px !important;">      	  
					<div class="btn-group" role="group">
					    <button type="button" class="btn btn-info dropdown-toggle" data-toggle="dropdown" aria-haspopup="true" aria-expanded="false">
					      <img src="<%=basePath %>images/four.jpg" class="img-circle" style="width: 20px;height: 18px;margin-right: 5px;">LeeDane
					      <span class="caret"></span>
					    </button>
					    <ul class="dropdown-menu" style="background-color: #31b0d5;">
					      <li><a href="#">个人中心</a></li>
					      <li><a href="<%=basePath %>page/admin/index1.jsp" target="_bland">管理员后台</a></li>
					      <li><a href="#">退出</a></li>
					    </ul>
				  </div>
		      	</label>
	      	<div>
		      	
	  		</div>
		</nav>
	
	
	<!-- 模态框（Modal） -->
	<div class="modal fade" id="login" tabindex="-1" role="dialog" 
	   aria-labelledby="myModalLabel" aria-hidden="true">
	   <div class="modal-dialog">
	      <div class="modal-content">
	         <div class="modal-header">
	            <button type="button" class="close" 
	               data-dismiss="modal" aria-hidden="true">
	                  &times;
	            </button>
	            <h4 class="modal-title" id="myModalLabel">用户登录<button type="button" class="btn btn-primary" data-toggle="modal" data-target="#register" data-dismiss="modal">免费注册</button></h4>
	         </div>
	         <div class="modal-body">
	           
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
							</form>
						</div>
					</div>
				</div>
	           
	         </div>
	         <div class="modal-footer">
	            <button type="button" class="btn btn-default" 
	               data-dismiss="modal">关闭
	            </button>
	            <button type="button" class="btn btn-primary">立即登录</button>
	         </div>
	      </div><!-- /.modal-content -->
		</div><!-- /.modal -->
	</div>
	
	<!-- 模态框（Modal）注册 -->
	<div class="modal fade" id="register" tabindex="-1" role="dialog" 
	   aria-labelledby="myModalLabel" aria-hidden="true">
	   <div class="modal-dialog">
	      <div class="modal-content">
	         <div class="modal-header">
	            <button type="button" class="close" 
	               data-dismiss="modal" aria-hidden="true">
	               &times;
	            </button>
	            <h4 class="modal-title" id="myModalLabel">用户注册<button type="button" class="btn btn-primary" data-toggle="modal" data-target="#login" data-dismiss="modal">立即登录</button></h4>
	         </div>
	         <div class="modal-body">
	           
	           <div class="container" style="margin:0 auto;">
					<div class="row">
						<div class="span12">
							<form class="form-horizontal">
								<div class="input-group" style="margin-top:20px;">
							         <span class="input-group-addon">*账号</span>
							         <input type="text" class="form-control" placeholder="账号，不分大小写" style="width: 300px;">
							     </div>
							     <div class="input-group" style="margin-top:20px;">
							         <span class="input-group-addon">*邮箱</span>
							         <input type="password" class="form-control" placeholder="邮箱，注意格式要正确" style="width: 300px;">
							     </div>
								<div class="input-group" style="margin-top:20px;">
							         <span class="input-group-addon">*密码</span>
							         <input type="password" class="form-control" placeholder="8位以上的密码" style="width: 300px;">
							     </div>
							     <div class="btn-group" data-toggle="buttons" style="margin-top:20px;">
								   <label class="btn btn-primary active">
								      <input type="radio" name="optionsRegister" id="option1" checked>用户
								   </label>
								   <label class="btn btn-primary">
								      <input type="radio" name="optionsRegister" id="option2">管理员
								   </label>
								   <label class="btn btn-primary">
								      <input type="radio" name="optionsRegister" id="option3">商家
								   </label>
								</div>
							</form>
						</div>
					</div>
				</div>
	           
	         </div>
	         <div class="modal-footer">
	            <button type="button" class="btn btn-default" 
	               data-dismiss="modal">关闭
	            </button>
	            <button type="button" class="btn btn-primary">免费注册</button>
	         </div>
	      </div><!-- /.modal-content -->
		</div><!-- /.modal -->
	</div>
	
	<!-- 底部 -->
	<!-- <nav class="navbar navbar-default navbar-fixed-bottom" role="navigation">
	   <div class="navbar-header">
	      <a class="navbar-brand" href="#">W3Cschool</a>
	   </div>
	   <div>
	      <ul class="nav navbar-nav">
	         <li class="active"><a href="#">iOS</a></li>
	         <li><a href="#">SVN</a></li>
	         <li class="dropdown">
	            <a href="#" class="dropdown-toggle" data-toggle="dropdown">
	               Java <b class="caret"></b>
	            </a>
	            <ul class="dropdown-menu">
	               <li><a href="#">jmeter</a></li>
	               <li><a href="#">EJB</a></li>
	               <li><a href="#">Jasper Report</a></li>
	               <li class="divider"></li>
	               <li><a href="#">分离的链接</a></li>
	               <li class="divider"></li>
	               <li><a href="#">另一个分离的链接</a></li>
	            </ul>
	         </li>
	      </ul>
	   </div>
	</nav> -->
</body>
</html>
	<!-- <div class="loginAndRegister" style="width:600px;margin-left: 100px;margin-top: 200px;">
          <div id="loginAndRegisterTitles">
          		<div class="loginAndRegisterTitle"  id="isLogin" style="background-color: blue;" title="点击切换登录">Login</div>
          		<div class="loginAndRegisterTitle" id="isRegister" style="background-color: gray;" title="点击切换注册">Register</div>
          </div>
          <div class="loginAndRegisterMain" id="loginContent">
          		<div id="loginMain">
          			<div style="margin-top: 10px;"><input onblur="inputBlur(this)" class="inputAccount" type="text" value="hah" title="账号或者邮箱"/></div>
          			<div style="margin-top: 30px;"><input onblur="inputBlur(this)" class="inputPassword" type="password" value="hah" title="密码"/></div>
          			<div style="margin-top: 30px;"><div><input onblur="inputBlur(this)" type="text" class="inputVerificationCode" value="hah" title="验证码"/><img src="images/password.png" style="width: 150px;height: 30px;border: 3px solid #717077;"/></div></div>
          			<div style="margin-top: 20px;" class="loginAndRegisterType">登录类型:<input type="radio" name="loginType" value="0"/>买家<input type="radio" name="loginType" value="1"/>商家<input type="radio" name="loginType" value="3"/>客服<input type="radio" name="loginType" value="2"/>管理员</div>
          			<div style="margin-top: 30px;"><div class="submitStyle">提交</div></div>
          			<div style="margin-top: 10px;">忘记密码？点此找回</div>
          		</div>
          </div>
          <div class="loginAndRegisterMain" id="registerContent" style="display: none;">
          		<div id="registerMain" >
          			<div style="margin-top: 10px;"><input onblur="inputBlur(this)" class="inputAccount" type="text" value="hah" title="账号"/></div>
          			<div style="margin-top: 30px;"><input onblur="inputBlur(this)" class="inputPassword" type="password" value="hah" title="密码"/></div>
          			<div style="margin-top: 30px;"><input onblur="inputBlur(this)" class="inputEmail" type="text" value="hah" title="邮箱"/></div>
          			<div style="margin-top: 30px;"><div><input onblur="inputBlur(this)" type="text" class="inputVerificationCode" value="hah" title="验证码"/><img src="images/password.png" style="width: 150px;height: 30px;border: 3px solid #717077;"/></div></div>
          			<div style="margin-top: 20px;" class="loginAndRegisterType">注册类型:<input type="radio" name="registerType" value="0"/>买家<input type="radio" name="registerType" value="1"/>商家<input type="radio" name="registerType" value="3"/>客服</div>
          			<div style="margin-top: 30px;"><div class="submitStyle">提交</div></div>
          			<div style="margin-top: 10px;">忘记密码？点此找回</div>
          		</div>
          </div>
      </div> -->
