package com.cn.leedane.Dao.impl;
import org.apache.log4j.Logger;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;

import com.cn.leedane.Dao.GalleryDao;
import com.cn.leedane.Utils.StringUtil;
import com.cn.leedane.bean.GalleryBean;
import com.cn.leedane.bean.UserBean;

/**
 * 图库dao实现类
 * @author LeeDane
 * 2016年1月15日 下午3:14:33
 * Version 1.0
 */
public class GalleryDaoImpl extends BaseDaoImpl<GalleryBean> implements GalleryDao<GalleryBean>{
	Logger logger = Logger.getLogger(getClass());
	
	@Autowired
	private JdbcTemplate jdbcTemplate;
	@Autowired  
	public void setSessionFactoryOverride(SessionFactory sessionFactory) {   
	      super.setSessionFactory(sessionFactory);   
	}
	@Override
	public boolean isExist(UserBean user, String path) {
		logger.info("GalleryDaoImpl-->isExist():account="+user.getAccount()+",path="+path);
		if(StringUtil.isNull(path))
			return true;
		String sql = "select id from t_gallery where path = ? and create_user_id = ? ";
		return jdbcTemplate.queryForList(sql, path, user.getId()).size() > 0;
	}     

}
