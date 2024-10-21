package clean.infrastructure;

import java.io.IOException;
import java.util.Map;

import com.fasterxml.jackson.core.type.TypeReference;

import clean.domain.user.UserSnapshot;
import clean.domain.user.UsersListener;

public class UserStore extends AbstractStore<UserSnapshot> implements UsersListener {
    public UserStore() throws IOException {
        super("Users.json");
    }

    public Map<String, UserSnapshot> loadAllValues() throws IOException {
        return objectMapper.readValue(jsonFile, new TypeReference<Map<String, UserSnapshot>>() {
        });
    }

    @Override
    public void userCreditChanged(String userId, UserSnapshot user) {
        this.values.put(userId, user);

        try {
            this.saveAllValues();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
