package clean.application.extension;

import java.io.IOException;

import clean.domain.user.User;

public interface Plugin {
    void applyPlugin(User user) throws IOException;
}
