package com.cn.leedane.service;

import java.io.Serializable;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import net.sf.json.JSONObject;

import com.cn.leedane.bean.FanBean;
import com.cn.leedane.bean.UserBean;

/**
 * 粉丝service接口类
 * @author LeeDane
 * 2016年4月11日 上午10:27:22
 * Version 1.0
 */
public interface FanService <T extends Serializable> extends BaseService<FanBean>{
	
	/**
	 * 关注成为其粉丝
	 * @param jo
	 * @param user
	 * @param request
	 * @return
	 */
	public Map<String, Object> addFan(JSONObject jo, UserBean user, HttpServletRequest request);
	
	/**
	 * 获取我的全部的关注的用户的ID和备注
	 * 注意：为了业务分开，这里只能调用的是登录用户的关注的用户列表，非登录用户的关注列表请调用getToAttentionsLimit()
	 * @param jo
	 * @param user
	 * @param request
	 * @return
	 */
	public Map<String, Object> getMyAttentionsLimit(JSONObject jo, UserBean user, HttpServletRequest request);
	
	
	/**
	 * 获取Ta关注的用户的ID和备注
	 * 注意：为了业务分开，这里只能调用的是非登录用户的关注的用户列表，登录用户的关注列表请调用getToAttentionsLimit()
	 * @param jo
	 * @param user
	 * @param request
	 * @return
	 */
	public Map<String, Object> getToAttentionsLimit(JSONObject jo, UserBean user, HttpServletRequest request);
	
	/**
	 * 获取全部的关注我的ID和备注
	 * 注意：为了业务分开，这里只能调用的是登录用户的粉丝列表，非登录用户的粉丝列表请调用getToFansLimit()
	 * @param jo
	 * @param user
	 * @param request
	 * @return
	 */
	public Map<String, Object> getMyFansLimit(JSONObject jo, UserBean user, HttpServletRequest request);
	
	/**
	 * 获取全部的关注Ta的ID(是否关注的展示相对我)
	 * 注意：为了业务分开，这里只能调用的是非登录用户的粉丝列表，登录用户的粉丝列表请调用getMyFansLimit()
	 * @param jo
	 * @param user
	 * @param request
	 * @return
	 */
	public Map<String, Object> getToFansLimit(JSONObject jo, UserBean user, HttpServletRequest request);
	
	/**
	 * 取消关注粉丝
	 * @param uid
	 * @param jo
	 * @param user
	 * @param request
	 * @return
	 */
	public boolean cancel(JSONObject jo, UserBean user, HttpServletRequest request);
	
	/**
	 * 判断两人是否互粉
	 * @param id  当前用户的id
	 * @param to_user_id  对方用户的id
	 * @return
	 */
	public boolean isFanEachOther(JSONObject jo, UserBean user, HttpServletRequest request);
	/**
	 * 是否粉她
	 * @param id  当前用户的id
	 * @param to_user_id  对方用户的id
	 * @return
	 */
	public Map<String, Object> isFan(JSONObject jo, UserBean user, HttpServletRequest request);
}
