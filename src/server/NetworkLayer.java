package server;

import java.io.IOException;
import java.net.InetSocketAddress;

import server.myhttphandler.*;

import com.sun.net.httpserver.HttpServer;



/**
 * ���������������Ӳ�
 * @author yzj
 *
 */
public class NetworkLayer implements INetworkLayer{
	/**
	 * Network������õ�BLL�ṩ�Ľӿ�
	 */
	private final IBusinessLogic m_Business;
	private final ISession m_Session;
	private HttpServer server;
	private int port=13051;
	private LoginHandler loginHandler;
	private RegisterHandler registerHandler;
	private ChangePasswdHandler changePwHandler;
	private FileHandler fileHandler;
	private DirectoryHandler directoryHandler;
	
	/**
	 * ���췽��
	 * @param business
	 */
	public NetworkLayer(IBusinessLogic business, ISession session){
		m_Business=business;
		m_Session=session;
		loginHandler=new LoginHandler(m_Business,m_Session);
		registerHandler=new RegisterHandler(m_Business);
		changePwHandler=new ChangePasswdHandler(m_Business,m_Session);
		fileHandler=new FileHandler(m_Business,m_Session);
		directoryHandler=new DirectoryHandler(m_Business,m_Session);
	}
	/**
	 * ����������
	 * @param port
	 * @throws IOException 
	 */
	public void startServer() throws IOException{
		server = HttpServer.create(new InetSocketAddress(port), 0); 	//����������
		server.createContext("/cloudshare/login",loginHandler); 		//��¼
		server.createContext("/cloudshare/register",registerHandler);   //ע��
		server.createContext("/cloudshare/changePw", changePwHandler); 	//�޸�����
		server.createContext("/cloudshare/file",fileHandler);			//�ļ�����
		server.createContext("/cloudshare/getDirectory",directoryHandler); //��ȡĿ¼
		server.setExecutor(java.util.concurrent.Executors.newCachedThreadPool());  //�̳߳�
		server.start(); //����������
	}
}
