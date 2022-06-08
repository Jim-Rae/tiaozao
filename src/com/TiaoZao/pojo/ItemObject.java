package com.TiaoZao.pojo;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * 
* 		@ClassName: ItemObject 
* 		@Description: 表结构字段和对象属性一样
* 		
* 		
 */
public class ItemObject {
	private String nickname;
	private String picUrl;
	private String time;
	private String goodsTitle;
	private String intro;
	private String category;
	private String price;
	private ArrayList<String> goodsPic;
	private ArrayList<Object> notes;
	private String isSelled;

	
	public boolean getIsSelled() {
		if(isSelled.equals("true"))
			return true;
		else
			return false;
	}
	public void setIsSelled(String isSelled) {
		this.isSelled = isSelled;
	}
	public String getNickname() {
		return nickname;
	}
	public void setNickname(String nickname) {
		this.nickname = nickname;
	}
	public String getPicUrl() {
		return picUrl;
	}
	public void setPicUrl(String picUrl) {
		this.picUrl = picUrl;
	}
	public String getTime() {
		return time;
	}
	public void setTime(String time) {
		this.time = time;
	}
	public String getGoodsTitle() {
		return goodsTitle;
	}
	public void setGoodsTitle(String goodsTitle) {
		this.goodsTitle = goodsTitle;
	}
	public String getIntro() {
		return intro;
	}
	public void setIntro(String intro) {
		this.intro = intro;
	}
	public String getCategory() {
		return category;
	}
	public void setCategory(String category) {
		this.category = category;
	}
	public String getPrice() {
		return price;
	}
	public void setPrice(String price) {
		this.price = price;
	}
	public ArrayList<String> getGoodsPic() {
		return goodsPic;
	}
	public void setGoodsPic(ArrayList<String> goodsPic) {
		this.goodsPic = goodsPic;
	}
	public ArrayList<Object> getNotes() {
		return notes;
	}
	public void setNotes(ArrayList<Object> notes) {
		this.notes = notes;
	}
	public ItemObject(String nickname, String picUrl, String time, String goodsTitle, String intro, String category,
			String price, ArrayList<String> goodsPic, ArrayList<Object> notes) {
		super();
		this.nickname = nickname;
		this.picUrl = picUrl;
		this.time = time;
		this.goodsTitle = goodsTitle;
		this.intro = intro;
		this.category = category;
		this.price = price;
		this.goodsPic = goodsPic;
		this.notes = notes;
	}
	public ItemObject(String nickname, String picUrl, String time, String goodsTitle, String intro, String category,
			String price, ArrayList<String> goodsPic, ArrayList<Object> notes, String isSelled) {
		super();
		this.nickname = nickname;
		this.picUrl = picUrl;
		this.time = time;
		this.goodsTitle = goodsTitle;
		this.intro = intro;
		this.category = category;
		this.price = price;
		this.goodsPic = goodsPic;
		this.notes = notes;
		this.isSelled = isSelled;
	}

	
	
}
