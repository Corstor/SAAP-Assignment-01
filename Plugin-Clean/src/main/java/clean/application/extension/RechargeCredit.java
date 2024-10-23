package clean.application.extension;

import java.io.IOException;

import clean.application.State;

public class RechargeCredit implements Plugin {

    @Override
    public void applyPlugin(State state) throws IOException {
        var user = state.getUser();
        user.rechargeCredit(100 - user.getUserSnapshot().credit());
        user.updated();
    }
    
}
