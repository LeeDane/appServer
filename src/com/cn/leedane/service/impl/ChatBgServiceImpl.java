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
import com.cn.leedane.bean.OperateLogBean;
import com.cn.leedane.bean.UserBean;
import com.cn.leedane.handler.NotificationHandler;
import com.cn.leedane.handler.UserHandler;
import com.cn.leedane.service.ChatBgService;
import com.cn.leedane.service.OperateLogService;

/**
 * 聊天信息列表service实现类
 * @author LeeDane
 * 2016年5月5日 下午11:59:29
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
}
