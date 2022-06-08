package com.TiaoZao.pojo;

import java.util.ArrayList;

public class ItemBriefWithOwner {
	private int id;
	private String nickname;
	private String picUrl;
	private String time;
	private String goodsTitle;
	private String category;
	private String price;
	private ArrayList<String> goodsPic;
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
	public String getId() {
		return Integer.toString(id);
	}
	public void setId(int id) {
		this.id = id;
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
	public ItemBriefWithOwner(int id, String nickname, String picUrl, String time, String goodsTitle, String category,
			String price, ArrayList<String> goodsPic) {
		super();
		this.id = id;
		this.nickname = nickname;
		this.picUrl = picUrl;
		this.time = time;
		this.goodsTitle = goodsTitle;
		this.category = category;
		this.price = price;
		this.goodsPic = goodsPic;
	}
	public ItemBriefWithOwner(int id, String nickname, String picUrl, String time, String goodsTitle, String category,
			String price, ArrayList<String> goodsPic, String isSelled) {
		super();
		this.id = id;
		this.nickname = nickname;
		this.picUrl = picUrl;
		this.time = time;
		this.goodsTitle = goodsTitle;
		this.category = category;
		this.price = price;
		this.goodsPic = goodsPic;
		this.isSelled = isSelled;
	}


	
	
}
