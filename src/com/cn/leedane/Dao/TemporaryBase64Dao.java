package com.cn.leedane.Dao;
import java.io.Serializable;

import org.springframework.dao.DataAccessException;

import com.cn.leedane.bean.TemporaryBase64Bean;

/**
 * base64临时上传的文件Dao接口类
 * @author LeeDane
 * 2015年12月1日 上午10:54:03
 * Version 1.0
 */
public interface TemporaryBase64Dao<T extends Serializable> extends BaseDao<TemporaryBase64Bean>{

	/**
	 * 批量删除(根据uuid)
	 * @param table(数据库中的表名称)
	 * @param uuids
	 * @return
	 */
	public boolean deleteBatchSql(String table, String[] uuids) throws DataAccessException;
	
	/**
	 * 通过uuid删除记录（删除某条心情下的全部上传记录）
	 * @param uuid
	 * @param tableName（最终所属的表名）
	 * @return
	 */
	public boolean deleteByUuid(String uuid, String tableName) throws DataAccessException;
	
	/**
	 * 通过uuid和order删除记录（删除某条心情下的指定order的记录）
	 * @param uuid
	 * @param order
	 * @param tableName（最终所属的表名）
	 * @return
	 * @throws DataAccessException
	 */
	public boolean deleteByUuidAndOrder(String uuid, int order, String tableName) throws DataAccessException;
}
