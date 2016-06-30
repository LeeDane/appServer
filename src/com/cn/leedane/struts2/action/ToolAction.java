package com.cn.leedane.struts2.action;
import java.util.HashSet;
import java.util.Set;

import javax.annotation.Resource;

import net.sf.json.JSONObject;

import com.cn.leedane.Utils.BaseActionContext;
import com.cn.leedane.Utils.ConstantsUtil;
import com.cn.leedane.Utils.EmailUtil;
import com.cn.leedane.Utils.EnumUtil;
import com.cn.leedane.Utils.HttpUtils;
import com.cn.leedane.Utils.JsonUtil;
import com.cn.leedane.Utils.StringUtil;
import com.cn.leedane.bean.UserBean;
import com.cn.leedane.service.UserService;
import com.cn.leedane.wechat.util.HttpRequestUtil;

/**
 * 工具操作action类
 * @author LeeDane
 * 2016年6月8日 下午2:20:02
 * Version 1.0
 */
public class ToolAction extends BaseActionContext {
	private static final long serialVersionUID = 1L;
	
	@Resource
	private UserService<UserBean> userService;
	
	public void setUserService(UserService<UserBean> userService) {
		this.userService = userService;
	}
	
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
	
	/**
	 * 发送邮件通知的接口
	 * @return
	 */
	public String sendEmail() {
		try {
			message.put("isSuccess", false);
			//{"id":1, "to_user_id": 2}
			JSONObject jo = HttpUtils.getJsonObjectFromInputStream(params, request);  
			if(jo == null || jo.isEmpty()) {	
				message.put("message", EnumUtil.getResponseValue(EnumUtil.ResponseCode.缺少请求参数.value));
				message.put("responseCode", EnumUtil.ResponseCode.缺少请求参数.value);
				return SUCCESS;
			}
			
			String toUserId = JsonUtil.getStringValue(jo, "to_user_id");//接收邮件的用户的Id，必须
			String content = JsonUtil.getStringValue(jo, "content"); //邮件的内容，必须
			String object = JsonUtil.getStringValue(jo, "object"); //邮件的标题，必须
			if(StringUtil.isNull(toUserId) || StringUtil.isNull(content) || StringUtil.isNull(object)){
				message.put("message", EnumUtil.getResponseValue(EnumUtil.ResponseCode.参数存在或为空.value));
				message.put("responseCode", EnumUtil.ResponseCode.参数存在或为空.value);
				return SUCCESS;
			}
			
			UserBean toUser = userService.findById(StringUtil.changeObjectToInt(toUserId));
			if(toUser == null){
				message.put("message", EnumUtil.getResponseValue(EnumUtil.ResponseCode.该用户不存在.value));
				message.put("responseCode", EnumUtil.ResponseCode.该用户不存在.value);
				return SUCCESS;
			}
			if(StringUtil.isNull(toUser.getEmail())){
				message.put("message", EnumUtil.getResponseValue(EnumUtil.ResponseCode.对方还没有绑定电子邮箱.value));
				message.put("responseCode", EnumUtil.ResponseCode.对方还没有绑定电子邮箱.value);
				return SUCCESS;
			}
			
			//String content = "用户："+user.getAccount() +"已经添加您为好友，请您尽快处理，谢谢！";
			//String object = "LeeDane好友添加请求确认";
			Set<UserBean> set = new HashSet<UserBean>();		
			set.add(toUser);	
			
			EmailUtil emailUtil = EmailUtil.getInstance(null, set, content, object);
			try {
				emailUtil.sendMore();
				message.put("message", EnumUtil.getResponseValue(EnumUtil.ResponseCode.邮件发送成功.value));
			} catch (Exception e) {
				e.printStackTrace();
				message.put("message", EnumUtil.getResponseValue(EnumUtil.ResponseCode.邮件发送失败.value)+",失败原因是："+e.toString());
				message.put("responseCode", EnumUtil.ResponseCode.邮件发送失败.value);
			}
			
			return SUCCESS;
		} catch (Exception e) {
			e.printStackTrace();
		}     
        message.put("message", EnumUtil.getResponseValue(EnumUtil.ResponseCode.服务器处理异常.value));
		message.put("responseCode", EnumUtil.ResponseCode.服务器处理异常.value);
        return SUCCESS;
	}
}
