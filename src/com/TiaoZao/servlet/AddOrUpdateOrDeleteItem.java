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
     
    // �ϴ��ļ��洢Ŀ¼
    private static final String UPLOAD_DIRECTORY = "upload";
 
    // �ϴ�����
    private static final int MEMORY_THRESHOLD   = 1024 * 1024 * 3;  // 3MB
    private static final int MAX_FILE_SIZE      = 1024 * 1024 * 40; // 40MB
    private static final int MAX_REQUEST_SIZE   = 1024 * 1024 * 50; // 50MB
 
    /**
     * �ϴ����ݼ������ļ�
     */
    protected void doPost(HttpServletRequest request,
        HttpServletResponse response) throws ServletException, IOException {
    	
    	response.setContentType("text/json;charset=utf-8");
		response.setCharacterEncoding("UTF-8");
		PrintWriter out = response.getWriter();
    	
		request.setCharacterEncoding("UTF-8");
		response.setHeader("Access-Control-Allow-Origin", "http://222.200.137.32"); 
		response.addHeader("Access-Control-Allow-Credentials", "true");  
		
        // ����Ƿ�Ϊ��ý���ϴ�
        if (!ServletFileUpload.isMultipartContent(request)) {
            // ���������ֹͣ
            PrintWriter writer = response.getWriter();
            writer.println("Error: ��������� enctype=multipart/form-data");
            writer.flush();
            return;
        }
 
        // �����ϴ�����
        DiskFileItemFactory factory = new DiskFileItemFactory();
        // �����ڴ��ٽ�ֵ - �����󽫲�����ʱ�ļ����洢����ʱĿ¼��
        factory.setSizeThreshold(MEMORY_THRESHOLD);
        // ������ʱ�洢Ŀ¼
        factory.setRepository(new File(System.getProperty("java.io.tmpdir")));
 
        ServletFileUpload upload = new ServletFileUpload(factory);
         
        // ��������ļ��ϴ�ֵ
        upload.setFileSizeMax(MAX_FILE_SIZE);
         
        // �����������ֵ (�����ļ��ͱ�����)
        upload.setSizeMax(MAX_REQUEST_SIZE);

        // ���Ĵ���
        upload.setHeaderEncoding("UTF-8"); 

        // ������ʱ·�����洢�ϴ����ļ�
        // ���·����Ե�ǰӦ�õ�Ŀ¼
//        String uploadPath = request.getServletContext().getRealPath("./")+ UPLOAD_DIRECTORY;
        String uploadPath = "/home/tomcat/apache-tomcat-8.5.8/webapps/upload/";
//        String uploadPath = "E:\\home" ;
         
        // ���Ŀ¼�������򴴽�
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
            // ���������������ȡ�ļ�����
            List<FileItem> formItems = upload.parseRequest(request);
            
            if (formItems != null && formItems.size() > 0) {
                // ����������
                for (FileItem item : formItems) {
                    // �����ڱ��е��ֶ�
                    if (!item.isFormField()) {
                        String fileName = new File(item.getName()).getName();
                        //�������ļ�
                        fileName = UUID.randomUUID().toString().replace("-", "");
//                        String filePath = uploadPath + File.separator + fileName;
                        String filePath = uploadPath + fileName;
                        File storeFile = new File(filePath);
                        // �ڿ���̨����ļ����ϴ�·��
                        //image.append("http://localhost:8090/TiaoZao/Image?imageUrl="+fileName+"-");
                        image.append(fileName+"-");
                        System.out.println(filePath);
                        // �����ļ���Ӳ��
                        item.write(storeFile);
                    }
                    //���������
                    else {
                    	String name = item.getFieldName();  
                        String value = item.getString("UTF-8");
                        params.put(name, value);
                        System.out.println("������"+name+" ��ֵ��"+value);
                    }
                }
            }
        } catch (Exception ex) {
            request.setAttribute("message",
                    "������Ϣ: " + ex.getMessage());
        }
        
        //����ǰ������
        HashMap<String , Object> map = new HashMap<String , Object>();
        String name = null;
        cookies = request.getCookies();
		if(null==cookies) {   
            map.put("status",false);
            map.put("message","�û�δ��¼");
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
		System.out.println("��ѯ���ݣ�jsonText==\n" + string);
    }
}