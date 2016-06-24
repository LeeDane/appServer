package com.cn.leedane.service.impl;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import net.sf.json.JSONObject;

import org.apache.log4j.Logger;

import com.cn.leedane.Dao.ChatDao;
import com.cn.leedane.Utils.ConstantsUtil;
import com.cn.leedane.Utils.DateUtil;
import com.cn.leedane.Utils.EmojiUtil;
import com.cn.leedane.Utils.EnumUtil;
import com.cn.leedane.Utils.JsonUtil;
import com.cn.leedane.Utils.StringUtil;
import com.cn.leedane.Utils.SensitiveWord.SensitivewordFilter;
import com.cn.leedane.bean.ChatBean;
import com.cn.leedane.bean.OperateLogBean;
import com.cn.leedane.bean.UserBean;
import com.cn.leedane.handler.NotificationHandler;
import com.cn.leedane.handler.UserHandler;
import com.cn.leedane.service.ChatService;
import com.cn.leedane.service.OperateLogService;

/**
 * 聊天信息列表service实现类
 * @author LeeDane
 * 2016年5月5日 下午11:59:29
 * Version 1.0
 */
public class ChatServiceImpl extends BaseServiceImpl<ChatBean> implements ChatService<ChatBean> {
	Logger logger = Logger.getLogger(getClass());
	@Resource
	private ChatDao<ChatBean> chatDao;
	
	public void setChatDao(ChatDao<ChatBean> chatDao) {
		this.chatDao = chatDao;
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
		int toUserId = JsonUtil.getIntValue(jo, "toUserId"); //发送给对方的用户ID
		if(toUserId < 1 || user.getId() == toUserId){
			message.put("message", EnumUtil.getResponseValue(EnumUtil.ResponseCode.没有操作权限.value));
			message.put("responseCode", EnumUtil.ResponseCode.没有操作权限.value);
			return message;
		}
		int lastId = JsonUtil.getIntValue(jo, "last_id", 0); //开始的页数
		int firstId = JsonUtil.getIntValue(jo, "first_id"); //开始的页数
		String method = JsonUtil.getStringValue(jo, "method", "firstloading"); //操作方式
		int pageSize = JsonUtil.getIntValue(jo, "pageSize", ConstantsUtil.DEFAULT_PAGE_SIZE); //每页的大小
		
		StringBuffer sql = new StringBuffer();
		List<Map<String, Object>> rs = new ArrayList<>();
		
		if(method.equalsIgnoreCase("uploading") && firstId < 1){
			message.put("isSuccess", true);
			message.put("message", rs);
			return message;
		}
	
		if("firstloading".equalsIgnoreCase(method)){
			sql.append("select c.id, c.create_user_id, c.to_user_id, date_format(c.create_time,'%Y-%c-%d %H:%i:%s') create_time, c.type, c.content");
			sql.append(" from t_chat c where ((c.create_user_id = ? and c.to_user_id =?) or (c.to_user_id =? and c.create_user_id = ?))");
			sql.append(" order by c.id desc limit 0,?");
			rs = chatDao.executeSQL(sql.toString(), user.getId(), toUserId,  user.getId(), toUserId,pageSize);
		//上刷新
		}else if("uploading".equalsIgnoreCase(method)){
			sql.append("select c.id, c.create_user_id, c.to_user_id , date_format(c.create_time,'%Y-%c-%d %H:%i:%s') create_time, c.type, c.content");
			sql.append(" from t_chat c where ((c.create_user_id = ? and c.to_user_id =?) or (c.to_user_id =? and c.create_user_id = ?))");
			sql.append(" and c.id < ? order by c.id desc limit 0,? ");
			rs = chatDao.executeSQL(sql.toString(), user.getId(), toUserId, user.getId(), toUserId, firstId, pageSize);
		}else if("lowloading".equalsIgnoreCase(method)){
			sql.append("select c.id, c.create_user_id, c.to_user_id , date_format(c.create_time,'%Y-%c-%d %H:%i:%s') create_time, c.type, c.content");
			sql.append(" from t_chat c where ((c.create_user_id = ? and c.to_user_id =?) or (c.to_user_id =? and c.create_user_id = ?))");
			sql.append(" and c.id > ? order by c.id desc limit 0,? ");
			rs = chatDao.executeSQL(sql.toString(), user.getId(), toUserId, user.getId(), toUserId, lastId, pageSize);
		}
		//保存操作日志
		operateLogService.saveOperateLog(user, request, null, StringUtil.getStringBufferStr(user.getAccount(),"获取聊天分页列表").toString(), "getLimit()", ConstantsUtil.STATUS_NORMAL, 0);
			
		message.put("isSuccess", true);
		message.put("message", rs);
		return message;
	}

	@Override
	public Map<String, Object> send(JSONObject jo, UserBean user,
			HttpServletRequest request) {
		logger.info("ChatServiceImpl-->send():jo="+jo.toString());
		Map<String, Object> message = new HashMap<String, Object>();
		message.put("isSuccess", false);
		int toUserId = JsonUtil.getIntValue(jo, "toUserId"); //发送给对方的用户ID
		if(toUserId < 1 || user.getId() == toUserId){
			message.put("message", EnumUtil.getResponseValue(EnumUtil.ResponseCode.没有操作权限.value));
			message.put("responseCode", EnumUtil.ResponseCode.没有操作权限.value);
			return message;
		}
		String content = JsonUtil.getStringValue(jo, "content"); //聊天内容
		
		//过滤掉emoji
		content = EmojiUtil.filterEmoji(content);
		if(StringUtil.isNull(content)){
			message.put("message", EnumUtil.getResponseValue(EnumUtil.ResponseCode.聊天内容不能为空.value));
			message.put("responseCode", EnumUtil.ResponseCode.聊天内容不能为空.value);
			return message;
		}
			
		//检测敏感词
		SensitivewordFilter filter = new SensitivewordFilter();
		long beginTime = System.currentTimeMillis();
		Set<String> set = filter.getSensitiveWord(content, 1);
		if(set.size() > 0){
			message.put("message", "有敏感词"+set.size()+"个："+set.toString());
			message.put("responseCode", EnumUtil.ResponseCode.系统检测到有敏感词.value);
			long endTime = System.currentTimeMillis();
			System.out.println("总共消耗时间为：" + (endTime - beginTime));
			return message;
		}
		
		int type = JsonUtil.getIntValue(jo, "type", 0); //聊天类型
		ChatBean chatBean = new ChatBean();
		chatBean.setContent(content);
		chatBean.setCreateTime(new Date());
		chatBean.setCreateUser(user);
		chatBean.setStatus(ConstantsUtil.STATUS_NORMAL);
		chatBean.setToUserId(toUserId);
		chatBean.setType(type);
		
		boolean result = chatDao.save(chatBean);
		//保存操作日志
		operateLogService.saveOperateLog(user, request, null, StringUtil.getStringBufferStr(user.getAccount(),"给用户ID为：", toUserId, "发送聊天信息", StringUtil.getSuccessOrNoStr(result)).toString(), "send()", ConstantsUtil.STATUS_NORMAL, 0);
		if(result){
			
			Map<String, Object> chatMap = chatBeanToMap(chatBean);
			//给对方发送通知
			notificationHandler.sendCustomMessageById(user, toUserId, chatMap);
			
			message.put("isSuccess", result);
			message.put("message", chatMap);
		}else{
			message.put("message", EnumUtil.getResponseValue(EnumUtil.ResponseCode.服务器处理异常.value));
			message.put("responseCode", EnumUtil.ResponseCode.服务器处理异常.value);
		}
		return message;
	}
	
	/**
	 * 将chatbean转化成map集合存储
	 * @param chatBean
	 * @return
	 */
	public static Map<String, Object> chatBeanToMap(ChatBean chatBean){
		Map<String, Object> chat = new HashMap<String, Object>();
		chat.put("id", chatBean.getId());
		chat.put("create_user_id", chatBean.getCreateUser().getId());
		chat.put("to_user_id", chatBean.getToUserId());
		chat.put("create_time", DateUtil.DateToString(chatBean.getCreateTime()));
		chat.put("type", chatBean.getType());
		chat.put("content", chatBean.getContent());
		return chat;
	}
}
