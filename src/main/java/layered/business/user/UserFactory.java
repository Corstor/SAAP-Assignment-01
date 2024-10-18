package layered.business.user;

import java.io.IOException;

import layered.persistence.UserDB;
import layered.persistence.UserStore;
import layered.persistence.UserStoreImpl;

public class UserFactory {
    private static UserFactory instance;
    private final UserStore userStore;

    private UserFactory() throws IOException {
        userStore = new UserStoreImpl();
    }

    public static UserFactory getInstance() throws IOException {
        if (instance == null) {
            instance = new UserFactory();
        }
        return instance;
    }
    
    public void createUser(String id) throws IOException {
        if (this.userStore.getUserFromIdOptional(id).isEmpty()) {
            this.userStore.saveUser(new UserDB(id, 0));
        } else {
            throw new UserAlreadyCreatedException(id);
        }
    }

    public void createUser(String id, int credit) throws IOException {
        this.createUser(id);
        this.userStore.changeUserCredit(id, credit);
    }

    public User getUserWithId(String id) throws IOException {
        var user = this.userStore.getUserFromIdOptional(id);
        if (user.isPresent()) {
            return new UserImpl(id, user.get().credit());
        } else {
            throw new UserDoesNotExists(id);
        }
    }
}
