package server;

import java.util.HashMap;
import common.*;

/**
 * ��������BLL��
 * @author yzj
 */
public class BusinessLogic implements IBusinessLogic{
	private final IDataAccess m_DataAccess;
	private HashMap<String,User> m_UserSheet=new HashMap<String,User>(); 
	
	/**
	 * ���췽��
	 */
	public BusinessLogic(IDataAccess dataAccess ){
		m_DataAccess=dataAccess;
		retrieveData();
		storeData();
	}
    
	private void retrieveData(){
		m_UserSheet=m_DataAccess.getUserSheet();   //������������
	}

	//���������ڲ��࣬�ػ��Զ��洢
	private void storeData(){
		Runtime.getRuntime().addShutdownHook(new Thread() {
			@Override
			public void run() { 
				m_DataAccess.storeUserSheet(m_UserSheet); //�洢�û��б�
				System.out.println("storeOK");
			}
		});
	}
	
	@Override
	/**
	 * ʵ��BLL��login����
	 * @param cred
	 * @return ���
	 */
	public LoginResult login(Credential cred) {
		User user=new User(cred.getUserID(),cred.getPassword());
		String userID=user.getUserID();
		
		if (!m_UserSheet.containsKey(userID))  //�û�������
			return LoginResult.wrong;
		else if (!(m_UserSheet.get(userID).equals(user)))  //�������
			return LoginResult.wrong;
		else 
			return LoginResult.OK;   //�ɹ�
	}

	@Override
	/**
	 * ʵ��BLL��register����
	 * @param cred
	 * @return ע����
	 */
	public RegisterResult register(Credential cred) {
		User user=new User(cred.getUserID(),cred.getPassword());  
		String userID=user.getUserID();
		String password=user.getPassword();
		
		if (!common.Validator.isUserIDLegal(userID)) //�û����Ƿ�
			return RegisterResult.illegalID;
		else if (!common.Validator.isPasswdLegal(password))  //����Ƿ�
			return RegisterResult.illegalPassword;
		else if (m_UserSheet.containsKey(userID))   //�û����ظ�
			return RegisterResult.usedID;  
		else{
			m_UserSheet.put(userID, user);   //�����û�
			return RegisterResult.OK;            //���ؽ��
		}
	}

	@Override
	/**
	 * ʵ��BLL��changePasswd����
	 * @param cred
	 * @param newPassword
	 * @return �޸Ľ��
	 */
	public ChangePasswdResult changePasswd(Credential cred, String newPassword) {
		User user=new User(cred.getUserID(),cred.getPassword());  
		String userID=user.getUserID();
		
		if (!common.Validator.isPasswdLegal(newPassword)) //����Ƿ�
			return ChangePasswdResult.illegalPassword;
		else if (!m_UserSheet.containsKey(userID))     //�û���������
			return ChangePasswdResult.wrong;
		else if (!(m_UserSheet.get(userID).equals(user)))  //�������
			return ChangePasswdResult.wrong;
		else{
			user.setPassword(newPassword);    //�޸ĳɹ�
			m_UserSheet.put(userID, user);  //�����ϣ����
			return ChangePasswdResult.OK;     //���سɹ�
		}
	}
}
