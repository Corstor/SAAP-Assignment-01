package clean.application.ride;

import java.io.IOException;

public class RideFactory {
    private static RideFactory istance;
    private int id;

    private RideFactory() {
        this.id = 0;
    }

    public static RideFactory getIstance() {
        if (istance == null) {
            istance = new RideFactory();
        }
        return istance;
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
