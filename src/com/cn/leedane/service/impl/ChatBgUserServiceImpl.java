package com.cn.leedane.service.impl;
import javax.annotation.Resource;

import org.apache.log4j.Logger;

import com.cn.leedane.Dao.ChatBgUserDao;
import com.cn.leedane.Utils.EnumUtil.DataTableType;
import com.cn.leedane.bean.ChatBgUserBean;
import com.cn.leedane.bean.OperateLogBean;
import com.cn.leedane.service.ChatBgUserService;
import com.cn.leedane.service.OperateLogService;

/**
 * 聊天信息列表service实现类
 * @author LeeDane
 * 2016年5月5日 下午11:59:29
 * Version 1.0
 */
public class ChatBgUserServiceImpl extends BaseServiceImpl<ChatBgUserBean> implements ChatBgUserService<ChatBgUserBean> {
	Logger logger = Logger.getLogger(getClass());
	@Resource
	private ChatBgUserDao<ChatBgUserBean> chatBgUserDao;
	
	public void setChatBgUserDao(ChatBgUserDao<ChatBgUserBean> chatBgUserDao) {
		this.chatBgUserDao = chatBgUserDao;
	}
	
	@Resource
	private OperateLogService<OperateLogBean> operateLogService;
	
	public void setOperateLogService(
			OperateLogService<OperateLogBean> operateLogService) {
		this.operateLogService = operateLogService;
	}
	
	
	@Override
	public boolean exists(int userId, int chatBgTableId) {
		logger.info("ChatBgUserServiceImpl-->exists():userId="+userId+",chatBgTableId="+chatBgTableId);
		return chatBgUserDao.executeSQL("select id from "+DataTableType.聊天背景与用户.value+" where create_user_id=? and chat_bg_table_id=?", userId, chatBgTableId).size() > 0;
	}
}
