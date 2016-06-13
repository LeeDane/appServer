package com.cn.leedane.service;

import java.io.Serializable;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import net.sf.json.JSONObject;

import com.cn.leedane.bean.ChatBgBean;
import com.cn.leedane.bean.UserBean;

/**
 * 聊天背景相关service接口类
 * @author LeeDane
 * 2016年6月10日 下午7:14:30
 * Version 1.0
 */
public interface ChatBgService <T extends Serializable> extends BaseService<ChatBgBean>{
	
	
	/**
	 * 获取聊天背景的分页列表
	 * @param jo
	 * @param user
	 * @param request
	 * @return
	 */
	public Map<String, Object> paging(JSONObject jo, UserBean user, HttpServletRequest request);

	/**
	 * 发布聊天背景
	 * @param jo
	 * @param user
	 * @param request
	 * @return
	 */
	public Map<String, Object> publish(JSONObject jo, UserBean user, HttpServletRequest request);
}
