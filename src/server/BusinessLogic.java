package server;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.UUID;
import java.util.Map.Entry;

import common.*;
import common.DownloadFileResult.DownloadFileStatus;
import common.FileDirectoryResult.FileDirectoryStatus;

/**
 * 服务器端BLL层
 * @author yzj
 */
public class BusinessLogic implements IBusinessLogic{
	private final IDataAccess m_DataAccess;
	private HashMap<String,User> m_UserSheet=new HashMap<String,User>(); 
	private HashMap<String,CloudFile> m_FileSheet=new HashMap<String,CloudFile>();
	private HashMap<String,HashMap<String,Note>> m_NoteSheet=new HashMap<String,HashMap<String,Note>>();
	
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
		m_NoteSheet=m_DataAccess.getNoteSheet();
	}

	//定义匿名内部类，关机自动存储
	private void storeData(){
		Runtime.getRuntime().addShutdownHook(new Thread() {
			@Override
			public void run() { 
				m_DataAccess.storeUserSheet(m_UserSheet); //存储用户列表
				m_DataAccess.storeFileSheet(m_FileSheet); //存储文件列表
				m_DataAccess.storeNoteSheet(m_NoteSheet); //存储备注列表
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
	public UploadFileResult uploadFile(CloudFile cloudFile, InputStream content) {
		String filename=cloudFile.getFilename();        //替换该用户的重名文件
		String creator=cloudFile.getCreator();         
		Iterator<Entry<String,CloudFile>> iter=m_FileSheet.entrySet().iterator();
		while (iter.hasNext()){
			CloudFile aFile=iter.next().getValue();
			if (aFile.getFilename().equals(filename)&&
					aFile.getCreator().equals(creator)){
			    m_DataAccess.uploadFile(aFile.getFileID(), content);
				return UploadFileResult.OK;
			}
		}
	
		String fileID;									//无重名文件时设置fileID									
		do{
			fileID=UUID.randomUUID().toString();
		}while (m_FileSheet.containsKey(fileID));  
		cloudFile.setFileID(fileID);
		m_DataAccess.uploadFile(fileID, content); 	//调用接口，存储文件到磁盘
		
		SimpleDateFormat df = 							//设置uploadTime
				new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String uploadTime=df.format(new Date());
		cloudFile.setUploadTime(uploadTime);
		
		m_FileSheet.put(fileID, cloudFile);				//加入文件列表
		m_NoteSheet.put(fileID, new HashMap<String,Note>());
		                                                //加入备注列表
		return UploadFileResult.OK;						//返回上传结果
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
			return new FileDirectoryResult(FileDirectoryStatus.wrong);
		
		ArrayList<CloudFile> fileDirectory=new ArrayList<CloudFile>(); //遍历文件列表
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
		return new FileDirectoryResult(fileDirectory,FileDirectoryStatus.OK);  //返回结果给上层
	}

	@Override
	/**
	 * 实现BLL层的删除文件接口服务
	 * @param fileID
	 * @param userID
	 * @return FileResult
	 */
	public DeleteFileResult deleteFile(String fileID, String userID) {
		CloudFile cloudFile=m_FileSheet.get(fileID);	//检查权限
		if (!cloudFile.getCreator().equals(userID))
			return DeleteFileResult.wrong;
		if (!m_DataAccess.deleteFile(fileID))			//检查是否成功删除
			return DeleteFileResult.wrong;
		else{											//若成功删除
			m_FileSheet.remove(fileID);					//从文件列表里除去
			m_NoteSheet.remove(fileID);                 //从备注列表里除去
			return DeleteFileResult.OK;					//返回删除成功
		}
	}

	@Override
	/**
	 * 实现BLL层下载文件的接口服务
	 * @param fileID
	 * @param userID
	 */
	public DownloadFileResult downloadFile(String fileID, String userID) {
		if (!m_FileSheet.containsKey(fileID))			//检查文件是否存在
			return new DownloadFileResult();
		CloudFile cloudFile=m_FileSheet.get(fileID);
		if ((!cloudFile.getCreator().equals(userID))&&	//检查是否有权限
			(cloudFile.getAuthorization().equals(Authorization.Private)))
			return new DownloadFileResult();
		try {
			byte[] content = m_DataAccess.downloadFile(fileID);
			return new DownloadFileResult(content,DownloadFileStatus.OK);
		} catch (FileNotFoundException e) {
			return new DownloadFileResult();
		} catch (IOException e) {
			return new DownloadFileResult();
		}
	}

    @Override
    /**
     * 实现BLL层增加备注服务
     */
    public NoteResult addNote(Note note) {
        String userID=note.getCreator();
        String fileID=note.getFileID();
        if (!m_FileSheet.containsKey(fileID))           //文件不存在返回wrong
            return NoteResult.wrong;            
        
        CloudFile file=m_FileSheet.get(fileID);         //无权限访问文件返回wrong
        if ((!file.getCreator().equals(userID))
                &&(!file.getAuthorization().equals(Authorization.Public)))
            return NoteResult.wrong;
        
        if (!m_NoteSheet.containsKey(fileID))           //防止noteSheet里没有fileID
            m_NoteSheet.put(fileID, new HashMap<String,Note>());
        
        String noteID;                                  //设置noteID                                  
        do{
            noteID=UUID.randomUUID().toString();
        }while (m_NoteSheet.get(fileID).containsKey(noteID));
        
              
        SimpleDateFormat df =                           //设置uploadTime
                new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String uploadTime=df.format(new Date());
        note.setUploadTime(uploadTime);
        
        m_NoteSheet.get(fileID).put(noteID,note);       //存入备注列表
        return NoteResult.OK;
    }

    @Override
    public NoteResult deleteNote(Note note, String userID) {
        String fileID=note.getFileID();
        String noteID=note.getNoteID();
        
        if (!m_FileSheet.containsKey(fileID))           //文件不存在返回wrong
            return NoteResult.wrong;            
        
        CloudFile file=m_FileSheet.get(fileID);         //无权限访问文件返回wrong
        if ((!file.getCreator().equals(userID))
                &&(!file.getAuthorization().equals(Authorization.Public)))
            return NoteResult.wrong;
        
        Note localNote=m_NoteSheet.get(fileID).get(noteID);
        if (!localNote.getCreator().equals(userID))     //非本人备注返回wrong
            return NoteResult.wrong;
        
        m_NoteSheet.get(fileID).remove(noteID);         //删除本条备注
        return NoteResult.OK;
    }
}
