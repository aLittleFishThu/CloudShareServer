package server;

import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.UUID;
import java.util.Map.Entry;

import common.*;

/**
 * 服务器端BLL层
 * @author yzj
 */
public class BusinessLogic implements IBusinessLogic{
	private final IDataAccess m_DataAccess;
	private HashMap<String,User> m_UserSheet=new HashMap<String,User>(); 
	private HashMap<String,CloudFile> m_FileSheet=new HashMap<String,CloudFile>();
	
	
	/**
	 * 构造方法
	 */
	public BusinessLogic(IDataAccess dataAccess ){
		m_DataAccess=dataAccess;
		retrieveData();
		storeData();
	}
    
	private void retrieveData(){
		m_UserSheet=m_DataAccess.getUserSheet();   //开机加载数据
		m_FileSheet=m_DataAccess.getFileSheet();
	}

	//定义匿名内部类，关机自动存储
	private void storeData(){
		Runtime.getRuntime().addShutdownHook(new Thread() {
			@Override
			public void run() { 
				m_DataAccess.storeUserSheet(m_UserSheet); //存储用户列表
				m_DataAccess.storeFileSheet(m_FileSheet); //存储文件列表
				System.out.println("storeOK");
			}
		});
	}
	
	@Override
	/**
	 * 实现BLL层login服务
	 * @param cred
	 * @return 结果
	 */
	public LoginResult login(Credential cred) {
		User user=new User(cred.getUserID(),cred.getPassword());
		String userID=user.getUserID();
		
		if (!m_UserSheet.containsKey(userID))  //用户名错误
			return LoginResult.wrong;
		else if (!(m_UserSheet.get(userID).equals(user)))  //密码错误
			return LoginResult.wrong;
		else 
			return LoginResult.OK;   //成功
	}

	@Override
	/**
	 * 实现BLL层register服务
	 * @param cred
	 * @return 注册结果
	 */
	public RegisterResult register(Credential cred) {
		User user=new User(cred.getUserID(),cred.getPassword());  
		String userID=user.getUserID();
		String password=user.getPassword();
		
		if (!common.Validator.isUserIDLegal(userID)) //用户名非法
			return RegisterResult.illegalID;
		else if (!common.Validator.isPasswdLegal(password))  //密码非法
			return RegisterResult.illegalPassword;
		else if (m_UserSheet.containsKey(userID))   //用户名重复
			return RegisterResult.usedID;  
		else{
			m_UserSheet.put(userID, user);   //加入用户
			return RegisterResult.OK;            //返回结果
		}
	}

	@Override
	/**
	 * 实现BLL层changePasswd服务
	 * @param cred
	 * @param newPassword
	 * @return 修改结果
	 */
	public ChangePasswdResult changePasswd(Credential cred, String newPassword) {
		User user=new User(cred.getUserID(),cred.getPassword());  
		String userID=user.getUserID();
		
		if (!common.Validator.isPasswdLegal(newPassword)) //密码非法
			return ChangePasswdResult.illegalPassword;
		else if (!m_UserSheet.containsKey(userID))     //用户名不存在
			return ChangePasswdResult.wrong;
		else if (!(m_UserSheet.get(userID).equals(user)))  //密码错误
			return ChangePasswdResult.wrong;
		else{
			user.setPassword(newPassword);    //修改成功
			m_UserSheet.put(userID, user);  //放入哈希表中
			return ChangePasswdResult.OK;     //返回成功
		}
	}

	@Override
	/**
	 * 实现BLL层上传文件uploadFile服务
	 * @param cloudFile 
	 * @param InputStream content
	 * 调用DAL接口，并生成fileID，在两个列表里新增内容
	 */
	public FileResult uploadFile(CloudFile cloudFile, InputStream content) {
		String filename=cloudFile.getFilename();        //除去该用户的重名文件
		String creator=cloudFile.getCreator();         
		Iterator<Entry<String,CloudFile>> iter=m_FileSheet.entrySet().iterator();
		while (iter.hasNext()){
			CloudFile aFile=iter.next().getValue();
			if (aFile.getFilename().equals(filename)&&
					aFile.getCreator().equals(creator)){
				m_DataAccess.deleteFile(aFile.getFileID()); //从磁盘里删除
				iter.remove();								//从表中删除
			}
		}
	
		String fileID;									//设置fileID									
		do{
			fileID=UUID.randomUUID().toString();
		}while (m_FileSheet.containsKey(fileID));  
		cloudFile.setFileID(fileID);
		
		m_DataAccess.uploadFile(cloudFile, content); 	//调用接口，存储文件到磁盘
		
		SimpleDateFormat df = 							//设置uploadTime
				new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String uploadTime=df.format(new Date());
		cloudFile.setUploadTime(uploadTime);
		
		m_FileSheet.put(fileID, cloudFile);				//加入文件列表
		return FileResult.OK;							//返回上传结果
	}

	
	@Override
	/**
	 * 实现BLL层获取指定用户目录的接口服务
	 * @param targetID 目标用户
	 * @param userID   当前用户
	 * @return 获取结果
	 */
	public FileDirectoryResult getDirectory(String targetID, String userID) {
		if (!m_UserSheet.containsKey(targetID))				//目标用户不存在直接返回wrong
			return new FileDirectoryResult(FileResult.wrong);
		
		HashSet<CloudFile> fileDirectory=new HashSet<CloudFile>(); //遍历文件列表
		Iterator<Entry<String,CloudFile>> iter=m_FileSheet.entrySet().iterator();
		while (iter.hasNext()){
			CloudFile aFile=iter.next().getValue();
			if (aFile.getCreator().equals(targetID)){		//找到目标用户的文件
				if ((targetID.equals(userID))||				//若为自己文件或他人公开文件		
						aFile.getAuthorization().equals(Authorization.Public)){
					fileDirectory.add(aFile);				//加入文件列表
				}
			}
		}
		return new FileDirectoryResult(fileDirectory,FileResult.OK);  //返回结果给上层
	}

	@Override
	public FileResult deleteFile(String fileID, String userID) {
		CloudFile cloudFile=m_FileSheet.get(fileID);	//检查权限
		if (!cloudFile.getCreator().equals(userID))
			return FileResult.wrong;
		if (!m_DataAccess.deleteFile(fileID))			//检查是否成功删除
			return FileResult.wrong;
		else{											//若成功删除
			m_FileSheet.remove(fileID);					//从文件列表里除去
			return FileResult.OK;						//返回删除成功
		}
	}
}
