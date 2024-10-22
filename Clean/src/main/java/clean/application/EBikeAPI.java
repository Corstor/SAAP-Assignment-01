package clean.application;

import io.vertx.core.json.JsonObject;

public interface EBikeAPI {
    JsonObject getEBikeState();

    void rechargeBattery();
    void decreaseBattery(int delta);

    boolean isAvailable();

    void moveTo(double x, double y);
}
