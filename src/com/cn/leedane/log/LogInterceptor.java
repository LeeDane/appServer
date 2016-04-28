package com.cn.leedane.log;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;

/**
 * 日志切面类
 * @author LeeDane
 * 2015年8月3日 上午10:24:59
 */
@Aspect
@Component
public class LogInterceptor {
	
	public long starttime;  //开始时间
	public long endtime;  //结束时间
	
	@Pointcut("execution(public * com.cn.leedane..*.save(..))")  
	public void aApplogic() {}  
	
	@Before(value = "aApplogic()")
	public void before(){
		starttime = System.currentTimeMillis();
		//System.out.println("操作开始前的拦截");
	}
	   
	@Around(value = "aApplogic() && @annotation(annotation) &&args(object,..) ", argNames = "annotation,object")  
	public Object interceptorApplogic(ProceedingJoinPoint pj,  
			LogAnnotation annotation, Object object) throws Throwable {  
	        //System.out.println("moduleName:"+annotation.moduleName());  
	        System.out.println("option:"+annotation.option());  
	        return pj.proceed();  
	}  
	
	@After(value = "aApplogic()")
	public void after(){
		endtime = System.currentTimeMillis();
		System.out.println("总计耗时：" + (endtime - starttime) + "毫秒");
	}
	 

}
