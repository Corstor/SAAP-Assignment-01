package layered.business.user;

import java.io.IOException;
import layered.persistence.Store;
import layered.persistence.StoreImpl;

public class UserFactory {
    private static UserFactory instance;
    private final Store<User> userStore;

    private UserFactory() throws IOException {
        userStore = new StoreImpl<>();
    }

    public static UserFactory getInstance() throws IOException {
        if (instance == null) {
            instance = new UserFactory();
        }
        return instance;
    }
    
    public void createUser(String id) throws IOException {
        if (this.userStore.getValueFromIdOptional(id).isEmpty()) {
            this.userStore.saveValue("user:" + id, new UserImpl(id));
        } else {
            throw new UserAlreadyCreatedException(id);
        }
    }

    public void createUser(String id, int credit) throws IOException {
        if (this.userStore.getValueFromIdOptional(id).isEmpty()) {
            this.userStore.saveValue("user:" + id, new UserImpl(id, credit));
        } else {
            throw new UserAlreadyCreatedException(id);
        }
    }

    public User getUserWithId(String id) throws IOException {
        var user = this.userStore.getValueFromIdOptional(id);
        if (user.isPresent()) {
            return user.get();
        } else {
            throw new UserDoesNotExists(id);
        }
    }
}
