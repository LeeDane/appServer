package com.cn.leedane.service;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import net.sf.json.JSONObject;

import com.cn.leedane.bean.SignInBean;
import com.cn.leedane.bean.UserBean;

/**
 * 签到service接口类
 * @author LeeDane
 * 2015年7月10日 下午6:18:22
 * Version 1.0
 */
public interface SignInService <T extends Serializable> extends BaseService<SignInBean>{
	
	/**
	 * 用户指定时间是否已经签到
	 * @param userId 用户ID
	 * @param dateTime 指定的日期
	 * @return
	 */
	public boolean isSign(int userId, String dateTime);
	
	/**
	 * 用户历史上是否有签到记录
	 * @param userId 用户ID
	 * @return
	 */
	public boolean hasHistorySign(int userId);

	
	/**
	 * 获取数据库中最新的记录
	 * @param userId
	 * @return
	 */
	public List<Map<String, Object>> getNewestRecore(int userId);

	/**
	 * 保存(签到),当天已经签到的直接返回false
	 * @param jo
	 * @param user
	 * @param request
	 * @return
	 */
	public boolean saveSignIn(JSONObject jo, UserBean user,
			HttpServletRequest request);


	/**
	 * 获取签到的分页记录
	 * @param jo 格式"{'uid':1, 'pageSize':5,'timeScope':1, 'start_date': '2015-12-31', 'end_date':'2016-01-18'}"
	 * 注意：timeScope大于0时，start_date和end_date将不起作用
	 * @param user
	 * @param request
	 * @return
	 */
	public List<Map<String, Object>> getSignInByLimit(JSONObject jo,
			UserBean user, HttpServletRequest request);
	
	
}
