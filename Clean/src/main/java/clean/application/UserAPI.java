package clean.application;

import clean.domain.user.UserListener;
import io.vertx.core.json.JsonObject;

public interface UserAPI {
    JsonObject getUserState();

    void addCredit(int amount);
    void removeCredit(int amount);

    void subscribeUserListener(UserListener l);
}
