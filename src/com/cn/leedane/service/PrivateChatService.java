package com.cn.leedane.service;

import java.io.Serializable;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import net.sf.json.JSONObject;

import com.cn.leedane.bean.PrivateChatBean;
import com.cn.leedane.bean.UserBean;

/**
 * 私信相关service接口类
 * @author LeeDane
 * 2016年6月30日 上午11:21:36
 * Version 1.0
 */
public interface PrivateChatService <T extends Serializable> extends BaseService<PrivateChatBean>{

	/**
	 * 获取私信的分页列表
	 * @param jo
	 * @param user
	 * @param request
	 * @return
	 */
	public Map<String, Object> getLimit(JSONObject jo, UserBean user, HttpServletRequest request);
	
	/**
	 * 发送私信信息
	 * @param jo
	 * @param user
	 * @param request
	 * @return
	 */
	public Map<String, Object> send(JSONObject jo, UserBean user, HttpServletRequest request);
}
