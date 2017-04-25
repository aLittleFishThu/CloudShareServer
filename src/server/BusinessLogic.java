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
 * ��������BLL��
 * @author yzj
 */
public class BusinessLogic implements IBusinessLogic{
	private final IDataAccess m_DataAccess;
	private HashMap<String,User> m_UserSheet=new HashMap<String,User>(); 
	private HashMap<String,CloudFile> m_FileSheet=new HashMap<String,CloudFile>();
	
	
	/**
	 * ���췽��
	 */
	public BusinessLogic(IDataAccess dataAccess ){
		m_DataAccess=dataAccess;
		retrieveData();
		storeData();
	}
    
	private void retrieveData(){
		m_UserSheet=m_DataAccess.getUserSheet();   //������������
		m_FileSheet=m_DataAccess.getFileSheet();
	}

	//���������ڲ��࣬�ػ��Զ��洢
	private void storeData(){
		Runtime.getRuntime().addShutdownHook(new Thread() {
			@Override
			public void run() { 
				m_DataAccess.storeUserSheet(m_UserSheet); //�洢�û��б�
				m_DataAccess.storeFileSheet(m_FileSheet); //�洢�ļ��б�
				System.out.println("storeOK");
			}
		});
	}
	
	@Override
	/**
	 * ʵ��BLL��login����
	 * @param cred
	 * @return ���
	 */
	public LoginResult login(Credential cred) {
		User user=new User(cred.getUserID(),cred.getPassword());
		String userID=user.getUserID();
		
		if (!m_UserSheet.containsKey(userID))  //�û�������
			return LoginResult.wrong;
		else if (!(m_UserSheet.get(userID).equals(user)))  //�������
			return LoginResult.wrong;
		else 
			return LoginResult.OK;   //�ɹ�
	}

	@Override
	/**
	 * ʵ��BLL��register����
	 * @param cred
	 * @return ע����
	 */
	public RegisterResult register(Credential cred) {
		User user=new User(cred.getUserID(),cred.getPassword());  
		String userID=user.getUserID();
		String password=user.getPassword();
		
		if (!common.Validator.isUserIDLegal(userID)) //�û����Ƿ�
			return RegisterResult.illegalID;
		else if (!common.Validator.isPasswdLegal(password))  //����Ƿ�
			return RegisterResult.illegalPassword;
		else if (m_UserSheet.containsKey(userID))   //�û����ظ�
			return RegisterResult.usedID;  
		else{
			m_UserSheet.put(userID, user);   //�����û�
			return RegisterResult.OK;            //���ؽ��
		}
	}

	@Override
	/**
	 * ʵ��BLL��changePasswd����
	 * @param cred
	 * @param newPassword
	 * @return �޸Ľ��
	 */
	public ChangePasswdResult changePasswd(Credential cred, String newPassword) {
		User user=new User(cred.getUserID(),cred.getPassword());  
		String userID=user.getUserID();
		
		if (!common.Validator.isPasswdLegal(newPassword)) //����Ƿ�
			return ChangePasswdResult.illegalPassword;
		else if (!m_UserSheet.containsKey(userID))     //�û���������
			return ChangePasswdResult.wrong;
		else if (!(m_UserSheet.get(userID).equals(user)))  //�������
			return ChangePasswdResult.wrong;
		else{
			user.setPassword(newPassword);    //�޸ĳɹ�
			m_UserSheet.put(userID, user);  //�����ϣ����
			return ChangePasswdResult.OK;     //���سɹ�
		}
	}

	@Override
	/**
	 * ʵ��BLL���ϴ��ļ�uploadFile����
	 * @param cloudFile 
	 * @param InputStream content
	 * ����DAL�ӿڣ�������fileID���������б�����������
	 */
	public FileResult uploadFile(CloudFile cloudFile, InputStream content) {
		String filename=cloudFile.getFilename();        //��ȥ���û��������ļ�
		String creator=cloudFile.getCreator();         
		Iterator<Entry<String,CloudFile>> iter=m_FileSheet.entrySet().iterator();
		while (iter.hasNext()){
			CloudFile aFile=iter.next().getValue();
			if (aFile.getFilename().equals(filename)&&
					aFile.getCreator().equals(creator)){
				iter.remove();
			}
		}
	
		String fileID;									//����fileID									
		do{
			fileID=UUID.randomUUID().toString();
		}while (m_FileSheet.containsKey(fileID));  
		cloudFile.setFileID(fileID);
		
		m_DataAccess.uploadFile(cloudFile, content); 	//���ýӿڣ��洢�ļ�������
		
		SimpleDateFormat df = 							//����uploadTime
				new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String uploadTime=df.format(new Date());
		cloudFile.setUploadTime(uploadTime);
		
		m_FileSheet.put(fileID, cloudFile);				//�����ļ��б�
		return FileResult.OK;							//�����ϴ����
	}

	
	@Override
	/**
	 * ʵ��BLL���ȡָ���û�Ŀ¼�Ľӿڷ���
	 * @param targetID Ŀ���û�
	 * @param userID   ��ǰ�û�
	 * @return ��ȡ���
	 */
	public FileDirectoryResult getDirectory(String targetID, String userID) {
		HashSet<CloudFile> fileDirectory=new HashSet<CloudFile>(); //�����ļ��б�
		Iterator<Entry<String,CloudFile>> iter=m_FileSheet.entrySet().iterator();
		while (iter.hasNext()){
			CloudFile aFile=iter.next().getValue();
			if (aFile.getCreator().equals(targetID)){		//�ҵ�Ŀ���û����ļ�
				if ((targetID.equals(userID))||				//��Ϊ�Լ��ļ������˹����ļ�		
						aFile.getAuthorization().equals(Authorization.Public)){
					fileDirectory.add(aFile);				//�����ļ��б�
				}
			}
		}
		return new FileDirectoryResult(fileDirectory,FileResult.OK);  //���ؽ�����ϲ�
	}
}
