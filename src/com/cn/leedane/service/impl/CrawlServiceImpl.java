package com.cn.leedane.service.impl;
import java.util.List;

import javax.annotation.Resource;

import org.apache.log4j.Logger;

import com.cn.leedane.Dao.CrawlDao;
import com.cn.leedane.bean.CrawlBean;
import com.cn.leedane.service.CrawlService;

/**
 * 爬虫的service实现类
 * @author LeeDane
 * 2015年7月17日 下午6:14:59
 * Version 1.0
 */
public class CrawlServiceImpl extends BaseServiceImpl<CrawlBean> implements CrawlService<CrawlBean> {
	Logger logger = Logger.getLogger(getClass());
	@Resource
	private CrawlDao<CrawlBean> crawlDao;
	
	public void setCrawlDao(CrawlDao<CrawlBean> crawlDao) {
		this.crawlDao = crawlDao;
	}
	
	public CrawlDao<CrawlBean> getCrawlDao() {
		return crawlDao;
	}
	
	@Override
	public boolean save(CrawlBean t){
		if(crawlDao.isExists(t.getUrl())){
			return false;
		}
		return super.save(t);
	}

	@Override
	public List<CrawlBean> findAllNotCrawl(int limit, String source) {
		return this.crawlDao.findAllNotCrawl(limit, source);
	}

	@Override
	public List<CrawlBean> findAllHotNotCrawl(int limit) {
		return this.crawlDao.findAllHotNotCrawl(limit);
	}

	@Override
	public boolean updateAllScore() {
		return this.crawlDao.updateAllScore();
	}
}
