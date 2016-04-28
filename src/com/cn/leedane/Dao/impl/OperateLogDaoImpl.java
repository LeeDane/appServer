package com.cn.leedane.Dao.impl;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;

import com.cn.leedane.Dao.OperateLogDao;
import com.cn.leedane.bean.OperateLogBean;
/**
 * 操作日志Dao实现类
 * @author LeeDane
 * 2015年5月29日 上午11:46:29
 * Version 1.0
 */
public class OperateLogDaoImpl extends BaseDaoImpl<OperateLogBean> implements OperateLogDao<OperateLogBean>{
	Logger logger = Logger.getLogger(getClass());
	@Autowired
	private JdbcTemplate jdbcTemplate;


}
