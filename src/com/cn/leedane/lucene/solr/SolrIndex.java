package com.cn.leedane.lucene.solr;

/**
 * solr索引查询的基本接口
 * @author LeeDane
 * 2016年2月18日 下午3:39:53
 * Version 1.0
 */
public interface SolrIndex {

	/**
	 * 建立索引查询
	 */
	public void query(int start, int rows);
}
