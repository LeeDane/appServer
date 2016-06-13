package com.cn.leedane.enums;

/**
 * 文件的类型
 * @author LeeDane
 * 2015年11月10日 下午12:09:52
 * Version 1.0
 */
public enum FileType {
	IMAGE("image"), 
	BASE64_60X60("base64_60X60"),
	TEMPORARY("temporary"),  //临时
	FILE("file"); //文件

	public String value;
	private FileType(String value){
		this.value = value;
	}
}
