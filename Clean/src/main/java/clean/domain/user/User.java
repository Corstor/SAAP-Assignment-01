package clean.domain.user;

public interface User {
    UserSnapshot getUserSnapshot();
    void decreaseCredit(int amount);
    void rechargeCredit(int amount);
    void addUserListener(UserListener l);
}
