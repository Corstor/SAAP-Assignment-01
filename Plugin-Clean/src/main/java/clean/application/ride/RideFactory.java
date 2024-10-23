package clean.application.ride;

import java.io.IOException;

public interface RideFactory {
    public Ride createRide(String userId, String bikeId) throws IOException;
    public String getId();
}
