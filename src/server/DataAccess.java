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
import java.util.Iterator;
import java.util.Map.Entry;

import org.apache.commons.io.IOUtils;

import common.Authorization;
import common.CloudFile;
import common.Note;
import common.User;

/**
 * 服务器端数据访问层
 * @author yzj    
 *
 */
public class DataAccess implements IDataAccess{
	//private String m_userSheetPath=System.getenv("yzjCloud");  //用户列表存放路径
	private String m_UserSheetPath="C:\\Users\\yzj\\Desktop\\1.dat"; //用户列表存放路径 
	private String m_FileSheetPath="C:\\Users\\yzj\\Desktop\\2.dat"; //文件列表存放路径
	private String m_NoteSheetPath="C:\\Users\\yzj\\Desktop\\3.dat"; //备注列表存放路径
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
			Iterator<Entry<String,CloudFile>> iter=fileSheet.entrySet().iterator();
	        while (iter.hasNext()){
	            CloudFile aFile=iter.next().getValue();
	            if (!isFileExists(aFile.getFileID())){       //删除不匹配的文件
	                iter.remove();
	            }
	        } 
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
	 * 删除文件
	 * @param fileID
	 * @return 文件不存在返回false，成功删除返回true
	 */
	public boolean deleteFile(String fileID) {
	    File file=new File(m_FilePath+'\\'+fileID+".dat");
	    boolean result=file.delete();
	    return result;		
	}

	@Override
	/**
	 * 下载文件
	 * @param File file
	 * @return 文件内容
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
     * 读取备注列表
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
                return new HashMap<String,HashMap<String,Note>>();  //文件不存在，创建文件，返回 空表
            }
            if (file.length()==0)
                return new HashMap<String,HashMap<String,Note>>();  //文件为空，返回空表
            fin=new FileInputStream(file);          //读取文件
            objreader=new ObjectInputStream(fin);  //转化为对象流
            noteSheet=(HashMap<String, HashMap<String,Note>>)objreader.readObject();  //强制转换为哈希表
            fin.close();         //关闭文件输入流
            objreader.close();   //关闭对象输入流
            return noteSheet;    //返回哈希表
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
                fos=new FileOutputStream(m_NoteSheetPath);  //打开文件输出流
                objwriter=new ObjectOutputStream(fos);    //打开对象输出流
                objwriter.writeObject(noteSheet);         //将哈希表写入
                objwriter.close();                        //关闭对象输出流
                fos.close();                              //关闭文件输出流
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public boolean isFileExists(String fileID) {
        String filePath=m_FilePath+"\\"+fileID+".dat";
        File file=new File(filePath);
        return file.exists();
    }
}
