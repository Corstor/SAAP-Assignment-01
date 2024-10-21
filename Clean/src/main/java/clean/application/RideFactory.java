package clean.application;

import java.io.IOException;

public class RideFactory {
    private static RideFactory instance;
    private int id;

    private RideFactory() {
        this.id = 0;
    }

    public static RideFactory getInstance() {
        if (instance == null) {
            instance = new RideFactory();
        }
        return instance;
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
