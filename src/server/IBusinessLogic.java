package server;

import common.ChangePasswdResult;
import common.Credential;
import common.LoginResult;
import common.RegisterResult;

/**
 * ��������BLL�ṩ�ӿ�
 * @author yzj
 * ��¼
 * ע��
 * �޸�����
 */
public interface IBusinessLogic {
	/**
	 * ��¼����ӿ�
	 * @param cred �����֤
	 * @return ��¼���
	 */
	public LoginResult login(Credential cred);
	/**
	 * ע�����ӿ�
	 * @param cred �����֤
	 * @return ע����
	 */
	public RegisterResult register(Credential cred);
	/**
	 * �޸��������ӿ�
	 * @param cred 
	 * @param newPassword
	 * @return �޸Ľ��
	 */
	public ChangePasswdResult changePasswd(Credential cred, String newPassword);
}
