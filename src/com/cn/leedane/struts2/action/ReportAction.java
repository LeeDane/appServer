package com.cn.leedane.struts2.action;
import java.util.List;
import java.util.Map;

import net.sf.json.JSONObject;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.cn.leedane.Utils.BaseActionContext;
import com.cn.leedane.Utils.EnumUtil;
import com.cn.leedane.Utils.HttpUtils;
import com.cn.leedane.bean.ReportBean;
import com.cn.leedane.service.ReportService;
/**
 * 举报action类
 * @author LeeDane
 * 2016年1月24日 下午8:42:09
 * Version 1.0
 */
public class ReportAction extends BaseActionContext{	
	protected final Log log = LogFactory.getLog(getClass());
	private static final long serialVersionUID = 1L;
	
	//举报service
	private ReportService<ReportBean> reportService;
	
	public void setReportService(ReportService<ReportBean> reportService) {
		this.reportService = reportService;
	}

	/**
	 * 添加举报
	 * @return
	 */
	public String add(){
		message.put("isSuccess", resIsSuccess);
		try {
			JSONObject jo = HttpUtils.getJsonObjectFromInputStream(params,request);
			if(jo == null || jo.isEmpty()) {	
				message.put("message", EnumUtil.getResponseValue(EnumUtil.ResponseCode.缺少请求参数.value));
				message.put("responseCode", EnumUtil.ResponseCode.缺少请求参数.value);
				return SUCCESS;
			}
			message.putAll(reportService.addReport(jo, user, request));
			return SUCCESS;
		} catch (Exception e) {
			e.printStackTrace();
		}
		message.put("message", EnumUtil.getResponseValue(EnumUtil.ResponseCode.服务器处理异常.value));
		message.put("responseCode", EnumUtil.ResponseCode.服务器处理异常.value);
        return SUCCESS;
	}
	
	/**
	 * 取消举报
	 * @return
	 */
	public String cancel(){
		long start = System.currentTimeMillis();
		message.put("isSuccess", resIsSuccess);
		try {
			JSONObject jo = HttpUtils.getJsonObjectFromInputStream(params,request);
			if(jo == null || jo.isEmpty()) {	
				message.put("message", EnumUtil.getResponseValue(EnumUtil.ResponseCode.缺少请求参数.value));
				message.put("responseCode", EnumUtil.ResponseCode.缺少请求参数.value);
				return SUCCESS;
			}
			resIsSuccess = reportService.cancel(jo, user, request);
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		message.put("isSuccess", resIsSuccess);
		long end = System.currentTimeMillis();
		System.out.println("取消举报总计耗时：" +(end - start) +"毫秒");
        return SUCCESS;
	}
	
	/**
	 * 获取举报列表
	 * @return
	 */
	public String getPagingCollections(){
		message.put("isSuccess", resIsSuccess);
		try {
			JSONObject jo = HttpUtils.getJsonObjectFromInputStream(params,request);
			if(jo == null || jo.isEmpty()) {	
				message.put("message", EnumUtil.getResponseValue(EnumUtil.ResponseCode.缺少请求参数.value));
				message.put("responseCode", EnumUtil.ResponseCode.缺少请求参数.value);
				return SUCCESS;
			}
			List<Map<String, Object>> result= reportService.getLimit(jo, user, request);
			System.out.println("获得举报的数量：" +result.size());
			message.put("isSuccess", true);
			message.put("message", result);
			return SUCCESS;
		} catch (Exception e) {
			e.printStackTrace();
		}
        return SUCCESS;
	}
}
