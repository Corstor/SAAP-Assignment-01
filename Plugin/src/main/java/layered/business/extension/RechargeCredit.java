package layered.business.extension;

import layered.business.user.User;

public class RechargeCredit implements Plugin {
    @Override
    public void applyPlugin(User user) {
        user.rechargeCredit(100 - user.getCredit());
    }
}
