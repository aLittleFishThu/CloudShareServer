package server;

import java.util.HashMap;
import common.*;

/**
 * 服务器端BLL层
 * @author yzj
 */
public class BusinessLogic implements IBusinessLogic{
	private final IDataAccess m_DataAccess;
	private HashMap<String,User> m_UserSheet=new HashMap<String,User>(); 
	
	/**
	 * 构造方法
	 */
	public BusinessLogic(IDataAccess dataAccess ){
		m_DataAccess=dataAccess;
		retrieveData();
		storeData();
	}
    
	private void retrieveData(){
		m_UserSheet=m_DataAccess.getUserSheet();   //开机加载数据
	}

	//定义匿名内部类，关机自动存储
	private void storeData(){
		Runtime.getRuntime().addShutdownHook(new Thread() {
			@Override
			public void run() { 
				m_DataAccess.storeUserSheet(m_UserSheet); //存储用户列表
				System.out.println("storeOK");
			}
		});
	}
	
	@Override
	/**
	 * 实现BLL层login服务
	 * @param cred
	 * @return 结果
	 */
	public LoginResult login(Credential cred) {
		User user=new User(cred.getUserID(),cred.getPassword());
		String userID=user.getUserID();
		
		if (!m_UserSheet.containsKey(userID))  //用户名错误
			return LoginResult.wrong;
		else if (!(m_UserSheet.get(userID).equals(user)))  //密码错误
			return LoginResult.wrong;
		else 
			return LoginResult.OK;   //成功
	}

	@Override
	/**
	 * 实现BLL层register服务
	 * @param cred
	 * @return 注册结果
	 */
	public RegisterResult register(Credential cred) {
		User user=new User(cred.getUserID(),cred.getPassword());  
		String userID=user.getUserID();
		String password=user.getPassword();
		
		if (!common.Validator.isUserIDLegal(userID)) //用户名非法
			return RegisterResult.illegalID;
		else if (!common.Validator.isPasswdLegal(password))  //密码非法
			return RegisterResult.illegalPassword;
		else if (m_UserSheet.containsKey(userID))   //用户名重复
			return RegisterResult.usedID;  
		else{
			m_UserSheet.put(userID, user);   //加入用户
			return RegisterResult.OK;            //返回结果
		}
	}

	@Override
	/**
	 * 实现BLL层changePasswd服务
	 * @param cred
	 * @param newPassword
	 * @return 修改结果
	 */
	public ChangePasswdResult changePasswd(Credential cred, String newPassword) {
		User user=new User(cred.getUserID(),cred.getPassword());  
		String userID=user.getUserID();
		
		if (!common.Validator.isPasswdLegal(newPassword)) //密码非法
			return ChangePasswdResult.illegalPassword;
		else if (!m_UserSheet.containsKey(userID))     //用户名不存在
			return ChangePasswdResult.wrong;
		else if (!(m_UserSheet.get(userID).equals(user)))  //密码错误
			return ChangePasswdResult.wrong;
		else{
			user.setPassword(newPassword);    //修改成功
			m_UserSheet.put(userID, user);  //放入哈希表中
			return ChangePasswdResult.OK;     //返回成功
		}
	}
}
