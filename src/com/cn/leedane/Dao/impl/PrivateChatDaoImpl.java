package com.cn.leedane.Dao.impl;
import org.apache.log4j.Logger;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;

import com.cn.leedane.Dao.PrivateChatDao;
import com.cn.leedane.bean.PrivateChatBean;

/**
 * 私信dao实现类
 * @author LeeDane
 * 2016年6月30日 上午11:20:36
 * Version 1.0
 */
public class PrivateChatDaoImpl extends BaseDaoImpl<PrivateChatBean> implements PrivateChatDao<PrivateChatBean>{
	Logger logger = Logger.getLogger(getClass());
	@Autowired
	private JdbcTemplate jdbcTemplate;
	
	@Autowired  
	public void setSessionFactoryOverride(SessionFactory sessionFactory) {   
	     super.setSessionFactory(sessionFactory);   
	}
}
