package server.myhttphandler;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;

import server.IBusinessLogic;
import server.ISession;

import com.sun.net.httpserver.Headers;
import com.sun.net.httpserver.HttpExchange;
import common.ChangePasswdResult;
import common.Convert;
import common.Credential;

public class FileHandler {
	private final IBusinessLogic m_Business;
	private final ISession m_Session;
	
	public FileHandler(IBusinessLogic business,ISession session){
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
		if (requestHeader.containsKey("Content-Type")){ //若Header中包含Content-Type信息，判断是否为json类型
			if (!requestHeader.getFirst("Content-Type").contains("application/json")){
				t.sendResponseHeaders(400, -1);
				return;
			}
		}
		/**
		 * 以下进行Cookie判断
		 */
		if (!requestHeader.containsKey("Cookie")){  //若Head中不包含Cookie，则返回403
			t.sendResponseHeaders(403, -1);
			return;
		}		
		List<String> cookieList=requestHeader.get("Cookie"); //获取Cookie列表
		String sessionID=null;
		for (String aCookie:cookieList){                    //遍历Cookie列表
			if (aCookie.startsWith("sessionID=")){
				sessionID=aCookie.substring(10);            //获取sessionID
				break;
			}
		}
		if (sessionID==null){                               //若Cookie里面没有sessionID返回403
			t.sendResponseHeaders(403, -1);
			return;
		}
		String userID=m_Session.getUserID(sessionID);       //根据sessionID取出userID
		if (userID==null){									//sessionID无效则返回403
			t.sendResponseHeaders(403, -1);
			return;
		}
		
		/**
		 * 以下进行Body解析及发送响应内容
		 */
		InputStream in=t.getRequestBody();                      //获取Body
		String requestBody=Convert.toString(in); 
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
