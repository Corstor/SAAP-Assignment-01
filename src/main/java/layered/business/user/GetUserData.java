package layered.business.user;

import java.util.List;

import layered.business.Pair;

public interface GetUserData {
    List<Pair<String, Integer>> getUsers();

    Pair<String, Integer> getUserFromId(String id);
}
