$(document).ready(function(){
	var url = window.location.href; //取得当前的地址栏地址信息
	var blog_id = getURLParam(url,"blog_id");
	if(blog_id == null || blog_id == '' || blog_id =='undefined' ){
		alert("博客不存在");
		return;
	}
		
	var datas = '{"blog_id" : "'+blog_id+'"}';
	$.ajax({
		type : 'post',
		data : {params : datas},
		url : getBasePath() + 'blog_getOneBlog.action',
		dataType : 'json',
		success : function(data){
			var msg = data;
			if(msg != null && msg.isSuccess){
				//显示
				$("#breadcrumbNav").show();//显示导航
				$(".full-text-body").show();//显示主体
				
				$(".breadcrumbTitle").html(msg.message[0].title);
				$(".full-text-create-time").html(stringToDateTime(msg.message[0].create_time));
				$(".full-text-account").html(msg.message[0].account);
				$(".full-Text-read-num").html(msg.message[0].read_number);
				if(msg.message[0].source != null && msg.message[0].source != "原创"){
					if(msg.message[0].origin_link != null){
						$(".full-text-source").html("引用自:<a href='"+msg.message[0].origin_link+"'>" + msg.message[0].source+"</a>");
					}else{
						$(".full-text-source").html("引用自:"+ msg.message[0].source);
					}
					
					$(".full-text-source").show();
				}
				$(".full-text-froms").html("来自:" +msg.message[0].froms);
				$(".full-text-main-left-main").html(msg.message[0].content);
			}else{
				layer.msg('服务器返回有误:'+msg.message, {icon: 5});
			}
		}
		
	});
	
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
});


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
			layer.msg("服务器异常", {icon: 5});
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
		view += '<li class="hostest-item list-group-item hand" data-id="'+message[i].id+'"><div class="circularDiv '+rankingColor[i]+'">'+(i+1)+'</div>'+message[i].title+'</li>';
	}
	
	view +='</ul>';
	return view;
}
/**
 * 格式化日期
 * @param date
 */
function formatDate(date){
	var year = date.getYear();
	var month = date.getMonth() + 1;
	var day = date.date;
	var hour = date.hour;
	var minutes = date.minutes;
	var second = date.second;
	return year +"-" +month +"-" +day + " " + hour+ ":" + minutes + ":" + second;
}

/**
 * 将日期对象转化成JavaScript date()对象
 * @param dateObj
 * @returns
 */
function buildDate(dateObj){
	if(dateObj == null) return dateObj;
	return new Date(parseInt(dateObj.time,10));
}

/**
 * 把日期对象转化成"yyyy-MM-dd HH:mm:ss"格式
 * @param dateObj
 */
function buildDateToDefaultFormat(dateObj){
	var result = "";
	var date = buildDate(dateObj);
	if(date != null){
		//处理日期部分
		var dateLocateString = date.toLocaleDateString();
		if(dateLocateString != null){
			var dateLocates = dateLocateString.split("/");
			var length = dateLocates.length;
			for(var i = 0; i < length; i++){
				if(i == length -1)
					result += dateLocates[i];
				else
					result += dateLocates[i] + "-";
			}	
		}
		//处理时间部分
		var hours = dateObj.hours;
		var minutes = dateObj.minutes;
		var seconds = dateObj.seconds;
		result = result + " " + hours +":" +minutes + ":" + seconds;	
	}
		
	return result;
}




