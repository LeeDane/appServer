$(document).ready(function(){
	
	$("[data-toggle='tooltip']").tooltip();
	
	/**增加标签*/
	$("#addTag").click(function(){
		//layer.msg('也可以不用显示图标哦', 1, -1);
		var addTagValue = $("#addTagValue").val();//增加标签的值
		if(addTagValue == ""){ //处理空的标签
			$("#addTagValue").focus();
			return;
		}
		var $tags = $(this).siblings("#tags");
		if($tags.find("span").length > 4){ //处理超过5个的标签
			layer.msg('已经5个标签，请删除后再添加', 1, -1);
			return;
		}
		$tags.append("<span class=\"label marginLeft20 tagaa\" data-toggle=\"tooltip\" title=\"点击删除该标签\">"+addTagValue+"</span>");
	});
	
	/**发布博客*/
	$("#releaseBlog").click(function(){
		var blogContentTitle = $("#blogContentTitle").val(); //博客标题的值
		var oldBlogContent = $("#editor").html(); //博客内容的值
		//alert(blogContentTitle + "-----" + blogContent)
		var $tags = $(this).closest("#right").find("#tags").find("span");//所有标签span
		var tags = "";
		$tags.each(function(index){			
			if(index < $tags.length-1 ){
				tags += $tags.eq(index).text() + ";";
			}else{
				tags += $tags.eq(index).text();
			}
		});
		
		var has_img = $("input[name='has_img']:checked").val()=="on";
		var has_digest = $("input[name='has_digest']:checked").val()=="on";
		var digest = $("input[name='digest']").val();
		
		var blogContent = oldBlogContent.replace("\"","'");
		var reg = new RegExp('"',"g");  
		blogContent = blogContent.replace(reg, "'");  
		
		var data = '{"title" : "'+blogContentTitle+'", "content" : "'+blogContent+'","status" : '+1+',"img_url":"","has_img":'+has_img+',"has_digest":'+has_digest+',"digest":"'+digest+'","tag" : "'+tags+'","froms:":"PC网页端"}';
		//alert(data);
		
		//return;
		$.ajax({
			type : 'post',
			data : {params : data},
			url : '/leedane/blog_releaseBlog.action',
			dataType : 'json',
			success : function(jsons){
				alert(jsons);
			}
			
		});
	});
	
	
	$(".tagaa").on("click", function () {
		 alert($(this).text());		 
      });
	
	
	
	
	
	/*laydate.skin('yahui');
	
	$("#release").bind("click",function(){
		var data = $("#releaseProductForm").serialize();
		var params = decodeURIComponent(data,true);  
		alert(params);
	});*/
	
	//alert($("#editor").html());
});

function deleteTag(object){
	var a = $(this);
	layer.msg('已经成功删除该标签', 1, -1);
}
