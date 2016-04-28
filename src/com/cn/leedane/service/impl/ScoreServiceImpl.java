package com.cn.leedane.service.impl;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import com.cn.leedane.Dao.ScoreDao;
import com.cn.leedane.bean.ScoreBean;
import com.cn.leedane.service.ScoreService;
/**
 * 积分service的实现类
 * @author LeeDane
 * 2016年3月4日 下午5:40:47
 * Version 1.0
 */
public class ScoreServiceImpl extends BaseServiceImpl<ScoreBean> implements ScoreService<ScoreBean>{
	Logger logger = Logger.getLogger(getClass());
	@Autowired
	private ScoreDao<ScoreBean> scoreDao;
	
	public void setScoreDao(ScoreDao<ScoreBean> scoreDao) {
		this.scoreDao = scoreDao;
	}

	@Override
	public int getTotalScore(int userId) {
		return scoreDao.getTotalScore(userId);
	}
}
