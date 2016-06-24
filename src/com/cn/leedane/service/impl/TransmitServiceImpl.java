package com.cn.leedane.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;

import net.sf.json.JSONObject;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import com.cn.leedane.Dao.TransmitDao;
import com.cn.leedane.Utils.ConstantsUtil;
import com.cn.leedane.Utils.EmojiUtil;
import com.cn.leedane.Utils.EnumUtil;
import com.cn.leedane.Utils.EnumUtil.NotificationType;
import com.cn.leedane.Utils.SensitiveWord.SensitivewordFilter;
import com.cn.leedane.Utils.JsonUtil;
import com.cn.leedane.Utils.StringUtil;
import com.cn.leedane.bean.FriendBean;
import com.cn.leedane.bean.OperateLogBean;
import com.cn.leedane.bean.TransmitBean;
import com.cn.leedane.bean.UserBean;
import com.cn.leedane.handler.CommonHandler;
import com.cn.leedane.handler.FriendHandler;
import com.cn.leedane.handler.NotificationHandler;
import com.cn.leedane.handler.TransmitHandler;
import com.cn.leedane.handler.UserHandler;
import com.cn.leedane.redis.util.RedisUtil;
import com.cn.leedane.service.FriendService;
import com.cn.leedane.service.OperateLogService;
import com.cn.leedane.service.TransmitService;
/**
 * 转发service的实现类
 * @author LeeDane
 * 2016年1月13日 上午11:36:02
 * Version 1.0
 */
public class TransmitServiceImpl extends BaseServiceImpl<TransmitBean> implements TransmitService<TransmitBean>{
	Logger logger = Logger.getLogger(getClass());
	
	private TransmitDao<TransmitBean> transmitDao;
	
	public void setTransmitDao(TransmitDao<TransmitBean> transmitDao) {
		this.transmitDao = transmitDao;
	}
	@Autowired
	private UserHandler userHandler;
	
	public void setUserHandler(UserHandler userHandler) {
		this.userHandler = userHandler;
	}
	@Autowired
	private FriendHandler friendHandler;
	
	public void setFriendHandler(FriendHandler friendHandler) {
		this.friendHandler = friendHandler;
	}
	@Autowired
	private CommonHandler commonHandler;
	
	public void setCommonHandler(CommonHandler commonHandler) {
		this.commonHandler = commonHandler;
	}
	
	@Autowired
	private TransmitHandler transmitHandler;
	
	public void setTransmitHandler(TransmitHandler transmitHandler) {
		this.transmitHandler = transmitHandler;
	}
	
	@Autowired
	private FriendService<FriendBean> friendService;
	
	public void setFriendService(FriendService<FriendBean> friendService) {
		this.friendService = friendService;
	}
	
	@Autowired
	private NotificationHandler notificationHandler;
	
	public void setNotificationHandler(NotificationHandler notificationHandler) {
		this.notificationHandler = notificationHandler;
	}
	
	@Autowired
	private OperateLogService<OperateLogBean> operateLogService;
	
	public void setOperateLogService(
			OperateLogService<OperateLogBean> operateLogService) {
		this.operateLogService = operateLogService;
	}
	private RedisUtil redisUtil = RedisUtil.getInstance();
	@Override
	public Map<String, Object> add(JSONObject jo, UserBean user,
			HttpServletRequest request) throws Exception {
		//{\"table_name\":\"t_mood\", \"table_id\":1, 'content':'转发信息'}
		logger.info("TransmitServiceImpl-->add():jsonObject=" +jo.toString() +", user=" +user.getAccount());
		String tableName = JsonUtil.getStringValue(jo, "table_name");
		int tableId = JsonUtil.getIntValue(jo, "table_id", 0);
		String content = JsonUtil.getStringValue(jo, "content");
		Map<String, Object> message = new HashMap<String, Object>();
		message.put("isSuccess", false);
				
		boolean result = false;
		
		//过滤掉emoji
		content = EmojiUtil.filterEmoji(content);
		
		if(StringUtil.isNull(tableName) || tableId < 1 || StringUtil.isNull(content)){
			message.put("message", EnumUtil.getResponseValue(EnumUtil.ResponseCode.某些参数为空.value));
			message.put("responseCode", EnumUtil.ResponseCode.某些参数为空.value);
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
		
		//检查该实体数据是否数据存在,防止对不存在的对象添加评论
		if(!recordExists(tableName, tableId)){
			message.put("message", EnumUtil.getResponseValue(EnumUtil.ResponseCode.没有操作实例.value));
			message.put("responseCode", EnumUtil.ResponseCode.没有操作实例.value);
			return message;
		}
		
		TransmitBean bean = new TransmitBean();
		bean.setCreateTime(new Date());
		bean.setCreateUser(user);
		bean.setStatus(ConstantsUtil.STATUS_NORMAL);
		bean.setTableName(tableName);
		bean.setTableId(tableId);
		bean.setContent(content);
		bean.setFroms(JsonUtil.getStringValue(jo, "froms"));
		if(transmitDao.save(bean)){
			result = true;
			int createUserId = 0;
			//String str = "{from_user_remark}转发："+content;
			createUserId = transmitDao.getObjectCreateUserId(tableName, tableId);
			if(createUserId > 0 && createUserId != user.getId()){
				Set<Integer> ids = new HashSet<Integer>();
				ids.add(createUserId);
				notificationHandler.sendNotificationByIds(false, user, ids, content, NotificationType.转发, tableName, tableId, bean);
			}
			
			//有@人通知相关人员
			Set<String> usernames = StringUtil.getAtUserName(content);
			if(usernames.size() > 0){
				//str = "{from_user_remark}在转发时候@您,点击查看详情";
				notificationHandler.sendNotificationByNames(false, user, usernames, content, NotificationType.艾特我, tableName, tableId, bean);
			}
			
		}
		String key = getTransmitKey(tableName, tableId);
		String count = null;
		//还没有添加到redis中
		if(StringUtil.isNull(redisUtil.getString(key))){
			//获取数据库中所有转发的数量
			List<Map<String, Object>> numbers = transmitDao.executeSQL("select count(id) number from t_transmit where table_name=? and table_id = ?", tableName, tableId);
			count = String.valueOf(StringUtil.changeObjectToInt(numbers.get(0).get("number")) +1);	
		}else{
			count = String.valueOf(Integer.parseInt(redisUtil.getString(key)) + 1);
		}
		redisUtil.addString(key, count);
		message.put("isSuccess", result);
		return message;
	}
	
	/**
	 * 获取redis存储的key
	 * @param tableName
	 * @param tableId
	 * @return
	 */
	private String getTransmitKey(String tableName, int tableId){
		StringBuffer buffer = new StringBuffer();
		buffer.append(ConstantsUtil.TRANSMIT_REDIS);
		buffer.append(tableName);
		buffer.append("_");
		buffer.append(tableId);
		return buffer.toString();
	}

	@Override
	public Map<String, Object> deleteTransmit(JSONObject jo, UserBean user,
			HttpServletRequest request) {
		logger.info("TransmitServiceImpl-->deleteTransmit():jsonObject=" +jo.toString() +", user=" +user.getAccount());
		int tid = JsonUtil.getIntValue(jo, "tid");
		int createUserId = JsonUtil.getIntValue(jo, "create_user_id");
		
		Map<String, Object> message = new HashMap<String, Object>();
		message.put("isSuccess", false);
		
		//非登录用户不能删除操作
		if(createUserId != user.getId()){
			message.put("message", EnumUtil.getResponseValue(EnumUtil.ResponseCode.请先登录.value));
			message.put("responseCode", EnumUtil.ResponseCode.请先登录.value);
			return message;
		}
		
		if(tid < 1){
			message.put("message", EnumUtil.getResponseValue(EnumUtil.ResponseCode.没有操作实例.value));
			message.put("responseCode", EnumUtil.ResponseCode.没有操作实例.value);
			return message;
		}
		
		boolean result = false;
		TransmitBean transmitBean = transmitDao.findById(tid);
		if(transmitBean != null && transmitBean.getCreateUser().getId() == createUserId){
			result = transmitDao.delete(transmitBean);
		}else{
			message.put("message", EnumUtil.getResponseValue(EnumUtil.ResponseCode.操作对象不存在.value));
			message.put("responseCode", EnumUtil.ResponseCode.操作对象不存在.value);
		}
		
		if(result){
			//保存操作日志
			operateLogService.saveOperateLog(user, request, null, StringUtil.getStringBufferStr(user.getAccount(),"删除转发ID为", tid, "的数据", StringUtil.getSuccessOrNoStr(result)).toString(), "deleteTransmit()", ConstantsUtil.STATUS_NORMAL, 0);
			transmitHandler.deleteTransmit(transmitBean.getTableId(), transmitBean.getTableName());
			message.put("isSuccess", true);
		}			
		
		return message;
	}

	@Override
	public List<Map<String, Object>> getLimit(JSONObject jo, UserBean user,
			HttpServletRequest request) {
		logger.info("TransmitServiceImpl-->getLimit():jsonObject=" +jo.toString() +", user=" +user.getAccount());
		int toUserId = JsonUtil.getIntValue(jo, "toUserId"); //操作的对象用户的id，如获取指定心情的转发数，这个为0
		String tableName = JsonUtil.getStringValue(jo, "table_name");
		int tableId = JsonUtil.getIntValue(jo, "table_id", 0);
		String method = JsonUtil.getStringValue(jo, "method", "firstloading"); //操作方式
		int pageSize = JsonUtil.getIntValue(jo, "pageSize", ConstantsUtil.DEFAULT_PAGE_SIZE); //每页的大小
		int lastId = JsonUtil.getIntValue(jo, "last_id"); //开始的页数
		int firstId = JsonUtil.getIntValue(jo, "first_id"); //结束的页数
		boolean showUserInfo = JsonUtil.getBooleanValue(jo, "showUserInfo");
		/*if(userId < 1)
			return new ArrayList<Map<String,Object>>();*/
		StringBuffer sql = new StringBuffer();
		List<Map<String, Object>> rs = new ArrayList<Map<String,Object>>();

		//查找该用户所有的转发
		if(StringUtil.isNull(tableName) && toUserId > 0 && tableId < 1){		
			if("firstloading".equalsIgnoreCase(method)){
				sql.append("select t.id, t.froms, t.content, t.table_name, t.table_id, t.create_user_id, u.account, date_format(t.create_time,'%Y-%c-%d %H:%i:%s') create_time ");
				sql.append(" from t_transmit t inner join t_user u on u.id = t.create_user_id where t.create_user_id = ? and t.status = ? ");
				sql.append(" order by t.id desc limit 0,?");
				rs = transmitDao.executeSQL(sql.toString(), toUserId, ConstantsUtil.STATUS_NORMAL, pageSize);
			//下刷新
			}else if("lowloading".equalsIgnoreCase(method)){
				sql.append("select t.id, t.froms, t.content, t.table_name, t.table_id, t.create_user_id, u.account, date_format(t.create_time,'%Y-%c-%d %H:%i:%s') create_time ");
				sql.append(" from t_transmit t inner join t_user u on u.id = t.create_user_id where t.create_user_id = ? and t.status = ? ");
				sql.append(" and t.id < ? order by t.id desc limit 0,? ");
				rs = transmitDao.executeSQL(sql.toString(), toUserId, ConstantsUtil.STATUS_NORMAL, lastId, pageSize);
			//上刷新
			}else if("uploading".equalsIgnoreCase(method)){
				sql.append("select t.id, t.froms, t.content, t.table_name, t.table_id, t.create_user_id, u.account, date_format(t.create_time,'%Y-%c-%d %H:%i:%s') create_time ");
				sql.append(" from t_transmit t inner join t_user u on u.id = t.create_user_id where t.create_user_id = ? and t.status = ? ");
				sql.append(" and t.id > ? limit 0,?  ");
				rs = transmitDao.executeSQL(sql.toString() , toUserId, ConstantsUtil.STATUS_NORMAL, firstId, pageSize);
			}
		}
		
		//查找指定表的数据
		if(StringUtil.isNotNull(tableName) && toUserId < 1 && tableId > 0){
			if("firstloading".equalsIgnoreCase(method)){
				sql.append("select t.id, t.froms, t.content, t.table_name, t.table_id, t.create_user_id, u.account, date_format(t.create_time,'%Y-%c-%d %H:%i:%s') create_time ");
				sql.append(" from t_transmit t inner join t_user u on u.id = t.create_user_id where  t.status = ? and t.table_name = ? and t.table_id = ? ");
				sql.append(" order by t.id desc limit 0,?");
				rs = transmitDao.executeSQL(sql.toString(), ConstantsUtil.STATUS_NORMAL, tableName, tableId, pageSize);
			//下刷新
			}else if("lowloading".equalsIgnoreCase(method)){
				sql.append("select t.id, t.froms, t.content, t.table_name, t.table_id, t.create_user_id, u.account, date_format(t.create_time,'%Y-%c-%d %H:%i:%s') create_time ");
				sql.append(" from t_transmit t inner join t_user u on u.id = t.create_user_id where t.status = ? and t.table_name = ? and t.table_id = ?");
				sql.append(" and t.id < ? order by t.id desc limit 0,? ");
				rs = transmitDao.executeSQL(sql.toString(), ConstantsUtil.STATUS_NORMAL, tableName, tableId, lastId, pageSize);
			//上刷新
			}else if("uploading".equalsIgnoreCase(method)){
				sql.append("select t.id, t.froms, t.content, t.table_name, t.table_id, t.create_user_id, u.account, date_format(t.create_time,'%Y-%c-%d %H:%i:%s') create_time ");
				sql.append(" from t_transmit t inner join t_user u on u.id = t.create_user_id where t.status = ? and t.table_name = ? and t.table_id = ?");
				sql.append(" and t.id > ? limit 0,?  ");
				rs = transmitDao.executeSQL(sql.toString() , ConstantsUtil.STATUS_NORMAL, tableName, tableId, firstId, pageSize);
			}
		}
		if(rs.size() >0){
			int createUserId = 0;
			JSONObject friendObject = friendHandler.getFromToFriends(user.getId());
			String tabName;
			int tabId;
			for(int i=0; i < rs.size(); i++){
				if(StringUtil.isNull(tableName) && tableId <1){
					//在非获取指定表下的转发列表的情况下的前面35个字符
					tabName = StringUtil.changeNotNull((rs.get(i).get("table_name")));
					tabId = StringUtil.changeObjectToInt(rs.get(i).get("table_id"));
					rs.get(i).put("source", commonHandler.getContentByTableNameAndId(tabName, tabId, user));
				}
				if(showUserInfo){
					createUserId = StringUtil.changeObjectToInt(rs.get(i).get("create_user_id"));
					rs.get(i).putAll(userHandler.getBaseUserInfo(createUserId, user, friendObject));
				}
			}
		}
		
		//保存操作日志
		operateLogService.saveOperateLog(user, request, null, StringUtil.getStringBufferStr(user.getAccount(),"获取用户ID为：",toUserId,",表名：",tableName,"，表id为：",tableId,"的转发列表").toString(), "getLimit()", ConstantsUtil.STATUS_NORMAL, 0);
						
		
		return rs;
	}

	@Override
	public int getTotalTransmits(int userId) {
		return this.transmitDao.getTotalTransmits(userId);
	}
	
}
