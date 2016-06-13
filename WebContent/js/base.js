var rankingColor = ["ranking1","ranking2","ranking3","ranking4","ranking5"];
/**
*获得全局的项目主路径
*/
function getBasePath(){
	return document.getElementById("basePath").value;
}

/**
*获得管理员后台的全局的tabpanel名称
*/
function getTabPanelName(){
	return "MainTabPanel";
}
	
/**
 * 获取url地址中的参数的方法
 * @param url url地址
 * @param sProp  参数的名称
 * @returns
 */
function getURLParam(url, sProp) {
	if(!url)
		url = window.location.href; //取得当前的饿地址栏地址信息
	
	// 正则字符串
	var re = new RegExp("[&,?]" + sProp + "=([^\\&]*)", "i");
	// 执行正则匹配
	var a = re.exec(url);
	if (a == null) {
		return "";
	}
	return a[1];
}

/**
 * 将日期字符串转化成时间显示
 * @param postdate
 * @returns
 */
function stringToDateTime(postdate) { 
	var second = 1000; 
	var minutes = second*60; 
	var hours = minutes*60; 
	var days = hours*24; 
	var months = days*30; 
	var twomonths = days*365; 
	var myDate = new Date(Date.parse(postdate)); 
	if (isNaN(myDate)){ 
		myDate =new Date(postdate.replace(/-/g, "/")); 
	} 
	var nowtime = new Date(); 
	var longtime =nowtime.getTime()- myDate.getTime(); 
	var showtime = 0; 
	if( longtime > months*2 ){ 
		return postdate; 
	}else if (longtime > months){ 
		return "1个月前"; 
	}else if (longtime > days*7){ 
		return ("1周前"); 
	}else if (longtime > days){ 
		return(Math.floor(longtime/days)+"天前"); 
	}else if (longtime > hours){ 
		return(Math.floor(longtime/hours)+"小时前"); 
	}else if (longtime > minutes){ 
		return(Math.floor(longtime/minutes)+"分钟前"); 
	}else if (longtime > second){ 
		return(Math.floor(longtime/second)+"秒前"); 
	}else{ 
		return(postdate); 
	} 
} 



