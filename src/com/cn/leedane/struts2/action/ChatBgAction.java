package com.cn.leedane.struts2.action;
import javax.annotation.Resource;

import net.sf.json.JSONObject;

import com.cn.leedane.Utils.BaseActionContext;
import com.cn.leedane.Utils.EnumUtil;
import com.cn.leedane.Utils.HttpUtils;
import com.cn.leedane.bean.ChatBgBean;
import com.cn.leedane.service.ChatBgService;
/**
 * 聊天背景action类
 * @author LeeDane
 * 2016年6月10日 下午7:07:11
 * Version 1.0
 */
public class ChatBgAction extends BaseActionContext{
	private static final long serialVersionUID = 1L;
	
	private ChatBgService<ChatBgBean> chatBgService;
	
	@Resource
	public void setChatBgService(ChatBgService<ChatBgBean> chatBgService) {
		this.chatBgService = chatBgService;
	}
	
	/**
	 * 发布聊天背景历史
	 * @return
	 */
	public String publish() {
		try {
			message.put("isSuccess", false);
			JSONObject jo = HttpUtils.getJsonObjectFromInputStream(params, request);  
			if(jo == null || jo.isEmpty()) {	
				message.put("message", EnumUtil.getResponseValue(EnumUtil.ResponseCode.缺少请求参数.value));
				message.put("responseCode", EnumUtil.ResponseCode.缺少请求参数.value);
				return SUCCESS;
			}
			message.putAll(chatBgService.publish(jo, user, request));
			return SUCCESS;
		} catch (Exception e) {
			e.printStackTrace();
		}     
        message.put("message", EnumUtil.getResponseValue(EnumUtil.ResponseCode.服务器处理异常.value));
		message.put("responseCode", EnumUtil.ResponseCode.服务器处理异常.value);
        return SUCCESS;
	}
	
	/**
	 * 分页获取聊天背景列表
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
			message.putAll(chatBgService.paging(jo, user, request));
			return SUCCESS;
		} catch (Exception e) {
			e.printStackTrace();
		}     
        message.put("message", EnumUtil.getResponseValue(EnumUtil.ResponseCode.服务器处理异常.value));
		message.put("responseCode", EnumUtil.ResponseCode.服务器处理异常.value);
        return SUCCESS;
	}
}
