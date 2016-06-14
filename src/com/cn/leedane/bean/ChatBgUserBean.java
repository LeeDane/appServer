package com.cn.leedane.bean;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * 聊天背景与用户关系的实体bean
 * @author LeeDane
 * 2016年6月13日 下午4:43:47
 * Version 1.0
 */
@Entity
@Table(name="T_CHAT_BG_USER")
public class ChatBgUserBean extends RecordTimeBean{

	private static final long serialVersionUID = 1L;
	
	private int chatBgTableId; //背景图像表的Id

	
	@Column(name="chat_bg_table_id")
	public int getChatBgTableId() {
		return chatBgTableId;
	}

	public void setChatBgTableId(int chatBgTableId) {
		this.chatBgTableId = chatBgTableId;
	}
	
	
}
