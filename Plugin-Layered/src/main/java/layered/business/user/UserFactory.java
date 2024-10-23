package layered.business.user;

import java.io.IOException;

public interface UserFactory {
    void createUser(String id) throws IOException;
    
    void createUser(String id, int credit) throws IOException;

    User getUserWithId(String id) throws IOException;

    void updateUser(User user) throws IOException;
}
