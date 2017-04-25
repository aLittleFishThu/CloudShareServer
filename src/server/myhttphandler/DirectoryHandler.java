package server.myhttphandler;

import java.io.IOException;
import java.io.OutputStream;
import java.net.URI;
import java.util.HashSet;

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
		 * ���½���Method�ж�
		 */
		String requestMethod=t.getRequestMethod();  //��ȡMethod
		if (!requestMethod.equals("GET")){         	//�ж�Method�Ƿ���ȷ
			t.sendResponseHeaders(400, -1);         
			return;
		}
		
		/**
		 * ���½���Cookie�ж�
		 */
	/*	Headers requestHeader=t.getRequestHeaders();
		String sessionID=HttpFormUtil.getCookie(requestHeader);
		String userID=m_Session.getUserID(sessionID);       //����sessionIDȡ��userID
		if (userID==null){									//sessionID��Ч�򷵻�403
			t.sendResponseHeaders(403, -1);
			return;
		}*/
		String userID="yzj";
		/**
		 * ���½��в�����ȡ
		 */
		URI uri=t.getRequestURI();						//��ȡURI
		String targetID=HttpFormUtil.getQueryParameter(uri, "targetID");
		if (targetID==null){  							//��ȡ����
			t.sendResponseHeaders(400,-1);				//��ʽ���󷵻�400
			return;
		}
		
		/**
		 * ����BLL��ӿڲ����ؽ��
		 */
		Headers h=t.getResponseHeaders();                 //������ӦͷHeader
		h.add("Content-Type","application/json");         //Content-Type������Ӧͷ
		
														  //���ýӿ�
		FileDirectoryResult directoryResult=m_Business.getDirectory(targetID, userID);
		String status=directoryResult.getResult().getStatus();
		HashSet<CloudFile> directory=directoryResult.getFileDirectory();
		
		JSONArray directoryArray=new JSONArray(directory);
		JSONObject jsonResponse=new JSONObject();     
		jsonResponse.put("status", status);				  //�������װΪJSON����
		jsonResponse.put("directory", directoryArray);   
		String response=jsonResponse.toString();          //תΪ�ַ���
		
		t.sendResponseHeaders(200, response.length());    //������Ӧ��Code
		
		OutputStream os = t.getResponseBody();            //���ؽ��д����ӦBody
        os.write(response.getBytes());
        os.close();               
	}
}