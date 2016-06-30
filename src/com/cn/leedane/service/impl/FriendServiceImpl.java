package com.cn.leedane.service.impl;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.apache.log4j.Logger;

import com.cn.leedane.Dao.FriendDao;
import com.cn.leedane.Utils.ConstantsUtil;
import com.cn.leedane.Utils.EnumUtil;
import com.cn.leedane.Utils.EnumUtil.DataTableType;
import com.cn.leedane.Utils.JsonUtil;
import com.cn.leedane.Utils.StringUtil;
import com.cn.leedane.bean.FriendBean;
import com.cn.leedane.bean.OperateLogBean;
import com.cn.leedane.bean.ShowContactsBean;
import com.cn.leedane.bean.UserBean;
import com.cn.leedane.handler.FriendHandler;
import com.cn.leedane.handler.UserHandler;
import com.cn.leedane.service.FriendService;
import com.cn.leedane.service.OperateLogService;
import com.cn.leedane.service.UserService;

/**
 * 博客service实现类
 * @author LeeDane
 * 2015年7月10日 下午6:19:30
 * Version 1.0
 */
public class FriendServiceImpl extends BaseServiceImpl<FriendBean> implements FriendService<FriendBean> {
	Logger logger = Logger.getLogger(getClass());
	@Resource
	private FriendDao<FriendBean> friendDao;
	
	public void setFriendDao(FriendDao<FriendBean> friendDao) {
		this.friendDao = friendDao;
	}
	
	@Resource
	private UserService<UserBean> userService;
	
	public void setUserService(UserService<UserBean> userService) {
		this.userService = userService;
	}
	
	@Resource
	private FriendHandler friendHandler;
	
	public void setFriendHandler(FriendHandler friendHandler) {
		this.friendHandler = friendHandler;
	}
	@Resource
	private UserHandler userHandler;
	
	public void setUserHandler(UserHandler userHandler) {
		this.userHandler = userHandler;
	}
	
	@Resource
	private OperateLogService<OperateLogBean> operateLogService;

	public void setOperateLogService(
			OperateLogService<OperateLogBean> operateLogService) {
		this.operateLogService = operateLogService;
	}
	
	@Override
	public List<Map<String, Object>> getFromToFriends(int uid) {
		logger.info("FriendServiceImpl-->getFromToFriends():uid="+uid);
		String sql = " select to_user_id id, (case when to_user_remark = '' || to_user_remark = null then (select u.account from "+DataTableType.用户.value+" u where  u.id = to_user_id and u.status = "+ConstantsUtil.STATUS_NORMAL+") else to_user_remark end ) remark from "+DataTableType.好友.value+" where from_user_id =? and status = "+ConstantsUtil.STATUS_NORMAL+" "
				+" UNION " 
				+" select from_user_id id, (case when from_user_remark = '' || from_user_remark = null then (select u.account from "+DataTableType.用户.value+" u where  u.id = from_user_id and u.status = "+ConstantsUtil.STATUS_NORMAL+") else from_user_remark end ) remark from "+DataTableType.好友.value+" where to_user_id = ? and status = "+ConstantsUtil.STATUS_NORMAL;
		return friendDao.executeSQL(sql, uid, uid);
	}
	
	@Override
	public List<Map<String, Object>> getToFromFriends(int uid) {
		logger.info("FriendServiceImpl-->getToFromFriends():uid="+uid);
		String sql = " select from_user_id id, (case when to_user_remark = '' || to_user_remark = null then (select u.account from "+DataTableType.用户.value+" u where  u.id = to_user_id and u.status =?) else to_user_remark end ) remark from "+DataTableType.好友.value+" where to_user_id =? and status =?"
				+" UNION " 
				+" select to_user_id id, (case when from_user_remark = '' || from_user_remark = null then (select u.account from "+DataTableType.用户.value+" u where  u.id = from_user_id and u.status =?) else from_user_remark end ) remark from "+DataTableType.好友.value+" where from_user_id = ? and status=?";
		return friendDao.executeSQL(sql, ConstantsUtil.STATUS_NORMAL, uid, ConstantsUtil.STATUS_NORMAL, ConstantsUtil.STATUS_NORMAL, uid, ConstantsUtil.STATUS_NORMAL);
	}

	@Override
	public boolean deleteFriends(int uid, int... friends) {
		logger.info("FriendServiceImpl-->deleteFriends():uid="+uid+",friends="+friends);
		return this.friendDao.deleteFriends(uid, friends);
	}

	@Override
	public boolean isFriend(int id, int to_user_id) {
		logger.info("FriendServiceImpl-->isFriend():id="+id+",to_user_id="+to_user_id);
		return this.friendDao.isFriend(id, to_user_id);
	}

	@Override
	public Map<String, Object> addFriend(JSONObject jo, UserBean user,
			HttpServletRequest request) {
		logger.info("FriendServiceImpl-->addFriend():jo="+jo.toString());
		int to_user_id = JsonUtil.getIntValue(jo, "to_user_id");
		Map<String, Object> message = new HashMap<String, Object>();
		message.put("isSuccess", false);
		if(to_user_id == 0) {
			message.put("message", "to_user_id为空"); 
			return message;			
		}
		
		if(isFriendRecord(user.getId(), to_user_id)){
			message.put("message", "等待对方确认..."); 
			message.put("isSuccess", true);
			return message;	
		}
		
		UserBean toUser = userService.findById(to_user_id);
		if(toUser == null){
			message.put("message", "添加的好友账号不存在"); 
			return message;	
		}
		
		if(to_user_id == user.getId()){
			message.put("message", "不能添加自己为好友"); 
			return message;	
		}
		FriendBean friendBean = new FriendBean();
		friendBean.setFromUserId(user.getId());
		friendBean.setToUserId(to_user_id);
		friendBean.setAddIntroduce(JsonUtil.getStringValue(jo, "add_introduce"));
		friendBean.setStatus(ConstantsUtil.STATUS_DISABLE);
		friendBean.setCreateUser(user);
		friendBean.setCreateTime(new Date());
		
		if(StringUtil.isNotNull(JsonUtil.getStringValue(jo, "to_user_remark"))){
			friendBean.setToUserRemark(JsonUtil.getStringValue(jo, "to_user_remark"));
		}else{
			friendBean.setToUserRemark(toUser.getAccount());
		}
		//因为from_user_remark字段不能为空，所以暂时给一个""值
		friendBean.setFromUserRemark("");
		if(!friendDao.save(friendBean)){
			message.put("message", "添加好友失败"); 
			return message;	
		}
		message.put("message", "等待对方确认..."); 
		message.put("isSuccess", true);
		//保存操作日志
		operateLogService.saveOperateLog(user, request, null, user.getAccount()+"请求添加账号："+toUser.getAccount()+"为好友", "addFriend()", ConstantsUtil.STATUS_NORMAL, 0);
		return message;
	}

	@Override
	public Map<String, Object> addAgree(JSONObject jo, UserBean user,
			HttpServletRequest request) {
		logger.info("FriendServiceImpl-->addAgree():jo="+jo.toString());
		int fid = JsonUtil.getIntValue(jo, "fid");
		Map<String, Object> message = new HashMap<String, Object>();
		message.put("isSuccess", false);
		if(fid == 0){
			message.put("message", EnumUtil.getResponseValue(EnumUtil.ResponseCode.缺少请求参数.value));
			message.put("responseCode", EnumUtil.ResponseCode.缺少请求参数.value);
			return message;	
		}
		FriendBean friendBean = friendDao.findById(fid);
		if(friendBean == null){
			message.put("message", EnumUtil.getResponseValue(EnumUtil.ResponseCode.好友关系不存在.value));
			message.put("responseCode", EnumUtil.ResponseCode.好友关系不存在.value);
			return message;		
		}
		
		if(friendBean.getStatus() != ConstantsUtil.STATUS_DISABLE){
			message.put("message", EnumUtil.getResponseValue(EnumUtil.ResponseCode.好友关系不是待确认状态.value));
			message.put("responseCode", EnumUtil.ResponseCode.好友关系不是待确认状态.value);
			return message;	
		}
		
		if(friendBean.getStatus() == ConstantsUtil.STATUS_NORMAL){
			message.put("isSuccess", true);
			message.put("message", "恭喜，TA已经是好友"); 
			return message;	
		}
		
		friendBean.setStatus(ConstantsUtil.STATUS_NORMAL);
		friendBean.setModifyUser(user);
		friendBean.setModifyTime(new Date());
		if(StringUtil.isNotNull(JsonUtil.getStringValue(jo, "from_user_remark"))){
			friendBean.setFromUserRemark(JsonUtil.getStringValue(jo, "from_user_remark"));
		}else{
			friendBean.setFromUserRemark(user.getAccount());
		}
			
		 
		if(!friendDao.save(friendBean)){
			message.put("message", "同意好友关系失败，请稍后重试"); 
			return message;	
		}
		
		//更新redis的朋友关系
		message.put("isSuccess", friendHandler.addFriends(friendBean.getToUserId(), friendBean.getFromUserId(), friendBean.getToUserRemark(), friendBean.getFromUserRemark()));
		message.put("message", "恭喜，TA已经是好友"); 
		
		//保存操作日志
		operateLogService.saveOperateLog(user, request, null, user.getAccount()+"同意好友关系"+fid, "addAgree()", ConstantsUtil.STATUS_NORMAL, 0);
		return message;
	}

	@Override
	public boolean isFriendRecord(int id, int to_user_id) {
		logger.info("FriendServiceImpl-->isFriendRecord():id="+id+",to_user_id="+to_user_id);
		return this.friendDao.isFriendRecord(id, to_user_id);
	}

	@Override
	public Map<String, Object> friendsAlreadyPaging(JSONObject jo, UserBean user,
			HttpServletRequest request) {
		logger.info("FriendServiceImpl-->friendsAlreadyPaging():jo="+jo.toString());
		Map<String, Object> message = new HashMap<String, Object>();
		message.put("isSuccess", false);
		int lastId = JsonUtil.getIntValue(jo, "last_id", 0); //开始的页数
		int firstId = JsonUtil.getIntValue(jo, "first_id", 0); //结束的页数
		String method = JsonUtil.getStringValue(jo, "method", "firstloading"); //操作方式
		int pageSize = JsonUtil.getIntValue(jo, "pageSize", ConstantsUtil.DEFAULT_PAGE_SIZE); //每页的大小
		
		StringBuffer sql = new StringBuffer();
		List<Map<String, Object>> rs = new ArrayList<>();
	
		if("firstloading".equalsIgnoreCase(method)){
			sql.append("select id, to_user_id fid, '' introduce, date_format(modify_time,'%Y-%c-%d %H:%i:%s') create_time, status, (case when to_user_remark = '' || to_user_remark = null then (select u.account from "+DataTableType.用户.value+" u where  u.id = to_user_id and u.status =?) else to_user_remark end ) remark from "+DataTableType.好友.value+" where from_user_id =? and status =?");
			sql.append(" UNION");
			sql.append(" select id, from_user_id fid, add_introduce introduce, date_format(modify_time,'%Y-%c-%d %H:%i:%s') create_time, status, (case when from_user_remark = '' || from_user_remark = null then (select u.account from "+DataTableType.用户.value+" u where  u.id = from_user_id and u.status =?) else from_user_remark end ) remark from "+DataTableType.好友.value+" where to_user_id = ? and status =?");
			sql.append(" order by id desc limit 0,?");
			rs = friendDao.executeSQL(sql.toString(), ConstantsUtil.STATUS_NORMAL, user.getId(),ConstantsUtil.STATUS_NORMAL, ConstantsUtil.STATUS_NORMAL, user.getId(), ConstantsUtil.STATUS_NORMAL, pageSize);
		//下刷新
		}else if("lowloading".equalsIgnoreCase(method)){
			sql.append("select id, to_user_id fid, '' introduce, date_format(modify_time,'%Y-%c-%d %H:%i:%s') create_time, status, (case when to_user_remark = '' || to_user_remark = null then (select u.account from "+DataTableType.用户.value+" u where  u.id = to_user_id and u.status =?) else to_user_remark end ) remark from "+DataTableType.好友.value+" where from_user_id =? and status =? and id < ?");
			sql.append(" UNION");
			sql.append(" select id, from_user_id fid, add_introduce introduce, date_format(modify_time,'%Y-%c-%d %H:%i:%s') create_time, status, (case when from_user_remark = '' || from_user_remark = null then (select u.account from "+DataTableType.用户.value+" u where  u.id = from_user_id and u.status =?) else from_user_remark end ) remark from "+DataTableType.好友.value+" where to_user_id = ? and status =?");
			sql.append(" and id < ? order by id desc limit 0,? ");
			rs = friendDao.executeSQL(sql.toString(), ConstantsUtil.STATUS_NORMAL, user.getId(),ConstantsUtil.STATUS_NORMAL, lastId, ConstantsUtil.STATUS_NORMAL, user.getId(), ConstantsUtil.STATUS_NORMAL, lastId, pageSize);
		//上刷新
		}else if("uploading".equalsIgnoreCase(method)){
			sql.append("select id, to_user_id fid, '' introduce, date_format(modify_time,'%Y-%c-%d %H:%i:%s') create_time, status, (case when to_user_remark = '' || to_user_remark = null then (select u.account from "+DataTableType.用户.value+" u where  u.id = to_user_id and u.status =?) else to_user_remark end ) remark from "+DataTableType.好友.value+" where from_user_id =? and status =? and id > ?");
			sql.append(" UNION");
			sql.append(" select id, from_user_id fid, add_introduce introduce, date_format(modify_time,'%Y-%c-%d %H:%i:%s') create_time, status, (case when from_user_remark = '' || from_user_remark = null then (select u.account from "+DataTableType.用户.value+" u where  u.id = from_user_id and u.status =?) else from_user_remark end ) remark from "+DataTableType.好友.value+" where to_user_id = ? and status =?");
			sql.append(" and id > ? limit 0,? ");
			rs = friendDao.executeSQL(sql.toString(), ConstantsUtil.STATUS_NORMAL, user.getId(),ConstantsUtil.STATUS_NORMAL, firstId, ConstantsUtil.STATUS_NORMAL, user.getId(), ConstantsUtil.STATUS_NORMAL, firstId, pageSize);
		}
		
		if(rs !=null && rs.size() > 0){
			int createUserId = 0;
			//为名字备注赋值
			for(int i = 0; i < rs.size(); i++){
				createUserId = StringUtil.changeObjectToInt(rs.get(i).get("fid"));
				rs.get(i).putAll(userHandler.getBaseUserInfo(createUserId));
			}	
		}
		
		//保存操作日志
		operateLogService.saveOperateLog(user, request, null, StringUtil.getStringBufferStr(user.getAccount(),"获取已经跟我成为好友关系的分页列表").toString(), "friendsAlreadyPaging()", ConstantsUtil.STATUS_NORMAL, 0);
			
		message.put("isSuccess", true);
		message.put("message", rs);
		return message;
	}
	
	@Override
	public Map<String, Object> friendsNotyetPaging(JSONObject jo, UserBean user,
			HttpServletRequest request) {
		logger.info("FriendServiceImpl-->friendsNotyetPaging():jo="+jo.toString());
		Map<String, Object> message = new HashMap<String, Object>();
		message.put("isSuccess", false);
		int lastId = JsonUtil.getIntValue(jo, "last_id", 0); //开始的页数
		int firstId = JsonUtil.getIntValue(jo, "first_id", 0); //结束的页数
		String method = JsonUtil.getStringValue(jo, "method", "firstloading"); //操作方式
		int pageSize = JsonUtil.getIntValue(jo, "pageSize", ConstantsUtil.DEFAULT_PAGE_SIZE); //每页的大小
		
		StringBuffer sql = new StringBuffer();
		List<Map<String, Object>> rs = new ArrayList<>();
	
		if("firstloading".equalsIgnoreCase(method)){
			sql.append("select id, "+ 4 +" status, to_user_id fid, add_introduce introduce, date_format((case when modify_time is null then create_time else modify_time end),'%Y-%c-%d %H:%i:%s') create_time, (case when to_user_remark = '' || to_user_remark = null then (select u.account from "+DataTableType.用户.value+" u where  u.id = to_user_id and u.status =?) else to_user_remark end ) remark from "+DataTableType.好友.value+" where from_user_id =? and status =?");
			sql.append(" UNION");
			sql.append(" select id, "+ ConstantsUtil.STATUS_DISABLE +" status, from_user_id fid, add_introduce introduce, date_format((case when modify_time is null then create_time else modify_time end),'%Y-%c-%d %H:%i:%s') create_time, (case when from_user_remark = '' || from_user_remark = null then (select u.account from "+DataTableType.用户.value+" u where  u.id = from_user_id and u.status =?) else from_user_remark end ) remark from "+DataTableType.好友.value+" where to_user_id = ? and status =?");
			sql.append(" order by id desc limit 0,?");
			rs = friendDao.executeSQL(sql.toString(), ConstantsUtil.STATUS_NORMAL, user.getId(),ConstantsUtil.STATUS_DISABLE, ConstantsUtil.STATUS_NORMAL, user.getId(), ConstantsUtil.STATUS_DISABLE, pageSize);
		//下刷新
		}else if("lowloading".equalsIgnoreCase(method)){
			sql.append("select id, "+ 4 +" status, to_user_id fid, add_introduce introduce, date_format((case when modify_time is null then create_time else modify_time end),'%Y-%c-%d %H:%i:%s') create_time, (case when to_user_remark = '' || to_user_remark = null then (select u.account from "+DataTableType.用户.value+" u where  u.id = to_user_id and u.status =?) else to_user_remark end ) remark from "+DataTableType.好友.value+" where from_user_id =? and status =? and id < ?");
			sql.append(" UNION");
			sql.append(" select id, "+ ConstantsUtil.STATUS_DISABLE +" status, from_user_id fid, add_introduce introduce, date_format((case when modify_time is null then create_time else modify_time end),'%Y-%c-%d %H:%i:%s') create_time, (case when from_user_remark = '' || from_user_remark = null then (select u.account from "+DataTableType.用户.value+" u where  u.id = from_user_id and u.status =?) else from_user_remark end ) remark from "+DataTableType.好友.value+" where to_user_id = ? and status =?");
			sql.append(" and id < ? order by id desc limit 0,? ");
			rs = friendDao.executeSQL(sql.toString(), ConstantsUtil.STATUS_NORMAL, user.getId(),ConstantsUtil.STATUS_DISABLE, lastId, ConstantsUtil.STATUS_NORMAL, user.getId(), ConstantsUtil.STATUS_DISABLE, lastId, pageSize);
		//上刷新
		}else if("uploading".equalsIgnoreCase(method)){
			sql.append("select id, "+ 4 +" status, to_user_id fid, add_introduce introduce, date_format((case when modify_time is null then create_time else modify_time end),'%Y-%c-%d %H:%i:%s') create_time, (case when to_user_remark = '' || to_user_remark = null then (select u.account from "+DataTableType.用户.value+" u where  u.id = to_user_id and u.status =?) else to_user_remark end ) remark from "+DataTableType.好友.value+" where from_user_id =? and status =? and id > ?");
			sql.append(" UNION");
			sql.append(" select id, "+ ConstantsUtil.STATUS_DISABLE +" status, from_user_id fid, add_introduce introduce, date_format((case when modify_time is null then create_time else modify_time end),'%Y-%c-%d %H:%i:%s') create_time, (case when from_user_remark = '' || from_user_remark = null then (select u.account from "+DataTableType.用户.value+" u where  u.id = from_user_id and u.status =?) else from_user_remark end ) remark from "+DataTableType.好友.value+" where to_user_id = ? and status =?");
			sql.append(" and id > ? limit 0,? ");
			rs = friendDao.executeSQL(sql.toString(), ConstantsUtil.STATUS_NORMAL, user.getId(),ConstantsUtil.STATUS_DISABLE, firstId, ConstantsUtil.STATUS_NORMAL, user.getId(), ConstantsUtil.STATUS_DISABLE, firstId, pageSize);
		}
		
		if(rs !=null && rs.size() > 0){
			int createUserId = 0;
			//为名字备注赋值
			for(int i = 0; i < rs.size(); i++){
				createUserId = StringUtil.changeObjectToInt(rs.get(i).get("fid"));
				rs.get(i).putAll(userHandler.getBaseUserInfo(createUserId));
			}	
		}
		
		//保存操作日志
		operateLogService.saveOperateLog(user, request, null, StringUtil.getStringBufferStr(user.getAccount(),"获取暂时未跟我成为好友关系的分页列表").toString(), "friendsNotyetPaging()", ConstantsUtil.STATUS_NORMAL, 0);
			
		message.put("isSuccess", true);
		message.put("message", rs);
		return message;
	}

	@Override
	public Map<String, Object> requestPaging(JSONObject jo, UserBean user,
			HttpServletRequest request) {
		logger.info("FriendServiceImpl-->requestPaging():jo="+jo.toString());
		Map<String, Object> message = new HashMap<String, Object>();
		message.put("isSuccess", false);
		if(user == null || user.getId() < 1){
			message.put("message", EnumUtil.getResponseValue(EnumUtil.ResponseCode.请先登录.value));
			message.put("responseCode", EnumUtil.ResponseCode.请先登录.value);
			return message;
		}
		
		int lastId = JsonUtil.getIntValue(jo, "last_id", 0); //开始的页数
		int firstId = JsonUtil.getIntValue(jo, "first_id", 0); //结束的页数
		String method = JsonUtil.getStringValue(jo, "method", "firstloading"); //操作方式
		int pageSize = JsonUtil.getIntValue(jo, "pageSize", ConstantsUtil.DEFAULT_PAGE_SIZE); //每页的大小
		
		StringBuffer sql = new StringBuffer();
		List<Map<String, Object>> rs = new ArrayList<>();
	
		if("firstloading".equalsIgnoreCase(method)){
			sql.append("select id, to_user_id fid, status, date_format(create_time,'%Y-%c-%d %H:%i:%s') create_time, (case when to_user_remark = '' || to_user_remark = null then (select u.account from "+DataTableType.用户.value+" u where  u.id = to_user_id and u.status =?) else to_user_remark end ) remark");
			sql.append(" from "+DataTableType.好友.value+" where from_user_id =?");
			sql.append(" order by id desc limit 0,?");
			rs = friendDao.executeSQL(sql.toString(), ConstantsUtil.STATUS_NORMAL, user.getId(), pageSize);
		//下刷新
		}else if("lowloading".equalsIgnoreCase(method)){
			sql.append("select id, to_user_id fid, status, date_format(create_time,'%Y-%c-%d %H:%i:%s') create_time, (case when to_user_remark = '' || to_user_remark = null then (select u.account from "+DataTableType.用户.value+" u where  u.id = to_user_id and u.status =?) else to_user_remark end ) remark");
			sql.append(" from "+DataTableType.好友.value+" where from_user_id =?");
			sql.append(" and id < ? order by id desc limit 0,? ");
			rs = friendDao.executeSQL(sql.toString(), ConstantsUtil.STATUS_NORMAL, user.getId(), lastId, pageSize);
		//上刷新
		}else if("uploading".equalsIgnoreCase(method)){
			sql.append("select id, to_user_id fid, status, date_format(create_time,'%Y-%c-%d %H:%i:%s') create_time, (case when to_user_remark = '' || to_user_remark = null then (select u.account from "+DataTableType.用户.value+" u where  u.id = to_user_id and u.status =?) else to_user_remark end ) remark");
			sql.append(" from "+DataTableType.好友.value+" where from_user_id =?");
			sql.append(" and id > ? limit 0,? ");
			rs = friendDao.executeSQL(sql.toString(), ConstantsUtil.STATUS_NORMAL, user.getId(), firstId, pageSize);
		}
		
		if(rs !=null && rs.size() > 0){
			int createUserId = 0;
			//为名字备注赋值
			for(int i = 0; i < rs.size(); i++){
				createUserId = StringUtil.changeObjectToInt(rs.get(i).get("fid"));
				rs.get(i).putAll(userHandler.getBaseUserInfo(createUserId));
			}	
		}
		
		//保存操作日志
		operateLogService.saveOperateLog(user, request, null, StringUtil.getStringBufferStr(user.getAccount(),"获取我发送的好友请求列表").toString(), "requestPaging()", ConstantsUtil.STATUS_NORMAL, 0);
			
		message.put("isSuccess", true);
		message.put("message", rs);
		return message;
	}

	@Override
	public Map<String, Object> responsePaging(JSONObject jo, UserBean user,
			HttpServletRequest request) {
		logger.info("FriendServiceImpl-->responsePaging():jo="+jo.toString());
		Map<String, Object> message = new HashMap<String, Object>();
		message.put("isSuccess", false);
		if(user == null || user.getId() < 1){
			message.put("message", EnumUtil.getResponseValue(EnumUtil.ResponseCode.请先登录.value));
			message.put("responseCode", EnumUtil.ResponseCode.请先登录.value);
			return message;
		}
		int lastId = JsonUtil.getIntValue(jo, "last_id", 0); //开始的页数
		int firstId = JsonUtil.getIntValue(jo, "first_id", 0); //结束的页数
		String method = JsonUtil.getStringValue(jo, "method", "firstloading"); //操作方式
		int pageSize = JsonUtil.getIntValue(jo, "pageSize", ConstantsUtil.DEFAULT_PAGE_SIZE); //每页的大小
		
		StringBuffer sql = new StringBuffer();
		List<Map<String, Object>> rs = new ArrayList<>();
	
		if("firstloading".equalsIgnoreCase(method)){
			sql.append("select id, to_user_id fid, status, date_format(create_time,'%Y-%c-%d %H:%i:%s') create_time, (case when to_user_remark = '' || to_user_remark = null then (select u.account from "+DataTableType.用户.value+" u where  u.id = to_user_id and u.status =?) else to_user_remark end ) remark");
			sql.append(" from "+DataTableType.好友.value+" where from_user_id =?");
			sql.append(" order by id desc limit 0,?");
			rs = friendDao.executeSQL(sql.toString(), ConstantsUtil.STATUS_NORMAL, user.getId(), pageSize);
		//下刷新
		}else if("lowloading".equalsIgnoreCase(method)){
			sql.append("select id, to_user_id fid, status, date_format(create_time,'%Y-%c-%d %H:%i:%s') create_time, (case when to_user_remark = '' || to_user_remark = null then (select u.account from "+DataTableType.用户.value+" u where  u.id = to_user_id and u.status =?) else to_user_remark end ) remark");
			sql.append(" from "+DataTableType.好友.value+" where from_user_id =?");
			sql.append(" and id < ? order by id desc limit 0,? ");
			rs = friendDao.executeSQL(sql.toString(), ConstantsUtil.STATUS_NORMAL, user.getId(), lastId, pageSize);
		//上刷新
		}else if("uploading".equalsIgnoreCase(method)){
			sql.append("select id, to_user_id fid, status, date_format(create_time,'%Y-%c-%d %H:%i:%s') create_time, (case when to_user_remark = '' || to_user_remark = null then (select u.account from "+DataTableType.用户.value+" u where  u.id = to_user_id and u.status =?) else to_user_remark end ) remark");
			sql.append(" from "+DataTableType.好友.value+" where from_user_id =?");
			sql.append(" and id > ? limit 0,? ");
			rs = friendDao.executeSQL(sql.toString(), ConstantsUtil.STATUS_NORMAL, user.getId(), firstId, pageSize);
		}
		
		if(rs !=null && rs.size() > 0){
			int createUserId = 0;
			//为名字备注赋值
			for(int i = 0; i < rs.size(); i++){
				createUserId = StringUtil.changeObjectToInt(rs.get(i).get("fid"));
				rs.get(i).putAll(userHandler.getBaseUserInfo(createUserId));
			}	
		}
		
		//保存操作日志
		operateLogService.saveOperateLog(user, request, null, StringUtil.getStringBufferStr(user.getAccount(),"获取等待我同意的好友关系列表").toString(), "responsePaging()", ConstantsUtil.STATUS_NORMAL, 0);
			
		message.put("isSuccess", true);
		message.put("message", rs);
		return message;
	}

	@Override
	public Map<String, Object> matchContact(JSONObject jo, UserBean user,
			HttpServletRequest request) {
		logger.info("FriendServiceImpl-->matchContact():jo="+jo.toString());
		Map<String, Object> message = new HashMap<String, Object>();
		message.put("isSuccess", false);
		
		String contacts = JsonUtil.getStringValue(jo, "contacts"); //用户本地上传的联系人
		if(StringUtil.isNull(contacts)){
			message.put("message", EnumUtil.getResponseValue(EnumUtil.ResponseCode.操作失败.value));
			message.put("responseCode", EnumUtil.ResponseCode.操作失败.value);
			return message;
		}	
		//获取所有通讯录用户
		JSONObject contactsObj = JSONObject.fromObject(contacts);
		@SuppressWarnings("unchecked")
		List<ShowContactsBean> showContactsBeans = (List<ShowContactsBean>) JSONObject.toBean(contactsObj);
		
		//获得所有系统用户的信息
		JSONArray userInfos = userHandler.getAllUserDetail();
		
		//获取该用户的所有好友ID
		Set<Integer> ids = friendHandler.getFromToFriendIds(user.getId());
		
		//保存操作日志
		operateLogService.saveOperateLog(user, request, null, StringUtil.getStringBufferStr(user.getAccount(),"本地联系人跟服务器上的好友进行匹配").toString(), "matchContact()", ConstantsUtil.STATUS_NORMAL, 0);
			
		message.put("isSuccess", true);
		message.put("message", null);
		return message;
	}

	@Override
	public Map<String, Object> friends(JSONObject jo, UserBean user,
			HttpServletRequest request) {
		logger.info("FriendServiceImpl-->matchContact():jo="+jo.toString());
		Map<String, Object> message = new HashMap<String, Object>();
		message.put("isSuccess", false);
		message.put("message", EnumUtil.getResponseValue(EnumUtil.ResponseCode.操作失败.value));
		
		List<Map<String, Object>> rs = getFromToFriends(user.getId());
		if(rs !=null && rs.size() > 0){
			int fId = 0;
			//为名字备注赋值
			for(int i = 0; i < rs.size(); i++){
				fId = StringUtil.changeObjectToInt(rs.get(i).get("id"));
				rs.get(i).putAll(userHandler.getBaseUserInfo(fId));
			}
		}
		message.put("isSuccess", true);
		message.put("message", rs);
		return message;
	}
	
}
