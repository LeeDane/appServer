package com.cn.leedane.Dao.impl;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;

import com.cn.leedane.Dao.CartDetailsDao;
import com.cn.leedane.bean.CartDetailsBean;
/**
 * 购物车清单Dao实现类
 * @author LeeDane
 * 2015年7月16日 上午11:45:55
 * Version 1.0
 */
public class CartDetailsDaoImpl extends BaseDaoImpl<CartDetailsBean> implements CartDetailsDao<CartDetailsBean>{
	Logger logger = Logger.getLogger(getClass());
	@Autowired
	private JdbcTemplate jdbcTemplate;


}
