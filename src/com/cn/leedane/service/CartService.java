package com.cn.leedane.service;
import java.io.Serializable;

import com.cn.leedane.bean.CartBean;
/**
 * 购物车的Service类
 * @author LeeDane
 * 2015年7月18日 上午11:47:09
 * Version 1.0
 */
public interface CartService<T extends Serializable> extends BaseService<CartBean>{

	public void addCart();
}
