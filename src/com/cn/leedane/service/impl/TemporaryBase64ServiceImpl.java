package com.cn.leedane.service.impl;

import java.util.Date;

import javax.servlet.http.HttpServletRequest;

import net.sf.json.JSONObject;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import com.cn.leedane.Dao.TemporaryBase64Dao;
import com.cn.leedane.Utils.ConstantsUtil;
import com.cn.leedane.Utils.JsonUtil;
import com.cn.leedane.Utils.EnumUtil.DataTableType;
import com.cn.leedane.bean.TemporaryBase64Bean;
import com.cn.leedane.bean.UserBean;
import com.cn.leedane.log.LogAnnotation;
import com.cn.leedane.service.TemporaryBase64Service;
/**
 * base64上传临时文件service的实现类
 * @author LeeDane
 * 2015年12月1日 上午10:57:55
 * Version 1.0
 */
public class TemporaryBase64ServiceImpl extends BaseServiceImpl<TemporaryBase64Bean> implements TemporaryBase64Service<TemporaryBase64Bean>{
	Logger logger = Logger.getLogger(getClass());
	@Autowired
	private TemporaryBase64Dao<TemporaryBase64Bean> temporaryBase64Dao;
	
	public void setTemporaryBase64Dao(
			TemporaryBase64Dao<TemporaryBase64Bean> temporaryBase64Dao) {
		this.temporaryBase64Dao = temporaryBase64Dao;
	}
	
	@LogAnnotation(moduleName="base64临时文件",option="上传数据")
	@Override
	public boolean saveBase64Str(JSONObject jo, UserBean user,
			HttpServletRequest request) throws Exception {
		logger.info("TemporaryBase64ServiceImpl-->saveBase64Str():jo="+jo.toString());
		int start = JsonUtil.getIntValue(jo, "start");
		int end = JsonUtil.getIntValue(jo, "end");
		String uuid = JsonUtil.getStringValue(jo, "uuid");
		int order = JsonUtil.getIntValue(jo, "order");
		String content = JsonUtil.getStringValue(jo, "content");	
		TemporaryBase64Bean base64Bean = new TemporaryBase64Bean();
		base64Bean.setContent(content);
		base64Bean.setCreateTime(new Date());
		base64Bean.setCreateUser(user);
		base64Bean.setEnd(end);
		base64Bean.setOrder(order);
		base64Bean.setTableName(DataTableType.心情.value);
		base64Bean.setStart(ConstantsUtil.STATUS_NORMAL);
		base64Bean.setStart(start);
		base64Bean.setUuid(uuid);
		return temporaryBase64Dao.save(base64Bean);
	}
}
