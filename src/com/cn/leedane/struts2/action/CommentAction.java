package com.cn.leedane.struts2.action;
import net.sf.json.JSONObject;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.cn.leedane.Utils.BaseActionContext;
import com.cn.leedane.Utils.EnumUtil;
import com.cn.leedane.Utils.HttpUtils;
import com.cn.leedane.bean.CommentBean;
import com.cn.leedane.service.CommentService;
/**
 * 评论action类
 * @author LeeDane
 * 2015年12月16日 上午11:32:05
 * Version 1.0
 */
public class CommentAction extends BaseActionContext{	
	protected final Log log = LogFactory.getLog(getClass());
	private static final long serialVersionUID = 1L;
	
	//评论service
	private CommentService<CommentBean> commentService;
	
	public void setCommentService(CommentService<CommentBean> commentService) {
		this.commentService = commentService;
	}
	
	/**
	 * 发表评论
	 * @return
	 */
	public String add(){
		long start = System.currentTimeMillis();
		message.put("isSuccess", resIsSuccess);
		try {
			JSONObject jo = HttpUtils.getJsonObjectFromInputStream(params,request);
			if(jo == null || jo.isEmpty()) {	
				message.put("message", EnumUtil.getResponseValue(EnumUtil.ResponseCode.缺少请求参数.value));
				message.put("responseCode", EnumUtil.ResponseCode.缺少请求参数.value);
				return SUCCESS;
			}
			message.put("isSuccess", commentService.add(jo, user, request));
			long end = System.currentTimeMillis();
			System.out.println("发表评论总计耗时：" +(end - start) +"毫秒");
			return SUCCESS;
		} catch (Exception e) {
			e.printStackTrace();
		}
		message.put("isSuccess", resIsSuccess);
        return SUCCESS;
	}

	/**
	 * 获取对象的主要评论列表
	 * @return
	 */
	public String paging(){
		long start = System.currentTimeMillis();
		message.put("isSuccess", resIsSuccess);
		try {
			JSONObject jo = HttpUtils.getJsonObjectFromInputStream(params,request);
			if(jo == null || jo.isEmpty()) {	
				message.put("message", EnumUtil.getResponseValue(EnumUtil.ResponseCode.缺少请求参数.value));
				message.put("responseCode", EnumUtil.ResponseCode.缺少请求参数.value);
				return SUCCESS;
			}
			message.put("message", commentService.getCommentsByLimit(jo, user, request));
			message.put("isSuccess", true);
			long end = System.currentTimeMillis();
			System.out.println("获取评论列表总计耗时：" +(end - start) +"毫秒");
			return SUCCESS;
		} catch (Exception e) {
			e.printStackTrace();
		}
		message.put("isSuccess", resIsSuccess);
        return SUCCESS;
	}
	
	
	/**
	 * 获取每条评论的子评论列表
	 * @return
	 */
	public String getItemsPaging(){
		long start = System.currentTimeMillis();
		message.put("isSuccess", resIsSuccess);
		try {
			JSONObject jo = HttpUtils.getJsonObjectFromInputStream(params,request);
			if(jo == null || jo.isEmpty()) {	
				message.put("message", EnumUtil.getResponseValue(EnumUtil.ResponseCode.缺少请求参数.value));
				message.put("responseCode", EnumUtil.ResponseCode.缺少请求参数.value);
				return SUCCESS;
			}
			message.put("message", commentService.getOneCommentItemsByLimit(jo, user, request));
			message.put("isSuccess", true);
			long end = System.currentTimeMillis();
			System.out.println("获取每条评论的子评论列表总计耗时：" +(end - start) +"毫秒");
			return SUCCESS;
		} catch (Exception e) {
			e.printStackTrace();
		}
		message.put("isSuccess", resIsSuccess);
        return SUCCESS;
	}
	
	/**
	 * 获取每一条评论的评论总数
	 * @return
	 */
	public String getCountByObject(){
		long start = System.currentTimeMillis();
		message.put("isSuccess", resIsSuccess);
		try {
			JSONObject jo = HttpUtils.getJsonObjectFromInputStream(params,request);
			if(jo == null || jo.isEmpty()) {	
				message.put("message", EnumUtil.getResponseValue(EnumUtil.ResponseCode.缺少请求参数.value));
				message.put("responseCode", EnumUtil.ResponseCode.缺少请求参数.value);
				return SUCCESS;
			}
			message.put("message", commentService.getCountByObject(jo, user, request));
			message.put("isSuccess", true);
			long end = System.currentTimeMillis();
			System.out.println("获取表的行记录评论数量总计耗时：" +(end - start) +"毫秒");
			return SUCCESS;
		} catch (Exception e) {
			e.printStackTrace();
		}
		message.put("isSuccess", resIsSuccess);
        return SUCCESS;
	}
	
	/**
	 * 获取用户所有的评论数量
	 * @return
	 */
	public String getCommentsCountByUser(){
		long start = System.currentTimeMillis();
		message.put("isSuccess", resIsSuccess);
		try {
			JSONObject jo = HttpUtils.getJsonObjectFromInputStream(params,request);
			if(jo == null || jo.isEmpty()) {	
				message.put("message", EnumUtil.getResponseValue(EnumUtil.ResponseCode.缺少请求参数.value));
				message.put("responseCode", EnumUtil.ResponseCode.缺少请求参数.value);
				return SUCCESS;
			}
			message.put("message", commentService.getCountByUser(jo, user, request));
			message.put("isSuccess", true);
			long end = System.currentTimeMillis();
			System.out.println("获取用户所有的评论数量总计耗时：" +(end - start) +"毫秒");
			return SUCCESS;
		} catch (Exception e) {
			e.printStackTrace();
		}
		message.put("isSuccess", resIsSuccess);
        return SUCCESS;
	}
	
	/**
	 * 删除评论
	 * @return
	 */
	public String delete() {
		try {
			message.put("isSuccess", false);
			//{"cid":1, "create_user_id":1}
			JSONObject jo = HttpUtils.getJsonObjectFromInputStream(params, request);  
			if(jo == null || jo.isEmpty()) {	
				message.put("message", EnumUtil.getResponseValue(EnumUtil.ResponseCode.缺少请求参数.value));
				message.put("responseCode", EnumUtil.ResponseCode.缺少请求参数.value);
				return SUCCESS;
			}
			message.putAll(commentService.deleteComment(jo, user, request));
			return SUCCESS;
		} catch (Exception e) {
			e.printStackTrace();
		}     
        message.put("message", EnumUtil.getResponseValue(EnumUtil.ResponseCode.服务器处理异常.value));
		message.put("responseCode", EnumUtil.ResponseCode.服务器处理异常.value);
        return SUCCESS;
	}
}
