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

import common.CloudFile;
import common.User;

/**
 * 服务器端数据访问层
 * @author yzj
 *
 */
public class DataAccess implements IDataAccess{
	//private String m_userSheetPath=System.getenv("yzjCloud");  //用户列表存放路径
	private String m_UserSheetPath="C:\\Users\\yzj\\Desktop\\1.txt"; //用户列表存放路径 
	private String m_FileSheetPath="C:\\Users\\yzj\\Desktop\\2.txt"; //文件列表存放路径
	//private String m_UserFileSheetPath="C:\\Users\\yzj\\Desktop\\3.txt";
																	 //用户文件库存放路径
	private String m_FilePath="C:\\Users\\yzj\\Desktop";             //文件存放路径
	
	@SuppressWarnings("unchecked")
	@Override
	/**
	 * 实现DAL层提供的读取用户列表接口
	 * @return 用户列表
	 */
	public HashMap<String, User> getUserSheet(){
		HashMap<String,User> userSheet=new HashMap<String,User>();
		FileInputStream fin;
		ObjectInputStream objreader;
		
		try {
			File file=new File(m_UserSheetPath);
			if (!file.exists()){
				file.createNewFile();  
				return new HashMap<String,User>();  //文件不存在，创建文件，返回 空表
			}
			if (file.length()==0)
				return new HashMap<String,User>();  //文件为空，返回空表
			fin=new FileInputStream(file);  		//读取文件
			objreader=new ObjectInputStream(fin);  //转化为对象流
			userSheet=(HashMap<String,User>)objreader.readObject();  //强制转换为哈希表
			fin.close();         //关闭文件输入流
			objreader.close();   //关闭对象输入流
			return userSheet;    //返回哈希表
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
	 * 实现DAL层提供的存储用户列表接口
	 * @param BLL层暂存的用户列表
	 */
	public void storeUserSheet(HashMap<String, User> userSheet)  {
		FileOutputStream fos;
		ObjectOutputStream objwriter;
		
		try {
			if (!userSheet.equals(null)){
				fos=new FileOutputStream(m_UserSheetPath);  //打开文件输出流
				objwriter=new ObjectOutputStream(fos);    //打开对象输出流
				objwriter.writeObject(userSheet);         //将哈希表写入
				objwriter.close();                        //关闭对象输出流
				fos.close();                              //关闭文件输出流
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
	 * 实现获取文件列表接口
	 */
	public HashMap<String, CloudFile> getFileSheet() {
		HashMap<String,CloudFile> fileSheet=new HashMap<String,CloudFile>();
		FileInputStream fin;
		ObjectInputStream objreader;
		
		try {
			File file=new File(m_FileSheetPath);
			if (!file.exists()){
				file.createNewFile();  
				return new HashMap<String,CloudFile>();  //文件不存在，创建文件，返回 空表
			}
			if (file.length()==0)
				return new HashMap<String,CloudFile>();  //文件为空，返回空表
			fin=new FileInputStream(file);  		//读取文件
			objreader=new ObjectInputStream(fin);  //转化为对象流
			fileSheet=(HashMap<String,CloudFile>)objreader.readObject();  //强制转换为哈希表
			fin.close();         //关闭文件输入流
			objreader.close();   //关闭对象输入流
			return fileSheet;    //返回哈希表
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
	 * 实现存储文件列表接口
	 */
	@Override
	public void storeFileSheet(HashMap<String, CloudFile> fileSheet) {
		FileOutputStream fos;
		ObjectOutputStream objwriter;
		
		try {
			if (!fileSheet.equals(null)){
				fos=new FileOutputStream(m_FileSheetPath);  //打开文件输出流
				objwriter=new ObjectOutputStream(fos);    //打开对象输出流
				objwriter.writeObject(fileSheet);         //将哈希表写入
				objwriter.close();                        //关闭对象输出流
				fos.close();                              //关闭文件输出流
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}


/*	*//**
	 * 获取以用户为关键字的文件目录
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
													//文件不存在，创建文件，返回 空表
			}
			if (file.length()==0)
				return new HashMap<String,HashSet<CloudFile>>();  
													//文件为空，返回空表
			fin=new FileInputStream(file);  		//读取文件
			objreader=new ObjectInputStream(fin);  //转化为对象流
			userFileSheet=(HashMap<String,HashSet<CloudFile>>)objreader.readObject();  
													//强制转换为哈希表
			fin.close();         //关闭文件输入流
			objreader.close();   //关闭对象输入流
			return userFileSheet;    //返回哈希表
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
	 * 存储以用户为关键字的文件目录
	 *//*
	@Override
	public void storeUserFileSheet(HashMap<String, HashSet<CloudFile>> userFileSheet) {
		FileOutputStream fos;
		ObjectOutputStream objwriter;
		
		try {
			if (!userFileSheet.equals(null)){
				fos=new FileOutputStream(m_UserFileSheetPath);  //打开文件输出流
				objwriter=new ObjectOutputStream(fos);    //打开对象输出流
				objwriter.writeObject(userFileSheet);     //将哈希表写入
				objwriter.close();                        //关闭对象输出流
				fos.close();                              //关闭文件输出流
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}*/
	
	/**
	 * 实现上传文件接口
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
