package com.cn.leedane.struts2.action;
import javax.annotation.Resource;

import net.sf.json.JSONObject;

import com.cn.leedane.Utils.BaseActionContext;
import com.cn.leedane.Utils.EnumUtil;
import com.cn.leedane.Utils.HttpUtils;
import com.cn.leedane.bean.FanBean;
import com.cn.leedane.service.FanService;
/**
 * 粉丝action类
 * @author LeeDane
 * 2016年4月11日 下午3:35:11
 * Version 1.0
 */
public class FanAction extends BaseActionContext{	
	private static final long serialVersionUID = 1L;

	@Resource
	private FanService<FanBean> fanService;
	
	public void setFanService(FanService<FanBean> fanService) {
		this.fanService = fanService;
	}
	
	/**
	 * 取消关注
	 * @return
	 * @throws Exception
	 */
	public String cancel() throws Exception {
		message.put("isSuccess", resIsSuccess);
		try {
			//{"toUserId": 2}
			JSONObject jo = HttpUtils.getJsonObjectFromInputStream(params, request);  
			if(jo == null || jo.isEmpty()) {	
				message.put("message", EnumUtil.getResponseValue(EnumUtil.ResponseCode.缺少请求参数.value));
				message.put("responseCode", EnumUtil.ResponseCode.缺少请求参数.value);
				return SUCCESS;
			}
			message.put("isSuccess", fanService.cancel(jo, user, request));
			return SUCCESS;	
		} catch (Exception e) {
			e.printStackTrace();
		}
		message.put("message", EnumUtil.getResponseValue(EnumUtil.ResponseCode.服务器处理异常.value));
		message.put("responseCode", EnumUtil.ResponseCode.服务器处理异常.value);
        return SUCCESS;
	}
	/**
	 * 添加关注
	 * @return
	 */
	public String add() {
		message.put("isSuccess", resIsSuccess);
		try {
			//{"id":1, "to_user_id": 2}
			JSONObject jo = HttpUtils.getJsonObjectFromInputStream(params, request);  
			if(jo == null || jo.isEmpty()) {	
				message.put("message", EnumUtil.getResponseValue(EnumUtil.ResponseCode.缺少请求参数.value));
				message.put("responseCode", EnumUtil.ResponseCode.缺少请求参数.value);
				return SUCCESS;
			}
			message.putAll(fanService.addFan(jo, user, request));
			return SUCCESS;	
		} catch (Exception e) {
			e.printStackTrace();
		}
		message.put("message", EnumUtil.getResponseValue(EnumUtil.ResponseCode.服务器处理异常.value));
		message.put("responseCode", EnumUtil.ResponseCode.服务器处理异常.value);
        return SUCCESS;
	}
	
	/**
	 * 判读两人是否是好友
	 * @return
	 */
	public String isFan() {
		try {
			//{"id":1, "to_user_id": 2}
			message.put("isSuccess", resIsSuccess);
			JSONObject jo = HttpUtils.getJsonObjectFromInputStream(params, request);  
			if(jo == null || jo.isEmpty()) {	
				message.put("message", EnumUtil.getResponseValue(EnumUtil.ResponseCode.缺少请求参数.value));
				message.put("responseCode", EnumUtil.ResponseCode.缺少请求参数.value);
				return SUCCESS;
			}
			message.putAll(fanService.isFan(jo, user, request));
			return SUCCESS;
		} catch (Exception e) {
			e.printStackTrace();
		}    
		message.put("message", EnumUtil.getResponseValue(EnumUtil.ResponseCode.服务器处理异常.value));
		message.put("responseCode", EnumUtil.ResponseCode.服务器处理异常.value);
        return SUCCESS;
	}
	
	/**
	 * 获取我关注的对象列表
	 * @return
	 */
	public String myAttentionPaging() {
		message.put("isSuccess", resIsSuccess);
		try {
			//{"id":1, "to_user_id": 2}
			JSONObject jo = HttpUtils.getJsonObjectFromInputStream(params, request);  
			if(jo == null || jo.isEmpty()) {	
				message.put("message", EnumUtil.getResponseValue(EnumUtil.ResponseCode.缺少请求参数.value));
				message.put("responseCode", EnumUtil.ResponseCode.缺少请求参数.value);
				return SUCCESS;
			}
			message.putAll(fanService.getMyAttentionsLimit(jo, user, request));
			return SUCCESS;	
		} catch (Exception e) {
			e.printStackTrace();
		}
		message.put("message", EnumUtil.getResponseValue(EnumUtil.ResponseCode.服务器处理异常.value));
		message.put("responseCode", EnumUtil.ResponseCode.服务器处理异常.value);
        return SUCCESS;
	}
	
	/**
	 * 获取Ta关注的对象列表
	 * @return
	 */
	public String toAttentionPaging() {
		message.put("isSuccess", resIsSuccess);
		try {
			//{"id":1, "to_user_id": 2}
			JSONObject jo = HttpUtils.getJsonObjectFromInputStream(params, request);  
			if(jo == null || jo.isEmpty()) {	
				message.put("message", EnumUtil.getResponseValue(EnumUtil.ResponseCode.缺少请求参数.value));
				message.put("responseCode", EnumUtil.ResponseCode.缺少请求参数.value);
				return SUCCESS;
			}
			message.putAll(fanService.getToAttentionsLimit(jo, user, request));
			return SUCCESS;	
		} catch (Exception e) {
			e.printStackTrace();
		}
		message.put("message", EnumUtil.getResponseValue(EnumUtil.ResponseCode.服务器处理异常.value));
		message.put("responseCode", EnumUtil.ResponseCode.服务器处理异常.value);
        return SUCCESS;
	}
	
	/**
	 * 获取我的粉丝列表
	 * @return
	 */
	public String myFansPaging() {
		message.put("isSuccess", resIsSuccess);
		try {
			//{"id":1, "to_user_id": 2}
			JSONObject jo = HttpUtils.getJsonObjectFromInputStream(params, request);  
			if(jo == null || jo.isEmpty()) {	
				message.put("message", EnumUtil.getResponseValue(EnumUtil.ResponseCode.缺少请求参数.value));
				message.put("responseCode", EnumUtil.ResponseCode.缺少请求参数.value);
				return SUCCESS;
			}
			message.putAll(fanService.getMyFansLimit(jo, user, request));
			return SUCCESS;	
		} catch (Exception e) {
			e.printStackTrace();
		}
		message.put("message", EnumUtil.getResponseValue(EnumUtil.ResponseCode.服务器处理异常.value));
		message.put("responseCode", EnumUtil.ResponseCode.服务器处理异常.value);
        return SUCCESS;
	}
	
	/**
	 * 获取她的粉丝列表(关注信息将相对我来展示)
	 * @return
	 */
	public String toFansPaging() {
		message.put("isSuccess", resIsSuccess);
		try {
			//{"id":1, "to_user_id": 2}
			JSONObject jo = HttpUtils.getJsonObjectFromInputStream(params, request);  
			if(jo == null || jo.isEmpty()) {	
				message.put("message", EnumUtil.getResponseValue(EnumUtil.ResponseCode.缺少请求参数.value));
				message.put("responseCode", EnumUtil.ResponseCode.缺少请求参数.value);
				return SUCCESS;
			}
			message.putAll(fanService.getToFansLimit(jo, user, request));
			return SUCCESS;	
		} catch (Exception e) {
			e.printStackTrace();
		}
		message.put("message", EnumUtil.getResponseValue(EnumUtil.ResponseCode.服务器处理异常.value));
		message.put("responseCode", EnumUtil.ResponseCode.服务器处理异常.value);
        return SUCCESS;
	}
}
