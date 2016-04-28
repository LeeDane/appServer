package com.cn.leedane.service;
import java.io.Serializable;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import net.sf.json.JSONObject;

import com.cn.leedane.bean.CommentBean;
import com.cn.leedane.bean.UserBean;
/**
 * 评论的Service类
 * @author LeeDane
 * 2015年12月16日 上午10:58:37
 * Version 1.0
 */
public interface CommentService<T extends Serializable> extends BaseService<CommentBean>{

	/**
	 * 发表评论
	 * {\"table_name\":\"t_mood\", \"table_id\":123, \"content\":\"我同意\"
	 * ,\"level\": 1, \"pid\":23, \"cid\":1, \"from\":\"Android客户端\"}
	 * @param jo
	 * @param user
	 * @param request
	 * @return
	 * @throws Exception
	 */
	public boolean add(JSONObject jo, UserBean user, HttpServletRequest request);

	/**
	 * 获取分页的评论
	 * {\"table_name\":\"t_mood\", \"table_id\":123,\"pageSize\":5
	 * , \"first_id\": 2, \"last_id\":2, \"method\":\"firstloading\"}
	 * @param jo
	 * @param user
	 * @param request
	 * @return
	 * @throws Exception
	 */
	public List<Map<String, Object>> getCommentsByLimit(JSONObject jo, UserBean user, HttpServletRequest request) throws Exception;
	
	/**
	 * 获得当个评论的所有子评论列表
	 *  {\"table_name\":\"t_mood\",\"cid\":1, \"table_id\":123,\"pageSize\":5
	 * , \"first_id\": 2, \"last_id\":2, \"method\":\"firstloading\"}
	 * @param jo
	 * @param user
	 * @param request
	 * @return
	 * @throws Exception
	 */
	public List<Map<String, Object>> getOneCommentItemsByLimit(JSONObject jo, UserBean user, HttpServletRequest request) throws Exception;

	/**
	 * 获取每个评论对象所有的评论数
	 * {\"table_name\":\"t_mood\", \"table_id\":123}
	 * @param jo
	 * @param user
	 * @param request
	 * @return
	 * @throws Exception
	 */
	public int getCountByObject(JSONObject jo, UserBean user,
			HttpServletRequest request) throws Exception;

	/**
	 * 获取每个用户的评论数量
	 *  {\"uid\":1}
	 * @param jo
	 * @param user
	 * @param request
	 * @return
	 */
	public int getCountByUser(JSONObject jo, UserBean user,
			HttpServletRequest request) throws Exception;

	/**
	 * 删除评论
	 * {"cid":1, "create_user_id":1}
	 * @param jo
	 * @param user
	 * @param request
	 * @return
	 */
	public Map<String, Object> deleteComment(JSONObject jo, UserBean user, HttpServletRequest request);
}
