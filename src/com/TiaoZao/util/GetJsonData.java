package com.TiaoZao.util;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

public class GetJsonData {

	//发送JSON字符串 如果成功则返回成功标识。
    public static String doJsonPost(String urlPath, JSONObject  Json) {
        // HttpClient 6.0被抛弃了
        String result = "";
        StringBuffer sb=new StringBuffer();
        BufferedReader reader = null;
        try {
            URL url = new URL(urlPath);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("POST");
            conn.setDoOutput(true);
            conn.setDoInput(true);
            conn.setUseCaches(false);
            conn.setRequestProperty("Connection", "Keep-Alive");
            conn.setRequestProperty("Charset", "UTF-8");
            // 设置文件类型:
            conn.setRequestProperty("Content-Type","application/json; charset=UTF-8");
            // 设置接收类型否则返回415错误
            //conn.setRequestProperty("accept","*/*")此处为暴力方法设置接受所有类型，以此来防范返回415;
          conn.setRequestProperty("accept","application/json");
            // 往服务器里面发送数据
    
                byte[] writebytes = Json.toString().getBytes();  
                // 设置文件长度
                conn.setRequestProperty("Content-Length", String.valueOf(writebytes.length));
                OutputStream outwritestream = conn.getOutputStream();
                outwritestream.write(Json.toString().getBytes());
                outwritestream.flush();
                outwritestream.close();
                
            
            if (conn.getResponseCode() == 200) {
            	
            	System.out.println("连接成功");
            	 // 请求返回的状态  
                if (HttpURLConnection.HTTP_OK == conn.getResponseCode()){  
                    System.out.println("连接成功");  
                    // 请求返回的数据  
                    InputStream in1 = conn.getInputStream();  
                    try {  
                          String readLine=new String();  
                          BufferedReader responseReader=new BufferedReader(new InputStreamReader(in1,"UTF-8"));  
                          while((readLine=responseReader.readLine())!=null){  
                            sb.append(readLine).append("\n");  
                          }  
                          responseReader.close();  
                          System.out.println(sb.toString());  
                          
                    } catch (Exception e1) {  
                        e1.printStackTrace();  
                    }  
                } else {  
                    System.out.println("error++");  
                      
                } 
                
                
                
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return sb.toString();
    }
    
	public static void main(String[] args) {
		com.alibaba.fastjson.JSONObject jsonParam = new com.alibaba.fastjson.JSONObject();
		jsonParam.put("cleint_id", "vvvvvvvv");
		jsonParam.put("password", "ssss");
		jsonParam.put("code", "eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJleHQiOjE1MjkyMjUzMjU3MDIsImNvZGUiOiJGWDY0MTkiLCJ1c2VySWQiOiIyMjIiLCJpYXQiOjE1MjkyMjE3MjU3MDIsImNsaWVudF9pZCI6ImNsaWVudF9pZD0zYjY1YWVlOC00NjU0LTRiMzYtYTQ3YS05YmM0NDk5NDlmOTImcmVkaXJlY3RfdXJsPWh0dHA6In0.0N5v4My-uVxhYMsruxvybDcQpJxiIQuxhv-Nfbz-EEo");
		String url="http://localhost:8080/AccountService/token";
		String data=GetJsonData.doJsonPost(url,jsonParam);
                //返回的是一个[{}]格式的字符串时:                               
              //  JSONArray jsonArray = new JSONArray(data);                       
               //返回的是一个{}格式的字符串时:                       
               /*JSONObject obj= new JSONObject(data);*/      
		System.out.println(data);
    }
}



