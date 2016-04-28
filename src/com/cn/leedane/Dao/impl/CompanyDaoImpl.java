package com.cn.leedane.Dao.impl;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;

import com.cn.leedane.Dao.CompanyDao;
import com.cn.leedane.bean.CompanyBean;
/**
 * 公司Dao实现类
 * @author LeeDane
 * 2015年7月17日 下午6:12:10
 * Version 1.0
 */
public class CompanyDaoImpl extends BaseDaoImpl<CompanyBean> implements CompanyDao<CompanyBean>{
	Logger logger = Logger.getLogger(getClass());
	@Autowired
	private JdbcTemplate jdbcTemplate;


}
