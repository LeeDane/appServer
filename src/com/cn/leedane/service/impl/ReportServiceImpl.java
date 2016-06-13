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

import com.cn.leedane.Dao.ReportDao;
import com.cn.leedane.Utils.ConstantsUtil;
import com.cn.leedane.Utils.EnumUtil;
import com.cn.leedane.Utils.JsonUtil;
import com.cn.leedane.Utils.StringUtil;
import com.cn.leedane.bean.OperateLogBean;
import com.cn.leedane.bean.ReportBean;
import com.cn.leedane.bean.UserBean;
import com.cn.leedane.service.OperateLogService;
import com.cn.leedane.service.ReportService;
/**
 * 举报service的实现类
 * @author LeeDane
 * 2016年1月24日 下午8:13:04
 * Version 1.0
 */
public class ReportServiceImpl extends BaseServiceImpl<ReportBean> implements ReportService<ReportBean>{
	Logger logger = Logger.getLogger(getClass());
	
	private ReportDao<ReportBean> reportDao;
	
	public void setReportDao(ReportDao<ReportBean> reportDao) {
		this.reportDao = reportDao;
	}
	
	@Resource
	private OperateLogService<OperateLogBean> operateLogService;
	
	public void setOperateLogService(
			OperateLogService<OperateLogBean> operateLogService) {
		this.operateLogService = operateLogService;
	}

	@Override
	public Map<String, Object> addReport(JSONObject jo, UserBean user,
			HttpServletRequest request) throws Exception {
		//{\"table_name\":\"t_mood\", \"table_id\":2334, 'reason':'青色'}
		logger.info("ReportServiceImpl-->addReport():jsonObject=" +jo.toString() +", user=" +user.getAccount());
		String tableName = JsonUtil.getStringValue(jo, "table_name");
		int tableId = JsonUtil.getIntValue(jo, "table_id");
		int type = JsonUtil.getIntValue(jo, "type", 0);
		String reason = JsonUtil.getStringValue(jo, "reason");
		Map<String, Object> message = new HashMap<String, Object>();
		message.put("isSuccess", false);
		if(type == 0){
			message.put("message", EnumUtil.getResponseValue(EnumUtil.ResponseCode.缺少参数.value));
			message.put("responseCode", EnumUtil.ResponseCode.缺少参数.value);
			return message;
		}
		
		if(reportDao.exists(tableName, tableId, user.getId())){
			message.put("message", EnumUtil.getResponseValue(EnumUtil.ResponseCode.需要添加的记录已经存在.value));
			message.put("responseCode", EnumUtil.ResponseCode.需要添加的记录已经存在.value);
			return message;
		}
		
		if(!recordExists(tableName, tableId)){
			message.put("message", EnumUtil.getResponseValue(EnumUtil.ResponseCode.操作对象不存在.value));
			message.put("responseCode", EnumUtil.ResponseCode.操作对象不存在.value);
			return message;
		}
		ReportBean bean = new ReportBean();
		bean.setCreateTime(new Date());
		bean.setCreateUser(user);
		bean.setStatus(ConstantsUtil.STATUS_NORMAL);
		bean.setTableName(tableName);
		bean.setTableId(tableId);
		if(type == EnumUtil.ReportType.倾诉投诉.value){
			bean.setReason(reason);
		}else{
			bean.setReason(EnumUtil.getReportType(type));
		}
		bean.setType(type);
		boolean result = reportDao.save(bean);
		message.put("isSuccess", result);
		if(!result){
			message.put("message", EnumUtil.getResponseValue(EnumUtil.ResponseCode.数据库保存失败.value));
			message.put("responseCode", EnumUtil.ResponseCode.数据库保存失败.value);
		}
		
		//保存操作日志
		String subject = user.getAccount() + "举报，表名："+tableName+",表ID:"+tableId+StringUtil.getSuccessOrNoStr(result);
		this.operateLogService.saveOperateLog(user, request, new Date(), subject, "addReport()", 1 , 0);
		return message;
	}

	@Override
	public boolean cancel(JSONObject jo, UserBean user,
			HttpServletRequest request) {
		logger.info("ReportServiceImpl-->cancel():jsonObject=" +jo.toString() +", user=" +user.getAccount());
		String tableName = JsonUtil.getStringValue(jo, "table_name");
		int tableId = JsonUtil.getIntValue(jo, "table_id");
			
		try {
			return reportDao.updateSQL("delete from t_report where table_id = ? and table_name = ? and create_user_id=?", tableId, tableName, user.getId());
		} catch (Exception e) {
			e.printStackTrace();
		}
		return false;
	}

	@Override
	public List<Map<String, Object>> getLimit(JSONObject jo, UserBean user,
			HttpServletRequest request) {
		logger.info("ReportServiceImpl-->getLimit():jsonObject=" +jo.toString() +", user=" +user.getAccount());
		int userId = JsonUtil.getIntValue(jo, "uid", user.getId());
		String tableName = JsonUtil.getStringValue(jo, "table_name");
		int tableId = JsonUtil.getIntValue(jo, "table_id", 0);
		String method = JsonUtil.getStringValue(jo, "method", "firstloading"); //操作方式
		int pageSize = JsonUtil.getIntValue(jo, "pageSize", ConstantsUtil.DEFAULT_PAGE_SIZE); //每页的大小
		int lastId = JsonUtil.getIntValue(jo, "last_id"); //开始的页数
		int firstId = JsonUtil.getIntValue(jo, "first_id"); //结束的页数
		
		if(userId < 1)
			return new ArrayList<Map<String,Object>>();
		StringBuffer sql = new StringBuffer();
		List<Map<String, Object>> rs = new ArrayList<Map<String,Object>>();
		
		return rs;
	}
	
}
