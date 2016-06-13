package com.cn.leedane.common;

import org.springframework.context.support.AbstractApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
/**
 * spring上下文
 * @author LeeDane
 * @date 2015年4月23日 下午4:11:57
 * Version 1.0
 */
public class AppContext {
	
	private AbstractApplicationContext context;
	private static AppContext appContext;
	
	private AppContext(){
		//WebApplicationContextUtils.getRequiredWebApplicationContext(get())
		if(context == null){
			context = new ClassPathXmlApplicationContext(
					"classpath*:applicationContext.xml");
		}
	}
	
	/**
	 * 获取Spring的applicationContext的实例
	 * @return
	 */
	public static synchronized AppContext getInstance() {
		
		if(appContext == null){
			appContext = new AppContext();
		}		
		return appContext;
	}
	
	/**
	 * 获得Spring上下文对象
	 * @return
	 */
	public AbstractApplicationContext getContext(){
		return context;
	}
}
