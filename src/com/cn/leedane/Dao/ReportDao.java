package com.cn.leedane.Dao;
import java.io.Serializable;

import com.cn.leedane.bean.ReportBean;

/**
 * 举报的Dao接口类
 * @author LeeDane
 * 2016年1月24日 下午8:08:21
 * Version 1.0
 */
public interface ReportDao<T extends Serializable> extends BaseDao<ReportBean>{
	/**
	 * 判断是否已经存在
	 * @param tableName
	 * @param tableIsd
	 * @param userId
	 * @return
	 */
	public boolean exists(String tableName, int tableId, int userId);
}
