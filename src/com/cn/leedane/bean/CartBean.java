package com.cn.leedane.bean;

import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.OneToMany;
import javax.persistence.Table;
/**
 * 购物车实体类
 * @author LeeDane
 * 2015年4月3日 上午10:18:00
 * Version 1.0
 */
@Entity
@Table(name="T_CART")
public class CartBean extends RecordTimeBean{

	private static final long serialVersionUID = 1L;
	//购物车状态，0：过期 1：正常
	

	/**
	 * 购物车的类型，0：永久 1：临时
	 */
	private int type;
	
	/**
	 * 购物车上的商品
	 * 双向一对多的关系，这样在获取购物车的时候就能获取所有商品明细的信息
	 */
	private List<CartDetailsBean> details; 
	
	@OneToMany(mappedBy="cart",cascade=CascadeType.ALL)
	//@OrderBy("price desc")
	public List<CartDetailsBean> getDetails() {
		return details;
	}

	public void setDetails(List<CartDetailsBean> details) {
		this.details = details;
	}
	
	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}

}
