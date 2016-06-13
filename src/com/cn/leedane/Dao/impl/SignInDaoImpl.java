package com.cn.leedane.Dao.impl;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;

import com.cn.leedane.Dao.SignInDao;
import com.cn.leedane.Utils.ConstantsUtil;
import com.cn.leedane.Utils.DateUtil;
import com.cn.leedane.Utils.StringUtil;
import com.cn.leedane.bean.SignInBean;

/**
 * 签到dao实现类
 * @author LeeDane
 * 2015年7月10日 下午6:17:11
 * Version 1.0
 */
public class SignInDaoImpl extends BaseDaoImpl<SignInBean> implements SignInDao<SignInBean>{
	Logger logger = Logger.getLogger(getClass());
	@Autowired
	private JdbcTemplate jdbcTemplate;

	@Override
	public boolean isSign(int userId, String dateTime) {	
		logger.info("SignInDaoImpl-->isSign():userId="+userId+",dateTime="+dateTime);
		String sql = "select id from t_sign_in where DATE(create_time) = ? and create_user_id = ? ";
		return jdbcTemplate.queryForList(sql, dateTime, userId).size() > 0;
	}
	
	@Override
	public boolean hasHistorySign(int userId) {
		logger.info("SignInDaoImpl-->hasHistorySign():userId="+userId);
		String sql = "select id from t_sign_in where create_user_id = ? limit 1";
		return jdbcTemplate.queryForList(sql, userId).size() > 0;
	}
	
	public boolean save(SignInBean bean, int userId){
		logger.info("SignInDaoImpl-->save():bean="+bean.toString()+",userId="+userId);
		
		try {
			this.getHibernateTemplate().save(bean);
			this.getHibernateTemplate().flush();
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}

	@Override
	public List<Map<String, Object>> getNewestRecore(int userId) {
		logger.info("SignInDaoImpl-->getNewestRecore():userId="+userId);
		String sql = "select * from t_sign_in where create_user_id = ? order by create_time desc limit 1";
		return jdbcTemplate.queryForList(sql, userId);
	}

	@Override
	public List<Map<String, Object>> getYesTodayRecore(int uid) {	
		//判断昨天是否签到，签到就获取昨天的连续签到数次
		String dateTime = DateUtil.DateToString(DateUtil.getYestoday(), "yyyy-MM-dd");
		String sql = "select * from t_sign_in where DATE(create_time) = ? and create_user_id = ? and status = ? ";
		return jdbcTemplate.queryForList(sql, dateTime, uid, ConstantsUtil.STATUS_NORMAL) ;
	}

	@Override
	public int getScore(int uid) {
		String sql = "select s.score from t_sign_in s where  s.create_user_id = ? and status = ? order by s.id desc limit 0,1";
		int score = 0;
		List<Map<String, Object>> list =jdbcTemplate.queryForList(sql, uid, ConstantsUtil.STATUS_NORMAL);
		if(list != null && list.size() ==1){
			score = StringUtil.changeObjectToInt(list.get(0).get("score"));
		}
		
		return score;
	}
	
}
