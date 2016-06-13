<%@ page language="java" contentType="text/html; charset=utf-8"
    pageEncoding="utf-8"%>
<%@ include file="/page/common/basePath.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<title>登录</title>
</head>

<script type="text/javascript" src="<%=basePath %>js/others/jquery-1.9.1.min.js"></script>
<script type="text/javascript" src="<%=basePath %>js/others/jquery.md5.js"></script>
<script type="text/javascript" src="<%=basePath %>other/layer/layer.js"></script>

<link rel="stylesheet" type="text/css" href="<%=basePath %>other/ext-4.1.1/resources/css/ext-all.css" />
<script type="text/javascript" src="<%=basePath %>other/ext-4.1.1/ext-all.js"></script>
<link rel="stylesheet" type="text/css" href="<%=basePath %>other/ext-4.1.1/examples/shared/example.css" />
<!-- Example -->

<script type="text/javascript">
	function login(){
		var loadi = layer.load('正在登录…'); //需关闭加载层时，执行layer.close(loadi)即可
		var url = getBasePath() + "user_login.action";
		var userName = $("#userName").val();
		var userPassword = $("#userPassword").val();	
		var data = '{"account" : "'+userName+'", "password" : "'+$.md5(userPassword)+'"}';
		$.ajax({
			type:'post',
			data:{params : data},
			url: url,
			dataType:'json',
			success:function(msg){
				if(msg.isSuccess){
					layer.msg(msg.message);
				}else{
					layer.msg(msg.message);
				}
				layer.close(loadi);
			}		
		});
	}
	
	function register(){
		//window.open("page/front/register.jsp");
		var loadi = layer.load('努力加载中…'); //需关闭加载层时，执行layer.close(loadi)即可
		var url = getBasePath() +"user_registerUser.action";
		var userName = $("#userName").val();
		var userPassword = $("#userPassword").val();		
		var data = '{"account" : "'+userName+'", "password" : "'+$.md5(userPassword)+'"}';
		
		$.ajax({
			type:'post',
			data:{params : data},
			url: url,
			dataType:'json',
			success:function(msg){
				layer.close(loadi);
				if(msg.isSuccess){
					alert(msg.message);
				}else{
					if(msg.isAccount){
						layer.tips(msg.message, "#userName", {
						    style: ['background-color:#78BA32; color:#fff', '#78BA32'],
						    maxWidth:185,
						    guide: 1,
						    time: 3000,
						    closeBtn:[0, false]
						});
					}else{
						layer.tips(msg.message, "#email", {
						    style: ['background-color:#78BA32; color:#fff', '#78BA32'],
						    maxWidth:185,
						    guide: 1,
						    time: 3000,
						    closeBtn:[0, false]
						});
					}
				}
				//layer.msg('错误,请稍后重试...', 1, -1);
			}		
		});
	}
	
	function againSendRegisterEmail(){
		//window.open("page/front/register.jsp");
		var loadi = layer.load('邮件发送中…'); //需关闭加载层时，执行layer.close(loadi)即可
		var url = getBasePath() +"user_againSendRegisterEmail.action";
		var userName = $("#userName").val();
		var userPassword = $("#userPassword").val();		
		var data = '{"account" : "'+userName+'", "password" : "'+$.md5(userPassword)+'"}';
		
		$.ajax({
			type:'post',
			data:{"params":data},
			url: url,
			dataType:'json',
			success:function(msg){
				layer.close(loadi);
				alert(msg.message);
			}		
		});
	}
	
Ext.require([
             //'Ext.form.*',
             //'Ext.layout.container.Column',
             //'Ext.tab.Panel'
             '*'
         ]);

Ext.onReady(function() {
        	 
  	 var isLogin = true;
  	 //var basePath = Ext.get("basePath").getValue();
	 var url = getBasePath() + "user_login.action";
       Ext.QuickTips.init();

       var bd = Ext.getBody();
       var msgTip;
       /*
        * ================  Form 4  =======================
        */
       Ext.get("showForm").createChild({tag: 'h2', html: 'Form 4 - Forms can be a TabPanel...'});
       var required = '<span style="color:red;font-weight:bold" data-qtip="Required">*</span>';

       var tabs = Ext.widget({
           xtype: 'form',
           id: 'tabForm',
           width: 350,
           border: false,
           bodyBorder: false,
           //renderTo: Ext.get("showForm"),
           activeTab: 1,
           fieldDefaults: {
               labelWidth: 75,
               msgTarget: 'side'
           },
           items: {
               xtype:'tabpanel',
               activeTab: 0,
               defaults:{
                   bodyPadding: 10,
                   layout: 'anchor'
               },

               items:[{
                   title:'用户登录',
                   defaultType: 'textfield',
                   //click: function(){alert('sss');},
                   id: "abcde",
                   defaults: {
                       anchor: '100%'
                   },
                   listeners : {activate : function(tab, newc, oldc) {
                  	 //页面加载的时候进来时oldc为null
                  	 if(oldc != null && oldc != 'undefined'){
                  		var values = this.up('form').getForm().getValues();
                   		console.log(values['account']);
                  		Ext.get("login").dom.innerHTML = '<em id="login-btnWrap"><button id="login-btnEl" type="button" class="x-btn-center" hidefocus="true" role="button" autocomplete="off" style="width: 69px; height: 16px;"><span id="login-btnInnerEl" class="x-btn-inner" style="width: 69px;">login</span><span id="login-btnIconEl" class="x-btn-icon "></span></button></em>';
                  		Ext.get("other").dom.innerHTML = '<em id="other-btnWrap"><button id="other-btnEl" type="button" class="x-btn-center" hidefocus="true" role="button" autocomplete="off" height: 16px;"><span id="other-btnInnerEl" class="x-btn-inner" style>findPassword</span><span id="other-btnIconEl" class="x-btn-icon "></span></button></em>';
                  		isLogin = true;
                  	 }
            	 }},
                   items: [{
                       fieldLabel: '&nbsp;&nbsp;&nbsp;账&nbsp;&nbsp;&nbsp;号',
                       name: 'account',
                       afterLabelTextTpl: required,
                       allowBlank: false
                   },{
                  	 xtype:'textfield',
                       fieldLabel: '电子邮箱',
                       name: 'email',
                       afterLabelTextTpl: required,
                       allowBlank: false,
                       vtype:'email'
                   },{
                       fieldLabel: '&nbsp;&nbsp;&nbsp;密&nbsp;&nbsp;&nbsp;码',
                       afterLabelTextTpl: required,
                       allowBlank: false,
                      // inputType: 'password',  以.表示用户输入的密码
                       name: 'password'
                   }]
               },{
                   title: '用户注册',
                   defaultType: 'textfield',
                   defaults: {
                       anchor: '100%'
                   },
                   listeners : {activate : function(tab, newc, oldc) {
                  	 //页面加载的时候进来时oldc为null
                  	 if(oldc != null && oldc != 'undefined'){
                  		Ext.get("login").dom.innerHTML = '<em id="login-btnWrap"><button id="login-btnEl" type="button" class="x-btn-center" hidefocus="true" role="button" autocomplete="off" style="width: 69px; height: 16px;"><span id="login-btnInnerEl" class="x-btn-inner" style="width: 69px;">register</span><span id="login-btnIconEl" class="x-btn-icon "></span></button></em>';
                  		Ext.get("other").dom.innerHTML = '<em id="other-btnWrap"><button id="other-btnEl" type="button" class="x-btn-center" hidefocus="true" role="button" autocomplete="off" style="height: 16px;"><span id="other-btnInnerEl" class="x-btn-inner" style>sendEmailAgain</span><span id="other-btnIconEl" class="x-btn-icon "></span></button></em>';
                  		isLogin = false;
                  	 }
            	 }},
                   items: [{
                       fieldLabel: '&nbsp;&nbsp;&nbsp;账&nbsp;&nbsp;&nbsp;号',
                       name: 'account',
                       afterLabelTextTpl: required
                   },{
                       fieldLabel: '&nbsp;&nbsp;&nbsp;密&nbsp;&nbsp;&nbsp;码',
                       name: 'password',
                       afterLabelTextTpl: required
                   },{
                       fieldLabel: '确认密码',
                       name: 'aginPassword',
                       afterLabelTextTpl: required
                   },{
                       fieldLabel: '电子邮箱',
                       name: 'email',
                       afterLabelTextTpl: required,
                       vtype:'email'
                   }]
               }]
           },
           
           buttons: [{
               text: 'findPassword',
               id: 'other',
               handler: function() {
            	   if(this.up('form').getForm().isValid()){
            		   var loadi = layer.load('处理中…'); //需关闭加载层时，执行layer.close(loadi)即可
	            	   if(isLogin){//找密码
	            		   Ext.Ajax.request({  
	       	                   	url: getBasePath() +"user_findPassword.action",  
	       	                    method : 'post',  
	       	                   	params: {params: buildJsonObjectParams(this.up('form').getForm(),0,isLogin)},  
	       	                    success : function(response, options){  
	       		                    var text = response.responseText;
	       		                    var resp = Ext.decode(JSON.parse(text));
	       		                    if(resp.isSuccess){//成功，将跳转到首页
	       		                    	layer.close(loadi);
	       		                    	Ext.Msg.alert('成功提示', resp.message);       		               						
	       		                    }else{
	       		                    	layer.close(loadi);
	       		                    	Ext.Msg.alert('错误提示', resp.message);
	       		                    } 
	       	                   	},  
	       	                   	failure : function(response){
	       	                   		layer.close(loadi);
	       	                   		Ext.Msg.alert('系统提示', '出错了请联系管理员!');
	       	                   	}  
	       	                });
	            	   }else{//发邮件
	            		   Ext.Ajax.request({  
	       	                   	url: getBasePath() +"user_againSendRegisterEmail.action",  
	       	                    method : 'post',  
	       	                   	params: {params: buildJsonObjectParams(this.up('form').getForm(),1,isLogin)},  
	       	                    success : function(response, options){  
	       		                    var text = response.responseText;
	       		                    var resp = Ext.decode(JSON.parse(text));
	       		                    if(resp.isSuccess){//成功，将跳转到首页
	       		                    	layer.close(loadi);
	       		                    	Ext.Msg.alert('成功提示', resp.message);       		              
	       		                    }else{
	       		                    	layer.close(loadi);
	       		                    	Ext.Msg.alert('错误提示', resp.message);
	       		                    } 
	       	                   	},  
	       	                   	failure : function(response){
	       	                   		layer.close(loadi);
	       	                   		Ext.Msg.alert('系统提示', '出错了请联系管理员!');
	       	                   	}  
	       	                });
	            	   }
            	   }
               }
           },{
               text: 'login',
               id: 'login',
               handler: function() {
                   if(this.up('form').getForm().isValid()){          	   
                	 msgTip = Ext.MessageBox.show({title:'提示', msg:'正在加载,请稍后......'});

                  	 if(isLogin){//登录
                  		 console.log(buildJsonObjectParams(this.up('form').getForm(),0,isLogin));
                  		 Ext.Ajax.request({  
       	                   	url: url,  
       	                    method : 'post',  
       	                   	params: {params: buildJsonObjectParams(this.up('form').getForm(),0,isLogin)},  
       	                    success : function(response, options){  
       		                    var text = response.responseText;
       		                    var resp = Ext.decode(JSON.parse(text));
       		                    if(resp.isSuccess){//成功，将跳转到首页
       		                    	msgTip.hide();
       		                    	Ext.Msg.alert('成功提示', resp.message);
       		                    	setTimeout(function () {
       		                           //windows.location.replace:强制将当前页面替换掉，使不能前进和后退
       		                          //window.location.replace(getBasePath() + "page/front/index.jsp");
       		                       },500);
					
       		                    }else{
       		                    	msgTip.hide();
       		                    	Ext.Msg.alert('错误提示', resp.message);
       		                    } 
       	                   	},  
       	                   	failure : function(response){
       	                   		msgTip.hide();
       	                   		Ext.Msg.alert('系统提示', '出错了请联系管理员!');
       	                   	}  
       	                });
                  	 }else{//注册
                  		 console.log(serialize(this.up('form').getForm(),1,isLogin));
                  	 }
                  	 
                   }
               }
           },{
               text: 'reset',
               id: 'reset',
               handler: function() {
                   this.up('form').getForm().reset();
               }
           }]
       });

       tabs.render(Ext.get("showForm"));
});
 /**
  * 自定义的序列化form函数
  * param form form表单对象
  * param no tab编号
  * param isLogin 是否是登录
  */
 function serialize(form,no,isLogin){
     var serial = '',
     values = form.getValues();

     for(var value in values){
    	 if(value == 'aginPassword'){
			continue;
    	 }	 
    	 
    	 serial += '&' + value + '=' + values[value][no];
     }
     return serial.substr(1);
 };
    
 /**
 *构建json对象的请求参数
 */
 function buildJsonObjectParams(form,no,isLogin){
	 var json = {},
     values = form.getValues();
	 if(isLogin){
		 json['type'] = 1;
	 }
     for(var value in values){
    	 if(value == 'aginPassword'){
			continue;
    	 }	 
    	 
    	 var v = values[value][no];
    	 if(value == 'password'){
    		 v = $.md5(v);
    	 }
    	 json[value] = v;
     }
     return JSON.stringify(json);
 }
	         
</script>

<body>
	<table border="1" style="text-align: center;display: block;" >
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
	<div id="showForm" style="width: 800;margin-top: 50;" align="center">
		
	</div>
</body>
</html>