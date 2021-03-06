package com.cn.leedane.service;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import net.sf.json.JSONObject;

import com.cn.leedane.bean.GalleryBean;
import com.cn.leedane.bean.UserBean;

/**
 * 图库service接口类
 * @author LeeDane
 * 2016年1月15日 下午3:15:34
 * Version 1.0
 */
public interface GalleryService <T extends Serializable> extends BaseService<GalleryBean>{
	/**
	 * 把链接加入图库
	 * * "{'path':'http://img.baidu.com/fjff.jpg', 'desc':'网络图片', 'width': 100, 'height':100, 'length':1040449}"
	 * @param jo
	 * @param user
	 * @param request
	 * @return
	 * @throws Exception
	 */
	public Map<String, Object> addLink(JSONObject jo, UserBean user, HttpServletRequest request) throws Exception;

	
	/**
	 * 检查该图片是否已经加入图库
	 * @param user
	 * @param path
	 * @return
	 */
	public boolean isExist(UserBean user, String path);

	

	/**
	 * 取消关注
	 * {'gid':1}
	 * @param jo
	 * @param user
	 * @param request
	 * @return
	 */
	public Map<String, Object> delete(JSONObject jo, UserBean user, HttpServletRequest request) ;
	
	/**
	 * 分页获取指定用户图库的图片的路径列表
	 * "{'uid':1, 'pageSize':5, 'last_id': 1, 'first_id':1, 'method':'lowloading'}"(uploading, firstloading)
	 * @param jo
	 * @param user
	 * @param request
	 * @return
	 */
	public List<Map<String, Object>> getGalleryByLimit(JSONObject jo,
			UserBean user, HttpServletRequest request);
	
}
