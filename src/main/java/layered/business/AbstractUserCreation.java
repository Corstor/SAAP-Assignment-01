package layered.business;

import java.util.List;
import java.util.LinkedList;

import layered.persistence.User;
import layered.persistence.UserImpl;

public abstract class AbstractUserCreation implements UserCreation {
    private final List<User> users;

    public AbstractUserCreation() {
        this.users = new LinkedList<>();
    }

    @Override
    public void createUser(String id) {
        if (this.userDoesNotExists(id)) {
            User user = new UserImpl(id);
            user.rechargeCredit(100);
            this.users.add(user);
        } else {
            
        }
        // TODO throws a personalized exception
    }

    private boolean userDoesNotExists(String id) {
        return this.users.stream().filter(e -> e.getId() == id).toList().isEmpty();
    }
}
