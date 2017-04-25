package server;

import java.io.IOException;
import java.net.InetSocketAddress;

import server.myhttphandler.*;

import com.sun.net.httpserver.HttpServer;



/**
 * 服务器端网络连接层
 * @author yzj
 *
 */
public class NetworkLayer implements INetworkLayer{
	/**
	 * Network层需调用的BLL提供的接口
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
	 * 构造方法
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
	 * 启动服务器
	 * @param port
	 * @throws IOException 
	 */
	public void startServer() throws IOException{
		server = HttpServer.create(new InetSocketAddress(port), 0); 	//创建服务器
		server.createContext("/cloudshare/login",loginHandler); 		//登录
		server.createContext("/cloudshare/register",registerHandler);   //注册
		server.createContext("/cloudshare/changePw", changePwHandler); 	//修改密码
		server.createContext("/cloudshare/file",fileHandler);			//文件操作
		server.createContext("/cloudshare/getDirectory",directoryHandler); //获取目录
		server.setExecutor(java.util.concurrent.Executors.newCachedThreadPool());  //线程池
		server.start(); //启动服务器
	}
}
