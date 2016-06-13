package com.cn.leedane.Dao.impl;
import org.apache.log4j.Logger;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;

import com.cn.leedane.Dao.ChatBgDao;
import com.cn.leedane.bean.ChatBgBean;

/**
 * 聊天背景dao实现类
 * @author LeeDane
 * 2016年6月10日 下午7:13:13
 * Version 1.0
 */
public class ChatBgDaoImpl extends BaseDaoImpl<ChatBgBean> implements ChatBgDao<ChatBgBean>{
	Logger logger = Logger.getLogger(getClass());
	@Autowired
	private JdbcTemplate jdbcTemplate;
	
	@Autowired  
	public void setSessionFactoryOverride(SessionFactory sessionFactory) {   
	     super.setSessionFactory(sessionFactory);   
	}
}
