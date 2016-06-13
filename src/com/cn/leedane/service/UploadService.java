package com.cn.leedane.service;
import java.io.Serializable;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import com.cn.leedane.bean.UploadBean;
import com.cn.leedane.bean.UserBean;
/**
 * 断点续传Service类
 * @author LeeDane
 * 2016年1月19日 下午3:03:54
 * Version 1.0
 */
public interface UploadService<T extends Serializable> extends BaseService<UploadBean>{

	/**
	 * 添加断点续传(已经存在直接返回true)
	 * @param upload
	 * @param user
	 * @param request
	 * @return
	 * @throws Exception
	 */
	public boolean addUpload(UploadBean upload, UserBean user, HttpServletRequest request) throws Exception;

	

	/**
	 * 删除单个断点续传记录(数据库和服务器断点片段)
	 * @param upload
	 * @param user
	 * @param request
	 * @return
	 */
	public boolean cancel(UploadBean upload, UserBean user, HttpServletRequest request) ;



	/**
	 * 获取当个文件的断点续传记录列表，按照从小到大排列
	 * @param tableUuid
	 * @param tableName
	 * @param order
	 * @param user
	 * @param request
	 * @return
	 */
	public List<Map<String, Object>> getOneUpload(String tableUuid, String tableName, int order, UserBean user,
			HttpServletRequest request);
}
