package layered.persistence;

public class UserImpl implements User {

    private final String id;
    private int credit;

    public UserImpl(final String id) {
        this.id = id;
        this.credit = 0;
    }

    @Override
    public String getId() {
        return this.id;
    }

    @Override
    public int getCredit() {
        return this.credit;
    }

    @Override
    public void decreaseCredit(final int amount) {
        this.credit -= amount;
        if (this.credit < 0) {
            this.credit = 0;
        }
    }

    @Override
    public void rechargeCredit(final int amount) {
        this.credit += amount;
    }

    public String toString() {
		return "{ id: " + this.id + ", credit: " + this.credit + " }";
	}
}
