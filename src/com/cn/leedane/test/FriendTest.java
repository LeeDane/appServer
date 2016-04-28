package com.cn.leedane.test;

import javax.annotation.Resource;

import net.sf.json.JSONObject;

import org.junit.Test;

import com.cn.leedane.Utils.ConstantsUtil;
import com.cn.leedane.bean.FriendBean;
import com.cn.leedane.bean.UserBean;
import com.cn.leedane.service.FriendService;
import com.cn.leedane.service.UserService;

/**
 * 好友相关的测试类
 * @author LeeDane
 * 2015年10月18日 上午9:46:34
 * Version 1.0
 */
public class FriendTest extends BaseTest {
	@Resource
	private FriendService<FriendBean> friendService;
	
	@Resource
	private UserService<UserBean> userService;
	@Test
	public void deleteFriends(){
		boolean delete = friendService.deleteFriends(3, 4, 2);
		System.out.println("detete:" +delete);
	}
	
	@Test
	public void agreeFriends(){
		String str = "{\"friendId\":9, \"from_user_remark\":\"小羊\"}";
		JSONObject jsonObject = JSONObject.fromObject(str);
		UserBean user = userService.findById(1);
		friendService.addAgree(jsonObject, user, null);
	}
}
