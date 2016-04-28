package com.cn.leedane.service.impl;

import java.util.Date;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import com.cn.leedane.Dao.OperateLogDao;
import com.cn.leedane.Utils.CommonUtil;
import com.cn.leedane.Utils.DateUtil;
import com.cn.leedane.bean.OperateLogBean;
import com.cn.leedane.bean.UserBean;
import com.cn.leedane.service.OperateLogService;
/**
 * 操作日志service的实现类
 * @author LeeDane
 * 2015年5月29日 上午11:48:26
 * Version 1.0
 */
public class OperateLogServiceImpl extends BaseServiceImpl<OperateLogBean> implements OperateLogService<OperateLogBean>{
	Logger logger = Logger.getLogger(getClass());
	@Autowired
	private OperateLogDao<OperateLogBean> operateLogDao;
	
	public void setOperateLogDao(OperateLogDao<OperateLogBean> operateLogDao) {
		this.operateLogDao = operateLogDao;
	}
	
	public OperateLogDao<OperateLogBean> getOperateLogDao() {
		return operateLogDao;
	}
	
	
	/*@Override
	public boolean save(OperateLogBean t) throws Exception {
		return operateLogDao.save(t);
	}*/

	@Override
	public boolean saveOperateLog(UserBean user, HttpServletRequest request,
			Date createTime, String subject, String method, int status, int operateType){
		OperateLogBean operateLogBean = new OperateLogBean();
		logger.info("OperateLogServiceImpl-->saveOperateLog():subject="+subject+",method="+method+",status="+status+",operateType="+operateType);
		if(request != null){
			String browserInfo = CommonUtil.getBroswerInfo(request);// 获取浏览器的类型
			String ip = CommonUtil.getIPAddress(request); //获得IP地址
			operateLogBean.setIp(ip);
			operateLogBean.setBrowser(browserInfo);
		}
		
		if(user != null){
			operateLogBean.setCreateUser(user);
		}
				
		operateLogBean.setCreateTime(createTime == null ? DateUtil.stringToDate(
				DateUtil.getSystemCurrentTime(DateUtil.DEFAULT_DATE_FORMAT),
				DateUtil.DEFAULT_DATE_FORMAT) : createTime);
		operateLogBean.setSubject(subject);
		operateLogBean.setStatus(status);
		operateLogBean.setMethod(method);
		operateLogBean.setOperateType(operateType);
		return this.save(operateLogBean);
	}
}
