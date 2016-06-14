package com.cn.leedane.Dao.impl;
import org.apache.log4j.Logger;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;

import com.cn.leedane.Dao.ChatBgUserDao;
import com.cn.leedane.bean.ChatBgUserBean;

/**
 * 聊天背景与用户关系dao实现类
 * @author LeeDane
 * 2016年6月13日 下午4:48:31
 * Version 1.0
 */
public class ChatBgUserDaoImpl extends BaseDaoImpl<ChatBgUserBean> implements ChatBgUserDao<ChatBgUserBean>{
	Logger logger = Logger.getLogger(getClass());
	@Autowired
	private JdbcTemplate jdbcTemplate;
	
	@Autowired  
	public void setSessionFactoryOverride(SessionFactory sessionFactory) {   
	     super.setSessionFactory(sessionFactory);   
	}
}
