package clean.domain.user;

import clean.domain.Listener;

public interface User {
    UserSnapshot getUserSnapshot();
    void decreaseCredit(int amount);
    void rechargeCredit(int amount);
    void addUserListener(Listener<UserSnapshot> l);
}
