package com.cn.leedane.service;
import java.io.Serializable;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import net.sf.json.JSONObject;

import com.cn.leedane.bean.MoodBean;
import com.cn.leedane.bean.UserBean;

/**
 * 心情service接口类
 * @author LeeDane
 * 2015年11月22日 下午10:41:49
 * Version 1.0
 */
public interface MoodService <T extends Serializable> extends BaseService<MoodBean>{
	/**
	 * 保存心情（草稿状态）
	 * @param jsonObject
	 * @param user
	 * @param status 状态
	 * @param request
	 * @return
	 */
	public Map<String, Object> saveMood(JSONObject jsonObject, UserBean user, int status, HttpServletRequest request)throws Exception;
	
	/**
	 * 保存纯文字的心情
	 * @param jsonObject
	 * @param user
	 * @param request
	 * @return
	 */
	public Map<String, Object> sendWord(JSONObject jsonObject, UserBean user, HttpServletRequest request);
	
	/**
	 * 更新心情状态
	 * @param jsonObject
	 * @param status
	 * @param request
	 * @param user
	 * @return
	 */
	public Map<String, Object> updateMoodStatus(JSONObject jsonObject, int status, HttpServletRequest request, UserBean user);

	/**
	 * 删除心情(逻辑删除)
	 * @param jo
	 * @param user
	 * @param request
	 * @return
	 */
	public Map<String, Object> deleteMood(JSONObject jo, UserBean user, HttpServletRequest request);

	/**
	 * 获取登录用户的心情列表
	 * @param jo 格式{"uid":1, "pageSize":5, "last_id": 1, "first_id":1, "method":"lowloading"}(uploading, firstloading)
	 * @param user
	 * @param request
	 * @return
	 */
	public Map<String, Object> getMoodByLimit(JSONObject jo, UserBean user, HttpServletRequest request);

	
	/**
	 * 保存上传的base64字符串的信息
	 * @param jo
	 * @param user
	 * @param request
	 * @return
	 */
	public boolean saveBase64Str(JSONObject jo, UserBean user, HttpServletRequest request);
	
	/**
	 * 保存心情（应用于已经上传完图片之后的发表）
	 * @param jsonObject
	 * @param user
	 * @param status 状态
	 * @param request
	 * @return
	 */
	public Map<String, Object> saveDividedMood(JSONObject jsonObject, UserBean user, int status, HttpServletRequest request);

	/**
	 * 获取指定用户所有的发表成功的心情总数
	 * {"mid":1"},非必须，为空表示当前登录用户
	 * @param jo
	 * @param user
	 * @param request
	 * @return
	 * @throws Exception
	 */
	public int getCountByUser(JSONObject jo, UserBean user, HttpServletRequest request);
	
	/**
	 * 获取指定心情的列表
	 * {'uid':1; 'mid':1},uid非必须，为空表示当前登录用户
	 * @param jo
	 * @param user
	 * @param request
	 * @params picSize 图像的大小，如：120x120
	 * @return
	 * @throws Exception
	 */
	public Map<String, Object> detail(JSONObject jo, UserBean user,
			HttpServletRequest request, String picSize) ;
	/**
	 * 获取指定心情的图片列表
	 * {'table_uuid':'leedane4e2f2684-ac82-4186-97fa-d8807211ef92', 'table_name':'t_mood'},uuid必须
	 * @param jo
	 * @param user
	 * @param request
	 * @return
	 * @throws Exception
	 */
	public List<Map<String, Object>> detailImgs(JSONObject jo, UserBean user,
			HttpServletRequest request) ;
}
