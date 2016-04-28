package com.cn.leedane.handler;

import net.sf.json.JSONObject;

import org.springframework.stereotype.Component;

import com.cn.leedane.Utils.ConstantsUtil;
import com.cn.leedane.Utils.JsonUtil;
import com.cn.leedane.Utils.StringUtil;
import com.cn.leedane.redis.util.RedisUtil;
import com.cn.leedane.wechat.bean.WeixinCacheBean;
import com.cn.leedane.wechat.util.WeixinUtil;

/**
 * 微信的处理类
 * @author LeeDane
 * 2016年4月7日 上午10:16:10
 * Version 1.0
 */
@Component
public class WechatHandler {
	
	
	private RedisUtil redisUtil = RedisUtil.getInstance();
	
	/**
	 * 添加该用户的缓存对象
	 * @param FromUserName
	 * @param cacheBean
	 */
	public void addCache(String FromUserName, WeixinCacheBean cacheBean){
		String fromUserKey = getWechatKey(FromUserName);
		JSONObject jsonObject = JSONObject.fromObject(cacheBean);
		redisUtil.addString(fromUserKey, jsonObject.toString());
	}
	
	/**
	 * 获取当前用户的缓存对象
	 * @param FromUserName
	 * @return
	 */
	public WeixinCacheBean getFromUserInfo(String FromUserName){
		String fromUserKey = getWechatKey(FromUserName);
		if(redisUtil.hasKey(fromUserKey)){
			String value = redisUtil.getString(fromUserKey);
			if(StringUtil.isNotNull(value)){
				System.out.println("WeixinCacheBean:"+value);
				return stringToCacheBean(value);
			}
		}
		return null;
	}
	
	/**
	 * 移出该用户的缓存对象
	 * @param FromUserName
	 */
	public void removeCache(String FromUserName){
		String fromUserKey = getWechatKey(FromUserName);
		redisUtil.delete(fromUserKey);
	}
	
	/**
	 * 将字符串转成WeixinCacheBean对象
	 * @param value
	 * @return
	 */
	private WeixinCacheBean stringToCacheBean(String value) {
		JSONObject jsonObject = JSONObject.fromObject(value);	
		String currentType = JsonUtil.getStringValue(jsonObject, "currentType", WeixinUtil.MODEL_MAIN_MENU);
		boolean isBindLogin = JsonUtil.getBooleanValue(jsonObject, "bindLogin");
		int lastBlogId = JsonUtil.getIntValue(jsonObject, "lastBlogId");
		WeixinCacheBean cacheBean = new WeixinCacheBean();
		cacheBean.setCurrentType(currentType);
		cacheBean.setBindLogin(isBindLogin);
		cacheBean.setLastBlogId(lastBlogId);
		return cacheBean;
	}

	/**
	 * 获得微信存储的用户对象的key
	 * @param FromUserName
	 * @return
	 */
	private String getWechatKey(String FromUserName) {
		return ConstantsUtil.WECHAT_USER_REDIS +FromUserName;
	}
}
