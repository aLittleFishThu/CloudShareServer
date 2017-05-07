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

/**
 * ��������BLL��
 * @author yzj
 */
public class BusinessLogic implements IBusinessLogic{
	private final IDataAccess m_DataAccess;
	private HashMap<String,User> m_UserSheet=new HashMap<String,User>(); 
	private HashMap<String,CloudFile> m_FileSheet=new HashMap<String,CloudFile>();
	private HashMap<String,HashMap<String,Note>> m_NoteSheet=new HashMap<String,HashMap<String,Note>>();
	
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
		m_NoteSheet=m_DataAccess.getNoteSheet();
	}

	//���������ڲ��࣬�ػ��Զ��洢
	private void storeData(){
		Runtime.getRuntime().addShutdownHook(new Thread() {
			@Override
			public void run() { 
				m_DataAccess.storeUserSheet(m_UserSheet); //�洢�û��б�
				m_DataAccess.storeFileSheet(m_FileSheet); //�洢�ļ��б�
				m_DataAccess.storeNoteSheet(m_NoteSheet); //�洢��ע�б�
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
		String filename=cloudFile.getFilename();        //�滻���û��������ļ�
		String creator=cloudFile.getCreator();         
		Iterator<Entry<String,CloudFile>> iter=m_FileSheet.entrySet().iterator();
		while (iter.hasNext()){
			CloudFile aFile=iter.next().getValue();
			if (aFile.getFilename().equals(filename)&&
					aFile.getCreator().equals(creator)){
			    m_DataAccess.uploadFile(aFile.getFileID(), content);
				return FileResult.OK;
			}
		}
	
		String fileID;									//�������ļ�ʱ����fileID									
		do{
			fileID=UUID.randomUUID().toString();
		}while (m_FileSheet.containsKey(fileID));  
		cloudFile.setFileID(fileID);
		m_DataAccess.uploadFile(fileID, content); 	//���ýӿڣ��洢�ļ�������
		
		SimpleDateFormat df = 							//����uploadTime
				new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String uploadTime=df.format(new Date());
		cloudFile.setUploadTime(uploadTime);
		
		m_FileSheet.put(fileID, cloudFile);				//�����ļ��б�
		m_NoteSheet.put(fileID, new HashMap<String,Note>());
		                                                //���뱸ע�б�
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
		if (!m_UserSheet.containsKey(targetID))				//Ŀ���û�������ֱ�ӷ���wrong
			return new FileDirectoryResult(FileResult.wrong);
		
		ArrayList<CloudFile> fileDirectory=new ArrayList<CloudFile>(); //�����ļ��б�
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

	@Override
	/**
	 * ʵ��BLL���ɾ���ļ��ӿڷ���
	 * @param fileID
	 * @param userID
	 * @return FileResult
	 */
	public FileResult deleteFile(String fileID, String userID) {
		CloudFile cloudFile=m_FileSheet.get(fileID);	//���Ȩ��
		if (!cloudFile.getCreator().equals(userID))
			return FileResult.wrong;
		if (!m_DataAccess.deleteFile(fileID))			//����Ƿ�ɹ�ɾ��
			return FileResult.wrong;
		else{											//���ɹ�ɾ��
			m_FileSheet.remove(fileID);					//���ļ��б����ȥ
			m_NoteSheet.remove(fileID);                 //�ӱ�ע�б����ȥ
			return FileResult.OK;						//����ɾ���ɹ�
		}
	}

	@Override
	/**
	 * ʵ��BLL�������ļ��Ľӿڷ���
	 * @param fileID
	 * @param userID
	 */
	public DownloadFileResult downloadFile(String fileID, String userID) {
		if (!m_FileSheet.containsKey(fileID))			//����ļ��Ƿ����
			return new DownloadFileResult();
		CloudFile cloudFile=m_FileSheet.get(fileID);
		if ((!cloudFile.getCreator().equals(userID))&&	//����Ƿ���Ȩ��
			(cloudFile.getAuthorization().equals(Authorization.Private)))
			return new DownloadFileResult();
		try {
			byte[] content = m_DataAccess.downloadFile(fileID);
			return new DownloadFileResult(content,FileResult.OK);
		} catch (FileNotFoundException e) {
			return new DownloadFileResult();
		} catch (IOException e) {
			return new DownloadFileResult();
		}
	}

    @Override
    /**
     * ʵ��BLL�����ӱ�ע����
     */
    public NoteResult addNote(Note note) {
        String userID=note.getCreator();
        String fileID=note.getFileID();
        if (!m_FileSheet.containsKey(fileID))           //�ļ������ڷ���wrong
            return NoteResult.wrong;            
        
        CloudFile file=m_FileSheet.get(fileID);         //��Ȩ�޷����ļ�����wrong
        if ((!file.getCreator().equals(userID))
                &&(!file.getAuthorization().equals(Authorization.Public)))
            return NoteResult.wrong;
        
        if (!m_NoteSheet.containsKey(fileID))           //��ֹnoteSheet��û��fileID
            m_NoteSheet.put(fileID, new HashMap<String,Note>());
        
        String noteID;                                  //����noteID                                  
        do{
            noteID=UUID.randomUUID().toString();
        }while (m_NoteSheet.get(fileID).containsKey(noteID));
        
              
        SimpleDateFormat df =                           //����uploadTime
                new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String uploadTime=df.format(new Date());
        note.setUploadTime(uploadTime);
        
        m_NoteSheet.get(fileID).put(noteID,note);       //���뱸ע�б�
        return NoteResult.OK;
    }

    @Override
    public NoteResult deleteNote(Note note, String userID) {
        String fileID=note.getFileID();
        String noteID=note.getNoteID();
        
        if (!m_FileSheet.containsKey(fileID))           //�ļ������ڷ���wrong
            return NoteResult.wrong;            
        
        CloudFile file=m_FileSheet.get(fileID);         //��Ȩ�޷����ļ�����wrong
        if ((!file.getCreator().equals(userID))
                &&(!file.getAuthorization().equals(Authorization.Public)))
            return NoteResult.wrong;
        
        Note localNote=m_NoteSheet.get(fileID).get(noteID);
        if (!localNote.getCreator().equals(userID))     //�Ǳ��˱�ע����wrong
            return NoteResult.wrong;
        
        m_NoteSheet.get(fileID).remove(noteID);         //ɾ��������ע
        return NoteResult.OK;
    }
}
