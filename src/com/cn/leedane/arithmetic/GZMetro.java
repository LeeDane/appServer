package com.cn.leedane.arithmetic;

import java.util.ArrayList;
import java.util.List;

/**
 * 广州地铁对象
 * @author LeeDane
 * 2015年8月7日 下午5:32:21
 * Version 1.0
 */
public class GZMetro {

	List<MetroLine> lines = new ArrayList<MetroLine>();
	
	public void setLines(List<MetroLine> lines) {
		this.lines = lines;
	}
	
	public List<MetroLine> getLines() {
		return lines;
	}
}
