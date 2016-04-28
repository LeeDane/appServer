package com.cn.leedane.bean;

import javax.persistence.Column;
import javax.persistence.MappedSuperclass;

/**
 * 状态的抽象类
 * @author LeeDane
 * 2015年4月3日 上午10:04:29
 * Version 1.0
 */
@MappedSuperclass
public abstract class StatusBean extends IDBean{
	
	private static final long serialVersionUID = 1L;
	
	
	/**
	 * -1：表示草稿，1：表示正常，0表示禁用，(其他在自身的类中注明)
	 */
	@Column(columnDefinition="SMALLINT default 1", length=2, nullable=true)  //设置默认值是1，表示正常
	protected int status; 

	
	public int getStatus() {
		return status;
	}
	public void setStatus(int status) {
		this.status = status;
	}
}
