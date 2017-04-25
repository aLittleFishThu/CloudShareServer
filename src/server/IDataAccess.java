package server;

import java.io.InputStream;
import java.util.HashMap;
//import java.util.HashSet;

import common.*;

/**
 * ��������DAL���ṩ�ӿ�
 * @author yzj
 * ��¼
 * ע��
 * �޸�����
 */
public interface IDataAccess {
	/**
	 * ��ȡ�û��б�
	 * @return �û��б���ϣ��
	 */
	public HashMap<String,User> getUserSheet();
	/**
	 * �洢�û��б�
	 * @param userSheet
	 */
	public void storeUserSheet(HashMap<String,User> userSheet);
	/**
	 * ��ȡ�ļ��б�
	 * @return fileSheet
	 */
	public HashMap<String,CloudFile> getFileSheet();
	/**
	 * �洢�ļ��б�
	 */
	public void storeFileSheet(HashMap<String,CloudFile> fileSheet);
/*	*//**
	 * ��ȡ�û��ļ���
	 * @return fileSheet
	 *//*
	public HashMap<String,HashSet<CloudFile>> getUserFileSheet();
	*//**
	 * �洢�û��ļ���
	 *//*
	public void storeUserFileSheet(HashMap<String,HashSet<CloudFile>> userFileSheet);*/
	/**
	 * �ϴ��ļ�
	 */
	public void uploadFile(CloudFile file,InputStream in);
	
}
