package com.cn.leedane.struts2.action;
import javax.annotation.Resource;

import net.sf.json.JSONObject;

import com.cn.leedane.Utils.BaseActionContext;
import com.cn.leedane.Utils.EnumUtil;
import com.cn.leedane.Utils.HttpUtils;
import com.cn.leedane.bean.ChatBean;
import com.cn.leedane.service.ChatService;
/**
 * 聊天信息action类
 * @author LeeDane
 * 2016年5月6日 上午12:03:43
 * Version 1.0
 */
public class ChatAction extends BaseActionContext{
	private static final long serialVersionUID = 1L;
	
	private ChatService<ChatBean> chatService;
	
	@Resource
	public void setChatService(ChatService<ChatBean> chatService) {
		this.chatService = chatService;
	}
	
	/**
	 * 发送聊天信息
	 * @return
	 */
	public String send() {
		try {
			message.put("isSuccess", false);
			JSONObject jo = HttpUtils.getJsonObjectFromInputStream(params, request);  
			if(jo == null || jo.isEmpty()) {	
				message.put("message", EnumUtil.getResponseValue(EnumUtil.ResponseCode.缺少请求参数.value));
				message.put("responseCode", EnumUtil.ResponseCode.缺少请求参数.value);
				return SUCCESS;
			}
			message.putAll(chatService.send(jo, user, request));
			return SUCCESS;
		} catch (Exception e) {
			e.printStackTrace();
		}     
        message.put("message", EnumUtil.getResponseValue(EnumUtil.ResponseCode.服务器处理异常.value));
		message.put("responseCode", EnumUtil.ResponseCode.服务器处理异常.value);
        return SUCCESS;
	}
	
	/**
	 * 分页获取聊天历史列表(两个人的聊天)
	 * @return
	 */
	public String paging() {
		try {
			message.put("isSuccess", false);
			JSONObject jo = HttpUtils.getJsonObjectFromInputStream(params, request);  
			if(jo == null || jo.isEmpty()) {	
				message.put("message", EnumUtil.getResponseValue(EnumUtil.ResponseCode.缺少请求参数.value));
				message.put("responseCode", EnumUtil.ResponseCode.缺少请求参数.value);
				return SUCCESS;
			}
			message.putAll(chatService.getLimit(jo, user, request));
			return SUCCESS;
		} catch (Exception e) {
			e.printStackTrace();
		}     
        message.put("message", EnumUtil.getResponseValue(EnumUtil.ResponseCode.服务器处理异常.value));
		message.put("responseCode", EnumUtil.ResponseCode.服务器处理异常.value);
        return SUCCESS;
	}
	
	/**
	 * 更新聊天信息为已读状态
	 * @return
	 */
	public String updateRead() {
		try {
			message.put("isSuccess", false);
			JSONObject jo = HttpUtils.getJsonObjectFromInputStream(params, request);  
			if(jo == null || jo.isEmpty()) {	
				message.put("message", EnumUtil.getResponseValue(EnumUtil.ResponseCode.缺少请求参数.value));
				message.put("responseCode", EnumUtil.ResponseCode.缺少请求参数.value);
				return SUCCESS;
			}
			message.putAll(chatService.updateRead(jo, user, request));
			return SUCCESS;
		} catch (Exception e) {
			e.printStackTrace();
		}     
        message.put("message", EnumUtil.getResponseValue(EnumUtil.ResponseCode.服务器处理异常.value));
		message.put("responseCode", EnumUtil.ResponseCode.服务器处理异常.value);
        return SUCCESS;
	}
	
	/**
	 * 获取用户全部未读的信息
	 * @return
	 */
	public String noReadList() {
		try {
			message.put("isSuccess", false);
			JSONObject jo = HttpUtils.getJsonObjectFromInputStream(params, request);  
			if(jo == null || jo.isEmpty()) {	
				message.put("message", EnumUtil.getResponseValue(EnumUtil.ResponseCode.缺少请求参数.value));
				message.put("responseCode", EnumUtil.ResponseCode.缺少请求参数.value);
				return SUCCESS;
			}
			message.putAll(chatService.noReadList(jo, user, request));
			return SUCCESS;
		} catch (Exception e) {
			e.printStackTrace();
		}     
        message.put("message", EnumUtil.getResponseValue(EnumUtil.ResponseCode.服务器处理异常.value));
		message.put("responseCode", EnumUtil.ResponseCode.服务器处理异常.value);
        return SUCCESS;
	}
}
