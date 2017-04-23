package server.myhttphandler;

import java.io.IOException;
import java.io.OutputStream;

import com.sun.net.httpserver.Headers;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

public class CPHandler implements HttpHandler{
	/*private final IBusinessLogic m_Business;
	private final ISession m_Session;
	
	public CPHandler(IBusinessLogic business,ISession session){
		m_Business=business;
		m_Session=session;
	}*/

	public void handle(HttpExchange t) throws IOException {
		Headers requestHeader=t.getRequestHeaders();   	//��ȡHeader
		/**
		 * ���½���Cookie�ж�
		 */
		if (requestHeader.containsKey("Cookie")){  //��Head�в�����Cookie���򷵻�403
			System.out.println("OK");
		}
		else{
			Headers h=t.getResponseHeaders();                   //������ӦͷHeader
			h.add("Set-Cookie","sessionID=123");           //Content-Type������Ӧͷ
			String response="AAA";
			t.sendResponseHeaders(200, response.length());    //������Ӧ��Code
			
			OutputStream os = t.getResponseBody();            //�����޸�д����ӦBody
            os.write(response.getBytes());
            os.close();         
		}
	}
}