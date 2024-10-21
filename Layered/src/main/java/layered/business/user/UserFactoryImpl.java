package layered.business.user;

import java.io.IOException;

import layered.persistence.Store;
import layered.persistence.StoreImpl;

public class UserFactoryImpl implements UserFactory {
    private static UserFactoryImpl instance;
    private final Store<UserImpl> userStore;

    private UserFactoryImpl() throws IOException {
        userStore = new StoreImpl<>("Users", UserImpl.class);
    }

    public static UserFactoryImpl getInstance() throws IOException {
        if (instance == null) {
            instance = new UserFactoryImpl();
        }
        return instance;
    }
    
    public void createUser(String id) throws IOException {
        checkUserExistance(id);

        this.userStore.saveValue(id, new UserImpl(id));
    }
    
    public void createUser(String id, int credit) throws IOException {
        checkUserExistance(id);

        this.userStore.saveValue(id, new UserImpl(id, credit));
    }
    
    private void checkUserExistance(String id) throws IOException {
        if (this.userStore.getValueFromIdOptional(id).isPresent()) {
            throw new UserAlreadyCreatedException(id);
        }
    }

    public User getUserWithId(String id) throws IOException {
        var user = this.userStore.getValueFromIdOptional(id);
        if (user.isEmpty()) {
            throw new UserDoesNotExists(id);
        }
        return user.get();
    }

    public void updateUser(User user) throws IOException {
        this.userStore.saveValue(user.getId(), (UserImpl) user);
    }
}
