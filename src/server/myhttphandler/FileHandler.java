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
		 * ȡ���Ự���ж�Cookie
		 */
		Headers requestHeader=t.getRequestHeaders();   	//��ȡHeader
		/*String sessionID=HttpFormUtil.getCookie(requestHeader);
		String userID=m_Session.getUserID(sessionID);   //����sessionIDȡ��userID
		if (userID==null){								//sessionID��Ч�򷵻�403
			t.sendResponseHeaders(403, -1);
			return;
		}*/
		String userID="yzj";
		/**
		 * ���½���Method�жϣ���һ���ַ����񣬻��FileResult
		 */
		String requestMethod=t.getRequestMethod();     //��ȡMethod
		FileResult result=null;
		if (requestMethod.equals("POST")){         	
			if (HttpFormUtil.judgeContentType(requestHeader, "application/json"))
				result=handleRename(t,userID);			//POST+JSON: Rename
			else 
				result=handleUpload(t,userID);			//POST+file��upload
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
		 * ������Ӧ��Ϣ
		 */
		if (result==null){
			t.sendResponseHeaders(400, -1);
			return;
		}
		Headers h=t.getResponseHeaders();               //������ӦͷHeader
		h.add("Content-Type","application/json");       //Content-Type������Ӧͷ
		
		JSONObject jsonResponse=new JSONObject();     
		jsonResponse.put("status", result.getStatus()); //���޸Ľ����װΪJSON����
		String response=jsonResponse.toString();        //תΪ�ַ���
		
		t.sendResponseHeaders(200, response.length());  //������Ӧ��Code
		
		OutputStream os = t.getResponseBody();          //�����ϴ����д����ӦBody
		os.write(response.getBytes());
		os.close();       
			
	}
	private FileResult handleUpload(HttpExchange t,String userID) throws IOException{
		/**
		 * ����ΪCloudFile�������ļ����ݻ�ȡ
		 */
		URI uri=t.getRequestURI();						//��ȡURI
		String filename=HttpFormUtil.getQueryParameter(uri, "filename");
		if (filename==null)  							//��ȡ����
			return null;
	
		CloudFile cloudFile=new CloudFile(userID,filename);
		InputStream content=t.getRequestBody();         //��ȡ�ļ�����
	    
		FileResult result=m_Business.uploadFile(cloudFile, content);   
														//�����ϴ��ļ�����  
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
