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
		Headers requestHeader=t.getRequestHeaders();   	//获取Header
		/**
		 * 以下进行Cookie判断
		 */
		if (requestHeader.containsKey("Cookie")){  //若Head中不包含Cookie，则返回403
			System.out.println("OK");
		}
		else{
			Headers h=t.getResponseHeaders();                   //设置响应头Header
			h.add("Set-Cookie","sessionID=123");           //Content-Type加入响应头
			String response="AAA";
			t.sendResponseHeaders(200, response.length());    //发送响应码Code
			
			OutputStream os = t.getResponseBody();            //返回修改写入响应Body
            os.write(response.getBytes());
            os.close();         
		}
	}
}