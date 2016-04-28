package com.cn.leedane.Dao.impl;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;

import com.cn.leedane.Dao.UserDao;
import com.cn.leedane.Utils.ConstantsUtil;
import com.cn.leedane.Utils.MD5Util;
import com.cn.leedane.Utils.StringUtil;
import com.cn.leedane.bean.UserBean;

/**
 * 用户dao实现类
 * @author LeeDane
 * 2015年4月3日 下午2:57:47
 * Version 1.0
 */
public class UserDaoImpl extends BaseDaoImpl<UserBean> implements UserDao<UserBean>{
	Logger logger = Logger.getLogger(getClass());
	@Autowired
	private JdbcTemplate jdbcTemplate;
	
	@Override
	public UserBean loadById(int Uid) {
		logger.info("UserDaoImpl-->loadById():Uid="+Uid);
		String hql = "from UserBean where id=?";
		@SuppressWarnings("unchecked")
		List<UserBean> list = (List<UserBean>)this.getHibernateTemplate().find(hql, new Object[]{Uid});
		return list.size() > 0 ? list.get(0) : null;
	}

	@SuppressWarnings("unchecked")
	@Override
	public UserBean loginUser(String condition, String password) { //登录
		logger.info("UserDaoImpl-->loginUser():condition="+condition+",password="+password);
		String hql = "from UserBean where (account=? or email=?) and password=?";
		List<UserBean> list = (List<UserBean>)this.getHibernateTemplate().find(hql, new Object[]{condition,condition,password});
		return list.size() > 0 ? list.get(0) : null;
	}

	@SuppressWarnings("unchecked")
	@Override
	public UserBean loginUserByPhone(String mobilePhone) { //手机号码登录
		logger.info("UserDaoImpl-->loginUserByPhone():mobilePhone="+mobilePhone);
		String hql = "from UserBean where mobilePhone = ?";
		List<UserBean> list = (List<UserBean>)this.getHibernateTemplate().find(hql, new Object[]{mobilePhone});
		return list.size() > 0 ? list.get(0) : null;
	}
	@Override
	public boolean saveUser(UserBean user) {	
		logger.info("UserDaoImpl-->saveUser():user="+user.toString());
		user.setStatus(2);
		boolean isSuccess = false;	
		this.getHibernateTemplate().save(user);//保存
		isSuccess = true;	
		return isSuccess;
	}

	@SuppressWarnings("unchecked")
	@Override
	public boolean isUniqueUser(UserBean user) {
		logger.info("UserDaoImpl-->isUniqueUser():user="+user.toString());
		String account = user.getAccount();			
		String hql = "from UserBean where account=?";
		List<UserBean> list = (List<UserBean>)this.getHibernateTemplate().find(hql, new Object[]{account});	
		return list.size()>0 ? false : true;
	}

	@SuppressWarnings("unchecked")
	@Override
	public UserBean find4OneUser(int id, String condition) {
		logger.info("UserDaoImpl-->find4OneUser():id="+id+",condition="+condition);
		List<UserBean> list = null;
		if(id == 0){ //没有登录/没有注册的用户
			String hql = "from UserBean where account=? or email=?";
			list = (List<UserBean>)this.getHibernateTemplate().find(hql, new Object[]{condition,condition});
			
		}else{ //已经登录的用户/或已经注册的用户
			String hql = "from UserBean where id=?";
			list = (List<UserBean>)this.getHibernateTemplate().find(hql, new Object[]{id});
		}			
		return list.size() > 0 ? list.get(0) : null;
	}

	@Override
	public UserBean checkRegisterCode(String registerCode) {
		logger.info("UserDaoImpl-->checkRegisterCode():registerCode="+registerCode);
		String hql = "from UserBean where registerCode=? and status = 2";
		@SuppressWarnings("unchecked")
		List<UserBean> list = (List<UserBean>)this.getHibernateTemplate().find(hql, new Object[]{registerCode});		
		//Query query =  this.getSessionFactory().getCurrentSession().createQuery(hql);
		//query.setParameter("registerzode", registerCode);
		return list.size()>0? list.get(0) : null;
	}

	@Override
	public boolean updateUserState(UserBean user) {
		try{
			this.getHibernateTemplate().update(user);
			return true;
		}catch(Exception e){
			return false;
		}
	}

	@Override
	public boolean updateField(String name, Object value, String table, int id) {
		logger.info("UserDaoImpl-->updateField():name="+name+",value="+value+",table="+table+",id="+id);
		String hql = "update "+table+" set "+name+"=? where id=?";
		this.getHibernateTemplate().find(hql, new Object[]{value,id});
		return false;
	}

	@Override
	public List<Map<String, Object>> find4MoreUser(String conditions,
			Object... params) {
		logger.info("UserDaoImpl-->find4MoreUser():conditions="+conditions+",params="+params);
		String sql= "select account,age,email,china_name,nation,marry,native_place,education_background,mobile_phone,date_format(register_time,'%Y-%m-%d') register_time from t_user where 0=0 " + conditions;
		return this.jdbcTemplate.queryForList(sql, params);
	}
	
	@SuppressWarnings("deprecation")
	@Override
	public int total(String tableName, String field, String where) {
		logger.info("UserDaoImpl-->total():tableName="+tableName+",field="+field+",where="+where);
		String sql = "select count(id) from  t_user"+ StringUtil.changeNotNull(where);
		return this.jdbcTemplate.queryForInt(sql);
	}

	@Override
	public List<Map<String, Object>> statisticsUserAge() {
		logger.info("UserDaoImpl-->statisticsUserAge()");
		String sql = "select age xaxis,count(id) yaxis from t_user GROUP BY age";
		return this.jdbcTemplate.queryForList(sql);
	}

	@Override
	public List<Map<String, Object>> statisticsUserAgeRang() {
		logger.info("UserDaoImpl-->statisticsUserAgeRang()");
		String sql = "select elt(interval(age,0,18,21,31,41,51),'0岁-17岁','18岁-20岁','21岁-30岁','31岁-40岁','41岁-50岁','>50岁') as 'xaxis' ,count(id) as 'yaxis' from t_user group by elt(interval(age,0,18,21,31,41,51), '0岁-17岁','18岁-20岁','21岁-30岁','31岁-40岁','41岁-50岁','>50岁');";
		return this.jdbcTemplate.queryForList(sql);
	}

	@Override
	public List<Map<String, Object>> statisticsUserRegisterByYear() {
		logger.info("UserDaoImpl-->statisticsUserRegisterByYear()");
		String sql = "select count(id) yaxis,year(register_time) xaxis from t_user group by year(register_time) order by year(register_time)";
		return this.jdbcTemplate.queryForList(sql);
	}
	
	@Override
	public List<Map<String, Object>> statisticsUserRegisterByMonth() {
		logger.info("UserDaoImpl-->statisticsUserRegisterByMonth()");
		String sql = "select count(id) yaxis,month(register_time) xaxis from t_user group by month(register_time) order by month(register_time)";
		return this.jdbcTemplate.queryForList(sql);
	}
	
	public List<Map<String, Object>> statisticsUserRegisterByNearWeek(){
		logger.info("UserDaoImpl-->statisticsUserRegisterByNearWeek()");
		String sql = "select count(t.id) yaxis,date_format(t.register_time,'%Y-%m-%d') xaxis from (select id,register_time from t_user where DATE_SUB(CURDATE(), INTERVAL 7 DAY) <= date(register_time)) t group by day(t.register_time) order by t.register_time";
		return this.jdbcTemplate.queryForList(sql);
	}
	
	public List<Map<String, Object>> statisticsUserRegisterByNearMonth(){
		logger.info("UserDaoImpl-->statisticsUserRegisterByNearMonth()");
		String sql = "select count(t.id) yaxis,date_format(t.register_time,'%Y-%m-%d') xaxis from (select id,register_time from t_user where DATE_SUB(CURDATE(),INTERVAL 1 MONTH) <= date(register_time)) t group by day(t.register_time) order by t.register_time";
		return this.jdbcTemplate.queryForList(sql);
	}

	@Override
	public UserBean getUserByNoLoginCode(String account, String noLoginCode) {
		logger.info("UserDaoImpl-->getUserByNoLoginCode():account="+account+",noLoginCode="+noLoginCode);
		String hql = "from UserBean where account = ? and no_login_code = ? and status = ? ";
		@SuppressWarnings("unchecked")
		List<UserBean> list = (List<UserBean>)this.getHibernateTemplate().find(hql, new Object[]{account, noLoginCode, ConstantsUtil.STATUS_NORMAL});
		return list.size() > 0 ? list.get(0) : null;
	}

	@Override
	public UserBean loginByWeChat(String FromUserName) {
		if(StringUtil.isNull(FromUserName))
			return null;
		logger.info("UserDaoImpl-->loginByWeChat():FromUserName="+FromUserName);
		String hql = "from UserBean where wechat_user_name = ? and status = ? ";
		@SuppressWarnings("unchecked")
		List<UserBean> list = (List<UserBean>)this.getHibernateTemplate().find(hql, new Object[]{FromUserName, ConstantsUtil.STATUS_NORMAL});
		return list.size() > 0 ? list.get(0) : null;
	}

	@Override
	public UserBean bindByWeChat(String FromUserName, String account,
			String password) {
		logger.info("UserDaoImpl-->bindByWeChat():FromUserName="+FromUserName+",account="+account);
		UserBean userBean = loginUser(account, MD5Util.compute(password));
		//先登录用户
		if(userBean != null && userBean.getStatus() == ConstantsUtil.STATUS_NORMAL){
			userBean.setWechatUserName(FromUserName);
			return update(userBean) ? userBean: null;
		}
		return null;
	}
}
