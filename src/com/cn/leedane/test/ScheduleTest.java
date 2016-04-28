package com.cn.leedane.test;

import org.springframework.context.support.AbstractApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 * 任务调度相关的测试类
 * @author LeeDane
 * 2015年7月3日 下午6:30:05
 * Version 1.0
 */
public class ScheduleTest{
	
	@SuppressWarnings("resource")
	public static void main(String[] args) {
		@SuppressWarnings("unused")
		AbstractApplicationContext context = new ClassPathXmlApplicationContext("spring-quartz.xml");
	}

}
