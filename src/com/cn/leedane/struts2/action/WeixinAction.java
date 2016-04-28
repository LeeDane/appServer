package com.cn.leedane.struts2.action;

import java.io.PrintWriter;
import java.util.Map;

import org.apache.struts2.ServletActionContext;
import org.springframework.beans.factory.annotation.Autowired;

import com.cn.leedane.Utils.BaseActionContext;
import com.cn.leedane.Utils.SpringUtils;
import com.cn.leedane.Utils.StringUtil;
import com.cn.leedane.bean.UserBean;
import com.cn.leedane.handler.WechatHandler;
import com.cn.leedane.service.UserService;
import com.cn.leedane.wechat.bean.WeixinCacheBean;
import com.cn.leedane.wechat.service.BaseXMLWechatService;
import com.cn.leedane.wechat.service.impl.BindUserXMLService;
import com.cn.leedane.wechat.service.impl.BlogXMLService;
import com.cn.leedane.wechat.service.impl.ChatXMLService;
import com.cn.leedane.wechat.service.impl.SearchXMLService;
import com.cn.leedane.wechat.service.impl.SendMoodXMLService;
import com.cn.leedane.wechat.service.impl.TranslationXMLService;
import com.cn.leedane.wechat.util.CheckUtil;
import com.cn.leedane.wechat.util.MessageUtil;
import com.cn.leedane.wechat.util.WeixinUtil;

/**
 * 微信相关的action类
 * @author LeeDane
 * 2015年7月17日 下午6:33:40
 * Version 1.0
 */
public class WeixinAction extends BaseActionContext{

	/**
	 * create time 2015年6月25日 下午3:38:37
	 */
	private static final long serialVersionUID = 1L;
	
	/**
	 * get请求方法名称
	 */
	public static final String REQUEST_METHOD_GET = "GET";
	
	/**
	 * post请求方法名称
	 */
	public static final String REQUEST_METHOD_POST = "POST";
	
	public String signature;
	public String timestamp;
	public String nonce;
	public String echostr;
	BaseXMLWechatService baseService;
	
	@Autowired
	private WechatHandler wechatHandler;
	
	public void setWechatHandler(WechatHandler wechatHandler) {
		this.wechatHandler = wechatHandler;
	}
	
	@Autowired
	private UserService<UserBean> userService;
	
	public void setUserService(UserService<UserBean> userService) {
		this.userService = userService;
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public String execute() throws Exception {
		request.setCharacterEncoding("utf-8");
		response.setCharacterEncoding("utf-8");
		PrintWriter out = response.getWriter();  
		String method = ServletActionContext.getRequest().getMethod();
		//get请求的处理
		if(method.equals(REQUEST_METHOD_GET)){
			if(CheckUtil.checkSignature(signature, timestamp, nonce))
				out.print(echostr);
		}else{
			if(wechatHandler == null){
				System.out.println("需要通过springUtil方式获取wechatHandler对象");
				wechatHandler = (WechatHandler) SpringUtils.getBean("wechatHandler");
			}
			
			if(userService == null){
				System.out.println("需要通过springUtil方式获取wechatHandler对象");
				userService = (UserService<UserBean>) SpringUtils.getBean("userService");
			}
			Map<String,String> map = MessageUtil.xmlToMap(request);
			String FromUserName = map.get("FromUserName");
			String ToUserName = map.get("ToUserName");
			String Content = map.get("Content");
			try{
				
				WeixinCacheBean cacheBean = wechatHandler.getFromUserInfo(FromUserName);
				String currentType = WeixinUtil.MODEL_MAIN_MENU;//默认是主菜单				
				if(cacheBean != null){
					if(!StringUtil.isNull(cacheBean.getCurrentType())){
						 currentType =  cacheBean.getCurrentType();
					}
				}else{
					cacheBean =new WeixinCacheBean();
				}
				
				System.out.println("用户发送的："+Content);
				
				String returnMsg = "请输入‘主菜单’，进入主菜单状态；\n\r 输入‘翻译’，进入翻译模式；\n\r 输入‘聊天’，进入聊天模式；\n\r 输入‘查询’，进入官网查询模式；\n\r 输入‘心情’，进入发布心情模式；\n\r 输入‘最新博客’，获取最新的博客；\n\r输入‘退出’，退出绑定。";
				//处理用户请求的内容，判断需要调用哪个接口
				//主菜单
				if(Content.startsWith("主菜单") || Content.equals(WeixinUtil.MODEL_MAIN_MENU)){
					cacheBean.setCurrentType(WeixinUtil.MODEL_MAIN_MENU);
					cacheBean.setLastBlogId(0);
					wechatHandler.addCache(FromUserName, cacheBean);
				//最新博客
				}else if(Content.startsWith("最新博客") || Content.equals(WeixinUtil.MODEL_NEWEST_BLOG)){	
					cacheBean.setCurrentType(WeixinUtil.MODEL_NEWEST_BLOG);
					wechatHandler.addCache(FromUserName, cacheBean);
					baseService = new BlogXMLService(request,map);
					returnMsg = baseService.responseXML();
				}else if(Content.startsWith("翻译") || currentType.equals(WeixinUtil.MODEL_TRANSLATION)){//翻译
					cacheBean.setCurrentType(WeixinUtil.MODEL_TRANSLATION);
					wechatHandler.addCache(FromUserName, cacheBean);
					baseService = new TranslationXMLService(request,map);
					returnMsg = baseService.responseXML();
					
				}else if( Content.startsWith("聊天") || currentType.equals(WeixinUtil.MODEL_CHAT)){//默认调用
					cacheBean.setCurrentType(WeixinUtil.MODEL_CHAT);
					wechatHandler.addCache(FromUserName, cacheBean);
					if(Content.startsWith("聊天")){
						Content = Content.substring(2, Content.length());
						if(StringUtil.isNull(Content)){
							returnMsg = "进入聊天模式";
						}else{
							baseService = new ChatXMLService(request,map);
							returnMsg = baseService.responseXML();
						}
					}else{
						baseService = new ChatXMLService(request,map);
						returnMsg = baseService.responseXML();
					}
					
				}else if(Content.startsWith("查询") || currentType.equals(WeixinUtil.MODEL_BLOG_SEARCH)){
					cacheBean.setCurrentType(WeixinUtil.MODEL_BLOG_SEARCH);
					wechatHandler.addCache(FromUserName, cacheBean);
					if(Content.startsWith("查询")){
						Content = Content.substring(2, Content.length());
					}
					if(!StringUtil.isNull(Content)){
						baseService = new SearchXMLService(request,map);
						returnMsg = baseService.responseXML();
					}else{
						returnMsg = "进入官网查询模式";
					}
						
				}else if(Content.startsWith("心情") || currentType.equals(WeixinUtil.MODEL_SEND_MOOD)){
					
					//判断用户是否已经登录
					if(!cacheBean.isBindLogin()){
						baseService = new BindUserXMLService(request,map);
						returnMsg = baseService.responseXML();
					}else{
						
						UserBean user = userService.loginByWeChat(FromUserName);
						
						if(user != null){
							cacheBean.setCurrentType(WeixinUtil.MODEL_SEND_MOOD);
							wechatHandler.addCache(FromUserName, cacheBean);
							if(Content.startsWith("心情")){
								Content = Content.substring(2, Content.length());
							}
							if(!StringUtil.isNull(Content)){
								baseService = new SendMoodXMLService(request,map);
								returnMsg = baseService.responseXML();
							}else{
								returnMsg = "进入发送心情模式";
							}
						}else{
							baseService = new BindUserXMLService(request,map);
							returnMsg = baseService.responseXML();
						}
					}
					
				}else if(Content.startsWith("退出") || Content.equals(WeixinUtil.MODEL_OUT)){
	
					cacheBean.setCurrentType(WeixinUtil.MODEL_MAIN_MENU);
					if(userService.wechatUnBind(FromUserName)){
						returnMsg = "成功解绑用户，已经退出";
						wechatHandler.removeCache(FromUserName);
					}else{
						returnMsg = "退出失败，请稍后重试";
					}
				}
				
				if(!returnMsg.startsWith("<xml>")){
					returnMsg = MessageUtil.sendText(ToUserName, FromUserName, returnMsg);
				}
				System.out.println(returnMsg +"----" +returnMsg);
				out.print(returnMsg);
				return null;
			}catch(Exception e){
				e.printStackTrace();
				out.print(MessageUtil.sendText(ToUserName, FromUserName, "服务器处理失败，请稍后重试"));
			}finally{
				out.close();
			}
			return null;
		}
		out.close();
		return null;
	}
}
