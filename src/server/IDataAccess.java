package server;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
//import java.util.HashSet;

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
	/**
	 * 获取文件列表
	 * @return fileSheet
	 */
	public HashMap<String,CloudFile> getFileSheet();
	/**
	 * 存储文件列表
	 */
	public void storeFileSheet(HashMap<String,CloudFile> fileSheet);
/*	*//**
	 * 获取用户文件库
	 * @return fileSheet
	 *//*
	public HashMap<String,HashSet<CloudFile>> getUserFileSheet();
	*//**
	 * 存储用户文件库
	 *//*
	public void storeUserFileSheet(HashMap<String,HashSet<CloudFile>> userFileSheet);*/
	/**
	 * 上传文件
	 */
	public void uploadFile(String fileID,InputStream in);
	/**
	 * 删除文件
	 */
	public boolean deleteFile(String fileID);
	/**
	 * 下载文件
	 * @throws FileNotFoundException 
	 * @throws IOException 
	 */
	public byte [] downloadFile(String fileID) throws FileNotFoundException, IOException;
}
