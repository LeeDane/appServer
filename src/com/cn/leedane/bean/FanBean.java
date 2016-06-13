package com.cn.leedane.bean;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * 粉丝的实体bean
 * @author LeeDane
 * 2016年4月11日 上午9:59:46
 * Version 1.0
 */
@Entity
@Table(name="T_FAN")
public class FanBean extends RecordTimeBean{

	//状态：1正常
	private static final long serialVersionUID = 1L;
	private int toUserId; //接收粉丝的用户ID
	
	private String remark;  //FromUserId对toUserId对应的备注信息
	
	@Column(name="to_user_id")
	public int getToUserId() {
		return toUserId;
	}
	public void setToUserId(int toUserId) {
		this.toUserId = toUserId;
	}
	@Column(name="user_remark")
	public String getRemark() {
		return remark;
	}
	public void setRemark(String remark) {
		this.remark = remark;
	}
	
}
