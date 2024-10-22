package clean.domain.user;

import java.util.LinkedList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

class UserImpl implements User {
    private final String id;
    private int credit;
    private final List<UserListener> listeners;

    @JsonCreator
    UserImpl(@JsonProperty("id") final String id, @JsonProperty("credit") final int credit) {
        this.id = id;
        this.credit = credit;
        this.listeners = new LinkedList<>();
    }

    UserImpl(final String id) {
        this(id, 0);
    }

    UserImpl(final UserSnapshot snapshot) {
        this(snapshot.id(), snapshot.credit());
    }

    @Override
    public void decreaseCredit(final int amount) {
        this.credit -= amount;
        if (this.credit < 0) {
            this.credit = 0;
        }
        updateListeners();
    }

    @Override
    public void rechargeCredit(final int amount) {
        this.credit += amount;
        updateListeners();
    }

    private void updateListeners() {
        this.listeners.forEach(e -> e.userCreditChanged(this.getUserSnapshot()));
    }

    @Override
    public String toString() {
        return "{ id: " + this.id + ", credit: " + this.credit + " }";
    }

    @Override
    public void addUserListener(UserListener l) {
        this.listeners.add(l);
    }

    @Override
    public UserSnapshot getUserSnapshot() {
        return new UserSnapshot(id, credit);
    }
}
