package layered.persistence;

public interface EBike {
    String getId();
    EBikeState getState();
    int getBatteryLevel();
    double getSpeed();
    V2d getDirection();
    P2d getPosition();
    
    void rechargeBattery();
    void decreaseBattery(final int delta);
    boolean isAvailable();

    void updateState(final EBikeState state);
    void updateLocation(final P2d location);
    void updateSpeed(final double speed);
    void updateDirection(final V2d direction);
}
