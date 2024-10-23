package clean.domain.user;

import clean.domain.Listener;
import clean.domain.Snapshot;

public interface User {
    UserSnapshot getUserSnapshot();
    void decreaseCredit(int amount);
    void rechargeCredit(int amount);
    void addUserListener(Listener<? extends Snapshot> l);

    void updated();
}
