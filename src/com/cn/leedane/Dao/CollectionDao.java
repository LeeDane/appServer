package com.cn.leedane.Dao;
import java.io.Serializable;

import com.cn.leedane.bean.CollectionBean;

/**
 * 收藏夹的Dao接口类
 * @author LeeDane
 * 2016年1月12日 下午6:19:24
 * Version 1.0
 */
public interface CollectionDao<T extends Serializable> extends BaseDao<CollectionBean>{
	/**
	 * 判断是否已经存在
	 * @param tableName
	 * @param tableId
	 * @param userId
	 * @return
	 */
	public boolean exists(String tableName, int tableId, int userId);
}
