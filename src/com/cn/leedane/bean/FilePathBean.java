package com.cn.leedane.bean;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import org.hibernate.annotations.Type;

/**
 * 文件路径实体类
 * @author LeeDane
 * 2015年11月22日 下午11:18:12
 * Version 1.0
 */

@Entity
@Table(name="T_FILE_PATH")
public class FilePathBean extends RecordTimeBean{
	
	private static final long serialVersionUID = 1L;
	
	//文件路径的状态,1：正常，0:禁用，2、删除
	
	/**
	 * 路径
	 */
	private String path;
	
	/**
	 * 附属表的uuid
	 */
	private String tableUuid;
	
	/**
	 * 表名称，跟tableId构成唯一
	 */
	private String tableName;
	
	/**
	 * 原图就必须，其他规格的图片可以为空，源base64字符串
	 */
	private String base64;
	
	/**
	 * 图片的宽度(非必须)
	 */
	private int width;
	
	/**
	 * 图片的高度(非必须)
	 */
	private int height;
	
	/**
	 * 文件的长度
	 */
	private long lenght;
	
	/**
	 * 必须，图像的规格,约定的值为：source, default, 30x30, 60x60...
	 */
	private String size;
	
	/**
	 * 必须，图像的排序，可以区分图像的位置
	 */
	private int order;
	
	/**
	 * 七牛服务器的存储路径
	 */
	private String qiniuPath;
	
	/**
	 * 是否上传七牛
	 */
	private int isUploadQiniu;
	
	
	/**
	 * 版本号（展示）
	 */
	private String version;
	
	/**
	 * 描述信息
	 */
	private String desc;

	@Column(length=255)
	public String getPath() {
		return path;
	}

	public void setPath(String path) {
		this.path = path;
	}

	@Column(name="table_uuid", length =120, nullable=true)
	public String getTableUuid() {
		return tableUuid;
	}

	public void setTableUuid(String tableUuid) {
		this.tableUuid = tableUuid;
	}
	
	@Column(name="table_name", length= 15, nullable=false)
	public String getTableName() {
		return tableName;
	}

	public void setTableName(String tableName) {
		this.tableName = tableName;
	}

	@Type(type="text")
	public String getBase64() {
		return base64;
	}

	public void setBase64(String base64) {
		this.base64 = base64;
	}

	@Column(name="pic_size", length =10, nullable=false)
	public String getSize() {
		return size;
	}

	public void setSize(String size) {
		this.size = size;
	}

	@Column(name="pic_order", length=2, nullable=false)
	public int getOrder() {
		return order;
	}

	public void setOrder(int order) {
		this.order = order;
	}

	@Column(name="qiniu_path", length=255)
	public String getQiniuPath() {
		return qiniuPath;
	}

	public void setQiniuPath(String qiniuPath) {
		this.qiniuPath = qiniuPath;
	}

	@Column(name="is_upload_qiniu", length=1)
	public int isUploadQiniu() {
		return isUploadQiniu;
	}

	public void setUploadQiniu(int isUploadQiniu) {
		this.isUploadQiniu = isUploadQiniu;
	}

	public int getWidth() {
		return width;
	}

	public void setWidth(int width) {
		this.width = width;
	}

	public int getHeight() {
		return height;
	}

	public void setHeight(int height) {
		this.height = height;
	}

	public long getLenght() {
		return lenght;
	}

	public void setLenght(long lenght) {
		this.lenght = lenght;
	}

	@Column(name="file_desc", length=1000, nullable=true)
	public String getDesc() {
		return desc;
	}

	public void setDesc(String desc) {
		this.desc = desc;
	}

	@Column(name="file_version", length=10, nullable=true)
	public String getVersion() {
		return version;
	}

	public void setVersion(String version) {
		this.version = version;
	}
	
	
	
}
