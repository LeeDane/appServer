package com.cn.leedane.Dao.impl;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;

import com.cn.leedane.Dao.UploadDao;
import com.cn.leedane.bean.UploadBean;
/**
 * 断点上传Dao实现类
 * @author LeeDane
 * 2016年1月19日 下午3:00:48
 * Version 1.0
 */
public class UploadDaoImpl extends BaseDaoImpl<UploadBean> implements UploadDao<UploadBean>{
	Logger logger = Logger.getLogger(getClass());
	@Autowired
	private JdbcTemplate jdbcTemplate;
	
	@Override
	public boolean exists(String tableName, String tableUuid, int order,
			int serialNumber, int userId) {
		List<Map<String, Object>> list = jdbcTemplate.queryForList("select id from t_upload where table_uuid = ? and table_name = ? and create_user_id = ? and f_order = ? and serial_number = ?", tableUuid, tableName, userId, order, serialNumber);
		return list != null && list.size() > 0;
	}
}
