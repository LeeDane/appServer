package com.cn.leedane.struts2.action;
import java.util.List;
import java.util.Map;

import net.sf.json.JSONObject;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.cn.leedane.Utils.BaseActionContext;
import com.cn.leedane.Utils.EnumUtil;
import com.cn.leedane.Utils.HttpUtils;
import com.cn.leedane.bean.GalleryBean;
import com.cn.leedane.service.GalleryService;
/**
 * 图库action类
 * @author LeeDane
 * 2016年1月15日 下午3:18:57
 * Version 1.0
 */
public class GalleryAction extends BaseActionContext{	
	protected final Log log = LogFactory.getLog(getClass());
	private static final long serialVersionUID = 1L;
	
	private GalleryService<GalleryBean> galleryService;
	
	public void setGalleryService(GalleryService<GalleryBean> galleryService) {
		this.galleryService = galleryService;
	}
	
	/**
	 * 添加网络链接到图库
	 * @return
	 */
	public String addLink(){
		message.put("isSuccess", resIsSuccess);
		try {
			JSONObject jo = HttpUtils.getJsonObjectFromInputStream(params,request);
			if(jo == null || jo.isEmpty()) {	
				message.put("message", EnumUtil.getResponseValue(EnumUtil.ResponseCode.缺少请求参数.value));
				message.put("responseCode", EnumUtil.ResponseCode.缺少请求参数.value);
				return SUCCESS;
			}
			message.putAll(galleryService.addLink(jo, user, request));
			return SUCCESS;
		} catch (Exception e) {
			e.printStackTrace();
		}
		message.put("message", EnumUtil.getResponseValue(EnumUtil.ResponseCode.服务器处理异常.value));
		message.put("responseCode", EnumUtil.ResponseCode.服务器处理异常.value);
        return SUCCESS;
	}
	
	/**
	 * 获取图库的照片列表
	 * @return
	 */
	public String getGalleryPaging(){
		message.put("isSuccess", resIsSuccess);
		try {
			JSONObject jo = HttpUtils.getJsonObjectFromInputStream(params,request);
			if(jo == null || jo.isEmpty()) {	
				message.put("message", EnumUtil.getResponseValue(EnumUtil.ResponseCode.缺少请求参数.value));
				message.put("responseCode", EnumUtil.ResponseCode.缺少请求参数.value);
				return SUCCESS;
			}
			List<Map<String, Object>> result= galleryService.getGalleryByLimit(jo, user, request);
			System.out.println("获得图库的数量：" +result.size());
			message.put("isSuccess", true);
			message.put("message", result);
			return SUCCESS;
		} catch (Exception e) {
			e.printStackTrace();
		}
        return SUCCESS;
	}
	

	/**
	 * 移出图库
	 * @return
	 */
	public String delete(){
		message.put("isSuccess", resIsSuccess);
		try {
			JSONObject jo = HttpUtils.getJsonObjectFromInputStream(params,request);
			if(jo == null || jo.isEmpty()) {	
				message.put("message", EnumUtil.getResponseValue(EnumUtil.ResponseCode.缺少请求参数.value));
				message.put("responseCode", EnumUtil.ResponseCode.缺少请求参数.value);
				return SUCCESS;
			}
			
			message.putAll(galleryService.delete(jo, user, request));
			return SUCCESS;
		} catch (Exception e) {
			e.printStackTrace();
		}
		message.put("message", EnumUtil.getResponseValue(EnumUtil.ResponseCode.服务器处理异常.value));
		message.put("responseCode", EnumUtil.ResponseCode.服务器处理异常.value);
        return SUCCESS;
	}
}
