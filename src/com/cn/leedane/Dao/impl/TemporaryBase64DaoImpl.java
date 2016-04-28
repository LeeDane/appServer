package com.cn.leedane.Dao.impl;
import java.sql.Types;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.object.BatchSqlUpdate;

import com.cn.leedane.Dao.TemporaryBase64Dao;
import com.cn.leedane.bean.TemporaryBase64Bean;
/**
 * base64位临时上传文件Dao实现类
 * @author LeeDane
 * 2015年12月1日 上午10:55:13
 * Version 1.0
 */
public class TemporaryBase64DaoImpl extends BaseDaoImpl<TemporaryBase64Bean> implements TemporaryBase64Dao<TemporaryBase64Bean>{
	Logger logger = Logger.getLogger(getClass());
	@Autowired
	private JdbcTemplate jdbcTemplate;


	@Override
	public boolean deleteBatchSql(String table, String[] uuids) throws DataAccessException{
		logger.info("TemporaryBase64DaoImpl-->deleteBatchSql():table="+table+",uuids="+uuids);
		BatchSqlUpdate batchSqlUpdate = new BatchSqlUpdate(jdbcTemplate.getDataSource(), "delete table "+ table+ " where uuid=? ");
		batchSqlUpdate.setBatchSize(uuids.length);
		batchSqlUpdate.setTypes(new int[]{Types.NVARCHAR});

		for(int i = 0; i < uuids.length; ++i){
			batchSqlUpdate.update(new Object[]{uuids[i]}) ;
		}
		batchSqlUpdate.flush();
		return true;
		
	}


	@Override
	public boolean deleteByUuid(String uuid, String tableName) throws DataAccessException {	
		logger.info("TemporaryBase64DaoImpl-->deleteByUuid():uuid="+uuid+",tableName="+tableName);
		return jdbcTemplate.update("delete table T_TEMP_BASE64 where uuid = ? and table_name=? ", uuid, tableName) > 0;
	}


	@Override
	public boolean deleteByUuidAndOrder(String uuid, int order, String tableName) throws DataAccessException {
		logger.info("TemporaryBase64DaoImpl-->deleteByUuidAndOrder():uuid="+uuid+",tableName="+tableName+",order="+order);
		return jdbcTemplate.update("delete table T_TEMP_BASE64 where uuid = ? and order = ? and table_name=? ", uuid, order, tableName) > 0;
	}
}
