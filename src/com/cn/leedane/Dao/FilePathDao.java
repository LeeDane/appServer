package com.cn.leedane.Dao;

import java.io.Serializable;

import com.cn.leedane.bean.FilePathBean;
/**
 * 文件路径dao接口类
 * @author LeeDane
 * 2015年11月22日 下午11:52:31
 * Version 1.0
 */
public interface FilePathDao <T extends Serializable> extends BaseDao<FilePathBean>{
	/**
	 * 批量更新uuid
	 * @param oldUuids
	 * @param newUuids
	 * @return
	 * @throws Exception
	 */
	public boolean updateBatchFilePath(final String[] oldUuids, final String[] newUuids);
	
	/**
	 * 获取单个心情的多张图片，多个用用“，” 分割开(存在bug)
	 * @param tableUUid
	 * @param tableName
	 * @param picSize
	 * @return
	 * @throws Exception
	 */
	public String getMoodImg(String tableUUid, String tableName, String picSize);
}
