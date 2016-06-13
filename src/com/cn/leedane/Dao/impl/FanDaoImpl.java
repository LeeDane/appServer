package com.cn.leedane.Dao.impl;
import org.apache.log4j.Logger;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;

import com.cn.leedane.Dao.FanDao;
import com.cn.leedane.Utils.ConstantsUtil;
import com.cn.leedane.bean.FanBean;

/**
 * 粉丝dao实现类
 * @author LeeDane
 * 2016年4月11日 上午10:21:35
 * Version 1.0
 */
public class FanDaoImpl extends BaseDaoImpl<FanBean> implements FanDao<FanBean>{
	Logger logger = Logger.getLogger(getClass());
	@Autowired
	private JdbcTemplate jdbcTemplate;
	
	@Autowired  
	public void setSessionFactoryOverride(SessionFactory sessionFactory) {   
	     super.setSessionFactory(sessionFactory);   
	}

	@Override
	public boolean cancel(int uid, int... fanIds) {
		logger.info("FanDaoImpl-->cancel():fanIds="+fanIds);
		boolean result = false;
		if(uid == 0 || fanIds.length == 0) return result;
		
		try {
			String sql = "delete from t_fan where ( create_user_id = "+ uid +" and to_user_id in("+ getFanStr(fanIds) +") ) ";			
			this.jdbcTemplate.execute(sql);
			result = true;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}     
	
	
	private String getFanStr(int... fanIds){
		logger.info("FanDaoImpl-->getFanStr():fanIds="+fanIds);
		if(fanIds.length > 0){
			StringBuffer buffer = new StringBuffer();
			for(int i = 0; i < fanIds.length; i++){
				
				if(i == fanIds.length -1){
					buffer.append(fanIds[i]);
				}else{
					buffer.append(fanIds[i] + ",");
				}
			}
			
			return buffer.toString();
		}
		return "";
	}

	@Override
	public boolean isFanEachOther(int id, int to_user_id) {	
		logger.info("FanDaoImpl-->isFanEachOther():id="+id+",to_user_id=" +to_user_id);
		return jdbcTemplate.queryForList("select id from t_fan where ((create_user_id = ? and to_user_id = ?) or (to_user_id = ? and create_user_id = ?)) and status = ?", id, to_user_id, id, to_user_id, ConstantsUtil.STATUS_NORMAL).size() == 2;
	}

}
