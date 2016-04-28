package com.cn.leedane.test;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.annotation.Resource;

import org.junit.Test;

import com.cn.leedane.Utils.SpringUtils;
import com.cn.leedane.Utils.StringUtil;
import com.cn.leedane.bean.CartBean;
import com.cn.leedane.bean.CartDetailsBean;
import com.cn.leedane.bean.ProductBean;
import com.cn.leedane.bean.UserBean;
import com.cn.leedane.cache.SystemCache;
import com.cn.leedane.service.CartDetailsService;
import com.cn.leedane.service.CartService;
import com.cn.leedane.service.ProductService;
import com.cn.leedane.service.UserService;

/**
 * 购物车相关的测试类
 * @author LeeDane
 * 2015年7月18日 上午11:52:03
 * Version 1.0
 */
public class CartTest extends BaseTest {
	
	@Resource
	private CartService<CartBean> cartService;
	
	@Resource
	private UserService<UserBean> userService;
	
	@Resource
	private ProductService<ProductBean> productService;
	/**
	 * 系统级别的缓存对象
	 */
	private SystemCache systemCache;
	
	
	@Resource
	private CartDetailsService<CartDetailsBean> cartDetailsService;
	@Test
	public void addCart() throws Exception{
		systemCache = (SystemCache) SpringUtils.getBean("systemCache");
		String adminId = (String) systemCache.getCache("admin-id");
		int aid = 1;
		if(!StringUtil.isNull(adminId)){
			aid = Integer.parseInt(adminId);
		}
		UserBean user = userService.loadById(aid);
		CartBean bean = new CartBean();
		bean.setCreateUser(user);
		bean.setCreateTime(new Date());
		
		List<CartDetailsBean> ls = new ArrayList<CartDetailsBean>();
		CartDetailsBean details1 = new CartDetailsBean();
		details1.setCreateUser(user);
		details1.setInventory(10);
		details1.setName("华为手机");
		details1.setNumber(2);
		details1.setOriginPrice(1999f);
		ProductBean product1 = new ProductBean();
		product1.setName("华为手机商品");
		details1.setPrice(1099f);
		details1.setTotalPrice(2198f);
		details1.setCart(bean);
		details1.setProduct(productService.findById(1));
		ls.add(details1);
		
		CartDetailsBean details2 = new CartDetailsBean();
		details2.setCreateUser(user);
		details2.setInventory(10);
		details2.setName("魅族手机");
		details2.setNumber(2);
		details2.setOriginPrice(2499f);
		ProductBean product2 = new ProductBean();
		product2.setName("魅族手机商品");
		details2.setPrice(1999f);
		details2.setTotalPrice(3998f);
		details2.setCart(bean);
		ls.add(details2);
		details2.setProduct(productService.findById(2));
		bean.setDetails(ls);
		cartService.save(bean);
	}	
	@Test
	public void loadCart(){
		List<CartBean> beans = cartService.executeHQL("CartBean", null);
		for(CartBean bean: beans){
			System.out.println(bean.getDetails().get(0).getName());
		}
		
	}
	
	@Test
	public void loadById(){
		cartService.findById(1);
	}
}
