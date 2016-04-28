package com.cn.leedane.service;

import java.io.Serializable;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import net.sf.json.JSONObject;

import com.cn.leedane.bean.FilePathBean;
import com.cn.leedane.bean.UserBean;

/**
 * App版本管理service接口类
 * @author LeeDane
 * 2016年3月27日 下午7:39:20
 * Version 1.0
 */
public interface AppVersionService <T extends Serializable> extends BaseService<FilePathBean>{
	/**
	 * 获取最新版本号
	 * @param jo 格式"{'version','1.0.0'}"
	 * @param user
	 * @param request
	 * @return
	 */
	public Map<String, Object> getNewest(JSONObject jo, UserBean user, HttpServletRequest request);
	
	
}
