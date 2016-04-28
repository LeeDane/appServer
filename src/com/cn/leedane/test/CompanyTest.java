package com.cn.leedane.test;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import javax.annotation.Resource;
import org.junit.Test;
import com.cn.leedane.Utils.DateUtil;
import com.cn.leedane.bean.CompanyBean;
import com.cn.leedane.bean.ProductBean;
import com.cn.leedane.bean.UserBean;
import com.cn.leedane.service.CompanyService;
import com.cn.leedane.service.ProductService;
import com.cn.leedane.service.UserService;

/**
 * 公司相关的测试类
 * @author LeeDane
 * 2015年7月17日 下午8:17:47
 * Version 1.0
 */
public class CompanyTest extends BaseTest {
	
	@Resource
	private ProductService<ProductBean> productService;
	
	@Resource
	private UserService<UserBean> userService;

	@Resource
	private CompanyService<CompanyBean> companyService;
	
	
	@Test
	public void addCompany() throws Exception{
		UserBean user = userService.loadById(1);
		UserBean user2 = userService.loadById(2);
		UserBean user3 = userService.loadById(3);
		UserBean user4 = userService.loadById(4);
		UserBean user5 = userService.loadById(5);
		
		Set<UserBean> custome1 = new HashSet<UserBean>();
		custome1.add(user2);
		custome1.add(user3);
		
		Set<UserBean> custome2 = new HashSet<UserBean>();
		custome2.add(user4);
		custome2.add(user5);
		
		CompanyBean company1 = new CompanyBean();
		company1.setAddress("广东省深圳市宝安区");
		company1.setBoss("任正非");
		company1.setCompanyCreateTime(DateUtil.stringToDate("1983-01-11 00:00:00"));
		company1.setCreateUser(user);
		company1.setCreateTime(new Date());
		company1.setCredit(3);
		company1.setEmployee(100000);
		company1.setIndustry("传统行业");
		company1.setMobilePhone("0799-2346666");
		company1.setName("华为科技责任有限公司");
		company1.setPrice("3000亿人民币");
		company1.setStatus(1);
		company1.setType("民营");
		company1.setCustomeService(custome1);
		companyService.save(company1);
		
		CompanyBean company2 = new CompanyBean();
		company2.setAddress("广东省珠海市某区");
		company2.setBoss("黄章");
		company2.setCompanyCreateTime(DateUtil.stringToDate("2003-05-21 00:00:00"));
		company2.setCreateUser(user);
		company2.setCreateTime(new Date());
		company2.setCredit(4);
		company2.setEmployee(20000);
		company2.setIndustry("移动互联网");
		company2.setMobilePhone("0749-2346666");
		company2.setName("魅族科技责任有限公司");
		company2.setPrice("400亿人民币");
		company2.setStatus(1);
		company2.setType("民营");
		company2.setCustomeService(custome2);
		companyService.save(company2);
	}	
	
	@Test
	public void lodById(){
		companyService.findById(1);
	}
}
