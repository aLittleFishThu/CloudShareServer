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

import org.apache.commons.io.IOUtils;

import common.CloudFile;
import common.Note;
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
	private String m_NoteSheetPath="C:\\Users\\yzj\\Desktop\\3.txt"; //��ע�б���·��
	//private String m_UserFileSheetPath="C:\\Users\\yzj\\Desktop\\3.txt";
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


/*	*//**
	 * ��ȡ���û�Ϊ�ؼ��ֵ��ļ�Ŀ¼
	 *//*
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

	*//**
	 * �洢���û�Ϊ�ؼ��ֵ��ļ�Ŀ¼
	 *//*
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
	}*/
	
	/**
	 * ʵ���ϴ��ļ��ӿ�
	 */
	@Override
	public void uploadFile(String fileID, InputStream content)  {
		String filePath=m_FilePath+"\\"+fileID+".dat";
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

	@Override
	/**
	 * ɾ���ļ�
	 * @param fileID
	 * @return �ļ������ڷ���false���ɹ�ɾ������true
	 */
	public boolean deleteFile(String fileID) {
	    File file=new File(m_FilePath+'\\'+fileID+".dat");
	    boolean result=file.delete();
	    return result;		
	}

	@Override
	/**
	 * �����ļ�
	 * @param File file
	 * @return �ļ�����
	 */
	public byte [] downloadFile(String fileID) throws IOException {
		File file=new File(m_FilePath+'\\'+fileID+".dat");
	    FileInputStream fis= new FileInputStream(file);
	    byte[] content=IOUtils.toByteArray(fis);
	    fis.close();
	    return content;
	}

    @SuppressWarnings("unchecked")
    @Override
    /**
     * ��ȡ��ע�б�
     * @return noteSheet();
     */
    public HashMap<String, HashMap<String,Note>> getNoteSheet() {
        HashMap<String,HashMap<String,Note>> noteSheet=new HashMap<String,HashMap<String,Note>>();
        FileInputStream fin;
        ObjectInputStream objreader;
        
        try {
            File file=new File(m_NoteSheetPath);
            if (!file.exists()){
                file.createNewFile();  
                return new HashMap<String,HashMap<String,Note>>();  //�ļ������ڣ������ļ������� �ձ�
            }
            if (file.length()==0)
                return new HashMap<String,HashMap<String,Note>>();  //�ļ�Ϊ�գ����ؿձ�
            fin=new FileInputStream(file);          //��ȡ�ļ�
            objreader=new ObjectInputStream(fin);  //ת��Ϊ������
            noteSheet=(HashMap<String, HashMap<String,Note>>)objreader.readObject();  //ǿ��ת��Ϊ��ϣ��
            fin.close();         //�ر��ļ�������
            objreader.close();   //�رն���������
            return noteSheet;    //���ع�ϣ��
        } catch (FileNotFoundException e) {
            e.printStackTrace(); 
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        return noteSheet;
    }

    @Override
    public void storeNoteSheet(HashMap<String, HashMap<String,Note>> noteSheet) {
        FileOutputStream fos;
        ObjectOutputStream objwriter;
        
        try {
            if (!noteSheet.equals(null)){
                fos=new FileOutputStream(m_NoteSheetPath);  //���ļ������
                objwriter=new ObjectOutputStream(fos);    //�򿪶��������
                objwriter.writeObject(noteSheet);         //����ϣ��д��
                objwriter.close();                        //�رն��������
                fos.close();                              //�ر��ļ������
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
