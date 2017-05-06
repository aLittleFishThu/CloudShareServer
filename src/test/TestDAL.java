package test;

import java.util.HashMap;
import java.util.HashSet;

import common.CloudFile;
import common.Note;
import common.User;
import server.DataAccess;

public class TestDAL {
	public static void main(String args[]){
        DataAccess dataAccess=new DataAccess();
		/*HashMap<String,User> sheet=new HashMap<String,User>();
		sheet.put("yzj", new User("yzj","123"));
		sheet.put("yzj2",new User("yzj2","123"));
		dataAccess.storeUserSheet(sheet);
		HashMap<String,User> sheet2=dataAccess.getUserSheet();
		System.out.println(sheet2);*/
        /*HashMap<String,HashSet<CloudFile>> sheet=new HashMap<String,HashSet<CloudFile>>();
        CloudFile cloud1=new CloudFile("yzj","123");
        CloudFile cloud2=new CloudFile("yzj2","123");
        HashSet<CloudFile> set=new HashSet<CloudFile>();
        set.add(cloud1);set.add(cloud2);
        sheet.put("1", set);
        dataAccess.storeUserFileSheet(sheet);*/
        HashMap<String,CloudFile> sheet3=dataAccess.getFileSheet();
        System.out.println(sheet3);
        Note note=new Note("yzj","123");
        HashMap<String,HashMap<String,Note>> noteSheet=new HashMap<String,HashMap<String,Note>>();
        HashMap<String,Note> set=new HashMap<String,Note>();
        set.put("123",note);
        noteSheet.put("342", set);
        dataAccess.storeNoteSheet(noteSheet);
        HashMap<String,HashMap<String,Note>> sheet4=dataAccess.getNoteSheet();
        System.out.println(sheet4);
        System.out.println(note);
	}
}
