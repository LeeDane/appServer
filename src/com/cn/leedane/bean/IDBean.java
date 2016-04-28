package com.cn.leedane.bean;

import java.io.Serializable;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;

import org.apache.solr.client.solrj.beans.Field;

/**
 * 自增长的 ID类的基类
 * @author LeeDane
 * 2015年4月3日 上午9:57:32
 * Version 1.0
 */
@MappedSuperclass
public class IDBean implements Serializable{
		
	private static final long serialVersionUID = 1L;
	
	/**
	 * 基本的ID列
	 */
	protected int id;
	
	@Id
    @GeneratedValue(strategy=GenerationType.AUTO)
	public int getId() {
		return id;
	}
	
	@Field
	public void setId(int id) {
		this.id = id;
	}
}
