package server;

import java.util.HashMap;
import common.*;

/**
 * 服务器端DAL层提供接口
 * @author yzj
 * 登录
 * 注册
 * 修改密码
 */
public interface IDataAccess {
	/**
	 * 获取用户列表
	 * @return 用户列表（哈希表）
	 */
	public HashMap<String,User> getUserSheet();
	/**
	 * 存储用户列表
	 * @param userSheet
	 */
	public void storeUserSheet(HashMap<String,User> userSheet);
}
