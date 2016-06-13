package com.cn.leedane.struts2.action;
import net.sf.json.JSONObject;

import com.cn.leedane.Utils.BaseActionContext;
import com.cn.leedane.Utils.EnumUtil;
import com.cn.leedane.Utils.HttpUtils;
/**
 * 操作日志action类
 * @author LeeDane
 * 2016年5月5日 下午3:27:32
 * Version 1.0
 */
public class OperateLogAction extends BaseActionContext{
	private static final long serialVersionUID = 1L;
	
	/**
	 * 分页获取用户登录操作日志列表
	 * @return
	 */
	public String loginPaging() {
		try {
			message.put("isSuccess", false);
			JSONObject jo = HttpUtils.getJsonObjectFromInputStream(params, request);  
			if(jo == null || jo.isEmpty()) {	
				message.put("message", EnumUtil.getResponseValue(EnumUtil.ResponseCode.缺少请求参数.value));
				message.put("responseCode", EnumUtil.ResponseCode.缺少请求参数.value);
				return SUCCESS;
			}
			message.putAll(operateLogService.getUserLoginLimit(jo, user, request));
			return SUCCESS;
		} catch (Exception e) {
			e.printStackTrace();
		}     
        message.put("message", EnumUtil.getResponseValue(EnumUtil.ResponseCode.服务器处理异常.value));
		message.put("responseCode", EnumUtil.ResponseCode.服务器处理异常.value);
        return SUCCESS;
	}
}
