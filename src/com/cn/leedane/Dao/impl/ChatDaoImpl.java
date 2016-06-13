package com.cn.leedane.Dao.impl;
import org.apache.log4j.Logger;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;

import com.cn.leedane.Dao.ChatDao;
import com.cn.leedane.bean.ChatBean;

/**
 * 聊天dao实现类
 * @author LeeDane
 * 2015年9月15日 下午2:40:07
 * Version 1.0
 */
public class ChatDaoImpl extends BaseDaoImpl<ChatBean> implements ChatDao<ChatBean>{
	Logger logger = Logger.getLogger(getClass());
	@Autowired
	private JdbcTemplate jdbcTemplate;
	
	@Autowired  
	public void setSessionFactoryOverride(SessionFactory sessionFactory) {   
	     super.setSessionFactory(sessionFactory);   
	}
}
