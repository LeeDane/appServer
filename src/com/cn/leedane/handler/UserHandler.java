package com.cn.leedane.handler;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.cn.leedane.Utils.ConstantsUtil;
import com.cn.leedane.Utils.JsonUtil;
import com.cn.leedane.Utils.StringUtil;
import com.cn.leedane.bean.UserBean;
import com.cn.leedane.redis.util.RedisUtil;
import com.cn.leedane.service.UserService;

/**
 * 用户的处理类
 * @author LeeDane
 * 2016年3月19日 下午10:24:12
 * Version 1.0
 */
@Component
public class UserHandler {

	@Autowired
	private UserService<UserBean> userService;
	private RedisUtil redisUtil = RedisUtil.getInstance();
	
	public void setUserService(UserService<UserBean> userService) {
		this.userService = userService;
	}
	
	/**
	 * 获取系统全部的用户
	 * @return
	 */
	public JSONArray getAllUserDetail(){
		List<Map<String, Object>> uids = userService.getAllUserId();
		JSONArray userInfos = null;
		if(uids != null && uids.size() > 0){
			userInfos = new JSONArray();
			int userId;
			for(int i = 0; i < uids.size(); i++){
				userId = StringUtil.changeObjectToInt(uids.get(i).get("id"));
				userInfos.add(getUserDetail(userId));
			}
		}
		return userInfos;
	}
	
	/**
	 * 获取用户的头像路径
	 * @return
	 */
	public String getUserPicPath(int userId, String picSize){
		String userPicKey = getRedisUserPicKey(userId, picSize);
		String userPicPath = null;
		if(redisUtil.hasKey(userPicKey)){
			userPicPath = redisUtil.getString(userPicKey);
		}else{
			//查找数据库，找到用户的头像
			List<Map<String, Object>> list = userService.executeSQL("select qiniu_path user_pic_path from t_file_path f where is_upload_qiniu=? and f.table_name = 't_user' and f.table_uuid = ? and f.pic_order = 0 "+buildPicSizeSQL("30x30")+"limit 0,1", true, userId);
			if(list != null && list.size()>0){
				userPicPath = StringUtil.changeNotNull(list.get(0).get("user_pic_path"));
				if(StringUtil.isNotNull(userPicPath))
					redisUtil.addString(userPicKey, userPicPath);
			}
			
		}
		return userPicPath;
	}
	
	/**
	 * 获取用户的信息
	 * @return
	 */
	public JSONObject getUserDetail(int userId){
		String userInfoKey = getRedisUserInfoKey(userId);
		JSONObject userInfo = null;
		if(redisUtil.hasKey(userInfoKey)){
			userInfo = JSONObject.fromObject(redisUtil.getString(userInfoKey));
		}else{
			//查找数据库，找到用户的头像
			List<Map<String, Object>> list = userService.executeSQL("select id, account, email, age, mobile_phone, qq, sex, is_admin, no_login_code, personal_introduction from t_user u where status=? and id=? limit 1", ConstantsUtil.STATUS_NORMAL, userId);
			if(list != null && list.size()>0){
				userInfo = JSONObject.fromObject(list.get(0));
				redisUtil.addString(userInfoKey, userInfo.toString());
			}
			
		}
		return userInfo;
	}
	
	/**
	 * 获取用户的名称
	 * @return
	 */
	public String getUserName(int userId){
		JSONObject userInfo = getUserDetail(userId);
		return JsonUtil.getStringValue(userInfo, "account");
	}
	
	/**
	 * 通过用户名获取用户的ID
	 * @return
	 */
	public int getUserIdByName(String username){
		String usernameKey = getRedisUserNameKey(username);
		int userId = 0;
		if(redisUtil.hasKey(usernameKey)){
			userId = Integer.parseInt(redisUtil.getString(usernameKey));
		}else{
			userId = userService.getUserIdByName(username);
			if(userId > 0 ){
				redisUtil.addString(usernameKey, String.valueOf(userId));
			}
		}
		return userId;
	}
	
	/**
	 * 获取用户的绑定的手机号码
	 * @return
	 */
	public String getUserMobilePhone(int userId){
		JSONObject userInfo = getUserDetail(userId);
		return JsonUtil.getStringValue(userInfo, "mobile_phone");
	}
	
	/**
	 * 获取用户的用户名和头像(30x30)
	 * 返回{"user_pic_path":"","account":""}集合
	 * @param createUserId
	 * @param user
	 * @param friendObject
	 * @return
	 */
	public Map<String, Object> getBaseUserInfo(int createUserId, UserBean user, JSONObject friendObject){
		Map<String, Object> infoMap = new HashMap<String, Object>();
		if(createUserId> 0){
			String account = null;
			//JSONObject friendObject = friendHandler.getFromToFriends(user.getId());
			infoMap.put("user_pic_path", getUserPicPath(createUserId, "30x30"));
			if(createUserId != user.getId()){
				if(friendObject != null){
					account = JsonUtil.getStringValue(friendObject, "user_" +createUserId);
					if(StringUtil.isNotNull(account))
						//替换好友称呼的名称
						infoMap.put("account", account);
				}
			}else{
				infoMap.put("account", "本人");
			}
			
		}
		
		return infoMap;
	}
	
	/**
	 * 获取用户的用户名和头像(30x30)
	 * 返回{"user_pic_path":"","account":""}集合
	 * @param createUserId
	 * @param user
	 * @param friendObject
	 * @return
	 */
	public Map<String, Object> getBaseUserInfo(int toUserId){
		Map<String, Object> infoMap = new HashMap<String, Object>();
		if(toUserId> 0){
			//JSONObject friendObject = friendHandler.getFromToFriends(user.getId());
			infoMap.put("user_pic_path", getUserPicPath(toUserId, "30x30"));
			infoMap.put("account", getUserName(toUserId));
		}
		return infoMap;
	}
	
	
	/**
	 * 构建获取图像大小的SQL
	 * @param picSize
	 * @return
	 */
	private String buildPicSizeSQL(String picSize){
		if(StringUtil.isNull(picSize))
			return "";
		
		return " and (f.pic_size = '" + picSize +"' or f.pic_size = 'source') ";
	}
	
	public static String getRedisUserPicKey(int userId, String picSize){
		return "user_pic_"+userId +"_" +picSize;
	}
	
	public static String getRedisUserInfoKey(int userId){
		return "user_info_"+userId;
	}
	
	/**
	 * 缓存用户名信息
	 * @param username
	 * @return
	 */
	public static String getRedisUserNameKey(String username){
		return "user_name_"+username;
	}
	
	/*private String getRedisUserAccountKey(int userId){
		return "t_user_account_"+userId;
	}*/
}
