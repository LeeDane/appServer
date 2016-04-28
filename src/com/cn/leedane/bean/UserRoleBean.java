package com.cn.leedane.bean;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
/**
 * 用户角色的实体类(需要单独去维持)
 * @author LeeDane
 * 2015年7月22日 下午6:50:36
 * Version 1.0
 */
@Entity
@Table(name="T_USER_ROLE")
public class UserRoleBean extends IDBean{
	
	private static final long serialVersionUID = 1L;

	private UserBean user;
	
	private RolesBean role;

	@ManyToOne(fetch=FetchType.LAZY, cascade=CascadeType.ALL)
	@JoinColumn(name="user_id" , referencedColumnName="id")
	public UserBean getUser() {
		return user;
	}

	public void setUser(UserBean user) {
		this.user = user;
	}

	@ManyToOne(fetch=FetchType.LAZY, cascade=CascadeType.ALL)
	@JoinColumn(name="role_id" , referencedColumnName="id")
	public RolesBean getRole() {
		return role;
	}

	public void setRole(RolesBean role) {
		this.role = role;
	}	

}
