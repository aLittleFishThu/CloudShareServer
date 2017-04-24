package server;

import java.io.InputStream;

import common.ChangePasswdResult;
import common.CloudFile;
import common.Credential;
import common.FileResult;
import common.LoginResult;
import common.RegisterResult;

/**
 * 服务器端BLL提供接口
 * @author yzj
 * 登录
 * 注册
 * 修改密码
 */
public interface IBusinessLogic {
	/**
	 * 登录服务接口
	 * @param cred 身份验证
	 * @return 登录结果
	 */
	public LoginResult login(Credential cred);
	/**
	 * 注册服务接口
	 * @param cred 身份验证
	 * @return 注册结果
	 */
	public RegisterResult register(Credential cred);
	/**
	 * 修改密码服务接口
	 * @param cred 
	 * @param newPassword
	 * @return 修改结果
	 */
	public ChangePasswdResult changePasswd(Credential cred, String newPassword);
	/**
	 * 上传文件服务接口
	 * @param cloudFile
	 * @param content
	 * @return 上传结果
	 */
	public FileResult uploadFile(CloudFile cloudFile, InputStream content);
	
}
