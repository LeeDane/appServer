package com.cn.leedane.struts2.action;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import com.cn.leedane.Utils.BaseActionContext;
import com.cn.leedane.file.upload.Progress;
import com.cn.leedane.file.upload.UploadFile;
/**
 * 多文件的上传
 * @author LeeDane
 * 2015年9月15日 上午10:21:29
 * Version 1.0
 */
public class MulFileUploadAction extends BaseActionContext{
    /** {field's description} */
    private static final long serialVersionUID = 6649027352616232244L;
    
	/**
	 * 响应结果信息
	 */
	private Map<String, Object> message = new HashMap<String, Object>();

    public Map<String, Object> getMessage() {
		return message;
	}
	public void setMessage(Map<String, Object> message) {
		this.message = message;
	}
	/**
     * 上传文件页面
     * 
     * @return page view
     */
    public String preupload() {
        return SUCCESS;
    }
    /**
     * 上传文件
     * 
     * @return page view
     */
    public String uploadfile() {
        try {
            UploadFile.upload(this.request, this.response);
        } catch (IOException e) {
            System.out.println("上传文件发生异常,错误原因 : " + e.getMessage());
        }
        return null;
    }
    /**
     * 显示上传文件进度进度
     * 
     * @return page view
     */
    public String progress() {
        //String callback1 = this.request.getParameter("callback1");
        String callback2 = this.request.getParameter("callback2");
        // 缓存progress对象的key值
        String key = Integer.toString(request.hashCode());
        // 新建当前上传文件的进度信息对象
        Progress p = new Progress();
        // 缓存progress对象
        this.request.getSession().setAttribute(key, p);
        
        
        response.setContentType("text/html;charset=UTF-8");
        response.setHeader("pragma", "no-cache");
        response.setHeader("cache-control", "no-cache");
        response.setHeader("expires", "0");
        try {
            //UploadFile.execClientScript(response, callback1 + "(" + key + ")");
        	//message = {"callback1",};
            long temp = 0l;
            while (!p.isComplete()) {
                if (temp != p.getCurrentLength()) {
                    temp = p.getCurrentLength();
                    // 向客户端显示进度
                   // UploadFile.execClientScript(response, callback2 + "("
                    //        + p.getCurrentLength() + "," + p.getLength() + ")");
                   // this.message = "{\"callback2\":"+callback2+",\"currentLenght\":"
                    //		+ p.getCurrentLength()+",\"lenght\":"+p.getLength()+"}";
                    message.put("callback2", callback2);
                    message.put("currentLenght", p.getCurrentLength());
                    message.put("lenght", p.getLength());
                    
                } else {
                    //System.out.println("progress的状态 ：" + p.isComplete());
                    //System.out.println("progress上传的数据量 ：+ " + p.getCurrentLength());
                    //上传进度没有变化时候,不向客户端写数据,写数据过于频繁会让chrome没响应
                    Thread.sleep(300);
                }
                
            }
            
        } catch (Exception e) {
            System.out.println("调用客户端脚本错误,原因 ：" + e.getMessage());
            p.setComplete(true);
            
        }
        
        this.request.getSession().removeAttribute(key);
        System.out.println("删除progress对象的session key");
        
        return SUCCESS;
    }
}
