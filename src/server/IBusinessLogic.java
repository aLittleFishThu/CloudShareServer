package server;

import java.io.InputStream;

import common.ChangePasswdResult;
import common.CloudFile;
import common.Credential;
import common.DownloadFileResult;
import common.FileDirectoryResult;
import common.FileResult;
import common.LoginResult;
import common.Note;
import common.NoteResult;
import common.RegisterResult;

/**
 * ��������BLL�ṩ�ӿ�
 * @author yzj
 * ��¼
 * ע��
 * �޸�����
 */
public interface IBusinessLogic {
	/**
	 * ��¼����ӿ�
	 * @param cred �����֤
	 * @return ��¼���
	 */
	public LoginResult login(Credential cred);
	/**
	 * ע�����ӿ�
	 * @param cred �����֤
	 * @return ע����
	 */
	public RegisterResult register(Credential cred);
	/**
	 * �޸��������ӿ�
	 * @param cred 
	 * @param newPassword
	 * @return �޸Ľ��
	 */
	public ChangePasswdResult changePasswd(Credential cred, String newPassword);
	/**
	 * �ϴ��ļ�����ӿ�
	 * @param cloudFile
	 * @param content
	 * @return �ϴ����
	 */
	public FileResult uploadFile(CloudFile cloudFile, InputStream content);
	/**
	 * ��ȡָ���û��ļ�Ŀ¼
	 * @param targetID Ŀ���û�
	 * @param userID   ��ǰ�û�
	 * @return ��ȡ�����Ŀ¼HashSet
	 */
	public FileDirectoryResult getDirectory(String targetID,String userID);
	/**
	 * ɾ��ָ���ļ�
	 * @param fileID
	 * @param userID
	 * @return ɾ��������ɹ�ɾ��ΪOK���ļ������ڻ���Ȩ��Ϊwrong��
	 */
	public FileResult deleteFile(String fileID,String userID);
	/**
	 * ����ָ���ļ�
	 * @param fileID
	 * @param userID
	 * @return ��������
	 */
	public DownloadFileResult downloadFile(String fileID,String userID);
	/**
	 * ���ӱ�ע
	 * @param note
	 * @return ���
	 */
	public NoteResult addNote(Note note);
	/**
	 * ɾ����ע
	 * @param noteID
	 * @param userID
	 * @return ���
	 */
	public NoteResult deleteNote(Note note,String userID);
}
