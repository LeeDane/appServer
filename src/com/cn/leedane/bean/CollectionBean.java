package com.cn.leedane.bean;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * 收藏实体类
 * @author LeeDane
 * 2015年4月3日 上午10:21:14
 * Version 1.0
 */
@Entity
@Table(name="T_COLLECTION")
public class CollectionBean extends RecordTimeBean{
	
	private static final long serialVersionUID = 1L;
	//收藏夹的状态,1：正常，0:禁用，2、删除
	
	
	/**
	 * 收藏对象的类型(对象表名)必须
	 */
	private String tableName;
	
	/**
	 * 收藏对象的ID，必须
	 */
	private int tableId;

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
}
