package com.cn.leedane.Dao.impl;

import java.io.Serializable;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.orm.hibernate4.support.HibernateDaoSupport;

import com.cn.leedane.Dao.BaseDao;
import com.cn.leedane.Utils.ConstantsUtil;
import com.cn.leedane.Utils.StringUtil;
/**
 * 基本Dao实现类
 * @author LeeDane
 * 2015年5月29日 上午11:46:29
 * version 1.0
 */
public class BaseDaoImpl<T extends Serializable> extends HibernateDaoSupport implements BaseDao<T> {
	Logger logger = Logger.getLogger(getClass());
	/**
	 * 默认的每页大小
	 */
	public static final int DEFAULT_PAGE_SIZE = 5;
	
	@Autowired
	private JdbcTemplate jdbcTemplate;
	
	@Autowired
	private SessionFactory sessionFactory;

	Class<T> clazz;

	@SuppressWarnings({ "unchecked", "rawtypes" })
	public BaseDaoImpl() {
		//System.out.println("BaseDaoImpl construct method");	
		Class c = getClass();  
		Type t = c.getGenericSuperclass();  
		if (t instanceof ParameterizedType) {  
			Type[] p = ((ParameterizedType) t).getActualTypeArguments();  
			this.clazz = (Class<T>) p[0];  
		}  
	}

	@Override
	public boolean save(T t){	
		logger.info("BaseDaoImpl-->save()");
		this.getHibernateTemplate().save(t);//保存实体
		return true;					
	}
	

	@Override
	public boolean saveOrUpdate(T t){
		logger.info("BaseDaoImpl-->saveOrUpdate()");
		this.getHibernateTemplate().saveOrUpdate(t);
		return true;
	}

	@Override
	public boolean update(T t){	
		logger.info("BaseDaoImpl-->update()");
		this.getHibernateTemplate().update(t);
		return true;			
	}
	
	@Override
	public boolean update(String sql) {
		logger.info("BaseDaoImpl-->update(), SQL:"+sql);
		return this.jdbcTemplate.update(sql) > 0;
	}
	
	@Override
	public boolean update(String sql, Object... params) {
		logger.info("BaseDaoImpl-->update(), SQL:"+sql+",params:"+params.toString());
		return this.jdbcTemplate.update(sql, params) > 0;
	}
	
	@Override
	public boolean updateSQL(String sql, Object ... obj){
		logger.info("BaseDaoImpl-->updateSQL()");
		return this.jdbcTemplate.update(sql, obj) > 0;
	}

	@Override
	public boolean delete(T t){	
		logger.info("BaseDaoImpl-->delete()");
		this.getHibernateTemplate().delete(t);
		return true;
	}

	@Override
	public T loadById(int id) {	
		logger.info("BaseDaoImpl-->loadById():id="+id);
		return this.getHibernateTemplate().load(clazz, id);
		//return jdbcTemplate.queryForList(sql, elementType)
	}

	@Override
	public T findById(int id) {
		logger.info("BaseDaoImpl-->findById():id="+id);
		return this.getHibernateTemplate().get(clazz, id);
	}
	
	@Override
	public void merge(T entity) {
		logger.info("BaseDaoImpl-->merge()");
		this.getHibernateTemplate().merge(entity);
		this.getHibernateTemplate().flush();
	}

	@Override
	public List<Map<String, Object>> getlimits(String tableName, String where,
			int pageSize, int pageNo) {
		if(StringUtil.isNull(tableName)) return null;
		logger.info("BaseDaoImpl-->getlimits():tableName="+tableName+",where="+where+",pageSize="+pageSize+",pageNo="+pageNo);
		//默认的5条
		if(pageSize < 1) pageSize = DEFAULT_PAGE_SIZE;
		
		int from = 0;
		
		if(pageNo >= 1) 
			from = (pageNo - 1) * pageSize;
		String sql = "select * from " + tableName + " " + StringUtil.changeNotNull(where) + " limit ?,? ";
		
		return this.jdbcTemplate.queryForList(sql, from, pageSize);
	}

	@Override
	public List<Map<String, Object>> getAll(String tableName, String where) {
		logger.info("BaseDaoImpl-->getAll():tableName="+tableName+",where="+where);
		if(StringUtil.isNull(tableName)) return null;
		
		String sql = "select * from " + tableName + " " + StringUtil.changeNotNull(where);
		return this.jdbcTemplate.queryForList(sql);
	}

	@Override
	public int getTotal(String tableName, String where) {
		logger.info("BaseDaoImpl-->getTotal():tableName="+tableName+",where="+where);
		String sql = "select count(id) count from "+ tableName + " " + StringUtil.changeNotNull(where);
		List<Map<String, Object>> ls =  this.jdbcTemplate.queryForList(sql);
		return ls != null && ls.size() == 1 ? StringUtil.changeObjectToInt(ls.get(0).get("count")) : 0;
	}

	@Override
	public boolean deleteById(String tableName, int id){
		logger.info("BaseDaoImpl-->deleteById():tableName="+tableName+",id="+id);
		String sql = "delete from "+ tableName + " where id = ? ";
		return this.jdbcTemplate.update(sql, id) > 0;
	}

	@Override
	public boolean updateBatch(String sql, final int[] ids){
		logger.info("BaseDaoImpl-->updateBatch():sql="+sql+",ids="+ids);
		int[] result = this.jdbcTemplate.batchUpdate(sql, new BatchPreparedStatementSetter() {
			
			@Override
			public void setValues(PreparedStatement preparedStatement, int index) throws SQLException {	
				//设置第一个?及其对应的值
			    preparedStatement.setObject(1, ids[index]);		    
			}
			
			@Override
			public int getBatchSize() {
				return ids.length;
			}
		});
		return result.length > 0;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<T> executeHQL(String beanName, String where) {
		logger.info("BaseDaoImpl-->executeHQL():beanName="+beanName+",where="+where);
		String hql = "from " + beanName + " " + StringUtil.changeNotNull(where);
		Session session = sessionFactory.openSession();
		Query query = session.createQuery(hql);
		
		return query.list();
	}

	@Override
	public List<Map<String, Object>> executeSQL(String sql, Object ...params) {
		logger.info("BaseDaoImpl-->executeSQL():sql="+sql+",params="+params);
		return this.jdbcTemplate.queryForList(sql,params);
	}

	@Override
	public List<Map<String, Object>> getlimitsByPageSizeAndPageNo(String sql,
			int pageSize, int pageNo) {
		logger.info("BaseDaoImpl-->getlimitsByPageSizeAndPageNo():sql="+sql+",pageSize="+pageSize+",pageNo="+pageNo);
		//默认的5条
		if(pageSize < 1) pageSize = DEFAULT_PAGE_SIZE;
		
		int from = 0;
		
		if(pageNo >= 1) 
			from = (pageNo - 1) * pageSize;
		return this.jdbcTemplate.queryForList(sql+" limit ?,? ", from, pageSize);
	}

	@Deprecated
	@Override
	public List<Map<String, Object>> getlimitsByPageSizeAndLastId(String sql,
			int pageSize, int lastId) {
		logger.info("BaseDaoImpl-->getlimitsByPageSizeAndLastId():sql="+sql+",pageSize="+pageSize+",lastId="+lastId);
		/*//默认的5条
		if(pageSize < 1) pageSize = DEFAULT_PAGE_SIZE;
		
		int from = 0;
		
		if(lastId > 1) 
			from = (pageNo - 1) * pageSize;
		return this.jdbcTemplate.queryForList(sql+" limit ?,? ", from, pageSize);*/
		return null;
	}

	@Override
	public boolean recordExists(String tableName, int tableId) {
		return executeSQL("select id from "+tableName +" where id = ?", tableId).size() > 0 ;
	}

	@Override
	public int getObjectCreateUserId(String tableName, int tableId) {
		int createUserId = 0;
		List<Map<String, Object>> list = executeSQL("select create_user_id from "+tableName+" where status=? and id=? limit 1", ConstantsUtil.STATUS_NORMAL, tableId);
		if(list != null && list.size() == 1){
			createUserId = StringUtil.changeObjectToInt(list.get(0).get("create_user_id"));
		}
		return createUserId;
	}
}
