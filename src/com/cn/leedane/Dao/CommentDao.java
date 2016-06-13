package com.cn.leedane.Dao;
import java.io.Serializable;

import com.cn.leedane.bean.CommentBean;

/**
 * 评论的Dao接口类
 * @author LeeDane
 * 2015年12月16日 上午10:56:04
 * Version 1.0
 */
public interface CommentDao<T extends Serializable> extends BaseDao<CommentBean>{
	/**
	 * 获取当前用户的总评论数
	 * @param userId
	 * @return
	 */
	public int getTotalComments(int userId);
}
