package clean.application;

import clean.domain.ebike.EBikeListener;
import io.vertx.core.json.JsonObject;

public interface EBikeAPI {
    JsonObject getEBikeState();

    void rechargeBattery();
    void decreaseBattery(int delta);

    boolean isAvailable();

    void moveTo(double x, double y);
    void turnLeft();
    void turnRight();
    void turnBack();
    void turn(double angle);
    void setSpeed(double speed);

    void subscribeEBikeListener(EBikeListener l);
}
