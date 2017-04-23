package server;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.HashMap;

import common.User;

/**
 * �����������ݷ��ʲ�
 * @author yzj
 *
 */
public class DataAccess implements IDataAccess{
	//private String m_userSheetPath=System.getenv("yzjCloud");  //�û��б���·��
	private String m_userSheetPath="C:\\Users\\yzj\\Desktop\\1.txt"; 
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
			File file=new File(m_userSheetPath);
			if (!file.exists()){
				file.createNewFile();  
				return new HashMap<String,User>();  //�ļ������ڣ������ļ������� �ձ�
			}										
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
				fos=new FileOutputStream(m_userSheetPath);  //���ļ������
				objwriter=new ObjectOutputStream(fos);    //�򿪶��������
				objwriter.writeObject(userSheet);         //����ϣ��д��
				objwriter.close();                        //�رն��������
				fos.close();                              //�ر��ļ������
			}
		} catch (FileNotFoundException e) {
			// TODO �Զ����ɵ� catch ��
			e.printStackTrace();
		} catch (IOException e) {
			// TODO �Զ����ɵ� catch ��
			e.printStackTrace();
		}
	}
	

}
