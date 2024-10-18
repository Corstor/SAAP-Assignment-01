package layered.persistence;

import java.util.List;
import java.util.Optional;
import java.io.IOException;


public interface UserStore {
    List<Pair<String, Integer>> loadAllUsers() throws IOException;

    void saveUser(Pair<String, Integer> user) throws IOException;
    void deleteUser(String id) throws IOException;

    void changeUserCredit(String id, int credit) throws IOException;
    Pair<String, Integer> getUserFromId(String id) throws IOException;
    Optional<Pair<String, Integer>> getUserFromIdOptional(String id) throws IOException;
}
