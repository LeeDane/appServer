<%@ page language="java" contentType="text/html; charset=utf-8"
    pageEncoding="utf-8"%>
<%@ include file="/page/common/basePath.jsp"%>

<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<%-- 不需要重复添加，否则会造成冲突，导致页面的标签不能正常使用 --%>
<%-- <script type="text/javascript" src="<%=basePath %>js/others/jquery-1.9.1.min.js"></script>
<script type="text/javascript" src="<%=basePath %>js/others/jquery.md5.js"></script>

<link rel="stylesheet" type="text/css" href="<%=basePath %>other/ext-4.1.1/resources/css/ext-all.css" />
<script type="text/javascript" src="<%=basePath %>other/ext-4.1.1/ext-all.js"></script>
<link rel="stylesheet" type="text/css" href="<%=basePath %>other/ext-4.1.1/examples/shared/example.css" /> --%>

<script type="text/javascript" src="<%=basePath %>other/ext-4.1.1/examples/shared/states.js"></script>

<title>基本资料</title>
</head>
<script type="text/javascript">
Ext.require('Ext.chart.*');
Ext.require(['Ext.layout.container.Fit', 'Ext.window.MessageBox']);

Ext.onReady(function () {
	
	 var ii = 0;
	 
	 var hits = "Hits";
	 var xaxis = "Months";
	 var maximum = 100;
	 var panel1;
	 var chart;
	 
	 var preModel = 0; //记录当前选择的模式
	 var store1;
	
	 var generateData = function(){
		 
		 	var data = [];
		 	var url = "";
		 	if(preModel == 1){
		 		url = getBasePath() +"user_statisticsUserAgeRang.action";
		 	}else if(preModel == 2){
		 		url = getBasePath() +"user_statisticsUserRegisterByYear.action";
		 	}else if(preModel == 3){
		 		url = getBasePath() +"user_statisticsUserRegisterByMonth.action";
		 	}else if(preModel == 4){
		 		url = getBasePath() +"user_statisticsUserRegisterByNearMonth.action";
		 	}else if(preModel == 5){
		 		url = getBasePath() +"user_statisticsUserRegisterByNearWeek.action";
		 	}else{
		 		url = getBasePath() +"user_statisticsUserAge.action"
		 	}
		 	
		 	/**
		 	*ajax同步加载数据
		 	*/
		 	Ext.Ajax.request({
	            url : url,            
	            method : 'POST',
	            async:false,
	            success : function(response) {
	              var text = response.responseText;
	              var json = JSON.parse(text);
	              maximum = json.maximum;
	              xaxis = json.xaxis;
	              yaxis = json.yaxis;
	              data = json.data;
	            }
	       	});
		 	
		 	if(maximum < 30){
		 		maximum = maximum + 1;
		 	}else{
		 		maximum = maximum + 10;
		 	}
	    
	        if(panel1){
	        	$("#showForm13").html("");
	        	console.log("yaxis:"+yaxis);
	        	console.log("xaxis:"+xaxis);	    

	        	chart = Ext.define("LeeDane.view.chart",
            		{extend:'Ext.chart.Chart', 
                    xtype: 'chart',
                    animate: true,
                    shadow: true,
                    store: store1,
                    axes: [{
                        type: 'Numeric',
                        position: 'left',
                        fields: ['yaxis'],
                        title: yaxis,
                        grid: true,
                        minimum: 0,
                        maximum: maximum
                    }, {
                        type: 'Category',
                        position: 'bottom',
                        fields: ['xaxis'],
                        title: xaxis,
                        label: {
                            rotate: {
                                degrees: 270
                            }
                        }
                    }],
                    series: [{
                        type: 'column',
                        axis: 'left',
                        gutter: 80,
                        xField: 'xaxis',
                        yField: ['yaxis'],
                        tips: {
                            trackMouse: true,
                            width: 114,
                            height: 38,
                            renderer: function(storeItem, item) {
                            	this.setTitle(yaxis + storeItem.get('yaxis'));
                            	//this.setTitle(xaxis + storeItem.get('xaxis')+'的'+ yaxis + storeItem.get('yaxis'));
                                //this.update(storeItem.get('yaxis'));
                            }
                        },
                        style: {
                            fill: '#38B8BF'
                        }
                    }]
	            });


	            panel1 = Ext.create('widget.panel', {
	                width: 800,
	                height: 400,
	                title: '关于用户信息的统计',
	                layout: 'fit',
	                tbar: [{
	                    text: '统计年龄',
	                    handler: function() {
	                    	preModel = 0;
	                        store1.loadData(generateData());
	                        /* Ext.MessageBox.confirm('Confirm Download', 'Would you like to download the chart as an image?', function(choice){
	                            if(choice == 'yes'){
	                                chart.save({
	                                    type: 'image/png'
	                                });
	                            }
	                        }); */
	                    }
	                }, {
	                    text: '刷新',
	                    handler: function() {
	                        store1.loadData(generateData());
	                    }
	                }, {
	                    text: '统计年龄段',
	                    handler: function() {
	                    	preModel = 1;
	                        store1.loadData(generateData());
	                    }
	                },{
	                    text: '统计注册时间的年份',
	                    handler: function() {
	                    	preModel = 2;
	                        store1.loadData(generateData());
	                    }
	                },{
	                    text: '统计注册时间的月份',
	                    handler: function() {
	                    	preModel = 3;
	                        store1.loadData(generateData());
	                    }
	                },{
	                    text: '统计最近一个月注册人数',
	                    handler: function() {
	                    	preModel = 4;
	                        store1.loadData(generateData());
	                    }
	                }
	                ,{
	                    text: '统计最近一周注册人数',
	                    handler: function() {
	                    	preModel = 5;
	                        store1.loadData(generateData());
	                    }
	                }],
	                items: chart
	            });            
	            panel1.render(Ext.get("showForm13"));
	        }
	        return data;
	    };
	        
		store1 = Ext.create('Ext.data.JsonStore', {
	        fields: ['xaxis', 'yaxis'],
	        data: generateData()
	    });

    chart = Ext.define("LeeDane.view.chart",
   		{extend:'Ext.chart.Chart', 
           xtype: 'chart',
           animate: true,
           shadow: true,
           store: store1,
           axes: [{
               type: 'Numeric',
               position: 'left',
               fields: ['yaxis'],
               title: yaxis,
               grid: true,
               minimum: 0,
               maximum: maximum
           }, {
               type: 'Category',
               position: 'bottom',
               fields: ['xaxis'],
               title: xaxis,
               label: {
                   rotate: {
                       degrees: 270
                   }
               }
           }],
           series: [{
               type: 'column',
               axis: 'left',
               gutter: 80,
               xField: 'xaxis',
               yField: ['yaxis'],
               tips: {
                   trackMouse: true,
                   width: 114,
                   height: 38,
                   renderer: function(storeItem, item) {
                       this.setTitle(yaxis + storeItem.get('yaxis'));
                       //this.setTitle(xaxis + storeItem.get('xaxis')+'的'+ yaxis + storeItem.get('yaxis'));
                       //this.update(storeItem.get('yaxis'));
                   }
               },
               style: {
                   fill: '#38B8BF'
               }
           }]
    });


    panel1 = Ext.create('widget.panel', {
        width: 800,
        height: 400,
        title: '关于用户信息的统计',
        layout: 'fit',
        tbar: [{
            text: '统计年龄',
            handler: function() {
            	preModel = 0;
                store1.loadData(generateData());
            }
        },{
            text: '刷新',
            handler: function() {
                store1.loadData(generateData());
            }
        },{
            text: '统计年龄段',
            handler: function() {
            	preModel = 1;
                store1.loadData(generateData());
            }
        },{
            text: '统计注册时间的年份',
            handler: function() {
            	preModel = 2;
                store1.loadData(generateData());
            }
        },{
            text: '统计注册时间的月份',
            handler: function() {
            	preModel = 3;
                store1.loadData(generateData());
            }
        },{
            text: '统计最近一个月注册人数',
            handler: function() {
            	preModel = 4;
                store1.loadData(generateData());
            }
        }
        ,{
            text: '统计最近一周注册人数',
            handler: function() {
            	preModel = 5;
                store1.loadData(generateData());
            }
        }],
        items: chart
    });
    
    panel1.render(Ext.get("showForm13"));
    
});
</script>
<body>
<div id="showForm13" style="width: 100%;" align="center"></div></body>
</html>