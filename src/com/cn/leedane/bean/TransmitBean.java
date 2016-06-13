package com.cn.leedane.bean;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import org.hibernate.annotations.Type;

/**
 * 转发实体类
 * @author LeeDane
 * 2016年1月13日 上午11:24:36
 * Version 1.0
 */
@Entity
@Table(name="T_TRANSMIT")
public class TransmitBean extends RecordTimeBean{
	
	private static final long serialVersionUID = 1L;
	//转发的状态,1：正常，0:禁用，2、删除，3、审核中， 4、审核不通过
	
	/**
	 * 转发时候的评论内容
	 */
	private String content;
	
	/**
	 * 来自什么方式
	 */
	private String froms;
	
	/**
	 * 转发对象的类型(对象表名)必须
	 */
	private String tableName;
	
	/**
	 * 转发对象的ID，必须
	 */
	private int tableId;
	
	@Type(type="text")
	@Column(name="content", nullable=false)
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
	
	@Column(name="table_name", length= 15, nullable=false)
	public String getTableName() {
		return tableName;
	}
	public void setTableName(String tableName) {
		this.tableName = tableName;
	}
	
	@Column(name="table_id", nullable = false)
	public int getTableId() {
		return tableId;
	}
	public void setTableId(int tableId) {
		this.tableId = tableId;
	}
	public String getFroms() {
		return froms;
	}
	public void setFroms(String froms) {
		this.froms = froms;
	}
	
}
