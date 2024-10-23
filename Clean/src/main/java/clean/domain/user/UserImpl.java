package clean.domain.user;

import java.util.LinkedList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import clean.domain.Listener;
import clean.domain.Snapshot;

class UserImpl implements User {
    private final String id;
    private int credit;
    private final List<Listener<UserSnapshot>> listeners;

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
    synchronized public void decreaseCredit(final int amount) {
        this.credit -= amount;
        if (this.credit < 0) {
            this.credit = 0;
        }
    }

    @Override
    synchronized public void rechargeCredit(final int amount) {
        this.credit += amount;
    }

    @Override
    synchronized public void updated() {
        this.listeners.forEach(e -> e.eventOccured(this.getUserSnapshot()));
    }

    @Override
    public String toString() {
        return "{ id: " + this.id + ", credit: " + this.credit + " }";
    }

    @SuppressWarnings("unchecked")
    @Override
    public void addUserListener(Listener<? extends Snapshot> l) {
        this.listeners.add((Listener<UserSnapshot>) l);
    }

    @Override
    public UserSnapshot getUserSnapshot() {
        return new UserSnapshot(id, credit);
    }
}
