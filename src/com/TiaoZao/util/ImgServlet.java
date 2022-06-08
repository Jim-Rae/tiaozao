
package com.TiaoZao.util;  
  
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.sql.SQLException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebFilter;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;  
import javax.servlet.http.HttpServletRequest;  
import javax.servlet.http.HttpServletResponse;

  
@WebServlet("/Image")
public class ImgServlet extends HttpServlet {  
  
    private static final long serialVersionUID = 1L;  
  
    public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {  

		String imageUrl = request.getParameter("imageUrl");

//		String uploadPath = request.getServletContext().getRealPath("/upload/")+ imageUrl;
		String uploadPath = "/home/tomcat/apache-tomcat-8.5.8/webapps/upload/"+imageUrl;
//		String uploadPath = "E:\\home\\"  + imageUrl;
		
		System.out.println(uploadPath);
		  
		if(null != uploadPath && !"".equals(uploadPath.trim())) {  
			File file = new File(uploadPath);
	        FileInputStream fis;
	        fis = new FileInputStream(file);
			long size = file.length();
	        byte[] temp = new byte[(int) size];
	        fis.read(temp, 0, (int) size);
	        fis.close();
	        response.setContentType("image/"+imageUrl.substring(imageUrl.lastIndexOf(".")+1, imageUrl.length()));
	        OutputStream out = response.getOutputStream();
	        out.write(temp);
	        out.flush();
	        out.close();   
		}
    }  
  

}