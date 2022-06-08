package com.TiaoZao.servlet;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
 
import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;

import com.TiaoZao.jdbc.JdbcExecute;
import com.TiaoZao.pojo.UserObject;
import com.alibaba.fastjson.JSON;
 

/**
 * Servlet implementation class UploadServlet
 */
@WebServlet("/AddOrUpdateOrDeleteItem")
public class AddOrUpdateOrDeleteItem extends HttpServlet {
    private static final long serialVersionUID = 1L;
     
    // 上传文件存储目录
    private static final String UPLOAD_DIRECTORY = "upload";
 
    // 上传配置
    private static final int MEMORY_THRESHOLD   = 1024 * 1024 * 3;  // 3MB
    private static final int MAX_FILE_SIZE      = 1024 * 1024 * 40; // 40MB
    private static final int MAX_REQUEST_SIZE   = 1024 * 1024 * 50; // 50MB
 
    /**
     * 上传数据及保存文件
     */
    protected void doPost(HttpServletRequest request,
        HttpServletResponse response) throws ServletException, IOException {
    	
    	response.setContentType("text/json;charset=utf-8");
		response.setCharacterEncoding("UTF-8");
		PrintWriter out = response.getWriter();
    	
		request.setCharacterEncoding("UTF-8");
		response.setHeader("Access-Control-Allow-Origin", "http://222.200.137.32"); 
		response.addHeader("Access-Control-Allow-Credentials", "true");  
		
        // 检测是否为多媒体上传
        if (!ServletFileUpload.isMultipartContent(request)) {
            // 如果不是则停止
            PrintWriter writer = response.getWriter();
            writer.println("Error: 表单必须包含 enctype=multipart/form-data");
            writer.flush();
            return;
        }
 
        // 配置上传参数
        DiskFileItemFactory factory = new DiskFileItemFactory();
        // 设置内存临界值 - 超过后将产生临时文件并存储于临时目录中
        factory.setSizeThreshold(MEMORY_THRESHOLD);
        // 设置临时存储目录
        factory.setRepository(new File(System.getProperty("java.io.tmpdir")));
 
        ServletFileUpload upload = new ServletFileUpload(factory);
         
        // 设置最大文件上传值
        upload.setFileSizeMax(MAX_FILE_SIZE);
         
        // 设置最大请求值 (包含文件和表单数据)
        upload.setSizeMax(MAX_REQUEST_SIZE);

        // 中文处理
        upload.setHeaderEncoding("UTF-8"); 

        // 构造临时路径来存储上传的文件
        // 这个路径相对当前应用的目录
//        String uploadPath = request.getServletContext().getRealPath("./")+ UPLOAD_DIRECTORY;
        String uploadPath = "/home/tomcat/apache-tomcat-8.5.8/webapps/upload/";
//        String uploadPath = "E:\\home" ;
         
        // 如果目录不存在则创建
        File uploadDir = new File(uploadPath);
        if (!uploadDir.exists()) {
            uploadDir.mkdir();
        }
 
        String usrname = new String();
        Cookie[] cookies = request.getCookies();
		if(null==cookies) {   

        }else{  
		    for (Cookie cookie: cookies) {
		        if (cookie.getName().equals("LogInUser")) {
		        	usrname = cookie.getValue();
		        	System.out.println(usrname); 
		        }
		    }
        }
        
        
        HashMap<String , String> params = new HashMap<String , String>();
        StringBuffer image = new StringBuffer();
        try {
            // 解析请求的内容提取文件数据
            List<FileItem> formItems = upload.parseRequest(request);
            
            if (formItems != null && formItems.size() > 0) {
                // 迭代表单数据
                for (FileItem item : formItems) {
                    // 处理不在表单中的字段
                    if (!item.isFormField()) {
                        String fileName = new File(item.getName()).getName();
                        //重命名文件
                        fileName = UUID.randomUUID().toString().replace("-", "");
//                        String filePath = uploadPath + File.separator + fileName;
                        String filePath = uploadPath + fileName;
                        File storeFile = new File(filePath);
                        // 在控制台输出文件的上传路径
                        //image.append("http://localhost:8090/TiaoZao/Image?imageUrl="+fileName+"-");
                        image.append(fileName+"-");
                        System.out.println(filePath);
                        // 保存文件到硬盘
                        item.write(storeFile);
                    }
                    //处理表单数据
                    else {
                    	String name = item.getFieldName();  
                        String value = item.getString("UTF-8");
                        params.put(name, value);
                        System.out.println("键名："+name+" 键值："+value);
                    }
                }
            }
        } catch (Exception ex) {
            request.setAttribute("message",
                    "错误信息: " + ex.getMessage());
        }
        
        //处理前端命令
        HashMap<String , Object> map = new HashMap<String , Object>();
        String name = null;
        cookies = request.getCookies();
		if(null==cookies) {   
            map.put("status",false);
            map.put("message","用户未登录");
        }else{  
		    for (Cookie cookie: cookies) {
		        if (cookie.getName().equals("LogInUser")) {
		        	name = cookie.getValue();
		        	System.out.println(name);
		        	System.out.println(params.toString());
		        	String goodsId = "";
		            if(params.get("cmd").equals("0")) {
		            	try {
		    				goodsId = JdbcExecute.addNewItem(params.get("goodsTitle") ,params.get("intro"), params.get("price"), name , params.get("category"), image.toString());
		    				System.out.println("addNewItem");
		            	} catch (ClassNotFoundException e) {
		    				// TODO Auto-generated catch block
		    				e.printStackTrace();
		    			} catch (SQLException e) {
		    				// TODO Auto-generated catch block
		    				e.printStackTrace();
		    			}
		            	map.put("goodsId",goodsId);
		            	map.put("status",true);
		            	map.put("message","");
		            }
		    		if(params.get("cmd").equals("1")) {
		    		    try {
							JdbcExecute.updateItem(params.get("goodsTitle"), params.get("intro"), params.get("price"), params.get("goodsId"), params.get("category"), image.toString());
						} catch (ClassNotFoundException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						} catch (SQLException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
		    		    map.put("goodsId","");
		    		    map.put("status",true);
		            	map.put("message","");
		    		}
		    		if(params.get("cmd").equals("2")) {
		    			try {
							JdbcExecute.deleteItem( params.get("goodsId"));
						} catch (ClassNotFoundException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						} catch (SQLException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
		    			map.put("goodsId", "");
		    			map.put("status",true);
		            	map.put("message","");
		    		}
		        }
		    }
        }

		
		String string = JSON.toJSONString(map, true).toString();
		out.write(string);
		System.out.println("查询数据：jsonText==\n" + string);
    }
}