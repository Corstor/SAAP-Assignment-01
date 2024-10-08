package layered.persistence.user;

import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

public class Users {
    private static final List<User> users = new LinkedList<>();
    
    public static void createUser(String id) {
        if (getUserWithId(id).isEmpty()) {
            users.add(new UserImpl(id));
        } else {
            throw new UserAlreadyCreatedException(id);
        }
    }

    public static Optional<User> getUserWithId(String id) {
        return users.stream().filter(e -> e.getId() == id).findFirst();
    }
}
