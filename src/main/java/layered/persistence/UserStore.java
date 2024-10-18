package layered.persistence;

import java.util.List;
import java.util.Optional;
import java.io.IOException;


public interface UserStore {
    List<UserDB> loadAllUsers() throws IOException;

    void saveUser(UserDB user) throws IOException;
    void deleteUser(String id) throws IOException;

    void changeUserCredit(String id, int credit) throws IOException;
    UserDB getUserFromId(String id) throws IOException;
    Optional<UserDB> getUserFromIdOptional(String id) throws IOException;
}
