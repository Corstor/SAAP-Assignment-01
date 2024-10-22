package clean.application;

import java.util.Date;
import java.util.Optional;

import io.vertx.core.json.JsonObject;

public interface Ride {
    String getId();
    JsonObject getUser();
    JsonObject getBike();

    void start();
    void end();

    Date getStartedDate();
    boolean isOngoing();
    Optional<Date> getEndDate();
}
