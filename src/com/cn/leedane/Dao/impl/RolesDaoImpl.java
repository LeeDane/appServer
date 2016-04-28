package com.cn.leedane.Dao.impl;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;

import com.cn.leedane.Dao.RolesDao;
import com.cn.leedane.bean.RolesBean;

/**
 * 角色dao实现类
 * @author LeeDane
 * 2015年7月22日 下午4:19:26
 * Version 1.0
 */
public class RolesDaoImpl extends BaseDaoImpl<RolesBean> implements RolesDao<RolesBean>{
	Logger logger = Logger.getLogger(getClass());
	@Autowired
	private JdbcTemplate jdbcTemplate;
	
}
