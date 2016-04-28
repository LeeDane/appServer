package com.cn.leedane.Dao;
import java.io.Serializable;

import com.cn.leedane.bean.ScoreBean;

/**
 * 积分的Dao接口类
 * @author LeeDane
 * 2016年3月4日 下午5:28:37
 * Version 1.0
 */
public interface ScoreDao<T extends Serializable> extends BaseDao<ScoreBean>{
	/**
	 * 获取当前用户的总积分
	 * @param userId
	 * @return
	 */
	public int getTotalScore(int userId);
}
