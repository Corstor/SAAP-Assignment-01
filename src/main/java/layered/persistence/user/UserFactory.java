package layered.persistence.user;

import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

public class UserFactory {
    private static UserFactory instance;
    private final List<User> users;

    private UserFactory() {
        users = new LinkedList<>();
    }

    public static UserFactory getInstance() {
        if (instance == null) {
            instance = new UserFactory();
        }
        return instance;
    }
    
    public void createUser(String id) {
        if (getUserWithIdOptional(id).isEmpty()) {
            users.add(new UserImpl(id));
        } else {
            throw new UserAlreadyCreatedException(id);
        }
    }

    public void createUser(String id, int credit) {
        this.createUser(id);
        this.getUserWithId(id).rechargeCredit(credit);
    }

    public Optional<User> getUserWithIdOptional(String id) {
        return users.stream().filter(e -> e.getId().equals(id)).findFirst();
    }

    public User getUserWithId(String id) {
        Optional<User> optionalUser = getUserWithIdOptional(id);
        if (optionalUser.isEmpty()) {
            throw new UserDoesNotExists(id);
        }
        return optionalUser.get();
    }
}
