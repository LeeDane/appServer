package com.cn.leedane.observer.template;

import com.cn.leedane.enums.NotificationType;

/**
 * 增加评论的通知模板类
 * @author LeeDane
 * 2016年3月28日 下午7:05:23
 * Version 1.0
 */
public class AddCommentTemplate implements NotificationTemplate {

	@Override
	public NotificationType getNotifitionType() {
		return NotificationType.ADD_COMMENT;
	}

	@Override
	public String getNotifitionContent() {
		return "{}{to_user_remark}更新了说说";
	}

}
