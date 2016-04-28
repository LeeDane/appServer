package com.cn.leedane.Dao.impl;
import org.apache.log4j.Logger;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;

import com.cn.leedane.Dao.MoodDao;
import com.cn.leedane.bean.MoodBean;

/**
 * 心情dao实现类
 * @author LeeDane
 * 2015年11月22日 下午10:40:29
 * Version 1.0
 */
public class MoodDaoImpl extends BaseDaoImpl<MoodBean> implements MoodDao<MoodBean>{
	Logger logger = Logger.getLogger(getClass());
	@Autowired
	private JdbcTemplate jdbcTemplate;
	@Autowired  
	public void setSessionFactoryOverride(SessionFactory sessionFactory) {   
	      super.setSessionFactory(sessionFactory);   
	}     
}
