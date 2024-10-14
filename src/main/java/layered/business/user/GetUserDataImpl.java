package layered.business.user;

import java.util.List;

import layered.business.Pair;
import layered.persistence.user.UserRepository;
import layered.persistence.user.UserRepositoryImpl;

public class GetUserDataImpl implements GetUserData {
    private final UserRepository repo = new UserRepositoryImpl();

    @Override
    public List<Pair<String, Integer>> getUsers() {
        return this.repo.getUsers().stream()
            .map(user -> new Pair<>(user.getId(), user.getCredit()))
            .toList();
    }

    @Override
    public Pair<String, Integer> getUserFromId(String id) {
        var user = this.repo.getUserFromId(id);
        return new Pair<String,Integer>(user.getId(), user.getCredit());
    }

}
