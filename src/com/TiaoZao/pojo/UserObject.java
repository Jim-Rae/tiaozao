package com.TiaoZao.pojo;

import java.util.ArrayList;

/**
 * 
* 		@ClassName: ItemObject 
* 		@Description: 表结构字段和对象属性一样
* 		
* 		
 */
public class UserObject {
	private int id;
	private String usrname;
	private String pass;
	private String nickname;
	private String sex;
	private String birthday;
	private String school;
	private ArrayList<String> address;
	private String image;
	private String introduce;
	private String ownedItem;
	private String collectedItem;
	private String shoppingCart;
	private String purchasedItem;
	
	public String getId() {
		return Integer.toString(id);
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getUsrname() {
		return usrname;
	}
	public void setUsrname(String usrname) {
		this.usrname = usrname;
	}
	public String getPass() {
		return pass;
	}
	public void setPass(String pass) {
		this.pass = pass;
	}
	public String getNickname() {
		return nickname;
	}
	public void setNickname(String nickname) {
		this.nickname = nickname;
	}
	public String getSex() {
		return sex;
	}
	public void setSex(String sex) {
		this.sex = sex;
	}
	public String getBirthday() {
		return birthday;
	}
	public void setBirthday(String birthday) {
		this.birthday = birthday;
	}
	public String getSchool() {
		return school;
	}
	public void setSchool(String school) {
		this.school = school;
	}

	public ArrayList<String> getAddress() {
		return address;
	}
	public void setAddress(ArrayList<String> address) {
		this.address = address;
	}
	public String getImage() {
		return image;
	}
	public void setImage(String image) {
		this.image = image;
	}
	public String getIntroduce() {
		return introduce;
	}
	public void setIntroduce(String introduce) {
		this.introduce = introduce;
	}
	public String getOwnedItem() {
		return ownedItem;
	}
	public void setOwnedItem(String ownedItem) {
		this.ownedItem = ownedItem;
	}
	public String getCollectedItem() {
		return collectedItem;
	}
	public void setCollectedItem(String collectedItem) {
		this.collectedItem = collectedItem;
	}
	public String getShoppingCart() {
		return shoppingCart;
	}
	public void setShoppingCart(String shoppingCart) {
		this.shoppingCart = shoppingCart;
	}
	public String getPurchasedItem() {
		return purchasedItem;
	}
	public void setPurchasedItem(String purchasedItem) {
		this.purchasedItem = purchasedItem;
	}
	public UserObject(String nickname, String sex, String birthday, String school, ArrayList<String> address,
			String image, String introduce) {
		super();
		this.nickname = nickname;
		this.sex = sex;
		this.birthday = birthday;
		this.school = school;
		this.address = address;
		this.image = image;
		this.introduce = introduce;
	}

	public UserObject() {
		
	}
	
	
}
