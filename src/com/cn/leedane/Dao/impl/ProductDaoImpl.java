package com.cn.leedane.Dao.impl;
import org.apache.log4j.Logger;

import com.cn.leedane.Dao.ProductDao;
import com.cn.leedane.bean.ProductBean;

/**
 * 商品dao实现类
 * @author LeeDane
 * 2015年7月17日 下午6:24:38
 * Version 1.0
 */
public class ProductDaoImpl extends BaseDaoImpl<ProductBean> implements ProductDao<ProductBean>{
	Logger logger = Logger.getLogger(getClass());
}
