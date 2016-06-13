package com.cn.leedane.Dao;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

import com.cn.leedane.bean.SignInBean;

/**
 * 签到dao接口类
 * @author LeeDane
 * 2015年7月10日 下午6:15:21
 * Version 1.0
 */
public interface SignInDao <T extends Serializable> extends BaseDao<SignInBean>{
	
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
	 * 保存(签到),当天已经签到的直接返回false
	 * @param signInBean
	 * @return
	 */
	public boolean save(SignInBean signInBean, int userId);
	
	/**
	 * 获取数据库中最新的记录
	 * @param userId
	 * @return
	 */
	public List<Map<String, Object>> getNewestRecore(int userId);
	
	/**
	 * 获取昨天的签到记录
	 * @param uid
	 * @return
	 */
	public List<Map<String, Object>> getYesTodayRecore(int uid);
	
	/**
	 * 获取用户当前的积分
	 * @param uid
	 * @return
	 */
	public int getScore(int uid);
}
