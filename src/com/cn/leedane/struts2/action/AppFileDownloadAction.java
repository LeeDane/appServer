package com.cn.leedane.struts2.action;

import java.io.File;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.io.RandomAccessFile;
import java.util.HashMap;
import java.util.Map;

import javax.annotation.Resource;

import com.cn.leedane.Utils.Base64Util;
import com.cn.leedane.Utils.BaseActionContext;
import com.cn.leedane.Utils.ConstantsUtil;
import com.cn.leedane.Utils.EnumUtil;
import com.cn.leedane.Utils.EnumUtil.ResponseCode;
import com.cn.leedane.Utils.StringUtil;
import com.cn.leedane.bean.FilePathBean;
import com.cn.leedane.bean.UserBean;
import com.cn.leedane.enums.FileType;
import com.cn.leedane.redis.util.RedisUtil;
import com.cn.leedane.service.FilePathService;
import com.cn.leedane.service.UserService;
/**
 * APP文件的断点下载
 * @author LeeDane
 * 2016年1月20日 上午10:01:00
 * Version 1.0
 */
public class AppFileDownloadAction extends BaseActionContext{
	private static final long serialVersionUID = 6649027352616232244L;
	   
    /**
     * 上传filePath表的service
     */
  	private FilePathService<FilePathBean> filePathService;
  	
  	@Resource
  	public void setFilePathService(FilePathService<FilePathBean> filePathService) {
  		this.filePathService = filePathService;
  	}
  	
  	private UserService<UserBean> userService;
  	
  	@Resource
  	public void setUserService(UserService<UserBean> userService) {
		this.userService = userService;
	}
  	
    /**
     * 下载文件（只提供下载file文件夹下面的文件，建议断点下载每次的文件不要超过1M）
     * 注意app要保证编号的正确性和顺序，到时合并的时候将按照大小进行合并
     * @return
     */
    public String execute() {
    	long startTime = System.currentTimeMillis();
        try {
        	int uid = StringUtil.parseInt(request.getParameter("uid"), 0);
            System.out.println("用户ID为："+uid);
            user = userService.findById(uid);
            if(user == null){
            	response.setStatus(ResponseCode.请先登录.value);
        		return SUCCESS;
        	}
            
        	String fileName = request.getParameter("fileName"); //必须是带后缀的文件名，断点续传可以不用赋值
        	int fileOwnerId = StringUtil.parseInt(request.getParameter("fileOwnerId"), uid); //文件拥有者ID,必须，为空表示登录用户ID
        	int from = StringUtil.parseInt(request.getParameter("from"), uid); //下载开始位置
        	int needSize = StringUtil.parseInt(request.getParameter("length"), uid); //下载长度
        	String downloadCode = request.getParameter("downloadCode"); //下载码
        	
        	//对下载码进行校验
        	if(StringUtil.isNull(downloadCode)){
        		if(!filePathService.canDownload(fileOwnerId, fileName)){
                	response.setStatus(ResponseCode.没有操作权限.value);
                	return SUCCESS;
            	}
        	}else{
        		//检验下载码
            	String code = RedisUtil.getInstance().getString(downloadCode);
            	if(StringUtil.isNull(code) || !code.equalsIgnoreCase(fileName)){
                	response.setStatus(ResponseCode.下载码失效.value);
                	return SUCCESS;
            	}
        	}
        	
        	String fileFullPath = ConstantsUtil.DEFAULT_SAVE_FILE_FOLDER +FileType.FILE.value +"//" +fileName;
            File file = new File(fileFullPath); 
            //判断文件存在
            if(!file.exists() || !file.isFile()){
            	response.setStatus(ResponseCode.文件不存在.value);
                return SUCCESS;
            }
            
            RandomAccessFile raFile = new RandomAccessFile(file, "r");
            OutputStream os = response.getOutputStream();  
              
            byte[] buffer = new byte[needSize]; 
            raFile.seek(from);  
            while(needSize > 0){  
                int len = raFile.read(buffer);  
                if(needSize < buffer.length){  
                    os.write(buffer,0,needSize);  
                } else {  
                    os.write(buffer,0,len);  
                    if(len < buffer.length){  
                        break;  
                    }  
                }  
                needSize -= buffer.length;  
            }  
                  
            raFile.close();  
            os.close(); 
            System.out.println("下载完成");
            return null;
        } catch (Exception e) {
            System.out.println("下载文件发生异常,错误原因 : " + e.toString());
        }
        
        long endTime = System.currentTimeMillis();
        response.setStatus(ResponseCode.服务器处理异常.value);
        System.out.println("下载总计耗时："+ (endTime - startTime) +"毫秒");
        return SUCCESS;
    }

	/**
     * 获取单个文件的大小和下载码
     * @return
     */
    public String getSizeAndDownloadCode() {
    	long startTime = System.currentTimeMillis();
        try {
        	message.put("isSuccess", false);
        	String fileName = request.getParameter("fileName");//必须
        	String tableUuid = request.getParameter("uuid");  //客户端生成的唯一性uuid标识符
        	int uid = StringUtil.parseInt(request.getParameter("uid"), 0);
        	
        	user = userService.findById(uid);
            System.out.println("用户ID为："+uid);
            
            if(user == null){
            	message.put("message", ResponseCode.请先登录.value);
            	message.put("responseCode", EnumUtil.getResponseValue(ResponseCode.请先登录.value));
            	return SUCCESS;
        	}
            
            String fileFullPath = ConstantsUtil.DEFAULT_SAVE_FILE_FOLDER +FileType.FILE.value +"//" +fileName;
            File file = new File(fileFullPath);
            
            //判断文件存在
            if(!file.exists() || !file.isFile()){  
            	message.put("message", ResponseCode.文件不存在.value);
            	message.put("responseCode", EnumUtil.getResponseValue(ResponseCode.文件不存在.value));
            	return SUCCESS;
            }
            
        	Map<String, Object> m = new HashMap<String, Object>();
        	message.put("isSuccess", true);
        	m.put("size", file.length());
        	String downloadCode = StringUtil.produceDownloadCode(uid, fileName, tableUuid);
        	RedisUtil.getInstance().addString(downloadCode, fileName);
        	m.put("downloadCode", downloadCode);
        	message.put("message", m);
        	return SUCCESS;
            
        } catch (Exception e) {
            System.out.println("获取下载文件信息发生异常,错误原因 : " + e.getMessage());
        }
        long endTime = System.currentTimeMillis();
        System.out.println("获取下载文件信息总计耗时："+ (endTime - startTime) +"毫秒");
        return SUCCESS;
    }
}
