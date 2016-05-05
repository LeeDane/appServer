package com.cn.leedane.Dao.impl;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;

import com.cn.leedane.Dao.CommentDao;
import com.cn.leedane.Utils.ConstantsUtil;
import com.cn.leedane.bean.CommentBean;
/**
 * 评论Dao实现类
 * @author LeeDane
 * 2015年12月16日 上午10:57:25
 * Version 1.0
 */
public class CommentDaoImpl extends BaseDaoImpl<CommentBean> implements CommentDao<CommentBean>{
	Logger logger = Logger.getLogger(getClass());
	@Autowired
	private JdbcTemplate jdbcTemplate;
	@SuppressWarnings("deprecation")
	@Override
	public int getTotalComments(int userId) {
		return jdbcTemplate.queryForInt("select count(id) total_comments from t_comment where create_user_id = ? and status=?", userId, ConstantsUtil.STATUS_NORMAL);
	}
}
