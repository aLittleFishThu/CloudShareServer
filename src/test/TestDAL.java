package test;

import java.util.HashMap;

import common.User;

import server.DataAccess;

public class TestDAL {
	public static void main(String args[]){
        DataAccess dataAccess=new DataAccess();
		HashMap<String,User> sheet=new HashMap<String,User>();
		sheet.put("yzj", new User("yzj","123"));
		sheet.put("yzj2",new User("yzj2","123"));
		dataAccess.storeUserSheet(sheet);
		HashMap<String,User> sheet2=dataAccess.getUserSheet();
		System.out.println(sheet2);
	}
}
