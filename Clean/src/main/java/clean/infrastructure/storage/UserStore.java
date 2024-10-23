package clean.infrastructure.storage;

import java.io.IOException;
import java.util.List;

import com.fasterxml.jackson.core.type.TypeReference;

import clean.domain.user.UserSnapshot;

public class UserStore extends AbstractStore<UserSnapshot> {
    public UserStore() throws IOException {
        super("Users.json");
    }

    public List<UserSnapshot> loadAllValues() throws IOException {
        return objectMapper.readValue(jsonFile, new TypeReference<List<UserSnapshot>>() {
        });
    }

    @Override
    public void eventOccured(UserSnapshot user) {
        try {
            this.updateValue(user);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
