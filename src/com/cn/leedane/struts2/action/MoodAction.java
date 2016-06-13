package com.cn.leedane.struts2.action;
import javax.annotation.Resource;

import net.sf.json.JSONObject;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.cn.leedane.Utils.BaseActionContext;
import com.cn.leedane.Utils.ConstantsUtil;
import com.cn.leedane.Utils.EnumUtil;
import com.cn.leedane.Utils.HttpUtils;
import com.cn.leedane.Utils.JsonUtil;
import com.cn.leedane.bean.FilePathBean;
import com.cn.leedane.bean.MoodBean;
import com.cn.leedane.bean.TemporaryBase64Bean;
import com.cn.leedane.service.FilePathService;
import com.cn.leedane.service.MoodService;
import com.cn.leedane.service.TemporaryBase64Service;
/**
 * 心情action类
 * @author LeeDane
 * 2015年11月22日 下午10:45:56
 * Version 1.0
 */
public class MoodAction extends BaseActionContext{	
	protected final Log log = LogFactory.getLog(getClass());
	private static final long serialVersionUID = 1L;
	
	//心情service
	private MoodService<MoodBean> moodService;
	
	//上传临时base64service
	private TemporaryBase64Service<TemporaryBase64Bean> temporaryBase64Service;
	
	//上传filePath表的service
	private FilePathService<FilePathBean> filePathService;
	
	
	@Resource
	public void setTemporaryBase64Service(
			TemporaryBase64Service<TemporaryBase64Bean> temporaryBase64Service) {
		this.temporaryBase64Service = temporaryBase64Service;
	}
	
	@Resource
	public void setFilePathService(FilePathService<FilePathBean> filePathService) {
		this.filePathService = filePathService;
	}
		
	@Resource
	public void setMoodService(MoodService<MoodBean> moodService) {
		this.moodService = moodService;
	}
	/**
	 * 发表心情(只是更新心情的草稿状态为正常状态)
	 * 适用于心情没有图片或者图片很小的情况下，其他情况下请使用uploadBase64()
	 * @return
	 */
	public String send(){
		message.put("isSuccess", resIsSuccess);
		try {
			JSONObject jo = HttpUtils.getJsonObjectFromInputStream(params,request);
			if(jo == null || jo.isEmpty()) {	
				message.put("message", EnumUtil.getResponseValue(EnumUtil.ResponseCode.缺少请求参数.value));
				message.put("responseCode", EnumUtil.ResponseCode.缺少请求参数.value);
				return SUCCESS;
			}
			message.putAll(moodService.updateMoodStatus(jo, ConstantsUtil.STATUS_NORMAL, request, user));
			return SUCCESS;
		} catch (Exception e) {
			e.printStackTrace();
		}
		message.put("message", EnumUtil.getResponseValue(EnumUtil.ResponseCode.服务器处理异常.value));
		message.put("responseCode", EnumUtil.ResponseCode.服务器处理异常.value);
		return SUCCESS;
	}
	
	/**
	 * 发表心情(为了用户可以后悔取消，这里只是先保存为草稿状态，需要用户再次发送请求调用send()方法保存为正常状态)
	 * @return
	 */
	public String sendDraft(){
		long start = System.currentTimeMillis();
		message.put("isSuccess", resIsSuccess);
		try {
			JSONObject jo = HttpUtils.getJsonObjectFromInputStream(params,request);
			if(jo == null || jo.isEmpty()) {	
				message.put("message", EnumUtil.getResponseValue(EnumUtil.ResponseCode.缺少请求参数.value));
				message.put("responseCode", EnumUtil.ResponseCode.缺少请求参数.value);
				return SUCCESS;
			}
			
			message.putAll(moodService.saveMood(jo, user, ConstantsUtil.STATUS_DRAFT, request));
			return SUCCESS;
		} catch (Exception e) {
			e.printStackTrace();
		}
		long end = System.currentTimeMillis();
		System.out.println("发表心情总计耗时：" +(end - start) +"毫秒");
		message.put("message", EnumUtil.getResponseValue(EnumUtil.ResponseCode.服务器处理异常.value));
		message.put("responseCode", EnumUtil.ResponseCode.服务器处理异常.value);
		return SUCCESS;
	}
	
	/**
	 * 发表文字心情
	 * @return
	 */
	public String sendWord(){
		long start = System.currentTimeMillis();	
        message.put("isSuccess", resIsSuccess);
		try {
			JSONObject jo = HttpUtils.getJsonObjectFromInputStream(params,request);
			if(jo == null || jo.isEmpty()) {	
				message.put("message", EnumUtil.getResponseValue(EnumUtil.ResponseCode.缺少请求参数.value));
				message.put("responseCode", EnumUtil.ResponseCode.缺少请求参数.value);
				return SUCCESS;
			}
			message.putAll(moodService.sendWord(jo, user, request));
			return SUCCESS;
		} catch (Exception e) {
			e.printStackTrace();
		}
		message.put("message", EnumUtil.getResponseValue(EnumUtil.ResponseCode.服务器处理异常.value));
		message.put("responseCode", EnumUtil.ResponseCode.服务器处理异常.value);
		long end = System.currentTimeMillis();
		System.out.println("发表文字心情总计耗时：" +(end - start) +"毫秒");
        return SUCCESS;
	}
	/**
	 * 删除心情
	 * @return
	 */
	public String delete(){
		message.put("isSuccess", resIsSuccess);
		try {
			JSONObject jo = HttpUtils.getJsonObjectFromInputStream(params,request);
			if(jo == null || jo.isEmpty()) {	
				message.put("message", EnumUtil.getResponseValue(EnumUtil.ResponseCode.缺少请求参数.value));
				message.put("responseCode", EnumUtil.ResponseCode.缺少请求参数.value);
				return SUCCESS;
			}
			
			message.putAll(moodService.deleteMood(jo, user, request));
			return SUCCESS;
		} catch (Exception e) {
			e.printStackTrace();
		}
		message.put("message", EnumUtil.getResponseValue(EnumUtil.ResponseCode.服务器处理异常.value));
		message.put("responseCode", EnumUtil.ResponseCode.服务器处理异常.value);
        return SUCCESS;
	}
	
	/**
	 * 获取指定符合条件的心情
	 * @return
	 */
	public String getPagingMood(){
		message.put("isSuccess", resIsSuccess);
		try {
			JSONObject jo = HttpUtils.getJsonObjectFromInputStream(params,request);
			if(jo == null || jo.isEmpty()) {	
				message.put("message", EnumUtil.getResponseValue(EnumUtil.ResponseCode.缺少请求参数.value));
				message.put("responseCode", EnumUtil.ResponseCode.缺少请求参数.value);
				return SUCCESS;
			}
			message.putAll(moodService.getMoodByLimit(jo, user, request));
			return SUCCESS;
		} catch (Exception e) {
			e.printStackTrace();
		}
		message.put("message", EnumUtil.getResponseValue(EnumUtil.ResponseCode.服务器处理异常.value));
		message.put("responseCode", EnumUtil.ResponseCode.服务器处理异常.value);
        return SUCCESS;
	}
	
	/**
	 * 模拟断点续传发送base64字符串
	 * 目的：某些图片文件过大，一次性传会因为网络等复杂因素发生意外导致不成功，下次再传又要重新发送，很消耗客户端的流量
	 * 实现：客户端分批上传文件，一部分上传成功，返回true，客户端自己记录上传的路径，服务器端负责将未完成的base64位的字符串
	 * 	保存在T_TEMP_BASE64表中
	 * 	当客户端传递结束的标记，就根据响应的命名规范合并相应的数据成一个完整的base64字符串，生成相应的图片放在file文件夹下，保存记录在T_FILEPATH记录表中，状态为0(禁用)，合并成功后批量删除T_TEMP_BASE64对应的数据，
	 *  以最新的覆盖前面的值(考虑到网络问题，其中某次服务器保存了，客户端没有接收到正确的返回，客户端可以再次发送请求)
	 * 客户端：发送的请求包含{"start":0, "content":"base64strhhhfdjhsdjAM,ZKKASKN", "end": 10000,"uuid":"78778223hdyy8e", "order":1, "isEnd": false}
	 * @return
	 */
	public String uploadBase64Str(){
		message.put("isSuccess", resIsSuccess);
		try {
			JSONObject jo = HttpUtils.getJsonObjectFromInputStream(params,request);
			if(jo == null || jo.isEmpty()) {	
				return SUCCESS;
			}
			boolean isEnd = JsonUtil.getBooleanValue(jo, "isEnd");
			
			//上传该base64字符串结束
			if(isEnd){
				filePathService.saveEachTemporaryBase64ToFilePath(jo, user, request);
			//直接保存上传记录
			}else{
				resIsSuccess = temporaryBase64Service.saveBase64Str(jo, user, request);
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		message.put("isSuccess", resIsSuccess);
        return SUCCESS;
	}
	
	/**
	 * 应用于分开上传图片都成功后的发表心情
	 * 客户端：发送的请求格式{"content":"今天天气不错", "froms": "android客户端","uuid":"78778223hdyy8e", "hasImg":true}
	 * @return
	 */
	public String sendDivideMood(){
		long start = System.currentTimeMillis();
		message.put("isSuccess", resIsSuccess);
		try {
			JSONObject jo = HttpUtils.getJsonObjectFromInputStream(params,request);
			if(jo == null || jo.isEmpty()) {	
				message.put("message", EnumUtil.getResponseValue(EnumUtil.ResponseCode.缺少请求参数.value));
				message.put("responseCode", EnumUtil.ResponseCode.缺少请求参数.value);
				return SUCCESS;
			}
			message.putAll(moodService.saveDividedMood(jo, user, ConstantsUtil.STATUS_NORMAL, request));
			return SUCCESS;
		} catch (Exception e) {
			e.printStackTrace();
		}
		long end = System.currentTimeMillis();
		System.out.println("发表心情总计耗时：" +(end - start) +"毫秒");
		message.put("message", EnumUtil.getResponseValue(EnumUtil.ResponseCode.服务器处理异常.value));
		message.put("responseCode", EnumUtil.ResponseCode.服务器处理异常.value);
        return SUCCESS;
	}
	
	/**
	 * 删除已经上传的base64文件和心情列表中相应的记录
	 * 用处：一般就是用户在文件上传过程中，自行取消发布的心情
	 * @return
	 */
	public String deleteUploadBase64Str(){
		return SUCCESS;
	}
	
	/**
	 * 下载图片源(查找t_filepath)
	 * {"uuid":"jhasdjdasd", "order": "1", "size":"default"}
	 * @return
	 */
	public String downloadBase64Str(){
		message.put("isSuccess", resIsSuccess);
		try {
			JSONObject jo = HttpUtils.getJsonObjectFromInputStream(params,request);
			if(jo == null || jo.isEmpty()) {	
				message.put("message", EnumUtil.getResponseValue(EnumUtil.ResponseCode.缺少请求参数.value));
				message.put("responseCode", EnumUtil.ResponseCode.缺少请求参数.value);
				return SUCCESS;
			}
			
			resmessage = filePathService.downloadBase64Str(jo, user, request);
			resIsSuccess = true;
		} catch (Exception e) {
			e.printStackTrace();
		}
		message.put("isSuccess", resIsSuccess);
		message.put("message", resmessage);
        return SUCCESS;
	}
	
	/**
	 * 获取单篇文章的图片列表(查t_mood表)
	 * {"uuid":"jhasdjdasd"}
	 * @return 图片地址列表
	 */
	/*public String getOneMoodImgs(){
		message.put("isSuccess", resIsSuccess);
		try {
			JSONObject jo = HttpUtils.getJsonObjectFromInputStream(params,request);
			if(jo.isEmpty()) {	
				return SUCCESS;
			}
			
			resmessage = filePathService.getOneMoodImgs(jo, user, request);
			resIsSuccess = true;
		} catch (Exception e) {
			e.printStackTrace();
		}
		message.put("isSuccess", resIsSuccess);
		message.put("message", resmessage);
        return SUCCESS;
	}*/
	
	/**
	 * 获取指定用户所有的发表成功的心情总数
	 * {"uid":1"},非必须，为空表示当前登录用户
	 * @return 图片地址列表
	 */
	public String getCountByUser(){
		message.put("isSuccess", resIsSuccess);
		try {
			JSONObject jo = HttpUtils.getJsonObjectFromInputStream(params,request);
			if(jo == null || jo.isEmpty()) {	
				return SUCCESS;
			}
			message.put("message", moodService.getCountByUser(jo, user, request));
			resIsSuccess = true;
			return SUCCESS;
		} catch (Exception e) {
			e.printStackTrace();
		}
		message.put("isSuccess", resIsSuccess);
		
        return SUCCESS;
	}
	/**
	 * 获取心情的信息
	 * {'uid':1; 'mid':1},uid非必须，为空表示当前登录用户
	 * @return 返回心情的内容，图片地址（120x120大小的图像）
	 */
	public String detail(){
		message.put("isSuccess", resIsSuccess);
		try {
			JSONObject jo = HttpUtils.getJsonObjectFromInputStream(params,request);
			if(jo == null || jo.isEmpty()) {	
				message.put("message", EnumUtil.getResponseValue(EnumUtil.ResponseCode.缺少请求参数.value));
				message.put("responseCode", EnumUtil.ResponseCode.缺少请求参数.value);
				return SUCCESS;
			}
			message.putAll(moodService.detail(jo, user, request, "120x120"));
			return SUCCESS;
		} catch (Exception e) {
			e.printStackTrace();
		}
		message.put("message", EnumUtil.getResponseValue(EnumUtil.ResponseCode.服务器处理异常.value));
		message.put("responseCode", EnumUtil.ResponseCode.服务器处理异常.value);
        return SUCCESS;
	}
	/**
	 * 获取心情的图片
	 * {'table_uuid':'leedane4e2f2684-ac82-4186-97fa-d8807211ef92', 'table_name':'t_mood'},uuid必须
	 * @return 返回心情的内容，图片地址（120x120大小的图像）
	 */
	public String detailImgs(){
		message.put("isSuccess", resIsSuccess);
		try {
			JSONObject jo = HttpUtils.getJsonObjectFromInputStream(params,request);
			if(jo == null || jo.isEmpty()) {	
				message.put("message", EnumUtil.getResponseValue(EnumUtil.ResponseCode.缺少请求参数.value));
				message.put("responseCode", EnumUtil.ResponseCode.缺少请求参数.value);
				return SUCCESS;
			}
			message.put("message", moodService.detailImgs(jo, user, request));
			message.put("isSuccess", true);
			return SUCCESS;
		} catch (Exception e) {
			e.printStackTrace();
		}
		message.put("message", EnumUtil.getResponseValue(EnumUtil.ResponseCode.服务器处理异常.value));
		message.put("responseCode", EnumUtil.ResponseCode.服务器处理异常.value);
        return SUCCESS;
	}
}
