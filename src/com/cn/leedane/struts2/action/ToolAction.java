package com.cn.leedane.struts2.action;
import net.sf.json.JSONObject;

import com.cn.leedane.Utils.BaseActionContext;
import com.cn.leedane.Utils.EnumUtil;
import com.cn.leedane.Utils.HttpUtils;
import com.cn.leedane.Utils.JsonUtil;
import com.cn.leedane.Utils.StringUtil;
import com.cn.leedane.wechat.util.HttpRequestUtil;

/**
 * 工具操作action类
 * @author LeeDane
 * 2016年6月8日 下午2:20:02
 * Version 1.0
 */
public class ToolAction extends BaseActionContext {
	private static final long serialVersionUID = 1L;
	
	/**
	 * 翻译
	 * @return
	 */
	public String fanyi() {
		message.put("isSuccess", resIsSuccess);
		try {
			JSONObject jo = HttpUtils.getJsonObjectFromInputStream(params,request);
			if(jo == null || jo.isEmpty()) {	
				message.put("message", EnumUtil.getResponseValue(EnumUtil.ResponseCode.缺少请求参数.value));
				message.put("responseCode", EnumUtil.ResponseCode.缺少请求参数.value);
				return SUCCESS;
			}
			String content = JsonUtil.getStringValue(jo, "content");
			String msg = HttpRequestUtil.sendAndRecieveFromYoudao(content);
			msg = StringUtil.getYoudaoFanyiContent(msg);
			message.put("isSuccess", true);
			message.put("message", msg);
			return SUCCESS;
		} catch (Exception e) {
			e.printStackTrace();
		}
		message.put("message", EnumUtil.getResponseValue(EnumUtil.ResponseCode.服务器处理异常.value));
		message.put("responseCode", EnumUtil.ResponseCode.服务器处理异常.value);
        return SUCCESS;
	}
}
