package com.cn.leedane.struts2.action;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import net.sf.json.JSONObject;

import com.cn.leedane.Utils.BaseActionContext;
import com.cn.leedane.Utils.ConstantsUtil;
import com.cn.leedane.Utils.EnumUtil;
import com.cn.leedane.Utils.HttpUtils;
import com.cn.leedane.bean.FriendBean;
import com.cn.leedane.bean.UserBean;
import com.cn.leedane.service.FriendService;
import com.cn.leedane.service.UserService;
/**
 * 好友action类
 * @author LeeDane
 * 2015年7月17日 下午6:32:32
 * Version 1.0
 */
public class FriendAction extends BaseActionContext{	
	//protected final Log log = LogFactory.getLog(getClass());
	private static final long serialVersionUID = 1L;
	
	private FriendService<FriendBean> friendService;
	//用户信息
	private UserService<UserBean> userService;
		
	@Resource
	public void setFriendService(FriendService<FriendBean> friendService) {
		this.friendService = friendService;
	}
	
	@Resource
	public void setUserService(UserService<UserBean> userService) {
		this.userService = userService;
	}
	
	/**
	 * 解除好友关系
	 * @return
	 * @throws Exception
	 */
	public String delete() throws Exception {
		message.put("isSuccess", resIsSuccess);
		try {
			//{"to_user_id": 2}
			JSONObject jo = HttpUtils.getJsonObjectFromInputStream(params, request);  
			if(jo == null || jo.isEmpty()) {	
				message.put("message", EnumUtil.getResponseValue(EnumUtil.ResponseCode.缺少请求参数.value));
				message.put("responseCode", EnumUtil.ResponseCode.缺少请求参数.value);
				return SUCCESS;
			}
			if(!jo.has("to_user_id")) {
				message.put("message", "删除的好友id为空"); 
				return SUCCESS;			
			}
			int to_user_id = jo.getInt("to_user_id");
			
			UserBean toUser = userService.findById(to_user_id);
			if(toUser != null){
				message.put("isSuccess", friendService.deleteFriends(user.getId(), to_user_id));
				
				//解除好友关系后，清除用户缓存的好友信息
				//加载全部的好友信息ID和备注信息进入session中
				//List<Map<String, Object>> friends = friendService.getFriends(user.getId());
				Object friendsObject = session.get(ConstantsUtil.MY_FRIENDS);
				if(friendsObject != null){
					@SuppressWarnings("unchecked")
					List<Map<String, Object>> friends = (List<Map<String, Object>>) friendsObject;
					if(friends != null && friends.size() > 0){
						for(int i = 0; i < friends.size(); i++){
							if(friends.get(i).containsKey(String.valueOf(to_user_id))){
								friends.remove(friends.get(i));
							}
						}
						session.put(ConstantsUtil.MY_FRIENDS, friends);
					}
				}

				// 保存操作日志信息
				String subject = user.getAccount()+"跟"+toUser.getAccount()+"解除好友关系成功";
				this.operateLogService.saveOperateLog(user, request, new Date(), subject, "isFriend", 1, 0);
				return SUCCESS;
			}
			
			
		} catch (Exception e) {
			e.printStackTrace();
		}
        message.put("isSuccess", false);     
        return SUCCESS;
	}
	/**
	 * 发起增加好友
	 * @return
	 */
	public String add() {
		message.put("isSuccess", resIsSuccess);
		try {
			//{"id":1, "to_user_id": 2, "add_introduce":"你好,我是XX"}
			JSONObject jo = HttpUtils.getJsonObjectFromInputStream(params, request);  
			if(jo == null || jo.isEmpty()) {	
				message.put("message", EnumUtil.getResponseValue(EnumUtil.ResponseCode.缺少请求参数.value));
				message.put("responseCode", EnumUtil.ResponseCode.缺少请求参数.value);
				return SUCCESS;
			}
			message.putAll(friendService.addFriend(jo, user, request));
			return SUCCESS;	
		} catch (Exception e) {
			//resmessage = "抱歉，添加好友执行出现异常！请核实提交的信息后重试或者联系管理员";
			e.printStackTrace();
		}
		message.put("message", EnumUtil.getResponseValue(EnumUtil.ResponseCode.服务器处理异常.value));
		message.put("responseCode", EnumUtil.ResponseCode.服务器处理异常.value);
        return SUCCESS;
	}
	
	/**
	 * 同意增加好友
	 * @return
	 */
	public String agreeFriend() {
		message.put("isSuccess", resIsSuccess);
		try {
			//{"relation_id":100}
			JSONObject jo = HttpUtils.getJsonObjectFromInputStream(params, request);  
			if(jo == null || jo.isEmpty()) {	
				message.put("message", EnumUtil.getResponseValue(EnumUtil.ResponseCode.缺少请求参数.value));
				message.put("responseCode", EnumUtil.ResponseCode.缺少请求参数.value);
				return SUCCESS;
			}
			if(!jo.has("relation_id") ) {
				message.put("message", "relation_id好友关系ID为空");   
				return SUCCESS;			
			}
			
			int relation_id = jo.getInt("relation_id");				 
			FriendBean friendBean = friendService.findById(relation_id);
			
			if(friendBean == null){
				message.put("message", "relation_id不存在");   
				return SUCCESS;		
			}
			
			friendBean.setStatus(ConstantsUtil.STATUS_NORMAL);
			
			if(!friendService.saveOrUpdate(friendBean)){
				message.put("message", "添加好友失败"); 
				return SUCCESS;	
			}
			
			//添加好友关系后，更新用户缓存的好友信息
			//加载全部的好友信息ID和备注信息进入session中
			List<Map<String, Object>> friends = friendService.getFromToFriends(user.getId());			
			session.put(ConstantsUtil.MY_FRIENDS, friends);			
			
			resIsSuccess = true;
			resmessage = "添加好友成功";
		} catch (Exception e) {
			resmessage = "抱歉，添加好友执行出现异常！请核实提交的信息后重试或者联系管理员";
			e.printStackTrace();
		}
        message.put("isSuccess", resIsSuccess);
		message.put("message", resmessage);        
        return SUCCESS;
	}
	
	/**
	 * 判读两人是否是好友
	 * @return
	 */
	public String isFriend() {
		try {
			message.put("isSuccess", false);
			//{"id":1, "to_user_id": 2}
			JSONObject jo = HttpUtils.getJsonObjectFromInputStream(params, request);  
			if(jo == null || jo.isEmpty()) {	
				message.put("message", EnumUtil.getResponseValue(EnumUtil.ResponseCode.缺少请求参数.value));
				message.put("responseCode", EnumUtil.ResponseCode.缺少请求参数.value);
				return SUCCESS;
			}
			if(jo.has("to_user_id")) {
				int to_user_id = jo.getInt("to_user_id");			
				UserBean toUser = userService.findById(to_user_id);
				if(toUser!= null){
					message.put("isSuccess", friendService.isFriend(user.getId(), to_user_id));
					
					// 保存操作日志信息
					String subject = user.getAccount()+"判断跟"+toUser.getAccount()+"是否是朋友";
					this.operateLogService.saveOperateLog(user, request, new Date(), subject, "isFriend", 1, 0);
					return SUCCESS;
				}	
					
			}
		} catch (Exception e) {
			e.printStackTrace();
		}     
        message.put("message", EnumUtil.getResponseValue(EnumUtil.ResponseCode.服务器处理异常.value));
		message.put("responseCode", EnumUtil.ResponseCode.服务器处理异常.value);
        return SUCCESS;
	}
	
	/**
	 * 获取已经跟我成为好友关系的分页列表
	 * @return
	 */
	public String friendsPaging() {
		try {
			message.put("isSuccess", false);
			//{"id":1, "to_user_id": 2}
			JSONObject jo = HttpUtils.getJsonObjectFromInputStream(params, request);  
			if(jo == null || jo.isEmpty()) {	
				message.put("message", EnumUtil.getResponseValue(EnumUtil.ResponseCode.缺少请求参数.value));
				message.put("responseCode", EnumUtil.ResponseCode.缺少请求参数.value);
				return SUCCESS;
			}
			message.putAll(friendService.friendsPaging(jo, user, request));
			return SUCCESS;
		} catch (Exception e) {
			e.printStackTrace();
		}     
        message.put("message", EnumUtil.getResponseValue(EnumUtil.ResponseCode.服务器处理异常.value));
		message.put("responseCode", EnumUtil.ResponseCode.服务器处理异常.value);
        return SUCCESS;
	}
	
	/**
	 * 获取我发送的好友请求列表
	 * @return
	 */
	public String requestPaging() {
		try {
			message.put("isSuccess", false);
			//{"id":1, "to_user_id": 2}
			JSONObject jo = HttpUtils.getJsonObjectFromInputStream(params, request);  
			if(jo == null || jo.isEmpty()) {	
				message.put("message", EnumUtil.getResponseValue(EnumUtil.ResponseCode.缺少请求参数.value));
				message.put("responseCode", EnumUtil.ResponseCode.缺少请求参数.value);
				return SUCCESS;
			}
			message.putAll(friendService.requestPaging(jo, user, request));
			return SUCCESS;
		} catch (Exception e) {
			e.printStackTrace();
		}     
        message.put("message", EnumUtil.getResponseValue(EnumUtil.ResponseCode.服务器处理异常.value));
		message.put("responseCode", EnumUtil.ResponseCode.服务器处理异常.value);
        return SUCCESS;
	}
	
	/**
	 * 获取等待我同意的好友关系列表
	 * @return
	 */
	public String responsePaging() {
		try {
			message.put("isSuccess", false);
			//{"id":1, "to_user_id": 2}
			JSONObject jo = HttpUtils.getJsonObjectFromInputStream(params, request);  
			if(jo == null || jo.isEmpty()) {	
				message.put("message", EnumUtil.getResponseValue(EnumUtil.ResponseCode.缺少请求参数.value));
				message.put("responseCode", EnumUtil.ResponseCode.缺少请求参数.value);
				return SUCCESS;
			}
			message.putAll(friendService.responsePaging(jo, user, request));
			return SUCCESS;
		} catch (Exception e) {
			e.printStackTrace();
		}     
        message.put("message", EnumUtil.getResponseValue(EnumUtil.ResponseCode.服务器处理异常.value));
		message.put("responseCode", EnumUtil.ResponseCode.服务器处理异常.value);
        return SUCCESS;
	}
	
	/**
	 * 用户本地联系人跟服务器上的好友进行匹配后返回
	 * @return
	 */
	public String matchContact() {
		try {
			message.put("isSuccess", false);
			//{"id":1, "to_user_id": 2}
			JSONObject jo = HttpUtils.getJsonObjectFromInputStream(params, request);  
			if(jo == null || jo.isEmpty()) {	
				message.put("message", EnumUtil.getResponseValue(EnumUtil.ResponseCode.缺少请求参数.value));
				message.put("responseCode", EnumUtil.ResponseCode.缺少请求参数.value);
				return SUCCESS;
			}
			message.putAll(friendService.matchContact(jo, user, request));
			return SUCCESS;
		} catch (Exception e) {
			e.printStackTrace();
		}     
        message.put("message", EnumUtil.getResponseValue(EnumUtil.ResponseCode.服务器处理异常.value));
		message.put("responseCode", EnumUtil.ResponseCode.服务器处理异常.value);
        return SUCCESS;
	}
}
