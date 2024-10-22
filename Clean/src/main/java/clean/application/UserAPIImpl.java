package clean.application;

import clean.domain.user.User;
import clean.domain.user.UserListener;
import io.vertx.core.json.JsonObject;

class UserAPIImpl implements UserAPI {
    private final User user;

    UserAPIImpl(User user) {
        this.user = user;
    }

    @Override
    public JsonObject getUserState() {
        JsonObject state = new JsonObject();
        state.put("id", user.getUserSnapshot().id());
        state.put("credit", user.getUserSnapshot().credit());
        return state;
    }

    @Override
    public void addCredit(int amount) {
        this.user.rechargeCredit(amount);
    }

    @Override
    public void removeCredit(int amount) {
        this.user.decreaseCredit(amount);
    }

    @Override
    public void subscribeUserListener(UserListener l) {
        this.user.addUserListener(l);
    }

}
