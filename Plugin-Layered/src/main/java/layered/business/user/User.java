package layered.business.user;

public interface User {
    String getId();
    int getCredit();
    void decreaseCredit(int amount);
    void rechargeCredit(int amount);
}
