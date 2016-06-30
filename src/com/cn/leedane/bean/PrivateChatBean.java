package com.cn.leedane.bean;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * 私信的实体bean
 * @author LeeDane
 * 2016年6月30日 上午11:18:27
 * Version 1.0
 */
@Entity
@Table(name="T_PRIVATE_CHAT")
public class PrivateChatBean extends RecordTimeBean{

	private static final long serialVersionUID = 1L;
	
	private int toUserId; //接收好友的用户ID
	
	private String content ;  //发送消息的内容
	
	private int type; //发送消息的类型，0：文本,1:语音, 3:图片
	
	@Column(name="to_user_id")
	public int getToUserId() {
		return toUserId;
	}
	public void setToUserId(int toUserId) {
		this.toUserId = toUserId;
	}
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
	public int getType() {
		return type;
	}
	public void setType(int type) {
		this.type = type;
	}
	
}
