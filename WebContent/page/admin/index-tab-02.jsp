<%@ page language="java" contentType="text/html; charset=utf-8"
    pageEncoding="utf-8"%>
<%@ include file="/page/common/basePath.jsp"%>

<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">

<title>修改登录密码</title>
</head>
<script type="text/javascript">
Ext.onReady(function() {
	
	var url = getBasePath() + "user_login.action";
    //Ext.QuickTips.init();

    var bd = Ext.getBody();
 
    var required = '<span style="color:red;font-weight:bold" data-qtip="Required">*</span>';
     
    var simple = Ext.widget({
        xtype: 'form',
        layout: 'form',
        collapsible: true,
        id: 'simpleForm',
        url: 'save-form.php',
        frame: true,
        title: '修改登录密码',
        bodyPadding: '5 5 0',
        height: 250,
        width: 550,
        margin: '30 0 0',
        defaults: {
            anchor: '100%'
        },
        fieldDefaults: {
            msgTarget: 'side',
            labelWidth: 135
        },
        defaultType: 'textfield',
        items: [{
            fieldLabel: '原始密码',
            afterLabelTextTpl: required,
            name: 'password',
            allowBlank: false,
        },{
            fieldLabel: '新密码',
            afterLabelTextTpl: required,
            name: 'newPassword',
            allowBlank: false,
            margin: '100 0 0'
        },{
        	fieldLabel: '再次输入新密码',
            afterLabelTextTpl: required,
            name: 'newPassword1',
            allowBlank: false
        }],

        buttons: [{
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

    simple.render(Ext.get("showForm02"));
  
});

</script>
<body>
<div id="showForm02" style="width: 100%;" align="center"></div></body>
</html>