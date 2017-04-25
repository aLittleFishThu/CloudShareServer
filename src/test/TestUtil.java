package test;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.UUID;
import java.util.Map.Entry;

import common.CloudFile;

public class TestUtil {
	public static void main(String args[]){
		/*SimpleDateFormat df = 							//设置uploadTime
				new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String uploadTime=df.format(new Date());
		System.out.println(uploadTime);
		System.out.println(UUID.randomUUID());*/
		/*String s="abc";
		String[] pairs=s.split("&");
		for (String pair:pairs)
			System.out.println(pair);*/
		CloudFile cloud1=new CloudFile("yzj","123");
		CloudFile cloud2=new CloudFile("yzj","134");
		CloudFile cloud3=new CloudFile("yzj1","123");
		HashMap<String,CloudFile> m_FileSheet=new HashMap<String,CloudFile>();
		m_FileSheet.put("1",cloud1);
		m_FileSheet.put("2",cloud2);
		m_FileSheet.put("3", cloud3);
		System.out.println(m_FileSheet);
		
		CloudFile cloudFile=new CloudFile("yzj","123");
		String filename=cloudFile.getFilename();        //除去该用户的重名文件
		String creator=cloudFile.getCreator();         
		Iterator<Entry<String,CloudFile>> iter=m_FileSheet.entrySet().iterator();
		while (iter.hasNext()){
			CloudFile aFile=iter.next().getValue();
			if (aFile.getFilename().equals(filename)&&
					aFile.getCreator().equals(creator)){
				iter.remove();
			}
		}
	   System.out.println(m_FileSheet);
	}
}
