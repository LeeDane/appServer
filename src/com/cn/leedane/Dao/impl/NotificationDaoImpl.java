package com.cn.leedane.Dao.impl;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;

import com.cn.leedane.Dao.NotificationDao;
import com.cn.leedane.bean.NotificationBean;
/**
 * 通知Dao实现类
 * @author LeeDane
 * 2015年11月30日 下午3:29:42
 * Version 1.0
 */
public class NotificationDaoImpl extends BaseDaoImpl<NotificationBean> implements NotificationDao<NotificationBean>{
	Logger logger = Logger.getLogger(getClass());
	@Autowired
	private JdbcTemplate jdbcTemplate;


}
