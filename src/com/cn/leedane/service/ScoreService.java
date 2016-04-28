package com.cn.leedane.service;
import java.io.Serializable;

import com.cn.leedane.bean.ScoreBean;
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
	
}
