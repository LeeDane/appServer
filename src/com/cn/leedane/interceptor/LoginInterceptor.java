package com.cn.leedane.interceptor;

import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;

import net.sf.json.JSONObject;

import com.cn.leedane.Utils.ConstantsUtil;
import com.cn.leedane.Utils.DateUtil;
import com.cn.leedane.Utils.EnumUtil;
import com.cn.leedane.Utils.HttpUtils;
import com.cn.leedane.Utils.JsonUtil;
import com.cn.leedane.Utils.SpringUtils;
import com.cn.leedane.Utils.StringUtil;
import com.cn.leedane.bean.UserBean;
import com.cn.leedane.cache.SystemCache;
import com.cn.leedane.handler.UserHandler;
import com.cn.leedane.redis.util.RedisUtil;
import com.cn.leedane.service.UserService;
import com.opensymphony.xwork2.Action;
import com.opensymphony.xwork2.ActionContext;
import com.opensymphony.xwork2.ActionInvocation;
import com.opensymphony.xwork2.interceptor.AbstractInterceptor;
import com.opensymphony.xwork2.util.ValueStack;

/**
 * 控制登录的拦截器
 * @author LeeDane
 * 2015年9月2日 下午4:59:14
 * Version 1.0
 */
public class LoginInterceptor extends AbstractInterceptor {

	private static final long serialVersionUID = 1L;
	/**
	 * 系统的缓存对象
	 */
	private SystemCache systemCache;
	
	@Autowired
	private UserService<UserBean> userService;
	
	public UserService<UserBean> getUserService() {
		return userService;
	}
	
	public void setUserService(UserService<UserBean> userService) {
		this.userService = userService;
	}
	
	@Autowired
	private UserHandler userHandler;
	
	public void setUserHandler(UserHandler userHandler) {
		this.userHandler = userHandler;
	}
		
	/**
	 * 获得systemCache对象
	 */
	private void initCache(){
		if(systemCache == null){
			systemCache = (SystemCache) SpringUtils.getBean("systemCache");
		}
	}

	@Override
	public String intercept(ActionInvocation invocation) throws Exception {
		initCache();
		
		ActionContext actionContext = invocation.getInvocationContext();
		//取得session对象
		Map<String,Object> session = actionContext.getSession();
		//取得HttpServletRequest对象
		HttpServletRequest request = (HttpServletRequest) actionContext.get(org.apache.struts2.StrutsStatics.HTTP_REQUEST);
		
		Map<String, Object> message = new HashMap<String, Object>();
		
		//判断是否是系统维护时间
		String maintenanceOrigin = systemCache.getCache("maintenance-period") == null ? null : (String)systemCache.getCache("maintenance-period");
		if(!StringUtil.isNull(maintenanceOrigin)){
			String maintenance = maintenanceOrigin.replaceAll("AM", "").replaceAll("PM", "");
			String[] dates = maintenance.split("-");
			int begin = 0, end = 0;
			if(dates.length ==2){
				begin = Integer.parseInt(dates[0]);
				end = Integer.parseInt(dates[1]);
	
				Calendar c = Calendar.getInstance();
				c.setTime(new Date());
				int hour = c.get(Calendar.HOUR_OF_DAY);
				if(begin <= hour || end >= hour){//在系统维护的时间内
					message.put("isSuccess", false);
					message.put("message", "抱歉，每天"+maintenanceOrigin+"是系统维护时间");
					message.put("isAccount", true);
					actionContext.put("errorJson", JSONObject.fromObject(message));
					return Action.ERROR;
				}
			}
			
		}
		
		//先判断session中是否有该用户的信息
		UserBean user = (UserBean) session.get(ConstantsUtil.USER_SESSION);
		//由于流只能读取一次，而且之前每个action中的params就是接收的是json的参数，
		//所以这里借助acttion中的params参数，将已经读取的流转化成json后赋值给params
		
		//System.out.println("stack.hashCode():"+stack.hashCode());
		ValueStack stack = actionContext.getValueStack();
		
		//session中缓存的用户
		if(user != null){
			stack.setValue("user", user);
			userHandler.addLastRequestTime(user.getId());
			return invocation.invoke();
		}else{
			
			//该链接是过滤掉的链接
			String actionPath = request.getRequestURI();
			System.out.println("请求的地址：" + actionPath);
			@SuppressWarnings("unchecked")
			List<String> filterUrls = systemCache.getCache("filterUrls") == null ? null : (List<String>)systemCache.getCache("filterUrls");
			if(filterUrls != null && filterUrls.size() > 0){
				for(String url: filterUrls){//遍历过滤url文件
					if(actionPath.contains(url)){  //找到需要过滤的路径
						return invocation.invoke();
					}
				}
			}
			//先从值栈中找params是否有值
			Object params = stack.findValue("params");
			//String returnErrorMeg = "您没有权限操作该地址"+actionPath;
			String returnErrorMeg = EnumUtil.getResponseValue(EnumUtil.ResponseCode.请先登录.value);
			int returnErrorCode = EnumUtil.ResponseCode.请先登录.value;
			if(params == null){
				//校验用户信息
				JSONObject jo = null;
				try {
					
					jo = HttpUtils.getJsonObjectFromInputStream(null, request);
				} catch (Exception e) {
					e.printStackTrace();
				}
				if(jo != null){		
					
					if(jo.has("id")){
						//设置为了防止过滤路径，直接在这里加载用户请求有id为默认登录用户
						try {
							user = userService.findById(JsonUtil.getIntValue(jo, "id"));
						} catch (Exception e) {
							e.printStackTrace();
						}
					}else{
						//设置为了防止过滤路径，直接在这里加载用户1为默认登录用户
						try {
							user = userService.findById(1);
						} catch (Exception e) {
							e.printStackTrace();
						}
					}
					
					//必须有免登陆验证码和账号
					if(jo.has("no_login_code") && jo.has("account")){
						
						stack.setValue("params", jo.toString());
						
						//拿到免登陆码
						String noLoginCode = JsonUtil.getStringValue(jo, "no_login_code");
						//拿到登录账户
						String account = JsonUtil.getStringValue(jo, "account");
						//UserService<UserBean> userService = new UserServiceImpl();
						//user = userService.getUserByNoLoginCode(account, noLoginCode);
						//if(user != null ){
							//获取登录用户的状态
							int status = user.getStatus();
							
							boolean canDo = false;
							
							//0:被禁止 1：正常，2、注册未激活  ，3：未完善信息 ， 4：被禁言 ，5:注销
							switch (status) {
								case ConstantsUtil.STATUS_DISABLE:
									returnErrorMeg = "账号"+user.getAccount()+"已经被禁用，有问题请联系管理员";
									returnErrorCode = EnumUtil.ResponseCode.账号已被禁用.value;
									break;
								case ConstantsUtil.STATUS_NORMAL:
									canDo = true;
									break;
								case 2:
									returnErrorMeg = "请先激活账号"+ user.getAccount();
									returnErrorCode = EnumUtil.ResponseCode.账号未被激活.value;
									break;
								case 3:
									returnErrorMeg = "请先完善账号"+ user.getAccount() +"的信息";
									returnErrorCode = EnumUtil.ResponseCode.请先完善账号信息.value;
									break;
								case 4:
									returnErrorMeg = "账号"+ user.getAccount()+"已经被禁言，有问题请联系管理员";
									returnErrorCode = EnumUtil.ResponseCode.账号已被禁言.value;
									break;
								case 5:
									returnErrorMeg = "账号"+ user.getAccount()+"已经被注销，有问题请联系管理员";
									returnErrorCode = EnumUtil.ResponseCode.账号已被注销.value;
									break;
								default:
									break;
							}
							
							userHandler.addLastRequestTime(user.getId());
							
							//当验证账号的状态是正常的情况，继续执行action
							if(canDo){
								stack.setValue("user", user);
								return invocation.invoke();	
							}
								
						//}
					}
				}
			}else{
				return invocation.invoke();	
			}
			
			message.put("isSuccess", false);
			message.put("message", returnErrorMeg);
			message.put("responseCode", returnErrorCode);
			message.put("isAccount", true);
			actionContext.put("errorJson", JSONObject.fromObject(message));
			return Action.ERROR;
		}
	}

}
