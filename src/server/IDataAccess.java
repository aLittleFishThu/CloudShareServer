package server;

import java.util.HashMap;
import common.*;

/**
 * ��������DAL���ṩ�ӿ�
 * @author yzj
 * ��¼
 * ע��
 * �޸�����
 */
public interface IDataAccess {
	/**
	 * ��ȡ�û��б�
	 * @return �û��б���ϣ��
	 */
	public HashMap<String,User> getUserSheet();
	/**
	 * �洢�û��б�
	 * @param userSheet
	 */
	public void storeUserSheet(HashMap<String,User> userSheet);
}
