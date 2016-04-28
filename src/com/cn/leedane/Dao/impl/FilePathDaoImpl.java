package com.cn.leedane.Dao.impl;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.apache.log4j.Logger;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;

import com.cn.leedane.Dao.FilePathDao;
import com.cn.leedane.Utils.ConstantsUtil;
import com.cn.leedane.Utils.StringUtil;
import com.cn.leedane.bean.FilePathBean;

/**
 * 文件路径dao实现类
 * @author LeeDane
 * 2015年11月22日 下午11:53:11
 * Version 1.0
 */
public class FilePathDaoImpl extends BaseDaoImpl<FilePathBean> implements FilePathDao<FilePathBean>{
	Logger logger = Logger.getLogger(getClass());
	
	@Autowired
	private JdbcTemplate jdbcTemplate;
	@Autowired  
	public void setSessionFactoryOverride(SessionFactory sessionFactory) {   
	      super.setSessionFactory(sessionFactory);   
	}     
	
	@Override
	public boolean updateBatchFilePath(final String[] oldUuids, final String[] newUuids){
		logger.info("FilePathDaoImpl-->updateBatchFilePath():oldUuids数量为"+oldUuids.length + ",newUuids数量为："+newUuids.length);
		int[] result = this.jdbcTemplate.batchUpdate("update table T_FILE_PATH set table_uuid = ? where table_uuid = ", new BatchPreparedStatementSetter() {
			
			@Override
			public void setValues(PreparedStatement preparedStatement, int index) throws SQLException {	
				//设置第一个?及其对应的值
			    preparedStatement.setObject(1, newUuids[index]);		
			    preparedStatement.setObject(2, oldUuids[index]);	
			}
			
			@Override
			public int getBatchSize() {
				return oldUuids.length;
			}
		});
		return result.length > 0;
	}

	@Override
	public String getMoodImg(String tableUUid, String tableName, String picSize){
		//按照pic_order从小到大排序
		String sql = "select GROUP_CONCAT((case when is_upload_qiniu = ? then qiniu_path else path end) ORDER BY pic_order desc) path from t_file_path where table_uuid = ? and table_name = ? and pic_size = ?  and status = ?";
		String size =  jdbcTemplate.queryForObject(sql, new Object[]{ConstantsUtil.STATUS_NORMAL, tableUUid, tableName, picSize, ConstantsUtil.STATUS_NORMAL}, new RowMapper<String>() {

			@Override
			public String mapRow(ResultSet rs, int arg1) throws SQLException {
				
				return rs.getString("path");
			}
		});
		
		if(StringUtil.isNotNull(size)){
			size =  jdbcTemplate.queryForObject(sql, new Object[]{ConstantsUtil.STATUS_NORMAL, tableUUid, tableName, "source", ConstantsUtil.STATUS_NORMAL}, new RowMapper<String>() {

				@Override
				public String mapRow(ResultSet rs, int arg1) throws SQLException {
					
					return rs.getString("path");
				}
			});
			
		}
		
		return size;
	}

}
