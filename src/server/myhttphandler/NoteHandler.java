package server.myhttphandler;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URI;

import org.apache.commons.io.IOUtils;
import org.json.JSONException;
import org.json.JSONObject;

import server.IBusinessLogic;
import server.ISession;

import com.sun.net.httpserver.Headers;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import common.Note;
import common.NoteResult;

public class NoteHandler implements HttpHandler{
	private final IBusinessLogic m_Business;
	private final ISession m_Session;
	
	public NoteHandler(IBusinessLogic business,ISession session){
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
		 * ���½���Method�жϣ���һ���ַ����񣬻��NoteResult
		 */
		String requestMethod=t.getRequestMethod();     //��ȡMethod
		if (requestMethod.equals("POST")){ 
		    handleAdd(t,userID);
		}
		else if (requestMethod.equals("DELETE")){
		    handleDelete(t,userID);
		}
		else{
		    t.sendResponseHeaders(400, -1);
		    return;
		}
	}
	
	private void handleAdd(HttpExchange t,String userID) throws IOException{
		/**
		 * ����Note�����ýӿ�
		 */
        InputStream in=t.getRequestBody();                      //��ȡBody
        String requestBody=IOUtils.toString(in,"UTF-8"); 
        in.close();             
        
        JSONObject jsonRequest;                                 //����Body
        try{
            jsonRequest=new JSONObject(requestBody);
            String content=jsonRequest.getString("content");    //��ȡ��Ϣ
            String fileID=jsonRequest.getString("fileID"); 
            
            Note note=new Note(content,fileID,userID);          //��JSON����ת��Ϊjava����
            NoteResult result=m_Business.addNote(note);         //�������ӱ�ע����                                                
            
            Headers h=t.getResponseHeaders();                   //������ӦͷHeader
            h.add("Content-Type","application/json");           //Content-Type������Ӧͷ
            
            JSONObject jsonResponse=new JSONObject();     
            jsonResponse.put("status", result.getStatus());     //���޸Ľ����װΪJSON����
            byte[] response=jsonResponse.toString().getBytes("UTF-8");   
                                                                //תΪbyte����
            
            
            t.sendResponseHeaders(200, response.length);        //������Ӧ��Code
            
            OutputStream os = t.getResponseBody();              //���ؽ��д����ӦBody
            os.write(response);
            os.close();                                       
            
        }catch (JSONException e){
            t.sendResponseHeaders(400, -1);     //JSON�����ʽ����
            return;
        }
    }
	
	
	private void handleDelete(HttpExchange t,String userID) throws IOException{
        /**
         * ��ȡfileID��noteID�����ýӿ�
         */
        URI uri=t.getRequestURI();                      //��ȡURI
        String fileID=HttpFormUtil.getQueryParameter(uri, "fileID");
        String noteID=HttpFormUtil.getQueryParameter(uri, "noteID");
        if ((fileID==null)||(noteID==null)){            //��ȡ����
            t.sendResponseHeaders(400, -1);
            return;
        }
        Note note=new Note();
        note.setNoteID(noteID);
        note.setFileID(fileID);
        NoteResult result=m_Business.deleteNote(note,userID);
                                                        //����ɾ����ע����     
                                                        
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
}
