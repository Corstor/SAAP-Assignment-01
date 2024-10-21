package clean.domain.user;

import java.io.IOException;

import clean.domain.Repository;

public class UserFactory {
    private final Repository<UserSnapshot> userRepository;

    public UserFactory(Repository<UserSnapshot> uRepository) throws IOException {
        this.userRepository = uRepository;
    }

    public User createUser(String id) throws IOException {
        checkUserExistance(id);

        var user = new UserImpl(id);
        this.userRepository.saveValue(id, user.getUserSnapshot());

        return user;
    }

    public User createUser(String id, int credit) throws IOException {
        checkUserExistance(id);

        var user = new UserImpl(id, credit);
        this.userRepository.saveValue(id, user.getUserSnapshot());

        return user;
    }

    private void checkUserExistance(String id) throws IOException {
        if (this.userRepository.getValueFromIdOptional(id).isPresent()) {
            throw new UserAlreadyCreatedException(id);
        }
    }

    public User getUserWithId(String id) throws IOException {
        var user = this.userRepository.getValueFromIdOptional(id);

        if (user.isEmpty()) {
            throw new UserDoesNotExists(id);
        }

        return new UserImpl(id, user.get());
    }
}
