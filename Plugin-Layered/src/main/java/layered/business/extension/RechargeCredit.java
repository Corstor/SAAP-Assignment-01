package layered.business.extension;

import java.io.IOException;

import layered.business.user.User;
import layered.business.user.UserFactory;

public class RechargeCredit implements Plugin {
    @Override
    public void applyPlugin(User user, UserFactory factory) throws IOException {
        user.rechargeCredit(100 - user.getCredit());
        factory.updateUser(user);
    }
}
