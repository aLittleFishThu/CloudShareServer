package server.myhttphandler;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URI;

import org.json.JSONObject;

import server.IBusinessLogic;
import server.ISession;

import com.sun.net.httpserver.Headers;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import common.CloudFile;
import common.DeleteFileResult;
import common.DownloadFileResult;
import common.DownloadFileResult.DownloadFileStatus;
import common.UploadFileResult;

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
		if (userID==null){								//sessionID��Ч�򷵻�401
			t.sendResponseHeaders(401, -1);
			return;
		}*/
		String userID="yzj";
		/**
		 * ���½���Method�жϣ���һ���ַ����񣬻��FileResult
		 */
		String requestMethod=t.getRequestMethod();     //��ȡMethod
		if (requestMethod.equals("POST")){         	
			if (HttpFormUtil.judgeContentType(requestHeader, "application/json"))
				handleRename(t,userID);					//POST+JSON: Rename
			else 
				handleUpload(t,userID);					//POST+file��upload
		}
		else if (requestMethod.equals("GET")){			//GET:download
			handleDownload(t,userID);
		}
		else if (requestMethod.equals("DELETE")){		//DELETE:download
			handleDelete(t,userID);
		}
		else{											//format error
			t.sendResponseHeaders(400, -1);
			return;
		}
	}
	private void handleUpload(HttpExchange t,String userID) throws IOException{
		/**
		 * ����CloudFile����ȡ�ļ����ݣ����ýӿ�
		 */
		URI uri=t.getRequestURI();						//��ȡURI
		String filename=HttpFormUtil.getQueryParameter(uri, "filename");
		if (filename==null){  							//��ȡ����
			t.sendResponseHeaders(400, -1);
			return;
		}
		CloudFile cloudFile=new CloudFile(userID,filename);
		InputStream content=t.getRequestBody();         //��ȡ�ļ�����
		UploadFileResult result=m_Business.uploadFile(cloudFile, content);   
														//�����ϴ��ļ�����
		
		/**
		 * ������Ӧ��Ϣ
		 */
		Headers h=t.getResponseHeaders();               //������ӦͷHeader
		h.add("Content-Type","application/json");       //Content-Type������Ӧͷ
		
		JSONObject jsonResponse=new JSONObject();     
		jsonResponse.put("status", result.getStatus()); //���޸Ľ����װΪJSON����
		byte[] response=jsonResponse.toString().getBytes("UTF-8");        
		                                                //תΪbyte����
		
		t.sendResponseHeaders(200, response.length);      //������Ӧ��Code
		
		OutputStream os = t.getResponseBody();          //�����ϴ����д����ӦBody
		os.write(response);
		os.close();       
	}
	
	private void handleDownload(HttpExchange t,String userID) throws IOException{
		/**
		 * ����CloudFile����ȡ�ļ����ݣ����ýӿ�
		 */
		URI uri=t.getRequestURI();						//��ȡURI
		String fileID=HttpFormUtil.getQueryParameter(uri, "fileID");
		if (fileID==null){  							//��ȡ����
			t.sendResponseHeaders(400, -1);
			return;
		}
		DownloadFileResult result=m_Business.downloadFile(fileID,userID);   
														//���������ļ�����
		
		/**
		 * ������Ӧ��Ϣ
		 */
		if (result.getResult().equals(DownloadFileStatus.wrong))//��Դ�����ڣ�����403
			t.sendResponseHeaders(403, -1);
		t.sendResponseHeaders(200, 0);					//������Ӧ��
	    byte[] content=result.getContent();
		OutputStream os = t.getResponseBody();          //�����ϴ����д����ӦBody
	    os.write(content);
	    os.close();  
	}
	
	private void handleDelete(HttpExchange t,String userID) throws IOException{
		/**
		 * ��ȡfileID�����ýӿ�
		 */
		URI uri=t.getRequestURI();						//��ȡURI
		String fileID=HttpFormUtil.getQueryParameter(uri, "fileID");
		if (fileID==null){  							//��ȡ����
			t.sendResponseHeaders(400, -1);
			return;
		}
		DeleteFileResult result=m_Business.deleteFile(fileID,userID);
														//����ɾ���ļ�����     
														
		/**
		 * ������Ӧ��Ϣ
		 */
		Headers h=t.getResponseHeaders();               //������ӦͷHeader
		h.add("Content-Type","application/json");       //Content-Type������Ӧͷ
		
		JSONObject jsonResponse=new JSONObject();     
		jsonResponse.put("status", result.getStatus()); //��ɾ�������װΪJSON����
        byte[] response=jsonResponse.toString().getBytes("UTF-8");        
                                                        //תΪbyte����

        t.sendResponseHeaders(200, response.length);     //������Ӧ��Code
        
        OutputStream os = t.getResponseBody();          //�����ϴ����д����ӦBody
        os.write(response);
        os.close();       		
	}
	
	private void handleRename(HttpExchange t,String userID){

	}
}
