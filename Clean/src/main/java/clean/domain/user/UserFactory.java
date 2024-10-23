package clean.domain.user;

import java.io.IOException;
import java.util.List;
import java.util.LinkedList;
import java.util.Optional;
import java.util.function.Function;

import clean.domain.Repository;

public class UserFactory {
    private static UserFactory istance;
    private Repository<UserSnapshot> userRepository;
    private final List<User> users;

    private UserFactory() {
        this.users = new LinkedList<>();
    }

    public static UserFactory getIstance() {
        if (istance == null) {
            istance = new UserFactory();
        }
        return istance;
    }

    public void setRepository(Repository<UserSnapshot> repo) throws IOException {
        this.userRepository = repo;
        this.users.forEach(user -> {
            try {
                userRepository.saveValue(user.getUserSnapshot());
            } catch (IOException e1) {
                e1.printStackTrace();
            }
        });
        this.users.clear();
        this.users.addAll(this.userRepository.loadAllValues().stream()
                .map(snapshot -> {
                    User user = new UserImpl(snapshot);
                    return user;
                }).toList());

    }

    public User createUser(String id) throws IOException {
        checkUserExistance(id, (user) -> user.isPresent(), new UserAlreadyCreatedException(id));

        var user = new UserImpl(id);
        saveUser(user);

        return user;
    }

    public User createUser(String id, int credit) throws IOException {
        checkUserExistance(id, (user) -> user.isPresent(), new UserAlreadyCreatedException(id));

        var user = new UserImpl(id, credit);
        saveUser(user);

        return user;
    }

    private void saveUser(User user) throws IOException{
        this.userRepository.saveValue(user.getUserSnapshot());
        this.users.add(user);
        user.addUserListener(this.userRepository);
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
