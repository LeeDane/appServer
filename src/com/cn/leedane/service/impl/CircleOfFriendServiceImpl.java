package com.cn.leedane.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import net.sf.json.JSONObject;

import org.apache.log4j.Logger;

import com.cn.leedane.Utils.ConstantsUtil;
import com.cn.leedane.Utils.JsonUtil;
import com.cn.leedane.Utils.StringUtil;
import com.cn.leedane.bean.MoodBean;
import com.cn.leedane.bean.OperateLogBean;
import com.cn.leedane.bean.TimeLineBean;
import com.cn.leedane.bean.UserBean;
import com.cn.leedane.handler.CircleOfFriendsHandler;
import com.cn.leedane.handler.CommentHandler;
import com.cn.leedane.handler.CommonHandler;
import com.cn.leedane.handler.FanHandler;
import com.cn.leedane.handler.FriendHandler;
import com.cn.leedane.handler.MoodHandler;
import com.cn.leedane.handler.TransmitHandler;
import com.cn.leedane.handler.UserHandler;
import com.cn.leedane.handler.ZanHandler;
import com.cn.leedane.service.CircleOfFriendService;
import com.cn.leedane.service.MoodService;
import com.cn.leedane.service.OperateLogService;
/**
 * 朋友圈service的实现类
 * @author LeeDane
 * 2016年4月15日 下午4:32:33
 * Version 1.0
 */
public class CircleOfFriendServiceImpl implements CircleOfFriendService<TimeLineBean>{
	Logger logger = Logger.getLogger(getClass());

	@Resource
	private OperateLogService<OperateLogBean> operateLogService;
	
	public void setOperateLogService(
			OperateLogService<OperateLogBean> operateLogService) {
		this.operateLogService = operateLogService;
	}
	
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
	
	@Resource
	private CommonHandler commonHandler;
	
	public void setCommonHandler(CommonHandler commonHandler) {
		this.commonHandler = commonHandler;
	}
	
	@Resource
	private UserHandler userHandler;
	
	public void setUserHandler(UserHandler userHandler) {
		this.userHandler = userHandler;
	}
	
	@Resource
	private MoodService<MoodBean> moodService;
	
	public void setMoodService(MoodService<MoodBean> moodService) {
		this.moodService = moodService;
	}
	
	@Resource
	private CommentHandler commentHandler;
	@Resource
	private TransmitHandler transmitHandler;
	@Resource
	private ZanHandler zanHandler;
	
	public void setCommentHandler(CommentHandler commentHandler) {
		this.commentHandler = commentHandler;
	}
	
	public void setTransmitHandler(TransmitHandler transmitHandler) {
		this.transmitHandler = transmitHandler;
	}
	
	public void setZanHandler(ZanHandler zanHandler) {
		this.zanHandler = zanHandler;
	}
	
	@Resource
	private MoodHandler moodHandler;
	
	public void setMoodHandler(MoodHandler moodHandler) {
		this.moodHandler = moodHandler;
	}
	
	@Resource
	private FanHandler fanHandler;
	
	public void setFanHandler(FanHandler fanHandler) {
		this.fanHandler = fanHandler;
	}
	
	@Override
	public Map<String, Object> getLimit(JSONObject jo, UserBean user,
			HttpServletRequest request) {
		logger.info("CircleOfFriendServiceImpl-->getLimit():jsonObject=" +jo.toString() +", user=" +user.getAccount());
		/*String method = JsonUtil.getStringValue(jo, "method", "firstloading"); //操作方式
		int pageSize = JsonUtil.getIntValue(jo, "pageSize", ConstantsUtil.DEFAULT_PAGE_SIZE); //每页的大小
		int lastId = JsonUtil.getIntValue(jo, "last_id"); //开始的页数
		int firstId = JsonUtil.getIntValue(jo, "first_id"); //结束的页数
		List<Map<String, Object>> rs = new ArrayList<Map<String,Object>>();
		Map<String, Object> message = new HashMap<String, Object>();
		message.put("isSuccess", false);
		
		int start = 0;
		int end = 0;
		if(method.equalsIgnoreCase("firstloading")){
			start = 0;
			end = pageSize;
		//下刷新
		}else if(method.equalsIgnoreCase("lowloading")){
			
			start = lastId;
			end = lastId + pageSize -1;
		//上刷新
		}else if(method.equalsIgnoreCase("uploading")){
			start = 0;
			end = firstId + pageSize;
		}
		rs = circleOfFriendsHandler.getMyTimeLines(user.getId(), start, end);
		if(rs !=null && rs.size() > 0){
			int createUserId = 0;
			//String account = "";
			String tabName;
			boolean hasSource;
			int tabId;
			//为名字备注赋值
			for(int i = 0; i < rs.size(); i++){
				hasSource = StringUtil.changeObjectToBoolean(rs.get(i).get("hasSource"));
				if(hasSource){
					tabName = StringUtil.changeNotNull(rs.get(i).get("tableName"));
					tabId = StringUtil.changeObjectToInt(rs.get(i).get("tableId"));
					if(StringUtil.isNotNull(tabName) && tabId >0){
						//在非获取指定表下的评论列表的情况下的前面35个字符
						rs.get(i).put("source", commonHandler.getContentByTableNameAndId(tabName, tabId, user));
					}
				}
				createUserId = StringUtil.changeObjectToInt(rs.get(i).get("createUserId"));
				rs.get(i).putAll(userHandler.getBaseUserInfo(createUserId));
				
			}	
		}
		message.put("isSuccess", true);
		message.put("message", rs);
		//保存操作日志
		operateLogService.saveOperateLog(user, request, null, StringUtil.getStringBufferStr(user.getAccount(),"获取朋友圈列表").toString(), "getLimit()", ConstantsUtil.STATUS_NORMAL, 0);
				
		return message;*/
		
		long start = System.currentTimeMillis();
		List<Map<String, Object>> rs = new ArrayList<Map<String,Object>>();
		String method = JsonUtil.getStringValue(jo, "method", "firstloading"); //操作方式
		int pageSize = JsonUtil.getIntValue(jo, "pageSize", ConstantsUtil.DEFAULT_PAGE_SIZE); //每页的大小
		int lastId = JsonUtil.getIntValue(jo, "last_id"); //开始的页数
		int firstId = JsonUtil.getIntValue(jo, "first_id"); //结束的页数
		String picSize = ConstantsUtil.DEFAULT_PIC_SIZE; //JsonUtil.getStringValue(jo, "pic_size"); //图像的规格(大小)		

		Set<String> fids = fanHandler.getMyAttentions(user.getId());
		if(fids == null)
			fids = new HashSet<String>();
		fids.add(String.valueOf(user.getId()));
		
		StringBuffer sql = new StringBuffer();
		Map<String, Object> message = new HashMap<String, Object>();
		message.put("isSuccess", false);
		if("firstloading".equalsIgnoreCase(method)){
			sql.append("select m.id table_id, 't_mood' table_name, m.content, m.froms, m.uuid, m.create_user_id, date_format(m.create_time,'%Y-%c-%d %H:%i:%s') create_time, m.has_img,");
			sql.append(" m.read_number, m.zan_number, m.comment_number, m.transmit_number, m.share_number, u.account");
			sql.append(" from t_mood m inner join t_user u on u.id = m.create_user_id where m.status = ? and ");
			sql.append(buildCreateUserIdInSQL(fids));
			sql.append(" order by m.id desc limit 0,?");
			rs = moodService.executeSQL(sql.toString(), ConstantsUtil.STATUS_NORMAL, pageSize);
		//下刷新
		}else if("lowloading".equalsIgnoreCase(method)){
			sql.append("select m.id table_id, 't_mood' table_name, m.content, m.froms, m.uuid, m.create_user_id, date_format(m.create_time,'%Y-%c-%d %H:%i:%s') create_time, m.has_img,");
			sql.append(" m.read_number, m.zan_number, m.comment_number, m.transmit_number, m.share_number, u.account");
			sql.append(" from t_mood m inner join t_user u on u.id = m.create_user_id where m.status = ? and ");
			sql.append(buildCreateUserIdInSQL(fids));
			sql.append(" and m.id < ? order by m.id desc limit 0,? ");
			rs = moodService.executeSQL(sql.toString(), ConstantsUtil.STATUS_NORMAL, lastId, pageSize);
		//上刷新
		}else if("uploading".equalsIgnoreCase(method)){
			sql.append("select m.id table_id, 't_mood' table_name, m.content, m.froms, m.uuid, m.create_user_id, date_format(m.create_time,'%Y-%c-%d %H:%i:%s') create_time, m.has_img,");
			sql.append(" m.read_number, m.zan_number, m.comment_number, m.transmit_number, m.share_number, u.account");
			sql.append(" from t_mood m inner join t_user u on u.id = m.create_user_id where m.status = ? and ");
			sql.append(buildCreateUserIdInSQL(fids));
			sql.append(" and m.id > ? limit 0,?  ");
			rs = moodService.executeSQL(sql.toString(), ConstantsUtil.STATUS_NORMAL, firstId, pageSize);
		}
		if(rs !=null && rs.size() > 0){
			JSONObject friendObject = friendHandler.getFromToFriends(user.getId());
			System.out.println("friendObject："+friendObject.toString());
			int createUserId = 0;
			boolean hasImg ;
			String uuid;
			int moodId;
			//为名字备注赋值
			for(int i = 0; i < rs.size(); i++){
				createUserId = StringUtil.changeObjectToInt(rs.get(i).get("create_user_id"));
				hasImg = StringUtil.changeObjectToBoolean(rs.get(i).get("has_img"));
				uuid = StringUtil.changeNotNull(rs.get(i).get("uuid"));
				moodId = StringUtil.changeObjectToInt(rs.get(i).get("table_id"));
				if(createUserId> 0){
					if(createUserId != user.getId()){
						if(friendObject != null){
							
							rs.get(i).putAll(userHandler.getBaseUserInfo(createUserId, user, friendObject));
						}else{
							rs.get(i).putAll(userHandler.getBaseUserInfo(createUserId));
						}
					}else{
						rs.get(i).put("account", "本人");
					}
					rs.get(i).put("zan_users", zanHandler.getZanUser(moodId, "t_mood", user, 6));
					rs.get(i).put("comment_number", commentHandler.getCommentNumber(moodId, "t_mood"));
					rs.get(i).put("transmit_number", transmitHandler.getTransmitNumber(moodId, "t_mood"));
					rs.get(i).put("zan_number", zanHandler.getZanNumber(moodId, "t_mood"));
				}
				
				//有图片的获取图片的路径
				if(hasImg && !StringUtil.isNull(uuid)){
					//System.out.println("图片地："+moodHandler.getMoodImg("t_mood", uuid, picSize));
					rs.get(i).put("imgs", moodHandler.getMoodImg("t_mood", uuid, picSize));
				}
			}	
		}
		//保存操作日志
		operateLogService.saveOperateLog(user, request, null, StringUtil.getStringBufferStr(user.getAccount(),"获取朋友圈列表").toString(), "getLimit()", ConstantsUtil.STATUS_NORMAL, 0);
				
		long end = System.currentTimeMillis();
		System.out.println("获取好友圈列表总计耗时：" +(end - start) +"毫秒");
		message.put("message", rs);
		message.put("isSuccess", true);
		return message;
	}
	
	/**
	 * 构建CreateUser的SQL语句字符串
	 * @param ids
	 * @return
	 */
	private String buildCreateUserIdInSQL(Set<String> ids){
		int length = ids.size();
		if(length == 0) 
			return "";
		StringBuffer buffer = new StringBuffer();
		buffer.append(" m.create_user_id in (");
		int i = 0;
		for(String id: ids){
			if(i == length -1){
				buffer.append(id);
			}else{
				buffer.append(id + ",");
			}
			i++;
		}
		buffer.append(") ");
		
		return buffer.toString();
	}
	
}
