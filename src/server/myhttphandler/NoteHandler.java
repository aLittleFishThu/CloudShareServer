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

import common.AddNoteResult;
import common.DeleteNoteResult;
import common.Note;

public class NoteHandler implements HttpHandler{
	private final IBusinessLogic m_Business;
	private final ISession m_Session;
	
	public NoteHandler(IBusinessLogic business,ISession session){
		m_Business=business;
		m_Session=session;
	}

	public void handle(HttpExchange t) throws IOException {
		/**
		 * 取出会话，判断Cookie
		 */
		Headers requestHeader=t.getRequestHeaders();   	//获取Header
		String sessionID=HttpFormUtil.getCookie(requestHeader);
		String userID=m_Session.getUserID(sessionID);   //根据sessionID取出userID
		if (userID==null){								//sessionID无效则返回401
			t.sendResponseHeaders(401, -1);
			return;
		}
		//String userID="yzj";
		/**
		 * 以下进行Method判断，进一步分发任务，获得NoteResult
		 */
		String requestMethod=t.getRequestMethod();     //获取Method
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
		 * 建立Note，调用接口
		 */
        InputStream in=t.getRequestBody();                      //获取Body
        String requestBody=IOUtils.toString(in,"UTF-8"); 
        in.close();             
        
        JSONObject jsonRequest;                                 //解析Body
        try{
            jsonRequest=new JSONObject(requestBody);
            String content=jsonRequest.getString("content");    //获取信息
            String fileID=jsonRequest.getString("fileID"); 
            
            Note note=new Note(content,fileID,userID);          //将JSON对象转换为java对象
            AddNoteResult result=m_Business.addNote(note);         //进行增加备注操作                                                
            
            Headers h=t.getResponseHeaders();                   //设置响应头Header
            h.add("Content-Type","application/json");           //Content-Type加入响应头
            
            JSONObject jsonResponse=new JSONObject();     
            jsonResponse.put("status", result.getStatus());     //将修改结果包装为JSON对象
            byte[] response=jsonResponse.toString().getBytes("UTF-8");   
                                                                //转为byte类型
            
            
            t.sendResponseHeaders(200, response.length);        //发送响应码Code
            
            OutputStream os = t.getResponseBody();              //返回结果写入响应Body
            os.write(response);
            os.close();                                       
            
        }catch (JSONException e){
            t.sendResponseHeaders(400, -1);     //JSON对象格式错误
            return;
        }
    }
	
	
	private void handleDelete(HttpExchange t,String userID) throws IOException{
        /**
         * 获取fileID和noteID，调用接口
         */
        URI uri=t.getRequestURI();                      //获取URI
        String fileID=HttpFormUtil.getQueryParameter(uri, "fileID");
        String noteID=HttpFormUtil.getQueryParameter(uri, "noteID");
        if ((fileID==null)||(noteID==null)){            //获取参数
            t.sendResponseHeaders(400, -1);
            return;
        }
        Note note=new Note();
        note.setNoteID(noteID);
        note.setFileID(fileID);
        DeleteNoteResult result=m_Business.deleteNote(note,userID);
                                                        //进行删除备注操作     
                                                        
        /**
         * 发送响应信息
         */
        Headers h=t.getResponseHeaders();               //设置响应头Header
        h.add("Content-Type","application/json");       //Content-Type加入响应头
        
        JSONObject jsonResponse=new JSONObject();     
        jsonResponse.put("status", result.getStatus()); //将删除结果包装为JSON对象
        byte[] response=jsonResponse.toString().getBytes("UTF-8");        
                                                        //转为byte类型

        t.sendResponseHeaders(200, response.length);     //发送响应码Code
        
        OutputStream os = t.getResponseBody();          //返回上传结果写入响应Body
        os.write(response);
        os.close();         	
	}
}
