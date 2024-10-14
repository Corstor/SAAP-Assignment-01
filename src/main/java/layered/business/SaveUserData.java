package layered.business;

public interface SaveUserData {
    void saveUser(String id);
    void deleteUser(String id);

    void changeUserCredit(String id, int credit);
}
