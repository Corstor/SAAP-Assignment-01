package layered.database;

import java.util.List;
import java.util.LinkedList;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.core.type.TypeReference;
import java.io.File;
import java.io.IOException;
import java.util.Optional;

public class UserStoreImpl implements UserStore {
    private final File jsonFile;
    private final List<UserDB> users;
    private final ObjectMapper objectMapper;

    public UserStoreImpl() throws IOException {
        this.jsonFile = new File("users.json");
        this.objectMapper = new ObjectMapper();
        this.users = new LinkedList<>();

        this.saveAllUsers(this.users); //Create the file
    }

    @Override
    public void saveUser(UserDB user) throws IOException {
        this.users.add(user);
        this.saveAllUsers(this.users);
    }

    @Override
    public void changeUserCredit(String id, int credit) throws IOException {
        Optional<UserDB> userOpt = users.stream()
        .filter(user -> user.id().equals(id))
        .findFirst();

        if (userOpt.isPresent()) {
            UserDB user = userOpt.get();
            this.users.remove(user);
            this.users.add(new UserDB(user.id(), credit));
            this.saveAllUsers(this.users);
        }
    }

    @Override
    public UserDB getUserFromId(String id) {
        return users.stream()
            .filter(user -> user.id().equals(id))
            .findFirst().get();
    }

    @Override
    public List<UserDB> loadAllUsers() throws IOException {
        this.users.clear();
        this.users.addAll(objectMapper.readValue(jsonFile, new TypeReference<List<UserDB>>() {}));

        return this.users;
    }

    @Override
    public void saveAllUsers(List<UserDB> users) throws IOException {
        objectMapper.writerWithDefaultPrettyPrinter().writeValue(jsonFile, users);
    }
    
}
