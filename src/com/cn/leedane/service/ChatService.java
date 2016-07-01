package com.cn.leedane.service;

import java.io.Serializable;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import net.sf.json.JSONObject;

import com.cn.leedane.bean.ChatBean;
import com.cn.leedane.bean.UserBean;

/**
 * 聊天相关service接口类
 * @author LeeDane
 * 2016年5月5日 下午11:58:04
 * Version 1.0
 */
public interface ChatService <T extends Serializable> extends BaseService<ChatBean>{
	
	
	/**
	 * 获取聊天信息的分页列表
	 * @param jo
	 * @param user
	 * @param request
	 * @return
	 */
	public Map<String, Object> getLimit(JSONObject jo, UserBean user, HttpServletRequest request);

	/**
	 * 发送聊天信息
	 * @param jo
	 * @param user
	 * @param request
	 * @return
	 */
	public Map<String, Object> send(JSONObject jo, UserBean user, HttpServletRequest request);

	/**
	 * 更新聊天信息为已读状态
	 * @param jo
	 * @param user
	 * @param request
	 * @return
	 */
	public Map<String, Object> updateRead(JSONObject jo, UserBean user, HttpServletRequest request);

	/**
	 * 获取用户全部未读的信息
	 * @param jo
	 * @param user
	 * @param request
	 * @return
	 */
	public Map<String, Object> noReadList(JSONObject jo, UserBean user, HttpServletRequest request);
}
