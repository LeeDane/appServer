package com.cn.leedane.arithmetic;

/**
 * 
 * 地铁站点类
 * @author LeeDane
 * 2015年8月7日 下午5:36:03
 * Version 1.0
 */
public class Station {

	
	private int id;  //站点的id，必须标记唯一
	private String name;  //站点的名称
	
	public void setId(int id) {
		this.id = id;
	}
	public int getId() {
		return id;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getName() {
		return name;
	}
	
}
