package com.cn.leedane.test;

import java.util.Date;

import javax.annotation.Resource;

import org.junit.Test;

import com.cn.leedane.Utils.SpringUtils;
import com.cn.leedane.Utils.StringUtil;
import com.cn.leedane.bean.OperateLogBean;
import com.cn.leedane.bean.UserBean;
import com.cn.leedane.cache.SystemCache;
import com.cn.leedane.service.OperateLogService;
import com.cn.leedane.service.UserService;
import com.cn.leedane.task.spring.scheduling.UploadQiniuCloud;

/**
 * 操作日志相关的测试类
 * @author LeeDane
 * 2015年7月3日 下午6:28:34
 * Version 1.0
 */
public class OperateLogTest extends BaseTest {
	@Resource
	private OperateLogService<OperateLogBean> operateLogService;
	
	@Resource
	private UserService<UserBean> userService;
	@Resource
	private UploadQiniuCloud uploadQiniuCloud;
	/**
	 * 缓存
	 */
	private SystemCache systemCache;

	@Test
	public void addOperateLog() throws Exception{
		systemCache = (SystemCache) SpringUtils.getBean("systemCache");
		String adminId = (String) systemCache.getCache("admin-id");
		int aid = 1;
		if(!StringUtil.isNull(adminId)){
			aid = Integer.parseInt(adminId);
		}
		OperateLogBean operateLogBean = new OperateLogBean();
		 //处理服务器的IP
    	String ip = "127.0.0.1";
        operateLogBean.setIp(ip);
        operateLogBean.setStatus(1);
        operateLogBean.setCreateUser(userService.loadById(aid));
        operateLogBean.setCreateTime(new Date());
        operateLogBean.setBrowser("猎豹浏览器");
        operateLogBean.setMethod("单元测试");
        operateLogBean.setOperateType(0);
        operateLogBean.setSubject("测试新增操作日志");
        operateLogService.save(operateLogBean);
		
		/*TimeConsumingTest test = new TimeConsumingTest();
		test.save();*/
	}
	
	@Test
	public void getlimits(){
		System.out.println(operateLogService.getlimits("t_operate_log", " order by id desc ", 5, 1));
	}
	
	
}
