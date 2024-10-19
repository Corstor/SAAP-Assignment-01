package layered.persistence;

import java.util.List;
import java.util.Optional;
import java.util.LinkedList;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.core.type.TypeReference;
import java.io.File;
import java.io.IOException;

public class UserStoreImpl implements UserStore {
    private final File jsonFile;
    private List<Pair<String, Integer>> users;
    private final ObjectMapper objectMapper;

    public UserStoreImpl() throws IOException {
        this.jsonFile = new File("users.json");
        this.objectMapper = new ObjectMapper();
        this.users = new LinkedList<>();
    }

    @Override
    public List<Pair<String, Integer>> loadAllUsers() throws IOException {
        return objectMapper.readValue(jsonFile, new TypeReference<List<Pair<String, Integer>>>() {});
    }

    private void saveAllUsers(List<Pair<String, Integer>> users) throws IOException {
        objectMapper.writerWithDefaultPrettyPrinter().writeValue(jsonFile, users);
    }

    @Override
    public void saveUser(Pair<String, Integer> user) throws IOException {
        this.users = loadAllUsers();
        this.users.add(user);
        this.saveAllUsers(this.users);
    }

    @Override
    public void deleteUser(String id) throws IOException {
        this.users = loadAllUsers().stream().filter(user -> !user.first().equals(id)).toList();
        this.saveAllUsers(this.users);
    }

    @Override
    public void changeUserCredit(String id, int credit) throws IOException {
        this.deleteUser(id);
        this.users.add(new Pair<>(id, credit));
        this.saveAllUsers(this.users);
    }

    @Override
    public Pair<String, Integer> getUserFromId(String id) throws IOException {
        return getUserFromIdOptional(id).get();
    }

    @Override
    public Optional<Pair<String, Integer>> getUserFromIdOptional(String id) throws IOException {
        this.users = loadAllUsers();
        return users.stream()
            .filter(user -> user.first().equals(id))
            .findFirst();
    }
}
