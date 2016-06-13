package com.cn.leedane.struts2.action;
import java.io.File;
import java.io.FileOutputStream;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import net.sf.json.JSONObject;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.cn.leedane.Utils.BaseActionContext;
import com.cn.leedane.Utils.ConstantsUtil;
import com.cn.leedane.Utils.DateUtil;
import com.cn.leedane.Utils.EnumUtil;
import com.cn.leedane.Utils.EnumUtil.DataTableType;
import com.cn.leedane.Utils.EnumUtil.ResponseCode;
import com.cn.leedane.Utils.FileUtil;
import com.cn.leedane.Utils.HttpUtils;
import com.cn.leedane.Utils.JsonUtil;
import com.cn.leedane.Utils.StringUtil;
import com.cn.leedane.bean.FilePathBean;
import com.cn.leedane.bean.UploadBean;
import com.cn.leedane.handler.UserHandler;
import com.cn.leedane.service.FilePathService;
import com.cn.leedane.service.UploadService;
/**
 * 文件action类
 * @author LeeDane
 * 2015年12月9日 上午11:17:26
 * Version 1.0
 */
public class FilePathAction extends BaseActionContext{	
	protected final Log log = LogFactory.getLog(getClass());
	private static final long serialVersionUID = 1L;
	
	//上传filePath表的service
	private FilePathService<FilePathBean> filePathService;
	
	@Resource
	public void setFilePathService(FilePathService<FilePathBean> filePathService) {
		this.filePathService = filePathService;
	}
	
//	private UserService<UserBean> userService;
//	@Resource
//	public void setUserService(UserService<UserBean> userService) {
//		this.userService = userService;
//	}
	private UploadService<UploadBean> uploadService;
	
	@Resource
	public void setUploadService(UploadService<UploadBean> uploadService) {
		this.uploadService = uploadService;
	}
	
	@Resource
	private UserHandler userHandler;
	
	public void setUserHandler(UserHandler userHandler) {
		this.userHandler = userHandler;
	}
		
	/**
	 * 分页获取指定的图片列表
	 * @return
	 */
	public String getUserImagePaging(){
		message.put("isSuccess", resIsSuccess);
		try {
			JSONObject jo = HttpUtils.getJsonObjectFromInputStream(params,request);
			if(jo == null || jo.isEmpty()) {	
				message.put("message", EnumUtil.getResponseValue(EnumUtil.ResponseCode.缺少请求参数.value));
				message.put("responseCode", EnumUtil.ResponseCode.缺少请求参数.value);
				return SUCCESS;
			}
			List<Map<String, Object>> result= filePathService.getUserImageByLimit(jo, user, request);
			System.out.println("获得文件路径的数量：" +result.size());
			message.put("isSuccess", true);
			message.put("message", result);
			return SUCCESS;
		} catch (Exception e) {
			e.printStackTrace();
		}
        return SUCCESS;
	}
	
	/**
     * 合并单个文件
     * 对断点上传的文件进行合并
     * @return
     */
    public String mergePortFile() {
    	long startTime = System.currentTimeMillis();
        try {
        	message.put("isSuccess", false);    
        	JSONObject jo = HttpUtils.getJsonObjectFromInputStream(params,request);
        	if(jo == null || jo.isEmpty()) {	
				message.put("message", EnumUtil.getResponseValue(EnumUtil.ResponseCode.缺少请求参数.value));
				message.put("responseCode", EnumUtil.ResponseCode.缺少请求参数.value);
				return SUCCESS;
			}
			if(user == null){
				message.put("message", EnumUtil.getResponseValue(EnumUtil.ResponseCode.请先登录.value));
				message.put("responseCode", EnumUtil.ResponseCode.请先登录.value);
				System.out.println(EnumUtil.getResponseValue(EnumUtil.ResponseCode.请先登录.value));
				return SUCCESS;
			}
		
			
        	String fileName = JsonUtil.getStringValue(jo, "fileName");//必须
        	String tableUuid = JsonUtil.getStringValue(jo, "uuid");  //客户端生成的唯一性uuid标识符
        	String tableName = JsonUtil.getStringValue(jo, "tableName");  //客户端生成的唯一性uuid标识符
        	int order = JsonUtil.getIntValue(jo, "order", 0); //多张图片时候的图片的位置，必须，为空默认是0	
        	String version = JsonUtil.getStringValue(jo, "file_version");  //文件版本信息
        	String desc = JsonUtil.getStringValue(jo, "file_desc"); //文件的描述信息
            
        	
        	//只有APP_Version才需要版本号和版本描述信息
        	if(user.isAdmin() && ConstantsUtil.UPLOAD_APP_VERSION_TABLE_NAME.equalsIgnoreCase(tableName)){
				if(StringUtil.isNull(version) || StringUtil.isNull(desc)){
					message.put("message", EnumUtil.getResponseValue(EnumUtil.ResponseCode.某些参数为空.value));
					message.put("responseCode", EnumUtil.ResponseCode.某些参数为空.value);
					return SUCCESS;
				}
			}
            //filePathService.merge(tableUuid, tableName, order, user, request);
            List<Map<String, Object>> list = uploadService.getOneUpload(tableUuid, tableName, order, user, request);
            if(list != null && list.size() >0){
            	
            	StringBuffer sourcePath = new StringBuffer();
            	sourcePath.append(ConstantsUtil.DEFAULT_SAVE_FILE_FOLDER);
            	sourcePath.append("temporary//");
            	sourcePath.append(user.getId());
            	sourcePath.append("_");
            	sourcePath.append(tableUuid);
            	sourcePath.append("_");
            	sourcePath.append(DateUtil.DateToString(new Date(), "yyyyMMddHHmmss"));
            	sourcePath.append("_");
            	sourcePath.append(fileName);
            	
            	File sourceFile = new File(sourcePath.toString());
            	if(sourceFile.exists()){
            		sourceFile.deleteOnExit();
            		sourceFile.createNewFile();
            	}else{
            		sourceFile.createNewFile();
            	}
            	
            	//保存合并后的输出对象
            	FileOutputStream out = new FileOutputStream(sourceFile);
            	for(Map<String, Object> map: list){
            		if(!FileUtil.readFile(StringUtil.changeNotNull(map.get("path")), out)){
            			return SUCCESS;	
            		}
            	}
            	//合并后关闭流
            	if(out != null){
            		out.flush();
            		out.close();
            	}
            	message.put("isSuccess", filePathService.saveSourceAndEachFile(sourcePath.toString(), user, tableUuid, tableName, order, version, desc));
            	
            	return SUCCESS;
            }else{
            	message.put("message", ResponseCode.没有操作实例.value);
            	message.put("responseCode", EnumUtil.getResponseValue(ResponseCode.没有操作实例.value));
            	return SUCCESS;
            }
        } catch (Exception e) {
            System.out.println("合并文件发生异常,错误原因 : " + e.getMessage());
        }
        long endTime = System.currentTimeMillis();
        System.out.println("合并文件总计耗时："+ (endTime - startTime) +"毫秒");
        return SUCCESS;
    }
    
    /**
     * 合并成功后删除片段的临时文件
     * @return
     */
    public String deletePortFile() {
    	long startTime = System.currentTimeMillis();
        try {
        	message.put("isSuccess", false);
        	JSONObject jo = HttpUtils.getJsonObjectFromInputStream(params,request);
        	if(jo == null || jo.isEmpty()) {	
				message.put("message", EnumUtil.getResponseValue(EnumUtil.ResponseCode.缺少请求参数.value));
				message.put("responseCode", EnumUtil.ResponseCode.缺少请求参数.value);
				return SUCCESS;
			}
        	String tableUuid = JsonUtil.getStringValue(jo, "uuid"); //客户端生成的唯一性uuid标识符
        	String tableName = JsonUtil.getStringValue(jo, "tableName");  //客户端生成的唯一性uuid标识符
        	if(tableName.equalsIgnoreCase(DataTableType.用户.value)){
        		System.out.println("更新用户的头像的缓存数据");
        		userHandler.updateUserPicPath(user.getId(), "30x30");
        	}
        	int order = JsonUtil.getIntValue(jo, "order", 0); //多张图片时候的图片的位置，必须，为空默认是0	
            List<Map<String, Object>> list = uploadService.getOneUpload(tableUuid, tableName, order, user, request);
            message.put("isSuccess", true);
            
            if(list != null && list.size() >0){   	
            	System.out.println("删除的文件的数量："+list.size());
            	UploadBean upload;
            	for(Map<String, Object> map: list){
        			upload = new UploadBean();
        			upload.setTableName(tableName);
        			upload.setTableUuid(tableUuid);
        			upload.setOrder(order);
        			upload.setSerialNumber(StringUtil.changeObjectToInt(map.get("serial_number")));
        			upload.setPath(String.valueOf(map.get("path")));
        			if(!uploadService.cancel(upload, user, request)){
        				return SUCCESS;
        			}
            	}
            	message.put("isSuccess", true);
            	return SUCCESS;
            }else{
            	System.out.println("删除的文件的数量为0");
            }
        } catch (Exception e) {
        	e.printStackTrace();
            System.out.println("删除断点片段文件发生异常,错误原因 : " + e.getMessage());
        }
        long endTime = System.currentTimeMillis();
        System.out.println("删除断点片段文件总计耗时："+ (endTime - startTime) +"毫秒");
        return SUCCESS;
    }
    
    /**
     * 分页获取上传的文件
     * @return
     */
    public String paging() {
    	long startTime = System.currentTimeMillis();
    	message.put("isSuccess", false);
    	try{
			JSONObject jo = HttpUtils.getJsonObjectFromInputStream(params,request);
			if(jo == null || jo.isEmpty()) {	
				message.put("message", EnumUtil.getResponseValue(EnumUtil.ResponseCode.缺少请求参数.value));
				message.put("responseCode", EnumUtil.ResponseCode.缺少请求参数.value);
				return SUCCESS;
			}
			message.putAll(filePathService.getUploadFileByLimit(jo, user, request));
			return SUCCESS;
		}catch(Exception e){
			e.printStackTrace();
		}
        long endTime = System.currentTimeMillis();
        System.out.println("分页获取上传的文件总计耗时："+ (endTime - startTime) +"毫秒");
        message.put("message", "获取上传文件列表失败");
        return SUCCESS;
    }
}
