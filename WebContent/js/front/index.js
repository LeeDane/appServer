var tagColors;
var pageSize = 5;
var first_id = 0;
var last_id = 0;
$(document).ready(function(){
	tagColors = ["is-olivedrab", "is-orchid", "is-orange", "is-blueviolet", "is-burlywood", "is-aquamarine", "is-thistle", "is-deepskyblue", "is-aquamarine"];
	var method ="firstloading";
	//加载层
	var index = layer.load(0, {shade: false}); //0代表加载的风格，支持0-2

	
	var params = "{\"method\":\""+method+"\",\"pageSize\":"+pageSize+",\"first_id\":"+first_id+",\"last_id\":"+last_id+"}";
	//异步去请求登录
	$.ajax({
		type : 'post',
		data : {params : params},
		timeout: 15000, 
		async : true,//同步
		url :  getBasePath() + 'blog_getPagingBlog.action', 
		dataType : 'json',
		success : function(jsonData){
			var message = jsonData.message;
			if(jsonData.isSuccess){
				//layer.msg(message);
				for(var i= 0 ; i < message.length; i++){
					getEachRowBlog(message[i], false);
					if(i == 0){
						first_id = message[i].id;
					}
					if(i == message.length -1){
						last_id = message[i].id;
					}
				}
			}else{
				//失败的显示
				layer.msg(message, {icon: 5});
			}
			layer.close(index);
		},error: function(e){
		}			
	});
	$(document).on('mouseover', '.thumbnail',  function(){
		$(this).addClass("highlight-thumbnail");
	}).on('mouseout', '.thumbnail', function() {
		$(this).removeClass("highlight-thumbnail");
	});
	
	/*$(document).on('click', '.thumbnail',  function(){
		var fullTextLink = $(this).find("a.fullTextLink").attr("href");
		if(fullTextLink)
			window.open(fullTextLink);
		else 
			return;
	});*/

	//以下是热门相关的控制
	getHostestBlogs();
	
	$(document).on('mouseover', '.list-group-item',  function(){
		$(this).addClass("color-blue");
	}).on('mouseout', '.list-group-item', function() {
		$(this).removeClass("color-blue");
	});
	
	$(document).on('click', '.hostest-item',  function(){
		var blog_id = $(this).attr("data-id");
		if(blog_id){
			var href = getBasePath() +"page/front/fullText.jsp?blog_id="+blog_id;
			window.location.href= href;
		}			
		else 
			return;
	});
	
	//以下是最新相关的控制
	getNewestBlogs();
	$(document).on('click', '.newest-item',  function(){
		var blog_id = $(this).attr("data-id");
		if(blog_id){
			var href = getBasePath() +"page/front/fullText.jsp?blog_id="+blog_id;
			window.location.href= href;
		}			
		else 
			return;
	});
   
});

/**
 * 获取热门的博客
 */
function getHostestBlogs(){
	//异步去请求登录
	$.ajax({
		type : 'post',
		timeout: 15000, 
		url :  getBasePath() + 'blog_getHotestBlogs.action', 
		dataType : 'json',
		success : function(jsonData){
			var message = jsonData.message;
			if(jsonData.isSuccess){			
				$(".hostest").append(getHostestHtml(message));
			}else{
				layer.msg(message, {icon: 5});
			}
			
		},error: function(e){
		}			
	});
}

function getHostestHtml(message){
	var view = '<ul class="list-group" >';/*+
			      
			      '<li class="list-group-item"><div class="circularDiv ranking2">2</div>免费 Window 空间托管</li>'+
			      '<li class="list-group-item"><div class="circularDiv ranking3">3</div>图像的数量</li>'+
			      '<li class="list-group-item"><div class="circularDiv ranking4">4</div>24*7 支持</li>'+
			      '<li class="list-group-item wordHidden" title="3、呵呵呵呵呵呵呵呵呵呵呵呵呵呵呵额呵呵呵呵">'+
			      		'<div class="circularDiv ranking5">5</div>每年更新成本呵呵呵呵呵呵呵呵呵呵呵呵呵额呵呵呵呵</li>'+
			   '</ul>';*/
	for(var i= 0 ; i < message.length; i++){
		view += '<li class="hostest-item list-group-item hand wordHidden" data-id="'+message[i].id+'"><div class="circularDiv '+rankingColor[i]+'">'+(i+1)+'</div>'+message[i].title+'</li>';
	}
	
	view +='</ul>';
	return view;
}

/**
 * 获取最新的博客
 */
function getNewestBlogs(){
	//异步去请求登录
	$.ajax({
		type : 'post',
		timeout: 15000, 
		url :  getBasePath() + 'blog_getNewestBlogs.action', 
		dataType : 'json',
		success : function(jsonData){
			var message = jsonData.message;
			if(jsonData.isSuccess){			
				$(".newest").append(getNewestHtml(message));
			}else{
				layer.msg(message, {icon: 5});
			}
			
		},error: function(e){
		}			
	});
}


function getNewestHtml(message){
	var view = '<ul class="list-group" >';
	for(var i= 0 ; i < message.length; i++){
		view += '<li class="newest-item list-group-item hand wordHidden" data-id="'+message[i].id+'"><div class="circularDiv '+rankingColor[i]+'">'+(i+1)+'</div>&nbsp;&nbsp;(<font color="red">'+stringToDateTime(message[i].create_time)+'</font>)'+message[i].title+'</li>';
	}	
	view +='</ul>';
	return view;
}

/**
 * 渲染每一条 博客的数据
 * @param {Object} mesg  message的json对象
 * @param {Object} isBefore 是否是在前面插入数据
 */
function getEachRowBlog(mesg, isBefore){
	var view = "";
	if(mesg.has_img){
		view = "<div class=\"hand col-sm-6 col-md-4\" data-toggle=\"tooltip\"  data-placement=\"left\" title=\""+mesg.title+"\"> "+
				    "<div class=\"thumbnail\">"+
				      "<img src=\""+mesg.img_url+"\" >"+
				      "<div class=\"caption\">"+
				        "<h3>"+mesg.title+"</h3>"+
				        "<p>"+mesg.digest+"...</p>"+
				        "<p>"+getTagHtml(mesg.tag)+"</p>"+
				        "<p><a href=\""+getBasePath()+"page/front/fullText.jsp?blog_id="+mesg.id+"\" class=\"btn btn-primary fullTextLink\" role=\"button\">全文阅读</a>" +
				        "<span class=\"imgbg\" data-toggle=\"tooltip\" data-placement=\"bottom\" title=\"赞\" style=\"float:right;margin-right: 0px;\"><img src=\""+getBasePath()+"images/xin.png\" alt=\"赞\" style=\"width: 15px;height: 15px;\"/><span class=\"marginRightAndLeft2px\">5</span></span>"+
						"<span class=\"imgbg\" data-toggle=\"tooltip\" data-placement=\"bottom\" title=\"评论\" style=\"float:right;margin-right: 0px;\"><img src=\""+getBasePath()+"images/pinglun.png\" alt=\"评论\" style=\"width: 15px;height: 15px;\"/><span class=\"marginRightAndLeft2px\">18</span></span>"+
						"<span class=\"imgbg\" data-toggle=\"tooltip\" data-placement=\"bottom\" title=\"转发\" style=\"float:right;margin-right: 0px;\"><img src=\""+getBasePath()+"images/zhuanfa.png\" alt=\"转发\" style=\"width: 15px;height: 15px;\"/><span class=\"marginRightAndLeft2px\">201</span></span>"+
				        
				        "</p>"+
				      "</div>"+
				      
				    "</div>"+
				  "</div>";
	}else{
		view = "<div class=\"hand col-sm-6 col-md-4\" data-toggle=\"tooltip\"  data-placement=\"left\" title=\""+mesg.title+"\"> "+
				    "<div class=\"thumbnail\">"+
				      "<div class=\"caption\">"+
				        "<h3>"+mesg.title+"</h3>"+
				        "<p>"+mesg.digest+"...</p>"+
				        "<div>"+getTagHtml(mesg.tag)+"</div>"+
				        "<p><a href=\""+getBasePath()+"page/front/fullText.jsp?blog_id="+mesg.id+"\" class=\"btn btn-primary fullTextLink\" role=\"button\">全文阅读</a>" +				        
				        "<span class=\"imgbg\" data-toggle=\"tooltip\" data-placement=\"bottom\" title=\"赞\" style=\"float:right;margin-right: 0px;\"><img src=\""+getBasePath()+"images/xin.png\" alt=\"赞\" style=\"width: 15px;height: 15px;\"/><span class=\"marginRightAndLeft2px\">5</span></span>"+
						"<span class=\"imgbg\" data-toggle=\"tooltip\" data-placement=\"bottom\" title=\"评论\" style=\"float:right;margin-right: 0px;\"><img src=\""+getBasePath()+"images/pinglun.png\" alt=\"评论\" style=\"width: 15px;height: 15px;\"/><span class=\"marginRightAndLeft2px\">18</span></span>"+
						"<span class=\"imgbg\" data-toggle=\"tooltip\" data-placement=\"bottom\" title=\"转发\" style=\"float:right;margin-right: 0px;\"><img src=\""+getBasePath()+"images/zhuanfa.png\" alt=\"转发\" style=\"width: 15px;height: 15px;\"/><span class=\"marginRightAndLeft2px\">201</span></span>"+
				        "</p>"+
				      "</div>"+
				    "</div>"+
				  "</div>";
	}
	
	if(isBefore){
		var aim = "#blog_" + first_id;
		$(aim).before(view);
		first_id = mesg.id;
	}else{
		$("#allBlogDiv").append(view); 
	}
}

function getTagHtml(tag){
	if(isNull(tag)) return "";
	var tags = tag.split(";");
	var tagHtml = "";
	for(var i = 0; i < tags.length; i++){
		tagHtml +="<span class=\""+ tagColors[Math.floor(Math.random()*tagColors.length)] +" label label-default\" style=\"margin-right:10px;\">"+tags[i]+"</span>";
	}	
	return tagHtml;
}
/**
 * 跳转到查看全文的页面
 */
function linkToFullText(){
	console.log("excute linkToFullText()");
	var a = 0;
	var $this = $(this);
	var blog_id = $this.closest("p").find("input[name='blog_id']").val();
	if(blog_id)
		window.open("fullText.jsp?blog_id=" + parseInt(blog_id));
	else 
		return;
}

/**
 * 字符串对象为空的判断
 * @param {Object} obj
 */
function isNull(obj){
	if(obj != undefined && obj !=null && obj != 'null' && obj != ""){
		return false;
	}
	return true;
}

/**
 * 将为空或undefined的字符串对象转成“”
 * @param {Object} obj
 */
function notNullString(obj){
	if(isNull(obj)) return "";
	return obj;
}

/**
 * 字符串对象不为空的判断
 * @param {Object} obj
 */
function isNotNull(obj){
	return !isNull(obj);
}

