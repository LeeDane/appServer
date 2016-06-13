package com.cn.leedane.struts2.action;

import java.io.File;
import java.io.IOException;

import org.apache.commons.io.FileUtils;

import com.cn.leedane.Utils.ConstantsUtil;
import com.opensymphony.xwork2.ActionSupport;

/**
 * 文件上传action类
 * @author LeeDane
 * 2015年7月17日 下午6:32:59
 * Version 1.0
 */
public class UploadAction extends ActionSupport {
	
	private static final long serialVersionUID = 1L;
	// 得到上传文件的名称一定与name值一直  
    private File upload[];  
    // 上传文件的类型 ContentType  
    private String uploadContentType[];  
    // 上传文件的名称  
    private String uploadFileName[];  
    public File[] getUpload() {  
        return upload;  
    }  
  
    public void setUpload(File[] upload) {  
        this.upload = upload;  
    }  
  
    public String[] getUploadContentType() {  
        return uploadContentType;  
    }  
  
    public void setUploadContentType(String[] uploadContentType) {  
        this.uploadContentType = uploadContentType;  
    }  
  
    public String[] getUploadFileName() {  
        return uploadFileName;  
    }  
  
    public void setUploadFileName(String[] uploadFileName) {  
        this.uploadFileName = uploadFileName;  
    }  
  
    public static long getSerialversionuid() {  
        return serialVersionUID;  
    }  
  
    public String uploads() {  
        String path = ConstantsUtil.DEFAULT_SAVE_FILE_FOLDER+"//temporary";
        		//ServletActionContext.getServletContext().getRealPath("/upload");  
  
        // 写到指定路径  
        File file = new File(path);  
        //判断指定的路径下是否有uplaod，如果没有，自动创建  
        if (!file.exists()) {  
            file.mkdirs();  
        }  
        try {  
            for(int i = 0;i<upload.length;i++){  
                FileUtils.copyFile(upload[i], new File(file, uploadFileName[i]));  
            }  
        } catch (IOException e) {  
            // TODO Auto-generated catch block  
            e.printStackTrace();  
            System.out.println("出错");
        }  
        return SUCCESS;  
    } 
}
