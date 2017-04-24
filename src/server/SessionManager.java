package server;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map.Entry;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

import org.json.JSONException;
import org.json.JSONObject;

import com.sun.net.httpserver.Headers;
import com.sun.net.httpserver.HttpExchange;

import common.ChangePasswdResult;
import common.Convert;
import common.Credential;
/**
 * �ӿ�ISession��ʵ����
 * @author yzj
 * ά��session�б�
 * ��Net���ṩuserID
 */
public class SessionManager implements ISession{
	/**
	 * session�б�
	 */
	private HashMap<String,String> sessionSheet=new HashMap<String,String>();
	private ReadWriteLock rwlock=new ReentrantReadWriteLock(); //��д��
	
	@Override
	/**
	 * ����session_idȡ��userID
	 * @author yzj
	 * @param sessionID
	 * @return userID or null(session_id������)
	 */
	public String getUserID(String sessionID) {
		rwlock.readLock().lock();   //�϶���
		if (sessionSheet.containsKey(sessionID)){
			rwlock.readLock().unlock();
			return sessionSheet.get(sessionID);
		}
		else {
			rwlock.readLock().unlock();
			return null;
		}
	}

	@Override
	public String createSession(Credential cred) {
		String userID=cred.getUserID();
		String sessionID=null;
		
		//��д��
		rwlock.writeLock().lock();
		//��֤һ��userIDֻ��Ӧһ��sessionID
		Iterator<Entry<String,String>> iter = sessionSheet.entrySet().iterator();
		while (iter.hasNext()){
			Entry<String,String> entry=iter.next();
			if (entry.getValue().equals(userID))
				sessionSheet.remove(entry.getKey());
		}
		//����sessionID
		do{
			sessionID=SessionIDGenerator.nextSessionId();
		}while (sessionSheet.containsKey(sessionID));
		//��<sessionID,userID>�����ϣ���в�����sessionIDֵ
		sessionSheet.put(sessionID, userID);
		rwlock.writeLock().unlock();
		return sessionID;
	}
}
