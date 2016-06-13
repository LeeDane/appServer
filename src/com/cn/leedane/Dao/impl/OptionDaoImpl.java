package com.cn.leedane.Dao.impl;
import org.apache.log4j.Logger;

import com.cn.leedane.Dao.OptionDao;
import com.cn.leedane.bean.OptionBean;
/**
 * 选项配置的Dao实现类
 * @author LeeDane
 * 2015年4月3日 下午2:51:10
 * Version 1.0
 */

public class OptionDaoImpl extends BaseDaoImpl<OptionBean> implements OptionDao<OptionBean>{
	Logger logger = Logger.getLogger(getClass());
	/*@Override
	public boolean save(OptionBean t) {
		// TODO Auto-generated method stub
		return false;
	}*/

	/*@Override
	public UserBean loadById(int Uid) {
		UserBean user=null;
		return user;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<UserBean> loginUser(String condition, String password) { //登录
		String hql = "from User where (account=? or email=?) and password=?";
		List<UserBean> list = (List<UserBean>)this.getHibernateTemplate().find(hql, new Object[]{condition,condition,password});
		return list;
	}

	@Override
	public boolean saveUser(UserBean user) {	
		boolean isSuccess = false;	
		this.getHibernateTemplate().save(user);//保存
		isSuccess = true;	
		return isSuccess;
	}

	@SuppressWarnings("unchecked")
	@Override
	public boolean isUniqueUser(UserBean user) {
		String account = user.getAccount();			
		String hql = "from User where account=?";
		List<UserBean> list = (List<UserBean>)this.getHibernateTemplate().find(hql, new Object[]{account});	
		return list.size()>0 ? false : true;
	}

	@SuppressWarnings("unchecked")
	@Override
	public UserBean find4OneUser(int id, String condition) {
		List<UserBean> list = null;
		if(id == 0){ //没有登录/没有注册的用户
			String hql = "from User where account=? or email=?";
			list = (List<UserBean>)this.getHibernateTemplate().find(hql, new Object[]{condition,condition});
			
		}else{ //已经登录的用户/或已经注册的用户
			String hql = "from User where id=?";
			list = (List<UserBean>)this.getHibernateTemplate().find(hql, new Object[]{id});
		}			
		return list.size() > 0 ? list.get(0) : null;
	}

	@Override
	public UserBean checkRegisterCode(String registerCode) {
		String hql = "from User where registerCode=? and state = 0";
		@SuppressWarnings("unchecked")
		List<UserBean> list = (List<UserBean>)this.getHibernateTemplate().find(hql, new Object[]{registerCode});		
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
	}*/
}
