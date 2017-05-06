package server.myhttphandler;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import org.apache.commons.io.IOUtils;
import org.json.JSONException;
import org.json.JSONObject;

import server.IBusinessLogic;
import server.ISession;

import com.sun.net.httpserver.Headers;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;



import common.Credential;
import common.ChangePasswdResult;


public class ChangePasswdHandler implements HttpHandler{
	private final IBusinessLogic m_Business;
	private final ISession m_Session;
	
	public ChangePasswdHandler(IBusinessLogic business,ISession session){
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
		if (userID==null){									//sessionID无效则返回403
			t.sendResponseHeaders(401, -1);
			return;
		}
		
		/**
		 * 以下进行Body解析及发送响应内容
		 */
		InputStream in=t.getRequestBody();                      //获取Body
		String requestBody=IOUtils.toString(in,"UTF-8"); 
		in.close();				
		
		JSONObject jsonRequest;									//解析Body
		try{
			jsonRequest=new JSONObject(requestBody);
			String password=jsonRequest.getString("password");  
			String newpassword=jsonRequest.getString("newPassword");
			
			Credential cred=new Credential(userID,password);    //将JSON对象转换为java对象
			ChangePasswdResult result=m_Business.changePasswd(cred, newpassword);   
																//进行修改密码操作
			
			Headers h=t.getResponseHeaders();                   //设置响应头Header
			h.add("Content-Type","application/json");           //Content-Type加入响应头
			
			JSONObject jsonResponse=new JSONObject();     
			jsonResponse.put("status", result.getStatus());   //将修改结果包装为JSON对象
			String response=jsonResponse.toString();          //转为字符串
			
			t.sendResponseHeaders(200, response.length());    //发送响应码Code
			
			OutputStream os = t.getResponseBody();            //返回修改写入响应Body
            os.write(response.getBytes());
            os.close();                                       
            
		}catch (JSONException e){
			t.sendResponseHeaders(400, -1);     //JSON对象格式错误
			return;
		}
	}
}