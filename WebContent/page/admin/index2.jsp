<%@ page language="java" contentType="text/html; charset=utf-8"
    pageEncoding="utf-8"%>
<%@ include file="/page/common/basePath.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<title>Admin</title>
</head>

<script type="text/javascript" src="<%=basePath %>js/others/jquery-1.9.1.min.js"></script>
<script type="text/javascript" src="<%=basePath %>js/others/jquery.md5.js"></script>
<script type="text/javascript" src="<%=basePath %>other/layer/layer.js"></script>

<link rel="stylesheet" type="text/css" href="<%=basePath %>other/ext-4.1.1/resources/css/ext-all.css" />
<script type="text/javascript" src="<%=basePath %>other/ext-4.1.1/ext-all.js"></script>
<link rel="stylesheet" type="text/css" href="<%=basePath %>other/ext-4.1.1/examples/shared/example.css" />
<!-- Example -->

<script type="text/javascript">

	Ext.define("AM.view.SystemTree", { 
	extend : 'Ext.tree.Panel', 
	alias : 'widget.systemTree', 
	rootVisible : false,// 不展示ROOT 
	displayField : 'text', 
	// title:'物流运输系统', 
	viewConfig : { // 具有拖拽功能 
	plugins : { 
	ptype : 'treeviewdragdrop' 
	}, 
	listeners : { // 拖拽 
	drop : function(node, data, overModel, dropPosition, options) { 
	alert("把: " + data.records[0].get('text') + " 移动到: " 
	+ overModel.get('text')); 
	} 
	} 
	}, 
	dockedItems : [ { 
	xtype : 'toolbar', 
	items : [ { 
	xtype : 'button', 
	id : 'allopen', 
	icon : 'resources/img/lock_open.png', 
	text : '展开全部' 
	}, { 
	xtype : 'button', 
	id : 'allclose', 
	icon : 'resources/img/lock.png', 
	text : '收起全部' 
	} ] 
	} ], 
	root : { 
	text : 'root', 
	leaf : false, 
	id : '0', 
	children : [ { 
	text : '运输管理', 
	checked : false, // 显示被选中 
	leaf : false, // 是否是叶子节点 
	icon : 'resources/img/folder_user.png', 
	id : '01', 
	children : [ { 
	text : '车辆信息管理', 
	checked : false, 
	icon : 'resources/img/report_edit.png', 
	leaf : true, 
	id : 'vehiclelist',　　//主要的要点在这里，这里的id要指定为你要打开的那个视图的别名 
	}] 
	}] 
	} 
	}); 
	
	
	

	Ext.define("AM.view.transportation.VehicleList",{ 
	extend:'Ext.grid.Panel', 
	alias:'widget.vehiclelist', 　　//这里一定要设置别名 
	id:'vehiclelist', 
	store:'VehicleStore', 
	　'systemTree':{ 
		itemclick:function(tree,record,item,index,e,options){ 
			var tabs = tree.ownerCt.ownerCt.ownerCt 
			.child('#center-grid').child("#tabpanel"); 
			//获取当前点击的节点 
			var treeNode=record.raw; 
			var id = treeNode.id; 
			var text=treeNode.text; 
			//获取点击的树节点相同的tab标签 
			var tab = tabs.getComponent(id); 
			if(!tab){//如果不存在 
			tabs.add({//用点击树的节点的ID、text新建一个tab 
			id:id, 
			closable: true, 
			title:text, 
			iconCls:id, 
			xtype:id　　//将tree设置好的id对应的TabPanel中去，相当与把对应的view填充到TabPanel中 
			}).show(); 
			}else{//如果存在 
			tabs.setActiveTab(tab);//Active 
			} 
			} 
			}, 
	columns : [ 
	{text:'车辆编号',dataIndex:'vehicleNo', 
	field:{ 
	xtype:'textfield', 
	allowBlank:false 
	} 
	}, 
	{text:'车辆描述',xtype:'templatecolumn', 
	tpl:'车辆的编号为{vehicleNo} 所在地区为{vehicleArea}' 
	} 
	], 
	initComponent:function(){ 
	this.callParent(arguments); 
	} 
	}); 
	
	
	
	

	Ext.define('AM.view.TabPanel',{ //主页面的tab面板 
	extend: 'Ext.tab.Panel', 
	alias:'widget.tabpanel', 
	closeAction: 'destroy', 
	defaults :{ 
	bodyPadding: 10 
	}, 
	items: [{ 
	title: '主页', 
	autoLoad:'content.jsp'　　　　//只有一个基本的panel 
	}], 
	}); 
	
	
	

	
	
	
	
	


</script>

<body>

</body>
</html>