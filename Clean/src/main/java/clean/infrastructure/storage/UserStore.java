package clean.infrastructure.storage;

import java.io.IOException;
import java.util.List;

import com.fasterxml.jackson.core.type.TypeReference;

import clean.domain.user.UserSnapshot;
import clean.domain.user.UserListener;

public class UserStore extends AbstractStore<UserSnapshot> implements UserListener {
    public UserStore() throws IOException {
        super("Users.json");
    }

    public List<UserSnapshot> loadAllValues() throws IOException {
        return objectMapper.readValue(jsonFile, new TypeReference<List<UserSnapshot>>() {
        });
    }

    @Override
    public void userCreditChanged(UserSnapshot user) {
        try {
            this.saveValue(user);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
