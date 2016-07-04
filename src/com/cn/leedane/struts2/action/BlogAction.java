package com.cn.leedane.struts2.action;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONObject;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import com.cn.leedane.Utils.BaseActionContext;
import com.cn.leedane.Utils.ConstantsUtil;
import com.cn.leedane.Utils.EnumUtil;
import com.cn.leedane.Utils.HttpUtils;
import com.cn.leedane.Utils.JsonUtil;
import com.cn.leedane.Utils.JsoupUtil;
import com.cn.leedane.Utils.SpringUtils;
import com.cn.leedane.Utils.StringUtil;
import com.cn.leedane.Utils.EnumUtil.DataTableType;
import com.cn.leedane.bean.BlogBean;
import com.cn.leedane.bean.UserBean;
import com.cn.leedane.cache.SystemCache;
import com.cn.leedane.service.BlogService;
import com.cn.leedane.service.UserService;

/**
 * 博客Action类
 * @author LeeDane
 * 2015年4月3日 上午11:28:36
 * Version 1.0
 */

public class BlogAction extends BaseActionContext{	
	protected final Log log = LogFactory.getLog(UserAction.class);
	/**
	 * 请求的参数信息
	 */
	public String params;
	
	/**
	 * 系统级别的缓存对象
	 */
	private SystemCache systemCache;
	
	public Map<String, Object> getMessage() {
		return message;
	}

	public void setMessage(Map<String, Object> message) {
		this.message = message;
	}
	
	BlogBean blog;//博客实体
	
	//返回结果中包含的是否成功
	private boolean resIsSuccess = false;
	//返回结果中包含的提示信息
	private String resmessage;
		
	private BlogService<BlogBean> blogService;
	
	private UserService<UserBean> userService;
		
	HttpServletResponse response;
	HttpServletRequest request;	
	Map<String, Object> session;
	
	@Resource
	public void setBlogService(BlogService<BlogBean> blogService) {
		this.blogService = blogService;
	}
	
	@Resource
	public void setUserService(UserService<UserBean> userService) {
		this.userService = userService;
	}
	private static final long serialVersionUID = 1L;

	
	public String getParams() {
		return params;
	}

	public void setParams(String params) {
		this.params = params;
	}

	/**
	 * 发布博客
	 * @return
	 * @throws Exception 
	 */
	public String releaseBlog() throws Exception {
		message.put("isSuccess", false);
		try{
			JSONObject jo = HttpUtils.getJsonObjectFromInputStream(params,request);
			if(jo == null || jo.isEmpty()) {
				message.put("message", EnumUtil.getResponseValue(EnumUtil.ResponseCode.缺少请求参数.value));
				message.put("responseCode", EnumUtil.ResponseCode.缺少请求参数.value);
				return SUCCESS;
			}
			systemCache = (SystemCache) SpringUtils.getBean("systemCache");
			String adminId = (String) systemCache.getCache("admin-id");
			int aid = 1;
			if(!StringUtil.isNull(adminId)){
				aid = Integer.parseInt(adminId);
			}
			UserBean user = userService.findById(aid);
			
			/**
			 * 是否有主图
			 */
			boolean hasImg = JsonUtil.getBooleanValue(jo, "has_img");
			
			/**
			 * 是否要自动截取摘要摘要
			 */
			boolean hasDigest = JsonUtil.getBooleanValue(jo, "has_digest");
			blog = new BlogBean();
			blog.setTitle(JsonUtil.getStringValue(jo, "title"));
			String content = JsonUtil.getStringValue(jo, "content");
			blog.setContent(content);
			blog.setTag(JsonUtil.getStringValue(jo, "tag"));
			blog.setFroms(JsonUtil.getStringValue(jo, "froms"));
			blog.setStatus(JsonUtil.getIntValue(jo, "status"));
			String imgUrl = JsonUtil.getStringValue(jo, "img_url");
			String digest = "";
			if(hasImg){
				//获取主图信息
				blog.setHasImg(hasImg);
						
				//判断是否有指定的图片，没有的话会自动解析内容中的第一张图片的src的值作为地址
				if(StringUtil.isNull(imgUrl)){
					Document h = Jsoup.parse(content);
					Elements a = h.getElementsByTag("img");
					imgUrl= a.get(0).attr("src");
				}
				
				blog.setImgUrl(imgUrl);
				
				/**
				 * 非链接的字符串
				 */
				if(!StringUtil.isLink(imgUrl)){
					JsoupUtil.getInstance().base64ToLink(imgUrl, user.getAccount());
					
				}
				//将base64位的图片保存在数据在本地
				/*String path = "F:/upload";
				File file = new File(path);
				System.out.println("==="+path);
				if(!file.exists()){
					file.mkdir();
				}
				String account = user != null ? user.getAccount() : "admin";
				String filePath = path + "/" + account +"_"+System.currentTimeMillis()+ (int)Math.random()*1000 +".png";
				System.out.println(ImageUtil.convertBase64ToImage(filePath, imgUrl));*/
				
			}
			
			blog.setCreateUser(user);
			blog.setCreateTime(new Date());
			
			if(hasDigest){
				digest = JsoupUtil.getInstance().getDigest(content, 0, 100);
			}else{
				digest = JsonUtil.getStringValue(jo, "digest");
			}
			
			blog.setDigest(digest);
			message.putAll(blogService.addBlog(blog));   
			return SUCCESS;
		}catch (Exception e) {
			e.printStackTrace();
		}
		message.put("message", EnumUtil.getResponseValue(EnumUtil.ResponseCode.服务器处理异常.value));
		message.put("responseCode", EnumUtil.ResponseCode.服务器处理异常.value);
		return SUCCESS;
	}
	
	/**
	 * 删除博客
	 * @return
	 */
	public String deleteBlog() {
		message.put("isSuccess", false);
		JSONObject jo;
		try {
			jo = HttpUtils.getJsonObjectFromInputStream(params,request);
			if(jo == null || jo.isEmpty()) {
				message.put("message", EnumUtil.getResponseValue(EnumUtil.ResponseCode.缺少请求参数.value));
				message.put("responseCode", EnumUtil.ResponseCode.缺少请求参数.value);
				return SUCCESS;
			}
			message.putAll(blogService.deleteById(jo, request, user));
			return SUCCESS;
		} catch (Exception e) {
			e.printStackTrace();
		}	
		message.put("message", EnumUtil.getResponseValue(EnumUtil.ResponseCode.服务器处理异常.value));
		message.put("responseCode", EnumUtil.ResponseCode.服务器处理异常.value);
		return SUCCESS;
	}
	
	/**
	 * 获得分页的Blog
	 * @return
	 */
	public String getPagingBlog(){
		message.put("isSuccess", resIsSuccess);
		try{
			JSONObject jo = HttpUtils.getJsonObjectFromInputStream(params,request);
			if(jo == null || jo.isEmpty()) {
				message.put("message", EnumUtil.getResponseValue(EnumUtil.ResponseCode.缺少请求参数.value));
				message.put("responseCode", EnumUtil.ResponseCode.缺少请求参数.value);
				return SUCCESS;
			}
			int pageSize = JsonUtil.getIntValue(jo, "pageSize"); //每页的大小
			int lastId = JsonUtil.getIntValue(jo, "last_id");
			int firstId = JsonUtil.getIntValue(jo, "first_id");
			
			//执行的方式，现在支持：uploading(向上刷新)，lowloading(向下刷新)，firstloading(第一次刷新)
			String method = JsonUtil.getStringValue(jo, "method");
			List<Map<String,Object>> r = new ArrayList<Map<String,Object>>();
			StringBuffer sql = new StringBuffer();
			System.out.println("执行的方式是："+method +",pageSize:"+pageSize+",lastId:"+lastId+",firstId:"+firstId);
			//下刷新
			if(method.equalsIgnoreCase("lowloading")){
				sql.append("select b.id, b.img_url, b.title, b.has_img, b.tag, date_format(b.create_time,'%Y-%c-%d %H:%i:%s') create_time");
				sql.append(" , b.digest, b.froms, b.create_user_id, u.account ");
				sql.append(" from "+DataTableType.博客.value+" b inner join "+DataTableType.用户.value+" u on b.create_user_id = u.id ");
				sql.append(" where b.status = ? and b.img_url != '' and b.id < ? order by b.id desc limit 0,?");
				r = blogService.executeSQL(sql.toString(), ConstantsUtil.STATUS_NORMAL, lastId, pageSize);
				
			//上刷新
			}else if(method.equalsIgnoreCase("uploading")){
				sql.append("select b.id, b.img_url, b.title, b.has_img, b.tag, date_format(b.create_time,'%Y-%c-%d %H:%i:%s') create_time ");
				sql.append(" , b.digest, b.froms, b.create_user_id, u.account ");
				sql.append(" from "+DataTableType.博客.value+" b inner join "+DataTableType.用户.value+" u on b.create_user_id = u.id ");
				sql.append(" where b.status = ? and b.img_url != '' and b.id > ?  limit 0,?");
				r = blogService.executeSQL(sql.toString(), ConstantsUtil.STATUS_NORMAL, firstId, pageSize);
				
			//第一次刷新
			}else if(method.equalsIgnoreCase("firstloading")){
				sql.append("select b.id, b.img_url, b.title, b.has_img, b.tag, date_format(b.create_time,'%Y-%c-%d %H:%i:%s') create_time ");
				sql.append(" , b.digest, b.froms, b.create_user_id, u.account ");
				sql.append(" from "+DataTableType.博客.value+" b inner join "+DataTableType.用户.value+" u on b.create_user_id = u.id ");
				sql.append(" where b.status = ? and b.img_url != ''  order by b.id desc limit 0,?");
				r = blogService.executeSQL(sql.toString(), ConstantsUtil.STATUS_NORMAL, pageSize);
			}else{
				message.put("isSuccess", resIsSuccess);
				message.put("message", "目前暂不支持的操作方法");
				return SUCCESS;
			}
			 
			resIsSuccess = true;
			System.out.println("数量："+r.size());
			if(r.size() == 5){
				System.out.println("开始ID:"+r.get(0).get("id"));
				System.out.println("结束ID:"+r.get(4).get("id"));
			}
			message.put("isSuccess", resIsSuccess);
			message.put("message", r);
		}catch(Exception e){
			e.printStackTrace();
		}
		return SUCCESS;
	}
	
	/**
	 * 获取博客的内容
	 * @return
	 */
	public String getContent(){
		try{
			/*JSONObject jo = HttpUtils.getJsonObjectFromInputStreamByCheck(params,request);
			if(jo == null || jo.isEmpty()) return SUCCESS;
			int blog_id = JsonUtil.getIntValue(jo, "blog_id"); //获取博客id
*/			
			String blogId = request.getParameter("blog_id");
			if(StringUtil.isNull(blogId)) {
				return SUCCESS;
			}
			int blog_id = Integer.parseInt(blogId);
			
			if(blog_id < 1) return SUCCESS;
			//int blog_id = 1;
			String sql = "select content, read_number from "+DataTableType.博客.value+" where status = ? and id = ?";
			List<Map<String,Object>> r = blogService.executeSQL(sql, ConstantsUtil.STATUS_NORMAL, blog_id);				
			if(r.size() == 1){
				Map<String,Object> map = r.get(0);
				//更新读取数量
				int readNum = StringUtil.changeObjectToInt(map.get("read_number"));
				blogService.updateReadNum(blog_id, readNum + 1);
				if(map.containsKey("content")){
					request.setAttribute("content", map.get("content"));
					return "content-page";
				}
			}
		}catch(Exception e){
			e.printStackTrace();
		}
		request.setAttribute("content", "抱歉,服务器获取博客失败");
		return "content-page";
		
	}
	
	/**
	 * 根据博客ID获取一条博客信息
	 * @return
	 * @throws Exception 
	 */
	public String getOneBlog() throws Exception {
		JSONObject jo = HttpUtils.getJsonObjectFromInputStream(params,request);
		if(jo == null || jo.isEmpty()) return SUCCESS;
		
		String id = jo.getString("blog_id");
		if(id == null || id.equals("")){
			resmessage = "博客ID不能为空";
			message.put("message", resmessage);
		}else{
			List<Map<String,Object>> ls = blogService.getOneBlog(Integer.parseInt(id));
			if(ls.size() == 0){
				resmessage = "该博客不存在";
				message.put("message", resmessage);
			}else if(ls.size() == 1){				
				int readNum = StringUtil.changeObjectToInt(ls.get(0).get("read_number"));
				int b_id = StringUtil.changeObjectToInt(ls.get(0).get("id"));
				int i = blogService.updateReadNum(b_id, readNum + 1);
				
				resIsSuccess = true;
				message.put("message", ls);
				System.out.println(i);
			}else{
				resmessage = "数据库数据有误";
				message.put("message", resmessage);
			}
		}
		message.put("isSuccess", resIsSuccess);
		
        return SUCCESS;
	}
	
	/**
	 * 获得最热门的n条记录
	 * @return
	 */
	public String getHotestBlogs() {
		try {
			List<Map<String, Object>> ls = this.blogService.getHotestBlogs(5);
			message.put("isSuccess", true);
			message.put("message", ls);
		} catch (Exception e) {
			e.printStackTrace();
			message.put("isSuccess", false);
			message.put("message", "获取数据异常");
		}
		
		return SUCCESS;
	}
	
	/**
	 * 获得最新的n条记录
	 * @return
	 */
	public String getNewestBlogs() {
		try {
			List<Map<String, Object>> ls = this.blogService.getNewestBlogs(5);
			message.put("isSuccess", true);
			message.put("message", ls);
		} catch (Exception e) {
			e.printStackTrace();
			message.put("isSuccess", false);
			message.put("message", "获取数据异常");
		}
		
		return SUCCESS;
	}
	
	/**
	 * 获得推荐的n条博客
	 * @return
	 */
	public String getRecommendBlogs(){
		try {
			List<Map<String, Object>> ls = this.blogService.getHotestBlogs(5);
			message.put("isSuccess", true);
			message.put("message", ls);
		} catch (Exception e) {
			e.printStackTrace();
			message.put("isSuccess", false);
			message.put("message", "获取数据异常");
		}
		return SUCCESS;
	}
	
	/**
	 * 获取轮播图片信息
	 * @return
	 */
	public String getCarouselImgs(){
		try{
			JSONObject jo = JSONObject.fromObject(this.params);  
			if(jo.isEmpty()) return SUCCESS;
			int num = JsonUtil.getIntValue(jo, "num"); //获取图片的数量
			//执行的方式，现在支持：simple(取最新)，hostest(取最热门的)
			String method = JsonUtil.getStringValue(jo, "method");
			
			List<Map<String,Object>> r = new ArrayList<Map<String,Object>>();
			String sql = "";
			System.out.println("执行的方式是："+method +",获取的数量:"+num);
			//普通获取，取最新的图片信息，按照create_time倒序排列
			if(method.equalsIgnoreCase("simple")){
				sql = "select id,img_url,title from "+DataTableType.博客.value+" where status = " + ConstantsUtil.STATUS_NORMAL + " and img_url != '' order by create_time desc,id desc limit 0,?";
				r = blogService.executeSQL(sql, num);
				
			//按照热度，取最热门的图片信息，按照id倒序排序
			}else if(method.equalsIgnoreCase("hostest")){
				sql = "select id,img_url,title from "+DataTableType.博客.value+" where status = " + ConstantsUtil.STATUS_NORMAL + "  and img_url != '' and NOW() < DATE_ADD(create_time,INTERVAL 7 DAY) order by (comment_number*0.45 + transmit_number*0.25 + share_number*0.2 + zan_number*0.1 + read_number*0.1) desc,id desc limit 0,?";
				r = blogService.executeSQL(sql, num);	
			}else{
				message.put("isSuccess", resIsSuccess);
				message.put("message", "目前暂不支持的操作方法");
				return SUCCESS;
			}
			 
			resIsSuccess = true;
			message.put("isSuccess", resIsSuccess);
			message.put("message", r);
		}catch(Exception e){
			e.printStackTrace();
		}
		
		return SUCCESS;
	}
	
	/**
	 * 添加标签
	 * @return
	 */
	public String addTag() {
		message.put("isSuccess", resIsSuccess);
		try {
			JSONObject jo = HttpUtils.getJsonObjectFromInputStream(params, request);  
			if(jo == null || jo.isEmpty()) {	
				message.put("message", EnumUtil.getResponseValue(EnumUtil.ResponseCode.缺少请求参数.value));
				message.put("responseCode", EnumUtil.ResponseCode.缺少请求参数.value);
				return SUCCESS;
			}
			message.putAll(blogService.addTag(jo, user, request));
			return SUCCESS;	
		} catch (Exception e) {
			//resmessage = "抱歉，添加好友执行出现异常！请核实提交的信息后重试或者联系管理员";
			e.printStackTrace();
		}
		message.put("message", EnumUtil.getResponseValue(EnumUtil.ResponseCode.服务器处理异常.value));
		message.put("responseCode", EnumUtil.ResponseCode.服务器处理异常.value);
        return SUCCESS;
	}
	
	public String managerBlog() {
		return SUCCESS;
	}
	
	@Override
	public void setServletResponse(HttpServletResponse response) {
		this.response = response;
		///this.response.setCharacterEncoding("utf-8");  servlet报错，暂时取消
	}

	@Override
	public void setServletRequest(HttpServletRequest request) {
		this.request = request;
		
	}
	@Override
	public void setSession(Map<String, Object> session) {
		this.session = session;
	}
}
