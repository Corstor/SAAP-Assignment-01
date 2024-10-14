package layered.database;

import java.util.List;
import java.io.IOException;


public interface UserStore {
    List<UserDB> loadAllUsers() throws IOException;
    void saveAllUsers(List<UserDB> users) throws IOException;

    void saveUser(UserDB user) throws IOException;
    void deleteUser(String id) throws IOException;

    void changeUserCredit(String id, int credit) throws IOException;
    UserDB getUserFromId(String id) throws IOException;
}
