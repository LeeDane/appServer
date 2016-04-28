package com.cn.leedane.service.impl;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import com.cn.leedane.Dao.BaseDao;
import com.cn.leedane.service.BaseService;

/**
 * 基本的service实现类
 * @author LeeDane
 * 2015年5月29日 上午11:58:04
 * version 1.0
 */
public class BaseServiceImpl<T extends Serializable> implements BaseService<T>{

	Logger logger = Logger.getLogger(getClass());
	@Autowired
	private BaseDao<T> baseDao;
	
	public BaseDao<T> getBaseDao() {
		return baseDao;
	}
	
	public void setBaseDao(BaseDao<T> baseDao) {
		this.baseDao = baseDao;
	}
	
	@Override
	public boolean save(T t){
		logger.info("BaseServiceImpl-->save():保存成功");
		return baseDao.save(t);
	}
	
	@Override
	public boolean saveOrUpdate(T t) throws Exception{
		logger.info("BaseServiceImpl-->saveOrUpdate()");
		return this.baseDao.saveOrUpdate(t);
	}
	

	@Override
	public boolean update(T t) throws Exception{
		logger.info("BaseServiceImpl-->update()");
		return baseDao.update(t);
	}
	
	@Override
	public boolean updateSQL(String sql, Object... obj) throws Exception {
		logger.info("BaseServiceImpl-->updateSQL()");
		return baseDao.updateSQL(sql, obj);
	}

	@Override
	public boolean delete(T t) throws Exception{
		logger.info("BaseServiceImpl-->delete()");
		return baseDao.delete(t);
	}

	@Override
	public T loadById(int id) {
		logger.info("BaseServiceImpl-->loadById():id="+id);
		return baseDao.loadById(id);
	}

	@Override
	public T findById(int id) {
		logger.info("BaseServiceImpl-->findById():id="+id);
		return baseDao.findById(id);
	}

	@Override
	public void merge(T entity) {
		logger.info("BaseServiceImpl-->merge()");
		this.baseDao.merge(entity);
	}

	@Override
	public List<Map<String, Object>> getlimits(String tableName, String where,
			int pageSize, int pageNo) {
		logger.info("BaseServiceImpl-->getlimits():tableName="+tableName+",where="+where+",pageSize="+pageSize+",pageNo="+pageNo);
		return this.baseDao.getlimits(tableName, where, pageSize, pageNo);
	}

	@Override
	public List<Map<String, Object>> getAll(String tableName, String where) {
		logger.info("BaseServiceImpl-->getAll():tableName="+tableName+",where="+where);
		return this.baseDao.getAll(tableName, where);
	}

	@Override
	public int getTotal(String tableName, String where) {
		logger.info("BaseServiceImpl-->getTotal():tableName="+tableName+",where="+where);
		return this.baseDao.getTotal(tableName, where);
	}


	@Override
	public boolean updateBatch(String tableName, int[] ids) throws Exception{
		logger.info("BaseServiceImpl-->updateBatch():tableName="+tableName+",ids="+ids);
		return this.baseDao.updateBatch(tableName, ids);
	}

	@Override
	public boolean deleteById(String tableName, int id) throws Exception {
		logger.info("BaseServiceImpl-->deleteById():tableName="+tableName+",id="+id);
		return this.baseDao.deleteById(tableName, id);
	}

	@Override
	public List<T> executeHQL(String beanName, String where) {
		logger.info("BaseServiceImpl-->executeHQL():beanName="+beanName+",where="+where);
		return this.baseDao.executeHQL(beanName, where);
	}

	@Override
	public List<Map<String, Object>> executeSQL(String sql, Object ...params) {
		logger.info("BaseServiceImpl-->executeSQL():sql="+sql+",params="+params);
		return this.baseDao.executeSQL(sql,params);
	}

	@Override
	public List<Map<String, Object>> getlimitsByPageSizeAndPageNo(String sql,
			int pageSize, int pageNo) {		
		logger.info("BaseServiceImpl-->getlimitsByPageSizeAndPageNo():sql="+sql+",pageSize="+pageSize+",pageNo="+pageNo);
		return this.baseDao.getlimitsByPageSizeAndPageNo(sql, pageSize, pageNo);
	}

	@Override
	public List<Map<String, Object>> getlimitsByPageSizeAndLastId(String sql,
			int pageSize, int lastId) {		
		logger.info("BaseServiceImpl-->getlimitsByPageSizeAndLastId():sql="+sql+",pageSize="+pageSize+",lastId="+lastId);
		return this.baseDao.getlimitsByPageSizeAndLastId(sql, pageSize, lastId);
	}

	@Override
	public boolean recordExists(String tableName, int tableId) {
			return this.baseDao.recordExists(tableName, tableId) ;
	}

	

	
}
