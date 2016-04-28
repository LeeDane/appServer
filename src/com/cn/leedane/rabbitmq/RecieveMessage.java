package com.cn.leedane.rabbitmq;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.QueueingConsumer;

public class RecieveMessage {

	private String toUserID;
	public RecieveMessage(String toUserID) {
		this.toUserID = toUserID;
	}
	
	public String getMsg() throws Exception{
		//打开连接和创建频道，与发送端一样
		ConnectionFactory factory = new ConnectionFactory();
		factory.setHost("localhost");
		Connection connection = factory.newConnection();
		Channel channel = connection.createChannel();
		//声明队列，主要为了防止消息接收者先运行此程序，队列还不存在时创建队列。
		//channel.queueDeclare(QUEUE_NAME, false, false, false, null);
		System.out.println("ID:"+toUserID+"正在等待消息");
		
		//创建队列消费者
		QueueingConsumer consumer = new QueueingConsumer(channel);
		//指定消费队列
		channel.basicConsume(this.toUserID+SendAndRecieveObject.DEFAULT_SUFFIX, true, consumer);
		String message = null;
		while (true)
		{
			//nextDelivery是一个阻塞方法（内部实现其实是阻塞队列的take方法）
			QueueingConsumer.Delivery delivery = consumer.nextDelivery();
			message = new String(delivery.getBody());
			System.out.println("用户ID"+toUserID+"接收到信息是：'" + message + "'");
			channel.basicAck(delivery.getEnvelope().getDeliveryTag(), false);
			if(message != null)
				return message;
		}
		
	}
}
