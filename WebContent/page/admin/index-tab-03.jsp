<%@ page language="java" contentType="text/html; charset=utf-8"
    pageEncoding="utf-8"%>
<%@ include file="/page/common/basePath.jsp"%>

<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">

<title>发送电子邮件</title>
</head>
<script type="text/javascript">
Ext.onReady(function() {
	
	var required = '<span style="color:red;font-weight:bold" data-qtip="Required">*</span>';
	
	var top = Ext.widget({
        xtype: 'form',
        id: 'multiColumnForm',
        collapsible: true,
        frame: true,
        title: '发送电子邮件',
        bodyPadding: '5 5 0',
        width: 600,
        fieldDefaults: {
            labelAlign: 'top',
            msgTarget: 'side'
        },

        items: [{
            xtype: 'container',
            anchor: '100%',
            layout: 'hbox',
            items:[{
                xtype: 'container',
                flex: 1,
                layout: 'anchor',
                items: [{
                    xtype:'textfield',
                    fieldLabel: '对方邮件地址',
                    afterLabelTextTpl: required,
                    allowBlank: false,
                    name: 'first',
                    anchor:'95%',
                }, {
                    xtype:'textfield',
                    fieldLabel: '主题/标题',
                    afterLabelTextTpl: required,
                    allowBlank: false,
                    name: 'company',
                    anchor:'95%'
                }]
            }]
        }, {
            xtype: 'htmleditor',
            name: 'bio',
            fieldLabel: '邮件内容',
            height: 200,
            anchor: '100%'
        }],

        buttons: [{
            text: '发送',
            handler: function() {
                this.up('form').getForm().isValid();
            }
        },{
            text: '清空',
            handler: function() {
                this.up('form').getForm().reset();
            }
        },{
            text: '关闭',
            handler: function() {
            	Ext.getCmp("MainTabPanel").remove("03");
            }
        }]
    });

    top.render(Ext.get("showForm03"));
    //top.hide();
});

</script>
<body>
<div id="showForm03" style="width: 100%;" align="center"></div></body>
</html>