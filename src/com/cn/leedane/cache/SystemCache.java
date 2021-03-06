package com.cn.leedane.cache;
import com.cn.leedane.Utils.StringUtil;

import net.sf.ehcache.Cache;
import net.sf.ehcache.Element;

/**
 * 系统缓存类
 * @author LeeDane
 * 2015年7月21日 上午10:06:58
 * Version 1.0
 */
public class SystemCache implements com.cn.leedane.cache.ICache{
	
	//定义缓存对象
	private static Cache systemEhCache;
	
	public static void setSystemEhCache(Cache systemEhCache) {
		SystemCache.systemEhCache = systemEhCache;
	}
	
	public static Cache getSystemEhCache() {
		return systemEhCache;
	}
	/**
	 * 将value添加到缓存中(不强制覆盖已经存在的key)
	 * @param key  键名称
	 * @param value 值
	 */
	public void addCache(String key, Object value){		
		addCache(key, value, false);
	}
	
	/**
	 * 将value添加到缓存中
	 * @param key  键名称
	 * @param value 值
	 * @param isCover 对已经存在的key是否强制覆盖
	 */
	public void addCache(String key, Object value, boolean isCover){		
		//不覆盖，就是对已经存在key的数据，不需要再保存
		if(!isCover){
			//该key已经有缓存
			if(!getCache(key).equals("")) return;
		}
		
		//空的键名称将直接返回
		if(StringUtil.isNull(key)) return;
		
		//把键和值存放在节点中
		Element element = new Element(key, value);	
		
		//sessionFactory.close();
		
		//将节点加载进缓存中
		systemEhCache.put(element);
		
		/*System.out.println("end cache......");
		
		System.out.println(cache.get("LeeDane").getObjectValue());
		System.out.println(cache.getKeys());
		cache.removeAll();
		System.out.println("全部移除。。。。");
		System.out.println(cache.getKeys());*/
	}
	
	/**
	 * 根据key获得缓存
	 * @param key
	 * @return
	 */
	public Object getCache(String key){
		return  systemEhCache.get(key) == null ? "" : systemEhCache.get(key).getObjectValue();
	}

	/**
	 * 根据key移除该缓存
	 * @param key
	 * @return
	 */
	public void removeCache(String key){
		if(systemEhCache.get(key) != null){
			systemEhCache.remove(key);
		}
	}

}
