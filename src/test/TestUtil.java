package test;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.UUID;

public class TestUtil {
	public static void main(String args[]){
		/*SimpleDateFormat df = 							//����uploadTime
				new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String uploadTime=df.format(new Date());
		System.out.println(uploadTime);
		System.out.println(UUID.randomUUID());*/
		String s="abc";
		String[] pairs=s.split("&");
		for (String pair:pairs)
			System.out.println(pair);
	}
}
