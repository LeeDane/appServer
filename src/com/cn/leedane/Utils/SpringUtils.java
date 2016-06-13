package com.cn.leedane.Utils;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

/**
 * spring相关工具类
 * @author LeeDane
 * 2015年7月17日 下午6:40:17
 * Version 1.0
 */
public class SpringUtils implements ApplicationContextAware {

	private static ApplicationContext applicationContext;

	public static Object getBean(String beanName) {	
		return applicationContext.getBean(beanName);
	}

	@Override
	public void setApplicationContext(ApplicationContext context)
			throws BeansException {
		SpringUtils.applicationContext = context;
	}
	
}
