package com.cn.leedane.wechat.service.impl;

import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import net.sf.json.JSONObject;

import com.cn.leedane.Utils.SpringUtils;
import com.cn.leedane.Utils.StringUtil;
import com.cn.leedane.bean.MoodBean;
import com.cn.leedane.bean.UserBean;
import com.cn.leedane.service.MoodService;
import com.cn.leedane.service.UserService;
import com.cn.leedane.wechat.service.BaseXMLWechatService;

/**
 * 发送心情的xml处理
 * @author LeeDane
 * 2016年4月7日 上午11:15:13
 * Version 1.0
 */
public class SendMoodXMLService extends BaseXMLWechatService {
	
	@Resource
	private MoodService<MoodBean> moodService;
	
	public void setMoodService(MoodService<MoodBean> moodService) {
		this.moodService = moodService;
	}
	
	@Resource
	private UserService<UserBean> userService;
	
	public void setUserService(UserService<UserBean> userService) {
		this.userService = userService;
	}
	
	public SendMoodXMLService(HttpServletRequest request, Map<String, String> map) {
		super(request, map);
	}
	
	@SuppressWarnings("unchecked")
	@Override
	protected String execute() {
		if(userService == null){
			userService = (UserService<UserBean>) SpringUtils.getBean("userService");
		}
		
		if(moodService == null){
			moodService = (MoodService<MoodBean>) SpringUtils.getBean("moodService");
		}
		UserBean user = userService.loginByWeChat(FromUserName);
		if(user != null){
			
			String str = "{'content':'"+Content+"','froms':'微信leedane公众号'}";
			JSONObject jsonObject = JSONObject.fromObject(str);
			Map<String, Object> result = moodService.sendWord(jsonObject, user, null);
			if(result.containsKey("isSuccess") && StringUtil.changeObjectToBoolean(result.get("isSuccess"))){
				return "发布心情成功";
			}else{
				return "发布心情失败";
			}
		}
		return "请先绑定leedane账号";
	}
}
