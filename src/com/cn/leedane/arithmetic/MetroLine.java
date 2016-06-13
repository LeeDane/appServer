package com.cn.leedane.arithmetic;

import java.util.List;

/**
 * 地铁线路对象
 * @author LeeDane
 * 2015年8月7日 下午5:34:11
 * Version 1.0
 */
public class MetroLine {

	private int id;  //线路(几号线)，标记唯一，如三号线和三号线延长线就要使用不同的id
	private String name; //线路的名称，如：1号线，方便用于展示
	private List<Station> station; //该号线上的地铁站点集合
	
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
	public void setStation(List<Station> station) {
		this.station = station;
	}
	public List<Station> getStation() {
		return station;
	}
	
}
