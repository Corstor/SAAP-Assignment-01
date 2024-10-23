package clean.domain.ebike;

import java.util.LinkedList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

import clean.domain.Listener;
import clean.domain.P2d;
import clean.domain.V2d;

class EBikeImpl implements EBike {

    private final String id;
    private EBikeState state;
    private P2d location;
    private V2d direction;
    private double speed;
    private int batteryLevel;
    private final List<Listener<EBikeSnapshot>> listeners;

    @JsonCreator
    EBikeImpl(@JsonProperty("id") final String id) {
        this.id = id;
        this.state = EBikeState.AVAILABLE;
        this.location = new P2d(0, 0);
        this.direction = new V2d(1, 0);
        this.batteryLevel = 100;
        this.speed = 0;

        this.listeners = new LinkedList<>();
    }

    EBikeImpl(final EBikeSnapshot snapshot) {
        this.id = snapshot.id();
        this.state = snapshot.state();
        this.location = snapshot.location();
        this.direction = snapshot.direction();
        this.batteryLevel = snapshot.batteryLevel();
        this.speed = snapshot.speed();
        this.listeners = new LinkedList<>();
    }

    @Override
    public EBikeSnapshot getEBikeSnapshot() {
        return new EBikeSnapshot(id, state, batteryLevel, speed, direction, location);
    }

    @Override
    public void rechargeBattery() {
        this.batteryLevel = 100;
        this.state = EBikeState.AVAILABLE;
        updateListeners();
    }

    @Override
    public void decreaseBatteryLevel(int delta) {
        this.batteryLevel -= delta;
        if (this.batteryLevel <= 0) {
            this.batteryLevel = 0;
            this.state = EBikeState.MAINTENANCE;
        }
        updateListeners();
    }

    @JsonIgnore
    @Override
    public boolean isAvailable() {
        return this.state.equals(EBikeState.AVAILABLE);
    }

    @Override
    public void updateBattery(int batteryLevel) {
        this.batteryLevel = batteryLevel;
        updateListeners();
    }

    @Override
    public void updateState(EBikeState state) {
        this.state = state;
        updateListeners();
    }

    @Override
    public void updateLocation(P2d location) {
        this.location = location;
        updateListeners();
    }

    @Override
    public void updateSpeed(double speed) {
        this.speed = speed;
        updateListeners();
    }

    @Override
    public void updateDirection(V2d direction) {
        this.direction = direction;
        updateListeners();
    }

    private void updateListeners() {
        this.listeners.forEach(e -> e.eventOccured(this.getEBikeSnapshot()));
    }

    public String toString() {
		return "EBike: " + this.id 
            + ", location: " + this.location 
            + ", battery level: " + this.batteryLevel
            + ", state: " + this.state;
	}

    @Override
    public void addEBikeListener(Listener<EBikeSnapshot> l) {
        this.listeners.add(l);
    }
}
