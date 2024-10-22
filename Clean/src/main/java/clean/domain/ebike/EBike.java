package clean.domain.ebike;

import clean.domain.P2d;
import clean.domain.V2d;

public interface EBike {
    EBikeSnapshot getEBikeSnapshot();
    
    void rechargeBattery();
    void decreaseBatteryLevel(int delta);
    boolean isAvailable();

    void updateBattery(int batteryLevel);
    void updateState(EBikeState state);
    void updateLocation(P2d location);
    void updateSpeed(double speed);
    void updateDirection(V2d direction);

    void addEBikeListener(EBikeListener l);
}
