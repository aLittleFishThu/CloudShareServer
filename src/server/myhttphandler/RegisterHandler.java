package server.myhttphandler;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import org.apache.commons.io.IOUtils;
import org.json.JSONException;
import org.json.JSONObject;

import server.IBusinessLogic;

import com.sun.net.httpserver.Headers;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import common.Credential;
import common.RegisterResult;

/**
 * 服务器端响应Register要求
 * @author yzj
 */
public class RegisterHandler implements HttpHandler{
	private final IBusinessLogic m_Business;

	public RegisterHandler(IBusinessLogic business){
		m_Business=business;
	}
	
	@Override
	public void handle(HttpExchange t) throws IOException {
		String requestMethod=t.getRequestMethod();  //获取Method
		if (!requestMethod.equals("POST")){         //判断Method是否正确
			t.sendResponseHeaders(400, -1);         
			return;
		}
		System.out.println(requestMethod);         
		
		Headers requestHeader=t.getRequestHeaders();   	//获取Header
		if (requestHeader.containsKey("Content-Type")){ //若Header中包含Content-Type信息，判断是否为json类型
			if (!requestHeader.getFirst("Content-Type").contains("application/json")){
				t.sendResponseHeaders(400, -1);
				return;
			}
		}
		System.out.println(requestHeader);
		
		InputStream in=t.getRequestBody();                      //获取Body
		String requestBody=IOUtils.toString(in,"UTF-8"); 
		in.close();				
		System.out.println(requestBody);
		
		JSONObject jsonRequest;									//解析Body
		try{
			jsonRequest=new JSONObject(requestBody);
			String userID=jsonRequest.getString("userID");
			String password=jsonRequest.getString("password");  
			Credential cred=new Credential(userID,password);    //将JSON对象转换为java对象
			RegisterResult result=m_Business.register(cred);    //进行注册验证
			System.out.println(result);
			
			Headers h=t.getResponseHeaders();                   //设置响应头Header
			h.add("Content-Type","application/json");           //Content-Type加入响应头
			
			System.out.println(result+"2");
			
			JSONObject jsonResponse=new JSONObject();     
			jsonResponse.put("status", result.getStatus());   //将登录结果包装为JSON对象
			String response=jsonResponse.toString();          //转为字符串
			
			System.out.println(result+"3");
			t.sendResponseHeaders(200, response.length());    //发送响应码Code
			
			OutputStream os = t.getResponseBody();            //返回注册状态写入响应Body
			System.out.println(result+"4");
            os.write(response.getBytes());
            os.close();              
            
            System.out.println("12");
		}catch (JSONException e){
			t.sendResponseHeaders(400, -1);     //JSON对象格式错误
			return;
		}
	}
}
