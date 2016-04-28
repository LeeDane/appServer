package com.cn.leedane.message;

import com.cn.leedane.message.notification.Notification;

/**
 * 发送通知的接口
 * @author LeeDane
 * 2015年11月4日 上午10:59:39
 * Version 1.0
 */
public interface ISendNotification {
	public boolean Send(Notification notification);
}
