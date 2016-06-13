package com.cn.leedane.Dao;

import java.io.Serializable;

import com.cn.leedane.bean.ProductBean;

/**
 * 商品dao接口类
 * @author LeeDane
 * 2015年7月17日 下午6:22:29
 * Version 1.0
 */
public interface ProductDao<T extends Serializable> extends BaseDao<ProductBean>{

}
