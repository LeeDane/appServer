package com.cn.leedane.task.spring.scheduling;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Callable;
import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.annotation.Resource;

import org.springframework.stereotype.Component;

import com.cn.leedane.Utils.ConstantsUtil;
import com.cn.leedane.Utils.DateUtil;
import com.cn.leedane.Utils.EnumUtil;
import com.cn.leedane.Utils.JsoupUtil;
import com.cn.leedane.Utils.StringUtil;
import com.cn.leedane.Utils.EnumUtil.DataTableType;
import com.cn.leedane.bean.BlogBean;
import com.cn.leedane.bean.CrawlBean;
import com.cn.leedane.bean.UserBean;
import com.cn.leedane.crawl.SanwenNet;
import com.cn.leedane.service.BlogService;
import com.cn.leedane.service.CrawlService;
import com.cn.leedane.service.UserService;
import com.cn.leedane.task.spring.scheduling.DeleteTemporaryFiles.SingleDeleteTask;

/**
 * 散文网爬虫Bean
 * @author LeeDane
 * 2015年7月1日 下午5:05:26
 * Version 1.0
 */
@Component("sanwenNetBean")
public class SanwenNetBean {

	@Resource
	private CrawlService<CrawlBean> crawlService;
	
	@Resource
	private BlogService<BlogBean> blogService;
	
	@Resource
	private UserService<UserBean> userService;

	/**
	 * 执行爬取方法
	 */
	public void crawl() throws Exception{
		//System.out.println(DateUtil.getSystemCurrentTime("yyyy-MM-dd HH:mm:ss") + ":Sanwen:crawl()");
		try {
			SanwenNet sanwenNet = new SanwenNet();
			sanwenNet.setUrl("http://www.sanwen.net/");
			sanwenNet.execute();
			List<String> homeUrls = sanwenNet.getHomeListsHref();
			for(String homeUrl : homeUrls){
				CrawlBean crawlBean = new CrawlBean();
				crawlBean.setUrl(homeUrl.trim());
				crawlBean.setSource("散文网");
				crawlBean.setCreateTime(new Date());
				System.out.println(crawlService.save(crawlBean));
			}
		} catch (IOException e) {
			e.printStackTrace();
			System.out.println("抓取散文网信息出现异常：Crawl()");
		}
	}
	
	/**
	 * 执行处理方法
	 */
	public void deal() throws Exception{
		System.out.println(DateUtil.getSystemCurrentTime("yyyy-MM-dd HH:mm:ss") + ":Sanwen:deal()");
		
		//获得用户
		UserBean user = userService.loadById(1);
		List<CrawlBean> beans = crawlService.findAllNotCrawl(0, EnumUtil.WebCrawlType.散文网.value);
		if(beans != null && beans.size()> 0){
			List<Future<Boolean>> futures = new ArrayList<Future<Boolean>>();
			ExecutorService threadpool = Executors.newFixedThreadPool(beans.size() >2 ? 3: beans.size());
			SingleDealTask dealTask;
			for(CrawlBean bean: beans){
				dealTask = new SingleDealTask(bean, user);
				futures.add(threadpool.submit(dealTask));
			}
			
			threadpool.shutdown();
			List<String> errors = new ArrayList<String>();
			for(int i = 0; i < futures.size(); i++){
				if(!futures.get(i).get()){
					errors.add(beans.get(i).getUrl());
				}
			}
			
			for(String url: errors){
				System.out.println("处理散文网信息出现异常的链接是："+url);
			}
		}
		System.out.println("处理散文网信息结束......");
	}
	
	/**
	 * 当个处理任务
	 * @author LeeDane
	 * 2016年3月18日 下午2:47:25
	 * Version 1.0
	 */
	class SingleDealTask implements Callable<Boolean>{
		private CrawlBean mCrawlBean;
		private UserBean mUser;
		public SingleDealTask(CrawlBean crawlBean, UserBean user){
			this.mCrawlBean = crawlBean;
			this.mUser = user;
		}

		@Override
		public Boolean call() throws Exception {
			String url = mCrawlBean.getUrl();
			if(!StringUtil.isNull(url)){
				Pattern p=Pattern.compile("http://www.sanwen.net/subject/[0-9]+");
				Matcher m=p.matcher(url);
				if(m.find()){
					SanwenNet sanwenNet = new SanwenNet(url,"","");
					sanwenNet.execute();
					
					String content = sanwenNet.getContent("#article .content",".adcontent");
					String title = sanwenNet.getTitle().trim();
					
					if(!StringUtil.isNull(content) && !StringUtil.isNull(title)){
						//判断是否已经存在相同的信息
						List<Map<String, Object>> existsBlogs = blogService.executeSQL("select id from "+DataTableType.博客.value+" where origin_link = ? ", url);
						if(existsBlogs!= null && existsBlogs.size() > 0){
							return true;
						}
						
						BlogBean blog = new BlogBean();
						blog.setTitle(title);
						blog.setContent(content);
						blog.setCreateUser(mUser);
						blog.setCreateTime(new Date());
						blog.setSource(EnumUtil.WebCrawlType.散文网.value);
						blog.setFroms("爬虫抓取");
						blog.setStatus(ConstantsUtil.STATUS_NORMAL);
						blog.setDigest(JsoupUtil.getInstance().getDigest(content, 0, 120));
						
						//获取主图
						String mainImgUrl = sanwenNet.getMainImg("#article .content img", 0);
						if( mainImgUrl != null && !mainImgUrl.equals("")){
							blog.setHasImg(true);
							
							//对base64位的src属性处理
							if(!StringUtil.isLink(mainImgUrl)){
								mainImgUrl = JsoupUtil.getInstance().base64ToLink(mainImgUrl, mUser.getAccount());
							}
							
							blog.setImgUrl(mainImgUrl);
						}
						blog.setOriginLink(url);
						//把抓取到的数据添加进博客表中
						Map<String,Object> result = blogService.addBlog(blog);
						
						//保存成功之后
						if(StringUtil.changeObjectToBoolean(result.get("isSuccess"))){
							mCrawlBean.setCrawl(true);
							//将抓取标记为已经抓取
							crawlService.update(mCrawlBean);
						}
					}
					
				//不合符抓取条件的记录也标记为已经抓取
				}else{
					mCrawlBean.setCrawl(true);
					//将抓取标记为已经抓取
					crawlService.update(mCrawlBean);
				}
			}
			return null;
		}
		
	}
}
