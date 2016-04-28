package com.cn.leedane.service;
import java.io.Serializable;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import net.sf.json.JSONObject;

import com.cn.leedane.bean.UserBean;
/**
 * 朋友圈的Service类
 * @author LeeDane
 * 2016年4月15日 下午4:32:40
 * Version 1.0
 */
public interface CircleOfFriendService<T extends Serializable>{

	/**
	 * 获取朋友圈列表
	 * {'uid':1,'table_name':'t_mood','table_id':123, 'method':'firstloadings'
	 * 'pageSize':5,'last_id':0,'first_id':0}
	 * @param jo
	 * @param user
	 * @param request
	 * @return
	 */
	public Map<String, Object> getLimit(JSONObject jo, UserBean user, HttpServletRequest request);
}
