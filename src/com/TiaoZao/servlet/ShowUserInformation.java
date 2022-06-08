package com.TiaoZao.servlet;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.SQLException;
import java.util.HashMap;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.TiaoZao.jdbc.JdbcExecute;
import com.TiaoZao.pojo.UserObject;
import com.alibaba.fastjson.JSON;

@WebServlet("/ShowUserInformation") 
public class ShowUserInformation extends HttpServlet {
	private static final long serialVersionUID = 1L;

	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		response.setContentType("text/json;charset=utf-8");
		response.setCharacterEncoding("UTF-8");
		PrintWriter out = response.getWriter();
		String string = "default_return";
		response.setHeader("Access-Control-Allow-Origin", "http://222.200.137.32"); 
		response.addHeader("Access-Control-Allow-Credentials", "true");  
		
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
		    			map.put("nickname", a.getNickname());
		    			map.put("sex", a.getSex());
		    			map.put("birthday", a.getBirthday());
		    			map.put("school", a.getSchool());
		    			map.put("address", a.getAddress());
		    			map.put("picUrl", a.getImage());
		    			map.put("introduce", a.getIntroduce());
		    			map.put("status", true);
		    			map.put("message", "");
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
