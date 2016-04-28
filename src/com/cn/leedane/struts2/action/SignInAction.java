package com.cn.leedane.struts2.action;
import java.util.Date;
import java.util.List;
import java.util.Map;

import net.sf.json.JSONObject;

import org.springframework.beans.factory.annotation.Autowired;

import com.cn.leedane.Utils.BaseActionContext;
import com.cn.leedane.Utils.DateUtil;
import com.cn.leedane.Utils.EnumUtil;
import com.cn.leedane.Utils.HttpUtils;
import com.cn.leedane.Utils.JsonUtil;
import com.cn.leedane.bean.SignInBean;
import com.cn.leedane.bean.UserBean;
import com.cn.leedane.service.SignInService;
import com.cn.leedane.service.UserService;

/**
 * 签到的action类
 * @author LeeDane
 * 2015年7月10日 下午6:06:21
 * Version 1.0
 */
public class SignInAction extends BaseActionContext {
	private static final long serialVersionUID = 1L;
	@Autowired
	private SignInService<SignInBean> signInService;
	@Autowired
	private UserService<UserBean> userService;
	/**
	 * 执行签到的主方法
	 * @return
	 * @throws Exception
	 */
	public String signIn() throws Exception {
		//保存签到记录
		//更新积分
		//更新操作日志
		
		try {
			JSONObject jo = HttpUtils.getJsonObjectFromInputStream(params, request);  
			if(jo == null || jo.isEmpty()) {	
				message.put("message", EnumUtil.getResponseValue(EnumUtil.ResponseCode.缺少请求参数.value));
				message.put("responseCode", EnumUtil.ResponseCode.缺少请求参数.value);
				return SUCCESS;
			}
			int id = JsonUtil.getIntValue(jo, "id", user.getId());
			boolean isSave = signInService.saveSignIn(jo, userService.findById(id), request);
			message.put("isSuccess", isSave);
			return SUCCESS;
		} catch (Exception e) {
			e.printStackTrace();
		}
        message.put("isSuccess", false);  
		return SUCCESS;
	}
	
	/**
	 * 判断当天是否已经登录
	 * @return
	 * @throws Exception
	 */
	public String currentDateIsSignIn() throws Exception {
		try {
			JSONObject jo = HttpUtils.getJsonObjectFromInputStream(params, request);  
			if(jo == null || jo.isEmpty()) {	
				message.put("message", EnumUtil.getResponseValue(EnumUtil.ResponseCode.缺少请求参数.value));
				message.put("responseCode", EnumUtil.ResponseCode.缺少请求参数.value);
				return SUCCESS;
			}
			if(jo.has("id")) {
				int id = JsonUtil.getIntValue(jo, "id", user.getId());
				String dateTime = DateUtil.DateToString(new Date(), "yyyy-MM-dd");		
				message.put("isSuccess", signInService.isSign(id, dateTime));
				
				// 保存操作日志信息
				String subject = user.getAccount()+"判断当天是否签到";
				this.operateLogService.saveOperateLog(user, request, new Date(), subject, "currentDateIsSignIn", 1, 0);
				return SUCCESS;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
        message.put("isSuccess", false);  
		return SUCCESS;
	}
	
	/**
	 * 获取签到历史记录
	 * @return
	 */
	public String getPaging(){
		message.put("isSuccess", resIsSuccess);
		try {
			JSONObject jo = HttpUtils.getJsonObjectFromInputStream(params,request);
			if(jo == null || jo.isEmpty()) {	
				message.put("message", EnumUtil.getResponseValue(EnumUtil.ResponseCode.缺少请求参数.value));
				message.put("responseCode", EnumUtil.ResponseCode.缺少请求参数.value);
				return SUCCESS;
			}
			List<Map<String, Object>> result= signInService.getSignInByLimit(jo, user, request);
			System.out.println("获得签到的数量：" +result.size());
			message.put("isSuccess", true);
			message.put("message", result);
			return SUCCESS;
		} catch (Exception e) {
			e.printStackTrace();
		}
        return SUCCESS;
	}
}
