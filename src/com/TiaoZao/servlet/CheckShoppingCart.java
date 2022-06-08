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

@WebServlet("/CheckShoppingCart") 
public class CheckShoppingCart extends HttpServlet {
	private static final long serialVersionUID = 1L;

	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		response.setContentType("text/json;charset=utf-8");
		response.setCharacterEncoding("UTF-8");
		PrintWriter out = response.getWriter();
		String string = "default_return";
		response.setHeader("Access-Control-Allow-Origin", "http://222.200.137.32"); 
		response.addHeader("Access-Control-Allow-Credentials", "true");  
		
		//�����￪ʼ��
		HashMap<String, Object> map = new HashMap<String, Object>();
		
		String name = null;
        Cookie[] cookies = request.getCookies();
		if(null==cookies) {   
            map.put("status",false);
            map.put("message","�û�δ��¼��");
        }else{  
		    for (Cookie cookie: cookies) {
		        if (cookie.getName().equals("LogInUser")) {
		        	name = cookie.getValue();
		        	System.out.println(name);
		        	try {
		    			map = JdbcExecute.userShoppingCart(name);
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
		System.out.println("��ѯ���ݣ�jsonText==\n" + string);

	}
	
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		doGet(request,response);
	}
	
}
