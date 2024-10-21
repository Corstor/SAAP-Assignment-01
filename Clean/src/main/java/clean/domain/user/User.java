package clean.domain.user;

public interface User {
    String getId();
    UserSnapshot getUserSnapshot();
    void decreaseCredit(int amount);
    void rechargeCredit(int amount);
    void addUserListener(UsersListener l);
}
