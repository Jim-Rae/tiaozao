
package com.TiaoZao.util;  
  
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.UUID;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebFilter;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;  
import javax.servlet.http.HttpServletRequest;  
import javax.servlet.http.HttpServletResponse;

import com.TiaoZao.jdbc.JdbcExecute;
import com.TiaoZao.pojo.UserObject;
import com.alibaba.fastjson.JSON;

  
@WebServlet("/TestCode")
public class TestCode extends HttpServlet {  
  
    private static final long serialVersionUID = 1L;  
  
    public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {  
    	
    	response.setContentType("image/*");
		  String imageUrl = request.getParameter("imageUrl");

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
		        	//这里编
		        }
		    }
        }
          
    }  
  

    public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException { 
    	doGet(request,response);
    }
    
    public static void main(String[] args) throws SQLException, ClassNotFoundException {
    	
    	String aa = UUID.randomUUID().toString().replace("-", "");
    	System.out.println(aa);
    	System.out.println();
    	
    }
    
}