package test;
import server.*;

public class Test {
	public static void main(String args[])throws Exception{
		DataAccess dataAccess=new DataAccess();  					//����DAL��
		BusinessLogic businessLogic=new BusinessLogic(dataAccess);	//����BLL��
		Builder builder=new Builder();
		builder.setBusiness(businessLogic);
		INetworkLayer network=builder.build();
		network.startServer();  //����������
	}
}
