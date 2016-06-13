<%@ page language="java" contentType="text/html; charset=utf-8"
    pageEncoding="utf-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<title>管理员中心</title>

<script type="text/javascript" src="../../js/others/jquery-1.9.1.min.js"></script>
<script type="text/javascript" src="../../js/admin/releaseProduct.js"></script>
<style>
</style>
</head>
<body style="background-color: #999999" lang="en">
	 <div id="admin">
		<%@ include file="topNav.jsp"%>
		<div id="main" style="clear: both;">
			<div id="left">
				<%@ include file="leftNav.jsp"%>				
			</div>		
			
			<div id="right">
				<div>
					<div style="margin-top: 30px;margin-left: 80px;">
						标题：<input id="blogContentTitle" type="text" style="width: 60%;height: 25px;">
					</div>
				</div>
				<div id="blogContentEdit" style="margin-left: 10px;margin-right: 10px;">
					 <%@ include file="editor.jsp"%>					 
				</div>
				<div id="tagGroups">
					<div style="margin-top: 30px;margin-left: 80px;">
						标签：<input id="addTagValue" type="text" style="width: 180px;height: 25px;vertical-align:top;" placeholder="标签,最多5个">
						<img id="addTag" data-toggle="tooltip" title="点击添加标签" alt="点击添加" src="../../images/add.png" style="width: 25px;height: 25px;cursor: pointer;vertical-align:top;margin-left: 8px;"><br>
						<div id="tags">
						</div>
					</div>
				</div>
				<div>
					<input type="file" name="img_url" title="上传主图"/>
				</div>
				<div class="checkbox">
			      <label>
			      <input name="has_img" type="checkbox">将第一张图片作为主图
			      </label>
			   </div>
			   <div>
			   		<input type="text" name="digest"/>
			   </div>
			   <div class="checkbox">
			      <label>
			      <input name="has_digest" type="checkbox">从内容中提取摘要
			      </label>
			   </div>
				
				 <div style="text-align: center;margin-top: 18px;">
				 	<button id="releaseBlog" type="button" class="btn btn-primary">立即发布</button>
					<button id="saveBlog" type="button" class="btn btn-info" style="margin-left: 28px;">存为草稿</button>
				 </div>
			</div>
		</div>	
	</div>
</body>
</html>