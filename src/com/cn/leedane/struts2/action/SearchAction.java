package com.cn.leedane.struts2.action;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import net.sf.json.JSONObject;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.response.QueryResponse;
import org.springframework.beans.factory.annotation.Autowired;

import com.cn.leedane.Utils.BaseActionContext;
import com.cn.leedane.Utils.ConstantsUtil;
import com.cn.leedane.Utils.EnumUtil;
import com.cn.leedane.Utils.HttpUtils;
import com.cn.leedane.Utils.JsonUtil;
import com.cn.leedane.Utils.StringUtil;
import com.cn.leedane.bean.BlogBean;
import com.cn.leedane.bean.MoodBean;
import com.cn.leedane.bean.UserBean;
import com.cn.leedane.lucene.solr.SolrHandler;
import com.cn.leedane.service.BlogService;
import com.cn.leedane.service.MoodService;
import com.cn.leedane.service.UserService;
/**
 * 搜索专用的action
 * @author LeeDane
 * 2016年2月17日 下午5:06:44
 * Version 1.0
 */
public class SearchAction extends BaseActionContext{	
	protected final Log log = LogFactory.getLog(getClass());
	private static final long serialVersionUID = 1L;
	
	@Autowired
	private UserService<UserBean> userService;
	
	public void setUserService(UserService<UserBean> userService) {
		this.userService = userService;
	}
	
	@Autowired
	private MoodService<MoodBean> moodService;
	
	public void setMoodService(MoodService<MoodBean> moodService) {
		this.moodService = moodService;
	}
	
	@Autowired
	private BlogService<BlogBean> blogService;
	
	public void setBlogService(BlogService<BlogBean> blogService) {
		this.blogService = blogService;
	}
	
	
	/**
	 * 执行搜索
	 */
	public String execute(){
		message.put("isSuccess", resIsSuccess);
		try {
			JSONObject jo = HttpUtils.getJsonObjectFromInputStream(params,request);
			if(jo == null || jo.isEmpty()) {	
				message.put("message", EnumUtil.getResponseValue(EnumUtil.ResponseCode.缺少请求参数.value));
				message.put("responseCode", EnumUtil.ResponseCode.缺少请求参数.value);
				return SUCCESS;
			}
			
			//查询的类型，目前支持0、全部，1、博客（正文和标题），2、说说(正文)，3、用户(姓名，中文名，邮件，手机号码，证件号码)
			int type = JsonUtil.getIntValue(jo, "type", 0);
			String keyword = JsonUtil.getStringValue(jo, "keyword"); //搜索关键字
			if(StringUtil.isNull(keyword)){
				message.put("message", "请检索关键字为空");
				return SUCCESS;
			}
			List<QueryResponse> responses = new ArrayList<QueryResponse>();
			List<Integer> tempIds = new ArrayList<Integer>();
			//获取全部
			if(type == 0){
				tempIds.add(1);
				tempIds.add(2);
				tempIds.add(3);
			}else{
				tempIds.add(type);
			}
			//启动多线程
			List<Future<QueryResponse>> futures = new ArrayList<Future<QueryResponse>>();
			SingleSearchTask searchTask;
			//派发5个线程执行
			ExecutorService threadpool = Executors.newFixedThreadPool(5);
			for(int tempId: tempIds){
				searchTask = new SingleSearchTask(tempId, keyword, 0);
				futures.add(threadpool.submit(searchTask));
			}
			threadpool.shutdown();
			
			for(int i = 0; i < futures.size() ;i++){
				try {
					//保存每次请求执行后的结果
					if(futures.get(i).get() != null)
							responses.add(futures.get(i).get());
				} catch (InterruptedException e) {
					e.printStackTrace();
					futures.get(i).cancel(true);
				} catch (ExecutionException e) {
					e.printStackTrace();
					futures.get(i).cancel(true);
				}
			}
			
			for(QueryResponse response: responses){
				//搜索得到的结果数
			    System.out.println("Find:" + response.getResults().getNumFound());
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
        return SUCCESS;
	}
	
	/**
	 * 搜索用户
	 * @return
	 */
	public String user() {
		try {
			message.put("isSuccess", false);
			JSONObject jo = HttpUtils.getJsonObjectFromInputStream(params, request);  
			if(jo == null || jo.isEmpty()) {	
				message.put("message", EnumUtil.getResponseValue(EnumUtil.ResponseCode.缺少请求参数.value));
				message.put("responseCode", EnumUtil.ResponseCode.缺少请求参数.value);
				return SUCCESS;
			}
			message.putAll(userService.search(jo, user, request));
			return SUCCESS;
		} catch (Exception e) {
			e.printStackTrace();
		}     
        message.put("message", EnumUtil.getResponseValue(EnumUtil.ResponseCode.服务器处理异常.value));
		message.put("responseCode", EnumUtil.ResponseCode.服务器处理异常.value);
        return SUCCESS;
	}
	
	/**
	 * 搜索心情
	 * @return
	 */
	public String mood() {
		try {
			message.put("isSuccess", false);
			JSONObject jo = HttpUtils.getJsonObjectFromInputStream(params, request);  
			if(jo == null || jo.isEmpty()) {	
				message.put("message", EnumUtil.getResponseValue(EnumUtil.ResponseCode.缺少请求参数.value));
				message.put("responseCode", EnumUtil.ResponseCode.缺少请求参数.value);
				return SUCCESS;
			}
			message.putAll(moodService.search(jo, user, request));
			return SUCCESS;
		} catch (Exception e) {
			e.printStackTrace();
		}     
        message.put("message", EnumUtil.getResponseValue(EnumUtil.ResponseCode.服务器处理异常.value));
		message.put("responseCode", EnumUtil.ResponseCode.服务器处理异常.value);
        return SUCCESS;
	}
	
	/**
	 * 搜索心情
	 * @return
	 */
	public String blog() {
		try {
			message.put("isSuccess", false);
			JSONObject jo = HttpUtils.getJsonObjectFromInputStream(params, request);  
			if(jo == null || jo.isEmpty()) {	
				message.put("message", EnumUtil.getResponseValue(EnumUtil.ResponseCode.缺少请求参数.value));
				message.put("responseCode", EnumUtil.ResponseCode.缺少请求参数.value);
				return SUCCESS;
			}
			message.putAll(blogService.search(jo, user, request));
			return SUCCESS;
		} catch (Exception e) {
			e.printStackTrace();
		}     
        message.put("message", EnumUtil.getResponseValue(EnumUtil.ResponseCode.服务器处理异常.value));
		message.put("responseCode", EnumUtil.ResponseCode.服务器处理异常.value);
        return SUCCESS;
	}
	
	
	/**
	 * 获取指定类型搜索的域（type不能为0，为0需要特殊处理）
	 * 查询的类型，目前支持0、全部，1、博客（正文和标题），2、说说(正文)，3、用户(姓名，中文名，邮件，手机号码,证件号码)
	 * @param tempId
	 * @return
	 */
	private String[] getSearchFields(int tempId){
		List<String> array = new ArrayList<String>();
		switch (tempId) {
		case 1:
			array.add("btitle");
			array.add("bcontent");
			break;
		case 2:
			array.add("mcontent");
			break;
		case 3:
			array.add("uchina_name");
			array.add("uaccount");
			array.add("ureal_name");
			array.add("umobile_phone");
			array.add("uid_card");
			array.add("uemail");
			break;
		}
		return (String[]) array.toArray();
	}
	
	/**
	 * 获取指定类型搜索的数量
	 * 查询的类型，目前支持0、全部，1、博客（正文和标题），2、说说(正文)，3、用户(姓名，中文名，邮件，手机号码,证件号码)
	 * @param type
	 * @return
	 */
	private int getSearchRows(int tempId){
		int rows = 0;
		switch (tempId) {
		case 1:
			rows = ConstantsUtil.DEFAULT_BLOG_SEARCH_ROWS;
			break;
		case 2:
			rows = ConstantsUtil.DEFAULT_MOOD_SEARCH_ROWS;
			break;
		case 3:
			rows = ConstantsUtil.DEFAULT_USER_SEARCH_ROWS;
			break;
		}
		return rows;
	}
	
	class SingleSearchTask implements Callable<QueryResponse>{
		private int tempId;
		private String keyword;
		private int start;
		public SingleSearchTask(int tempId, String keyword, int start) {
			this.tempId = tempId;
			this.keyword = keyword;
			this.start = start;
		}

		@Override
		public QueryResponse call() throws Exception {
			SolrQuery query = new SolrQuery();
		    query.setQuery(keyword);
		    query.setFields(getSearchFields(tempId));
		    //query.setSort("price", ORDER.asc);
		    query.setStart(start);
		    query.setRows(getSearchRows(tempId));
			return SolrHandler.getInstance().query(query);
		}
	}
}
