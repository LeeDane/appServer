package com.cn.leedane.enums;

/**
 * base64图像大小类型
 * @author LeeDane
 * 2015年11月10日 下午12:10:00
 * Version 1.0
 */
public enum Base64Type {
	BASE64_30X30("base64_30X30"), 
	BASE64_60X60("base64_60X60"); 

	public String value;
	private Base64Type(String value){
		this.value = value;
	}
}
