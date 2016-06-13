package com.cn.leedane.struts2.action;
import net.sf.json.JSONObject;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.cn.leedane.Utils.BaseActionContext;
import com.cn.leedane.Utils.EnumUtil;
import com.cn.leedane.Utils.HttpUtils;
import com.cn.leedane.bean.TimeLineBean;
import com.cn.leedane.service.CircleOfFriendService;
/**
 * 朋友圈action类
 * @author LeeDane
 * 2016年1月13日 上午9:43:34
 * Version 1.0
 */
public class CircleOfFriendAction extends BaseActionContext{	
	protected final Log log = LogFactory.getLog(getClass());
	private static final long serialVersionUID = 1L;
	
	//朋友圈service
	private CircleOfFriendService<TimeLineBean> circleOfFriendService;
	
	public void setCircleOfFriendService(
			CircleOfFriendService<TimeLineBean> circleOfFriendService) {
		this.circleOfFriendService = circleOfFriendService;
	}
	
	/**
	 * 获取朋友圈列表
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
			
			message.putAll(circleOfFriendService.getLimit(jo, user, request));
			return SUCCESS;
		} catch (Exception e) {
			e.printStackTrace();
		}
		message.put("message", EnumUtil.getResponseValue(EnumUtil.ResponseCode.服务器处理异常.value));
		message.put("responseCode", EnumUtil.ResponseCode.服务器处理异常.value);
        return SUCCESS;
	}
}
