package com.TiaoZao.servlet;

import java.io.IOException;
import java.sql.SQLException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.TiaoZao.jdbc.JdbcExecute;
import com.TiaoZao.util.GetJsonData;
import com.alibaba.fastjson.JSONObject;

/**
 * Servlet implementation class AccountService
 */
@WebServlet("/index")
public class AccountService extends HttpServlet {
	private static final long serialVersionUID = 1L;
       

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

		String code =request.getParameter("code");
		System.out.println("tiaozao:"+code);
		request.setCharacterEncoding("utf-8");
		com.alibaba.fastjson.JSONObject jsonParam = new com.alibaba.fastjson.JSONObject();
		jsonParam.put("code", code);
		jsonParam.put("cleint_id", "3b65aee8-4654-4b36-a47a-9bc449949f92");
		jsonParam.put("password", "215215");
		String url="http://www.micowxy.xin/AccountService/token";
		String data=GetJsonData.doJsonPost(url,jsonParam);
                //返回的是一个[{}]格式的字符串时:                               
              //  JSONArray jsonArray = new JSONArray(data);                       
               //返回的是一个{}格式的字符串时:                       
               /*JSONObject obj= new JSONObject(data);*/      
		System.out.println("tiaozao"+data);
	     JSONObject jsonStr= JSONObject.parseObject(data);
		System.out.println(jsonStr.get("userId"));
		
		String name =jsonStr.get("userId").toString();
		
		System.out.println(name);
		try {
			if(!JdbcExecute.whetherUserExist(name)) {
				JdbcExecute.addAccountServiceUser(name, "12345678");
			}
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		 Cookie cookie = new Cookie("LogInUser", name);  
         cookie.setMaxAge(30* 60);
         cookie.setPath("/");  
         response.addCookie(cookie);  
         cookie.setHttpOnly(true);
		 System.out.println("设置cookie");  
		response.sendRedirect("http://www.micowxy.xin/h5/tiaozaoH5/pages/index/index.html");
	}


}
