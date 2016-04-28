package com.cn.leedane.handler;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.cn.leedane.Utils.ConstantsUtil;
import com.cn.leedane.Utils.StringUtil;
import com.cn.leedane.bean.CommentBean;
import com.cn.leedane.redis.util.RedisUtil;
import com.cn.leedane.service.CommentService;

/**
 * 评论的处理类
 * @author LeeDane
 * 2016年3月19日 下午11:10:06
 * Version 1.0
 */
@Component
public class CommentHandler {
	
	@Autowired
	private CommentService<CommentBean> commentService;
	
	private RedisUtil redisUtil = RedisUtil.getInstance();
	

	public void setCommentService(CommentService<CommentBean> commentService) {
		this.commentService = commentService;
	}
	public void addComment(String tableName, int tableId){
		/**
		 * 通过表名+ID唯一存储
		 */
		RedisUtil redisUtil = RedisUtil.getInstance();
		String key = getCommentKey(tableName, tableId);
		String count = null;
		//还没有添加到redis中
		if(StringUtil.isNull(redisUtil.getString(key))){
			//获取数据库中所有评论的数量
			List<Map<String, Object>> numbers = commentService.executeSQL("select count(id) number from t_comment where table_name=? and table_id = ?", tableName, tableId);
			count = String.valueOf(StringUtil.changeObjectToInt(numbers.get(0).get("number")));	
		}else{
			count = String.valueOf(Integer.parseInt(redisUtil.getString(key)) + 1);
		}
		redisUtil.addString(key, count);
	}
	
	/**
	 * 获取redis存储的key
	 * @param tableName
	 * @param tableId
	 * @return
	 */
	private String getCommentKey(String tableName, int tableId){
		StringBuffer buffer = new StringBuffer();
		buffer.append(ConstantsUtil.COMMENT_REDIS);
		buffer.append(tableName);
		buffer.append("_");
		buffer.append(tableId);
		return buffer.toString();
	}
	

	/**
	 * 获取评论总数
	 * @param tableId
	 * @param tableName
	 * @return
	 */
	public int getCommentNumber(int tableId, String tableName){
		String commentKey = getCommentKey(tableId, tableName);
		int commentNumber;
		//评论
		if(!redisUtil.hasKey(commentKey)){
			commentNumber = commentService.getTotal("t_comment", "where table_id = "+tableId+" and table_name='"+tableName+"'");
			redisUtil.addString(commentKey, String.valueOf(commentNumber));
		}else{
			commentNumber = Integer.parseInt(redisUtil.getString(commentKey));
		}
		return commentNumber;
	}
	
	/**
	 * 删除评论后修改评论数量
	 * @param tableId
	 * @param tableName
	 */
	public void deleteComment(int tableId, String tableName){
		String commentKey = getCommentKey(tableId, tableName);
		int commentNumber;
		//评论
		if(!redisUtil.hasKey(commentKey)){
			commentNumber = commentService.getTotal("t_comment", "where table_id = "+tableId+" and table_name='"+tableName+"'");
			redisUtil.addString(commentKey, String.valueOf(commentNumber));
		}else{
			commentNumber = Integer.parseInt(redisUtil.getString(commentKey));
			commentNumber = commentNumber -1;//评论减去1
			redisUtil.addString(commentKey, String.valueOf(commentNumber));
		}
	}
	
	/**
	 * 获取评论在redis的key
	 * @param id
	 * @return
	 */
	public static String getCommentKey(int tableId, String tableName){
		return ConstantsUtil.COMMENT_REDIS +tableName+"_"+tableId;
	}
}
