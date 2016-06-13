package com.cn.leedane.service.impl;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import net.sf.json.JSONObject;

import org.apache.log4j.Logger;

import com.cn.leedane.Dao.FanDao;
import com.cn.leedane.Utils.ConstantsUtil;
import com.cn.leedane.Utils.EnumUtil;
import com.cn.leedane.Utils.JsonUtil;
import com.cn.leedane.Utils.StringUtil;
import com.cn.leedane.bean.FanBean;
import com.cn.leedane.bean.OperateLogBean;
import com.cn.leedane.bean.UserBean;
import com.cn.leedane.handler.CircleOfFriendsHandler;
import com.cn.leedane.handler.FanHandler;
import com.cn.leedane.handler.UserHandler;
import com.cn.leedane.service.FanService;
import com.cn.leedane.service.OperateLogService;
import com.cn.leedane.service.UserService;

/**
 * 粉丝service实现类
 * @author LeeDane
 * 2016年4月11日 上午10:32:07
 * Version 1.0
 */
public class FanServiceImpl extends BaseServiceImpl<FanBean> implements FanService<FanBean> {
	Logger logger = Logger.getLogger(getClass());
	@Resource
	private FanDao<FanBean> fanDao;
	
	public void setFanDao(FanDao<FanBean> fanDao) {
		this.fanDao = fanDao;
	}
	
	@Resource
	private UserService<UserBean> userService;
	
	public void setUserService(UserService<UserBean> userService) {
		this.userService = userService;
	}
	
	@Resource
	private UserHandler userHandler;
	
	public void setUserHandler(UserHandler userHandler) {
		this.userHandler = userHandler;
	}
	
	@Resource
	private FanHandler fanHandler;
	
	public void setFanHandler(FanHandler fanHandler) {
		this.fanHandler = fanHandler;
	}
	
	@Resource
	private CircleOfFriendsHandler circleOfFriendsHandler;
	
	public void setCircleOfFriendsHandler(
			CircleOfFriendsHandler circleOfFriendsHandler) {
		this.circleOfFriendsHandler = circleOfFriendsHandler;
	}
	
	@Resource
	private OperateLogService<OperateLogBean> operateLogService;

	public void setOperateLogService(
			OperateLogService<OperateLogBean> operateLogService) {
		this.operateLogService = operateLogService;
	}
	
	@Override
	public Map<String, Object> getMyAttentionsLimit(JSONObject jo, UserBean user, HttpServletRequest request) {
		long start = System.currentTimeMillis();
		int pageSize = JsonUtil.getIntValue(jo, "pageSize", ConstantsUtil.DEFAULT_PAGE_SIZE); //每页的大小
		int lastId = JsonUtil.getIntValue(jo, "last_id"); //开始的页数
		int firstId = JsonUtil.getIntValue(jo, "first_id"); //结束的页数
		int toUserId = JsonUtil.getIntValue(jo, "toUserId", user.getId());
		String method = JsonUtil.getStringValue(jo, "method", "firstloading"); //操作方式
		
		logger.info("FanServiceImpl-->getMyAttentionFansLimit():jo="+jo.toString());
		
		Map<String, Object> message = new HashMap<String, Object>();
		message.put("isSuccess", false);
		
		//暂时不支持查看别人的关注好友列表
		if(toUserId != user.getId()){
			message.put("message", EnumUtil.getResponseValue(EnumUtil.ResponseCode.没有操作权限.value));
			message.put("responseCode", EnumUtil.ResponseCode.没有操作权限.value);
	        return message;
		}
		
		List<Map<String, Object>> rs = new ArrayList<Map<String,Object>>();
		StringBuffer sql;
		if("firstloading".equalsIgnoreCase(method)){
			sql = new StringBuffer();
			sql.append("select f.id, f.create_user_id, f.to_user_id user_id, f.user_remark remark, date_format(f.create_time,'%Y-%c-%d %H:%i:%s') create_time ");
			sql.append(" from t_fan f");
			sql.append(" where f.status=? and f.create_user_id=? ");
			sql.append(" order by f.id desc limit 0,?");
			rs = fanDao.executeSQL(sql.toString(), ConstantsUtil.STATUS_NORMAL, toUserId, pageSize);
		//下刷新
		}else if("lowloading".equalsIgnoreCase(method)){
			sql = new StringBuffer();
			sql.append("select f.id, f.create_user_id, f.to_user_id user_id, f.user_remark remark, date_format(f.create_time,'%Y-%c-%d %H:%i:%s') create_time ");
			sql.append(" from t_fan f");
			sql.append(" where f.status=? and f.create_user_id=? ");
			sql.append(" and f.id < ? order by f.id desc limit 0,? ");
			rs = fanDao.executeSQL(sql.toString(), ConstantsUtil.STATUS_NORMAL, toUserId, lastId, pageSize);
		//上刷新
		}else if("uploading".equalsIgnoreCase(method)){
			sql = new StringBuffer();
			sql.append("select f.id, f.create_user_id, f.to_user_id user_id, f.user_remark remark, date_format(f.create_time,'%Y-%c-%d %H:%i:%s') create_time ");
			sql.append(" from t_fan f");
			sql.append(" where f.status=? and f.create_user_id=? ");
			sql.append(" and f.id > ? limit 0,? ");
			rs = fanDao.executeSQL(sql.toString(), ConstantsUtil.STATUS_NORMAL, toUserId,firstId, pageSize);
		}
				
		if(rs !=null && rs.size() > 0){
			int toId = 0;
			for(int i = 0; i < rs.size(); i++){
				toId = StringUtil.changeObjectToInt(rs.get(i).get("user_id"));
				if(toId> 0){
					rs.get(i).putAll(userHandler.getBaseUserInfo(toId));
				}
				rs.get(i).put("is_fan", fanHandler.inAttention(toId, toUserId));
				rs.get(i).put("is_attention", true);
			}	
		}
		//保存操作日志
		operateLogService.saveOperateLog(user, request, null, user.getAccount()+"获取所关注的对象的列表", "getMyAttentionFansLimit()", ConstantsUtil.STATUS_NORMAL, 0);
		
		long end = System.currentTimeMillis();
		System.out.println("获取我关注对象列表总计耗时：" +(end - start) +"毫秒");
		message.put("message", rs);
		message.put("isSuccess", true);
		return message;
	}
	
	@Override
	public Map<String, Object> getToAttentionsLimit(JSONObject jo, UserBean user, HttpServletRequest request) {
		long start = System.currentTimeMillis();
		int pageSize = JsonUtil.getIntValue(jo, "pageSize", ConstantsUtil.DEFAULT_PAGE_SIZE); //每页的大小
		int lastId = JsonUtil.getIntValue(jo, "last_id"); //开始的页数
		int firstId = JsonUtil.getIntValue(jo, "first_id"); //结束的页数
		int toUserId = JsonUtil.getIntValue(jo, "toUserId");
		String method = JsonUtil.getStringValue(jo, "method", "firstloading"); //操作方式
		
		logger.info("FanServiceImpl-->getToAttentionsLimit():jo="+jo.toString());
		
		Map<String, Object> message = new HashMap<String, Object>();
		message.put("isSuccess", false);
		
		if(toUserId < 1 || toUserId == user.getId()){
			message.put("message", EnumUtil.getResponseValue(EnumUtil.ResponseCode.操作对象不存在.value));
			message.put("responseCode", EnumUtil.ResponseCode.操作对象不存在.value);
			return message;
		}
		
		List<Map<String, Object>> rs = new ArrayList<Map<String,Object>>();
		StringBuffer sql;
		if("firstloading".equalsIgnoreCase(method)){
			sql = new StringBuffer();
			sql.append("select f.id, f.create_user_id, f.to_user_id user_id, f.user_remark remark, date_format(f.create_time,'%Y-%c-%d %H:%i:%s') create_time ");
			sql.append(" from t_fan f");
			sql.append(" where f.status=? and f.create_user_id=? ");
			sql.append(" order by f.id desc limit 0,?");
			rs = fanDao.executeSQL(sql.toString(), ConstantsUtil.STATUS_NORMAL, toUserId, pageSize);
		//下刷新
		}else if("lowloading".equalsIgnoreCase(method)){
			sql = new StringBuffer();
			sql.append("select f.id, f.create_user_id, f.to_user_id user_id, f.user_remark remark, date_format(f.create_time,'%Y-%c-%d %H:%i:%s') create_time ");
			sql.append(" from t_fan f");
			sql.append(" where f.status=? and f.create_user_id=? ");
			sql.append(" and f.id < ? order by f.id desc limit 0,? ");
			rs = fanDao.executeSQL(sql.toString(), ConstantsUtil.STATUS_NORMAL, toUserId, lastId, pageSize);
		//上刷新
		}else if("uploading".equalsIgnoreCase(method)){
			sql = new StringBuffer();
			sql.append("select f.id, f.create_user_id, f.to_user_id user_id, f.user_remark remark, date_format(f.create_time,'%Y-%c-%d %H:%i:%s') create_time ");
			sql.append(" from t_fan f");
			sql.append(" where f.status=? and f.create_user_id=? ");
			sql.append(" and f.id > ? limit 0,? ");
			rs = fanDao.executeSQL(sql.toString(), ConstantsUtil.STATUS_NORMAL, toUserId,firstId, pageSize);
		}
				
		if(rs !=null && rs.size() > 0){
			int createUserId = 0;
			for(int i = 0; i < rs.size(); i++){
				createUserId = StringUtil.changeObjectToInt(rs.get(i).get("user_id"));
				System.out.println("user_id:"+createUserId);
				if(createUserId> 0){
					rs.get(i).putAll(userHandler.getBaseUserInfo(createUserId));
				}
				//是否粉和是否关注都相对于我(user)而已展示
				rs.get(i).put("is_fan", fanHandler.inAttention(user.getId(), createUserId));
				rs.get(i).put("is_attention", fanHandler.inAttention(user.getId(), createUserId));
			}	
		}
		//保存操作日志
		operateLogService.saveOperateLog(user, request, null, user.getAccount()+"获取ID为"+toUserId+"的用户所关注的对象的列表", "getToAttentionsLimit()", ConstantsUtil.STATUS_NORMAL, 0);
		
		long end = System.currentTimeMillis();
		System.out.println("获取TA关注对象列表总计耗时：" +(end - start) +"毫秒");
		message.put("message", rs);
		message.put("isSuccess", true);
		return message;
	}
	
	@Override
	public Map<String, Object> getMyFansLimit(JSONObject jo, UserBean user, HttpServletRequest request) {
		long start = System.currentTimeMillis();
		int pageSize = JsonUtil.getIntValue(jo, "pageSize", ConstantsUtil.DEFAULT_PAGE_SIZE); //每页的大小
		int lastId = JsonUtil.getIntValue(jo, "last_id"); //开始的页数
		int firstId = JsonUtil.getIntValue(jo, "first_id"); //结束的页数
		int toUserId = JsonUtil.getIntValue(jo, "toUserId", user.getId());
		String method = JsonUtil.getStringValue(jo, "method", "firstloading"); //操作方式
		
		logger.info("FanServiceImpl-->getMyFansLimit():jo="+jo.toString());
		Map<String, Object> message = new HashMap<String, Object>();
		message.put("isSuccess", false);
		if(toUserId != user.getId()){
			message.put("message", EnumUtil.getResponseValue(EnumUtil.ResponseCode.操作对象不存在.value));
			message.put("responseCode", EnumUtil.ResponseCode.操作对象不存在.value);
			return message;
		}
		List<Map<String, Object>> rs = new ArrayList<Map<String,Object>>();
		StringBuffer sql;
		if("firstloading".equalsIgnoreCase(method)){
			sql = new StringBuffer();
			sql.append("select f.id, f.create_user_id user_id, f.to_user_id, date_format(f.create_time,'%Y-%c-%d %H:%i:%s') create_time");
			sql.append(" from t_fan f");
			sql.append(" where f.status=? and f.to_user_id=? ");
			sql.append(" order by f.id desc limit 0,?");
			rs = fanDao.executeSQL(sql.toString(), ConstantsUtil.STATUS_NORMAL, toUserId, pageSize);
		//下刷新
		}else if("lowloading".equalsIgnoreCase(method)){
			sql = new StringBuffer();
			sql.append("select f.id, f.create_user_id user_id, f.to_user_id, date_format(f.create_time,'%Y-%c-%d %H:%i:%s') create_time ");
			sql.append(" from t_fan f");
			sql.append(" where f.status=? and f.to_user_id=? ");
			sql.append(" and f.id < ? order by f.id desc limit 0,? ");
			rs = fanDao.executeSQL(sql.toString(), ConstantsUtil.STATUS_NORMAL, toUserId, lastId, pageSize);
		//上刷新
		}else if("uploading".equalsIgnoreCase(method)){
			sql = new StringBuffer();
			sql.append("select f.id, f.create_user_id user_id, f.to_user_id, date_format(f.create_time,'%Y-%c-%d %H:%i:%s') create_time");
			sql.append(" from t_fan f");
			sql.append(" where f.status=? and f.to_user_id=? ");
			sql.append(" and f.id > ? limit 0,? ");
			rs = fanDao.executeSQL(sql.toString(), ConstantsUtil.STATUS_NORMAL, toUserId,firstId, pageSize);
		}
				
		if(rs !=null && rs.size() > 0){
			int createUserId = 0;
			for(int i = 0; i < rs.size(); i++){
				createUserId = StringUtil.changeObjectToInt(rs.get(i).get("user_id"));
				System.out.println("user_id:"+createUserId);
				if(createUserId> 0){
					rs.get(i).putAll(userHandler.getBaseUserInfo(createUserId));
				}
				rs.get(i).put("is_fan", true);
				rs.get(i).put("is_attention", fanHandler.inAttention(toUserId, createUserId));
			}	
		}
		//保存操作日志
		operateLogService.saveOperateLog(user, request, null, user.getAccount()+"获取粉丝列表", "getMyFansLimit()", ConstantsUtil.STATUS_NORMAL, 0);
		
		long end = System.currentTimeMillis();
		System.out.println("获取我的粉丝列表总计耗时：" +(end - start) +"毫秒");
		message.put("message", rs);
		message.put("isSuccess", true);
		return message;
	}
	
	@Override
	public Map<String, Object> getToFansLimit(JSONObject jo, UserBean user, HttpServletRequest request) {
		long start = System.currentTimeMillis();
		int pageSize = JsonUtil.getIntValue(jo, "pageSize", ConstantsUtil.DEFAULT_PAGE_SIZE); //每页的大小
		int lastId = JsonUtil.getIntValue(jo, "last_id"); //开始的页数
		int firstId = JsonUtil.getIntValue(jo, "first_id"); //结束的页数
		int toUserId = JsonUtil.getIntValue(jo, "toUserId");
		String method = JsonUtil.getStringValue(jo, "method", "firstloading"); //操作方式
		
		logger.info("FanServiceImpl-->getToFansLimit():jo="+jo.toString());
		
		Map<String, Object> message = new HashMap<String, Object>();
		message.put("isSuccess", false);
		if(toUserId < 1 || toUserId == user.getId()){
			message.put("message", EnumUtil.getResponseValue(EnumUtil.ResponseCode.操作对象不存在.value));
			message.put("responseCode", EnumUtil.ResponseCode.操作对象不存在.value);
			return message;
		}
		List<Map<String, Object>> rs = new ArrayList<Map<String,Object>>();
		StringBuffer sql;
		if("firstloading".equalsIgnoreCase(method)){
			sql = new StringBuffer();
			sql.append("select f.id, f.create_user_id user_id, f.to_user_id, date_format(f.create_time,'%Y-%c-%d %H:%i:%s') create_time");
			sql.append(" from t_fan f");
			sql.append(" where f.status=? and f.to_user_id=? ");
			sql.append(" order by f.id desc limit 0,?");
			rs = fanDao.executeSQL(sql.toString(), ConstantsUtil.STATUS_NORMAL, toUserId, pageSize);
		//下刷新
		}else if("lowloading".equalsIgnoreCase(method)){
			sql = new StringBuffer();
			sql.append("select f.id, f.create_user_id user_id, f.to_user_id, date_format(f.create_time,'%Y-%c-%d %H:%i:%s') create_time ");
			sql.append(" from t_fan f");
			sql.append(" where f.status=? and f.to_user_id=? ");
			sql.append(" and f.id < ? order by f.id desc limit 0,? ");
			rs = fanDao.executeSQL(sql.toString(), ConstantsUtil.STATUS_NORMAL, toUserId, lastId, pageSize);
		//上刷新
		}else if("uploading".equalsIgnoreCase(method)){
			sql = new StringBuffer();
			sql.append("select f.id, f.create_user_id user_id, f.to_user_id, date_format(f.create_time,'%Y-%c-%d %H:%i:%s') create_time");
			sql.append(" from t_fan f");
			sql.append(" where f.status=? and f.to_user_id=? ");
			sql.append(" and f.id > ? limit 0,? ");
			rs = fanDao.executeSQL(sql.toString(), ConstantsUtil.STATUS_NORMAL, toUserId,firstId, pageSize);
		}
				
		if(rs !=null && rs.size() > 0){
			int createUserId = 0;
			for(int i = 0; i < rs.size(); i++){
				createUserId = StringUtil.changeObjectToInt(rs.get(i).get("user_id"));
				System.out.println("user_id:"+createUserId);
				if(createUserId> 0){
					rs.get(i).putAll(userHandler.getBaseUserInfo(createUserId));
				}
				//是否粉和是否关注都相对于我(user)而已展示
				rs.get(i).put("is_fan", fanHandler.inAttention(user.getId(), createUserId));
				rs.get(i).put("is_attention", fanHandler.inAttention(user.getId(), createUserId));
			}	
		}
		//保存操作日志
		operateLogService.saveOperateLog(user, request, null, user.getAccount()+"获取用户ID为："+toUserId+"的粉丝列表", "getToFansLimit()", ConstantsUtil.STATUS_NORMAL, 0);
		
		long end = System.currentTimeMillis();
		System.out.println("获取TA的粉丝列表总计耗时：" +(end - start) +"毫秒");
		message.put("message", rs);
		message.put("isSuccess", true);
		return message;
	}

	@Override
	public boolean cancel(JSONObject jo, UserBean user, HttpServletRequest request) {
		String toUserIds = JsonUtil.getStringValue(jo, "toUserIds");
		logger.info("FanServiceImpl-->cancel():id="+user.getId()+",toUserIds="+toUserIds);
		boolean result = false;
		if(StringUtil.isNotNull(toUserIds)){
			String[] ids = toUserIds.split(",");
			int[] fanIds = new int[ids.length];
			for(int i=0; i< ids.length; i++){
				fanIds[i] = Integer.parseInt(ids[i]);
			}
			result = this.fanDao.cancel(user.getId(), fanIds);
			if(result){
				for(int i = 0; i< fanIds.length; i++)
					fanHandler.cancelAttention(user.getId(), fanIds[i]);
			}
		}
		
		return result;
	}

	@Override
	public boolean isFanEachOther(JSONObject jo, UserBean user, HttpServletRequest request) {
		int toUserId = JsonUtil.getIntValue(jo, "toUserId");
		logger.info("FanServiceImpl-->isFanEachOther():id="+user.getId()+",toUserId="+toUserId);
		return this.fanDao.isFanEachOther(user.getId(), toUserId);
	}

	@Override
	public Map<String, Object> addFan(JSONObject jo, UserBean user,
			HttpServletRequest request) {
		logger.info("FanServiceImpl-->addFan():jo="+jo.toString());
		int toUserId = JsonUtil.getIntValue(jo, "toUserId");
		String remark = JsonUtil.getStringValue(jo, "remark");
		Map<String, Object> message = new HashMap<String, Object>();
		message.put("isSuccess", false);
		if(toUserId == 0) {
			message.put("message", EnumUtil.getResponseValue(EnumUtil.ResponseCode.某些参数为空.value));
			message.put("responseCode", EnumUtil.ResponseCode.某些参数为空.value);
			return message;			
		}

		UserBean toUser = userService.findById(toUserId);
		if(toUser == null){
			message.put("message", EnumUtil.getResponseValue(EnumUtil.ResponseCode.操作对象不存在.value));
			message.put("responseCode", EnumUtil.ResponseCode.操作对象不存在.value);
			return message;	
		}
		
		if(toUserId == user.getId()){
			message.put("message", "不能添加自己为好友"); 
			return message;	
		}
		
		
		if(cheakIsAddFan(user.getId(), toUserId)){
			message.put("message", EnumUtil.getResponseValue(EnumUtil.ResponseCode.需要添加的记录已经存在.value));
			message.put("responseCode", EnumUtil.ResponseCode.需要添加的记录已经存在.value);
			return message;	
		}
		FanBean fanBean = new FanBean();
		fanBean.setToUserId(toUserId);
		fanBean.setStatus(ConstantsUtil.STATUS_NORMAL);
		fanBean.setCreateUser(user);
		fanBean.setCreateTime(new Date());
		fanBean.setRemark(remark);
		
		if(!fanDao.save(fanBean)){
			message.put("message", EnumUtil.getResponseValue(EnumUtil.ResponseCode.数据库保存失败.value));
			message.put("responseCode", EnumUtil.ResponseCode.数据库保存失败.value);
			return message;	
		}
		
		fanHandler.addAttention(user, toUser);
		
		//合并两个用户的朋友圈
		//myCircleOfFriendsHandler.mergeTimeLine(user.getId(), toUserId);
		
		message.put("message", "恭喜您成为"+toUser.getAccount()+"的粉丝，今后他/她的动态将在您的朋友圈出现"); 
		message.put("isSuccess", true);
		//保存操作日志
		operateLogService.saveOperateLog(user, request, null, user.getAccount()+"成为："+toUser.getAccount()+"为粉丝", "addFan()", ConstantsUtil.STATUS_NORMAL, 0);
		return message;
	}

	/**
	 * 判断是否已经成为TA的粉丝
	 * @param id
	 * @param toUserId
	 * @return
	 */
	private boolean cheakIsAddFan(int id, int toUserId) {
		return this.fanDao.executeSQL("select id from t_fan where create_user_id=? and to_user_id =? and status=?", id, toUserId, ConstantsUtil.STATUS_NORMAL).size() > 0;
	}

	@Override
	public Map<String, Object> isFan(JSONObject jo, UserBean user,
			HttpServletRequest request) {
		logger.info("FanServiceImpl-->isFan():jo="+jo.toString());
		Map<String, Object> message = new HashMap<String, Object>();
		message.put("isSuccess", false);
		int toUserId = JsonUtil.getIntValue(jo, "toUserId");
		
		if(toUserId == 0) {
			message.put("message", EnumUtil.getResponseValue(EnumUtil.ResponseCode.某些参数为空.value));
			message.put("responseCode", EnumUtil.ResponseCode.某些参数为空.value);
			return message;			
		}
		
		if(isFanEachOther(jo, user, request)){
			message.put("message", "已互相关注");
			message.put("isSuccess", true);
			return message;
		}
		
		boolean isFan = fanDao.executeSQL("select id from t_fan where status=? and create_user_id=? and to_user_id=?", ConstantsUtil.STATUS_NORMAL, user.getId(), toUserId).size() > 0;
		if(isFan){
			message.put("message", "已关注");
			message.put("isSuccess", true);
			return message;
		}else{
			message.put("message", "关注Ta");
			return message;
			
		}
	}
	
}
