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
 * ����������ӦLoginҪ��
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
		String requestMethod=t.getRequestMethod();  //��ȡMethod
		if (!requestMethod.equals("POST")){         //�ж�Method�Ƿ���ȷ
			t.sendResponseHeaders(400, -1);         
			return;
		}
		
		Headers requestHeader=t.getRequestHeaders();   	//��ȡHeader
		if (!HttpFormUtil.judgeContentType(requestHeader, "application/json")){ 
			t.sendResponseHeaders(400, -1);
			return;
		}
		
		InputStream in=t.getRequestBody();                      //��ȡBody
		String requestBody=IOUtils.toString(in,"UTF-8"); 
		in.close();				
		
		JSONObject jsonRequest;									//����Body
		try{
			jsonRequest=new JSONObject(requestBody);
			String userID=jsonRequest.getString("userID");
			String password=jsonRequest.getString("password");  
			Credential cred=new Credential(userID,password);    //��JSON����ת��Ϊjava����
			LoginResult result=m_Business.login(cred);          //���������֤
			
			Headers h=t.getResponseHeaders();                   //������ӦͷHeader
			h.add("Content-Type","application/json");           //Content-Type������Ӧͷ
			if (result.equals(LoginResult.OK)){                 //�������֤ͨ��
				String sessionID=m_Session.createSession(cred); //��ȡsessionID����Ϊcookie
				h.add("Set-Cookie", "sessionID="+sessionID);                     //Cookie������Ӧͷ
			}
			
			JSONObject jsonResponse=new JSONObject();     
			jsonResponse.put("status", result.getStatus());   //����¼�����װΪJSON����
			String response=jsonResponse.toString();          //תΪ�ַ���
			
			t.sendResponseHeaders(200, response.length());    //������Ӧ��Code
			
			OutputStream os = t.getResponseBody();            //���ص�¼״̬д����ӦBody
            os.write(response.getBytes());
            os.close();                                       
			
		}catch (JSONException e){
			t.sendResponseHeaders(400, -1);     //JSON�����ʽ����
			return;
		}
	}
}
