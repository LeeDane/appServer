package com.cn.leedane.struts2.action;
import java.util.List;
import java.util.Map;

import net.sf.json.JSONObject;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.cn.leedane.Utils.BaseActionContext;
import com.cn.leedane.Utils.EnumUtil;
import com.cn.leedane.Utils.HttpUtils;
import com.cn.leedane.Utils.JsonUtil;
import com.cn.leedane.bean.AttentionBean;
import com.cn.leedane.service.AttentionService;
/**
 * 关注action类
 * @author LeeDane
 * 2016年1月13日 上午11:12:34
 * Version 1.0
 */
public class AttentionAction extends BaseActionContext{	
	protected final Log log = LogFactory.getLog(getClass());
	private static final long serialVersionUID = 1L;
	
	//关注service
	private AttentionService<AttentionBean> attentionService;
	
	public void setAttentionService(
			AttentionService<AttentionBean> attentionService) {
		this.attentionService = attentionService;
	}

	/**
	 * 添加关注
	 * @return
	 */
	public String add(){
		message.put("isSuccess", resIsSuccess);
		try {
			JSONObject jo = HttpUtils.getJsonObjectFromInputStream(params,request);
			if(jo == null || jo.isEmpty()) {	
				message.put("message", EnumUtil.getResponseValue(EnumUtil.ResponseCode.缺少请求参数.value));
				message.put("responseCode", EnumUtil.ResponseCode.缺少请求参数.value);
				return SUCCESS;
			}
			resIsSuccess = attentionService.addAttention(jo, user, request);
		} catch (Exception e) {
			e.printStackTrace();
		}
		message.put("isSuccess", resIsSuccess);
        return SUCCESS;
	}
	
	/**
	 * 取消关注
	 * @return
	 */
	public String delete(){
		message.put("isSuccess", resIsSuccess);
		try {
			JSONObject jo = HttpUtils.getJsonObjectFromInputStream(params,request);
			if(jo == null || jo.isEmpty()) {	
				message.put("message", EnumUtil.getResponseValue(EnumUtil.ResponseCode.缺少请求参数.value));
				message.put("responseCode", EnumUtil.ResponseCode.缺少请求参数.value);
				return SUCCESS;
			}
			message.putAll(attentionService.deleteAttention(jo, user, request));
			return SUCCESS;
		} catch (Exception e) {
			e.printStackTrace();
		}
		message.put("message", EnumUtil.getResponseValue(EnumUtil.ResponseCode.服务器处理异常.value));
		message.put("responseCode", EnumUtil.ResponseCode.服务器处理异常.value);
        return SUCCESS;
	}
	
	/**
	 * 获取关注列表
	 * @return
	 */
	public String paging(){
		message.put("isSuccess", resIsSuccess);
		try {
			JSONObject jo = HttpUtils.getJsonObjectFromInputStream(params,request);
			if(jo == null || jo.isEmpty()) {	
				message.put("message", EnumUtil.getResponseValue(EnumUtil.ResponseCode.缺少请求参数.value));
				message.put("responseCode", EnumUtil.ResponseCode.缺少请求参数.value);
				return SUCCESS;
			}
			//为了安全，必须是登录用户才能操作
			int toUserId = JsonUtil.getIntValue(jo, "toUserId");
			if(toUserId != user.getId()){
				message.put("message", EnumUtil.getResponseValue(EnumUtil.ResponseCode.没有操作权限.value));
				message.put("responseCode", EnumUtil.ResponseCode.没有操作权限.value);
				return SUCCESS;
			}
			List<Map<String, Object>> result= attentionService.getLimit(jo, user, request);
			System.out.println("获得关注的数量：" +result.size());
			message.put("isSuccess", true);
			message.put("message", result);
			return SUCCESS;
		} catch (Exception e) {
			e.printStackTrace();
		}
        return SUCCESS;
	}
}
