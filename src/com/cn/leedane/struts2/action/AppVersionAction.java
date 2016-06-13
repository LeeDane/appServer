package com.cn.leedane.struts2.action;
import net.sf.json.JSONObject;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.cn.leedane.Utils.BaseActionContext;
import com.cn.leedane.Utils.EnumUtil;
import com.cn.leedane.Utils.HttpUtils;
import com.cn.leedane.bean.FilePathBean;
import com.cn.leedane.service.AppVersionService;
/**
 * AndroidApp版本action类
 * @author LeeDane
 * 2016年3月27日 下午7:36:55
 * Version 1.0
 */
public class AppVersionAction extends BaseActionContext{	
	protected final Log log = LogFactory.getLog(getClass());
	private static final long serialVersionUID = 1L;

	//上传filePath表的service
	private AppVersionService<FilePathBean> appVersionService;
	
	private int responseCode;//返回的编码
	
	public void setAppVersionService(
			AppVersionService<FilePathBean> appVersionService) {
		this.appVersionService = appVersionService;
	}
	
	/**
	 * 获取APP的最新版本信息
	 * @return
	 */
	public String getNewest(){
		long start = System.currentTimeMillis();	
        message.put("isSuccess", resIsSuccess);
		try {
			JSONObject jo = HttpUtils.getJsonObjectFromInputStream(params,request);
			if(jo == null || jo.isEmpty()) {	
				message.put("message", EnumUtil.getResponseValue(EnumUtil.ResponseCode.缺少请求参数.value));
				message.put("responseCode", EnumUtil.ResponseCode.缺少请求参数.value);
				return SUCCESS;
			}
			message.putAll(appVersionService.getNewest(jo, user, request));
			return SUCCESS;
		} catch (Exception e) {
			e.printStackTrace();
			resmessage = EnumUtil.getResponseValue(EnumUtil.ResponseCode.服务器处理异常.value);
			responseCode = EnumUtil.ResponseCode.服务器处理异常.value;
		}
		message.put("isSuccess", resIsSuccess);
		message.put("message", resmessage);
		message.put("responseCode", responseCode);
		long end = System.currentTimeMillis();
		System.out.println("检查最新app版本" +(end - start) +"毫秒");
        return SUCCESS;
	}
	
}
