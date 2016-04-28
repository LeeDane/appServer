package com.cn.leedane.rabbitmq;

import com.cn.leedane.Exception.ErrorException;
import com.cn.leedane.Utils.DateUtil;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.MessageProperties;

public class SendMessage {

	private SendAndRecieveObject object;

	public SendMessage(SendAndRecieveObject object) throws ErrorException {
		this.object = object;
		if (object == null) {
			throw new ErrorException("发送对象不能为空");
		}
	}

	public boolean sendMsg() {
		boolean success = false;
		try {
			/**
			 * 创建连接连接到MabbitMQ
			 */
			ConnectionFactory factory = new ConnectionFactory();
			// 设置MabbitMQ所在主机ip或者主机名
			factory.setHost("localhost");
			// 创建一个连接
			Connection connection = factory.newConnection();
			// 创建一个频道
			Channel channel = connection.createChannel();
			boolean durable = true; // 队列持久
			// 指定一个队列
			channel.queueDeclare(object.getToUserID()
					+ SendAndRecieveObject.DEFAULT_SUFFIX, durable, false,
					false, null);
			// 发送的消息
			String message = object.getFromUserID() + "@@"+object.getMsg()+"@@"+DateUtil.DateToString(object.getCreateTime());
			// 往队列中发出一条消息(MessageProperties.PERSISTENT_TEXT_PLAIN指定消息的持久化)
			channel.basicPublish("", object.getToUserID()
					+ SendAndRecieveObject.DEFAULT_SUFFIX,
					MessageProperties.PERSISTENT_TEXT_PLAIN, message.getBytes());
			System.out.println(object.getFromUserID()+"给"+object.getToUserID()+ "发送的消息:" + message + "'");
		} catch (Exception e) {
			e.printStackTrace();
		}
		return success;
	}
}
