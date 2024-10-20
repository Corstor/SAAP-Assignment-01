package layered.business.extension;

import layered.business.user.User;

public interface Plugin {
    void applyPlugin(User user);
}
