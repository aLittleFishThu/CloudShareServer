package test;
import server.*;

public class Test {
	public static void main(String args[])throws Exception{
		DataAccess dataAccess=new DataAccess();  					//创建DAL类
		BusinessLogic businessLogic=new BusinessLogic(dataAccess);	//创建BLL类
		Builder builder=new Builder();
		builder.setBusiness(businessLogic);
		INetworkLayer network=builder.build();
		network.startServer();  //启动服务器
	}
}
