package server.myhttphandler;

import java.util.List;

import com.sun.net.httpserver.Headers;

public class HttpFormUtil {
	/**
	 * �ж�Content-Type��ʽ�Ƿ���ȷ
	 * @param requestHeader
	 * @param contentType
	 * @return true or false
	 */
	public static boolean judgeContentType(Headers requestHeader,String contentType){
		if (requestHeader.containsKey("Content-Type")){ 
			if (!requestHeader.getFirst("Content-Type").contains(contentType)){
				return false;
			}
			else 
				return true;
		}
		else
			return true;
	}
	
	/**
	 * ������ͷ�л�ȡsessionID
	 * @param requestHeader
	 * @return sessionID
	 */
	public static String getCookie(Headers requestHeader){
		if (!requestHeader.containsKey("Cookie")){  //��Head�в�����Cookie���򷵻�403
			return null;
		}		
		List<String> cookieList=requestHeader.get("Cookie"); //��ȡCookie�б�
		String sessionID=null;
		for (String aCookie:cookieList){                    //����Cookie�б�
			if (aCookie.startsWith("sessionID=")){
				sessionID=aCookie.substring(10);            //��ȡsessionID
				break;
			}
		}
		return sessionID;
	}
}
