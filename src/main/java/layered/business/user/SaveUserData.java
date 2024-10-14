package layered.business.user;

public interface SaveUserData {
    void saveUser(String id);
    void deleteUser(String id);

    void changeUserCredit(String id, int credit);
}
