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
 * ����������ӦRegisterҪ��
 * @author yzj
 */
public class RegisterHandler implements HttpHandler{
	private final IBusinessLogic m_Business;

	public RegisterHandler(IBusinessLogic business){
		m_Business=business;
	}
	
	@Override
	public void handle(HttpExchange t) throws IOException {
		String requestMethod=t.getRequestMethod();  //��ȡMethod
		if (!requestMethod.equals("POST")){         //�ж�Method�Ƿ���ȷ
			t.sendResponseHeaders(400, -1);         
			return;
		}
		System.out.println(requestMethod);         
		
		Headers requestHeader=t.getRequestHeaders();   	//��ȡHeader
		if (requestHeader.containsKey("Content-Type")){ //��Header�а���Content-Type��Ϣ���ж��Ƿ�Ϊjson����
			if (!requestHeader.getFirst("Content-Type").contains("application/json")){
				t.sendResponseHeaders(400, -1);
				return;
			}
		}
		System.out.println(requestHeader);
		
		InputStream in=t.getRequestBody();                      //��ȡBody
		String requestBody=IOUtils.toString(in,"UTF-8"); 
		in.close();				
		System.out.println(requestBody);
		
		JSONObject jsonRequest;									//����Body
		try{
			jsonRequest=new JSONObject(requestBody);
			String userID=jsonRequest.getString("userID");
			String password=jsonRequest.getString("password");  
			Credential cred=new Credential(userID,password);    //��JSON����ת��Ϊjava����
			RegisterResult result=m_Business.register(cred);    //����ע����֤
			
			Headers h=t.getResponseHeaders();                   //������ӦͷHeader
			h.add("Content-Type","application/json");           //Content-Type������Ӧͷ
			
			JSONObject jsonResponse=new JSONObject();     
			jsonResponse.put("status", result.getStatus());   //����¼�����װΪJSON����
			byte[] response=jsonResponse.toString().getBytes("UTF-8");  //תΪbyte����
			
			t.sendResponseHeaders(200, response.length);     //������Ӧ��Code
			
			OutputStream os = t.getResponseBody();            //����ע��״̬д����ӦBody
            os.write(response);
            os.close();              
		}catch (JSONException e){
			t.sendResponseHeaders(400, -1);     //JSON�����ʽ����
			return;
		}
	}
}
