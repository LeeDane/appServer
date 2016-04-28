package com.cn.leedane.struts2.action;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.springframework.beans.factory.annotation.Autowired;

import com.cn.leedane.Utils.BaseActionContext;
import com.cn.leedane.Utils.ConstantsUtil;
import com.cn.leedane.Utils.DateUtil;
import com.cn.leedane.Utils.EnumUtil;
import com.cn.leedane.Utils.HttpUtils;
import com.cn.leedane.Utils.JsonUtil;
import com.cn.leedane.Utils.MD5Util;
import com.cn.leedane.Utils.SpringUtils;
import com.cn.leedane.Utils.StringUtil;
import com.cn.leedane.bean.FriendBean;
import com.cn.leedane.bean.UserBean;
import com.cn.leedane.cache.SystemCache;
import com.cn.leedane.enums.LoginType;
import com.cn.leedane.handler.UserHandler;
import com.cn.leedane.handler.WechatHandler;
import com.cn.leedane.service.FriendService;
import com.cn.leedane.service.UserService;
import com.cn.leedane.wechat.bean.WeixinCacheBean;
import com.cn.leedane.wechat.util.WeixinUtil;
import com.opensymphony.xwork2.ActionContext;

/**
 * 用户/管理员/客服Action类
 * @author LeeDane
 * 2015年7月17日 下午6:33:23
 * Version 1.0
 */
public class UserAction extends BaseActionContext {
	// protected final Log log = LogFactory.getLog(getClass());
	public UserBean user;
	
	/**
	 * 请求的信息
	 */
	public String params;

	public String registerCode;// 注册码
	private SystemCache systemCache;
	/**
	 * 响应结果信息
	 */
	public Map<String, Object> message = new HashMap<String, Object>();
	
	
	public Map<String, Object> getMessage() {
		return message;
	}

	public void setMessage(Map<String, Object> message) {
		this.message = message;
	}


	//用户信息
	private UserService<UserBean> userService;
	
	@Autowired
	private UserHandler userHandler;
	
	public void setUserHandler(UserHandler userHandler) {
		this.userHandler = userHandler;
	}
	
	@Autowired
	private WechatHandler wechatHandler;
	
	public void setWechatHandler(WechatHandler wechatHandler) {
		this.wechatHandler = wechatHandler;
	}
	//好友信息
	@Autowired
	private FriendService<FriendBean> friendService; 
	
	public void setFriendService(FriendService<FriendBean> friendService) {
		this.friendService = friendService;
	}

	//返回结果中包含的是否成功
	private boolean resIsSuccess = false;
	//返回结果中包含的提示信息
	private String resmessage;
	//返回结果中包含的响应码
	private int responseCode;

	@Resource
	public void setUserService(UserService<UserBean> userService) {
		this.userService = userService;
	}

	private static final long serialVersionUID = 1L;

	/**
	 * 登录
	 * @return
	 * @throws Exception
	 */
	public String login() throws Exception {
		try {
			JSONObject jo = HttpUtils.getJsonObjectFromInputStream(params, request);
			if(jo == null || jo.isEmpty()) {	
				message.put("message", EnumUtil.getResponseValue(EnumUtil.ResponseCode.缺少请求参数.value));
				message.put("responseCode", EnumUtil.ResponseCode.缺少请求参数.value);
				return SUCCESS;
			}
			
			//执行密码等信息的验证
			user = userService.loginUser(
					String.valueOf(jo.get("account")),String.valueOf(jo.get("password")));

			if (user != null) {
			
				//校验权限和角色
				if(checkRole(user) && checkPemission(user)){
					//登录成功后加载权限和角色信息缓存中
					
					if(user != null){
						if(user.getStatus() == 4 ){
							resmessage = EnumUtil.getResponseValue(EnumUtil.ResponseCode.用户已经注销.value);
							message.put("responseCode", EnumUtil.ResponseCode.用户已经注销.value);
						}else if(user.getStatus() == 0){
							resmessage = EnumUtil.getResponseValue(EnumUtil.ResponseCode.请先验证邮箱.value);
							message.put("responseCode", EnumUtil.ResponseCode.请先验证邮箱.value);
						}
						else {
							
							this.resIsSuccess = true;
							//判断是android平台的登录
							if(jo.has("login_mothod") && LoginType.LOGIN_TYPE_ANDROID.getValue().equals(jo.get("login_mothod"))){
								
								String noLoginCode = JsonUtil.getStringValue(jo, "no_login_code");
								//检查免登陆码是否存在
								//不存在
								if(StringUtil.isNull(noLoginCode)){
									noLoginCode = StringUtil.getNoLoginCode(user.getAccount());
									//upDateNoLoginCode(user, noLoginCode);	
									//更新免登陆验证码
									user.setNoLoginCode(noLoginCode);
									userService.update(user);
								}else{
									//存在的话检查验证码的有效性，失效的验证码就更新验证码
								}
							}else{
								// 登录成功后将必要信息加载到session
								putInSessionAfterLoginSuccess();
							}
							
							message.put("userinfo", getUserInfo(user, true));
							resmessage = EnumUtil.getResponseValue(EnumUtil.ResponseCode.恭喜您登录成功.value);
							message.put("responseCode", EnumUtil.ResponseCode.恭喜您登录成功.value);
						}
					}else{
						resmessage = EnumUtil.getResponseValue(EnumUtil.ResponseCode.账号或密码不匹配.value);
						message.put("responseCode", EnumUtil.ResponseCode.账号或密码不匹配.value);
					}
				}else{	
					resmessage = EnumUtil.getResponseValue(EnumUtil.ResponseCode.没有操作权限.value);
					message.put("responseCode", EnumUtil.ResponseCode.没有操作权限.value);
				}
				
				// 保存用户登录日志信息
				String subject = user != null ? user.getAccount()+"登录系统": "账号"+jo.get("account")+"登录系统失败";
				this.operateLogService.saveOperateLog(user, request, new Date(), subject, "login", resIsSuccess? 1 : 0, 0);
			}else{
				resmessage = EnumUtil.getResponseValue(EnumUtil.ResponseCode.账号或密码不匹配.value);
				message.put("responseCode", EnumUtil.ResponseCode.账号或密码不匹配.value);
			}
		} catch (Exception e) {
			resmessage = EnumUtil.getResponseValue(EnumUtil.ResponseCode.服务器处理异常.value);
			message.put("responseCode", EnumUtil.ResponseCode.服务器处理异常.value);
			e.printStackTrace();
		}
		message.put("isSuccess", resIsSuccess);
		message.put("message", resmessage);
		return SUCCESS;
	}
	
	/**
	 * 根据用户id获取当个用户的id
	 * @return
	 */
	public String searchUserByUserId(){
		try {
			JSONObject jo = HttpUtils.getJsonObjectFromInputStream(params, request);
			if(jo == null || jo.isEmpty()) {	
				message.put("message", EnumUtil.getResponseValue(EnumUtil.ResponseCode.缺少请求参数.value));
				message.put("responseCode", EnumUtil.ResponseCode.缺少请求参数.value);
				return SUCCESS;
			}
			//获取查找的用户id
			int searchUserId = JsonUtil.getIntValue(jo, "searchUserId");
			//执行密码等信息的验证
			UserBean searchUser = userService.findById(searchUserId);

			if (searchUser != null) {			
				
				// 保存操作日志信息
				String subject = user.getAccount() + "查看" + searchUser.getAccount() + "个人基本信息";
				this.operateLogService.saveOperateLog(user, request, new Date(), subject, "searchUserByUserId", 1, 0);
				message.put("userinfo", getUserInfo(searchUser, false));
				message.put("isSuccess", true);
				return SUCCESS;
			}else{
				resmessage = EnumUtil.getResponseValue(EnumUtil.ResponseCode.用户不存在或请求参数不对.value);
				message.put("responseCode", EnumUtil.ResponseCode.用户不存在或请求参数不对.value);
			}
		} catch (Exception e) {
			resmessage = EnumUtil.getResponseValue(EnumUtil.ResponseCode.服务器处理异常.value);
			message.put("responseCode", EnumUtil.ResponseCode.服务器处理异常.value);
			e.printStackTrace();
		}
		message.put("isSuccess", resIsSuccess);
		message.put("message", resmessage);
		return SUCCESS;
	}

	/**
	 * 获取提供调用使用的用户信息
	 * @param user2
	 * @param isSelf  是否是自己，自己的话可以不加载一些信息
	 * @return
	 */
	private Map<String, Object> getUserInfo(UserBean user2, boolean isSelf) {
		HashMap<String, Object> infos = new HashMap<String, Object>();
		if(user2 != null){
			infos.put("id", user2.getId());
			infos.put("account", user2.getAccount());
			infos.put("email", user2.getEmail());
			infos.put("age", user2.getAge());
			infos.put("mobile_phone", user2.getMobilePhone());
			//infos.put("pic_path", user2.getPicPath());
			infos.put("qq", user2.getQq());
			infos.put("sex", user2.getSex());
			infos.put("is_admin", user2.isAdmin());
			infos.put("head_path", userHandler.getUserPicPath(user2.getId(), "30x30"));
			/*String str = "{\"uid\":"+user2.getId()+", \"pic_size\":\"60x60\"}";
			JSONObject jo = JSONObject.fromObject(str);
			try {
				infos.put("head_path", userService.getHeadFilePathStrById(jo, user2, request));
			} catch (Exception e) {
				e.printStackTrace();
			}*/
			
			if(isSelf){
				infos.put("no_login_code", user2.getNoLoginCode());
			}
			infos.put("personal_introduction", user2.getPersonalIntroduction());
		}
		return infos;
	}

	/**
	 * 检查用户的权限
	 * @param user
	 * @return
	 */
	private boolean checkPemission(UserBean user) {
		return true;
	}

	/**
	 * 检查用户角色
	 * @param user
	 * @return
	 */
	private boolean checkRole(UserBean user) {
		return true;
	}

	/**
	 * 登录成功后系统的缓存数据
	 */
	private void putInSessionAfterLoginSuccess() {
		//先把session全部清空
		session.clear();
		//加载全部的好友信息ID和备注信息进入session中
		List<Map<String, Object>> friends = friendService.getFromToFriends(user.getId());
		session.put(ConstantsUtil.MY_FRIENDS, friends);
		session.put(ConstantsUtil.USER_ACCOUNT_SESSION, user.getAccount());
		session.put(ConstantsUtil.USER_SESSION, user);
	}

	/**
	 * 注册用户
	 * @return
	 * @throws Exception
	 */
	public String registerUser() throws Exception {		
		//判断是否有在线的用户，那就先取消该用户的session
		if(ActionContext.getContext().getSession().get(ConstantsUtil.USER_SESSION) != null) {
			removeMultSession(ConstantsUtil.USER_SESSION);
		}
		try{	
			JSONObject jo = HttpUtils.getJsonObjectFromInputStream(params, request);
			if(jo == null || jo.isEmpty()) {	
				message.put("message", EnumUtil.getResponseValue(EnumUtil.ResponseCode.缺少请求参数.value));
				message.put("responseCode", EnumUtil.ResponseCode.缺少请求参数.value);
				return SUCCESS;
			}
			user = new UserBean();
			user.setAccount(String.valueOf(jo.get("account")));
			user.setEmail(String.valueOf(jo.get("email")));
			user.setPassword(MD5Util.compute(jo.getString("password")));
			Date registerTime = new Date();
			user.setRegisterTime(registerTime);
			user.setRegisterCode(StringUtil.produceRegisterCode(DateUtil.DateToString(registerTime, "YYYYMMDDHHmmss"),
					jo.getString("account")));
			message = userService.saveUser(user);
			systemCache = (SystemCache) SpringUtils.getBean("systemCache");
			String adminId = (String) systemCache.getCache("admin-id");
			int aid = 1;
			if(!StringUtil.isNull(adminId)){
				aid = Integer.parseInt(adminId);
			}
			UserBean opearteUser = userService.findById(aid);
			//保存操作日志
			this.operateLogService.saveOperateLog(opearteUser, request, null, user.getAccount()+"注册成功", "register", 1, 0);
			
		}catch(Exception e){
			e.printStackTrace();
		}
		return SUCCESS;
	}

	/**
	 * 完成注册
	 * 
	 * @return
	 */
	public String completeRegister() {
		if (this.userService.updateCheckRegisterCode(this.registerCode))
			return "completeRegisterSuccess";
		else
			return "completeRegisterFailure";
	}
	
	/**
	 * 再次发送邮箱验证信息
	 */
	public String againSendRegisterEmail() {
		JSONObject jo = null;
		try {	
			jo = HttpUtils.getJsonObjectFromInputStream(params,request);
			if(jo == null || jo.isEmpty()) {	
				message.put("message", EnumUtil.getResponseValue(EnumUtil.ResponseCode.缺少请求参数.value));
				message.put("responseCode", EnumUtil.ResponseCode.缺少请求参数.value);
				return SUCCESS;
			}
			//根据账号和密码找到该用户(密码需要再进行MD5加密)
			user = userService.loginUser(jo.getString("account"), jo.getString("password"));
			
			if(user != null && user.getStatus() == 2){
				//生成注册码
				String newRegisterCode = StringUtil.produceRegisterCode(DateUtil.DateToString(new Date(),"YYYYMMDDHHmmss"),
						user.getAccount());
				user.setRegisterCode(newRegisterCode);
				boolean isUpdate = userService.update(user);
					if(isUpdate)
						//发送邮件
						userService.sendEmail(user);
					
					this.resIsSuccess = true;
					this.resmessage = EnumUtil.getResponseValue(EnumUtil.ResponseCode.邮件已发送成功.value);
					this.responseCode = EnumUtil.ResponseCode.邮件已发送成功.value;
			}else{
				this.resmessage = EnumUtil.getResponseValue(EnumUtil.ResponseCode.不是未注册状态邮箱不能发注册码.value);
				this.responseCode = EnumUtil.ResponseCode.不是未注册状态邮箱不能发注册码.value;
			}	
			
			this.operateLogService.saveOperateLog(user, request, null, user.getAccount()+"请求发送邮箱", "againSendRegisterEmail", resIsSuccess? 1 : 0, 0);
		} catch (Exception e) {
			e.printStackTrace();
			this.resmessage = EnumUtil.getResponseValue(EnumUtil.ResponseCode.邮件发送失败.value);
			this.responseCode = EnumUtil.ResponseCode.邮件发送失败.value;
		}finally{
			message.put("isSuccess", resIsSuccess);
			message.put("message", resmessage);
			message.put("responseCode", responseCode);
		}
		return SUCCESS;
	}
	
	/**
	 * 找回密码
	 * @return
	 */
	public String findPassword(){
		JSONObject jo = null;
		try {	
			jo = HttpUtils.getJsonObjectFromInputStream(params,request);
			if(jo == null || jo.isEmpty()) {	
				message.put("message", EnumUtil.getResponseValue(EnumUtil.ResponseCode.缺少请求参数.value));
				message.put("responseCode", EnumUtil.ResponseCode.缺少请求参数.value);
				return SUCCESS;
			}
			//获得找回密码的类型(0:邮箱,1:手机)
			if(jo.getInt("type") == 0){
				this.resmessage = EnumUtil.getResponseValue(EnumUtil.ResponseCode.暂时不支持手机找回密码功能.value);
				this.responseCode = EnumUtil.ResponseCode.暂时不支持手机找回密码功能.value;
			}else if(jo.getInt("type") == 1){
				this.resmessage = EnumUtil.getResponseValue(EnumUtil.ResponseCode.暂时不支持邮箱找回密码功能.value);
				this.responseCode = EnumUtil.ResponseCode.暂时不支持邮箱找回密码功能.value;
			}else{
				this.resmessage = EnumUtil.getResponseValue(EnumUtil.ResponseCode.未知的找回密码类型.value);
				this.responseCode = EnumUtil.ResponseCode.未知的找回密码类型.value;
			}
			//this.operateLogService.saveOperateLog(user, request, null, user.getAccount()+"寻找密码", "findPassword", resIsSuccess? 1 : 0, 0);
		} catch (Exception e) {
			e.printStackTrace();
			this.resmessage = EnumUtil.getResponseValue(EnumUtil.ResponseCode.服务器处理异常.value);
			this.responseCode = EnumUtil.ResponseCode.服务器处理异常.value;
		}finally{
			message.put("message", this.resmessage);
			message.put("responseCode", responseCode);
			message.put("isSuccess", this.resIsSuccess);
		}
		return SUCCESS;
	}
	
	/**
	 * 退出系统
	 * @return
	 */
	public String logout(){
		
		//判断是否有在线的用户，那就先取消该用户的session
		if(session.get(ConstantsUtil.USER_SESSION) != null) {
			user = (UserBean) ActionContext.getContext().getSession().get("user");
			try {
				this.operateLogService.saveOperateLog(user, request, null, user.getAccount()+"退出系统", "logout", resIsSuccess? 1 : 0, 0);
			} catch (Exception e) {
				e.printStackTrace();
			}
			session.clear();
			//removeSession(ConstantsUtil.USER_SESSION);
		}
		
		message.put("resmessage", EnumUtil.getResponseValue(EnumUtil.ResponseCode.注销成功.value));
		message.put("responseCode", EnumUtil.ResponseCode.注销成功.value);
		message.put("isSuccess", true);
		return SUCCESS;
	}
	
	/**
	 * 根据用户的id获取用户的base64位图像信息
	 * {"uid":2, "size":"30x30"} "order":0默认是0, tablename:"t_user"
	 * @return
	 */
	public String getHeadBase64StrById(){
		message.put("isSuccess", resIsSuccess);
		try {
			JSONObject jo = HttpUtils.getJsonObjectFromInputStream(params,request);
			if(jo == null || jo.isEmpty()) {	
				message.put("message", EnumUtil.getResponseValue(EnumUtil.ResponseCode.缺少请求参数.value));
				message.put("responseCode", EnumUtil.ResponseCode.缺少请求参数.value);
				return SUCCESS;
			}
			
			resmessage = userService.getHeadBase64StrById(jo, user, request);
			resIsSuccess = true;
		} catch (Exception e) {
			e.printStackTrace();
		}
		message.put("isSuccess", resIsSuccess);
		message.put("message", resmessage);
        return SUCCESS;
	}
	
	/**
	 * 用户上传个人的头像
	 * {"base64":"hhdjshuffnfbnfds"}
	 * @return
	 */
	public String uploadHeadBase64Str(){
		message.put("isSuccess", resIsSuccess);
		try {
			JSONObject jo = HttpUtils.getJsonObjectFromInputStream(params,request);
			if(jo == null || jo.isEmpty()) {	
				message.put("message", EnumUtil.getResponseValue(EnumUtil.ResponseCode.缺少请求参数.value));
				message.put("responseCode", EnumUtil.ResponseCode.缺少请求参数.value);
				return SUCCESS;
			}
			
			resIsSuccess = userService.uploadHeadBase64StrById(jo, user, request);
		} catch (Exception e) {
			e.printStackTrace();
		}
		message.put("isSuccess", resIsSuccess);
		//保存操作日志
		try {
			operateLogService.saveOperateLog(user, request, null, user.getAccount()+"上传头像" + StringUtil.getSuccessOrNoStr(resIsSuccess), "uploadHeadBase64Str", ConstantsUtil.STATUS_NORMAL, 0);
		} catch (Exception e) {
			e.printStackTrace();
		}
        return SUCCESS;
	}
	
	
	/**
	 * 取得所有用户
	 * @return
	 */
	public String getAllUsers(){
		String page = request.getParameter("page");
		String start = request.getParameter("start");
		String limit = request.getParameter("limit");
		String sort = "";
		sort = request.getParameter("sort");
		systemCache = (SystemCache) SpringUtils.getBean("systemCache");
		String adminId = (String) systemCache.getCache("admin-id");
		int aid = 1;
		if(!StringUtil.isNull(adminId)){
			aid = Integer.parseInt(adminId);
		}
		UserBean user = userService.findById(aid);
		
		if(user == null){
			this.resmessage = "请先登录";
			message.put("isSuccess", false);
			message.put("resmessage", resmessage);
			return SUCCESS;
		}
		
		List<Map<String, Object>> ls = new ArrayList<Map<String, Object>>();
		
		//要是有一个为空，说明只加载
		if(StringUtil.isNull(page) || StringUtil.isNull(start) || StringUtil.isNull(limit)){
			
		}else{
			//int p = Integer.parseInt(page);
			int s = Integer.parseInt(start);
			int l = Integer.parseInt(limit);
			if(!StringUtil.isNull(sort)){
				JSONArray ja = JSONArray.fromObject(sort);
				if(ja != null){
					sort = "order by " + ja.getJSONObject(0).getString("property") + " " + ja.getJSONObject(0).getString("direction") + " ";
				}
			}
			sort = sort == null || sort.equals("") ? " " : sort + " ";
			int total = userService.total("t_user", "id", " where status=1 ");
			ls = userService.find4MoreUser(sort + "limit ?,?", s, l);
			buildGetAllUserResp(ls,total);
		}
		
		try {
			this.operateLogService.saveOperateLog(user, request, null, user.getAccount()+"查看所有用户", "getAllUsers", resIsSuccess? 1 : 0, 0);
		} catch (Exception e) {
			e.printStackTrace();
		}
				
		return SUCCESS;
	}
	
	/**
	 * 统计所有用户的年龄
	 * @return
	 */
	public String statisticsUserAge() {
		List<Map<String, Object>> ls = new ArrayList<Map<String, Object>>();		
		ls = userService.statisticsUserAge();
				
		message.put("xaxis", "统计所有用户的年龄"); //X轴名称
		message.put("yaxis", "人数"); //Y轴名称
		message.put("maximum", getMaximum(ls)); //年龄段人数最多的数字
		message.put("data", ls);
		
		try {
			this.operateLogService.saveOperateLog(user, request, null, "统计所有用户的年龄", "getAllUsers", resIsSuccess? 1 : 0, 0);
		} catch (Exception e) {
			e.printStackTrace();
		}

		return SUCCESS;
	}
	
	/**
	 * 统计所有用户的年龄段
	 * @return
	 */
	public String statisticsUserAgeRang() {
		List<Map<String, Object>> ls = new ArrayList<Map<String, Object>>();		
		ls = userService.statisticsUserAgeRang();
				
		message.put("xaxis", "统计所有用户的年龄"); //X轴名称
		message.put("yaxis", "人数"); //Y轴名称
		message.put("maximum", getMaximum(ls)); //Y轴人数最多的数字
		message.put("data", ls);
		return SUCCESS;
	}
	
	/**
	 * 统计所有用户的注册时间的年份
	 * @return
	 */
	public String statisticsUserRegisterByYear() {
		List<Map<String, Object>> ls = new ArrayList<Map<String, Object>>();		
		ls = userService.statisticsUserRegisterByYear();	
		message.put("xaxis", "统计所有用户的注册时间的年份"); //X轴名称
		message.put("yaxis", "人数"); //Y轴名称
		message.put("maximum", getMaximum(ls)); //Y轴人数最多的数字
		message.put("data", ls);
		return SUCCESS;
	}
	
	/**
	 * 统计所有用户的注册时间的月份
	 * @return
	 */
	public String statisticsUserRegisterByMonth() {
		List<Map<String, Object>> ls = new ArrayList<Map<String, Object>>();		
		ls = userService.statisticsUserRegisterByMonth();
				
		message.put("xaxis", "统计所有用户的注册时间的月份"); //X轴名称
		message.put("yaxis", "人数"); //Y轴名称
		message.put("maximum", getMaximum(ls)); //Y轴人数最多的数字
		message.put("data", ls);
		return SUCCESS;
	}
	
	/**
	 * 统计所有用户的最近一个月的注册人数
	 * @return
	 */
	public String statisticsUserRegisterByNearMonth() {
		List<Map<String, Object>> ls = new ArrayList<Map<String, Object>>();		
		ls = userService.statisticsUserRegisterByNearMonth();
				
		message.put("xaxis", "统计所有用户的最近一个月的注册人数"); //X轴名称
		message.put("yaxis", "人数"); //Y轴名称
		message.put("maximum", getMaximum(ls)); //Y轴人数最多的数字
		message.put("data", ls);
		return SUCCESS;
	}
	
	/**
	 * 统计所有用户的最近一周的注册人数
	 * @return
	 */
	public String statisticsUserRegisterByNearWeek() {
		List<Map<String, Object>> ls = new ArrayList<Map<String, Object>>();		
		ls = userService.statisticsUserRegisterByNearWeek();
				
		message.put("xaxis", "统计所有用户的最近一周的注册人数"); //X轴名称
		message.put("yaxis", "人数"); //Y轴名称
		message.put("maximum", getMaximum(ls)); //Y轴人数最多的数字
		message.put("data", ls);
		return SUCCESS;
	}
	
	/**
	 * 获得y轴中人数最多的数量
	 * @param ls
	 * @return
	 */
	private int getMaximum(List<Map<String, Object>> ls) {
		int maximum = 0;
		if(ls == null || ls.size() == 0)
			return maximum;
		for(Map<String, Object> m: ls){
			int i = StringUtil.changeObjectToInt(m.get("yaxis"));
			if(i > maximum){
				maximum = i;
			}
		}
		
		return maximum;
	}

	/**
	 * 构建获得全部用户返回响应的数据
	 */
	private void buildGetAllUserResp(List<Map<String, Object>> ls, int total) {
		message.put("rows", ls);
		message.put("total", total);
		
	}
	
	/**
	 * 获取手机注册的验证码
	 * @return
	 */
	public String getPhoneRegisterCode(){
		message.put("isSuccess", resIsSuccess);
		try {
			JSONObject jo = HttpUtils.getJsonObjectFromInputStream(params,request);
			if(jo == null || jo.isEmpty()) {	
				message.put("message", EnumUtil.getResponseValue(EnumUtil.ResponseCode.缺少请求参数.value));
				message.put("responseCode", EnumUtil.ResponseCode.缺少请求参数.value);
				return SUCCESS;
			}
			message.putAll(userService.getPhoneRegisterCode(jo, request));
			return SUCCESS;
		} catch (Exception e) {
			e.printStackTrace();
		}
		message.put("message", EnumUtil.getResponseValue(EnumUtil.ResponseCode.服务器处理异常.value));
		message.put("responseCode", EnumUtil.ResponseCode.服务器处理异常.value);
		return SUCCESS;
	}
	
	/**
	 * 获取手机登录的验证码
	 * @return
	 */
	public String getPhoneLoginCode(){
		message.put("isSuccess", resIsSuccess);
		try {
			JSONObject jo = HttpUtils.getJsonObjectFromInputStream(params,request);
			if(jo == null || jo.isEmpty()) {	
				message.put("message", EnumUtil.getResponseValue(EnumUtil.ResponseCode.缺少请求参数.value));
				message.put("responseCode", EnumUtil.ResponseCode.缺少请求参数.value);
				return SUCCESS;
			}
			message.putAll(userService.getPhoneLoginCode(jo, request));
			return SUCCESS;
		} catch (Exception e) {
			e.printStackTrace();
		}
		message.put("message", EnumUtil.getResponseValue(EnumUtil.ResponseCode.服务器处理异常.value));
		message.put("responseCode", EnumUtil.ResponseCode.服务器处理异常.value);
		return SUCCESS;
	}
	/**
	 * 通过手机注册
	 * @return
	 */
	public String registerByPhone(){
		message.put("isSuccess", resIsSuccess);
		try {
			JSONObject jo = HttpUtils.getJsonObjectFromInputStream(params,request);
			if(jo == null || jo.isEmpty()) {	
				message.put("message", EnumUtil.getResponseValue(EnumUtil.ResponseCode.缺少请求参数.value));
				message.put("responseCode", EnumUtil.ResponseCode.缺少请求参数.value);
				return SUCCESS;
			}
			
			user = userService.registerByPhone(jo, request);
			if(user == null){
				resmessage = "用户不存在或者参数不正确";
			}else{
				if(user.getStatus() == 4 ){
					resmessage = "用户已经被注销,有疑问请联系客服";
				}else if(user.getStatus() == 0){
					resmessage = "请先登录邮箱完成注册...";
				}else{
					message.put("userinfo", getUserInfo(user, true));
					resmessage = "登录成功，正在为您跳转...";
					this.resIsSuccess = true;
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		message.put("isSuccess", resIsSuccess);
		return SUCCESS;
	}
	/**
	 * 通过手机登录
	 * @return
	 */
	public String loginByPhone(){
		message.put("isSuccess", resIsSuccess);
		try {
			JSONObject jo = HttpUtils.getJsonObjectFromInputStream(params,request);
			if(jo == null || jo.isEmpty()) {	
				message.put("message", EnumUtil.getResponseValue(EnumUtil.ResponseCode.缺少请求参数.value));
				message.put("responseCode", EnumUtil.ResponseCode.缺少请求参数.value);
				return SUCCESS;
			}
			
			user = userService.loginByPhone(jo, request);
			if(user == null){
				resmessage = EnumUtil.getResponseValue(EnumUtil.ResponseCode.用户不存在或请求参数不对.value);
				message.put("responseCode", EnumUtil.ResponseCode.用户不存在或请求参数不对.value);
			}else{
				if(user.getStatus() == 4 ){
					resmessage = EnumUtil.getResponseValue(EnumUtil.ResponseCode.用户已经注销.value);
					message.put("responseCode", EnumUtil.ResponseCode.用户已经注销.value);
				}else if(user.getStatus() == 0){
					resmessage = EnumUtil.getResponseValue(EnumUtil.ResponseCode.请先验证邮箱.value);
					message.put("responseCode", EnumUtil.ResponseCode.请先验证邮箱.value);
				}else{
					message.put("userinfo", getUserInfo(user, true));
					resmessage = EnumUtil.getResponseValue(EnumUtil.ResponseCode.恭喜您登录成功.value);
					message.put("responseCode", EnumUtil.ResponseCode.恭喜您登录成功.value);
					this.resIsSuccess = true;
				}
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		message.put("isSuccess", resIsSuccess);
		message.put("message", resmessage);
		return SUCCESS;
	}
	/**
	 * 检查账号是否已经存在
	 * @return
	 */
	public String checkAccount(){
		message.put("isSuccess", resIsSuccess);
		try {
			JSONObject jo = HttpUtils.getJsonObjectFromInputStream(params,request);
			if(jo == null || jo.isEmpty()) {	
				message.put("message", EnumUtil.getResponseValue(EnumUtil.ResponseCode.缺少请求参数.value));
				message.put("responseCode", EnumUtil.ResponseCode.缺少请求参数.value);
				return SUCCESS;
			}
			
			message.putAll(userService.checkAccount(jo, request, user));
			return SUCCESS;
		} catch (Exception e) {
			e.printStackTrace();
		}
		message.put("message", EnumUtil.getResponseValue(EnumUtil.ResponseCode.服务器处理异常.value));
		message.put("responseCode", EnumUtil.ResponseCode.服务器处理异常.value);
		return SUCCESS;
	}
	
	/**
	 * 绑定微信账号
	 * @return
	 * @throws Exception
	 */
	public String bingWechat() throws Exception {
		try {
			message.put("isSuccess", resIsSuccess);
			JSONObject jo = HttpUtils.getJsonObjectFromInputStream(params, request);
			if(jo == null || jo.isEmpty()) {	
				message.put("message", EnumUtil.getResponseValue(EnumUtil.ResponseCode.缺少请求参数.value));
				message.put("responseCode", EnumUtil.ResponseCode.缺少请求参数.value);
				return SUCCESS;
			}
			
			String FromUserName = JsonUtil.getStringValue(jo, "FromUserName");
			String account = JsonUtil.getStringValue(jo, "account");
			String password = JsonUtil.getStringValue(jo, "password");
			if(StringUtil.isNull(FromUserName) || StringUtil.isNull(account) || StringUtil.isNull(password)){
				message.put("message", EnumUtil.getResponseValue(EnumUtil.ResponseCode.某些参数为空.value));
				message.put("responseCode", EnumUtil.ResponseCode.某些参数为空.value);
				return SUCCESS;
			}
			
			//执行绑定
			user = userService.bindByWeChat(FromUserName, account, password);
			if (user != null) {
				WeixinCacheBean cacheBean = new WeixinCacheBean();
				String currentType = JsonUtil.getStringValue(jo, "currentType", WeixinUtil.MODEL_MAIN_MENU);
				cacheBean.setBindLogin(true);
				cacheBean.setCurrentType(currentType);
				cacheBean.setLastBlogId(0);
				
				wechatHandler.addCache(FromUserName, cacheBean);
				
				message.put("message", EnumUtil.getResponseValue(EnumUtil.ResponseCode.操作成功.value));
				message.put("responseCode", EnumUtil.ResponseCode.操作成功.value);
				message.put("isSuccess", true);
				
				// 保存用户绑定日志信息
				String subject = user.getAccount()+"绑定账号微信账号"+ FromUserName +"成功";
				this.operateLogService.saveOperateLog(user, request, new Date(), subject, "bingWechat", 1, 0);
				return SUCCESS;
			}else{
				message.put("message", EnumUtil.getResponseValue(EnumUtil.ResponseCode.账号或密码不匹配.value));
				message.put("responseCode", EnumUtil.ResponseCode.账号或密码不匹配.value);
				return SUCCESS;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		message.put("message", EnumUtil.getResponseValue(EnumUtil.ResponseCode.服务器处理异常.value));
		message.put("responseCode", EnumUtil.ResponseCode.服务器处理异常.value);
		return SUCCESS;
	}
}
