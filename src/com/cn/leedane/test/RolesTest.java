package com.cn.leedane.test;

import javax.annotation.Resource;

import org.junit.Test;
import com.cn.leedane.bean.RolesBean;
import com.cn.leedane.service.RolesService;

/**
 * 角色相关的测试类
 * @author LeeDane
 * 2015年7月22日 下午4:25:54
 * Version 1.0
 */

public class RolesTest extends BaseTest {
	
	@Resource
	private RolesService<RolesBean> rolesService;
	
	@Test
	public void addCompany() throws Exception{
		RolesBean roles = rolesService.loadById(1);
		
		rolesService.save(roles);
	}	
	
}
