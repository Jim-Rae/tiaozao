package com.TiaoZao.pojo;

import java.util.ArrayList;
import java.util.List;

public class ItemBrief {
	private int id;
	private String time;
	private String goodsTitle;
	private String price;
	private ArrayList<String> goodsPic;
	
	public String getId() {
		return Integer.toString(id);
	}
	public void setId(int id) {
		this.id = id;
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
	public String getPrice() {
		return price;
	}
	public void setPrice(String price) {
		this.price = price;
	}
	public List<String> getGoodsPic() {
		return goodsPic;
	}
	public void setGoodsPic(ArrayList<String> goodsPic) {
		this.goodsPic = goodsPic;
	}
	public ItemBrief(int id, String time, String goodsTitle, String price, ArrayList<String> goodsPic) {
		super();
		this.id = id;
		this.time = time;
		this.goodsTitle = goodsTitle;
		this.price = price;
		this.goodsPic = goodsPic;
	}
	
}
