package layered.persistence.user;

import java.io.IOException;
import java.util.LinkedList;
import java.util.List;

import layered.database.UserDB;
import layered.database.UserStore;
import layered.database.UserStoreImpl;

public class UserRepositoryImpl implements UserRepository {
    private UserStore store;
    private final UserFactory factory;

    public UserRepositoryImpl() {
        this.factory = UserFactory.getInstance();
        try {
            this.store = new UserStoreImpl();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public List<User> getUsers() {
        List<User> users = new LinkedList<>();
        try {
            users = this.store.loadAllUsers().stream()
            .map(userDB -> {
                this.factory.createUser(userDB.id(), userDB.credit());
                return this.factory.getUserWithId(userDB.id());
            }).toList();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return users;
    }

    @Override
    public void saveUsers(List<User> users) {
        try {
            this.store.saveAllUsers(
                users.stream()
                .map(user -> new UserDB(user.getId(), user.getCredit()))
                .toList()
            );
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void saveUser(User user) {
        try {
            this.store.saveUser(new UserDB(user.getId(), user.getCredit()));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void deleteUser(String id) {
        try {
            this.store.deleteUser(id);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void changeUserCredit(String id, int credit) {
        try {
            this.store.changeUserCredit(id, credit);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public User getUserFromId(String id) {
        UserDB user;
        try {
            user = this.store.getUserFromId(id);
            this.factory.createUser(user.id(), user.credit());
        } catch (IOException e) {
            e.printStackTrace();
        }
        
        return this.factory.getUserWithId(id);
    }
    
}
