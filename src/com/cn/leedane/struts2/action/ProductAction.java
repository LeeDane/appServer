package com.cn.leedane.struts2.action;
import javax.annotation.Resource;

import net.sf.json.JSONObject;

import com.cn.leedane.Utils.BaseActionContext;
import com.cn.leedane.Utils.EnumUtil;
import com.cn.leedane.bean.ProductBean;
import com.cn.leedane.bean.UserBean;
import com.cn.leedane.service.UserService;
import com.opensymphony.xwork2.ActionContext;
import com.opensymphony.xwork2.ModelDriven;

/**
 * 商品action类
 * @author LeeDane
 * 2015年7月17日 下午6:32:32
 * Version 1.0
 */
public class ProductAction extends BaseActionContext implements ModelDriven<ProductBean>{	
	//protected final Log log = LogFactory.getLog(getClass());

	private String params;	
	
	public String registerCode;//注册码
	
	private UserService<UserBean> userService;
	
	//private Map<String,Object> messageService;
	
	//用户信息
	private UserBean user;
	
	private ProductBean productBean = new ProductBean();
	
	@Resource
	public void setUserService(UserService<UserBean> userService) {
		this.userService = userService;
	}
	private static final long serialVersionUID = 1L;

	@Override
	public String execute() throws Exception {
        JSONObject jo = JSONObject.fromObject(params);     
        if(jo == null || jo.isEmpty()) {	
			message.put("message", EnumUtil.getResponseValue(EnumUtil.ResponseCode.缺少请求参数.value));
			message.put("responseCode", EnumUtil.ResponseCode.缺少请求参数.value);
			return SUCCESS;
		}
        user = userService.loginUser(jo.getString("account"), (String) jo.get("password"));    
        
        if(user != null){
        	ActionContext.getContext().getSession().put("Logining", true);
        }
        
        //resmessage = jo.fromObject(messageService).toString();        
        return SUCCESS;
	}
		
	public String saveProduct() {
		//System.out.println(ActionContext.getContext().getSession().get("Logining"));
		//log.info("sssssssssssssss");
        //JSONObject jo = JSONObject.fromObject(params);  
      
        //messageService = null;    
        //this.message =  jo.fromObject(messageService).toString();  
        return SUCCESS;
	}
	
	@Override
	public ProductBean getModel() {
		return productBean;
	}
	
}
