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
        this.users.add(new UserImpl(id));
    }
    
}
