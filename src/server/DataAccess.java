package server;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.HashMap;
import java.util.HashSet;

import common.CloudFile;
import common.User;

/**
 * �����������ݷ��ʲ�
 * @author yzj
 *
 */
public class DataAccess implements IDataAccess{
	//private String m_userSheetPath=System.getenv("yzjCloud");  //�û��б���·��
	private String m_UserSheetPath="C:\\Users\\yzj\\Desktop\\1.txt"; //�û��б���·�� 
	private String m_FileSheetPath="C:\\Users\\yzj\\Desktop\\2.txt"; //�ļ��б���·��
	private String m_UserFileSheetPath="C:\\Users\\yzj\\Desktop\\3.txt";
																	 //�û��ļ�����·��
	private String m_FilePath="C:\\Users\\yzj\\Desktop";             //�ļ����·��
	
	@SuppressWarnings("unchecked")
	@Override
	/**
	 * ʵ��DAL���ṩ�Ķ�ȡ�û��б�ӿ�
	 * @return �û��б�
	 */
	public HashMap<String, User> getUserSheet(){
		HashMap<String,User> userSheet=new HashMap<String,User>();
		FileInputStream fin;
		ObjectInputStream objreader;
		
		try {
			File file=new File(m_UserSheetPath);
			if (!file.exists()){
				file.createNewFile();  
				return new HashMap<String,User>();  //�ļ������ڣ������ļ������� �ձ�
			}
			if (file.length()==0)
				return new HashMap<String,User>();  //�ļ�Ϊ�գ����ؿձ�
			fin=new FileInputStream(file);  		//��ȡ�ļ�
			objreader=new ObjectInputStream(fin);  //ת��Ϊ������
			userSheet=(HashMap<String,User>)objreader.readObject();  //ǿ��ת��Ϊ��ϣ��
			fin.close();         //�ر��ļ�������
			objreader.close();   //�رն���������
			return userSheet;    //���ع�ϣ��
		} catch (FileNotFoundException e) {
			e.printStackTrace(); 
		} catch (IOException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
		return userSheet;
	}

	@Override
	/**
	 * ʵ��DAL���ṩ�Ĵ洢�û��б�ӿ�
	 * @param BLL���ݴ���û��б�
	 */
	public void storeUserSheet(HashMap<String, User> userSheet)  {
		FileOutputStream fos;
		ObjectOutputStream objwriter;
		
		try {
			if (!userSheet.equals(null)){
				fos=new FileOutputStream(m_UserSheetPath);  //���ļ������
				objwriter=new ObjectOutputStream(fos);    //�򿪶��������
				objwriter.writeObject(userSheet);         //����ϣ��д��
				objwriter.close();                        //�رն��������
				fos.close();                              //�ر��ļ������
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@SuppressWarnings("unchecked")
	@Override
	/**
	 * ʵ�ֻ�ȡ�ļ��б�ӿ�
	 */
	public HashMap<String, CloudFile> getFileSheet() {
		HashMap<String,CloudFile> fileSheet=new HashMap<String,CloudFile>();
		FileInputStream fin;
		ObjectInputStream objreader;
		
		try {
			File file=new File(m_FileSheetPath);
			if (!file.exists()){
				file.createNewFile();  
				return new HashMap<String,CloudFile>();  //�ļ������ڣ������ļ������� �ձ�
			}
			if (file.length()==0)
				return new HashMap<String,CloudFile>();  //�ļ�Ϊ�գ����ؿձ�
			fin=new FileInputStream(file);  		//��ȡ�ļ�
			objreader=new ObjectInputStream(fin);  //ת��Ϊ������
			fileSheet=(HashMap<String,CloudFile>)objreader.readObject();  //ǿ��ת��Ϊ��ϣ��
			fin.close();         //�ر��ļ�������
			objreader.close();   //�رն���������
			return fileSheet;    //���ع�ϣ��
		} catch (FileNotFoundException e) {
			e.printStackTrace(); 
		} catch (IOException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
		return fileSheet;
	}
	/**
	 * ʵ�ִ洢�ļ��б�ӿ�
	 */
	@Override
	public void storeFileSheet(HashMap<String, CloudFile> fileSheet) {
		FileOutputStream fos;
		ObjectOutputStream objwriter;
		
		try {
			if (!fileSheet.equals(null)){
				fos=new FileOutputStream(m_FileSheetPath);  //���ļ������
				objwriter=new ObjectOutputStream(fos);    //�򿪶��������
				objwriter.writeObject(fileSheet);         //����ϣ��д��
				objwriter.close();                        //�رն��������
				fos.close();                              //�ر��ļ������
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}


	/**
	 * ��ȡ���û�Ϊ�ؼ��ֵ��ļ�Ŀ¼
	 */
	@SuppressWarnings("unchecked")
	@Override
	public HashMap<String, HashSet<CloudFile>> getUserFileSheet() {
		HashMap<String,HashSet<CloudFile>> userFileSheet
			=new HashMap<String,HashSet<CloudFile>>();
		FileInputStream fin;
		ObjectInputStream objreader;
		
		try {
			File file=new File(m_UserFileSheetPath);
			if (!file.exists()){
				file.createNewFile();  
				return new HashMap<String,HashSet<CloudFile>>();  
													//�ļ������ڣ������ļ������� �ձ�
			}
			if (file.length()==0)
				return new HashMap<String,HashSet<CloudFile>>();  
													//�ļ�Ϊ�գ����ؿձ�
			fin=new FileInputStream(file);  		//��ȡ�ļ�
			objreader=new ObjectInputStream(fin);  //ת��Ϊ������
			userFileSheet=(HashMap<String,HashSet<CloudFile>>)objreader.readObject();  
													//ǿ��ת��Ϊ��ϣ��
			fin.close();         //�ر��ļ�������
			objreader.close();   //�رն���������
			return userFileSheet;    //���ع�ϣ��
		} catch (FileNotFoundException e) {
			e.printStackTrace(); 
		} catch (IOException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
		return userFileSheet;
	}

	/**
	 * �洢���û�Ϊ�ؼ��ֵ��ļ�Ŀ¼
	 */
	@Override
	public void storeUserFileSheet(HashMap<String, HashSet<CloudFile>> userFileSheet) {
		FileOutputStream fos;
		ObjectOutputStream objwriter;
		
		try {
			if (!userFileSheet.equals(null)){
				fos=new FileOutputStream(m_UserFileSheetPath);  //���ļ������
				objwriter=new ObjectOutputStream(fos);    //�򿪶��������
				objwriter.writeObject(userFileSheet);     //����ϣ��д��
				objwriter.close();                        //�رն��������
				fos.close();                              //�ر��ļ������
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * ʵ���ϴ��ļ��ӿ�
	 */
	@Override
	public void uploadFile(CloudFile cloudFile, InputStream content)  {
		String filePath=m_FilePath+'\\'+cloudFile.getFileID()+".dat";
		FileOutputStream result;
		try {
			result = new FileOutputStream(filePath);
			byte[] buffer = new byte[1024];
			int length;
			while ((length = content.read(buffer)) != -1) {
			    result.write(buffer, 0, length);
			}
			content.close();
			result.flush();
			result.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
