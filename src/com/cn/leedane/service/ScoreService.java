package com.cn.leedane.service;
import java.io.Serializable;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import net.sf.json.JSONObject;

import com.cn.leedane.bean.ScoreBean;
import com.cn.leedane.bean.UserBean;
/**
 * 积分的Service类
 * @author LeeDane
 * 2016年3月4日 下午5:30:41
 * Version 1.0
 */
public interface ScoreService<T extends Serializable> extends BaseService<ScoreBean>{
	/**
	 * 获取当前用户的总积分
	 * @param userId
	 * @return
	 */
	public int getTotalScore(int userId);

	/**
	 * 分页获得积分历史列表
	 * @param jo
	 * @param user
	 * @param request
	 * @return
	 */
	public Map<String, Object> getLimit(JSONObject jo, UserBean user, HttpServletRequest request);
	
}
