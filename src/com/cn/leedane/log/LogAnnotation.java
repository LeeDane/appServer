package com.cn.leedane.log;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 日志业务处理注释类
 * @author LeeDane
 * 2015年8月3日 上午10:07:42
 * Version 1.0
 */

@Retention(RetentionPolicy.RUNTIME)//代表的是表示在源码、编译好的.class文件中保留信息，在执行的时候会把这一些信息加载到JVM中去的。
@Target({ElementType.METHOD})    //表示作用于方法上
public @interface LogAnnotation {

	//模块名  
	String moduleName();  
	//操作内容  
	String option();  
	
	//String clName();  //

}
