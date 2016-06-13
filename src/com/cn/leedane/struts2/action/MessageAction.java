package com.cn.leedane.struts2.action;

import java.util.Date;

import net.sf.json.JSONObject;

import com.cn.leedane.Utils.BaseActionContext;
import com.cn.leedane.Utils.ConstantsUtil;
import com.cn.leedane.Utils.DateUtil;
import com.cn.leedane.Utils.EnumUtil;
import com.cn.leedane.Utils.HttpUtils;
import com.cn.leedane.Utils.StringUtil;
import com.cn.leedane.bean.UserBean;
import com.cn.leedane.rabbitmq.RecieveMessage;
import com.cn.leedane.rabbitmq.SendAndRecieveObject;
import com.cn.leedane.rabbitmq.SendMessage;

/**
 * 消息的action类
 * @author LeeDane
 * 2015年8月12日 下午3:52:06
 * Version 1.0
 */
public class MessageAction extends BaseActionContext {
	private static final long serialVersionUID = 1L;
	
	
	/**
	 * 执行发送消息的方法
	 * @return
	 * @throws Exception
	 */
	public String send() throws Exception {
		JSONObject jo = HttpUtils.getJsonObjectFromInputStream(params, request);
		if(jo == null || jo.isEmpty()) {	
			message.put("message", EnumUtil.getResponseValue(EnumUtil.ResponseCode.缺少请求参数.value));
			message.put("responseCode", EnumUtil.ResponseCode.缺少请求参数.value);
			return SUCCESS;
		}
		UserBean user = (UserBean) getSession().get(ConstantsUtil.USER_SESSION);
		int fromUserID = 1;
		if(user != null){
			fromUserID = user.getId();
		}
		 
		int toUserID = jo.getInt("uid");
		String msg = jo.getString("content");
		SendAndRecieveObject object = new SendAndRecieveObject();
		object.setToUserID(String.valueOf(toUserID));
		object.setFromUserID(String.valueOf(fromUserID));
		object.setCreateTime(new Date());
		object.setMsg(msg);
		SendMessage sender = new SendMessage(object);
		sender.sendMsg();
		message.put("isSuccess", true);
		message.put("message", "发送消息成功");
		return SUCCESS;
	}
	
	/**
	 * 执行发送消息的方法
	 * @return
	 * @throws Exception
	 */
	public String receive() throws Exception {
		UserBean user = (UserBean) session.get(ConstantsUtil.USER_SESSION);
		if(user != null){
			RecieveMessage recieveMessage = new RecieveMessage(String.valueOf(user.getId()));
			String getMsg = recieveMessage.getMsg();
			if(!StringUtil.isNull(getMsg)){
				String array[] = getMsg.split("@@");
				if(array.length == 3){
					SendAndRecieveObject object = new SendAndRecieveObject();
					object.setFromUserID(array[0]);
					object.setMsg(array[1]);
					object.setCreateTime(DateUtil.stringToDate(array[2]));
					message.put("isSuccess", true);
					message.put("message", object);
					return SUCCESS;
				}
			}
			message.put("isSuccess", false);
			message.put("message", "获取信息出错");
		}else{
			message.put("isSuccess", false);
			message.put("message", "请先登录");
		}
		return SUCCESS;
	}
}
