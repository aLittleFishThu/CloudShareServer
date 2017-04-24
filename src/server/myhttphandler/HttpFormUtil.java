package server.myhttphandler;

import java.util.List;

import com.sun.net.httpserver.Headers;

public class HttpFormUtil {
	/**
	 * 判断Content-Type格式是否正确
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
	 * 从请求头中获取sessionID
	 * @param requestHeader
	 * @return sessionID
	 */
	public static String getCookie(Headers requestHeader){
		if (!requestHeader.containsKey("Cookie")){  //若Head中不包含Cookie，则返回403
			return null;
		}		
		List<String> cookieList=requestHeader.get("Cookie"); //获取Cookie列表
		String sessionID=null;
		for (String aCookie:cookieList){                    //遍历Cookie列表
			if (aCookie.startsWith("sessionID=")){
				sessionID=aCookie.substring(10);            //获取sessionID
				break;
			}
		}
		return sessionID;
	}
}
