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
import com.cn.leedane.Utils.JsonUtil;
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
			JSONObject jo = HttpUtils.getJsonObjectFromInputStream(params, request);  
			if(jo == null || jo.isEmpty()) {	
				message.put("message", EnumUtil.getResponseValue(EnumUtil.ResponseCode.缺少请求参数.value));
				message.put("responseCode", EnumUtil.ResponseCode.缺少请求参数.value);
				return SUCCESS;
			}
			message.putAll(friendService.deleteFriends(jo, user, request));
			return SUCCESS;
		} catch (Exception e) {
			e.printStackTrace();
		}
		message.put("message", EnumUtil.getResponseValue(EnumUtil.ResponseCode.服务器处理异常.value));
		message.put("responseCode", EnumUtil.ResponseCode.服务器处理异常.value);
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
			message.putAll(friendService.addAgree(jo, user, request));
			return SUCCESS;
		} catch (Exception e) {
			e.printStackTrace();
		}
		message.put("message", EnumUtil.getResponseValue(EnumUtil.ResponseCode.服务器处理异常.value));
		message.put("responseCode", EnumUtil.ResponseCode.服务器处理异常.value);      
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
			message.putAll(friendService.friendsAlreadyPaging(jo, user, request));
			return SUCCESS;
		} catch (Exception e) {
			e.printStackTrace();
		}     
        message.put("message", EnumUtil.getResponseValue(EnumUtil.ResponseCode.服务器处理异常.value));
		message.put("responseCode", EnumUtil.ResponseCode.服务器处理异常.value);
        return SUCCESS;
	}
	
	/**
	 * 获取还未跟我成为好友关系的用户(我发起对方未答应或者对方发起我还未答应的)
	 * @return
	 */
	public String friendsNotyetPaging() {
		try {
			message.put("isSuccess", false);
			//{"id":1, "to_user_id": 2}
			JSONObject jo = HttpUtils.getJsonObjectFromInputStream(params, request);  
			if(jo == null || jo.isEmpty()) {	
				message.put("message", EnumUtil.getResponseValue(EnumUtil.ResponseCode.缺少请求参数.value));
				message.put("responseCode", EnumUtil.ResponseCode.缺少请求参数.value);
				return SUCCESS;
			}
			message.putAll(friendService.friendsNotyetPaging(jo, user, request));
			return SUCCESS;
		} catch (Exception e) {
			e.printStackTrace();
		}     
        message.put("message", EnumUtil.getResponseValue(EnumUtil.ResponseCode.服务器处理异常.value));
		message.put("responseCode", EnumUtil.ResponseCode.服务器处理异常.value);
        return SUCCESS;
	}
	
	
	/**
	 * 获取全部已经跟我成为好友关系列表
	 * @return
	 */
	public String friends() {
		try {
			message.put("isSuccess", false);
			JSONObject jo = HttpUtils.getJsonObjectFromInputStream(params, request);  
			if(jo == null || jo.isEmpty()) {	
				message.put("message", EnumUtil.getResponseValue(EnumUtil.ResponseCode.缺少请求参数.value));
				message.put("responseCode", EnumUtil.ResponseCode.缺少请求参数.value);
				return SUCCESS;
			}
			message.putAll(friendService.friends(jo, user, request));
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
