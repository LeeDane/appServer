package com.cn.leedane.struts2.action;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;

import com.cn.leedane.Utils.BaseActionContext;
import com.cn.leedane.Utils.StringUtil;
import com.cn.leedane.bean.BlogBean;
import com.cn.leedane.bean.SignInBean;
import com.cn.leedane.service.BlogService;
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
	
	@Autowired
	private BlogService<BlogBean> blogService;
	/**
	 * 执行方法
	 * @return
	 * @throws Exception
	 */
	public String execute() {
		/*List<SignInBean> signInBeans = signInService.executeHQL("SignInBean", null);
		if(signInBeans.size() > 0){
			Date time = null;
			for(SignInBean signInBean: signInBeans){
				time = signInBean.getCreateTime();
				signInBean.setCreateDate(DateUtil.DateToString(time, "yyyy-MM-dd"));
				signInService.update(signInBean);
			}
		}*/
		
		for(int i = 1504;i > 0; i--){
			List<Map<String, Object>> blogs = blogService.executeSQL("select id, digest from t_blog where id=?", i);
			if(!blogs.isEmpty() && blogs.size() >0){
				List<BlogBean> blogs2 = blogService.executeHQL("BlogBean", "where digest = '"+StringUtil.changeNotNull(blogs.get(0).get("digest"))+"' and id != "+StringUtil.changeObjectToInt(blogs.get(0).get("id")));
				if(blogs2.size() > 0){
					for(BlogBean blog2: blogs2){
						blogService.executeSQL("delete from t_blog where id = ?", blog2.getId());
					}
				}
			}
			
			try {
				Thread.sleep(100);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			System.out.println("i="+i);
		}
		
		return SUCCESS;
	}
}
