package layered.business.user;

class UserImpl implements User {
    private final String id;
    private int credit;

    UserImpl(final String id, final int credit) {
        this.id = id;
        this.credit = credit;
    }

    UserImpl(final String id) {
        this(id, 0);
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
