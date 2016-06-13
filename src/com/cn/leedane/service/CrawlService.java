package com.cn.leedane.service;

import java.io.Serializable;
import java.util.List;

import com.cn.leedane.bean.CrawlBean;

/**
 * 爬虫service的接口类
 * @author LeeDane
 * 2015年7月17日 下午6:18:14
 * Version 1.0
 */
public interface CrawlService <T extends Serializable> extends BaseService<CrawlBean>{
	
	/**
	 * 找出所有没有爬取的数据链接
	 * @param limit 现在的数量,等于0表示全部
	 * @param source 来源，空表示全部
	 * @return
	 */
	public List<CrawlBean> findAllNotCrawl(int limit, String source);
	
	/**
	 * 找出所有没有爬取的热门数据链接,根据score从大到小
	 * @param limit 现在的数量,等于0表示全部
	 * @return
	 */
	public List<CrawlBean> findAllHotNotCrawl(int limit);
	
	/**
	 * 更新所有的score
	 * @return
	 */
	public boolean updateAllScore();
}
