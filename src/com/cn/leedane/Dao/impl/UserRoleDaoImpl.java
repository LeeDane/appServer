package com.cn.leedane.Dao.impl;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;

import com.cn.leedane.Dao.UserRoleDao;
import com.cn.leedane.bean.UserRoleBean;

/**
 * 用户角色dao实现类
 * @author LeeDane
 * 2015年7月22日 下午6:42:23
 * Version 1.0
 */
public class UserRoleDaoImpl extends BaseDaoImpl<UserRoleBean> implements UserRoleDao<UserRoleBean>{
	Logger logger = Logger.getLogger(getClass());
	@Autowired
	private JdbcTemplate jdbcTemplate;
	
}
