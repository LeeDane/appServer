package com.cn.leedane.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import net.sf.json.JSONObject;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import com.cn.leedane.Dao.NotificationDao;
import com.cn.leedane.Utils.ConstantsUtil;
import com.cn.leedane.Utils.EnumUtil;
import com.cn.leedane.Utils.JsonUtil;
import com.cn.leedane.Utils.StringUtil;
import com.cn.leedane.bean.NotificationBean;
import com.cn.leedane.bean.OperateLogBean;
import com.cn.leedane.bean.UserBean;
import com.cn.leedane.handler.CommonHandler;
import com.cn.leedane.handler.NotificationHandler;
import com.cn.leedane.handler.UserHandler;
import com.cn.leedane.log.LogAnnotation;
import com.cn.leedane.service.NotificationService;
import com.cn.leedane.service.OperateLogService;
/**
 * 通知service的实现类
 * @author LeeDane
 * 2015年11月30日 下午3:32:07
 * Version 1.0
 */
public class NotificationServiceImpl extends BaseServiceImpl<NotificationBean> implements NotificationService<NotificationBean>{
	Logger logger = Logger.getLogger(getClass());
	@Autowired
	private NotificationDao<NotificationBean> notificationDao;
	
	public void setNotificationDao(
			NotificationDao<NotificationBean> notificationDao) {
		this.notificationDao = notificationDao;
	}
	
	@Autowired
	private UserHandler userHandler;
	
	public void setUserHandler(UserHandler userHandler) {
		this.userHandler = userHandler;
	}
	
	@Autowired
	private CommonHandler commonHandler;
	
	public void setCommonHandler(CommonHandler commonHandler) {
		this.commonHandler = commonHandler;
	}
	
	@Autowired
	private OperateLogService<OperateLogBean> operateLogService;
	
	public void setOperateLogService(
			OperateLogService<OperateLogBean> operateLogService) {
		this.operateLogService = operateLogService;
	}
	
	@Autowired
	private NotificationHandler notificationHandler;
	
	public void setNotificationHandler(NotificationHandler notificationHandler) {
		this.notificationHandler = notificationHandler;
	}
	
	@LogAnnotation(moduleName="通知管理",option="发送通知")
	@Override
	public boolean save(NotificationBean t) {
		return super.save(t);
	}

	@Override
	public Map<String, Object> getLimit(JSONObject jo, UserBean user,
			HttpServletRequest request) {
		logger.info("NotificationServiceImpl-->getLimit():jsonObject=" +jo.toString() +", user=" +user.getAccount());
		String type = JsonUtil.getStringValue(jo, "type"); //通知类型
		String method = JsonUtil.getStringValue(jo, "method", "firstloading"); //操作方式
		int pageSize = JsonUtil.getIntValue(jo, "pageSize", ConstantsUtil.DEFAULT_PAGE_SIZE); //每页的大小
		int lastId = JsonUtil.getIntValue(jo, "last_id"); //开始的页数
		int firstId = JsonUtil.getIntValue(jo, "first_id"); //结束的页数
		
		Map<String, Object> message = new HashMap<String, Object>();
		message.put("isSuccess", false);
		if(StringUtil.isNull(type)){
			message.put("message", EnumUtil.getResponseValue(EnumUtil.ResponseCode.该通知类型不存在.value));
			message.put("responseCode", EnumUtil.ResponseCode.该通知类型不存在.value);
			return message;
		}
		StringBuffer sql = new StringBuffer();
		List<Map<String, Object>> rs = new ArrayList<Map<String,Object>>();
		//分页查找该用户的通知列表
		if("firstloading".equalsIgnoreCase(method)){
			sql.append("select n.id, n.from_user_id, n.to_user_id, n.content, n.type, n.extra, n.create_time, n.table_name, n.table_id, n.is_push_error, n.is_read");
			sql.append(" from t_notification n where n.to_user_id = ? and n.status = ? and n.type=?");
			sql.append(" order by n.id desc limit 0,?");
			rs = notificationDao.executeSQL(sql.toString(), user.getId(), ConstantsUtil.STATUS_NORMAL, type, pageSize);
		//下刷新
		}else if("lowloading".equalsIgnoreCase(method)){
			sql.append("select n.id, n.from_user_id, n.to_user_id, n.content, n.type, n.extra, n.create_time, n.table_name, n.table_id, n.is_push_error, n.is_read");
			sql.append(" from t_notification n where n.to_user_id = ? and n.status = ? and n.type=?");
			sql.append(" and n.id < ? order by n.id desc limit 0,? ");
			rs = notificationDao.executeSQL(sql.toString(), user.getId(), ConstantsUtil.STATUS_NORMAL, type, lastId, pageSize);
		//上刷新
		}else if("uploading".equalsIgnoreCase(method)){
			sql.append("select n.id, n.from_user_id, n.to_user_id, n.content, n.type, n.extra, n.create_time, n.table_name, n.table_id, n.is_push_error, n.is_read");
			sql.append(" from t_notification n where n.to_user_id = ? and n.status = ? and n.type=?");
			sql.append(" and n.id > ? limit 0,? ");
			rs = notificationDao.executeSQL(sql.toString(), user.getId(), ConstantsUtil.STATUS_NORMAL, type, firstId, pageSize);
		}
		
		if(rs !=null && rs.size() > 0){
			int fromUserId = 0;
			String tableName = null;
			int tableId = 0;
			//为名字备注赋值
			for(int i = 0; i < rs.size(); i++){
				//在非获取指定表下的评论列表的情况下的前面35个字符
				tableName = StringUtil.changeNotNull((rs.get(i).get("table_name")));
				tableId = StringUtil.changeObjectToInt(rs.get(i).get("table_id"));
				rs.get(i).put("source", commonHandler.getContentByTableNameAndId(tableName, tableId, user));
			
				fromUserId = StringUtil.changeObjectToInt(rs.get(i).get("from_user_id"));
				rs.get(i).putAll(userHandler.getBaseUserInfo(fromUserId));
			}	
		}
		
		//保存操作日志
		operateLogService.saveOperateLog(user, request, null, StringUtil.getStringBufferStr(user.getAccount(),"获取收到的通知列表").toString(), "getLimit()", ConstantsUtil.STATUS_NORMAL, 0);
		message.put("isSuccess", true);
		message.put("message", rs);
		return message;
	}

	@Override
	public Map<String, Object> sendBroadcast(JSONObject jo, UserBean user,
			HttpServletRequest request) {
		logger.info("NotificationServiceImpl-->sendBroadcast():jsonObject=" +jo.toString() +", user=" +user.getAccount());
		Map<String, Object> message = new HashMap<String, Object>();
		message.put("isSuccess", false);
		String broadcast = JsonUtil.getStringValue(jo, "broadcast");
		if(StringUtil.isNull(broadcast)){
			message.put("message", EnumUtil.getResponseValue(EnumUtil.ResponseCode.某些参数为空.value));
			message.put("responseCode", EnumUtil.ResponseCode.某些参数为空.value);
			return message;
		}
		
		boolean result = notificationHandler.sendBroadcast(broadcast);
		
		//保存操作日志
		operateLogService.saveOperateLog(user, request, null, StringUtil.getStringBufferStr(user.getAccount(),"向所有在线用户发送通知", StringUtil.getSuccessOrNoStr(result)).toString(), "sendBroadcast()", ConstantsUtil.STATUS_NORMAL, 0);
		message.put("isSuccess", result);
		return message;
	}
	
	@Override
	public Map<String, Object> deleteNotification(JSONObject jo, UserBean user,
			HttpServletRequest request) {
		logger.info("NotificationServiceImpl-->deleteNotification():jsonObject=" +jo.toString() +", user=" +user.getAccount());
		Map<String, Object> message = new HashMap<String, Object>();
		message.put("isSuccess", false);
		int nid = JsonUtil.getIntValue(jo, "nid");
		if(nid < 1){
			message.put("message", EnumUtil.getResponseValue(EnumUtil.ResponseCode.操作对象不存在.value));
			message.put("responseCode", EnumUtil.ResponseCode.操作对象不存在.value);
			return message;
		}
		NotificationBean notificationBean = notificationDao.findById(nid);
		
		boolean result = false;
		if(notificationBean != null){
			result = notificationDao.delete(notificationBean);
			if(result){
				message.put("message", EnumUtil.getResponseValue(EnumUtil.ResponseCode.删除通知成功.value));
			}
		}else{
			message.put("message", EnumUtil.getResponseValue(EnumUtil.ResponseCode.删除通知失败.value));
			message.put("responseCode", EnumUtil.ResponseCode.删除通知失败.value);
			return message;
		}
		
		//保存操作日志
		operateLogService.saveOperateLog(user, request, null, StringUtil.getStringBufferStr(user.getAccount(),"刪除通知Id为：", nid , StringUtil.getSuccessOrNoStr(result)).toString(), "deleteNotification()", ConstantsUtil.STATUS_NORMAL, 0);
		message.put("isSuccess", result);
		return message;
	}
}
