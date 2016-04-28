package com.cn.leedane.service;
import java.io.Serializable;
import com.cn.leedane.bean.CartDetailsBean;
/**
 * 购物车清单的Service类
 * @author LeeDane
 * 2015年7月16日 下午 6:27:09
 * Version 1.0
 */
public interface CartDetailsService<T extends Serializable> extends BaseService<CartDetailsBean>{

	public void addCartDetails();
}
