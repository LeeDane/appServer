package com.cn.leedane.struts2.action;
import java.io.OutputStream;
import java.io.RandomAccessFile;

import net.sf.json.JSONObject;

import com.cn.leedane.Utils.Base64ImageUtil;
import com.cn.leedane.Utils.BaseActionContext;
import com.cn.leedane.Utils.ConstantsUtil;
import com.cn.leedane.Utils.EnumUtil;
import com.cn.leedane.Utils.HttpUtils;
import com.cn.leedane.Utils.StringUtil;

/**
 * 下载的action类
 * @author LeeDane
 * 2015年10月20日 下午3:13:06
 * Version 1.0
 */
public class DownloadAction extends BaseActionContext {

	private static final long serialVersionUID = 1L;
	/**
	 * 请求的参数信息
	 */
	public String params;
	
	private int start; //开始的位置
	private int end;  //结束的位置
	
	private int requestSize;//请求获取的文件大小
	
	private String filename;//请求下载的文件名称
	
	/**
	 * 下载
	 */
	public void executeDown() throws Exception{
		
		//获取下载的范围
		String range = request.getHeader("RANGE");
		if(!StringUtil.isNull(range) && range.startsWith("bytes=")){
			String[] values =range.split("=")[1].split("-");  
            start = Integer.parseInt(values[0]);  
            //end = Integer.parseInt(values[1]);  
		}
		
		if(end!=0 && end > start){  
            requestSize = end - start + 1;  
            response.addHeader("content-length", ""+(requestSize));  
        } else {  
            requestSize = Integer.MAX_VALUE;  
        }  
		
		JSONObject jo = HttpUtils.getJsonObjectFromInputStream(params,request);
		if(jo != null && jo.containsKey("filename")){
			filename = jo.getString("filename");
		}
		
		if(StringUtil.isNull(filename)){
			filename = "F:/个人/资料/萌头像/02.jpg";
		}
		
		RandomAccessFile raFile = new RandomAccessFile(filename, "r");
		byte[] buffer = new byte[4096];  
        
		response.setContentType("application/x-download");  
		response.addHeader("Content-Disposition", "attachment;filename=a.iso");  
		
		response.addHeader("Pragma", "no-cache");
		response.addHeader("Cache-Control", "no-cache");
        OutputStream os = response.getOutputStream();  
          
        int needSize = requestSize;  
        
        response.setContentLength(needSize);
        // 设置状态 HTTP/1.1 206 Partial Content
        response.setStatus(javax.servlet.http.HttpServletResponse.SC_PARTIAL_CONTENT);

        // 表示使用了断点续传（默认是“none”，可以不指定）
        response.setHeader("Accept-Ranges", "bytes");
          
        raFile.seek(start);  
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
	}
	
	/**
	 * 
	 * 获取webroot/file文件夹下面的图片
	 * @return
	 * @throws Exception 
	 */
	public String getLocalBase64Image(){
		try{
			JSONObject jo = HttpUtils.getJsonObjectFromInputStream(params,request);
			message.put("isSuccess", resIsSuccess);
			if(jo == null || jo.isEmpty()) {	
				message.put("message", EnumUtil.getResponseValue(EnumUtil.ResponseCode.缺少请求参数.value));
				message.put("responseCode", EnumUtil.ResponseCode.缺少请求参数.value);
				return SUCCESS;
			}
			String filename = jo.getString("filename");
			if(StringUtil.isNull(filename)){
				message.put("isSuccess", resIsSuccess);
				message.put("message", "文件名称为空");
				return SUCCESS;
			}
			
			String suffixs = StringUtil.getSuffixs(filename);	
			System.out.println("suffixs:"+suffixs);
			filename = ConstantsUtil.DEFAULT_SAVE_FILE_FOLDER + "file//" + filename;
			resmessage = Base64ImageUtil.convertImageToBase64(filename, suffixs);
			resIsSuccess = true;
		}catch(Exception e){
			e.printStackTrace();
			resmessage = "获取文件出现异常，请稍后重试";
		}
		message.put("isSuccess", resIsSuccess);
		message.put("message", resmessage);
		
		return SUCCESS;
	}
	
}
