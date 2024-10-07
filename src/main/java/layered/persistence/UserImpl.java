package layered.persistence;

public class UserImpl implements User {

	private String id;
	private int credit;
	
	public UserImpl(String id) {
		this.id = id;
		this.credit = 0;
	}
	
	public String getId() {
		return id;
	}
	
	public int getCredit() {
		return credit;
	}
	
	public void rechargeCredit(int deltaCredit) {
		credit += deltaCredit;
	}
	
	public void decreaseCredit(int amount) {
		credit -= amount;
		if (credit < 0) {
			credit = 0;
		}
	}
	
	public String toString() {
		return "{ id: " + id + ", credit: " + credit + " }";
	}
}
