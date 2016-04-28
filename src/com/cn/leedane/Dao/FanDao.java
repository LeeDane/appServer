package com.cn.leedane.Dao;

import java.io.Serializable;

import com.cn.leedane.bean.FanBean;
/**
 * 粉丝dao接口类
 * @author LeeDane
 * 2016年4月11日 上午10:03:13
 * Version 1.0
 */
public interface FanDao <T extends Serializable> extends BaseDao<FanBean>{
	
	
	/**
	 * 根据用户id删除批量取消粉丝
	 * @param uid
	 * @param fanId
	 * @return
	 */
	public boolean cancel(int uid, int ... fanId);

	/**
	 * 判断两人是否是互粉关系
	 * @param id  当前用户的id
	 * @param to_user_id  对方用户的id
	 * @return
	 */
	public boolean isFanEachOther(int id, int to_user_id);
	
}
