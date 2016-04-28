package com.cn.leedane.wechat.service.impl;

import java.io.IOException;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import com.cn.leedane.Utils.StringUtil;
import com.cn.leedane.wechat.service.BaseXMLWechatService;
import com.cn.leedane.wechat.util.HttpRequestUtil;

public class TranslationXMLService extends BaseXMLWechatService {

	public TranslationXMLService(HttpServletRequest request, Map<String, String> map) {
		super(request, map);
	}
	@Override
	protected String execute() {
		String r = "";	
		try {			
			if(Content.startsWith("翻译")) {
				Content = Content.substring(2, Content.length());	
				if(StringUtil.isNull(Content)){
					r = "进入翻译模式";
				}else{					
					r = HttpRequestUtil.sendAndRecieveFromYoudao(Content);
					r = getYoudaoFanyiContent(r);
				}
			}else{
				r = HttpRequestUtil.sendAndRecieveFromYoudao(Content);
				r = getYoudaoFanyiContent(r);
			}	
		} catch (IOException e) {
			e.printStackTrace();
		}
		return r;
	}
	
	
	/**
	 * 从有道翻译返回的json数据中获得翻译内容
	 * @param returnMsg
	 * @return
	 */
	private String getYoudaoFanyiContent(String returnMsg) {
		System.out.println("有道翻译返回的信息:"+returnMsg);
		StringBuffer buffer = new StringBuffer();
		JSONObject json = JSONObject.fromObject(returnMsg);
		
		JSONArray array = json.getJSONArray("translation");
		if(array.size() > 0){
			for(int i = 0 ; i < array.size(); i++){
				buffer.append(array.get(i).toString()+"  \n");
			}
		}
		return buffer.toString();
	}

}
