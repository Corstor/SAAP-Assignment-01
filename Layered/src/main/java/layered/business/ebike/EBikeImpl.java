package layered.business.ebike;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

import layered.business.P2d;
import layered.business.V2d;

public class EBikeImpl implements EBike {

    private final String id;
    private EBikeState state;
    private P2d location;
    private V2d direction;
    private double speed;
    private int batteryLevel;

    @JsonCreator
    public EBikeImpl(@JsonProperty("id") final String id) {
        this.id = id;
        this.state = EBikeState.AVAILABLE;
        this.location = new P2d(0, 0);
        this.direction = new V2d(1, 0);
        this.batteryLevel = 100;
        this.speed = 0;
    }

    @Override
    public String getId() {
        return this.id;
    }

    @Override
    public EBikeState getState() {
        return this.state;
    }

    @Override
    public int getBatteryLevel() {
        return this.batteryLevel;
    }

    @Override
    public double getSpeed() {
        return this.speed;
    }

    @Override
    public V2d getDirection() {
        return this.direction;
    }

    @Override
    public P2d getLocation() {
        return this.location;
    }

    @Override
    public void rechargeBattery() {
        this.batteryLevel = 100;
        this.state = EBikeState.AVAILABLE;
    }

    @Override
    public void decreaseBatteryLevel(int delta) {
        this.batteryLevel -= delta;
        if (this.batteryLevel <= 0) {
            this.batteryLevel = 0;
            this.state = EBikeState.MAINTENANCE;
        }
    }

    @JsonIgnore
    @Override
    public boolean isAvailable() {
        return this.state.equals(EBikeState.AVAILABLE);
    }

    @Override
    public void updateState(EBikeState state) {
        this.state = state;
    }

    @Override
    public void updateLocation(P2d location) {
        this.location = location;
    }

    @Override
    public void updateSpeed(double speed) {
        this.speed = speed;
    }

    @Override
    public void updateDirection(V2d direction) {
        this.direction = direction;
    }

    public String toString() {
		return "EBike: " + this.id 
            + ", location: " + this.location 
            + ", battery level: " + this.batteryLevel
            + ", state: " + this.state;
	}
}
