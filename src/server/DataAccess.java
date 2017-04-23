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
 * 服务器端数据访问层
 * @author yzj
 *
 */
public class DataAccess implements IDataAccess{
	//private String m_userSheetPath=System.getenv("yzjCloud");  //用户列表存放路径
	private String m_userSheetPath="C:\\Users\\yzj\\Desktop\\1.txt"; 
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
			File file=new File(m_userSheetPath);
			if (!file.exists()){
				file.createNewFile();  
				return new HashMap<String,User>();  //文件不存在，创建文件，返回 空表
			}										
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
				fos=new FileOutputStream(m_userSheetPath);  //打开文件输出流
				objwriter=new ObjectOutputStream(fos);    //打开对象输出流
				objwriter.writeObject(userSheet);         //将哈希表写入
				objwriter.close();                        //关闭对象输出流
				fos.close();                              //关闭文件输出流
			}
		} catch (FileNotFoundException e) {
			// TODO 自动生成的 catch 块
			e.printStackTrace();
		} catch (IOException e) {
			// TODO 自动生成的 catch 块
			e.printStackTrace();
		}
	}
	

}
