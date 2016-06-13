package com.cn.leedane.Dao.impl;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;

import com.cn.leedane.Dao.ReportDao;
import com.cn.leedane.bean.ReportBean;
/**
 * 举报Dao实现类
 * @author LeeDane
 * 2016年1月24日 下午8:09:37
 * Version 1.0
 */
public class ReportDaoImpl extends BaseDaoImpl<ReportBean> implements ReportDao<ReportBean>{
	Logger logger = Logger.getLogger(getClass());
	@Autowired
	private JdbcTemplate jdbcTemplate;
	
	@Override
	public boolean exists(String tableName, int tableId, int userId) {
		List<Map<String, Object>> list = jdbcTemplate.queryForList("select id from t_report where table_id = ? and table_name = ? and create_user_id = ? ", tableId, tableName, userId);
		return list != null && list.size() > 0;
	}


}
