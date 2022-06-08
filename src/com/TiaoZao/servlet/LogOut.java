package com.TiaoZao.servlet;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.alibaba.fastjson.JSON;

@WebServlet("/LogOut") 
public class LogOut extends HttpServlet {
	private static final long serialVersionUID = 1L;

	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		response.setContentType("text/json;charset=utf-8");
		response.setCharacterEncoding("UTF-8");
		PrintWriter out = response.getWriter();
		String string = "default_return";
		response.setHeader("Access-Control-Allow-Origin", "http://222.200.137.32"); 
		response.addHeader("Access-Control-Allow-Credentials", "true");   
		
		HashMap<String , Object> map = new HashMap<String , Object>();  
		Cookie[] cookies = request.getCookies();
		if(null==cookies) {  
            System.out.println("没有cookie");  
            map.put("status",false); 
            map.put("massage","用户没有登陆。"); 
        }else{  
		    for (Cookie cookie: cookies) {
		        if (cookie.getName().equals("LogInUser")) {
		            cookie.setMaxAge(0);
		            cookie.setPath("/");
		            cookie.setHttpOnly(true);
		            response.addCookie(cookie);
		            System.out.println("已删除cookie"); 
		        }
		    }
	        map.put("status",true); 
	        map.put("massage",""); 
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
