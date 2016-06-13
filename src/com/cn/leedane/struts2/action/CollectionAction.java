package com.cn.leedane.struts2.action;
import java.util.List;
import java.util.Map;

import net.sf.json.JSONObject;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.cn.leedane.Utils.BaseActionContext;
import com.cn.leedane.Utils.EnumUtil;
import com.cn.leedane.Utils.HttpUtils;
import com.cn.leedane.Utils.JsonUtil;
import com.cn.leedane.bean.CollectionBean;
import com.cn.leedane.service.CollectionService;
/**
 * 收藏夹action类
 * @author LeeDane
 * 2016年1月13日 上午9:43:34
 * Version 1.0
 */
public class CollectionAction extends BaseActionContext{	
	protected final Log log = LogFactory.getLog(getClass());
	private static final long serialVersionUID = 1L;
	
	//收藏夹service
	private CollectionService<CollectionBean> collectionService;
	
	public void setCollectionService(
			CollectionService<CollectionBean> collectionService) {
		this.collectionService = collectionService;
	}

	/**
	 * 添加收藏
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
			resIsSuccess = collectionService.addCollect(jo, user, request);
		} catch (Exception e) {
			e.printStackTrace();
		}
		message.put("isSuccess", resIsSuccess);
        return SUCCESS;
	}
	
	/**
	 * 删除收藏
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
			message.putAll(collectionService.deleteCollection(jo, user, request));
			return SUCCESS;
		} catch (Exception e) {
			e.printStackTrace();
		}
		message.put("message", EnumUtil.getResponseValue(EnumUtil.ResponseCode.服务器处理异常.value));
		message.put("responseCode", EnumUtil.ResponseCode.服务器处理异常.value);
        return SUCCESS;
	}
	
	/**
	 * 获取收藏列表
	 * @return
	 */
	public String paging(){
		message.put("isSuccess", resIsSuccess);
		try {
			JSONObject jo = HttpUtils.getJsonObjectFromInputStream(params,request);
			if(jo == null || jo.isEmpty()) {	
				message.put("message", EnumUtil.getResponseValue(EnumUtil.ResponseCode.缺少请求参数.value));
				message.put("responseCode", EnumUtil.ResponseCode.缺少请求参数.value);
				return SUCCESS;
			}
			
			//为了安全，必须是登录用户才能操作
			int toUserId = JsonUtil.getIntValue(jo, "toUserId");
			if(toUserId != user.getId()){
				message.put("message", EnumUtil.getResponseValue(EnumUtil.ResponseCode.没有操作权限.value));
				message.put("responseCode", EnumUtil.ResponseCode.没有操作权限.value);
				return SUCCESS;
			}
			
			List<Map<String, Object>> result= collectionService.getLimit(jo, user, request);
			System.out.println("获得收藏的数量：" +result.size());
			message.put("isSuccess", true);
			message.put("message", result);
			return SUCCESS;
		} catch (Exception e) {
			e.printStackTrace();
		}
        return SUCCESS;
	}
}
