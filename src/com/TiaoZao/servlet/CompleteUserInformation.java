package com.TiaoZao.servlet;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.TiaoZao.jdbc.JdbcExecute;
import com.TiaoZao.pojo.UserObject;
import com.alibaba.fastjson.JSON;

@WebServlet("/CompleteUserInformation") 
public class CompleteUserInformation extends HttpServlet {
	private static final long serialVersionUID = 1L;

	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		response.setContentType("text/json;charset=utf-8");
		response.setCharacterEncoding("UTF-8");
		PrintWriter out = response.getWriter();
		String string = "default_return";
		response.setHeader("Access-Control-Allow-Origin", "http://222.200.137.32"); 
		response.addHeader("Access-Control-Allow-Credentials", "true");   
		request.setCharacterEncoding("utf-8");
		
		String nicknameGet = request.getParameter("nickname");
		String sexGet = request.getParameter("sex");
		String birthdayGet = request.getParameter("birthday");
		String schoolGet = request.getParameter("school");
		String addressGet = request.getParameter("address");
		String introduceGet = request.getParameter("introduce");
		
		//在这里开始编
		HashMap<String, Object> map = new HashMap<String, Object>();
		String name = null;
        Cookie[] cookies = request.getCookies();
		if(null==cookies) {   
            map.put("status",false);
            map.put("message","用户未登录。");
        }else{  
		    for (Cookie cookie: cookies) {
		        if (cookie.getName().equals("LogInUser")) {
		        	name = cookie.getValue();
		        	System.out.println(name);
		    		try {
		    			UserObject a = JdbcExecute.userInformation(name);
		    			String nickname = a.getNickname();
		    			String sex = a.getSex();
		    			String birthday = a.getBirthday();
		    			String school = a.getSchool();
		    			String introduce = a.getIntroduce();
		    			ArrayList<String> tempaddress = a.getAddress();
		    			Iterator it1 = tempaddress.iterator();
		    			StringBuffer addressbuffer = new StringBuffer();
		    	        while (it1.hasNext()) {
		    	        	addressbuffer.append(it1.next()+"-");
		    	        }
		    	        String address = addressbuffer.substring(0, addressbuffer.length()-1);
		    	        
//		    	        if(!nicknameGet.equals("") && !nicknameGet.equals(nickname)) 
		    	        	nickname = nicknameGet;
//		    	        if(!sexGet.equals("") && !sexGet.equals(sex))
		    	        	sex = sexGet;
//		    	        if(!birthdayGet.equals("") && !birthdayGet.equals(birthday))
		    	        	birthday = birthdayGet;
//		    	        if(!schoolGet.equals("") && !schoolGet.equals(school))
		    	        	school = schoolGet;
//		    	        if(!introduceGet.equals("") && !introduceGet.equals(introduce))
		    	        	introduce = introduceGet;
//		    	        if(!addressGet.equals("") && !addressGet.equals(address))
		    	        	address = addressGet;
		    	        JdbcExecute.userInformationCompletion(nickname, sex, birthday, school, address, introduce, name);
		    	        map.put("status",true);
		                map.put("message","");
		    		} catch (ClassNotFoundException | SQLException e) {
		    			// TODO Auto-generated catch block
		    			e.printStackTrace();
		    		}   
		        }
		    }
        }
		        
		string = JSON.toJSONString(map, true).toString();
		
		out.write(string);
		System.out.println("查询数据：jsonText==\n" + string);

	}
	
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		doGet(request,response);
	}
	
}
