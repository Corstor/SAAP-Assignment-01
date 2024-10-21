package layered.business.ebike;

import layered.business.P2d;
import layered.business.V2d;

public interface EBike {
    String getId();
    EBikeState getState();
    int getBatteryLevel();
    double getSpeed();
    V2d getDirection();
    P2d getLocation();
    
    void rechargeBattery();
    void decreaseBatteryLevel(int delta);
    boolean isAvailable();

    void updateState(EBikeState state);
    void updateLocation(P2d location);
    void updateSpeed(double speed);
    void updateDirection(V2d direction);
}
