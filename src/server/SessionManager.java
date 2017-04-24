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
 * 接口ISession的实现类
 * @author yzj
 * 维护session列表
 * 向Net层提供userID
 */
public class SessionManager implements ISession{
	/**
	 * session列表
	 */
	private HashMap<String,String> sessionSheet=new HashMap<String,String>();
	private ReadWriteLock rwlock=new ReentrantReadWriteLock(); //读写锁
	
	@Override
	/**
	 * 根据session_id取出userID
	 * @author yzj
	 * @param sessionID
	 * @return userID or null(session_id不存在)
	 */
	public String getUserID(String sessionID) {
		rwlock.readLock().lock();   //上读锁
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
		
		//上写锁
		rwlock.writeLock().lock();
		//保证一个userID只对应一个sessionID
		Iterator<Entry<String,String>> iter = sessionSheet.entrySet().iterator();
		while (iter.hasNext()){
			Entry<String,String> entry=iter.next();
			if (entry.getValue().equals(userID))
				sessionSheet.remove(entry.getKey());
		}
		//生成sessionID
		do{
			sessionID=SessionIDGenerator.nextSessionId();
		}while (sessionSheet.containsKey(sessionID));
		//将<sessionID,userID>放入哈希表中并返回sessionID值
		sessionSheet.put(sessionID, userID);
		rwlock.writeLock().unlock();
		return sessionID;
	}
}
