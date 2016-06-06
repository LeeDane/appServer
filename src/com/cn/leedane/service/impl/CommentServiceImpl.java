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

import com.cn.leedane.Dao.CommentDao;
import com.cn.leedane.Exception.ErrorException;
import com.cn.leedane.Utils.ConstantsUtil;
import com.cn.leedane.Utils.EnumUtil;
import com.cn.leedane.Utils.EnumUtil.NotificationType;
import com.cn.leedane.Utils.SensitiveWord.SensitivewordFilter;
import com.cn.leedane.Utils.JsonUtil;
import com.cn.leedane.Utils.StringUtil;
import com.cn.leedane.bean.CommentBean;
import com.cn.leedane.bean.FriendBean;
import com.cn.leedane.bean.NotificationBean;
import com.cn.leedane.bean.OperateLogBean;
import com.cn.leedane.bean.UserBean;
import com.cn.leedane.handler.CommentHandler;
import com.cn.leedane.handler.CommonHandler;
import com.cn.leedane.handler.FriendHandler;
import com.cn.leedane.handler.NotificationHandler;
import com.cn.leedane.handler.UserHandler;
import com.cn.leedane.service.CommentService;
import com.cn.leedane.service.FriendService;
import com.cn.leedane.service.NotificationService;
import com.cn.leedane.service.OperateLogService;
/**
 * 评论service的实现类
 * @author LeeDane
 * 2015年12月16日 上午10:59:11
 * Version 1.0
 */
public class CommentServiceImpl extends BaseServiceImpl<CommentBean> implements CommentService<CommentBean>{
	Logger logger = Logger.getLogger(getClass());
	@Autowired
	private CommentDao<CommentBean> commentDao;
	@Autowired
	private FriendService<FriendBean> friendService;
	
	@Autowired
	private NotificationService<NotificationBean> notificationService;
	
	public void setCommentDao(CommentDao<CommentBean> commentDao) {
		this.commentDao = commentDao;
	}
	
	public void setFriendService(FriendService<FriendBean> friendService) {
		this.friendService = friendService;
	}
	
	public void setNotificationService(
			NotificationService<NotificationBean> notificationService) {
		this.notificationService = notificationService;
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
	private CommentHandler commentHandler;
	
	public void setCommentHandler(CommentHandler commentHandler) {
		this.commentHandler = commentHandler;
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

	@Override
	public Map<String, Object> add(JSONObject jo, UserBean user,
			HttpServletRequest request){
		// {\"table_name\":\"t_mood\", \"table_id\":123
		//, \"content\":\"我同意\" ,\"level\": 1, \"pid\":23, \"from\":\"Android客户端\"}
		logger.info("CommentServiceImpl-->add():jsonObject=" +jo.toString() +", user=" +user.getAccount());
		String tableName = JsonUtil.getStringValue(jo, "table_name");
		int tableId = JsonUtil.getIntValue(jo, "table_id", 0);
		String content = JsonUtil.getStringValue(jo, "content");
		String from = JsonUtil.getStringValue(jo, "froms");
		Map<String, Object> message = new HashMap<String, Object>();
		message.put("isSuccess", false);
		
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
		
		if(StringUtil.isNull(tableName) || tableId < 1 || StringUtil.isNull(content)){
			message.put("message", EnumUtil.getResponseValue(EnumUtil.ResponseCode.某些参数为空.value));
			message.put("responseCode", EnumUtil.ResponseCode.某些参数为空.value);
			return message;
		}
		
		//检查该实体数据是否数据存在,防止对不存在的对象添加评论
		if(!recordExists(tableName, tableId)){
			message.put("message", EnumUtil.getResponseValue(EnumUtil.ResponseCode.没有操作实例.value));
			message.put("responseCode", EnumUtil.ResponseCode.没有操作实例.value);
			return message;
		}
		
		int level = JsonUtil.getIntValue(jo, "level", 5);
		int pid = JsonUtil.getIntValue(jo, "pid", 0);
		CommentBean bean = new CommentBean();
		bean.setCommentLevel(level);
		bean.setContent(content);
		bean.setCreateTime(new Date());
		bean.setCreateUser(user);
		bean.setPid(pid);
		bean.setFroms(from);
		bean.setStatus(ConstantsUtil.STATUS_NORMAL);
		bean.setTableId(tableId);
		bean.setTableName(tableName);
		//bean.setCid(cid);
		
		if(commentDao.save(bean)){
			int createUserId = 0;
			//String str = "{from_user_remark}评论您："+content;
			if(pid > 0){//说明是回复别人的评论
				createUserId = getCommentCreateUserId(pid);			
			}else{ //说明评论的是某一个对象
				createUserId = commentDao.getObjectCreateUserId(tableName, tableId);
				
			}
			if(createUserId > 0 && createUserId != user.getId()){
				Set<Integer> ids = new HashSet<Integer>();
				ids.add(createUserId);
				notificationHandler.sendNotificationByIds(false, user, ids, content, NotificationType.评论, tableName, tableId, bean);
			}
			//当一个人既有评论也有@时候，@不做处理
			//有@人通知相关人员
			Set<String> usernames = StringUtil.getAtUserName(content);
			if(usernames.size() > 0){
				if(pid > 0){
					String createUserName = userHandler.getUserName(createUserId);
					if(StringUtil.isNotNull(createUserName)){
						usernames.remove(createUserName);
					}
				}
				if(usernames.size() > 0){
					//str = "{from_user_remark}在评论时候@您,点击查看详情";
					notificationHandler.sendNotificationByNames(false, user, usernames, content, NotificationType.艾特我, tableName, tableId, bean);
				}
			}
			
			/**
			 * 通过表名+ID唯一存储
			 */
			commentHandler.addComment(tableName, tableId);
			/*RedisUtil redisUtil = RedisUtil.getInstance();
			String key = getCommentKey(tableName, tableId);
			String count = null;
			//还没有添加到redis中
			if(StringUtil.isNull(redisUtil.getString(key))){
				//获取数据库中所有评论的数量
				List<Map<String, Object>> numbers = commentDao.executeSQL("select count(id) number from t_comment where table_name=? and table_id = ?", tableName, tableId);
				count = String.valueOf(StringUtil.changeObjectToInt(numbers.get(0).get("number")) +1);	
			}else{
				count = String.valueOf(Integer.parseInt(redisUtil.getString(key)) + 1);
			}
			redisUtil.addString(key, count);*/
		}
		message.put("isSuccess", true);
		return message;
	}
	
	/**
	 * 根据评论的ID获取评论的创建人ID
	 * @param commentId
	 * @return
	 */
	private int getCommentCreateUserId(int commentId){
		int createUserId = 0;
		List<Map<String, Object>> list = commentDao.executeSQL("select create_user_id from t_comment where status=? and id=? limit 1", ConstantsUtil.STATUS_NORMAL, commentId);
		if(list != null && list.size() == 1){
			createUserId = StringUtil.changeObjectToInt(list.get(0).get("create_user_id"));
		}
		return createUserId;
	}

	@Override
	public List<Map<String, Object>> getCommentsByLimit(JSONObject jo,
			UserBean user, HttpServletRequest request) throws Exception {
		logger.info("CommentServiceImpl-->getCommentByLimit():jsonObject=" +jo.toString() +", user=" +user.getAccount());
		 //{\"uid\":2,\"table_name\":\"t_mood\", \"table_id\":123,\"pageSize\":5
		//, \"first_id\": 2, \"last_id\":2, \"method\":\"firstloading\"}
		String tableName = JsonUtil.getStringValue(jo, "table_name"); //操作表名
		int tableId = JsonUtil.getIntValue(jo, "table_id", 0); //操作表中的id
		int pageSize = JsonUtil.getIntValue(jo, "pageSize", ConstantsUtil.DEFAULT_PAGE_SIZE); //每页的大小
		int lastId = JsonUtil.getIntValue(jo, "last_id", 0); //开始的页数
		int toUserId = JsonUtil.getIntValue(jo, "toUserId"); //操作的对象用户的id
		int firstId = JsonUtil.getIntValue(jo, "first_id", 0); //结束的页数
		String method = JsonUtil.getStringValue(jo, "method", "firstloading"); //操作方式
		boolean showUserInfo = JsonUtil.getBooleanValue(jo, "showUserInfo");
		StringBuffer sql = new StringBuffer();
		List<Map<String, Object>> rs = new ArrayList<>();
	
		//查找该用户所有的转发
		if(StringUtil.isNull(tableName) && toUserId > 0){		
			if("firstloading".equalsIgnoreCase(method)){
				sql.append("select c.id, c.pid, c.froms, c.content, c.table_id, c.table_name, c.create_user_id, u.account");
				sql.append(", date_format(c.create_time,'%Y-%c-%d %H:%i:%s') create_time,c.comment_level, c.table_id ");
				sql.append("  from T_COMMENT c inner join t_user u on u.id = c.create_user_id");
				sql.append(" where c.create_user_id =? and c.status = ? ");
				sql.append(" order by c.id desc limit 0,?");
				rs = commentDao.executeSQL(sql.toString(), toUserId, ConstantsUtil.STATUS_NORMAL, pageSize);
			//下刷新
			}else if("lowloading".equalsIgnoreCase(method)){
				sql.append("select c.id, c.pid, c.froms, c.content, c.table_id, c.table_name, c.create_user_id, u.account");
				sql.append(", date_format(c.create_time,'%Y-%c-%d %H:%i:%s') create_time,c.comment_level, c.table_id ");
				sql.append("  from T_COMMENT c inner join t_user u on u.id = c.create_user_id");
				sql.append(" where c.create_user_id =? and c.status = ? ");
				sql.append(" and c.id < ? order by c.id desc limit 0,? ");
				rs = commentDao.executeSQL(sql.toString(), toUserId, ConstantsUtil.STATUS_NORMAL, lastId, pageSize);
			//上刷新
			}else if("uploading".equalsIgnoreCase(method)){
				sql.append("select c.id, c.pid, c.froms, c.content, c.table_id, c.table_name, c.create_user_id, u.account");
				sql.append(", date_format(c.create_time,'%Y-%c-%d %H:%i:%s') create_time,c.comment_level, c.table_id ");
				sql.append("  from T_COMMENT c inner join t_user u on u.id = c.create_user_id");
				sql.append(" where c.create_user_id =? and c.status = ? ");
				sql.append(" and c.id > ? limit 0,?  ");
				rs = commentDao.executeSQL(sql.toString(), toUserId, ConstantsUtil.STATUS_NORMAL, firstId, pageSize);
			}
		}
				
		//查找指定表的数据
		if(StringUtil.isNotNull(tableName) && toUserId < 1 && tableId > 0){
			if("firstloading".equalsIgnoreCase(method)){
				sql.append("select c.id, c.pid, c.froms, c.content, c.table_id, c.table_name, c.create_user_id, u.account");
				sql.append(", date_format(c.create_time,'%Y-%c-%d %H:%i:%s') create_time,c.comment_level, c.table_id ");
				sql.append("  from T_COMMENT c inner join t_user u on u.id = c.create_user_id");
				sql.append(" where c.table_name = ? and c.table_id = ? and c.status = ?");
				sql.append(" order by c.id desc limit 0,?");
				rs = commentDao.executeSQL(sql.toString(), tableName, tableId, ConstantsUtil.STATUS_NORMAL, pageSize);
			//下刷新
			}else if("lowloading".equalsIgnoreCase(method)){			
				sql.append("select c.id, c.pid, c.froms, c.content, c.table_id, c.table_name, c.create_user_id, u.account ");
				sql.append(", date_format(c.create_time,'%Y-%c-%d %H:%i:%s') create_time,c.comment_level, c.table_id ");
				sql.append("  from T_COMMENT c inner join t_user u on u.id = c.create_user_id ");
				sql.append(" where c.table_name = ? and c.table_id = ? and c.status = ?");
				sql.append(" and c.id < ? order by c.id desc limit 0,?");
				rs = commentDao.executeSQL(sql.toString(), tableName, tableId, ConstantsUtil.STATUS_NORMAL, lastId, pageSize);
			//上刷新
			}else if("uploading".equalsIgnoreCase(method)){
				sql.append("select c.id, c.pid, c.froms, c.content, c.table_id, c.table_name, c.create_user_id, u.account");
				sql.append(", date_format(c.create_time,'%Y-%c-%d %H:%i:%s') create_time,c.comment_level, c.table_id ");
				sql.append("  from T_COMMENT c inner join t_user u on u.id = c.create_user_id ");
				sql.append(" where c.table_name = ? and c.table_id = ? and c.status = ?");
				sql.append(" and c.id > ? limit 0,?");
				rs = commentDao.executeSQL(sql.toString(), tableName, tableId, ConstantsUtil.STATUS_NORMAL, firstId, pageSize);
			}
		}
		if(rs !=null && rs.size() > 0){
			int createUserId = 0;
			JSONObject friendObject = friendHandler.getFromToFriends(user.getId());
			
			//String account = "";
			String tabName;
			int tabId;
			int pid = 0;
			int pCreateUserId = 0;
			String atUsername = ""; //@用户的名称
			String resultContent = null;
			//为名字备注赋值
			for(int i = 0; i < rs.size(); i++){
				if(StringUtil.isNull(tableName) && tableId <1){
					//在非获取指定表下的评论列表的情况下的前面35个字符
					tabName = StringUtil.changeNotNull((rs.get(i).get("table_name")));
					tabId = StringUtil.changeObjectToInt(rs.get(i).get("table_id"));
					rs.get(i).put("source", commonHandler.getContentByTableNameAndId(tabName, tabId, user));
				}else{
					pid = StringUtil.changeObjectToInt(rs.get(i).get("pid"));
					if(pid > 0 ){
						pCreateUserId = getCommentCreateUserId(pid);
						if(pCreateUserId > 0){
							atUsername = userHandler.getUserName(pCreateUserId);
							if(StringUtil.isNotNull(atUsername)){
								resultContent = StringUtil.changeNotNull((rs.get(i).get("content")));
								if(resultContent.indexOf("@"+atUsername) > 0 )
									rs.get(i).put("content", resultContent);
								else{
									rs.get(i).put("content", "回复@"+atUsername + " " + resultContent);
								}
							}
						}
					}
				}
				if(showUserInfo){
					createUserId = StringUtil.changeObjectToInt(rs.get(i).get("create_user_id"));
					rs.get(i).putAll(userHandler.getBaseUserInfo(createUserId, user, friendObject));
				}
			}	
		}
		
		//保存操作日志
		operateLogService.saveOperateLog(user, request, null, StringUtil.getStringBufferStr(user.getAccount(),"获取用户ID为：",toUserId,",表名：",tableName,"，表id为：",tableId,"的评论列表").toString(), "getCommentByLimit()", ConstantsUtil.STATUS_NORMAL, 0);
				
		return rs;
	}
	
	/**
	 * 构建CreateUser的SQL语句字符串
	 * @param ids
	 * @return
	 */
	/*private String buildCreateUserIdInSQL(Object[] ids){
		int length = ids.length;
		if(length == 0) 
			return "";
		StringBuffer buffer = new StringBuffer();
		buffer.append(" and t.create_user_id in (");
		for(int i =0; i < length; i++){
			if(i == length -1){
				buffer.append(ids[i]);
			}else{
				buffer.append(ids[i] + ",");
			}
		}
		buffer.append(") ");
		
		return buffer.toString();
	}*/

	@Override
	public List<Map<String, Object>> getOneCommentItemsByLimit(JSONObject jo,
			UserBean user, HttpServletRequest request) throws Exception {
		logger.info("CommentServiceImpl-->getOneCommentItemsByLimit():jsonObject=" +jo.toString() +", user=" +user.getAccount());
		 //{\"table_name\":\"t_mood\", \"table_id\":123
		//, \"first_id\": 2, \"last_id\":2, \"method\":\"firstloading\"}
		List<Map<String, Object>> rs = new ArrayList<>();
		int cid = JsonUtil.getIntValue(jo, "cid");
		if(cid < 1) 
			throw new ErrorException("评论id为空");
		
		String tableName = JsonUtil.getStringValue(jo, "table_name"); //操作表名
		int tableId = JsonUtil.getIntValue(jo, "table_id", 0); //操作表中的id
		int pageSize = JsonUtil.getIntValue(jo, "pageSize"); //每页的大小
		int lastId = JsonUtil.getIntValue(jo, "last_id", 0); //开始的页数
		int firstId = JsonUtil.getIntValue(jo, "first_id", 0); //结束的页数
		String method = JsonUtil.getStringValue(jo, "method", "firstloading"); //操作方式
		
		StringBuffer sql = new StringBuffer();
		if("firstloading".equalsIgnoreCase(method)){
			sql.append("select c.id, c.content, c.table_id, c.table_name, c.create_user_id, u.account ");
			sql.append(", date_format(c.create_time,'%Y-%c-%d %H:%i:%s') create_time,c.comment_level, c.table_id, c.pid ");
			sql.append("  from T_COMMENT c inner join t_user u on u.id = c.create_user_id ");
			sql.append(" where c.table_name = ? and c.table_id = ? and c.status = ? and c.cid = ? ");
			sql.append(" order by c.id desc");
			sql.append(getLimitSql(pageSize));
			rs = commentDao.executeSQL(sql.toString()
					, tableName, tableId, ConstantsUtil.STATUS_NORMAL, cid);
		//下刷新
		}else if("lowloading".equalsIgnoreCase(method)){			
			sql.append("select c.id, c.content, c.table_id, c.table_name, c.create_user_id, u.account ");
			sql.append(", date_format(c.create_time,'%Y-%c-%d %H:%i:%s') create_time,c.comment_level, c.table_id, c.pid ");
			sql.append("  from T_COMMENT c inner join t_user u on u.id = c.create_user_id ");
			sql.append(" where c.table_name = ? and c.table_id = ? and c.status = ? and c.cid = ? ");
			sql.append(" and c.id < ? order by c.id desc");
			sql.append(getLimitSql(pageSize));
			rs = commentDao.executeSQL(sql.toString(), tableName, tableId, ConstantsUtil.STATUS_NORMAL, 
					tableName, tableId, ConstantsUtil.STATUS_NORMAL, lastId, cid);
		//上刷新
		}else if("uploading".equalsIgnoreCase(method)){
			sql.append("select c.id, c.content, c.table_id, c.table_name, c.create_user_id, u.account ");
			sql.append(", date_format(c.create_time,'%Y-%c-%d %H:%i:%s') create_time,c.comment_level, c.table_id, c.pid ");
			sql.append("  from T_COMMENT c inner join t_user u on u.id = c.create_user_id ");
			sql.append(" where c.table_name = ? and c.table_id = ? and c.status = ? and c.pid = ? ");
			sql.append(" and c.id > ? ");
			sql.append(getLimitSql(pageSize));
			rs = commentDao.executeSQL(sql.toString(), tableName, tableId, ConstantsUtil.STATUS_NORMAL, 
					tableName, tableId, ConstantsUtil.STATUS_NORMAL, firstId, cid);
		}
		
		//保存操作日志
		operateLogService.saveOperateLog(user, request, null, StringUtil.getStringBufferStr(user.getAccount(),"查看表：",tableName,"，表id为：",tableId,"，评论ID为：", cid, "的评论详情列表").toString(), "getOneCommentItemsByLimit()", ConstantsUtil.STATUS_NORMAL, 0);
				
		return rs;
	}
	
	/**
	 * 根据页码生成Limit的SQL
	 * @param pageSize
	 * @return
	 */
	private String getLimitSql(int pageSize){
		if(pageSize == 0) return "";
		return " limit 0,"+ pageSize;
	}

	@Override
	public int getCountByObject(JSONObject jo, UserBean user,
			HttpServletRequest request) throws Exception {
		logger.info("CommentServiceImpl-->getCountByObject():jsonObject=" +jo.toString() +", user=" +user.getAccount());
		
		String tableName = JsonUtil.getStringValue(jo, "table_name"); //操作表名
		int tableId = JsonUtil.getIntValue(jo, "table_id", 0); //操作表中的id
		
		StringBuffer sql = new StringBuffer();
		sql.append("where table_name = '" + tableName +"'");
		sql.append(" and table_id = '" + tableId +"'");
		sql.append(" and status = " +ConstantsUtil.STATUS_NORMAL);
		int count = 0;
		count = commentDao.getTotal("t_comment", sql.toString());
		//保存操作日志
		operateLogService.saveOperateLog(user, request, null, StringUtil.getStringBufferStr(user.getAccount(),"查看表：",tableName,"，表id为：",tableId,"，查询得到的总数是：", count, "条").toString(), "getCountByObject()", ConstantsUtil.STATUS_NORMAL, 0);
				
		return count;
	}

	@Override
	public int getCountByUser(JSONObject jo, UserBean user,
			HttpServletRequest request) throws Exception{
		logger.info("CommentServiceImpl-->getCountByUser():jsonObject=" +jo.toString() +", user=" +user.getAccount());
		
		int uid = JsonUtil.getIntValue(jo, "uid", user.getId()); //计算的用户id
		
		StringBuffer sql = new StringBuffer();
		sql.append(" where create_user_id = " + uid + " and status = " +ConstantsUtil.STATUS_NORMAL);
		int count = 0;
		count = commentDao.getTotal("t_comment", sql.toString());
		//保存操作日志
		operateLogService.saveOperateLog(user, request, null, StringUtil.getStringBufferStr(user.getAccount(),"查询用户ID为：", uid, "得到其评论总数是：", count, "条").toString(), "getCountByUser()", ConstantsUtil.STATUS_NORMAL, 0);
				
		return count;
	}

	@Override
	public Map<String, Object> deleteComment(JSONObject jo, UserBean user,
			HttpServletRequest request) {
		logger.info("CommentServiceImpl-->deleteComment():jsonObject=" +jo.toString() +", user=" +user.getAccount());
		Map<String, Object> message = new HashMap<String, Object>();
		message.put("isSuccess", false);
		int cid = JsonUtil.getIntValue(jo, "cid");
		int createUserId = JsonUtil.getIntValue(jo, "create_user_id");
		
		//非登录用户不能删除操作
		if(createUserId != user.getId()){
			message.put("message", EnumUtil.getResponseValue(EnumUtil.ResponseCode.请先登录.value));
			message.put("responseCode", EnumUtil.ResponseCode.请先登录.value);
			return message;
		}
		
		if(cid < 1){
			message.put("message", EnumUtil.getResponseValue(EnumUtil.ResponseCode.没有操作实例.value));
			message.put("responseCode", EnumUtil.ResponseCode.没有操作实例.value);
			return message;
		}
		
		boolean result = false;
		CommentBean commentBean = commentDao.findById(cid);
		if(commentBean != null && commentBean.getCreateUser().getId() == createUserId){
			result = commentDao.delete(commentBean);
		}else{
			message.put("message", EnumUtil.getResponseValue(EnumUtil.ResponseCode.操作对象不存在.value));
			message.put("responseCode", EnumUtil.ResponseCode.操作对象不存在.value);
		}
		
		if(result){
			//保存操作日志
			operateLogService.saveOperateLog(user, request, null, StringUtil.getStringBufferStr(user.getAccount(),"删除评论ID为", cid, "的数据", StringUtil.getSuccessOrNoStr(result)).toString(), "deleteComment()", ConstantsUtil.STATUS_NORMAL, 0);
			commentHandler.deleteComment(commentBean.getTableId(), commentBean.getTableName());
			message.put("isSuccess", true);
		}			
		
		return message;
	}

	@Override
	public int getTotalComments(int userId) {
		return this.commentDao.getTotalComments(userId);
	}
	
}
