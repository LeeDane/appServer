package com.cn.leedane.Utils;

import com.cn.leedane.bean.BlogBean;
import com.cn.leedane.bean.MoodBean;
import com.cn.leedane.bean.TimeLineBean;

/**
 * 时间线实体的工具类
 * @author LeeDane
 * 2016年4月8日 下午11:09:15
 * Version 1.0
 */
public class TimeLineBeanUtil {

	
	public TimeLineBean toTimeLineBean(BlogBean bean){
		TimeLineBean timeLineBean = new TimeLineBean();
		
		return timeLineBean;
	}
	
	public TimeLineBean toTimeLineBean(MoodBean bean){
		TimeLineBean timeLineBean = new TimeLineBean();
		
		return timeLineBean;
	}
	
	public static void main(String[] args) {
		System.out.println(test());
		System.out.println("hello");
	}
	
	public static String test(){
		
		new Thread(new Runnable() {
			
			@Override
			public void run() {
				for(int i = 0; i < 50000; i++){
					System.out.println("i:"+i);
				}
				System.out.println("world");
			}
		}).start();
		
		
		return "hhff";
	}
}
