package com.cn.leedane.webservice;

public class AppWebservice {

	public String sayHello(String name){
		//System.out.println("--------------------------------");
		try {
			Thread.sleep(15000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		//System.out.println("*********************************");
		return "hello:"+name;
	}
}
