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

@WebServlet("/JasonReturnSample") 
public class JasonReturnSample extends HttpServlet {
	private static final long serialVersionUID = 1L;

	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		response.setContentType("text/json;charset=utf-8");
		response.setCharacterEncoding("UTF-8");
		request.setCharacterEncoding("UTF-8");
		PrintWriter out = response.getWriter();
		String string = "default_return";
		response.setHeader("Access-Control-Allow-Origin", "*"); 
		response.addHeader("Access-Control-Allow-Credentials", "true"); 
		
		String aa = request.getParameter("aa");
		
		//在这里开始编
		HashMap<String, Object> map = new HashMap<String, Object>();
		 
		        
		string = JSON.toJSONString(map, true).toString();
		
		out.write(string);
		System.out.println("查询数据：jsonText==\n" + string);

	}
	
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		doGet(request,response);
	}
	
}
