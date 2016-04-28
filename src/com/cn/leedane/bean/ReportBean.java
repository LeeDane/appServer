package com.cn.leedane.bean;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import org.hibernate.annotations.Type;

/**
 * 举报实体类
 * @author LeeDane
 * 2016年1月24日 下午8:04:31
 * Version 1.0
 */
@Entity
@Table(name="T_REPORT")
public class ReportBean extends RecordTimeBean{
	
	private static final long serialVersionUID = 1L;
	//举报的状态,1：正常，0:禁用，2、删除，3、审核中， 4、审核不通过
	
	/**
	 * 举报原因
	 */
	private String reason;
	
	/**
	 * 举报对象的类型(对象表名)必须
	 */
	private String tableName;
	
	/**
	 * 举报对象的ID，必须
	 */
	private int tableId;
	
	/**
	 * 类型
	 */
	private int type;
	
	@Type(type="text")
	@Column(name="reason", nullable=false)
	public String getReason() {
		return reason;
	}
	public void setReason(String reason) {
		this.reason = reason;
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
	
	@Column(name="report_type", nullable = false)
	public int getType() {
		return type;
	}
	public void setType(int type) {
		this.type = type;
	}
	
	
}
