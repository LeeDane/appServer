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

import com.cn.leedane.Dao.ChatBgDao;
import com.cn.leedane.Utils.ConstantsUtil;
import com.cn.leedane.Utils.EnumUtil;
import com.cn.leedane.Utils.JsonUtil;
import com.cn.leedane.Utils.StringUtil;
import com.cn.leedane.bean.ChatBgBean;
import com.cn.leedane.bean.ChatBgUserBean;
import com.cn.leedane.bean.OperateLogBean;
import com.cn.leedane.bean.ScoreBean;
import com.cn.leedane.bean.UserBean;
import com.cn.leedane.handler.ChatBgUserHandler;
import com.cn.leedane.handler.NotificationHandler;
import com.cn.leedane.handler.UserHandler;
import com.cn.leedane.redis.util.RedisUtil;
import com.cn.leedane.service.ChatBgService;
import com.cn.leedane.service.ChatBgUserService;
import com.cn.leedane.service.OperateLogService;
import com.cn.leedane.service.ScoreService;

/**
 * 聊天背景相关service实现类
 * @author LeeDane
 * 2016年6月5日 下午11:59:29
 * Version 1.0
 */
public class ChatBgServiceImpl extends BaseServiceImpl<ChatBgBean> implements ChatBgService<ChatBgBean> {
	Logger logger = Logger.getLogger(getClass());
	@Resource
	private ChatBgDao<ChatBgBean> chatBgDao;
	
	public void setChatBgDao(ChatBgDao<ChatBgBean> chatBgDao) {
		this.chatBgDao = chatBgDao;
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
	
	@Resource
	private ScoreService<ScoreBean> scoreService;
	
	public void setScoreService(ScoreService<ScoreBean> scoreService) {
		this.scoreService = scoreService;
	}
	
	@Resource
	private ChatBgUserService<ChatBgUserBean> chatBgUserService;
	
	public void setChatBgUserService(
			ChatBgUserService<ChatBgUserBean> chatBgUserService) {
		this.chatBgUserService = chatBgUserService;
	}
	
	@Override
	public Map<String, Object> paging(JSONObject jo, UserBean user,
			HttpServletRequest request) {
		logger.info("ChatBgServiceImpl-->paging():jo="+jo.toString());
		Map<String, Object> message = new HashMap<String, Object>();
		message.put("isSuccess", false);
		
		int lastId = JsonUtil.getIntValue(jo, "last_id", 0); //开始的页数
		int type = JsonUtil.getIntValue(jo, "type", 0); //聊天背景的类型，0：免费,1:收费, 2:全部
		int firstId = JsonUtil.getIntValue(jo, "first_id"); //开始的页数
		String method = JsonUtil.getStringValue(jo, "method", "firstloading"); //操作方式
		int pageSize = JsonUtil.getIntValue(jo, "pageSize", ConstantsUtil.DEFAULT_PAGE_SIZE); //每页的大小
		
		StringBuffer sql = new StringBuffer();
		List<Map<String, Object>> rs = new ArrayList<>();
	
		if("firstloading".equalsIgnoreCase(method)){
			sql = new StringBuffer();
			sql.append("select c.id, c.create_user_id, c.path, c.chat_bg_desc, c.type, c.score, date_format(c.create_time,'%Y-%c-%d %H:%i:%s') create_time ");
			sql.append(" from t_chat_bg c");
			sql.append(" where c.status=? ");
			sql.append(buildChatBgTypeSql(type));
			sql.append(" order by c.id desc limit 0,?");
			rs = chatBgDao.executeSQL(sql.toString(), ConstantsUtil.STATUS_NORMAL, pageSize);
		//下刷新
		}else if("lowloading".equalsIgnoreCase(method)){
			sql = new StringBuffer();
			sql.append("select c.id, c.create_user_id, c.path, c.chat_bg_desc, c.type, c.score, date_format(c.create_time,'%Y-%c-%d %H:%i:%s') create_time ");
			sql.append(" from t_chat_bg c");
			sql.append(" where c.status=? ");
			sql.append(buildChatBgTypeSql(type));
			sql.append(" and c.id < ? order by c.id desc limit 0,? ");
			rs = chatBgDao.executeSQL(sql.toString(), ConstantsUtil.STATUS_NORMAL, lastId, pageSize);
		//上刷新
		}else if("uploading".equalsIgnoreCase(method)){
			sql = new StringBuffer();
			sql.append("select c.id, c.create_user_id, c.path, c.chat_bg_desc, c.type, c.score, date_format(c.create_time,'%Y-%c-%d %H:%i:%s') create_time ");
			sql.append(" from t_chat_bg c");
			sql.append(" where c.status=? ");
			sql.append(buildChatBgTypeSql(type));
			sql.append(" and c.id > ? limit 0,? ");
			rs = chatBgDao.executeSQL(sql.toString(), ConstantsUtil.STATUS_NORMAL, firstId, pageSize);
		}
		//保存操作日志
		operateLogService.saveOperateLog(user, request, null, StringUtil.getStringBufferStr(user.getAccount(),"获取聊天背景分页列表").toString(), "paging()", ConstantsUtil.STATUS_NORMAL, 0);
			
		message.put("isSuccess", true);
		message.put("message", rs);
		return message;
	}

	/**
	 * 构建聊天背景颜色类型SQL
	 * @param type
	 * @return
	 */
	private String buildChatBgTypeSql(int type) {
		String sql = "";
		switch(type){
			case 0: //免费
				sql = " and c.type = 0 ";
				break;
			case 1:  //收费
				sql = " and c.type = 1 ";
				break;
			case 2://全部
				sql = " and c.type in(0, 1) ";
				break;
		}
		return sql;
	}


	@Override
	public Map<String, Object> publish(JSONObject jo, UserBean user,
			HttpServletRequest request) {
		logger.info("ChatBgServiceImpl-->send():jo="+jo.toString());
		Map<String, Object> message = new HashMap<String, Object>();
		message.put("isSuccess", false);
		String desc = JsonUtil.getStringValue(jo, "desc"); //聊天背景的描述
		String path = JsonUtil.getStringValue(jo, "path"); //聊天背景的路径	
		int type = JsonUtil.getIntValue(jo, "type", 0); //聊天背景的类型
		int score = JsonUtil.getIntValue(jo, "score", 0); //聊天背景的扣除的分数
		
		if(type == 1 && score < 1){
			message.put("message", EnumUtil.getResponseValue(EnumUtil.ResponseCode.请填写每次用户每次下载扣取的积分.value));
			message.put("responseCode", EnumUtil.ResponseCode.请填写每次用户每次下载扣取的积分.value);
			return message;
		}
		
		//检查是否有数据存在
		if(this.chatBgDao.executeSQL("select id from t_chat_bg where create_user_id = ? and path=?", user.getId(), path).size() > 0 ){
			message.put("message", EnumUtil.getResponseValue(EnumUtil.ResponseCode.需要添加的记录已经存在.value));
			message.put("responseCode", EnumUtil.ResponseCode.需要添加的记录已经存在.value);
			return message;
		}
		
		ChatBgBean chatBgBean = new ChatBgBean();
		chatBgBean.setDesc(desc);
		chatBgBean.setCreateTime(new Date());
		chatBgBean.setCreateUser(user);
		chatBgBean.setStatus(ConstantsUtil.STATUS_NORMAL);
		chatBgBean.setPath(path);
		chatBgBean.setType(type);
		chatBgBean.setScore(score);
		
		boolean result = chatBgDao.save(chatBgBean);
		//保存操作日志
		operateLogService.saveOperateLog(user, request, null, StringUtil.getStringBufferStr(user.getAccount(), "发布聊天背景到平台", StringUtil.getSuccessOrNoStr(result)).toString(), "send()", ConstantsUtil.STATUS_NORMAL, 0);
		if(result){
			message.put("isSuccess", result);
			message.put("message", "发布聊天背景成功");
		}else{
			message.put("message", EnumUtil.getResponseValue(EnumUtil.ResponseCode.服务器处理异常.value));
			message.put("responseCode", EnumUtil.ResponseCode.服务器处理异常.value);
		}
		return message;
	}


	@Override
	public Map<String, Object> verifyChatBg(JSONObject jo, UserBean user,
			HttpServletRequest request) {
		logger.info("ChatBgServiceImpl-->verifyChatBg():jo="+jo.toString());
		Map<String, Object> message = new HashMap<String, Object>();
		message.put("isSuccess", false);
		int cid = JsonUtil.getIntValue(jo, "cid", 0); //聊天背景的ID
		
		ChatBgBean chatBg = null;
		if(cid < 1 || (chatBg = chatBgDao.findById(cid)) == null){
			message.put("message", EnumUtil.getResponseValue(EnumUtil.ResponseCode.操作对象不存在.value));
			message.put("responseCode", EnumUtil.ResponseCode.操作对象不存在.value);
			return message;
		}
		
		if(chatBg.getCreateUser().getId() == user.getId()){
			message.put("message", EnumUtil.getResponseValue(EnumUtil.ResponseCode.自己上传的聊天背景资源.value));
			message.put("isSuccess", true);
			return message;
		}
		
		RedisUtil redisUtil = RedisUtil.getInstance();
		
		String chatBgUserKey = ChatBgUserHandler.getChatBgUserKey(user.getId(), cid);
		boolean result = false;
		//还没有缓存记录
		if(!redisUtil.hasKey(chatBgUserKey)){
			System.out.println("没有缓存");
			boolean e = chatBgUserService.exists(user.getId(), cid);
			if(!e){//还没有下载记录
				redisUtil.addString(chatBgUserKey, "true");
				//保存下载记录
				ChatBgUserBean t = new ChatBgUserBean();
				t.setChatBgTableId(chatBg.getId());
				t.setCreateTime(new Date());
				t.setCreateUser(user);
				t.setStatus(ConstantsUtil.STATUS_NORMAL);
				result = chatBgUserService.save(t);
				if(chatBg.getType() == 0){
					message.put("message", "这个是免费的资源，不需要扣下载积分");
					message.put("isSuccess", true);
					return message;
				}
				result = saveScore(chatBg, user);
			}else{
				redisUtil.addString(chatBgUserKey, "false");
				message.put("message", "已经下载过该资源");
				message.put("isSuccess", true);
				return message;
			}
		}else{
			String val = redisUtil.getString(chatBgUserKey);
			System.out.println("已经缓存："+val);
			if(StringUtil.isNotNull(val)){
				System.out.println("缓存："+StringUtil.changeObjectToBoolean(val));
				if(!StringUtil.changeObjectToBoolean(val)){
					redisUtil.addString(chatBgUserKey, "true");
					//保存下载记录
					ChatBgUserBean t = new ChatBgUserBean();
					t.setChatBgTableId(chatBg.getId());
					t.setCreateTime(new Date());
					t.setCreateUser(user);
					t.setStatus(ConstantsUtil.STATUS_NORMAL);
					result = chatBgUserService.save(t);
					if(chatBg.getType() == 0){
						message.put("message", "这个是免费的资源，不需要扣下载积分");
						message.put("isSuccess", true);
						return message;
					}
					result = saveScore(chatBg, user);
				}else{
					message.put("message", "已经下载过该资源");
					message.put("isSuccess", true);
					return message;
				}
			}else{
				message.put("message", EnumUtil.getResponseValue(EnumUtil.ResponseCode.服务器处理异常.value));
				message.put("responseCode", EnumUtil.ResponseCode.服务器处理异常.value);
				return message;
			}
		}
		
		//保存操作日志
		operateLogService.saveOperateLog(user, request, null, StringUtil.getStringBufferStr(user.getAccount(), "下载聊天背景的资源", StringUtil.getSuccessOrNoStr(result)).toString(), "verifyChatBg()", ConstantsUtil.STATUS_NORMAL, 0);
		if(result){
			message.put("isSuccess", result);
			message.put("message", "扣除下载积分成功!");
		}else{
			message.put("message", EnumUtil.getResponseValue(EnumUtil.ResponseCode.数据库保存失败.value));
			message.put("responseCode", EnumUtil.ResponseCode.数据库保存失败.value);
		}
		return message;
	}
	
	private boolean saveScore(ChatBgBean chatBg, UserBean user){
		boolean result = false;
		Date d = new Date();  //共用时间
		int bgScore = chatBg.getScore();  //下载需要处理的积分
		//扣除用户下载积分
		ScoreBean scoreBean = new ScoreBean();
		int score = 0 - bgScore;
		scoreBean.setTotalScore(scoreService.getTotalScore(user.getId()) + score);
		scoreBean.setScore(score);
		scoreBean.setCreateTime(d);
		scoreBean.setCreateUser(user);
		scoreBean.setDesc("下载聊天背景");
		scoreBean.setStatus(ConstantsUtil.STATUS_NORMAL);
		scoreBean.setTableId(chatBg.getId());
		scoreBean.setTableName("t_chat_bg");
		result = scoreService.save(scoreBean);
		if(result){
			//增加用户下载资源积分
			ScoreBean scoreBean1 = new ScoreBean();
			scoreBean1.setTotalScore(scoreService.getTotalScore(user.getId()) + bgScore);
			scoreBean1.setScore(bgScore);
			scoreBean1.setCreateTime(d);
			scoreBean1.setCreateUser(chatBg.getCreateUser());
			scoreBean1.setDesc("下载聊天背景");
			scoreBean1.setStatus(ConstantsUtil.STATUS_NORMAL);
			scoreBean1.setTableId(chatBg.getId());
			scoreBean1.setTableName("t_chat_bg");
			result = scoreService.save(scoreBean1);
		}
		return result;
	}

}
