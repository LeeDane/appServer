package com.cn.leedane.Dao.impl;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;

import com.cn.leedane.Dao.TransmitDao;
import com.cn.leedane.Utils.ConstantsUtil;
import com.cn.leedane.bean.TransmitBean;
/**
 * 转发Dao实现类
 * @author LeeDane
 * 2016年1月13日 上午11:30:20
 * Version 1.0
 */
public class TransmitDaoImpl extends BaseDaoImpl<TransmitBean> implements TransmitDao<TransmitBean>{
	Logger logger = Logger.getLogger(getClass());
	@Autowired
	private JdbcTemplate jdbcTemplate;
	@Override
	public int getTotalTransmits(int userId) {
		return jdbcTemplate.queryForInt("select sum(id) total_transmits from t_transmit where create_user_id = ? and status=?", userId, ConstantsUtil.STATUS_NORMAL);
	}
}
