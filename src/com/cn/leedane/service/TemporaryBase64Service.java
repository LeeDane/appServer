package com.cn.leedane.service;
import java.io.Serializable;

import javax.servlet.http.HttpServletRequest;

import net.sf.json.JSONObject;

import com.cn.leedane.bean.TemporaryBase64Bean;
import com.cn.leedane.bean.UserBean;
/**
 * base64上传临时文件的Service类
 * @author LeeDane
 * 2015年12月1日 上午10:57:40
 * Version 1.0
 */
public interface TemporaryBase64Service<T extends Serializable> extends BaseService<TemporaryBase64Bean>{
	
	/**
	 * 保存临时上传的base64字符串文件
	 * @param jo
	 * @param user
	 * @param request
	 * @return
	 * @throws Exception
	 */
	public boolean saveBase64Str(JSONObject jo, UserBean user, HttpServletRequest request) throws Exception;
}
