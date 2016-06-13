<%@ page language="java" contentType="text/html; charset=utf-8"
    pageEncoding="utf-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<title>Insert title here</title>
<script type="text/javascript" src="../../js/admin/navLeft.js"></script>

</head>
<body>

	<div class="panel-group" id="accordion">
		<div class="panel panel-default">
			<div class="panel-heading">
				<h4 class="panel-title">
			        <a data-toggle="collapse" data-parent="#accordion" href="#collapseOne">博客</a>
			        <span class="badge pull-right" style="background-color: gray;display: none;">3</span>
				</h4>
			</div>
		    <div id="collapseOne" class="panel-collapse collapse in">
		      	<div class="panel-body">
		        	<div id="releaseProduct" class="accordion-inner handBlue navLeft"><a href="releaseProduct.jsp">发布博客</a></div>
		        	<div id="managerBlog" class="accordion-inner handBlue navLeft"><a href="#">管理博客</a></div>
		        	<div id="statisticsBlog" class="accordion-inner handBlue navLeft"><a href="#">博客统计</a></div>
		      	</div>
		    </div>
		</div>
	  	<div class="panel panel-default">
		    <div class="panel-heading">
				<h4 class="panel-title">
				  	<a data-toggle="collapse" data-parent="#accordion" href="#collapseTwo">商品</a>
				  	<span class="badge pull-right" style="background-color: gray;">3</span>
				</h4>
		    </div>
		    <div id="collapseTwo" class="panel-collapse collapse">
		      	<div class="panel-body">
		        	<div id="releaseProduct" class="accordion-inner handBlue navLeft"><a href="#">发布商品</a></div>
		        	<div id="managerProduct" class="accordion-inner handBlue navLeft"><a href="#">管理商品</a></div>
		        	<div id="statisticsProduct" class="accordion-inner handBlue navLeft"><a href="#">商品统计</a></div>
		      	</div>
		    </div>
	 	</div>
		<div class="panel panel-default">
		  	<div class="panel-heading">
		    	<h4 class="panel-title">
		      		<a data-toggle="collapse" data-parent="#accordion" href="#collapseThree">用户 </a>
		      		<span class="badge pull-right" style="background-color: gray;">3</span>
		    	</h4>
		  	</div>
		  	<div id="collapseThree" class="panel-collapse collapse">
		    <div class="panel-body">
		      	<div id="releaseProduct" class="accordion-inner handBlue navLeft"><a href="#">审核用户</a></div>
		        <div id="managerProduct" class="accordion-inner handBlue navLeft"><a href="#">管理用户</a></div>
		        <div id="statisticsProduct" class="accordion-inner handBlue navLeft"><a href="#">用户统计</a></div>
		    </div>
		  	</div>
		</div>
		<div class="panel panel-default">
		  	<div class="panel-heading">
		    	<h4 class="panel-title">
		      		<a data-toggle="collapse" data-parent="#accordion" href="#collapseFour">订单 </a>
		      		<span class="badge pull-right" style="background-color: gray;">3</span>
		    	</h4>
		  	</div>
		  	<div id="collapseFour" class="panel-collapse collapse">
		    <div class="panel-body">
		      	<div id="releaseProduct" class="accordion-inner handBlue navLeft"><a href="#">审核订单</a></div>
		        <div id="managerProduct" class="accordion-inner handBlue navLeft"><a href="#">管理订单</a></div>
		        <div id="statisticsProduct" class="accordion-inner handBlue navLeft"><a href="#">订单统计</a></div>
		    </div>
		  	</div>
		</div>
		<div class="panel panel-default">
		  	<div class="panel-heading">
		    	<h4 class="panel-title">
		      		<a data-toggle="collapse" data-parent="#accordion" href="#collapseFive">设置</a>
		      		<span class="badge pull-right" style="background-color: gray;">2</span>
		    	</h4>
		  	</div>
		  	<div id="collapseFive" class="panel-collapse collapse">
		    <div class="panel-body">
		      	<div id="releaseProduct" class="accordion-inner handBlue navLeft"><a href="#">基本设置</a></div>
		        <div id="managerProduct" class="accordion-inner handBlue navLeft"><a href="#">系统设置</a></div>
		    </div>
		  	</div>
		</div>
	</div>
				
</body>
</html>