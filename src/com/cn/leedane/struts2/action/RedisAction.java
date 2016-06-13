package com.cn.leedane.struts2.action;
import net.sf.json.JSONObject;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.cn.leedane.Utils.BaseActionContext;
import com.cn.leedane.Utils.EnumUtil;
import com.cn.leedane.Utils.HttpUtils;
import com.cn.leedane.Utils.StringUtil;
import com.cn.leedane.redis.util.RedisUtil;
/**
 * Redis管理action类
 * @author LeeDane
 * 2016年3月30日 上午10:16:44
 * Version 1.0
 */
public class RedisAction extends BaseActionContext{	
	protected final Log log = LogFactory.getLog(getClass());
	private static final long serialVersionUID = 1L;
	
	
	
	
	/**
	 * 添加赞
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
			
			String key = jo.getString("key");
			if(StringUtil.isNull(key)){
				message.put("message", EnumUtil.getResponseValue(EnumUtil.ResponseCode.缺少参数.value));
				message.put("responseCode", EnumUtil.ResponseCode.缺少参数.value);
				return SUCCESS;
			}
			RedisUtil redisUtil = RedisUtil.getInstance();
			boolean result = redisUtil.delete(key);
			if(result){
				message.put("isSuccess", true);
			}else{
				message.put("message", EnumUtil.getResponseValue(EnumUtil.ResponseCode.操作失败.value));
				message.put("responseCode", EnumUtil.ResponseCode.操作失败.value);
			}
			return SUCCESS;
		} catch (Exception e) {
			e.printStackTrace();
		}
		message.put("message", EnumUtil.getResponseValue(EnumUtil.ResponseCode.服务器处理异常.value));
		message.put("responseCode", EnumUtil.ResponseCode.服务器处理异常.value);
        return SUCCESS;
	}
	
	/**
	 * 清除所有的缓存数据
	 * @return
	 */
	public String clearAll(){
		message.put("isSuccess", resIsSuccess);
		try {
			RedisUtil redisUtil = RedisUtil.getInstance();
			boolean result = redisUtil.clearAll();
			if(result){
				message.put("isSuccess", true);
			}else{
				message.put("message", EnumUtil.getResponseValue(EnumUtil.ResponseCode.操作失败.value));
				message.put("responseCode", EnumUtil.ResponseCode.操作失败.value);
			}
			return SUCCESS;
		} catch (Exception e) {
			e.printStackTrace();
		}
		message.put("message", EnumUtil.getResponseValue(EnumUtil.ResponseCode.服务器处理异常.value));
		message.put("responseCode", EnumUtil.ResponseCode.服务器处理异常.value);
        return SUCCESS;
	}
}
