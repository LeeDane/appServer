package com.cn.leedane.service.impl;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import net.sf.json.JSONObject;

import org.apache.log4j.Logger;

import com.cn.leedane.Dao.PrivateChatDao;
import com.cn.leedane.Utils.ConstantsUtil;
import com.cn.leedane.Utils.DateUtil;
import com.cn.leedane.Utils.EnumUtil;
import com.cn.leedane.Utils.EnumUtil.DataTableType;
import com.cn.leedane.Utils.EnumUtil.NotificationType;
import com.cn.leedane.Utils.FilterUtil;
import com.cn.leedane.Utils.JsonUtil;
import com.cn.leedane.Utils.StringUtil;
import com.cn.leedane.bean.OperateLogBean;
import com.cn.leedane.bean.PrivateChatBean;
import com.cn.leedane.bean.UserBean;
import com.cn.leedane.handler.NotificationHandler;
import com.cn.leedane.handler.UserHandler;
import com.cn.leedane.service.OperateLogService;
import com.cn.leedane.service.PrivateChatService;

/**
 * 私信信息列表service实现类
 * @author LeeDane
 * 2016年6月30日 上午11:22:53
 * Version 1.0
 */
public class PrivateChatServiceImpl extends BaseServiceImpl<PrivateChatBean> implements PrivateChatService<PrivateChatBean> {
	Logger logger = Logger.getLogger(getClass());
	@Resource
	private PrivateChatDao<PrivateChatBean> privateChatDao;
	
	public void setPrivateChatDao(PrivateChatDao<PrivateChatBean> privateChatDao) {
		this.privateChatDao = privateChatDao;
	}
	
	@Resource
	private OperateLogService<OperateLogBean> operateLogService;
	
	public void setOperateLogService(
			OperateLogService<OperateLogBean> operateLogService) {
		this.operateLogService = operateLogService;
	}
	
	@Resource
	private UserHandler userHandler;
	
	public void setUserHandler(UserHandler userHandler) {
		this.userHandler = userHandler;
	}
	
	@Resource
	private NotificationHandler notificationHandler;
	
	public void setNotificationHandler(NotificationHandler notificationHandler) {
		this.notificationHandler = notificationHandler;
	}
	
	@Override
	public Map<String, Object> getLimit(JSONObject jo, UserBean user,
			HttpServletRequest request) {
		logger.info("ChatServiceImpl-->getLimit():jo="+jo.toString());
		Map<String, Object> message = new HashMap<String, Object>();
		message.put("isSuccess", false);
		return message;
	}


	@Override
	public Map<String, Object> send(JSONObject jo, UserBean user,
			HttpServletRequest request) {
		logger.info("PrivateChatServiceImpl-->send():jo="+jo.toString());
		Map<String, Object> message = new HashMap<String, Object>();
		message.put("isSuccess", false);
		int toUserId = JsonUtil.getIntValue(jo, "to_user_id"); //发送给对方的用户ID
		if(toUserId < 1 || user.getId() == toUserId){
			message.put("message", EnumUtil.getResponseValue(EnumUtil.ResponseCode.用户不存在或请求参数不对.value));
			message.put("responseCode", EnumUtil.ResponseCode.用户不存在或请求参数不对.value);
			return message;
		}
		String content = JsonUtil.getStringValue(jo, "content"); //私信内容
		
		//进行敏感词过滤和emoji过滤
		if(FilterUtil.filter(content, message))
			return message;
		
		if(StringUtil.isNull(content)){
			message.put("message", EnumUtil.getResponseValue(EnumUtil.ResponseCode.私信内容不能为空.value));
			message.put("responseCode", EnumUtil.ResponseCode.私信内容不能为空.value);
			return message;
		}
			
		int type = JsonUtil.getIntValue(jo, "type", 0); //私信类型
		PrivateChatBean privateChatBean = new PrivateChatBean();
		privateChatBean.setContent(content);
		privateChatBean.setCreateTime(new Date());
		privateChatBean.setCreateUser(user);
		privateChatBean.setStatus(ConstantsUtil.STATUS_NORMAL);
		privateChatBean.setToUserId(toUserId);
		privateChatBean.setType(type);
		
		boolean result = privateChatDao.save(privateChatBean);
			
		if(result){
			
			Map<String, Object> chatMap = privateChatBeanToMap(privateChatBean);
			Set<Integer> ids = new HashSet<Integer>();
			ids.add(toUserId);
			//给对方发送通知
			notificationHandler.sendNotificationByIds(false, user, ids, content, NotificationType.私信, DataTableType.私信.value, privateChatBean.getId(), null);
			
			message.put("isSuccess", result);
			message.put("message", chatMap);
		}else{
			message.put("message", EnumUtil.getResponseValue(EnumUtil.ResponseCode.服务器处理异常.value));
			message.put("responseCode", EnumUtil.ResponseCode.服务器处理异常.value);
		}
		
		//保存操作日志
		operateLogService.saveOperateLog(user, request, null, StringUtil.getStringBufferStr(user.getAccount(),"给用户Id为：", toUserId, "发送私信，内容是：" , content, StringUtil.getSuccessOrNoStr(result)).toString(), "send()", ConstantsUtil.STATUS_NORMAL, 0);
					
		return message;
	}
	
	/**
	 * 将chatbean转化成map集合存储
	 * @param chatBean
	 * @return
	 */
	public static Map<String, Object> privateChatBeanToMap(PrivateChatBean privateChatBean){
		Map<String, Object> chat = new HashMap<String, Object>();
		chat.put("id", privateChatBean.getId());
		chat.put("create_user_id", privateChatBean.getCreateUser().getId());
		chat.put("to_user_id", privateChatBean.getToUserId());
		chat.put("create_time", DateUtil.DateToString(privateChatBean.getCreateTime()));
		chat.put("type", privateChatBean.getType());
		chat.put("content", privateChatBean.getContent());
		return chat;
	}
}
