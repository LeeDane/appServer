package com.cn.leedane.service.impl;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import net.sf.json.JSONObject;

import org.apache.log4j.Logger;

import com.cn.leedane.Dao.FilePathDao;
import com.cn.leedane.Dao.MoodDao;
import com.cn.leedane.Utils.ConstantsUtil;
import com.cn.leedane.Utils.DateUtil;
import com.cn.leedane.Utils.EmojiUtil;
import com.cn.leedane.Utils.EnumUtil;
import com.cn.leedane.Utils.EnumUtil.NotificationType;
import com.cn.leedane.Utils.SensitiveWord.SensitivewordFilter;
import com.cn.leedane.Utils.JsonUtil;
import com.cn.leedane.Utils.StringUtil;
import com.cn.leedane.bean.FilePathBean;
import com.cn.leedane.bean.FriendBean;
import com.cn.leedane.bean.MoodBean;
import com.cn.leedane.bean.OperateLogBean;
import com.cn.leedane.bean.TimeLineBean;
import com.cn.leedane.bean.UserBean;
import com.cn.leedane.handler.CircleOfFriendsHandler;
import com.cn.leedane.handler.CommentHandler;
import com.cn.leedane.handler.FriendHandler;
import com.cn.leedane.handler.MoodHandler;
import com.cn.leedane.handler.NotificationHandler;
import com.cn.leedane.handler.TransmitHandler;
import com.cn.leedane.handler.UserHandler;
import com.cn.leedane.handler.ZanHandler;
import com.cn.leedane.observer.ConcreteWatched;
import com.cn.leedane.observer.ConcreteWatcher;
import com.cn.leedane.observer.Watched;
import com.cn.leedane.observer.Watcher;
import com.cn.leedane.observer.template.UpdateMoodTemplate;
import com.cn.leedane.service.FilePathService;
import com.cn.leedane.service.FriendService;
import com.cn.leedane.service.MoodService;
import com.cn.leedane.service.OperateLogService;
import com.cn.leedane.service.UserService;

/**
 * 心情service实现类
 * @author LeeDane
 * 2015年11月22日 下午10:42:43
 * Version 1.0
 */
public class MoodServiceImpl extends BaseServiceImpl<MoodBean> implements MoodService<MoodBean> {
	Logger logger = Logger.getLogger(getClass());
	@Resource
	private MoodDao<MoodBean> moodDao;
	
	@Resource
	private FilePathDao<FilePathBean> filePathDao;
	
	@Resource
	private FilePathService<FilePathBean> filePathService;
	
	@Resource
	private OperateLogService<OperateLogBean> operateLogService;
	
	@Resource
	private UserService<UserBean> userService;
	
	@Resource
	private FriendService<FriendBean> friendService;
	
	@Resource
	private UserHandler userHandler;
	@Resource
	private CommentHandler commentHandler;
	@Resource
	private TransmitHandler transmitHandler;
	@Resource
	private ZanHandler zanHandler;
	@Resource
	private MoodHandler moodHandler;
	@Resource
	private CircleOfFriendsHandler circleOfFriendsHandler;
	
	public void setCircleOfFriendsHandler(
			CircleOfFriendsHandler circleOfFriendsHandler) {
		this.circleOfFriendsHandler = circleOfFriendsHandler;
	}
	
	@Resource
	private FriendHandler friendHandler;
	public void setFriendHandler(FriendHandler friendHandler) {
		this.friendHandler = friendHandler;
	}
	
	public void setMoodDao(MoodDao<MoodBean> moodDao) {
		this.moodDao = moodDao;
	}
	
	public void setFilePathDao(FilePathDao<FilePathBean> filePathDao) {
		this.filePathDao = filePathDao;
	}
	
	public void setFilePathService(FilePathService<FilePathBean> filePathService) {
		this.filePathService = filePathService;
	}
	public void setOperateLogService(
			OperateLogService<OperateLogBean> operateLogService) {
		this.operateLogService = operateLogService;
	}
	
	public void setUserService(UserService<UserBean> userService) {
		this.userService = userService;
	}
	
	public void setFriendService(FriendService<FriendBean> friendService) {
		this.friendService = friendService;
	}
	
	public void setUserHandler(UserHandler userHandler) {
		this.userHandler = userHandler;
	}
	
	public void setCommentHandler(CommentHandler commentHandler) {
		this.commentHandler = commentHandler;
	}
	
	public void setTransmitHandler(TransmitHandler transmitHandler) {
		this.transmitHandler = transmitHandler;
	}
	
	public void setZanHandler(ZanHandler zanHandler) {
		this.zanHandler = zanHandler;
	}
	
	public void setMoodHandler(MoodHandler moodHandler) {
		this.moodHandler = moodHandler;
	}
	
	@Resource
	private NotificationHandler notificationHandler;
	
	public void setNotificationHandler(NotificationHandler notificationHandler) {
		this.notificationHandler = notificationHandler;
	}
	
	@Override
	public Map<String, Object> saveMood(JSONObject jsonObject, UserBean user, int status, HttpServletRequest request) throws Exception {
		logger.info("MoodServiceImpl-->saveMood():jsonObject=" +jsonObject.toString() +", status=" +status);
		String content = JsonUtil.getStringValue(jsonObject, "content");
		Map<String, Object> message = new HashMap<String, Object>();
		message.put("isSuccess", false);
		
		//过滤掉emoji
		content = EmojiUtil.filterEmoji(content);
		if(StringUtil.isNull(content)){
			message.put("message", EnumUtil.getResponseValue(EnumUtil.ResponseCode.某些参数为空.value));
			message.put("responseCode", EnumUtil.ResponseCode.某些参数为空.value);
			return message;
		}
				
		MoodBean moodBean = new MoodBean();
		moodBean.setContent(content);
		String location = JsonUtil.getStringValue(jsonObject, "location");
		if(StringUtil.isNotNull(location)){
			double longitude = JsonUtil.getDoubleValue(jsonObject, "longitude");
			double latitude = JsonUtil.getDoubleValue(jsonObject, "latitude");
			moodBean.setLocation(location);
			moodBean.setLongitude(longitude);
			moodBean.setLatitude(latitude);
		}
		moodBean.setCreateTime(new Date());
		moodBean.setFroms(JsonUtil.getStringValue(jsonObject, "froms"));
		moodBean.setPublishNow(true);
		moodBean.setStatus(status);
		moodBean.setCreateUser(user);
		String uuid = UUID.randomUUID().toString() + System.currentTimeMillis();
		moodBean.setUuid(uuid);
		
		String base64Str = JsonUtil.getStringValue(jsonObject, "base64");
		
		if(!StringUtil.isNull(base64Str)){
			String[] base64s = base64Str.split("&&");

			for(int i=0; i < base64s.length; i++){
				filePathService.saveEachFile(i, base64s[i], user, uuid, "t_mood");
			}
			moodBean.setHasImg(true);
		}
		
		boolean result = moodDao.save(moodBean);
		if(result){
			int i = moodBean.getId();
	        if(i < 0){
	        	//通过观察者的模式发送消息通知
				Watched watched = new ConcreteWatched();       
		        Watcher watcher = new ConcreteWatcher();
		        watched.addWatcher(watcher);
		        watched.notifyWatchers(userService.findById(user.getId()), new UpdateMoodTemplate());      
	        	message.put("message", i);
		        message.put("isSuccess", true);
	        }
	        
		}else{
			message.put("message", EnumUtil.getResponseValue(EnumUtil.ResponseCode.服务器处理异常.value));
			message.put("responseCode", EnumUtil.ResponseCode.服务器处理异常.value);
		}
		// 保存发表心情日志信息
		String subject = user.getAccount() + "发表了心情" + StringUtil.getSuccessOrNoStr(result);
		this.operateLogService.saveOperateLog(user, request, new Date(), subject, "saveMood()", ConstantsUtil.STATUS_NORMAL, 0);
		return message;
	}

	
	@Override
	public Map<String, Object> updateMoodStatus(JSONObject jsonObject, int status, HttpServletRequest request, UserBean user) {
		int mid = JsonUtil.getIntValue(jsonObject, "mid");
		logger.info("MoodServiceImpl-->updateMoodStatus():mid=" +mid +", status=" +status + ",jsonObject="+jsonObject.toString());
		Map<String, Object> message = new HashMap<String, Object>();
		message.put("isSuccess", false);
		boolean result = false;
		if(mid < 1){
			message.put("message", EnumUtil.getResponseValue(EnumUtil.ResponseCode.操作对象不存在.value));
			message.put("responseCode", EnumUtil.ResponseCode.操作对象不存在.value);
			return message;
		}
		try {
			//moodDao.executeSQL("update t_mood set status = ? where id = ? ", status, mid);
			result = moodDao.updateSQL("update t_mood set status = ? where id = ? ", status, mid);
			if(status == ConstantsUtil.STATUS_NORMAL){
				//通过观察者的模式发送消息通知
				Watched watched = new ConcreteWatched();       
		        Watcher watcher = new ConcreteWatcher();
		        watched.addWatcher(watcher);
		        watched.notifyWatchers(userService.findById(user.getId()), new UpdateMoodTemplate());
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		if(result){
			message.put("isSuccess", result);
		}else{
			message.put("message", EnumUtil.getResponseValue(EnumUtil.ResponseCode.服务器处理异常.value));
			message.put("responseCode", EnumUtil.ResponseCode.服务器处理异常.value);
		}
		return message;
	}

	@Override
	public Map<String, Object> deleteMood(JSONObject jo, UserBean user, HttpServletRequest request){
		int mid = JsonUtil.getIntValue(jo, "mid");
		logger.info("MoodServiceImpl-->deleteMood():mid=" +mid +",jo="+jo.toString());
		Map<String, Object> message = new HashMap<String, Object>();
		message.put("isSuccess", false);
		if(mid < 1) {
			message.put("message", EnumUtil.getResponseValue(EnumUtil.ResponseCode.操作对象不存在.value));
			message.put("responseCode", EnumUtil.ResponseCode.操作对象不存在.value);
			return message;
		}
		MoodBean moodBean = moodDao.findById(mid);
		String tableUuid = moodBean.getUuid();
		//有图片的先删掉图片
		if(moodBean != null && moodBean.isHasImg()){
			List<Map<String, Object>> list = filePathDao.executeSQL("select f.id from t_file_path f where f.table_uuid = ? and f.table_name=?", moodBean.getUuid(), "t_mood");
		
			if(list != null && list.size() >0){
				int fid = 0;
				for(Map<String, Object> map: list){
					fid = StringUtil.changeObjectToInt(map.get("id"));
					if(fid >0)
						filePathDao.deleteById("t_file_path", fid);
				}
				
			}
		}
		
		boolean result = moodDao.delete(moodBean);
		// 删除心情日志信息
		String subject = user.getAccount() + "删除了心情，心情id为:" + mid+StringUtil.getSuccessOrNoStr(result);
		this.operateLogService.saveOperateLog(user, request, new Date(), subject, "deleteMood()", 1 , 0);
	
		if(result){
			moodHandler.delete(mid, "t_mood", tableUuid);
			//同时删除朋友圈的数据
			circleOfFriendsHandler.deleteMyAndFansTimeLine(user, EnumUtil.DataTableType.心情.value, mid);
			message.put("isSuccess", result);
		}else{
			message.put("message", EnumUtil.getResponseValue(EnumUtil.ResponseCode.服务器处理异常.value));
			message.put("responseCode", EnumUtil.ResponseCode.服务器处理异常.value);
		}
		return message;
	}

	@Override
	public Map<String, Object> getMoodByLimit(JSONObject jo,
			UserBean user, HttpServletRequest request){
		logger.info("MoodServiceImpl-->getMoodByLimit():jo=" +jo.toString());
		long start = System.currentTimeMillis();
		int toUserId = JsonUtil.getIntValue(jo, "toUserId", user.getId()); //
		List<Map<String, Object>> rs = new ArrayList<Map<String,Object>>();
		int pageSize = JsonUtil.getIntValue(jo, "pageSize", ConstantsUtil.DEFAULT_PAGE_SIZE); //每页的大小
		int lastId = JsonUtil.getIntValue(jo, "last_id"); //开始的页数
		int firstId = JsonUtil.getIntValue(jo, "first_id"); //结束的页数
		String method = JsonUtil.getStringValue(jo, "method", "firstloading"); //操作方式
		String picSize = ConstantsUtil.DEFAULT_PIC_SIZE; //JsonUtil.getStringValue(jo, "pic_size"); //图像的规格(大小)		
				
		StringBuffer sql = new StringBuffer();
		Map<String, Object> message = new HashMap<String, Object>();
		message.put("isSuccess", false);
		
		if("firstloading".equalsIgnoreCase(method)){
			sql.append("select m.id, m.content, m.froms, m.uuid, m.create_user_id, date_format(m.create_time,'%Y-%c-%d %H:%i:%s') create_time, m.has_img,");
			sql.append(" m.read_number, m.location, m.longitude, m.latitude, m.zan_number, m.comment_number, m.transmit_number, m.share_number, u.account");
			sql.append(" from t_mood m inner join t_user u on u.id = m.create_user_id where m.status = ? and ");
			sql.append(" m.create_user_id = ?");
			sql.append(" order by m.id desc limit 0,?");
			rs = moodDao.executeSQL(sql.toString(), ConstantsUtil.STATUS_NORMAL, toUserId, pageSize);
		//下刷新
		}else if("lowloading".equalsIgnoreCase(method)){
			sql.append("select m.id, m.content, m.froms, m.uuid, m.create_user_id, date_format(m.create_time,'%Y-%c-%d %H:%i:%s') create_time, m.has_img,");
			sql.append(" m.read_number, m.location, m.longitude, m.latitude, m.zan_number, m.comment_number, m.transmit_number, m.share_number, u.account");
			sql.append(" from t_mood m inner join t_user u on u.id = m.create_user_id where m.status = ? and ");
			sql.append(" m.create_user_id = ?");
			sql.append(" and m.id < ? order by m.id desc limit 0,? ");
			rs = moodDao.executeSQL(sql.toString(), ConstantsUtil.STATUS_NORMAL, toUserId, lastId, pageSize);
		//上刷新
		}else if("uploading".equalsIgnoreCase(method)){
			sql.append("select m.id, m.content, m.froms, m.uuid, m.create_user_id, date_format(m.create_time,'%Y-%c-%d %H:%i:%s') create_time, m.has_img,");
			sql.append(" m.read_number, m.location, m.longitude, m.latitude, m.zan_number, m.comment_number, m.transmit_number, m.share_number, u.account");
			sql.append(" from t_mood m inner join t_user u on u.id = m.create_user_id where m.status = ? and ");
			sql.append(" m.create_user_id = ?");
			sql.append(" and m.id > ? limit 0,?  ");
			rs = moodDao.executeSQL(sql.toString(), ConstantsUtil.STATUS_NORMAL, toUserId, firstId, pageSize);
		}
		
		if(rs !=null && rs.size() > 0){
			boolean hasImg ;
			String uuid;
			int moodId;
			//为名字备注赋值
			for(int i = 0; i < rs.size(); i++){
				hasImg = StringUtil.changeObjectToBoolean(rs.get(i).get("has_img"));
				uuid = StringUtil.changeNotNull(rs.get(i).get("uuid"));
				moodId = StringUtil.changeObjectToInt(rs.get(i).get("id"));
				
				rs.get(i).put("zan_users", zanHandler.getZanUser(moodId, "t_mood", user, 6));
				rs.get(i).put("comment_number", commentHandler.getCommentNumber(moodId, "t_mood"));
				rs.get(i).put("transmit_number", transmitHandler.getTransmitNumber(moodId, "t_mood"));
				rs.get(i).put("zan_number", zanHandler.getZanNumber(moodId, "t_mood"));
				
				
				//有图片的获取图片的路径
				if(hasImg && !StringUtil.isNull(uuid)){
					rs.get(i).put("imgs", moodHandler.getMoodImg("t_mood", uuid, picSize));
				}
			}	
		}
		//保存操作日志
		operateLogService.saveOperateLog(user, request, null, user.getAccount()+"查看用户id为"+toUserId+"个人中心", "getMoodByLimit()", ConstantsUtil.STATUS_NORMAL, 0);
		
		long end = System.currentTimeMillis();
		System.out.println("获取心情列表总计耗时：" +(end - start) +"毫秒");
		message.put("message", rs);
		message.put("isSuccess", true);
		return message;
	}
	
	@Override
	public boolean saveBase64Str(JSONObject jo, UserBean user,
			HttpServletRequest request) {
		boolean result = false;
		logger.info("MoodServiceImpl-->saveBase64Str():jo=" +jo.toString());
		return result;
	}

	@Override
	public Map<String, Object> saveDividedMood(JSONObject jsonObject, UserBean user, int status, HttpServletRequest request){
		logger.info("MoodServiceImpl-->saveDividedMood():status=" +status +",jsonObject="+jsonObject.toString());
		String content = JsonUtil.getStringValue(jsonObject, "content");
		String uuid = JsonUtil.getStringValue(jsonObject, "uuid");
		String froms = JsonUtil.getStringValue(jsonObject, "froms");
		boolean hasImg = JsonUtil.getBooleanValue(jsonObject, "hasImg");
		Map<String, Object> message = new HashMap<String, Object>();
		message.put("isSuccess", false);
		//检查uuid是否已经存在了
		if(moodDao.executeSQL("select id from T_MOOD where table_uuid =? ", uuid).size() > 0){
			String oldUUid = uuid;
			uuid = UUID.randomUUID().toString() + System.currentTimeMillis();
			filePathDao.updateBatchFilePath(new String[]{oldUUid}, new String[]{uuid});
		}
		
		MoodBean moodBean = new MoodBean();
		moodBean.setContent(content);
		String location = JsonUtil.getStringValue(jsonObject, "location");
		if(StringUtil.isNotNull(location)){
			double longitude = JsonUtil.getDoubleValue(jsonObject, "longitude");
			double latitude = JsonUtil.getDoubleValue(jsonObject, "latitude");
			moodBean.setLocation(location);
			moodBean.setLongitude(longitude);
			moodBean.setLatitude(latitude);
		}
		moodBean.setCreateTime(new Date());
		moodBean.setCreateUser(user);
		moodBean.setFroms(froms);
		moodBean.setHasImg(hasImg);
		moodBean.setPublishNow(true);
		moodBean.setStatus(status);
		moodBean.setUuid(uuid);
		boolean result = moodDao.save(moodBean);

        if(result){
			message.put("isSuccess", result);
			//通过观察者的模式发送消息通知
			/*Watched watched = new ConcreteWatched();       
	        Watcher watcher = new ConcreteWatcher();
	        watched.addWatcher(watcher);
	        watched.notifyWatchers(userService.findById(user.getId()), new UpdateMoodTemplate());*/
		}else{
			message.put("message", EnumUtil.getResponseValue(EnumUtil.ResponseCode.服务器处理异常.value));
			message.put("responseCode", EnumUtil.ResponseCode.服务器处理异常.value);
		}
		return message;
	}
	
	

	@Override
	public int getCountByUser(JSONObject jo, UserBean user, HttpServletRequest request){
		logger.info("MoodServiceImpl-->getCountByUser():jsonObject=" +jo.toString() +", user=" +user.getAccount());
		
		int uid = JsonUtil.getIntValue(jo, "uid", user.getId()); //计算的用户id
		
		StringBuffer sql = new StringBuffer();
		sql.append(" where create_user_id = " + uid + " and status = " +ConstantsUtil.STATUS_NORMAL);
		int count = 0;
		count = moodDao.getTotal("t_mood", sql.toString());
		//保存操作日志
		operateLogService.saveOperateLog(user, request, null, StringUtil.getStringBufferStr(user.getAccount(),"查询用户ID为：", uid, "得到其已经发表成功的心情总数是：", count, "条").toString(), "getCountByUser()", ConstantsUtil.STATUS_NORMAL, 0);
				
		return count;
	}

	@Override
	public Map<String, Object> detail(JSONObject jo, UserBean user,
			HttpServletRequest request, String picSize){
		logger.info("MoodServiceImpl-->detail():jsonObject=" +jo.toString() +", user=" +user.getAccount());
		int mid = JsonUtil.getIntValue(jo, "mid", 0); //心情ID
		Map<String, Object> message = new HashMap<String, Object>();
		message.put("isSuccess", false);
		try {
			if(mid < 1){
				message.put("message", EnumUtil.getResponseValue(EnumUtil.ResponseCode.操作对象不存在.value));
				message.put("responseCode", EnumUtil.ResponseCode.操作对象不存在.value);
				return message;
			}
			List<Map<String, Object>> list = new ArrayList<Map<String,Object>>();
			list = moodHandler.getMoodDetail(mid, user);
			if(list.size() > 0){
				message.put("isSuccess", true);
				//从Redis缓存直接获取
				message.put("message", list);
			}else{
				message.put("message", EnumUtil.getResponseValue(EnumUtil.ResponseCode.操作对象不存在.value));
				message.put("responseCode", EnumUtil.ResponseCode.操作对象不存在.value);
			}
			
			//保存操作日志
			operateLogService.saveOperateLog(user, request, null, StringUtil.getStringBufferStr(user.getAccount(),"获取心情ID为", mid, "的详情").toString(), "detail()", ConstantsUtil.STATUS_NORMAL, 0);
					
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return message;
	}

	@Override
	public Map<String, Object> sendWord(JSONObject jsonObject, UserBean user, HttpServletRequest request) {
		logger.info("MoodServiceImpl-->sendWord():jsonObject=" +jsonObject.toString());
		String content = JsonUtil.getStringValue(jsonObject, "content");
		Map<String, Object> message = new HashMap<String, Object>();
		message.put("isSuccess", false);
		//过滤掉emoji
		content = EmojiUtil.filterEmoji(content);
		
		if(StringUtil.isNull(content)){
			message.put("message", EnumUtil.getResponseValue(EnumUtil.ResponseCode.某些参数为空.value));
			message.put("responseCode", EnumUtil.ResponseCode.某些参数为空.value);
			return message;
		}
		
		//从客户端获取uuid(有图片的情况下)，为空表示无图，有图就有值
		String uuid = JsonUtil.getStringValue(jsonObject, "uuid");
		
		//检测敏感词
		SensitivewordFilter filter = new SensitivewordFilter();
		long beginTime = System.currentTimeMillis();
		Set<String> set = filter.getSensitiveWord(content, 1);
		if(set.size() > 0){
			message.put("message", "有敏感词"+set.size()+"个:"+set.toString());
			message.put("responseCode", EnumUtil.ResponseCode.系统检测到有敏感词.value);
			long endTime = System.currentTimeMillis();
			System.out.println("总共消耗时间为：" + (endTime - beginTime));
			return message;
		}
		
		MoodBean moodBean = new MoodBean();
		moodBean.setContent(content);
		String location = JsonUtil.getStringValue(jsonObject, "location");
		if(StringUtil.isNotNull(location)){
			double longitude = JsonUtil.getDoubleValue(jsonObject, "longitude");
			double latitude = JsonUtil.getDoubleValue(jsonObject, "latitude");
			moodBean.setLocation(location);
			moodBean.setLongitude(longitude);
			moodBean.setLatitude(latitude);
		}
		moodBean.setCreateTime(new Date());
		moodBean.setFroms(JsonUtil.getStringValue(jsonObject, "froms"));
		moodBean.setPublishNow(true);
		moodBean.setStatus(ConstantsUtil.STATUS_NORMAL);
		moodBean.setCreateUser(user);
		moodBean.setUuid(uuid);
		if(!StringUtil.isNull(uuid)){
			moodBean.setHasImg(true);
		}
		
		boolean result = moodDao.save(moodBean);
		if(result){
			/*//通过观察者的模式发送消息通知
			Watched watched = new ConcreteWatched();       
	        Watcher watcher = new ConcreteWatcher();
	        watched.addWatcher(watcher);
	        watched.notifyWatchers(userService.findById(user.getId()), new UpdateMoodTemplate());*/
			TimeLineBean timeLineBean = new TimeLineBean();
			timeLineBean.setContent(moodBean.getContent());
			timeLineBean.setCreateTime(DateUtil.DateToString(new Date()));
			timeLineBean.setCreateUserId(moodBean.getCreateUser().getId());
			timeLineBean.setFroms(moodBean.getFroms());
			timeLineBean.setSource(null);
			timeLineBean.setHasSource(false);
			timeLineBean.setTableId(moodBean.getId());
			timeLineBean.setTableName(EnumUtil.DataTableType.心情.value);
			//更新用户的时间线
			circleOfFriendsHandler.upDateMyAndFansTimeLine(timeLineBean);
			
			//有@人通知相关人员
			Set<String> usernames = StringUtil.getAtUserName(content);
			if(usernames.size() > 0){
				//String str = "{from_user_remark}在发表的心情中@您,点击查看详情";
				notificationHandler.sendNotificationByNames(false, user, usernames, content, NotificationType.艾特我, "t_mood", moodBean.getId(), moodBean);
			}
			message.put("isSuccess", result);
		}else{
			message.put("message", EnumUtil.getResponseValue(EnumUtil.ResponseCode.服务器处理异常.value));
			message.put("responseCode", EnumUtil.ResponseCode.服务器处理异常.value);
		}
		
		// 保存发表心情日志信息
		String subject = user.getAccount() + "发表了心情" + StringUtil.getSuccessOrNoStr(result);
		this.operateLogService.saveOperateLog(user, request, new Date(), subject, "sendWord", ConstantsUtil.STATUS_NORMAL, 0);
		
		return message;
	}
	
	@Override
	public List<Map<String, Object>> detailImgs(JSONObject jo, UserBean user, HttpServletRequest request) {
		logger.info("MoodServiceImpl-->detailImgs():jsonObject=" +jo.toString() +", user=" +user.getAccount());
		int mid = JsonUtil.getIntValue(jo, "mid", 0); //心情ID
		List<Map<String, Object>> list = new ArrayList<Map<String,Object>>();
		try {
			if(mid < 1){
				throw new NullPointerException();
			}
			String tableName = JsonUtil.getStringValue(jo, "table_name"); //数据量表名
			String tableUuid = JsonUtil.getStringValue(jo, "table_uuid"); //uuid
			list = moodHandler.getMoodIms(tableName, tableUuid);
			//保存操作日志
			operateLogService.saveOperateLog(user, request, null, StringUtil.getStringBufferStr(user.getAccount(),"获取心情ID为", mid, "的图像列表").toString(), "detailImgs()", ConstantsUtil.STATUS_NORMAL, 0);
					
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return list;
	}

	@Override
	public Map<String, Object> search(JSONObject jo, UserBean user,
			HttpServletRequest request) {
		logger.info("MoodServiceImpl-->search():jo="+jo.toString());
		String searchKey = JsonUtil.getStringValue(jo, "searchKey");
		Map<String, Object> message = new HashMap<String, Object>();
		message.put("isSuccess", false);
		
		if(StringUtil.isNull(searchKey)){
			message.put("message", EnumUtil.getResponseValue(EnumUtil.ResponseCode.检索关键字不能为空.value));
			message.put("responseCode", EnumUtil.ResponseCode.检索关键字不能为空.value);
			return message;
		}
		
		List<Map<String, Object>> rs = moodDao.executeSQL("select id, content, uuid, froms, create_user_id, date_format(create_time,'%Y-%c-%d %H:%i:%s') create_time, has_img from t_mood where status=? and content like '%"+searchKey+"%' order by create_time desc limit 25", ConstantsUtil.STATUS_NORMAL);
		if(rs != null && rs.size() > 0){
			int createUserId = 0;
			boolean hasImg;
			String uuid;
			for(int i = 0; i < rs.size(); i++){
				createUserId = StringUtil.changeObjectToInt(rs.get(i).get("create_user_id"));
				hasImg = StringUtil.changeObjectToBoolean(rs.get(i).get("has_img"));
				uuid = StringUtil.changeNotNull(rs.get(i).get("uuid"));
				
				rs.get(i).putAll(userHandler.getBaseUserInfo(createUserId));
				//有图片的获取图片的路径
				if(hasImg && !StringUtil.isNull(uuid)){
					rs.get(i).put("imgs", moodHandler.getMoodImg("t_mood", uuid, ConstantsUtil.DEFAULT_PIC_SIZE));
				}
			}
		}
		message.put("isSuccess", true);
		message.put("message", rs);
		return message;
	}
}
