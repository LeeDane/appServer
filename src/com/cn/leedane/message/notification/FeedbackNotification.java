package com.cn.leedane.message.notification;

import java.util.HashMap;

/**
 * 点击需要反馈的通知
 * @author LeeDane
 * 2015年11月4日 上午10:22:29
 * Version 1.0
 */
public class FeedbackNotification extends Notification{
	/**
	 * 用户点击通知后执行的参数，点击后从中获取
	 */
	private HashMap<String, Object> clickLinkParams;
}
