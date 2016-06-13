package com.cn.leedane.Dao.impl;
import org.apache.log4j.Logger;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;

import com.cn.leedane.Dao.FriendDao;
import com.cn.leedane.Utils.ConstantsUtil;
import com.cn.leedane.bean.FriendBean;

/**
 * 好友关系dao实现类
 * @author LeeDane
 * 2015年9月15日 下午2:40:07
 * Version 1.0
 */
public class FriendDaoImpl extends BaseDaoImpl<FriendBean> implements FriendDao<FriendBean>{
	Logger logger = Logger.getLogger(getClass());
	@Autowired
	private JdbcTemplate jdbcTemplate;
	
	@Autowired  
	public void setSessionFactoryOverride(SessionFactory sessionFactory) {   
	     super.setSessionFactory(sessionFactory);   
	}

	@Override
	public boolean deleteFriends(int uid, int... friends) {
		logger.info("FriendDaoImpl-->deleteFriends():uid="+",friends="+friends);
		boolean result = false;
		if(uid == 0 || friends.length == 0) return result;
		
		try {
			String sql = "delete from t_friend where ( from_user_id = "+ uid +" and to_user_id in("+ getFriendStr(friends) +") ) or ( to_user_id = "+ uid +" and from_user_id in("+  getFriendStr(friends) +") )";			
			this.jdbcTemplate.execute(sql);
			result = true;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}     
	
	
	private String getFriendStr(int... friends){
		logger.info("FriendDaoImpl-->getFriendStr():friends="+friends);
		if(friends.length > 0){
			StringBuffer buffer = new StringBuffer();
			for(int i = 0; i < friends.length; i++){
				
				if(i == friends.length -1){
					buffer.append(friends[i]);
				}else{
					buffer.append(friends[i] + ",");
				}
			}
			
			return buffer.toString();
		}
		return "";
	}

	@Override
	public boolean isFriend(int id, int to_user_id) {	
		logger.info("FriendDaoImpl-->isFriend():id="+id+",to_user_id=" +to_user_id);
		return jdbcTemplate.queryForList("select id from t_friend where ((from_user_id = ? and to_user_id = ?) or (to_user_id = ? and from_user_id = ?)) and status = ?", id, to_user_id, id, to_user_id, ConstantsUtil.STATUS_NORMAL).size() > 0;
	}

	@Override
	public boolean isFriendRecord(int id, int to_user_id) {
		logger.info("FriendDaoImpl-->isFriendRecord():id="+id+",to_user_id=" +to_user_id);
		return jdbcTemplate.queryForList("select id from t_friend where ((from_user_id = ? and to_user_id = ?) or (to_user_id = ? and from_user_id = ?))", id, to_user_id, id, to_user_id).size() > 0;
	}
}
