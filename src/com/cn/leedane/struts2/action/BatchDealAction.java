package com.cn.leedane.struts2.action;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;

import com.cn.leedane.Utils.BaseActionContext;
import com.cn.leedane.Utils.ConstantsUtil;
import com.cn.leedane.Utils.StringUtil;
import com.cn.leedane.Utils.EnumUtil.DataTableType;
import com.cn.leedane.bean.BlogBean;
import com.cn.leedane.bean.FilePathBean;
import com.cn.leedane.bean.SignInBean;
import com.cn.leedane.handler.CloudStoreHandler;
import com.cn.leedane.service.BlogService;
import com.cn.leedane.service.FilePathService;
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
	
	@Autowired
	private FilePathService<FilePathBean> filePathService;
	
	@Autowired
	private CloudStoreHandler cloudStoreHandler;
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
			List<Map<String, Object>> blogs = blogService.executeSQL("select id, digest from "+DataTableType.博客.value+" where id=?", i);
			if(!blogs.isEmpty() && blogs.size() >0){
				List<BlogBean> blogs2 = blogService.executeHQL("BlogBean", "where digest = '"+StringUtil.changeNotNull(blogs.get(0).get("digest"))+"' and id != "+StringUtil.changeObjectToInt(blogs.get(0).get("id")));
				if(blogs2.size() > 0){
					for(BlogBean blog2: blogs2){
						blogService.executeSQL("delete from "+DataTableType.博客.value+" where id = ?", blog2.getId());
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
	
	/**
	 * 执行上传最新的10条记录
	 * @return
	 * @throws Exception
	 */
	public String upload10() {
		try {
			List<Map<String, Object>> paths = filePathService.executeSQL("select * from "+DataTableType.文件.value+" where is_upload_qiniu = 0 and table_name <> '"+DataTableType.心情.value+"' order by id desc limit 10");
			if(paths != null && paths.size() > 0){	
				List<Map<String,Object>> fileBeans = cloudStoreHandler.executeUpload(paths);
				if(fileBeans != null && fileBeans.size() >0){
					for(Map<String, Object> m: fileBeans){
						if(m.containsKey("id")){
							filePathService.updateUploadQiniu(StringUtil.changeObjectToInt(m.get("id")), ConstantsUtil.QINIU_SERVER_URL + StringUtil.changeNotNull(m.get("path")));
						}
					}
				}
			}else{
				System.out.println("没有要上传的文件");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return SUCCESS;
	}
}
