<%@ page language="java" contentType="text/html; charset=utf-8"
    pageEncoding="utf-8"%>
<%@ include file="/page/common/basePath.jsp"%>

<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<%-- 不需要重复添加，否则会造成冲突，导致页面的标签不能正常使用
<script type="text/javascript" src="<%=basePath %>js/others/jquery-1.9.1.min.js"></script>
<script type="text/javascript" src="<%=basePath %>js/others/jquery.md5.js"></script>

<link rel="stylesheet" type="text/css" href="<%=basePath %>other/ext-4.1.1/resources/css/ext-all.css" />
<script type="text/javascript" src="<%=basePath %>other/ext-4.1.1/ext-all.js"></script>
<link rel="stylesheet" type="text/css" href="<%=basePath %>other/ext-4.1.1/examples/shared/example.css" /> --%>

<script type="text/javascript" src="<%=basePath %>other/ext-4.1.1/examples/shared/states.js"></script>

<title>基本资料</title>
</head>
<script type="text/javascript">

Ext.require([
             'Ext.form.*',
             'Ext.data.*'
         ]);

Ext.onReady(function() {
	
	var url = getBasePath() + "user_login.action";
    Ext.QuickTips.init();

    var bd = Ext.getBody();
 
    var required = '<span style="color:red;font-weight:bold" data-qtip="Required">*</span>';
     
	var tab11 = Ext.widget({
        title: '基本信息',
        xtype: 'form',
        id: 'innerTabsForm',
        collapsible: true,
        bodyPadding: 10,
        fieldDefaults: {
            labelAlign: 'top',
            msgTarget: 'side'
        },
        defaults: {
            anchor: '80%'
        },

        items: [{
            xtype: 'container',
            layout:'hbox',
            items:[{
                xtype: 'container',
                flex: 1,
                border:false,
                layout: 'anchor',
                defaultType: 'textfield',
                items: [{
                    fieldLabel: '姓名',
                    afterLabelTextTpl: required,
                    allowBlank: false,
                    name: 'account',
                    anchor:'95%'
                }, {
                	anchor:'95%',
                    xtype: 'combo',
                    mode: 'local',
                    value: '男',
                    triggerAction: 'all',
                    forceSelection: true,
                    editable: false,
                    fieldLabel: '性别',
                    name: 'sex',
                    displayField: 'value',
                    valueField: 'value',
                    queryMode: 'local',
                    store: Ext.create('Ext.data.Store', {
                        fields: ['name', 'value'],
                        data: [
                            {name: 'mr', value: '男'},
                            {name: 'Mrs', value: '女'},
                            {name: 'Miss', value: '未知'}
                        ]
                    })                 
                },{
                    fieldLabel: '密码',
                    afterLabelTextTpl: required,
                    allowBlank: false,
                    name: 'password',
                    inputType: 'password',
                    anchor:'95%'
                }]
            },{
                xtype: 'container',
                flex: 1,
                layout: 'anchor',
                defaultType: 'textfield',
                items: [{
                    fieldLabel: '出生年月',
                    afterLabelTextTpl: required,
                    allowBlank: false,
                    xtype: 'datefield',
                    value: '01/01/1990',
                    name: 'birthDay',
                    anchor:'95%'
                },{
                    fieldLabel: '年龄',
                    afterLabelTextTpl: required,
                    allowBlank: false,
                    xtype: 'numberfield',
                    value: 18,
                    name: 'age',
                    minValue: 0,
                    maxValue: 100,
                    anchor:'95%'
                },{
                    fieldLabel: '确认密码',
                    afterLabelTextTpl: required,
                    allowBlank: false,
                    name: 'againPassword',
                    inputType: 'password',
                    anchor:'95%'
                }]
            },{
                xtype: 'container',
                flex: 1,
                layout: 'anchor',
                defaultType: 'textfield',
                items: [{
                    fieldLabel: '民族',
                    anchor:'95%',
                    xtype: 'combo',
                    mode: 'local',
                    value: '汉族',
                    triggerAction: 'all',
                    forceSelection: true,
                    editable: false,
                    afterLabelTextTpl: required,
                    allowBlank: false,
                    name: 'nation',
                    displayField: 'value',
                    valueField: 'value',
                    queryMode: 'local',
                    store: Ext.create('Ext.data.Store', {
                        fields: ['name', 'value'],
                        data: [
                            {name: 'han', value: '汉族'},
                            {name: 'shaoshu', value: '少数民族'},
                        ]
                    }) 
                },{
                	fieldLabel: '婚否',
                    anchor:'95%',
                    xtype: 'combo',
                    mode: 'local',
                    value: '未婚',
                    triggerAction: 'all',
                    forceSelection: true,
                    afterLabelTextTpl: required,
                    allowBlank: false,
                    editable: false,
                    name: 'marry',
                    displayField: 'value',
                    valueField: 'value',
                    queryMode: 'local',
                    store: Ext.create('Ext.data.Store', {
                        fields: ['name', 'value'],
                        data: [
                            {name: 'unmarried', value: '未婚'},
                            {name: 'married', value: '已婚'},
                            {name: 'divorce', value: '离婚'},
                            {name: 'deathspouse', value: '丧偶'}
                        ]
                    }) 
                }]
            },{
                xtype: 'container',
                flex: 1,
                layout: 'anchor',
                defaultType: 'textfield',
                items: [{
                	fieldLabel: '身份证号码',
                    name: 'idCard',
                    anchor:'95%'
                },{
                    fieldLabel: '籍贯',
                    name: 'nativePlace',
                    anchor:'95%'
                }]
            }]
        },{
            xtype:'tabpanel',
            plain:true,
            activeTab: 0,
            height:235,
            autoScroll:true,
            defaults:{
                bodyPadding: 10
            },
            items:[{
                title:'详细信息',
                defaults: {
                    width: 230
                },
                defaultType: 'textfield',

                items: [{
                    fieldLabel: '最高学历',
                    name: 'educationBackground',
                },{
                    fieldLabel: '毕业学校',
                    name: 'school',
                },{
                    fieldLabel: '目前公司',
                    name: 'company',
                },{
                    fieldLabel: '公司地址',
                    name: 'companyAddress',
                }]
            },{
                title:'电话号码',
                defaults: {
                    width: 230
                },
                defaultType: 'textfield',

                items: [{
                    fieldLabel: '家庭号码',
                    name: 'homePhone',
                    value: '格式如：(888) 555-1212'
                },{
                    fieldLabel: '手机号码',
                    name: 'mobilePhone'
                },{
                    fieldLabel: '公司电话',
                    name: 'companyPhone'
                }]
            },{
                cls: 'x-plain',
                title: '个人介绍',
                layout: 'fit',
                items: {
                    xtype: 'htmleditor',
                    name: 'personalIntroduction',
                }
            }]
        }],

        buttons: [{
            text: '保存',
            handler: function() {
                this.up('form').getForm().isValid();
            }
        },{
            text: '清空',
            handler: function() {
                this.up('form').getForm().reset();
            }
        }]
    });

    tab11.render(Ext.get("showForm11"));
});

</script>
<body>
<div id="showForm11" style="width: 100%;" align="center"></div></body>
<!-- <div id="showForm1" style="width: 45%;padding-top: 50;float: left;" align="center"></div></body>
 --></html>