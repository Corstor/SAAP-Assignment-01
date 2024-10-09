package layered.persistence.user;

import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

public class Users {
    private static final List<User> users = new LinkedList<>();
    
    public static void createUser(String id) {
        if (getUserWithIdOptional(id).isEmpty()) {
            users.add(new UserImpl(id));
        } else {
            throw new UserAlreadyCreatedException(id);
        }
    }

    public static Optional<User> getUserWithIdOptional(String id) {
        return users.stream().filter(e -> e.getId().equals(id)).findFirst();
    }

    public static User getUserWithId(String id) {
        Optional<User> optionalUser = getUserWithIdOptional(id);
        if (optionalUser.isEmpty()) {
            throw new UserDoesNotExists(id);
        }
        return optionalUser.get();
    }
}
