package layered.persistence;

public interface User {
    String getId();
    int getCredit();
    void decreaseCredit(int amount);
    void rechargeCredit(int amount);
}
