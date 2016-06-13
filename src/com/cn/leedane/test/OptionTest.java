package com.cn.leedane.test;

import javax.annotation.Resource;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import com.cn.leedane.bean.OptionBean;
import com.cn.leedane.service.OptionService;
import com.cn.leedane.service.TimeConsumingTest;

/**
 * Option选项实体相关的测试类
 * @author LeeDane
 * 2015年4月3日 下午3:40:31
 * Version 1.0
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations={"classpath:applicationContext.xml","classpath:spring-beans.xml","classpath:spring-aop.xml"}) 
public class OptionTest {
	
	@Resource
	private OptionService<OptionBean> optionService;

	public void setOptionService(OptionService<OptionBean> optionService) {
		this.optionService = optionService;
	}

	@Test
	public void testSave() throws Exception {
		OptionBean bean = new OptionBean();
		bean.setId(1);
		bean.setOptionKey("hello");
		bean.setOptionValue("world");
		bean.setVersion(3);
		optionService.save(bean);

	}
	
	@Test
	public void testUpdate() throws Exception{
		
		OptionBean bean = optionService.loadById(1);
		bean.setVersion(3);
		optionService.update(bean);
	}
	
	@Test
	public void testDelete(){
		TimeConsumingTest test = new TimeConsumingTest();
		test.save();
	}
}
