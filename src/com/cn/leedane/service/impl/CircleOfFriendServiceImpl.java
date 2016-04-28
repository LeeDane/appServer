package com.cn.leedane.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import net.sf.json.JSONObject;

import org.apache.log4j.Logger;

import com.cn.leedane.Utils.ConstantsUtil;
import com.cn.leedane.Utils.JsonUtil;
import com.cn.leedane.Utils.StringUtil;
import com.cn.leedane.bean.OperateLogBean;
import com.cn.leedane.bean.TimeLineBean;
import com.cn.leedane.bean.UserBean;
import com.cn.leedane.handler.CircleOfFriendsHandler;
import com.cn.leedane.handler.CommonHandler;
import com.cn.leedane.handler.FriendHandler;
import com.cn.leedane.handler.UserHandler;
import com.cn.leedane.service.CircleOfFriendService;
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
	
	@Override
	public Map<String, Object> getLimit(JSONObject jo, UserBean user,
			HttpServletRequest request) {
		logger.info("CircleOfFriendServiceImpl-->getLimit():jsonObject=" +jo.toString() +", user=" +user.getAccount());
		String method = JsonUtil.getStringValue(jo, "method", "firstloading"); //操作方式
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
				
		return message;
	}
	
}
