<!DOCTYPE html>
<%@ page language="java" contentType="text/html; charset=utf-8"
    pageEncoding="utf-8"%>
<%@ include file="indexNavTop.jsp"%>
<html lang="en">
<head>
<meta charset="utf-8">
<meta http-equiv="X-UA-Compatible" content="IE=edge">
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<title>首页</title>
<link type="text/css" rel="stylesheet" href="<%=basePath %>css/front/index.css">

<script type="text/javascript" src="<%=basePath %>js/front/index.js"></script>
</head>
<body>
	<!-- <div style="width: 100%;height: auto;background-color: gray;"> -->
		<!-- <div style="width: 100%;margin: 0 auto;padding-top:8px;">	 -->	
		<div class="container">				
			<!-- 轮播开始 -->
			<div id="myCarousel" data-ride="carousel" class="carousel slide">
			   <!-- 轮播（Carousel）指标 -->
			   <ol class="carousel-indicators">
			      <li data-target="#myCarousel" data-slide-to="0" class="active"></li>
			      <li data-target="#myCarousel" data-slide-to="1"></li>
			      <li data-target="#myCarousel" data-slide-to="2"></li>
			      <li data-target="#myCarousel" data-slide-to="3"></li>
			   </ol>   
			   <!-- 轮播（Carousel）项目 -->
			   <div class="carousel-inner">
			      <div class="item active">
			         <img src="<%=basePath %>images/one.jpg" style="width: 100%;height: 400px;" alt="First slide">
			      </div>
			      <div class="item">
			         <img src="<%=basePath %>images/four.jpg" style="width: 100%;height: 400px;" alt="Second slide">
			      </div>
			      <div class="item">
			         <img src="<%=basePath %>images/five.jpg" style="width: 100%;height: 400px;" alt="Third slide">
			      </div>
			      <div class="item">
			         <img src="<%=basePath %>images/nine.jpg" style="width: 100%;height: 400px;" alt="Third slide">
			      </div>
			   </div>
			   <!-- 轮播（Carousel）导航 -->
			   <a class="carousel-control left" href="#myCarousel" data-slide="prev"><span class="glyphicon glyphicon-chevron-left"></span></a>
			   <a class="carousel-control right" href="#myCarousel" data-slide="next"><span class="glyphicon glyphicon-chevron-right"></span></a>
			</div> 
			<!-- 轮播结束 -->			
		</div>
		<div id="indexContent"  style="width: 100%;margin: 0 auto;background-color: #fff;border-radius:4px;">
			<div class="container" style="/*margin-top: 10px; border:1px #ddd solid; */">
				<div class="row">
					<div class="col-md-12" id="breadcrumbNav" style="max-width:1168px;z-index: 999;" >
						<ol class="breadcrumb" style="border-left:1px #ddd solid;/* border-right:1px #bce8f1 solid; */">
						  	<li>当前位置</li>
						  	<li class="active"><a href="#">首页</a></li>
						</ol>
					</div>
				</div>
				<div class="row">						
					<div class="col-md-8" >
						<div class="row" id="allBlogDiv"></div>	
					</div>
					<div class="col-md-4">
						<div class="panel panel-info hostest">
						   	<div class="panel-heading">						      	
						       		<span data-toggle="tooltip" title="热门推荐">热门推荐</span>						      	
						   	</div>
						</div>
						<div class="panel panel-info newest">
						   	<div class="panel-heading ">					   
						       		<span data-toggle="tooltip" title="最新文章">最新文章</span>						      
						   	</div>
						</div>
					</div>
				</div>
			</div>
		</div>
	<div><%@ include file="indexNavBottom.jsp"%></div>
	 <script type="text/javascript">	 		
         jQuery(function($) {
           $(document).ready( function() {
           	$('[data-toggle="tooltip"]').tooltip();
           	
           	$('#breadcrumbNav').stickUp({
           		marginTop: 'auto'
                 });
   			});
           
         });
     </script>
	
</body>
</html>