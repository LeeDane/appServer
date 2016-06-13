<%@ page language="java" contentType="text/html; charset=utf-8"
    pageEncoding="utf-8"%>
<%@ include file="indexNavTop.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<title>全文阅读</title>
<link type="text/css" rel="stylesheet" href="<%=basePath %>css/front/fullText.css">

<script type="text/javascript" src="<%=basePath %>js/front/fullText.js"></script>
<script type="text/javascript" src="<%=basePath %>js/base.js"></script>

<script type="text/javascript" charset="utf-8" src="<%=basePath %>other/ueditor/ueditor.config.js"></script>
<script type="text/javascript" charset="utf-8" src="<%=basePath %>other/ueditor/ueditor.all.min.js"></script>
<link type="text/css" rel="stylesheet" href="<%=basePath %>other/ueditor/themes/default/css/ueditor.css">
</head>
<body>
	<div id="breadcrumbNav" style="width:100%;z-index: 999;margin-top: 1px;" >
			<ol class="breadcrumb" style="width: 1024px;margin: 0 auto;background-color: #8470FF;filter: alpha(opacity=100); opacity:0.75;">
			  	<li class="breadcrumbTitle"></li>
			</ol>
			 <script type="text/javascript">	 		
		         jQuery(function($) {
		           $(document).ready( function() {
		           	$("[data-toggle='tooltip']").tooltip();
		           	
		           	$('#breadcrumbNav').stickUp({
		           		marginTop: 'auto'
		                 });
		   			});
		           
		         });
		     </script>
	</div>
	<div class="full-text-body">
		<div class="full-text-title" style="margin-top: 4px;font-size: 14px;font-family: '微软雅黑','宋体'" >
			
		
			<label style="margin-left: 10px;margin-right: 10px;"><span>作者: </span><span class="full-text-account"></span></label>
			<label style="margin-right: 10px;">发表于: <span class="full-text-create-time"></span></label>
			<label style="margin-right: 10px;display: none;" class="full-text-source"></label>
			<label style="margin-right: 10px;" class="full-text-froms"></label>
			<span class="" data-toggle="tooltip" data-placement="bottom" title="阅读数" style="float:right;margin-right: 10px;float: right;"><img src="<%=basePath %>images/read.png" alt="阅读数" style="width: 18px;margin-right: 2px;"/><span class="marginRightAndLeft2px full-Text-read-num"></span></span>
			<!-- <label style="margin-right: 10px;float: right;">阅读数:<span class="fullTextReadNum"></span></label> -->
		</div>
		<div class="full-text-main" style="margin-top: 8px;">
			<div class="full-text-main-left">
				<div class="full-text-main-left-main"></div>
				<div class="full-text-operate">
					<span class="imgbg" data-toggle="tooltip" data-placement="bottom" title="赞" style="float:right;margin-right: 3px;"><img src="<%=basePath %>images/xin.png" alt="赞" style="width: 20px;height: 20px;"/><span class="marginRightAndLeft2px">15</span></span>
					<span class="imgbg" data-toggle="tooltip" data-placement="bottom" title="评论" style="float:right;margin-right: 8px;"><img src="<%=basePath %>images/pinglun.png" alt="评论" style="width: 20px;height: 20px;"/><span class="marginRightAndLeft2px">55</span></span>
					<span class="imgbg" data-toggle="tooltip" data-placement="bottom" title="转发" style="float:right;margin-right: 8px;"><img src="<%=basePath %>images/zhuanfa.png" alt="转发" style="width: 20px;height: 20px;"/><span class="marginRightAndLeft2px">601</span></span>
				</div>
				<div class="full-text-add-comment" align="center">
						
						<textarea id="addComment" name="content" style="width: 98%;"></textarea>
						<br/>
						<button type="button" class="btn btn-info">立即发表</button>
						<!-- <div class="submitStyle">提交</div> -->
						<script type="text/javascript">
							/* var ueditor = new baidu.editor.ui.Editor();
							ueditor.render("newEditor"); */
							 var editor = UE.getEditor('addComment',{  
								//这里可以选择自己需要的工具按钮名称,此处仅选择如下五个  
								 toolbars:[['undo', 'redo', 'bold', 'indent','italic','underline'
								            ,"blockquote","pasteplain","unlink","link","fontfamily","fontsize","paragraph","simpleupload"
								            ,"insertimage","map","justifyleft","justifyright","justifycenter","justifyjustify","forecolor"
								            ,"insertorderedlist","insertunorderedlist","imagecenter","attachment","lineheight"
								            ,"autotypeset","touppercase","tolowercase","scrawl","inserttable"]],  
								zIndex: 1,
								initialFrameWidth: 690,
								initialFrameHeight:240  
							}); 

						</script>
				</div>
				<div class="full-text-comment-list">
					<div>一个<label style="color:red;">10</label>条记录</div>
					<div class="comment-list-each" data-toggle="tooltip" data-placement="left" title="leedane">
						<div class="comment-list-each-left">						
							<img class="comment-list-user-img-bg" alt="200x260" src="<%=basePath %>images/six.jpg" style="width: 100px;height: 100px;"/>
							<label class="comment-list-time"><span><img src="<%=basePath %>images/zhong.png" style="width: 16px;height: 16px;"/></span><span style="margin-left: 8px;">20秒前</span></label>							
						</div>
						<div class="comment-list-each-right">		
							<label class="comment-list-title">回复: @晓明</label>					
							<p class="comment-list-content">
								<a>sdfsff</a>便也是我的。一曲霓裳羽衣曲，仿佛是三郎与玉环的前世今生，再也不要离别了，再也不要在马嵬坡下横刀断马了。　　
								  　　现世安稳，岁月静好。晨起的朝露里，有我们安详而幸福的长歌。那是莘国和秦王朝的最好联盟，于是，我便不由自主得在秋雾里放浪清歌：关关雎鸠，在河之洲，窈窕淑女，君子好逑……　
								  　　尘世是写满意外的情书。突然，你消失了……消失得无影无踪。看着摩天大楼里，现代化的赞歌，我在极尽全力得安慰自己：他出征打仗，那是大丈夫的做派，因为你选择的是——英雄！　　
								  　　一天，两天，三天……　
								  　　我用写满了情感的血书，让信鸽捎去炽热的温暖。
							</p>	
							<p>
								<span class="imgbg" data-toggle="tooltip" data-placement="bottom" title="赞" style="float:right;margin-right: 3px;"><img src="<%=basePath %>images/xin.png" alt="赞" style="width: 18px;height: 18px;"/><span class="marginRightAndLeft2px">5</span></span>
								<span class="imgbg" data-toggle="tooltip" data-placement="bottom" title="评论" style="float:right;margin-right: 8px;"><img src="<%=basePath %>images/pinglun.png" alt="评论" style="width: 18px;height: 18px;"/><span class="marginRightAndLeft2px">18</span></span>
								<span class="imgbg" data-toggle="tooltip" data-placement="bottom" title="转发" style="float:right;margin-right: 8px;"><img src="<%=basePath %>images/zhuanfa.png" alt="转发" style="width: 18px;height: 18px;"/><span class="marginRightAndLeft2px">201</span></span>
							</p>								
						</div>
					</div>
					
				</div>
			</div>
			<div id="right" style="float:right;width: 30%;height: 800px;">	
				<div style="border: 1px;">
					<div class="panel panel-info hostest" >
					   	<div class="panel-heading">						      	
					       		<span data-toggle="tooltip" title="热门推荐">热门推荐</span>						      	
					   	</div>
					</div>
					
					<div class="panel panel-info">
					   	<div class="panel-heading">					   
					       		<span data-toggle="tooltip" title="活跃用户">活跃用户</span>						      
					   	</div>
					   	<!-- <div class="panel-body">
					     	Body
					   	</div> -->
					   	<ul class="list-group">
					      <li class="list-group-item"><div class="circularDiv ranking1">1</div>免费域名注册</li>
					      <li class="list-group-item"><div class="circularDiv ranking2">2</div>免费 Window 空间托管</li>
					      <li class="list-group-item"><div class="circularDiv ranking3">3</div>图像的数量</li>
					      <li class="list-group-item"><div class="circularDiv ranking4">4</div>24*7 支持</li>
					      <li class="list-group-item" style="text-overflow:ellipsis; white-space:nowrap; overflow:hidden;" title="3、呵呵呵呵呵呵呵呵呵呵呵呵呵呵呵额呵呵呵呵">
					      		<div class="circularDiv ranking5">5</div>每年更新成本呵呵呵呵呵呵呵呵呵呵呵呵呵额呵呵呵呵</li>
					   </ul>
					</div>				
				</div>			
			</div>
		</div>
		<div><%@ include file="indexNavBottom.jsp"%></div>
		<div class="full-text-bottom">
		
		</div>
	</div>

</body>
</html>