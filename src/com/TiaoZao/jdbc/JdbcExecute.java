package com.TiaoZao.jdbc;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;

import javax.print.DocFlavor.STRING;

import com.TiaoZao.pojo.ItemBrief;
import com.TiaoZao.pojo.ItemBriefWithOwner;
import com.TiaoZao.pojo.ItemObject;
import com.TiaoZao.pojo.UserBrief;
import com.TiaoZao.pojo.UserObject;
import com.TiaoZao.util.DBUtil;
import com.alibaba.fastjson.JSON;

public class JdbcExecute {
	

	//识别用户名是否存在
	public static boolean whetherUserExist(String usrname) throws SQLException, ClassNotFoundException  {

		Class.forName("com.mysql.jdbc.Driver");
		Connection con = DBUtil.getConnection();
		PreparedStatement ps = con.prepareStatement("SELECT * FROM userinfo where usrname=?");

		ps.setString(1,usrname);
		
		ResultSet re = ps.executeQuery();

		if(re.next()) {
			re.close();
			ps.close();
			con.close();
			return true;
		}
		else {
			re.close();
			ps.close();
			con.close();
			return false;
		}
	}
	//增加一个新用户
	public static void addNewUser(String name, String pass) throws SQLException, ClassNotFoundException  {

		Class.forName("com.mysql.jdbc.Driver");
		Connection con = DBUtil.getConnection();
		PreparedStatement ps = con.prepareStatement("INSERT INTO userinfo(usrname,pass,nickname,image,introduce) values (?,?,?,?,?)");

		ps.setString(1,name); 
		ps.setString(2,pass);
		ps.setString(3,"跳蚤用户"+name);
		ps.setString(4,"http://120.79.234.43/TiaoZao/Image?imageUrl=default.png");
		ps.setString(5,"这个用户很懒，暂未设置个人介绍。");
		ps.execute();
		ps.close();
		con.close();
	}	
	
	public static void addAccountServiceUser(String name, String pass) throws SQLException, ClassNotFoundException  {

		Class.forName("com.mysql.jdbc.Driver");
		Connection con = DBUtil.getConnection();
		PreparedStatement ps = con.prepareStatement("INSERT INTO userinfo(usrname,pass,nickname,image,introduce) values (?,?,?,?,?)");

		ps.setString(1,name); 
		ps.setString(2,pass);
		ps.setString(3,"平台用户"+name);
		ps.setString(4,"http://120.79.234.43/TiaoZao/Image?imageUrl=default.png");
		ps.setString(5,"这个用户很懒，暂未设置个人介绍。");
		ps.execute();
		ps.close();
		con.close();
	}	
	
	//判断账号密码是否匹配
	public static boolean whetherSucceedLogIn(String usrname,String pass) throws SQLException, ClassNotFoundException  {

		Class.forName("com.mysql.jdbc.Driver");
		Connection con = DBUtil.getConnection();
		PreparedStatement ps = con.prepareStatement("SELECT * FROM userinfo where usrname=? and pass=?");
		
		ps.setString(1,usrname);
		ps.setString(2,pass);
		
		ResultSet re = ps.executeQuery();
		
		if(re.next()) {
			re.close();
			ps.close();
			con.close();
			return true;
		}
		else {
			re.close();
			ps.close();
			con.close();
			return false;
		}
	}
	//返回某个用户拥有的商品的信息
	public static HashMap<String , Object> userOwnedItem (String usrname) throws ClassNotFoundException, SQLException{
		
		Class.forName("com.mysql.jdbc.Driver");
		Connection con = DBUtil.getConnection();
		PreparedStatement ps = con.prepareStatement("SELECT * FROM iteminfo where ownerName=? order by id desc");
		
		//这里开始编写内容
		HashMap<String , Object> a = new HashMap<String , Object>();
		ArrayList<ItemBrief> list = new ArrayList<ItemBrief>();
		
		ps.setString(1, usrname);
		
		ResultSet re = ps.executeQuery();
		while (re.next()) {
			//根据需要返回的goods内容创建一个类，类的成员变量对应返回类型
			int id = re.getInt(1);
			String goodsTitle = re.getString(2);
			String picTmp = re.getString(4);
			String price = re.getString(5);
			String time = re.getString(11);	
			ArrayList<String> goodsPic = new ArrayList<String>();
			if(picTmp.indexOf("-")>0) {
				for(;;) {
					int i = picTmp.indexOf("-");
					if(i>0) {
						goodsPic.add("http://120.79.234.43/TiaoZao/Image?imageUrl="+picTmp.substring(0, i));
						picTmp = picTmp.substring(i+1);
					}
					else {
						goodsPic.add("http://120.79.234.43/TiaoZao/Image?imageUrl="+picTmp);
						break;
					}
				}
			}
			else {
				goodsPic.add("http://120.79.234.43/TiaoZao/Image?imageUrl="+picTmp);
			}
			ItemBrief item = new ItemBrief(id, time, goodsTitle, price, goodsPic);
			list.add(item);
		}
		a.put("goods", list);
		re.close();
		ps.close();
		con.close(); 
		return a;
	}
	//发布新商品
	public static String addNewItem (String itemname ,String introduce, String price,String ownerName ,String category, String image) throws ClassNotFoundException, SQLException {
		
		Class.forName("com.mysql.jdbc.Driver");
		Connection con = DBUtil.getConnection();
		PreparedStatement ps = con.prepareStatement("insert into iteminfo( itemname ,introduce ,price ,ownerName ,category ,uploadTime ,image) value(?,?,?,?,?,?,?)");
		String returnItemId = null;
		ps.setString(1,itemname);
		ps.setString(2,introduce);
		ps.setString(3,price);
		ps.setString(4,ownerName);
		ps.setString(5,category);
		ps.setString(7,image.substring(0, image.length()-1));
		Calendar now = Calendar.getInstance();
		int month = now.get(Calendar.MONTH) + 1;
		int day = now.get(Calendar.DAY_OF_MONTH);
		int hour = now.get(Calendar.HOUR_OF_DAY);
		int min = now.get(Calendar.MINUTE);
		String time =null; 
		if(min<10) {
			time = month+"月"+day+"日 "+hour+":0"+min;
			ps.setString(6,time);
		}
		else {
			time = month+"月"+day+"日 "+hour+":"+min;
			ps.setString(6,time);
		}
		ps.execute();
		
		//在用户表也要修改ownedItem
		ps = con.prepareStatement("select id from iteminfo where itemname=? and introduce=? and uploadTime=?");
		ps.setString(1,itemname);
		ps.setString(2,introduce);
		ps.setString(3,time);
		ResultSet re = ps.executeQuery();
		String itemId = null;
		while (re.next()) {
			itemId = re.getString(1);
		}
		returnItemId = itemId;
		ps.close();
		
		ps = con.prepareStatement("select ownedItem from userinfo where usrname=?");
		ps.setString(1,ownerName);
		re = ps.executeQuery();
		String ownedItem = null;
		while (re.next()) {
			ownedItem = re.getString(1);
		}
		ps.close();
		
		ps = con.prepareStatement("update userinfo set ownedItem=? where usrname=?");
		if(ownedItem == null) {
			ps.setString(1, itemId);
		}
		else {
			ps.setString(1,ownedItem+"-"+itemId);
		}
		ps.setString(2,ownerName);
		ps.execute();
		re.close();
		ps.close();
		con.close();
		return returnItemId;
	}
	//更改商品信息
	public static void updateItem (String itemname ,String introduce, String price,String id ,String category, String image) throws ClassNotFoundException, SQLException {
		
		Class.forName("com.mysql.jdbc.Driver");
		Connection con = DBUtil.getConnection();
//		PreparedStatement ps = con.prepareStatement("select image from iteminfo where id=?");
//		ps.setString(1,id);
//		ResultSet re = ps.executeQuery();
//		StringBuffer preImage = new StringBuffer();
//		while (re.next()) {
//			preImage = preImage.append(re.getString(1));
//			System.out.println(preImage);
//		}
		
		PreparedStatement ps = con.prepareStatement("update iteminfo set itemname=?,introduce=?,price=?,category=?,image=? ,uploadTime=?where id=?");
		ps.setString(1,itemname);
		ps.setString(2,introduce);
		ps.setString(3,price);
		ps.setString(4,category);
		ps.setString(5,image.substring(0, image.length()-1));
		Calendar now = Calendar.getInstance();
		int month = now.get(Calendar.MONTH) + 1;
		int day = now.get(Calendar.DAY_OF_MONTH);
		int hour = now.get(Calendar.HOUR_OF_DAY);
		int min = now.get(Calendar.MINUTE);
		if(min<10)
			ps.setString(6,month+"月"+day+"日 "+hour+":0"+min);
		else
			ps.setString(6,month+"月"+day+"日 "+hour+":"+min);
		ps.setString(7,id);
		ps.execute();
		ps.close();
		con.close();
	}
	//删除商品
	public static void deleteItem (String id) throws ClassNotFoundException, SQLException {
		
		Class.forName("com.mysql.jdbc.Driver");
		Connection con = DBUtil.getConnection();		
		PreparedStatement ps = con.prepareStatement("select ownerName from iteminfo where id=?");
		ps.setString(1,id);
		ResultSet re = ps.executeQuery();
		String ownerName = new String();
		while (re.next()) {
			ownerName = re.getString(1);
			System.out.println("ownerName="+ownerName);
		}
		ps.close();
		
		ps = con.prepareStatement("select ownedItem from userinfo where usrname = ?");
		ps.setString(1,ownerName);
		re = ps.executeQuery();
		String ownedItem = new String();
		while (re.next()) {
			ownedItem = re.getString(1);
			System.out.println("ownedItem="+ownedItem);
		}
		ownedItem = ownedItem.replace("-"+id, "");
		ownedItem = ownedItem.replace(id+"-", "");
		System.out.println("ownedItem处理后="+ownedItem);
		ps.close();
		
		ps = con.prepareStatement("update userinfo set ownedItem=? where usrname=?");
		ps.setString(1,ownedItem);
		ps.setString(2,ownerName);
		ps.execute();
		ps.close();
		
		ps = con.prepareStatement("delete from iteminfo where id = ?");
		ps.setString(1,id);
		ps.execute();
		ps.close();
		con.close();
	}
	//查看购物车
	public static HashMap<String , Object> userShoppingCart (String username) throws ClassNotFoundException, SQLException {
		Connection con = DBUtil.getConnection();
		PreparedStatement ps = con.prepareStatement("select shoppingCart from userinfo where usrname=?");
		ps.setString(1, username);
		ResultSet re = ps.executeQuery();
		StringBuffer setWord = new StringBuffer();
		HashMap<String , Object> a = new HashMap<String , Object>();
		ArrayList<Object> list = new ArrayList<Object>();
		if (re.next()) {
			String shoppingCart = re.getString(1);
			if(shoppingCart == null) {
				a.put("goods", list);
				ps.close();
				return a;
			}
			else {
				String[] shoppingCartList = shoppingCart.split("[-]");
				for(int i=0;i<shoppingCartList.length ;i++) {
					setWord.append("id="+shoppingCartList[i]+" or ");
				}
				System.out.println(shoppingCart);
			}
		}
		String setWordFinal = setWord.toString();
		try {
			setWordFinal = setWordFinal.substring(0, setWordFinal.length()-3);
		} catch (Exception e) {
			a.put("goods", list);
			ps.close();
			re.close();
			con.close();
			return a;
		}
		System.out.println("select * from iteminfo where "+setWordFinal);
		if(setWordFinal.equals("id= ;")) {
			a.put("goods", list);
			ps.close();
			re.close();
			con.close();
			return a;
		}
		ps = con.prepareStatement("select * from iteminfo where "+setWordFinal+" order by id desc;");
		re = ps.executeQuery();
		while (re.next()) {
			//根据需要返回的goods内容创建一个类，类的成员变量对应返回类型
			int id = re.getInt(1);
			String goodsTitle = re.getString(2);
			String tempPic = re.getString(4);
			String ownerName = re.getString(8);
			String nickname = getNickname(ownerName);
			String picUrl = getImage(ownerName);
			ArrayList<String> goodsPic = new ArrayList<String>();
			if(tempPic.indexOf("-")>0) {
				for(;;) {
					int i = tempPic.indexOf("-");
					if(i>0) {
						goodsPic.add("http://120.79.234.43/TiaoZao/Image?imageUrl="+tempPic.substring(0, i));
						tempPic = tempPic.substring(i+1);
					}
					else {
						goodsPic.add("http://120.79.234.43/TiaoZao/Image?imageUrl="+tempPic);
						break;
					}
				}
			}
			else {
				goodsPic.add("http://120.79.234.43/TiaoZao/Image?imageUrl="+tempPic);
			}			
			String price = re.getString(5);
			String isSelled = re.getString(9);
			String category = re.getString(10);
			String time = re.getString(11);
			UserBrief user = new UserBrief(id,nickname, picUrl, time, goodsTitle,category, price, goodsPic,isSelled);
			list.add(user);
		}
		a.put("goods", list);
		ps.close();
		re.close();
		con.close();
		return a;
	}
	//获取个人信息
	public static UserObject userInformation (String username) throws ClassNotFoundException, SQLException {
		Class.forName("com.mysql.jdbc.Driver");
		Connection con = DBUtil.getConnection();		
		PreparedStatement ps = con.prepareStatement("select * from userinfo where usrname=?");
		ps.setString(1, username);
		ResultSet re = ps.executeQuery();
		UserObject a = new UserObject();
		String nickname="",sex="",birthday="",school="",tempaddress="",image="",introduce="";
		ArrayList<String> address = new ArrayList<String>();
		while (re.next()) {
			if(re.getString(4) != null)
				nickname = re.getString(4);
			if(re.getString(5) != null)
				sex = re.getString(5);
			if(re.getString(6) != null)
				birthday = re.getString(6);
			if(re.getString(7) != null)
				school = re.getString(7);
			if(re.getString(8) != null) {
				tempaddress = re.getString(8);
				if(tempaddress != null && tempaddress.indexOf("-")>0) {
					for(;;) {
						int i = tempaddress.indexOf("-");
						if(i>0) {
							address.add(tempaddress.substring(0, i));
							tempaddress = tempaddress.substring(i+1);
						}
						else {
							address.add(tempaddress);
							break;
						}
					}
				}
				else {
					address.add(tempaddress);
				}
			}
			else
				address.add("");
			image = re.getString(9);
			if(image == null || image.equals(""))
				image = "http://120.79.234.43/TiaoZao/Image?imageUrl=default.png";
			introduce = re.getString(10);		
			
			a = new UserObject(nickname, sex, birthday, school, address, image, introduce);
		}
		ps.close();
		re.close();
		con.close();
		return a;
	}
	//完善个人信息
	public static void userInformationCompletion (String nickname,String sex,String birthday,String school,String address,String introduce,String usrname) throws SQLException, ClassNotFoundException {
		Class.forName("com.mysql.jdbc.Driver");
		Connection con = DBUtil.getConnection();		
		PreparedStatement ps = con.prepareStatement("update userinfo set nickname=?, sex=?, birthday=?, school=?, address=?, introduce=? where usrname=?");
		
		ps.setString(1,nickname);
		ps.setString(2,sex);
		ps.setString(3,birthday);
		ps.setString(4,school);
		ps.setString(5,address);
		ps.setString(6,introduce);
		ps.setString(7,usrname);

		ps.execute();
		ps.close();
		con.close();
	}
	//根据usrname返回nickname
	public static String getNickname(String usrname) throws SQLException, ClassNotFoundException {
		Class.forName("com.mysql.jdbc.Driver");
		Connection con = DBUtil.getConnection();		
		PreparedStatement ps = con.prepareStatement("select nickname from userinfo where usrname=?");
		ps.setString(1,usrname);
		ps.execute();
		ResultSet re = ps.executeQuery();
		String nickname = null;
		if(re.next()) {
			nickname = re.getString(1);
		}
		re.close();
		ps.close();
		con.close();
		return nickname;
	}
	//根据usrname返回image
	public static String getImage(String usrname) throws SQLException, ClassNotFoundException {
		Class.forName("com.mysql.jdbc.Driver");
		Connection con = DBUtil.getConnection();		
		PreparedStatement ps = con.prepareStatement("select image from userinfo where usrname=?");
		ps.setString(1,usrname);
		ps.execute();
		ResultSet re = ps.executeQuery();
		String image = null;
		if(re.next()) {
			image = re.getString(1);
		}
		ps.close();
		re.close();
		con.close();
		return image;
	}
	
	//查看商品详情
	public static ItemObject itemDetail (String itemId) throws ClassNotFoundException, SQLException {
		Class.forName("com.mysql.jdbc.Driver");
		Connection con = DBUtil.getConnection();		
		PreparedStatement ps = con.prepareStatement("select * from iteminfo where id=?");
		ps.setString(1,itemId);
		
		ResultSet re = ps.executeQuery();
		re.next();
		String ownerName = re.getString(8);
		String nickname = getNickname(ownerName);
		String picUrl = getImage(ownerName);
		String time = re.getString(11);
		String goodsTitle = re.getString(2);
		String intro = re.getString(3);
		String category = re.getString(10);
		String price = re.getString(5);
		String tempgoodsPic = re.getString(4);
		String tempnotes = re.getString(7);
		String isSelled = re.getString(9);
		
		
		ArrayList<String> goodsPic = new ArrayList<String>();
		if(tempgoodsPic.indexOf("-")>0) {
			for(;;) {
				int i = tempgoodsPic.indexOf("-");
				if(i>0) {
					goodsPic.add("http://120.79.234.43/TiaoZao/Image?imageUrl="+tempgoodsPic.substring(0, i));
					tempgoodsPic = tempgoodsPic.substring(i+1);
				}
				else {
					goodsPic.add("http://120.79.234.43/TiaoZao/Image?imageUrl="+tempgoodsPic);
					break;
				}
			}
		}
		else {
			goodsPic.add("http://120.79.234.43/TiaoZao/Image?imageUrl="+tempgoodsPic);
		}

		ArrayList<Object> notes = new ArrayList<Object>();
		if(tempnotes != null) {
	    	String[] sourceStrArray = tempnotes.split("-");
	    	for (int i = 0; i < sourceStrArray.length; i++) {
	    		String[] a = sourceStrArray[i].split("[+]");
	    		HashMap<String,String> innernotes = new HashMap<String,String>();
				innernotes.put("nickname", getNickname(a[0]));
				innernotes.put("content", a[1]);
				innernotes.put("picUrl",getImage(a[0]));
	    		notes.add(innernotes);
	    	}
		}
		else {
			
		}
		ItemObject a =new ItemObject( nickname, picUrl, time, goodsTitle, intro, category, price, goodsPic, notes,isSelled);
		re.close();
		ps.close();
		con.close();
		return a;
	}
	//按分类查看商品
	public static HashMap<String, Object> itemListSortedByCategory (String category) throws ClassNotFoundException, SQLException {
		Class.forName("com.mysql.jdbc.Driver");
		Connection con = DBUtil.getConnection();		
		PreparedStatement ps = con.prepareStatement("select * from iteminfo where category=? order by id desc");
		ps.setString(1,category);
		ps.execute();
		
		HashMap<String, Object> map = new HashMap<String, Object>();
		ResultSet re = ps.executeQuery();
		ArrayList<ItemBriefWithOwner> list = new ArrayList<ItemBriefWithOwner>();
		while(re.next()) {
			int id = re.getInt(1);
			String ownerName = re.getString(8);
			String nickname = getNickname(ownerName);
			String time = re.getString(11);
			String goodsTitle = re.getString(2);
			String price = re.getString(5);
			String tempgoodsPic = re.getString(4);	
			String isSelled = re.getString(9);
			ArrayList<String> goodsPic = new ArrayList<String>();
			if(tempgoodsPic.indexOf("-")>0) {
				for(;;) {
					int i = tempgoodsPic.indexOf("-");
					if(i>0) {
						goodsPic.add("http://120.79.234.43/TiaoZao/Image?imageUrl="+tempgoodsPic.substring(0, i));
						tempgoodsPic = tempgoodsPic.substring(i+1);
					}
					else {
						goodsPic.add("http://120.79.234.43/TiaoZao/Image?imageUrl="+tempgoodsPic);
						break;
					}
				}
			}
			else {
				goodsPic.add("http://120.79.234.43/TiaoZao/Image?imageUrl="+tempgoodsPic);
			}
			PreparedStatement ps1 = con.prepareStatement("select image from userinfo where usrname=?");
			ps1.setString(1,ownerName);
			ResultSet re1 = ps1.executeQuery();
			re1.next();
			String picUrl = re1.getString(1);
			if(isSelled.equals("false")) {
				ItemBriefWithOwner a =new ItemBriefWithOwner(id, nickname, picUrl, time, goodsTitle, category, price, goodsPic,isSelled) ;
				list.add(a);
			}
			ps1.close();
			re1.close();
		}
		map.put("goods", list);
		ps.close();
		re.close();
		con.close();
		return map;
	}
	//用户给商品留言
	public static void leaveMessage (String username, String message, String goodsId) throws ClassNotFoundException, SQLException {
		Class.forName("com.mysql.jdbc.Driver");
		Connection con = DBUtil.getConnection();		
		PreparedStatement ps = con.prepareStatement("select message from iteminfo where id=?");
		ps.setString(1, goodsId);
		ResultSet re = ps.executeQuery();
		String preMassage = null;
		if(re.next()) {
			preMassage = re.getString(1); 
		}		
		ps = con.prepareStatement("update iteminfo set message=? where id=?");
		if(preMassage != null)
			ps.setString(1, preMassage+"-"+username+"+"+message);
		else
			ps.setString(1, username+"+"+message);
		ps.setString(2, goodsId);
		ps.execute();
		ps.close();
		re.close();
		con.close();
	}
	//查看收藏的商品
	public static HashMap<String, Object> collectedItem (String username) throws ClassNotFoundException, SQLException {
		Connection con = DBUtil.getConnection();		
		PreparedStatement ps = con.prepareStatement("select collectedItem from userinfo where usrname=? ");
		ps.setString(1, username);
		ResultSet re = ps.executeQuery();
		String tempCollectedItem =null;
		if(re.next()) {
			tempCollectedItem = re.getString(1); 
		}
		HashMap<String, Object> map = new HashMap<String, Object>();
		ArrayList<Object> list = new ArrayList<Object>();
		if(tempCollectedItem == null) {
//			list.add("");
		}
		else {
	    	String[] sourceStrArray = tempCollectedItem.split("-");
			for(int j=sourceStrArray.length-1 ; j>=0 ;j--) {
				ps = con.prepareStatement("select * from iteminfo where id=?");
				ps.setString(1, sourceStrArray[j]);
				ResultSet re1 = ps.executeQuery();
				if(re1.next()) {
					int id = re1.getInt(1);
					String ownerName = re1.getString(8);
					String nickname = getNickname(ownerName);
					String time = re1.getString(11);
					String goodsTitle = re1.getString(2);
					String price = re1.getString(5);
					String tempgoodsPic = re1.getString(4);	
					String category = re1.getString(10);
					String isSelled =re1.getString(9);
					ArrayList<String> goodsPic = new ArrayList<String>();
					if(tempgoodsPic.indexOf("-")>0) {
						for(;;) {
							int i = tempgoodsPic.indexOf("-");
							if(i>0) {
								goodsPic.add("http://120.79.234.43/TiaoZao/Image?imageUrl="+tempgoodsPic.substring(0, i));
								tempgoodsPic = tempgoodsPic.substring(i+1);
							}
							else {
								goodsPic.add("http://120.79.234.43/TiaoZao/Image?imageUrl="+tempgoodsPic);
								break;
							}
						}
					}
					else {
						goodsPic.add("http://120.79.234.43/TiaoZao/Image?imageUrl="+tempgoodsPic);
					}
					
					PreparedStatement ps1 = con.prepareStatement("select image from userinfo where usrname=?");
					ps1.setString(1,ownerName);
					ResultSet re2 = ps1.executeQuery();
					re2.next();
					String picUrl = re2.getString(1);
					
					ItemBriefWithOwner a =new ItemBriefWithOwner(id, nickname, picUrl, time, goodsTitle, category, price, goodsPic, isSelled) ;
					list.add(a); 
					ps1.close();
				}
				ps.close();
			}
		}
		map.put("goods", list);
		ps.close();
		re.close();
		con.close();
		return map;
	}
	//收藏 或 取消收藏
	public static void AddOrDeleteCollectedItem (String username, String goodsId) throws ClassNotFoundException, SQLException {
		Connection con = DBUtil.getConnection();		
		PreparedStatement ps = con.prepareStatement("select collectedItem from userinfo where usrname=?");
		ps.setString(1, username);
		ResultSet re = ps.executeQuery();
		String collectedItemString = new String();
		if(re.next()) {
			collectedItemString = re.getString(1); 
		}		
		
		String collectedTime = null;
		String[] goodsIdsplit = goodsId.split("[-]");
		for(int j=0;j<goodsIdsplit.length;j++) {
			if(collectedItemString == null || collectedItemString.indexOf(goodsIdsplit[j])<0) {
				if(collectedItemString == null|| collectedItemString.equals("")) {
					collectedItemString = goodsIdsplit[j];
					System.out.println("原收藏列表为空");
				}
				else {
					collectedItemString = collectedItemString +"-"+goodsIdsplit[j];
				}
				ps = con.prepareStatement("select collectedTime from iteminfo where id=?");
				ps.setString(1, goodsIdsplit[j]);
				re = ps.executeQuery();
				if(re.next()) {
					collectedTime = re.getString(1);
				}
				collectedTime = Integer.toString(Integer.parseInt(collectedTime)+1);
				
				ps = con.prepareStatement("update iteminfo set collectedTime=? where id=?");
				ps.setString(1, collectedTime);
				ps.setString(2, goodsIdsplit[j]);
				ps.execute();
				System.out.println("收藏");
			}
			else {
				collectedItemString = collectedItemString.replace("-"+goodsIdsplit[j], "");
				collectedItemString = collectedItemString.replace(goodsIdsplit[j]+"-", "");
				collectedItemString = collectedItemString.replace(goodsIdsplit[j], "");
				ps = con.prepareStatement("select collectedTime from iteminfo where id=?");
				ps.setString(1, goodsIdsplit[j]);
				re = ps.executeQuery();
				if(re.next()) {
					collectedTime = re.getString(1);
				}
				collectedTime = Integer.toString(Integer.parseInt(collectedTime)-1);
				
				ps = con.prepareStatement("update iteminfo set collectedTime=? where id=?");
				ps.setString(1, collectedTime);
				ps.setString(2, goodsIdsplit[j]);
				ps.execute();
				System.out.println("取消收藏");
			}
			
		}
		ps = con.prepareStatement("update userinfo set collectedItem=? where usrname=?");
		ps.setString(1, collectedItemString);
		ps.setString(2, username);
		ps.execute();
		ps.close();
		re.close();
		con.close();
	}
	//按学校查看商品
	public static HashMap<String, Object> itemListSortedBySchool (String serchSchool) throws ClassNotFoundException, SQLException {
		Connection con = DBUtil.getConnection();		
		PreparedStatement ps = con.prepareStatement("");
		StringBuffer setWord = new StringBuffer();
		HashMap<String, Object> map = new HashMap<String, Object>();
		if(serchSchool == null || serchSchool.equals("华南师范大学") || serchSchool.equals("")) {
			ps = con.prepareStatement("select ownedItem from userinfo where school is null or school=? ;");
			ps.setString(1, "华南师范大学");
			map.put("test", "select1");
		}
		else {
			ps = con.prepareStatement("select ownedItem from userinfo where school= ? ");
			ps.setString(1, serchSchool);
			map.put("test", "select2_"+serchSchool);
		}
		ResultSet re = ps.executeQuery();
		int aa=0;
		while(re.next()) {
			System.out.println("aa的值为："+aa++);
			String ownedItem = re.getString(1);
			if(ownedItem != null) {
				String[] ownedItemList = ownedItem.split("[-]");
				for(int i=0; i<ownedItemList.length; i++) {
					setWord.append("id="+ownedItemList[i]+" or ");
				}
				System.out.println(ownedItem);
			}
			else
				continue;
		}
		String setWordFinal = setWord.toString();
		try {
			setWordFinal = setWordFinal.substring(0, setWordFinal.length()-3);
		} catch (Exception e) {
			ArrayList<ItemBriefWithOwner> list = new ArrayList<ItemBriefWithOwner>();
			map.put("goods", list);
			ps.close();
			re.close();
			con.close();
			return map;
		}
		System.out.println("select * from iteminfo where "+setWordFinal);
		if(setWordFinal.equals("id= ;")) {
			ArrayList<ItemBriefWithOwner> list = new ArrayList<ItemBriefWithOwner>();
			map.put("goods", list);
			ps.close();
			re.close();
			con.close();
			return map;
		}
		ps.close();
		re.close();
		con.close();
		
		Connection con1 = DBUtil.getConnection();
		PreparedStatement ps1 = con1.prepareStatement("select * from iteminfo where "+setWordFinal+" order by id desc;");
		ResultSet re1 = ps1.executeQuery();
		ArrayList<ItemBriefWithOwner> list = new ArrayList<ItemBriefWithOwner>();
		while(re1.next()) {
			int id = re1.getInt(1);
			String category = re1.getString(10);
			String ownerName = re1.getString(8);
			String nickname = getNickname(ownerName);
			String time = re1.getString(11);
			String goodsTitle = re1.getString(2);
			String price = re1.getString(5);
			String tempgoodsPic = re1.getString(4);	
			String isSelled = re1.getString(9);
			ArrayList<String> goodsPic = new ArrayList<String>();
			if(tempgoodsPic.indexOf("-")>0) {
				for(;;) {
					int i = tempgoodsPic.indexOf("-");
					if(i>0) {
						goodsPic.add("http://120.79.234.43/TiaoZao/Image?imageUrl="+tempgoodsPic.substring(0, i));
						tempgoodsPic = tempgoodsPic.substring(i+1);
					}
					else {
						goodsPic.add("http://120.79.234.43/TiaoZao/Image?imageUrl="+tempgoodsPic);
						break;
					}
				}
			}
			else {
				goodsPic.add("http://120.79.234.43/TiaoZao/Image?imageUrl="+tempgoodsPic);
			}
			Connection con2 = DBUtil.getConnection();
			PreparedStatement ps2 = con2.prepareStatement("select image from userinfo where usrname=?");
			ps2.setString(1,ownerName);
			ResultSet re2 = ps2.executeQuery();
			re2.next();
			String picUrl = re2.getString(1);
			if(isSelled.equals("false")) {
				ItemBriefWithOwner a =new ItemBriefWithOwner(id, nickname, picUrl, time, goodsTitle, category, price, goodsPic,isSelled) ;
				list.add(a);
			}
			ps2.close();
			re2.close();
			con2.close();
		}
		map.put("goods", list);
		ps1.close();
		re1.close();
		con1.close();
		return map;
	}
	//查看用户买到的商品
	public static HashMap<String, Object> checkPurchasedItem (String username) throws ClassNotFoundException, SQLException {
		Connection con = DBUtil.getConnection();		
		PreparedStatement ps = con.prepareStatement("select purchasedItem from userinfo where usrname=?");
		ps.setString(1, username);
		ResultSet re = ps.executeQuery();
		HashMap<String, Object> map = new HashMap<String, Object>();
		ArrayList<Object> list = new ArrayList<Object>();
		String temppurchasedItem =null;
		if(re.next()) {
			temppurchasedItem = re.getString(1); 
		}
		if(temppurchasedItem == null) {
//			list.add("");
		}
		else {
	    	String[] sourceStrArray = temppurchasedItem.split("-");
			for(int j=sourceStrArray.length-1 ; j>=0 ;j--) {
				ps = con.prepareStatement("select * from iteminfo where id=?");
				ps.setString(1, sourceStrArray[j]);
				ResultSet re1 = ps.executeQuery();
				if(re1.next()) {
					int id = re1.getInt(1);
					String ownerName = re1.getString(8);
					String nickname = getNickname(ownerName);
					String time = re1.getString(11);
					String goodsTitle = re1.getString(2);
					String price = re1.getString(5);
					String tempgoodsPic = re1.getString(4);	
					String category = re1.getString(10);
					ArrayList<String> goodsPic = new ArrayList<String>();
					if(tempgoodsPic.indexOf("-")>0) {
						for(;;) {
							int i = tempgoodsPic.indexOf("-");
							if(i>0) {
								goodsPic.add("http://120.79.234.43/TiaoZao/Image?imageUrl="+tempgoodsPic.substring(0, i));
								tempgoodsPic = tempgoodsPic.substring(i+1);
							}
							else {
								goodsPic.add("http://120.79.234.43/TiaoZao/Image?imageUrl="+tempgoodsPic);
								break;
							}
						}
					}
					else {
						goodsPic.add("http://120.79.234.43/TiaoZao/Image?imageUrl="+tempgoodsPic);
					}
					ps = con.prepareStatement("select image from userinfo where usrname=?");
					ps.setString(1,ownerName);
					ResultSet re2 = ps.executeQuery();
					re2.next();
					String picUrl = re2.getString(1);
					
					ItemBriefWithOwner a =new ItemBriefWithOwner(id, nickname, picUrl, time, goodsTitle, category, price, goodsPic, "true") ;
					list.add(a); 
				}
			}
		}
		map.put("goods", list);
		ps.close();
		re.close();
		con.close();
		return map;
	}
	
	//购买商品
	public static void purchasedItem (String username, String goodsId) throws ClassNotFoundException, SQLException {
		Connection con = DBUtil.getConnection();		
		PreparedStatement ps = con.prepareStatement("select purchasedItem from userinfo where usrname=?");
		ps.setString(1, username);
		ResultSet re = ps.executeQuery();
		String purchasedItem = "";
		if(re.next()) {
			purchasedItem = re.getString(1); 
			if(purchasedItem == null) {
				purchasedItem = goodsId;
			}
			else {
				purchasedItem = purchasedItem + "-" +goodsId;
			}	
		}
		ps = con.prepareStatement("update userinfo set purchasedItem=? where usrname=?");
		ps.setString(1, purchasedItem);
		ps.setString(2, username);
		ps.execute();
		ps = con.prepareStatement("select shoppingCart from userinfo where usrname = ?");
		ps.setString(1, username);
		re = ps.executeQuery();
		String shoppingCart =null;
		if(re.next()) {
			shoppingCart = re.getString(1);
		}
		String[] goodsIdList = goodsId.split("-");
		for(int i =0; i<goodsIdList.length ;i++) {
			ps = con.prepareStatement("update iteminfo set sold=? where id=?");
			ps.setString(1, "true");
			ps.setString(2, goodsIdList[i]);
			ps.execute();
			if(shoppingCart!=null && shoppingCart.contains(goodsIdList[i])) {
				shoppingCart = shoppingCart.replace("-"+goodsIdList[i], "");
				shoppingCart = shoppingCart.replace(goodsIdList[i]+"-", "");
				shoppingCart = shoppingCart.replace(goodsIdList[i], "");
			}
		}
		ps = con.prepareStatement("update userinfo set shoppingCart=? where usrname=?");
		ps.setString(1, shoppingCart);
		ps.setString(2, username);
		ps.execute();
		ps.close();
		re.close();
		con.close();
	}
	//添加 或 删除 购物车
	public static void AddOrDeleteShoppingCart (String username, String goodsId) throws ClassNotFoundException, SQLException {
		Connection con = DBUtil.getConnection();		
		PreparedStatement ps = con.prepareStatement("select shoppingCart from userinfo where usrname=?");
		ps.setString(1, username);
		ResultSet re = ps.executeQuery();
		String shoppingCart = new String();
		if(re.next()) {
			shoppingCart = re.getString(1); 
			System.out.println(shoppingCart);
		}
		String[] shoppingCartsplit = goodsId.split("[-]");
		for(int j=0;j<shoppingCartsplit.length;j++) {
			System.out.println(shoppingCartsplit[j]);
			if(shoppingCart == null || shoppingCart.indexOf(shoppingCartsplit[j])<0) {
				if(shoppingCart == null || shoppingCart.equals("")) {
					shoppingCart = shoppingCartsplit[j];
					System.out.println("购物车为空");
				}
				else {
					shoppingCart = shoppingCart +"-"+shoppingCartsplit[j];
				}
				System.out.println("添加商品到购物车");
			}
			else {
				shoppingCart = shoppingCart.replace("-"+shoppingCartsplit[j], "");
				shoppingCart = shoppingCart.replace(shoppingCartsplit[j]+"-", "");
				shoppingCart = shoppingCart.replace(shoppingCartsplit[j], "");
				System.out.println("删除购物车商品");
			}
		}
		ps = con.prepareStatement("update userinfo set shoppingCart=? where usrname=?");
		ps.setString(1, shoppingCart);
		ps.setString(2, username);
		ps.execute();
		ps.close();
		re.close();
		con.close();
	}
	//用户反馈
	public static void feedBack (String username, String message) throws ClassNotFoundException, SQLException {
		Connection con = DBUtil.getConnection();		
		PreparedStatement ps = con.prepareStatement("select feedBack from userinfo where usrname=?");
		ps.setString(1, username);
		ResultSet re = ps.executeQuery();
		String feedBack = null; 
		if(re.next()) {
			feedBack = re.getString(1);
		}
		if(feedBack == null) {
			feedBack = message;
		}
		else {
			feedBack = feedBack + "-" + message;
		}
		ps = con.prepareStatement("update userinfo set feedBack=? where usrname=?");
		ps.setString(1, feedBack);
		ps.setString(2, username);
		ps.execute();
		ps.close();
		re.close();
		con.close();
	}
	//判断用户是否收藏该商品 
	public static boolean isCollected (String username, String goodsId) throws ClassNotFoundException, SQLException {
		Connection con = DBUtil.getConnection();		
		PreparedStatement ps = con.prepareStatement("select collectedItem from userinfo where usrname=?");
		ps.setString(1, username);
		ResultSet re = ps.executeQuery();
		String collectedItem = null;
		if(re.next()) {
			collectedItem = re.getString(1);
		}
		if(collectedItem == null || collectedItem.equals("")) {
			ps.close();
			re.close();
			con.close();
			return false;
		}
		else {
			if(collectedItem.contains(goodsId)) {
				ps.close();
				re.close();
				con.close();
				return true;
			}
			else {
				ps.close();
				re.close();
				con.close();
				return false;
			}
		}
	}
	//判断用户是否将该商品加入购物车 
	public static boolean isAddCar (String username, String goodsId) throws ClassNotFoundException, SQLException {
		Connection con = DBUtil.getConnection();		
		PreparedStatement ps = con.prepareStatement("select shoppingCart from userinfo where usrname=?");
		ps.setString(1, username);
		ResultSet re = ps.executeQuery();
		String shoppingCart = null;
		if(re.next()) {
			shoppingCart = re.getString(1);
		}
		if(shoppingCart == null || shoppingCart.equals("")) {
			ps.close();
			re.close();
			con.close();
			return false;
		}
		else {
			if(shoppingCart.contains(goodsId)) {
				ps.close();
				re.close();
				con.close();
				return true;
			}
			else {
				ps.close();
				re.close();
				con.close();
				return false;
			}
		}
	}
	//模糊搜索
	public static HashMap<String, Object> checkItem (String checkWord) throws ClassNotFoundException, SQLException {
		Connection con = DBUtil.getConnection();		
		PreparedStatement ps = con.prepareStatement("");
		HashMap<String, Object> map = new HashMap<String, Object>();
		ArrayList<ItemBriefWithOwner> list = new ArrayList<ItemBriefWithOwner>();
		if(checkWord == null || checkWord.equals("")) {
			ps = con.prepareStatement("select * from iteminfo order by id desc");
		}
		else {
			ps = con.prepareStatement("select * from iteminfo where itemname like ? order by id desc");
			ps.setString(1, "%"+checkWord+"%");
		}
		ResultSet re = ps.executeQuery();
		while(re.next()) {
			int id = re.getInt(1);
			String ownerName = re.getString(8);
			String nickname = getNickname(ownerName);
			String time = re.getString(11);
			String goodsTitle = re.getString(2);
			String price = re.getString(5);
			String tempgoodsPic = re.getString(4);	
			String category = re.getString(10);
			String sold = re.getString(9);
			ArrayList<String> goodsPic = new ArrayList<String>();
			if(tempgoodsPic.indexOf("-")>0) {
				for(;;) {
					int i = tempgoodsPic.indexOf("-");
					if(i>0) {
						goodsPic.add("http://120.79.234.43/TiaoZao/Image?imageUrl="+tempgoodsPic.substring(0, i));
						tempgoodsPic = tempgoodsPic.substring(i+1);
					}
					else {
						goodsPic.add("http://120.79.234.43/TiaoZao/Image?imageUrl="+tempgoodsPic);
						break;
					}
				}
			}
			else {
				goodsPic.add("http://120.79.234.43/TiaoZao/Image?imageUrl="+tempgoodsPic);
			}
			ps = con.prepareStatement("select image from userinfo where usrname=?");
			ps.setString(1,ownerName);
			ResultSet re2 = ps.executeQuery();
			re2.next();
			String picUrl = re2.getString(1);
			if(sold.equals("false")) {
				ItemBriefWithOwner a =new ItemBriefWithOwner(id, nickname, picUrl, time, goodsTitle, category, price, goodsPic, sold) ;
				list.add(a); 
			}
			re2.close();
		}
		
		map.put("goods", list);
		ps.close();
		re.close();
		con.close();
		return map;
	}
	//重新上传用户头像
	public static void imageUpdate (String usrname, String imageUrl) throws ClassNotFoundException, SQLException {
		Connection con = DBUtil.getConnection();		
		PreparedStatement ps = con.prepareStatement("update userinfo set image = ? where usrname = ?");
		ps.setString(1, imageUrl);	
		ps.setString(2, usrname);	
		ps.execute();
		ps.close();
		con.close();
	}
	
	public static void aaaa(String aa) throws SQLException, ClassNotFoundException  {
		Connection con = DBUtil.getConnection();		
		PreparedStatement ps = con.prepareStatement("select usrname from userinfo where school= ? ");
		ps.setString(1, aa);
		ResultSet re = ps.executeQuery();
		int i=0;
		while(re.next()) {
			String usrname = re.getString(1);
			System.out.println("i="+i+"    "+usrname);
			i++;
			ps = con.prepareStatement("select * from iteminfo where ownerName= ? ");
			ps.setString(1, usrname);
			ResultSet re1 = ps.executeQuery();
			while(re1.next()) {
				System.out.println("商品ID："+re1.getString(1));
			}
			re1.close();
		}
		ps.close();
		re.close();
		con.close();
	}
	
	
	public static void main(String[] args) throws SQLException, ClassNotFoundException {

		feedBack("xiaoxiao","asd");
	}

}
