package server;

import common.Credential;
/**
 * Session管理层对外提供的接口
 * @author yzj
 *
 */
public interface ISession {
	/**
	 * 根据sessionID匹配用户名
	 * @param sessionID
	 * @return userID
	 */
	public String getUserID(String sessionID);  
	/**
	 * 登录用户创建新session，并返回session_id
	 * @param cred
	 * @return sessionID
	 */
	public String createSession(Credential cred);  
}
