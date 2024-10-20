package layered.business.extension;

import java.io.IOException;

import layered.business.user.User;
import layered.business.user.UserFactory;

public interface Plugin {
    void applyPlugin(User user, UserFactory factory) throws IOException;
}
