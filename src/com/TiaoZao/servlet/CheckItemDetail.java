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

@WebServlet("/CheckItemDetail") 
public class CheckItemDetail extends HttpServlet {
	private static final long serialVersionUID = 1L;

	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		response.setContentType("text/json;charset=utf-8");
		response.setCharacterEncoding("UTF-8");
		PrintWriter out = response.getWriter();
		String string = "default_return";
		response.setHeader("Access-Control-Allow-Origin", "http://222.200.137.32"); 
		response.addHeader("Access-Control-Allow-Credentials", "true");   
		
		String goodsId = request.getParameter("goodsId");
		
		//�����￪ʼ��
		HashMap<String, Object> map = new HashMap<String, Object>();
		String name = null;
        Cookie[] cookies = request.getCookies();
		if(null==cookies) {   
			map.put("isLogin", false);
			map.put("isCollected", "");
			map.put("isAddCar", "");
        }else{  
		    for (Cookie cookie: cookies) {
		        if (cookie.getName().equals("LogInUser")) {
		        	name = cookie.getValue();
		        	System.out.println(name);
		        	map.put("isLogin", true);
		        	try {
						map.put("isCollected", JdbcExecute.isCollected(name,goodsId));
						map.put("isAddCar", JdbcExecute.isAddCar(name, goodsId));
					} catch (ClassNotFoundException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} catch (SQLException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
		        }
		    }
        }

		try {
			map.put("goods", JdbcExecute.itemDetail(goodsId));
			map.put("status", true);
			map.put("message", "");
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		        
		string = JSON.toJSONString(map, true).toString();
		
		out.write(string);
		System.out.println("��ѯ���ݣ�jsonText==\n" + string);
	}
	
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		doGet(request,response);
	}
	
}
