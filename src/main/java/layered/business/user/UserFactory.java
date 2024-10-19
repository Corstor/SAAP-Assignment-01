package layered.business.user;

import java.io.IOException;
import java.util.Optional;

import layered.persistence.Store;
import layered.persistence.StoreImpl;

public class UserFactory {
    private static UserFactory instance;
    private final Store<UserImpl> userStore;

    private UserFactory() throws IOException {
        userStore = new StoreImpl<>("Users", UserImpl.class);
    }

    public static UserFactory getInstance() throws IOException {
        if (instance == null) {
            instance = new UserFactory();
        }
        return instance;
    }
    
    public void createUser(String id) throws IOException {
        if (getUserFromStore(id).isEmpty()) {
            this.userStore.saveValue("user:" + id, new UserImpl(id));
        } else {
            throw new UserAlreadyCreatedException(id);
        }
    }

    private Optional<User> getUserFromStore(String id) throws IOException {
        Optional<User> user = Optional.empty();
        System.out.println("storedUser");
        var storedUser = this.userStore.getValueFromIdOptional("user:" + id);
        if (storedUser.isPresent()) {
            user = Optional.of(storedUser.get());
        }
        return user;
    }

    public void createUser(String id, int credit) throws IOException {
        if (getUserFromStore(id).isEmpty()) {
            this.userStore.saveValue("user:" + id, new UserImpl(id, credit));
        } else {
            throw new UserAlreadyCreatedException(id);
        }
    }

    public User getUserWithId(String id) throws IOException {
        var user = getUserFromStore(id);
        if (user.isPresent()) {
            return user.get();
        } else {
            throw new UserDoesNotExists(id);
        }
    }
}
