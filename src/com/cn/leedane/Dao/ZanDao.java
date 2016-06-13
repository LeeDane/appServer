package com.cn.leedane.Dao;
import java.io.Serializable;

import com.cn.leedane.bean.ZanBean;

/**
 * 赞的Dao接口类
 * @author LeeDane
 * 2016年1月13日 下午2:26:22
 * Version 1.0
 */
public interface ZanDao<T extends Serializable> extends BaseDao<ZanBean>{
	/**
	 * 判断是否已经存在
	 * @param tableName
	 * @param tableId
	 * @param userId
	 * @return
	 */
	public boolean exists(String tableName, int tableId, int userId);
}
