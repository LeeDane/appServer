package com.cn.leedane.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.annotation.Resource;

import org.apache.log4j.Logger;

import com.cn.leedane.Dao.CartDao;
import com.cn.leedane.Dao.UserDao;
import com.cn.leedane.bean.CartBean;
import com.cn.leedane.bean.CartDetailsBean;
import com.cn.leedane.bean.ProductBean;
import com.cn.leedane.bean.UserBean;
import com.cn.leedane.service.CartService;
/**
 * 购物车service的实现类
 * @author LeeDane
 * 2015年7月17日 上午11:48:33
 * Version 1.0
 */
public class CartServiceImpl extends BaseServiceImpl<CartBean> implements CartService<CartBean>{
	Logger logger = Logger.getLogger(getClass());
	@Resource
	private UserDao<UserBean> userDao;
	
	@Resource
	private CartDao<CartBean> cartDao;
	
	public void setUserDao(UserDao<UserBean> userDao) {
		this.userDao = userDao;
	}
	
	public void setCartDao(CartDao<CartBean> cartDao) {
		this.cartDao = cartDao;
	}
	
	@Override
	public void addCart(){
		UserBean user = userDao.loadById(1);
		CartBean bean = new CartBean();
		bean.setCreateUser(user);
		bean.setCreateTime(new Date());
		
		List<CartDetailsBean> ls = new ArrayList<CartDetailsBean>();
		CartDetailsBean details1 = new CartDetailsBean();
		details1.setCreateUser(user);
		details1.setInventory(10);
		details1.setName("中兴手机");
		details1.setNumber(2);
		details1.setOriginPrice(1999f);
		ProductBean product1 = new ProductBean();
		product1.setName("中兴手机商品");
		//details1.setProduct(product1);
		details1.setPrice(1099f);
		details1.setTotalPrice(2198f);
		ls.add(details1);
		
		CartDetailsBean details2 = new CartDetailsBean();
		details2.setCreateUser(user);
		details2.setInventory(10);
		details2.setName("魅族手机");
		details2.setNumber(2);
		details2.setOriginPrice(2499f);
		ProductBean product2 = new ProductBean();
		product2.setName("魅族手机商品");
		//details2.setProduct(product1);
		details2.setPrice(1999f);
		details2.setTotalPrice(3998f);
		ls.add(details2);
		bean.setDetails(ls);
		
		cartDao.save(bean);
		
	}

}
