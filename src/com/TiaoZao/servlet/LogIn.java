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
import com.alibaba.fastjson.JSON;


@WebServlet("/LogIn") 
public class LogIn extends HttpServlet {
	private static final long serialVersionUID = 1L;

	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		request.setCharacterEncoding("utf-8");
		response.setContentType("text/json;charset=utf-8");
		response.setCharacterEncoding("UTF-8");
		PrintWriter out = response.getWriter();
		String string = "default_return";
		response.setHeader("Access-Control-Allow-Origin", "http://222.200.137.32"); 
		response.addHeader("Access-Control-Allow-Credentials", "true");  
		
		
		String name = request.getParameter("username");
		String pass = request.getParameter("password");
		HashMap<String , Object> map = new HashMap<String , Object>();  
		 try {
			 if(JdbcExecute.whetherSucceedLogIn(name, pass))
			 {
				 Cookie cookie = new Cookie("LogInUser", name);  
		         cookie.setMaxAge(30* 60);// 设置为30min  
		         cookie.setPath("/");  
		         response.addCookie(cookie);  
		         cookie.setHttpOnly(true);
				 System.out.println("已添加cookie");  
				 map.put("status",true); 
				 map.put("massage",""); 
			 }
			 else
			 {
				 map.put("status",false);
				 map.put("massage","账号或密码错误。"); 
			 }
		}catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} //访问数据库，返回json数据
		 catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
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
