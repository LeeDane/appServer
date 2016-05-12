package com.cn.leedane.test;

import com.cn.leedane.message.JpushCustomMessage;

public class JPushTest {

	public static void main(String[] args) {
		/*JPushClient jpushClient = new JPushClient(ConstantsUtil.JPUSH_MASTER_SECRET, ConstantsUtil.JPUSH_APPKEY, 10);
		
		// For push, all you need do is to build PushPayload object.
        PushPayload payload = buildPushObject_all_all_alert();

        try {
            PushResult result = jpushClient.sendPush(payload);
            //jpushClient.sendIosNotificationWithRegistrationID(alert, extras, registrationID)
            System.out.println("Got result - " + result);

        } catch (APIConnectionException e) {
            // Connection error, should retry later
        	System.out.println("Connection error, should retry later");

        } catch (APIRequestException e) {
            // Should review the error, and fix the request
        	System.out.println("Should review the error, and fix the request");
            System.out.println("HTTP Status: " + e.getStatus());
            System.out.println("Error Code: " + e.getErrorCode());
            System.out.println("Error Message: " + e.getErrorMessage());
        }*/
		/*MessageNotification messageNotification = new JPushMessageNotificationImpl();
		messageNotification.sendToAlias("leedane_user_"+1, "你好。。 server3");*/
		JpushCustomMessage message= new JpushCustomMessage();
		System.out.println(message.sendToAlias("leedane_user_1", "我是测试号", "toUserId", "2"));
	}
	
}
