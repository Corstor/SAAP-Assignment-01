package clean.application.ride;

import java.io.IOException;

public class RideFactory {
    private int id;

    public RideFactory() {
        this.id = 0;
    }

    public Ride createRide(String userId, String bikeId) throws IOException {
        return new RideImpl(realId(this.id++), userId, bikeId);
    }

    public String getId() {
        return realId(this.id);
    }

    private String realId(int id) {
        return "ride-" + id;
    }
}
