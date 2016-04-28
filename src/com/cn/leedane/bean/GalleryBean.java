package com.cn.leedane.bean;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * 图库实体类
 * @author LeeDane
 * 2016年1月15日 下午3:10:04
 * Version 1.0
 */

@Entity
@Table(name="T_GALLERY")
public class GalleryBean extends RecordTimeBean{
	
	private static final long serialVersionUID = 1L;
	
	//图库的状态,1：正常，0:禁用，2、删除
	
	/**
	 * 路径
	 */
	private String path;
	
	/**
	 * 图片的宽度
	 */
	private int width;
	
	/**
	 * 图片的高度
	 */
	private int height;
	
	/**
	 * 图片的大小
	 */
	private long length;

	private String desc;

	@Column(length=255, nullable=false)
	public String getPath() {
		return path;
	}

	public void setPath(String path) {
		this.path = path;
	}

	@Column(name="gallery_desc", nullable=true)
	public String getDesc() {
		return desc;
	}

	public void setDesc(String desc) {
		this.desc = desc;
	}

	public int getWidth() {
		return width;
	}

	public void setWidth(int width) {
		this.width = width;
	}

	public int getHeight() {
		return height;
	}

	public void setHeight(int height) {
		this.height = height;
	}

	public long getLength() {
		return length;
	}

	public void setLength(long length) {
		this.length = length;
	}
	
}
