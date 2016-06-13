<%@ page language="java" contentType="text/html; charset=utf-8"
    pageEncoding="utf-8"%>
<%@ include file="/page/common/basePath.jsp"%>

<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html> 
<head> 
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8"> 
<title>用户操作</title> 

<style type="text/css"> 
.button {   
     display: inline-block;   
     zoom: 1; /* zoom and *display = ie7 hack for display:inline-block */   
     *display: inline;   
     vertical-align: baseline;   
     margin: 0 2px;   
     outline: none;   
     cursor: pointer;   
     text-align: center;   
     text-decoration: none;   
     font: 8px/100% Arial, Helvetica, sans-serif;   
     padding: .2em 2em .5em;   
     text-shadow: 0 1px 1px rgba(0,0,0,.3);   
     -webkit-border-radius: .5em;    
     -moz-border-radius: .5em;   
     border-radius: .5em;   
     -webkit-box-shadow: 0 1px 2px rgba(0,0,0,.2);   
     -moz-box-shadow: 0 1px 2px rgba(0,0,0,.2);   
     box-shadow: 0 1px 2px rgba(0,0,0,.2);   
}   
.button:hover {   
     text-decoration: none;  
}   
.button:active {   
     position: relative;   
     top: 1px;   
}   
     
/* green */ 
.green { 
     color: #e8f0de; 
     border: solid 1px #538312; 
     background: #64991e; 
     background: -webkit-gradient(linear, left top, left bottom, from(#7db72f), to(#4e7d0e)); 
     background: -moz-linear-gradient(top,  #7db72f,  #4e7d0e); 
     filter:  progid:DXImageTransform.Microsoft.gradient(startColorstr='#7db72f', endColorstr='#4e7d0e'); 
} 
.green:hover { 
     background: #538018; 
     background: -webkit-gradient(linear, left top, left bottom, from(#6b9d28), to(#436b0c)); 
     background: -moz-linear-gradient(top,  #6b9d28,  #436b0c); 
     filter:  progid:DXImageTransform.Microsoft.gradient(startColorstr='#6b9d28', endColorstr='#436b0c'); 
} 
.green:active { 
     color: #a9c08c; 
     background: -webkit-gradient(linear, left top, left bottom, from(#4e7d0e), to(#7db72f)); 
     background: -moz-linear-gradient(top,  #4e7d0e,  #7db72f); 
     filter:  progid:DXImageTransform.Microsoft.gradient(startColorstr='#4e7d0e', endColorstr='#7db72f'); 
}  
</style>  

<script type="text/javascript">

(function(){ 
    Ext.onReady(function(){ 
        Ext.QuickTips.init(); 
        
      //1.定义Model
        Ext.define("MyApp.model.User", {
            extend: "Ext.data.Model",
            
            fields: [
                { name: 'account', type: 'string' },
                { name: 'age', type: 'int' },
                { name: 'email', type: 'string' },
                { name: 'china_name', type: 'string' },
                { name: 'nation', type: 'string' },
                { name: 'marry', type: 'string' },
                { name: 'native_place', type: 'string' },
                { name: 'education_background', type: 'string' },
                { name: 'mobile_phone', type: 'int' },
                { name: 'register_time', type: 'string'  }
            ]
        });
        //数据集  
        var store = Ext.create('Ext.data.Store', { 
       	 model: "MyApp.model.User",
       	 remoteSort: true,                
       	 /*sortInfo: [{                    
       		 	field: 'age',                    
       		 	direction: 'ASC'},{field: 'email',direction: 'DESC'}], */
       	 //fields: ['account','email','china_name','nation','marry','native_place','education_background','mobile_phone','register_time'],
       	 autoLoad: true,
       	 pageSize: 15,
            proxy: { 
           	 type: 'ajax', 
                url: getBasePath() +"user_getAllUsers.action",
                reader: { 
               	/* datatype: 'json',*/
               	 type:'json',
               	 totalProperty: 'total',
                    root: 'rows' 
                }
            } 
        }); 
        
        var grid = Ext.create("Ext.grid.Panel", {
       	 	title:"用户管理", 
       	 	frame: true,  //大边框   
       	 	forceFit: true, //自动填充 
       	    xtype: "grid",
       	    store: store,
       	 	anchor: '98% 100%',
       	    margin: 0,
       	    columnLines: true,
       	    renderTo: Ext.get("userGrid"),
       	    selModel: {
       	        injectCheckbox: 0,
       	        mode: "SIMPLE",     //"SINGLE"/"SIMPLE"/"MULTI"
       	        checkOnly: true     //只能通过checkbox选择
       	    },
       	    selType: "checkboxmodel",
       	    columns: [
       	        {text: '账号', dataIndex: 'account', tooltip:'账号'/* ,remoteSort: false */},
       	        {
       	        	text: '年龄', dataIndex: 'age', xtype: 'numbercolumn', format: '0', tooltip:'年龄', remoteSort: true,
       	            editor: {
       	                xtype: "numberfield",
       	                decimalPrecision: 0,
       	                selectOnFocus: true
       	            }
       	        },
       	        {text: '邮箱', width:150, dataIndex: 'email', editor: 'textfield', tooltip:'邮箱'},
       	        {text: '中文名', dataIndex: 'china_name', editor: 'textfield', tooltip:'中文名'},
       	     	{text: '民族', dataIndex: 'nation', editor: 'textfield', tooltip:'民族'},
       	     	{text: '婚姻状态', dataIndex: 'marry', editor: 'textfield', tooltip:'婚姻状态'},
       	     	{text: '籍贯', dataIndex: 'native_place', editor: 'textfield', tooltip:'籍贯'},
       	     	{text: '学历', dataIndex: 'education_background', editor: 'textfield', tooltip:'学历'},
       	     	{text: '手机号码', dataIndex: 'mobile_phone', editor: 'textfield', tooltip:'手机号码'},
       	        {text: '注册时间', dataIndex: 'register_time', tooltip:'注册时间'}
       	        
       	    ],
       	    plugins: [
       	        Ext.create('Ext.grid.plugin.CellEditing', {
       	            clicksToEdit: 1
       	        })
       	    ],
       	    listeners: {
       	        itemdblclick: function (me, record, item, index, e, eOpts) {
       	            //双击事件的操作
       	        	alert("双击");
       	        }
       	    },
       	    bbar: { xtype: "pagingtoolbar", store: store, displayInfo: true }
       	    , 
               dockedItems:[{
                   xtype: 'toolbar',
                   items: [{
                       text:'添加',
                       tooltip:'add',
                       iconCls:'add',handler:function(button){
                    	   var tabPanel = Ext.getCmp(getTabPanelName());
                    	   var tab = Ext.getCmp('11');
                    	    if (tab) { tabPanel.setActiveTab(tab); return; }
                    	   tab =tabPanel.add({
                    	        id: '11',
                    	        title: '新增用户',
                    	        closable: true,
                    	        autoLoad: {url: getBasePath() + "page/admin/index-tab-11.jsp",scripts:true}//打开链接并开启script脚本
                    	    });
                    	   tabPanel.setActiveTab(tab);
						}
                   }, '-', {
                       text:'修改',
                       tooltip:'update',
                       iconCls:'option',handler:function(button){alert(button.text); }
                   }, '-', {
                       text:'查看',
                       tooltip:'view',
                       iconCls:'option',handler:function(button){alert(button.text); }
                   },'-',{
                       itemId: 'removeButton',
                       text:'删除',
                       tooltip:'delete',
                       iconCls:'remove',
                       	handler:function(button){
                       		//获取选中的行  
                               var grid = button.findParentByType("gridpanel"); 
                               var data = grid.getSelectionModel().getSelection(); 
                               if(data.length==0){ 
                                   alert("请选择一条数据");  
                               }else{ 
                                   //先得到主键 这里假定是name   
                                   var store = grid.getStore(); 
                                   var ids = []; 
                                   Ext.Array.each(data,function(record){ 
                                       ids.push(record.get("name")); 
                                   }); 
                                   console.log(ids); 
                                   //后台删除 这里没有后台 假定成功了, 返回一个数组，ids  
                                   Ext.Array.each(data,function(record){ 
                                       store.remove(record); 
                                   }); 
                               } 
                       	}
                       /*disabled: true*/
                   }]
               }],buttons: [{
                   text: '提交',
                   handler: function() {
                       this.up('form').getForm().isValid();
                       simple.getForm().load({
                           url: 'xml-form-errors.xml',
                           submitEmptyText: false,
                           waitMsg: 'Saving Data...'
                       });
                   }
               },{
                   text: '清空',
                   handler: function() {
                       this.up('form').getForm().reset();
                   }
               }]
       	});
        //big panel  
        /*var grid = Ext.create("Ext.grid.Panel",{ 
            title:"Grid demo", 
            frame: true,  //大边框   
            forceFit: true, //自动填充  
            columns:[  //age email 可编辑  
                {text:"name",dataIndex:"name"   }, 
                {text:"age", dataIndex:"age",field:{xtype:"textfield",allowBlank:false}}, 
                {text:"email", dataIndex:"email",field:{xtype:"textfield",allowBlank:false}} 
            ], 
            renderTo:Ext.get("griddemo"),  //要渲染的页面组件，就是一个div  
            width:500, 
            height:300, 
            store: Ext.data.StoreManager.lookup('simpsonsStore'),  //载入数据集  
            tbar:[ //topbar 上面工具栏,每个配置依次是组件名称，名字，样式，单击事件  
                {xtype:"button", text:"添加",handler:function(button){alert(button.text); }}, 
                {xtype:"button", text:"修改",handler:function(button){alert(button.text); }}, 
                {xtype:"button", text:"查看",handler:function(button){alert(button.text); }}, 
                {xtype:"button", text:"删除",cls:"button green", 
                    handler:function(button){ 
                   	 alert(button.text);
                        //获取选中的行  
                        var grid = button.findParentByType("gridpanel"); 
                        var data = grid.getSelectionModel().getSelection(); 
                        if(data.length==0){ 
                            alert("请选择一条数据");  
                        }else{ 
                            //先得到主键 这里假定是name   
                            var store = grid.getStore(); 
                            var ids = []; 
                            Ext.Array.each(data,function(record){ 
                                ids.push(record.get("name")); 
                            }); 
                            console.log(ids); 
                            //后台删除 这里没有后台 假定成功了, 返回一个数组，ids  
                            Ext.Array.each(data,function(record){ 
                                store.remove(record); 
                            }); 
                        } 
                    } 
                } 
            ], 
            dockedItems:[{ 
                xtype:"pagingtoolbar", //分页组件  
                store:Ext.data.StoreManager.lookup('simpsonsStore'),  
                dock:"bottom",  //下面的分页栏  
                displayInfo:true //右下方信息显示  
                
            }, {
                xtype: 'toolbar',
                items: [{
                    text:'添加',
                    tooltip:'add',
                    iconCls:'add',handler:function(button){alert(button.text); }
                }, '-', {
                    text:'修改',
                    tooltip:'update',
                    iconCls:'option',handler:function(button){alert(button.text); }
                }, '-', {
                    text:'查看',
                    tooltip:'view',
                    iconCls:'option',handler:function(button){alert(button.text); }
                },'-',{
                    itemId: 'removeButton',
                    text:'删除',
                    tooltip:'delete',
                    iconCls:'remove',handler:function(button){alert(button.text); }
                    disabled: true
                }]
            }], 
            selType:"checkboxmodel",  //多选框选择模式  
            multiSelect:true,   //允许多选  
            plugins: [ 
                Ext.create('Ext.grid.plugin.CellEditing', { 
                    clicksToEdit: 1 
                }) 
            ] 
        })     */
         
         
    //ready end      
    });      
})(); 
</script>
</head> 
<body> 
<div id="userGrid"></div>  
</body> 
</html> 