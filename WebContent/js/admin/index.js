/*window.WebSocket = window.WebSocket || window.MozWebSocket; 
if(!window.WebSocket){ 
    //alert("浏览器不支持websocket!!!"); 
	console.log("浏览器不支持websocket!!!"); 
} else{
	var ws = new WebSocket("ws://localhost:8080/MyBlog/AdminMessageWebSocket");
	ws.onopen = function(evt) {
	 	console.log("Openened connection to websocket");  
	 	//$('embed').remove(); 
	};
	ws.onmessage = function(msg) {
		$('.admin_msg').html(msg.data);
		//$('body').append('<embed src="/MyBlog/static/media/msg.wav" autostart="true" hidden="true" loop="false">');
	
	};
}*/


$(document).ready(function(){
	
	/*发布*/
	/*$("#releaseProduct").bind("click",function(){
		//window.frames["win"].location.href="releaseBlog.jsp";
		
		window.open
		
		$.layer({
		    type: 2,
		    shade: [0],
		    fix: false,
		    title: '发布商品',
		    maxmin: true,
		    iframe: {src : '../../page/admin/releaseProduct.jsp'},
		    area: ['800px' , '500px'],
		    close: function(index){
		        //layer.msg('您获得了子窗口标记：' + layer.getChildFrame('#name', index).val(),3,1);
		    }
		}); 
	});*/
	
	/*发布小调查*/
	$("#release_minSurvey").bind("click",function(){
		window.frames["win"].location.href="release_minSurvey.jsp";
	});
	
	
	$("#release_mood").bind("click",function(){
		window.frames["win"].location.href="release_mood.jsp";
	});
	
	
	
	
	
	
	
	
	
	/*管理*/
	$("#manager_blog").bind("click",function(){
		window.frames["win"].location.href="manager_blog.jsp";
	});
	
	/*消息*/
	$("#accept_msg").bind("click",function(){
		window.frames["win"].location.href="accept_msg.jsp";
	});
	
	
	
	$(".release_style:even").css("background-color","#87ceeb");
	$(".release_style").each(function(index){
		
		$(this).hover(function(){	
			$(this).css("background-color","#999999");
			$(this).css("color","#87ceeb");
		},function(){
			if(index%2==0){
				$(this).css("background-color","#87ceeb");
				$(this).css("color","#000000");
			}else{
				$(this).css("background-color","#ffffff");
				$(this).css("color","#000000");
			}
		});
	});
	
	
	
	
	
	
	
});

function SetWinHeight(obj){ 
	var win=obj; 
	if (document.getElementById){ 
		if (win && !window.opera){ 
			if(win.contentDocument && win.contentDocument.body.offsetHeight) 
				win.height = win.contentDocument.body.offsetHeight; 
			else if(win.Document && win.Document.body.scrollHeight) 
				win.height = win.Document.body.scrollHeight; 
		} 
	} 
} 