package com.cn.leedane.struts2.action;
import javax.annotation.Resource;

import net.sf.json.JSONObject;

import com.cn.leedane.Utils.BaseActionContext;
import com.cn.leedane.Utils.EnumUtil;
import com.cn.leedane.Utils.HttpUtils;
import com.cn.leedane.bean.PrivateChatBean;
import com.cn.leedane.service.PrivateChatService;
/**
 * 私信信息action类
 * @author LeeDane
 * 2016年6月30日 上午11:27:43
 * Version 1.0
 */
public class PrivateChatAction extends BaseActionContext{
	private static final long serialVersionUID = 1L;
	
	private PrivateChatService<PrivateChatBean> privateChatService;
	
	@Resource
	public void setPrivateChatService(
			PrivateChatService<PrivateChatBean> privateChatService) {
		this.privateChatService = privateChatService;
	}
	
	/**
	 * 发送私信
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
			message.putAll(privateChatService.send(jo, user, request));
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
			message.putAll(privateChatService.getLimit(jo, user, request));
			return SUCCESS;
		} catch (Exception e) {
			e.printStackTrace();
		}     
        message.put("message", EnumUtil.getResponseValue(EnumUtil.ResponseCode.服务器处理异常.value));
		message.put("responseCode", EnumUtil.ResponseCode.服务器处理异常.value);
        return SUCCESS;
	}
}
