<%@ page language="java" contentType="text/html; charset=utf-8"
    pageEncoding="utf-8"%>
<%@ include file="/page/common/basePath.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<title>管理员后台</title>
</head>

<script type="text/javascript" src="<%=basePath %>js/others/jquery-1.9.1.min.js"></script>
<script type="text/javascript" src="<%=basePath %>js/others/jquery.md5.js"></script>
<script type="text/javascript" src="<%=basePath %>other/layer/layer.js"></script>

<link rel="stylesheet" type="text/css" href="<%=basePath %>other/ext-4.1.1/resources/css/ext-all.css" />
<script type="text/javascript" src="<%=basePath %>other/ext-4.1.1/ext-all.js"></script>
<link rel="stylesheet" type="text/css" href="<%=basePath %>other/ext-4.1.1/examples/shared/example.css" />
<!-- Example -->

<script type="text/javascript">
Ext.onReady(function () {
    Ext.create('Ext.container.Viewport', {
        layout: 'border',
        items: [{
            region: 'north',
            html: '<h1 class="x-panel-header">Logo</h1>',
            border: false,
            height: 50,
            margins: '0 0 0 0'
        }, {
            region: 'west',
            collapsible: true,
            split: true,
            id: 'MainMenu',
            title: '系统导航',
            width: 150,
            layout: 'accordion',
            items: [
                {
                    title: '个人信息',
                    layout: 'fit',
                    items: [
                        {
                            xtype: 'treepanel',
                            border: 0,
                            rootVisible: false,
                            root: {
                                expanded: true,
                                children: [
									{ id: "00", text: "个人图片", leaf: true, href: '#' },
                                    { id: "01", text: "基本资料", leaf: true, href: '#' },
                                    { id: "02", text: "密码修改", leaf: true, href: '#' },
                                    { id: "03", text: "发送邮件", leaf: true, href: '#' },
                                    { id: "04", text: "操作日志", leaf: true, href: '#' },
                                    { id: "05", text: "我的私信", leaf: true, href: '#' },
                                    { id: "06", text: "我的消息", leaf: true, href: '#' }
                                ]
                            }
                        }
                    ]
                },
                {
                    title: '用户管理',
                    layout: 'fit',
                    items: [
                        {
                            xtype: 'treepanel',
                            border: 0,
                            rootVisible: false,
                            root: {
                                expanded: true,
                                children: [
                                    { id: "11", text: "新增用户", leaf: true, href: '#' },
                                    { id: "12", text: "查询用户", leaf: true, href: '#' },
                                    { id: "13", text: "用户统计", leaf: true, href: '#' },
                                    { id: "14", text: "黑名单", leaf: true, href: '#' }
                                ]
                            }
                        }
                    ]
                },
                {
                    title: '博客管理',
                    layout: 'fit',
                    items: [
                        {
                            xtype: 'treepanel',
                            border: 0,
                            rootVisible: false,
                            root: {
                                expanded: true,
                                children: [
                                    { id: "21", text: "新增博客", leaf: true, href: '#' },
                                    { id: "22", text: "查询博客", leaf: true, href: '#' },
                                    { id: "23", text: "博客统计", leaf: true, href: '#' },
                                    { id: "24", text: "草稿箱", leaf: true, href: '#' }
                                ]
                            }
                        }
                    ]
                },
                {
                    title: '收藏关注',
                    layout: 'fit',
                    items: [
                        {
                            xtype: 'treepanel',
                            border: 0,
                            rootVisible: false,
                            root: {
                                expanded: true,
                                children: [
                                    { id: "31", text: "关注的用户", leaf: true, href: '#' },
                                    { id: "32", text: "收藏的博客", leaf: true, href: '#' },
                                    { id: "33", text: "喜欢的标签", leaf: true, href: '#' },
                                    { id: "34", text: "过滤的标签", leaf: true, href: '#' }
                                ]
                            }
                        }
                    ]
                },
                {
                    title: '系统设置',
                    layout: 'fit',
                    items: [
                        {
                            xtype: 'treepanel',
                            border: 0,
                            rootVisible: false,
                            root: {
                                expanded: true,
                                children: [
                                    { id: "41", text: "基本设置", leaf: true, href: '#' },
                                    { id: "42", text: "高级设置", leaf: true, href: '#' },
                                    { id: "43", text: "关于我们", leaf: true, href: '#' }
                                ]
                            }
                        }
                    ]
                }
            ]
            // could use a TreePanel or AccordionLayout for navigational items
        }, {
            region: 'south',
            collapsible: false,
            html: "<div align='center'>当前的系统时间是："+new Date()+",用户数：<span style='color:red;'>12/120</span>人</div>",
            split: false,
            height: 22,
            margin: '20 0 0 0'
        }/* , {
            region: 'east',
            title: '在线用户',
            collapsible: true,
            split: true,
            width: 200
        }  */, {
            region: 'center',
            xtype: 'tabpanel', 
            id: 'MainTabPanel',
            
            activeTab: 0,  
            height: 800,
            items: {
                title: '首页',
                html: '<h1>欢迎使用</h1>'
            }
        }]
    });

    bindNavToTab("MainMenu", "MainTabPanel");
});


function bindNavToTab(accordionId, tabId) {
    var accordionPanel = Ext.getCmp(accordionId);
    if (!accordionPanel) return;

    var treeItems = accordionPanel.queryBy(function (cmp) {
        if (cmp && cmp.getXType() === 'treepanel') return true;
        return false;
    });
    if (!treeItems || treeItems.length == 0) return;

    for (var i = 0; i < treeItems.length; i++) {
        var tree = treeItems[i];

        tree.on('itemclick', function (view, record, htmlElement, index, event, opts) {
            if (record.isLeaf()) {
                // 阻止事件传播
                event.stopEvent();

                var href = record.data.href;

                if (!href) return;
                // 修改地址栏
                //window.location.hash = '#' + href;
                // 新增Tab节点
                CreateIframeTab(tabId, record.data.id, record.data.text, href);
            }
        });
    }
}

function CreateIframeTab(tabpanelId, tabId, tabTitle, iframeSrc) {
    var tabpanel = Ext.getCmp(tabpanelId);
    if (!tabpanel) return;  //未找到tabpanel，返回

    //寻找id相同的tab
    var tab = Ext.getCmp(tabId);
    if (tab) { tabpanel.setActiveTab(tab); return; }
    
    loadHtmlPage(tabpanel, tabId, tabTitle, getBasePath() + "/page/admin/index-tab-"+tabId+".jsp");

   
}

/**
 * 在tab中插入html页面
 */
function loadHtmlPage(tabpanel,tabId,tabTitle,url){
	 //新建一个tab，并将其添加到tabpanel中
    //tab = Ext.create('Ext.tab.Tab', );
   var tab = tabpanel.add({
        id: tabId,
        title: tabTitle,
        closable: true,
        //autoScroll:true,//为panel里面的内容增加滚动条  
        autoLoad: {url: url,scripts:true}//打开链接并开启script脚本
        //autoLoad: {url: getBasePath() +"page/front/fullText.jsp",params:{ blog_id: 11}, method: 'GET',scripts:true}//打开链接并开启script脚本
    });
    tabpanel.setActiveTab(tab);
}

</script>

<body>
	<div align="left"></div>
</body>
</html>