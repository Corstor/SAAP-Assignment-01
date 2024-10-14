package layered.persistence.user;

import java.util.List;

public interface UserRepository {
    List<User> getUsers();

    void saveUser(String id);
    void deleteUser(String id);

    void changeUserCredit(String id, int credit);
    User getUserFromId(String id);
}
