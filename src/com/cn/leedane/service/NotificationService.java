package com.cn.leedane.service;
import java.io.Serializable;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import net.sf.json.JSONObject;

import com.cn.leedane.bean.NotificationBean;
import com.cn.leedane.bean.UserBean;
/**
 * 通知Service类
 * @author LeeDane
 * 2015年11月30日 下午3:30:46
 * Version 1.0
 */
public interface NotificationService<T extends Serializable> extends BaseService<NotificationBean>{
	/**
	 * 获取通知列表
	 * {'table_name':'t_mood','table_id':1, 'method':'firstloadings'
	 * 'pageSize':5,'last_id':0,'first_id':0}
	 * @param jo
	 * @param user
	 * @param request
	 * @return
	 */
	public Map<String, Object> getLimit(JSONObject jo, UserBean user,
			HttpServletRequest request);
	
	/**
	 * 发送广播
	 * {'broadcast':'大家好'}
	 * @param jo
	 * @param user
	 * @param request
	 * @return
	 */
	public Map<String, Object> sendBroadcast(JSONObject jo, UserBean user,
			HttpServletRequest request);
}
