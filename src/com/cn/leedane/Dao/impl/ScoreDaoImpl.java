package com.cn.leedane.Dao.impl;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;

import com.cn.leedane.Dao.ScoreDao;
import com.cn.leedane.Utils.ConstantsUtil;
import com.cn.leedane.bean.ScoreBean;
/**
 * 积分Dao实现类
 * @author LeeDane
 * 2016年3月4日 下午5:29:32
 * Version 1.0
 */
public class ScoreDaoImpl extends BaseDaoImpl<ScoreBean> implements ScoreDao<ScoreBean>{
	Logger logger = Logger.getLogger(getClass());
	@Autowired
	private JdbcTemplate jdbcTemplate;
	
	@SuppressWarnings("deprecation")
	@Override
	public int getTotalScore(int userId) {
		return jdbcTemplate.queryForInt("select sum(score) total_score from t_score where create_user_id = ? and status=?", userId, ConstantsUtil.STATUS_NORMAL);
	}


}
