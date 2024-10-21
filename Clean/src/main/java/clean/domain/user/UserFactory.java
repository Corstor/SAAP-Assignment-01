package clean.domain.user;

import java.io.IOException;
import java.util.List;

import clean.domain.Repository;

public class UserFactory {
    private final Repository<UserSnapshot> userRepository;
    private final List<User> users;

    public UserFactory(Repository<UserSnapshot> uRepository) throws IOException {
        this.userRepository = uRepository;
        this.users = this.userRepository.loadAllValues().entrySet().stream()
                .map(e -> {
                    User user = new UserImpl(e.getKey(), e.getValue());
                    return user;
                }).toList();
    }

    public User createUser(String id) throws IOException {
        checkUserExistance(id);

        var user = new UserImpl(id);
        this.userRepository.saveValue(id, user.getUserSnapshot());
        this.users.add(user);

        return user;
    }

    public User createUser(String id, int credit) throws IOException {
        checkUserExistance(id);

        var user = new UserImpl(id, credit);
        this.userRepository.saveValue(id, user.getUserSnapshot());
        this.users.add(user);

        return user;
    }

    private void checkUserExistance(String id) throws IOException {
        if (this.users.stream().filter(e -> e.getId().equals(id)).findFirst().isPresent()) {
            throw new UserAlreadyCreatedException(id);
        }
    }

    public User getUserWithId(String id) throws IOException {
        var user = this.users.stream().filter(e -> e.getId().equals(id)).findFirst();

        if (user.isEmpty()) {
            throw new UserDoesNotExists(id);
        }

        return user.get();
    }
}
