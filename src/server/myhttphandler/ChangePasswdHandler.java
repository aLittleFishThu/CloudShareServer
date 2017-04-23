package server.myhttphandler;

import java.util.List;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import org.json.JSONException;
import org.json.JSONObject;

import server.IBusinessLogic;
import server.ISession;

import com.sun.net.httpserver.Headers;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import common.Credential;
import common.Convert;
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
		 * ���½���Method�ж�
		 */
		String requestMethod=t.getRequestMethod();  //��ȡMethod
		if (!requestMethod.equals("POST")){         //�ж�Method�Ƿ���ȷ
			t.sendResponseHeaders(400, -1);         
			return;
		}
		/**
		 * ���½���Content-Type�ж�
		 */
		Headers requestHeader=t.getRequestHeaders();   	//��ȡHeader
		if (requestHeader.containsKey("Content-Type")){ //��Header�а���Content-Type��Ϣ���ж��Ƿ�Ϊjson����
			if (!requestHeader.getFirst("Content-Type").contains("application/json")){
				t.sendResponseHeaders(400, -1);
				return;
			}
		}
		/**
		 * ���½���Cookie�ж�
		 */
		if (!requestHeader.containsKey("Cookie")){  //��Head�в�����Cookie���򷵻�403
			t.sendResponseHeaders(403, -1);
			return;
		}		
		List<String> cookieList=requestHeader.get("Cookie"); //��ȡCookie�б�
		String sessionID=null;
		for (String aCookie:cookieList){                    //����Cookie�б�
			if (aCookie.startsWith("sessionID=")){
				sessionID=aCookie.substring(10);            //��ȡsessionID
				break;
			}
		}
		if (sessionID==null){                               //��Cookie����û��sessionID����403
			t.sendResponseHeaders(403, -1);
			return;
		}
		String userID=m_Session.getUserID(sessionID);       //����sessionIDȡ��userID
		if (userID==null){									//sessionID��Ч�򷵻�403
			t.sendResponseHeaders(403, -1);
			return;
		}
		
		/**
		 * ���½���Body������������Ӧ����
		 */
		InputStream in=t.getRequestBody();                      //��ȡBody
		String requestBody=Convert.toString(in); 
		in.close();				
		
		JSONObject jsonRequest;									//����Body
		try{
			jsonRequest=new JSONObject(requestBody);
			String password=jsonRequest.getString("password");  
			String newpassword=jsonRequest.getString("newPassword");
			
			Credential cred=new Credential(userID,password);    //��JSON����ת��Ϊjava����
			ChangePasswdResult result=m_Business.changePasswd(cred, newpassword);   
																//�����޸��������
			
			Headers h=t.getResponseHeaders();                   //������ӦͷHeader
			h.add("Content-Type","application/json");           //Content-Type������Ӧͷ
			
			JSONObject jsonResponse=new JSONObject();     
			jsonResponse.put("status", result.getStatus());   //���޸Ľ����װΪJSON����
			String response=jsonResponse.toString();          //תΪ�ַ���
			
			t.sendResponseHeaders(200, response.length());    //������Ӧ��Code
			
			OutputStream os = t.getResponseBody();            //�����޸�д����ӦBody
            os.write(response.getBytes());
            os.close();                                       
            
		}catch (JSONException e){
			t.sendResponseHeaders(400, -1);     //JSON�����ʽ����
			return;
		}
	}
}