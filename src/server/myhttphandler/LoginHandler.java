package server.myhttphandler;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import org.apache.commons.io.IOUtils;
import org.json.JSONObject;

import com.sun.net.httpserver.Headers;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import common.Credential;
import common.LoginResult;

import org.json.*;

import server.IBusinessLogic;
import server.ISession;

/**
 * 服务器端响应Login要求
 * @author yzj
 */

public class LoginHandler implements HttpHandler{
	private final IBusinessLogic m_Business;
	private final ISession m_Session;
	
	public LoginHandler(IBusinessLogic business,ISession session){
		m_Business=business;
		m_Session=session;
	}

	public void handle(HttpExchange t) throws IOException {
		String requestMethod=t.getRequestMethod();  //获取Method
		if (!requestMethod.equals("POST")){         //判断Method是否正确
			t.sendResponseHeaders(400, -1);         
			return;
		}
		
		Headers requestHeader=t.getRequestHeaders();   	//获取Header
		if (!HttpFormUtil.judgeContentType(requestHeader, "application/json")){ 
			t.sendResponseHeaders(400, -1);
			return;
		}
		
		InputStream in=t.getRequestBody();                      //获取Body
		String requestBody=IOUtils.toString(in,"UTF-8"); 
		in.close();				
		
		JSONObject jsonRequest;									//解析Body
		try{
			jsonRequest=new JSONObject(requestBody);
			String userID=jsonRequest.getString("userID");
			String password=jsonRequest.getString("password");  
			Credential cred=new Credential(userID,password);    //将JSON对象转换为java对象
			LoginResult result=m_Business.login(cred);          //进行身份验证
			
			Headers h=t.getResponseHeaders();                   //设置响应头Header
			h.add("Content-Type","application/json");           //Content-Type加入响应头
			if (result.equals(LoginResult.OK)){                 //若身份验证通过
				String sessionID=m_Session.createSession(cred); //获取sessionID，作为cookie
				h.add("Set-Cookie", "sessionID="+sessionID);    //Cookie加入响应头
			}
			
			JSONObject jsonResponse=new JSONObject();     
			jsonResponse.put("status", result.getStatus());   //将登录结果包装为JSON对象
		    byte[] response=jsonResponse.toString().getBytes("UTF-8");        
                                                              //转为byte类型

            t.sendResponseHeaders(200, response.length);      //发送响应码Code
            
            OutputStream os = t.getResponseBody();            //返回上传结果写入响应Body
            os.write(response);
            os.close();                                            
			
		}catch (JSONException e){
			t.sendResponseHeaders(400, -1);     //JSON对象格式错误
			return;
		}
	}
}
