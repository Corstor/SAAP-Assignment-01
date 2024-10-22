package clean.application;

import clean.domain.P2d;
import clean.domain.V2d;
import clean.domain.ebike.EBike;
import clean.domain.ebike.EBikeListener;
import io.vertx.core.json.JsonObject;

class EBikeAPIImpl implements EBikeAPI {
    private final EBike bike;

    EBikeAPIImpl(EBike bike) {
        this.bike = bike;
    }

    @Override
    public JsonObject getEBikeState() {
        JsonObject info = new JsonObject();
        info.put("id", bike.getEBikeSnapshot().id());
        info.put("state", bike.getEBikeSnapshot().state());
        info.put("batteryLevel", bike.getEBikeSnapshot().batteryLevel());
        info.put("speed", bike.getEBikeSnapshot().speed());
        info.put("direction", bike.getEBikeSnapshot().direction());
        info.put("location", bike.getEBikeSnapshot().location());
        return info;
    }

    @Override
    public void rechargeBattery() {
        this.bike.rechargeBattery();
    }

    @Override
    public void decreaseBattery(int delta) {
        this.bike.decreaseBatteryLevel(delta);
    }

    @Override
    public boolean isAvailable() {
        return this.bike.isAvailable();
    }

    @Override
    public void moveTo(double x, double y) {
        this.bike.updateLocation(new P2d(x, y));
    }

    @Override
    public void turnLeft() {
        turn(90);
    }

    @Override
    public void turnRight() {
        turn(-90);
    }

    @Override
    public void turnBack() {
        turn(180);
    }

    @Override
    public void turn(double angle) {
        this.bike.updateDirection(getDirection().rotate(angle));
    }

    @Override
    public void setSpeed(double speed) {
        this.bike.updateSpeed(speed);
    }

    @Override
    public void subscribeEBikeListener(EBikeListener l) {
        this.bike.addEBikeListener(l);
    }

    public P2d getLocation() {
        return this.bike.getEBikeSnapshot().location();
    }

    public V2d getDirection() {
        return this.bike.getEBikeSnapshot().direction();
    }

    public double getSpeed() {
        return this.bike.getEBikeSnapshot().speed();
    }
}
