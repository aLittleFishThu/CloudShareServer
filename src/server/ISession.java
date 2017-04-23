package server;

import common.Credential;
/**
 * Session���������ṩ�Ľӿ�
 * @author yzj
 *
 */
public interface ISession {
	/**
	 * ����sessionIDƥ���û���
	 * @param sessionID
	 * @return userID
	 */
	public String getUserID(String sessionID);  
	/**
	 * ��¼�û�������session��������session_id
	 * @param cred
	 * @return sessionID
	 */
	public String createSession(Credential cred);  
}
