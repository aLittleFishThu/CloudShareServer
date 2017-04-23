package test;

import common.Credential;

import server.BusinessLogic;
import server.DataAccess;

public class TestBLL {
	public static void main(String args[]){
		DataAccess dataAccess=new DataAccess();
		BusinessLogic businessLogic=new BusinessLogic(dataAccess);
		
		Credential cred=new Credential("yz12","1246dgad");
		String s1=businessLogic.login(cred).toString();
		System.out.println(s1);
	}
}
