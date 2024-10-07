package layered.persistence;

public interface User {
    
	String getId();
	
	int getCredit();
	
	void rechargeCredit(int deltaCredit);
	
	void decreaseCredit(int amount);
}
