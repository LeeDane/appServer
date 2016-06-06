package com.cn.leedane.service.impl;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import net.sf.json.JSONObject;

import org.apache.log4j.Logger;

import com.cn.leedane.Dao.FilePathDao;
import com.cn.leedane.Utils.ConstantsUtil;
import com.cn.leedane.Utils.JsonUtil;
import com.cn.leedane.Utils.StringUtil;
import com.cn.leedane.bean.FilePathBean;
import com.cn.leedane.bean.OperateLogBean;
import com.cn.leedane.bean.UserBean;
import com.cn.leedane.service.AppVersionService;
import com.cn.leedane.service.OperateLogService;

/**
 * App版本service实现类
 * @author LeeDane
 * 2016年3月27日 下午7:40:27
 * Version 1.0
 */
public class AppVersionServiceImpl extends BaseServiceImpl<FilePathBean> implements AppVersionService<FilePathBean> {
	Logger logger = Logger.getLogger(getClass());
	@Resource
	private FilePathDao<FilePathBean> filePathDao;
	
	@Resource
	private OperateLogService<OperateLogBean> operateLogService;
	
	public void setOperateLogService(
			OperateLogService<OperateLogBean> operateLogService) {
		this.operateLogService = operateLogService;
	}
	
	public void setFilePathDao(FilePathDao<FilePathBean> filePathDao) {
		this.filePathDao = filePathDao;
	}

	@Override
	public Map<String, Object> getNewest(JSONObject jo, UserBean user,
			HttpServletRequest request) {
		logger.info("AppVersionServiceImpl-->getNewest():jo="+jo.toString());
		Map<String, Object> message = new HashMap<String, Object>();
		message.put("isSuccess", false);
		String versionName = JsonUtil.getStringValue(jo, "versionName");
		//String versionCode = JsonUtil.getStringValue(jo, "versionCode");
		
		List<Map<String, Object>>  list = new ArrayList<Map<String,Object>>();
		if(StringUtil.isNull(versionName)){
			//直接返回最新的记录
			list = getNewestVersion();
		}else{
			//该版本已经存
			int oldFileId = check(versionName);
			if(oldFileId > 0){
				StringBuffer sql = new StringBuffer();
				sql.append("select f.id, f.file_desc, f.file_version, f.qiniu_path path, lenght");
				sql.append(" from t_file_path f");
				sql.append(" where f.status=? and f.id >? and f.is_upload_qiniu = ? and f.table_name=? order by f.id desc limit 1");
				list = filePathDao.executeSQL(sql.toString(), ConstantsUtil.STATUS_NORMAL, oldFileId, true, ConstantsUtil.UPLOAD_APP_VERSION_TABLE_NAME);
			}else{
				//直接返回最新的记录
				list = getNewestVersion();
			}
		}
		
		
		if(list != null && list.size() ==1){
			message.put("isSuccess", true);
			message.put("message", list);
		}
		return message;
	}

	/**
	 * 检查数据库是否存在该版本
	 * @param versionName
	 * @return
	 */
	private int check(String versionName){
		String sql = "select id from t_file_path where status = ? and table_name=? and  file_version = ? and is_upload_qiniu = ? limit 1";
		List<Map<String, Object>> list = filePathDao.executeSQL(sql.toString(), ConstantsUtil.STATUS_NORMAL, ConstantsUtil.UPLOAD_APP_VERSION_TABLE_NAME, versionName, true );
		return list != null && list.size() == 1? StringUtil.changeObjectToInt(list.get(0).get("id")) : 0;
	}

	/**
	 * 获取数据库中上传的最新版本
	 * @return
	 */
	public List<Map<String, Object>> getNewestVersion(){
		List<Map<String, Object>>  list = new ArrayList<Map<String,Object>>();
		StringBuffer sql = new StringBuffer();
		sql.append("select f.id, f.file_desc, f.file_version, f.qiniu_path path, f.lenght");
		sql.append(" from t_file_path f");
		sql.append(" where f.status=? and f.is_upload_qiniu = ? and f.table_name=? order by f.id desc limit 1");
		list = filePathDao.executeSQL(sql.toString(), ConstantsUtil.STATUS_NORMAL, true, ConstantsUtil.UPLOAD_APP_VERSION_TABLE_NAME);
		return list;
	}
	
}
