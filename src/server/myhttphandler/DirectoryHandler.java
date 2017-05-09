package server.myhttphandler;

import java.io.IOException;
import java.io.OutputStream;
import java.net.URI;
import java.util.ArrayList;
import org.json.JSONArray;
import org.json.JSONObject;

import server.IBusinessLogic;
import server.ISession;

import com.sun.net.httpserver.Headers;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import common.CloudFile;
import common.FileDirectoryResult;

public class DirectoryHandler implements HttpHandler{
	private final IBusinessLogic m_Business;
	private final ISession m_Session;
	
	public DirectoryHandler(IBusinessLogic business,ISession session){
		m_Business=business;
		m_Session=session;
	}
	
	@Override
	public void handle(HttpExchange t) throws IOException {
		/**
		 * 以下进行Method判断
		 */
		String requestMethod=t.getRequestMethod();  //获取Method
		if (!requestMethod.equals("GET")){         	//判断Method是否正确
			t.sendResponseHeaders(400, -1);         
			return;
		}
		
		/**
		 * 以下进行Cookie判断
		 */
	/*	Headers requestHeader=t.getRequestHeaders();
		String sessionID=HttpFormUtil.getCookie(requestHeader);
		String userID=m_Session.getUserID(sessionID);       //根据sessionID取出userID
		if (userID==null){									//sessionID无效则返回403
			t.sendResponseHeaders(401, -1);
			return;
		}*/
		String userID="yzj";
		/**
		 * 以下进行参数提取
		 */
		URI uri=t.getRequestURI();						//获取URI
		String targetID=HttpFormUtil.getQueryParameter(uri, "targetID");
		if (targetID==null){  							//获取参数
			t.sendResponseHeaders(400,-1);				//格式错误返回400
			return;
		}
		
		/**
		 * 调用BLL层接口并返回结果
		 */
		Headers h=t.getResponseHeaders();                 //设置响应头Header
		h.add("Content-Type","application/json");         //Content-Type加入响应头
		
														  //调用接口
		FileDirectoryResult directoryResult=m_Business.getDirectory(targetID, userID);
		String status=directoryResult.getResult().getStatus();
		ArrayList<CloudFile> directory=directoryResult.getFileDirectory();
		
		JSONArray directoryArray=new JSONArray(directory);
		JSONObject jsonResponse=new JSONObject();     
		jsonResponse.put("status", status);				  //将结果包装为JSON对象
		jsonResponse.put("directory", directoryArray);   
		byte[] response=jsonResponse.toString().getBytes("UTF-8");
		
		t.sendResponseHeaders(200, response.length);    //发送响应码Code
		
		OutputStream os = t.getResponseBody();            //返回结果写入响应Body
        os.write(response);
        os.close();               
	}
}
