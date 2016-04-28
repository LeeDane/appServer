package com.cn.leedane.service;
import java.io.Serializable;
import java.util.Date;

import javax.servlet.http.HttpServletRequest;

import com.cn.leedane.bean.OperateLogBean;
import com.cn.leedane.bean.UserBean;
/**
 * 操作日志的Service类
 * @author LeeDane
 * 2015年5月29日 上午11:47:19
 * version 1.0
 */
public interface OperateLogService<T extends Serializable> extends BaseService<OperateLogBean>{

	/**
	 * 保存操作日志
	 * @param user  用户
	 * @param request  请求
	 * @param createTime 创建时间
	 * @param subject  标题
	 * @param method  方式
	 * @param status  状态
	 * @param operateType 操作类型
	 * @return
	 */
	public boolean saveOperateLog(UserBean user,HttpServletRequest request,Date createTime,String subject, String method,int status,int operateType);

	
}
