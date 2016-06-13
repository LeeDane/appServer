package com.cn.leedane.task.spring.scheduling;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.cn.leedane.Utils.ConstantsUtil;
import com.cn.leedane.Utils.StringUtil;
import com.cn.leedane.bean.FilePathBean;
import com.cn.leedane.handler.CloudStoreHandler;
import com.cn.leedane.service.FilePathService;

/**
 * 上传文件到七牛云存储服务器
 * @author LeeDane
 * 2015年11月23日 上午10:30:16
 * Version 1.0
 */
@Component("uploadQiniuCloud")
public class UploadQiniuCloud {
	
	@Autowired
	private FilePathService<FilePathBean> filePathService; 
	
	public void setFilePathService(FilePathService<FilePathBean> filePathService) {
		this.filePathService = filePathService;
	}
	
	@Autowired
	private CloudStoreHandler cloudStoreHandler;
	
	public void setCloudStoreHandler(CloudStoreHandler cloudStoreHandler) {
		this.cloudStoreHandler = cloudStoreHandler;
	}
	
	public void upload() {
		if(filePathService != null){
			try {
				/*Object object = systemCache.getCache("leedaneProperties"); 
				Map<String, Object> properties = new HashMap<String, Object>();
				if(object != null){
					properties = (Map<String, Object>) object;
				}
				
				long requestTimeOut = 30000;
				
				if(properties.containsKey("qiniuRequestTimeOut")){
					requestTimeOut = Long.parseLong(StringUtil.changeNotNull(properties.get("qiniuRequestTimeOut")));
				}*/
				
				List<Map<String, Object>> filePathBeans = filePathService.executeSQL("select * from t_file_path where is_upload_qiniu = 0 and table_name <> 't_mood'");

				if(filePathBeans != null && filePathBeans.size() > 0){		
					List<Map<String, Object>> fileBeans = cloudStoreHandler.executeUpload(filePathBeans);
					if(fileBeans != null && fileBeans.size() >0){
						for(Map<String, Object> m: fileBeans){
							if(m.containsKey("id")){
								filePathService.updateUploadQiniu(StringUtil.changeObjectToInt(m.get("id")), ConstantsUtil.QINIU_SERVER_URL + StringUtil.changeNotNull(m.get("path")));
							}
						}
					}
				}else{
					//uploadManager = null;
					//System.gc();
					System.out.println("没有要上传到七牛云存储服务器的文件");
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
			
		}else{
			System.out.println("filePathService为空");
		}		
	}	
}
