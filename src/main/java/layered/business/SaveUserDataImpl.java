package layered.business;

import layered.persistence.user.UserRepository;
import layered.persistence.user.UserRepositoryImpl;

public class SaveUserDataImpl implements SaveUserData {
    private final UserRepository repo = new UserRepositoryImpl();

    @Override
    public void saveUser(String id) {
        this.repo.saveUser(id);
    }

    @Override
    public void deleteUser(String id) {
        this.repo.deleteUser(id);
    }

    @Override
    public void changeUserCredit(String id, int credit) {
        this.repo.changeUserCredit(id, credit);
    }
}
