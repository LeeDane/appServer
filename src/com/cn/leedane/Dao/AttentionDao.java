package com.cn.leedane.Dao;
import java.io.Serializable;

import com.cn.leedane.bean.AttentionBean;

/**
 * 关注的Dao接口类
 * @author LeeDane
 * 2016年1月13日 上午10:54:15
 * Version 1.0
 */
public interface AttentionDao<T extends Serializable> extends BaseDao<AttentionBean>{
	/**
	 * 判断是否已经存在
	 * @param tableName
	 * @param tableIsd
	 * @param userId
	 * @return
	 */
	public boolean exists(String tableName, int tableId, int userId);
}
