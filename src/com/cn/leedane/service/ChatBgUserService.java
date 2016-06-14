package com.cn.leedane.service;

import java.io.Serializable;

import com.cn.leedane.bean.ChatBgUserBean;

/**
 * 聊天背景与用户关系相关service接口类
 * @author LeeDane
 * 2016年6月13日 下午4:48:46
 * Version 1.0
 */
public interface ChatBgUserService <T extends Serializable> extends BaseService<ChatBgUserBean>{
	
	/**
	 * 判断是否下载过
	 * @param userId
	 * @param chatBgTableId
	 * @return
	 */
	public boolean exists(int userId, int chatBgTableId);
}
