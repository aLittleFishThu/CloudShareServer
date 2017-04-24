package server.myhttphandler;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URI;

import org.json.JSONException;
import org.json.JSONObject;

import server.IBusinessLogic;
import server.ISession;

import com.sun.net.httpserver.Headers;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import common.CloudFile;
import common.FileResult;

public class FileHandler implements HttpHandler{
	private final IBusinessLogic m_Business;
	private final ISession m_Session;
	
	public FileHandler(IBusinessLogic business,ISession session){
		m_Business=business;
		m_Session=session;
	}

	public void handle(HttpExchange t) throws IOException {
		
		/**
		 * 取出会话，判断Cookie
		 */
		Headers requestHeader=t.getRequestHeaders();   	//获取Header
		/*String sessionID=HttpFormUtil.getCookie(requestHeader);
		String userID=m_Session.getUserID(sessionID);   //根据sessionID取出userID
		if (userID==null){								//sessionID无效则返回403
			t.sendResponseHeaders(403, -1);
			return;
		}*/
		String userID="yzj";
		/**
		 * 以下进行Method判断，进一步分发任务，获得FileResult
		 */
		String requestMethod=t.getRequestMethod();     //获取Method
		FileResult result=null;
		if (requestMethod.equals("POST")){         	
			if (HttpFormUtil.judgeContentType(requestHeader, "application/json"))
				result=handleRename(t,userID);			//POST+JSON: Rename
			else 
				result=handleUpload(t,userID);			//POST+file：upload
		}
		else if (requestMethod.equals("GET")){			//GET:download
			result=handleDownload(t,userID);
		}
		else if (requestMethod.equals("DELETE")){		//DELETE:download
			result=handleDelete(t,userID);
		}
		else{											//format error
			t.sendResponseHeaders(400, -1);
			return;
		}
		
		/**
		 * 发送响应信息
		 */
		if (result==null){
			t.sendResponseHeaders(400, -1);
			return;
		}
		Headers h=t.getResponseHeaders();               //设置响应头Header
		h.add("Content-Type","application/json");       //Content-Type加入响应头
		
		JSONObject jsonResponse=new JSONObject();     
		jsonResponse.put("status", result.getStatus()); //将修改结果包装为JSON对象
		String response=jsonResponse.toString();        //转为字符串
		
		t.sendResponseHeaders(200, response.length());  //发送响应码Code
		
		OutputStream os = t.getResponseBody();          //返回上传结果写入响应Body
		os.write(response.getBytes());
		os.close();       
			
	}
	private FileResult handleUpload(HttpExchange t,String userID) throws IOException{
		/**
		 * 以下为CloudFile建立及文件内容获取
		 */
		URI uri=t.getRequestURI();						//获取URI
		String filename=HttpFormUtil.getQueryParameter(uri, "filename");
		if (filename==null)  							//获取参数
			return null;
	
		CloudFile cloudFile=new CloudFile(userID,filename);
		InputStream content=t.getRequestBody();         //获取文件内容
	    
		FileResult result=m_Business.uploadFile(cloudFile, content);   
														//进行上传文件操作  
		return result;
	}
	
	private FileResult handleDownload(HttpExchange t,String userID){
		return null;
	}
	
	private FileResult handleDelete(HttpExchange t,String userID){
		return null;
	}
	
	private FileResult handleRename(HttpExchange t,String userID){
		return null;
	}
}
