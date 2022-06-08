package com.TiaoZao.servlet;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileUploadException;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;

import com.TiaoZao.jdbc.JdbcExecute;
import com.alibaba.fastjson.JSON;


@WebServlet("/CompleteUserImage") 
public class CompleteUserImage extends HttpServlet {
	private static final long serialVersionUID = 1L;
	
    // �ϴ��ļ��洢Ŀ¼
    private static final String UPLOAD_DIRECTORY = "upload";
    // �ϴ�����
    private static final int MEMORY_THRESHOLD   = 1024 * 1024 * 3;  // 3MB
    private static final int MAX_FILE_SIZE      = 1024 * 1024 * 40; // 40MB
    private static final int MAX_REQUEST_SIZE   = 1024 * 1024 * 50; // 50MB
 
	
	
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		request.setCharacterEncoding("utf-8");

		String name = null;
		
		response.setContentType("text/json;charset=utf-8");
		response.setCharacterEncoding("UTF-8");
		PrintWriter out = response.getWriter();	
		response.setHeader("Access-Control-Allow-Origin", "http://222.200.137.32"); 
		response.addHeader("Access-Control-Allow-Credentials", "true");   
        
		
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
		
		
		String string = "default_return";
		String fileName = null;
		String filePath = null;

		
		HashMap<String , String> map = new HashMap<String , String>();  
		
		Cookie[] cookies = request.getCookies();
		if(null==cookies) {   
            map.put("status","false");
            map.put("message","�û�δ��¼��");
        }else{  
		    for (Cookie cookie: cookies) {
		        if (cookie.getName().equals("LogInUser")) {
		        	name = cookie.getValue();
		        	System.out.println(name);
		        }
		    }
		    map.put("status","true");
		    map.put("message","");
		    List<FileItem> formItems = null;
			try {
				formItems = upload.parseRequest(request);
			} catch (FileUploadException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		    
            if (formItems != null && formItems.size() > 0) {
                // ����������
                for (FileItem item : formItems) {
                    // �����ڱ��е��ֶ�
                    if (!item.isFormField()) {
                        fileName = new File(item.getName()).getName();
                        fileName = name + "Pic";
//                        filePath = uploadPath + File.separator + fileName;
                        filePath = uploadPath + fileName;
                        File storeFile = new File(filePath);
                        // �ڿ���̨����ļ����ϴ�·��
                        System.out.println(filePath);
                        // �����ļ���Ӳ��
                        try {
							item.write(storeFile);
						} catch (Exception e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
                    }
                }
            }
            try {
				JdbcExecute.imageUpdate(name, "http://120.79.234.43/TiaoZao/Image?imageUrl="+fileName);
			} catch (ClassNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
            map.put("picUrl","http://120.79.234.43/TiaoZao/Image?imageUrl="+fileName);
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
