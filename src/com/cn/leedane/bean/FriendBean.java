package com.cn.leedane.bean;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * 好友关系的实体bean
 * @author LeeDane
 * 2015年9月15日 上午11:23:10
 * Version 1.0
 */
@Entity
@Table(name="T_FRIEND")
public class FriendBean extends RecordTimeBean{

	//状态：0:请求好友，1:正式好友，2：已经删除的好友，3：黑名单好友
	private static final long serialVersionUID = 1L;
	
	private int fromUserId;  //发起请求好友的用户ID
	private int toUserId; //接收好友的用户ID
	
	private String fromUserRemark;  //toUserId对FromUserId对应的备注信息
	private String toUserRemark;  //FromUserId对toUserId对应的备注信息
	
	private String addIntroduce ;  //FromUserId对toUserId的自我介绍信息
	
	@Column(name="from_user_id")
	public int getFromUserId() {
		return fromUserId;
	}
	public void setFromUserId(int fromUserId) {
		this.fromUserId = fromUserId;
	}
	
	@Column(name="to_user_id")
	public int getToUserId() {
		return toUserId;
	}
	public void setToUserId(int toUserId) {
		this.toUserId = toUserId;
	}
	
	@Column(name="from_user_remark")
	public String getFromUserRemark() {
		return fromUserRemark;
	}
	public void setFromUserRemark(String fromUserRemark) {
		this.fromUserRemark = fromUserRemark;
	}
	
	@Column(name="to_user_remark")
	public String getToUserRemark() {
		return toUserRemark;
	}
	public void setToUserRemark(String toUserRemark) {
		this.toUserRemark = toUserRemark;
	}
	
	@Column(name="add_introduce")
	public String getAddIntroduce() {
		return addIntroduce;
	}
	public void setAddIntroduce(String addIntroduce) {
		this.addIntroduce = addIntroduce;
	}
	
}
