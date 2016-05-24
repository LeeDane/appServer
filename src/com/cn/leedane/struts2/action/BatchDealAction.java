package com.cn.leedane.struts2.action;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import com.cn.leedane.Utils.BaseActionContext;
import com.cn.leedane.Utils.DateUtil;
import com.cn.leedane.bean.SignInBean;
import com.cn.leedane.service.SignInService;

/**
 * 批量操作action类
 * @author LeeDane
 * 2016年5月24日 下午1:22:26
 * Version 1.0
 */
public class BatchDealAction extends BaseActionContext {
	private static final long serialVersionUID = 1L;
	@Autowired
	private SignInService<SignInBean> signInService;
	/**
	 * 执行方法
	 * @return
	 * @throws Exception
	 */
	public String execute() throws Exception {
		List<SignInBean> signInBeans = signInService.executeHQL("SignInBean", null);
		if(signInBeans.size() > 0){
			Date time = null;
			for(SignInBean signInBean: signInBeans){
				time = signInBean.getCreateTime();
				signInBean.setCreateDate(DateUtil.DateToString(time, "yyyy-MM-dd"));
				signInService.update(signInBean);
			}
		}
		return SUCCESS;
	}
}
