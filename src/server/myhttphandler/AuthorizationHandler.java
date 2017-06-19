package server.myhttphandler;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URI;

import org.apache.commons.io.IOUtils;
import org.json.JSONException;
import org.json.JSONObject;

import server.IBusinessLogic;
import server.ISession;

import com.sun.net.httpserver.Headers;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;







import common.Authorization;
import common.AuthorizationResult;
import common.Credential;
import common.ChangePasswdResult;
import common.RenameFileResult;


public class AuthorizationHandler implements HttpHandler{
	private final IBusinessLogic m_Business;
	private final ISession m_Session;
	
	public AuthorizationHandler(IBusinessLogic business,ISession session){
		m_Business=business;
		m_Session=session;
	}

	public void handle(HttpExchange t) throws IOException {
		/**
		 * 以下进行Method判断
		 */
		String requestMethod=t.getRequestMethod();  //获取Method
		if (!requestMethod.equals("POST")){         //判断Method是否正确
			t.sendResponseHeaders(400, -1);         
			return;
		}
		/**
		 * 以下进行Content-Type判断
		 */
		Headers requestHeader=t.getRequestHeaders();   	//获取Header
		if (!HttpFormUtil.judgeContentType(requestHeader, "application/json")){ 
			t.sendResponseHeaders(400, -1);
			return;
		}
		/**
		 * 以下进行Cookie判断
		 */
		String sessionID=HttpFormUtil.getCookie(requestHeader);
		String userID=m_Session.getUserID(sessionID);       //根据sessionID取出userID
		if (userID==null){									//sessionID无效则返回401
			t.sendResponseHeaders(401, -1);
			return;
		}
		
		/**
		 * 以下进行Body解析及发送响应内容
		 */
    	URI uri=t.getRequestURI();                           //获取URI
        String fileID=HttpFormUtil.getQueryParameter(uri, "fileID");
        if (fileID==null){                                   //获取参数
             t.sendResponseHeaders(400, -1);
             return;
        }
        
		InputStream in=t.getRequestBody();                      //获取Body
		String requestBody=IOUtils.toString(in,"UTF-8"); 
		in.close();				
		
		JSONObject jsonRequest;									//解析Body
		try{
			jsonRequest=new JSONObject(requestBody);
			String authorization=jsonRequest.getString("authorization"); 
			AuthorizationResult result;
			if (authorization.equalsIgnoreCase("self"))
			    result=m_Business.setAuthorization(fileID, userID, false);
			else if (authorization.equalsIgnoreCase("open"))
			    result=m_Business.setAuthorization(fileID, userID, true);
			else{
			    t.sendResponseHeaders(400, -1);
			    return;
			}
			
			Headers h=t.getResponseHeaders();                   //设置响应头Header
			h.add("Content-Type","application/json");           //Content-Type加入响应头
			
			JSONObject jsonResponse=new JSONObject();     
			jsonResponse.put("status", result.getStatus());   //将修改结果包装为JSON对象
			byte[] response=jsonResponse.toString().getBytes("UTF-8");   
			                                                    //转为byte类型
			
			t.sendResponseHeaders(200, response.length);    //发送响应码Code
			
			OutputStream os = t.getResponseBody();            //返回修改写入响应Body
            os.write(response);
            os.close();                                       
            
		}catch (JSONException e){
			t.sendResponseHeaders(400, -1);     //JSON对象格式错误
			return;
		}
	}
}