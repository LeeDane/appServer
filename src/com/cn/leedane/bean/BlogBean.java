package com.cn.leedane.bean;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import org.apache.solr.client.solrj.beans.Field;
import org.hibernate.annotations.Type;

/**
 * Blog实体类
 * @author LeeDane
 * 2015年7月17日 下午6:25:15
 * Version 1.0
 */

@Entity
@Table(name="T_BLOG")
//@Cache(usage = CacheConcurrencyStrategy.READ_WRITE, region="javaClassName")  
public class BlogBean extends RecordTimeBean{
	
	private static final long serialVersionUID = 1L;

	//博客的状态,-1：草稿，1：正常，0:禁用，2、删除
	
	
	/**
	 * 非必须，uuid,唯一(如在图像的时候关联filepath表)
	 */
	private String uuid;
	
	/**
	 * 博客的标题
	 */
	private String title; 
	
	/**
	 * 博客内容
	 */
	private String content;
	
	/**
	 * 博客摘要(建议一般不要超过50个字)
	 */
	private String digest;
	
	/**
	 * 标签(多个用逗号隔开)
	 */
	private String tag;   
	
	/**
	 * 来自(指的是来自发表的方式，如：Android客户端，iPhone客户端等)
	 */
	private String froms;
	
	/**
	 * 是否有图片
	 */
	private boolean hasImg;
	
	/**
	 * 图片的地址
	 */
	private String imgUrl;
	
	/**
	 * 100x100中图的地址
	 */
	private String imgUrl_100X100;
	
	/**
	 * 60x60中图的地址
	 */
	private String imgUrl_60X60;
	
	/**
	 * 30x30小图的地址
	 */
	private String imgUrl_30X30;
	
	/**
	 * 原文的链接
	 */
	private String originLink;
	
	/**
	 * 来源（指的是文章的来源，其他网站的话写的是其他网站的信息，原创的话直接写原创）
	 */
	private String source; 
	
	 /**
     * 是否可以评论(默认可以评论)
     */
    private boolean canComment;
    
    /**
     * 是否可以转发(默认可以转发)
     */
    private boolean canTransmit;
	
	/**
	 * 该条博客是否被索引了
	 */
	private boolean isIndex;
	
	/**
	 * 该条博客是否被solr索引了(冗余字段)
	 */
	private boolean isSolrIndex;
		

	/**
	 * 是否被阅读
	 */
	private boolean isRead; 
	
	/**
	 * 阅读次数
	 */
	private int readNumber; 
	
	/**
	 * 统计赞的数量
	 */
	private int zanNumber;   
	
	/**
	 * 统计评论的数量
	 */
	private int commentNumber; 
	
	/**
	 * 统计转发的数量
	 */
	private int transmitNumber ;
	
	/**
	 * 统计分享的数量
	 */
	private int shareNumber;
	
	/**
	 * 是否立即发布
	 */
	private boolean isPublishNow;
	
	/**
	 * 扩展字段1
	 */
	private String str1;
	
	/**
	 * 扩展字段2
	 */
	private String str2;
	
	public String getUuid() {
		return uuid;
	}
	public void setUuid(String uuid) {
		this.uuid = uuid;
	}
	public String getTitle() {
		return title;
	}
	@Field
	public void setTitle(String title) {
		this.title = title;
	}
	
	@Type(type="text")
	@Column(name="content",nullable=false)
	public String getContent() {
		return content;
	}
	
	@Field
	public void setContent(String content) {
		this.content = content;
	}
	
	public String getDigest() {
		return digest;
	}
	
	@Field
	public void setDigest(String digest) {
		this.digest = digest;
	}
	public String getTag() {
		return tag;
	}
	
	@Field
	public void setTag(String tag) {
		this.tag = tag;
	}
	
	public String getStr1() {
		return str1;
	}
	public void setStr1(String str1) {
		this.str1 = str1;
	}
	public String getStr2() {
		return str2;
	}
	public void setStr2(String str2) {
		this.str2 = str2;
	}
	
	@Column(length=21,columnDefinition="INT default 0",name="read_number")
	public int getReadNumber() {
		return readNumber;
	}
	public void setReadNumber(int readNumber) {
		this.readNumber = readNumber;
	}
	
	@Column(columnDefinition="INT default 0",name="zan_number")  //设置默认值是0
	public int getZanNumber() {
		return zanNumber;
	}
	public void setZanNumber(int zanNumber) {
		this.zanNumber = zanNumber;
	}
	
	@Column(columnDefinition="INT default 0", name="comment_number")
	public int getCommentNumber() {
		return commentNumber;
	}
	public void setCommentNumber(int commentNumber) {
		this.commentNumber = commentNumber;
	}
	
	@Column(columnDefinition="INT default 0",name="transmit_number")
	public int getTransmitNumber() {
		return transmitNumber;
	}
	public void setTransmitNumber(int transmitNumber) {
		this.transmitNumber = transmitNumber;
	}
	
	@Column(columnDefinition="INT default 0",name="share_number")
	public int getShareNumber() {
		return shareNumber;
	}
	public void setShareNumber(int shareNumber) {
		this.shareNumber = shareNumber;
	}
	
	@Column(name="is_read", columnDefinition="bit(1) default 0")
	public boolean isRead() {
		return isRead;
	}
	public void setRead(boolean isRead) {
		this.isRead = isRead;
	}
	
	@Column(name="has_img", columnDefinition="bit(1) default 0")
	public boolean isHasImg() {
		return hasImg;
	}
	public void setHasImg(boolean hasImg) {
		this.hasImg = hasImg;
	}
	
	@Type(type="text")
	@Column(name="img_url")
	public String getImgUrl() {
		return imgUrl;
	}
	public void setImgUrl(String imgUrl) {
		this.imgUrl = imgUrl;
	}
	
	@Type(type="text")
	@Column(name="img_url_100X100")
	public String getImgUrl_100X100() {
		return imgUrl_100X100;
	}
	public void setImgUrl_100X100(String imgUrl_100X100) {
		this.imgUrl_100X100 = imgUrl_100X100;
	}
	
	@Type(type="text")
	@Column(name="img_url_60X60")
	public String getImgUrl_60X60() {
		return imgUrl_60X60;
	}
	public void setImgUrl_60X60(String imgUrl_60X60) {
		this.imgUrl_60X60 = imgUrl_60X60;
	}
	
	@Type(type="text")
	@Column(name="img_url_30X30")
	public String getImgUrl_30X30() {
		return imgUrl_30X30;
	}
	public void setImgUrl_30X30(String imgUrl_30X30) {
		this.imgUrl_30X30 = imgUrl_30X30;
	}
	
	@Type(type="text")
	@Column(name="origin_link", length=800)
	public String getOriginLink() {
		return originLink;
	}
	public void setOriginLink(String originLink) {
		this.originLink = originLink;
	}
	public String getSource() {
		return source;
	}
	public void setSource(String source) {
		this.source = source;
	}
	public String getFroms() {
		return froms;
	}
	public void setFroms(String froms) {
		this.froms = froms;
	}
	
	@Column(name="is_publish_now", columnDefinition="bit(1) default 0")
	public boolean isPublishNow() {
		return isPublishNow;
	}
	public void setPublishNow(boolean isPublishNow) {
		this.isPublishNow = isPublishNow;
	}
	
	@Column(name="is_index", columnDefinition="bit(1) default 0")
	public boolean isIndex() {
		return isIndex;
	}
	public void setIndex(boolean isIndex) {
		this.isIndex = isIndex;
	}
	
	@Column(name="is_solr_index", columnDefinition="bit(1) default 0")
	public boolean isSolrIndex() {
		return isSolrIndex;
	}
	public void setSolrIndex(boolean isSolrIndex) {
		this.isSolrIndex = isSolrIndex;
	}	
	
	@Column(name="can_comment", nullable=false, columnDefinition="bit(1) default 1")
	public boolean isCanComment() {
		return canComment;
	}
	public void setCanComment(boolean canComment) {
		this.canComment = canComment;
	}
	
	@Column(name="can_transmit", nullable=false, columnDefinition="bit(1) default 1")
	public boolean isCanTransmit() {
		return canTransmit;
	}
	public void setCanTransmit(boolean canTransmit) {
		this.canTransmit = canTransmit;
	}
}
