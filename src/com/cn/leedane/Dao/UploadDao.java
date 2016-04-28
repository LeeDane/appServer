package com.cn.leedane.Dao;
import java.io.Serializable;

import com.cn.leedane.bean.UploadBean;

/**
 * 断点上传Dao接口类
 * @author LeeDane
 * 2016年1月19日 下午2:58:42
 * Version 1.0
 */
public interface UploadDao<T extends Serializable> extends BaseDao<UploadBean>{
	/**
	 * 判断是否已经存在
	 * @param tableName
	 * @param tableUuid
	 * @param order
	 * @param serialNumber
	 * @param userId
	 * @return
	 */
	public boolean exists(String tableName, String tableUuid, int order, int serialNumber, int userId);
}
