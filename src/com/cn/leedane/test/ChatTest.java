package com.cn.leedane.test;
import java.util.Map;
import java.util.Map.Entry;

import javax.annotation.Resource;

import net.sf.json.JSONObject;

import org.junit.Test;

import com.cn.leedane.bean.ChatBean;
import com.cn.leedane.bean.UserBean;
import com.cn.leedane.service.ChatService;
import com.cn.leedane.service.UserService;

/**
 * 聊天相关的测试类
 * @author LeeDane
 * 2016年5月9日 下午2:48:04
 * Version 1.0
 */
public class ChatTest extends BaseTest {

	@Resource
	private UserService<UserBean> userService;
	
	@Resource
	private ChatService<ChatBean> chatService;

	
	@Test
	public void sendChat(){
		UserBean user = userService.loadById(5);
		String str = "{\"toUserId\":\"1\",\"content\":\"锄禾日当午，汗滴禾下土，谁知盘中餐，粒粒皆辛苦。---李白\"}";
		JSONObject jo = JSONObject.fromObject(str);
		try {
			Map<String, Object> ls = chatService.send(jo, user, null);
			System.out.println("总数:" +ls.size());
			for(Entry<String, Object> entry :ls.entrySet()){
				System.out.println(entry.getKey() +":" +entry.getValue());
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
