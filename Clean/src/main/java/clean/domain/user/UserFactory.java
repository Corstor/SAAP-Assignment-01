package clean.domain.user;

import java.io.IOException;
import java.util.List;
import java.util.Optional;
import java.util.function.Function;

import clean.domain.Repository;

public class UserFactory {
    private final Repository<UserSnapshot> userRepository;
    private final List<User> users;

    public UserFactory(Repository<UserSnapshot> uRepository) throws IOException {
        this.userRepository = uRepository;
        this.users = this.userRepository.loadAllValues().stream()
                .map(e -> {
                    User user = new UserImpl(e);
                    return user;
                }).toList();
    }

    public User createUser(String id) throws IOException {
        checkUserExistance(id, (user) -> user.isPresent(), new UserAlreadyCreatedException(id));

        var user = new UserImpl(id);
        this.userRepository.saveValue(user.getUserSnapshot());
        this.users.add(user);

        return user;
    }

    public User createUser(String id, int credit) throws IOException {
        checkUserExistance(id, (user) -> user.isPresent(), new UserAlreadyCreatedException(id));

        var user = new UserImpl(id, credit);
        this.userRepository.saveValue(user.getUserSnapshot());
        this.users.add(user);

        return user;
    }

    private Optional<User> checkUserExistance(String id, Function<Optional<User>, Boolean> consumer,
            IllegalArgumentException exception) throws IOException {
        var user = this.users.stream().filter(e -> e.getUserSnapshot().id().equals(id)).findFirst();
        if (consumer.apply(user)) {
            throw exception;
        }
        return user;
    }

    public User getUserWithId(String id) throws IOException {
        return checkUserExistance(id, (user) -> user.isEmpty(), new UserDoesNotExists(id)).get();
    }

    public void deleteUser(String id) throws IOException {
        var user = checkUserExistance(id, (u) -> u.isEmpty(), new UserDoesNotExists(id)).get();
        this.userRepository.deleteValue(user.getUserSnapshot());
        this.users.remove(user);
    }
}
