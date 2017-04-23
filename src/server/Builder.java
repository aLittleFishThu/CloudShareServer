package server;

/**
 * ��װSession��Network
 * @author yzj
 *
 */
public class Builder {
	private IBusinessLogic m_Business;
	public void setBusiness(IBusinessLogic business){
		m_Business=business;
	}
	public INetworkLayer build(){
		INetworkLayer network;
		SessionManager session=new SessionManager();
		network=new NetworkLayer(m_Business,session);
		return network;
	}
}
