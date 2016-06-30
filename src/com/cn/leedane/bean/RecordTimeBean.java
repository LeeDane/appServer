package com.cn.leedane.bean;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.MappedSuperclass;

import org.apache.struts2.json.annotations.JSON;

/**
 * 记录时间的基类
 * @author LeeDane
 * 2015年4月3日 上午10:06:22
 * Version 1.0
 */
@MappedSuperclass
public abstract class RecordTimeBean extends StatusBean{

	private static final long serialVersionUID = 1L;
	
	/**
	 * 创建时间
	 */
	private Date createTime;
	
	/**
	 * 创建作者(人)
	 */
	private UserBean createUser;
	
	/**
	 * 最后修改时间
	 */
	private Date modifyTime; 
	
	/**
	 * 最后修改者(人)
	 */
	private UserBean modifyUser; 
	
	@Column(name="create_time")
	@JSON(format = "yyyy-MM-dd HH:mm:ss")
	public Date getCreateTime() {
		return createTime;
	}
	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}
	
	@ManyToOne(targetEntity = UserBean.class)
	@JoinColumn(name="create_user_id", referencedColumnName="id")//外键为create_user_id，与user中的id关联
	public UserBean getCreateUser() {
		return createUser;
	}
	public void setCreateUser(UserBean createUser) {
		this.createUser = createUser;
	}
	@Column(name="modify_time")
	@JSON(format = "yyyy-MM-dd HH:mm:ss")
	public Date getModifyTime() {
		return modifyTime;
	}
	public void setModifyTime(Date modifyTime) {
		this.modifyTime = modifyTime;
	}
	
	@ManyToOne(targetEntity = UserBean.class)
	@JoinColumn(name="modify_user_id", referencedColumnName="id")//外键为create_user_id，与user中的id关联
	public UserBean getModifyUser() {
		return modifyUser;
	}
	public void setModifyUser(UserBean modifyUser) {
		this.modifyUser = modifyUser;
	}
	
}
