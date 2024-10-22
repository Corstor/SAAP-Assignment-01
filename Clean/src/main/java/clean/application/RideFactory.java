package clean.application;

import java.io.IOException;

import clean.domain.ebike.EBikeFactory;
import clean.domain.user.UserFactory;

public class RideFactory {
    private int id;
    private final UserFactory userFactory;
    private final EBikeFactory bikeFactory;

    public RideFactory(UserFactory userFactory, EBikeFactory bikeFactory) {
        this.id = 0;
        this.userFactory = userFactory;
        this.bikeFactory = bikeFactory;
    }

    public Ride createRide(String userId, String bikeId) throws IOException {
        var user = userFactory.getUserWithId(bikeId);
        var bike = bikeFactory.getEBikeWithId(bikeId);
        return new RideImpl(realId(this.id++), new UserAPIImpl(user), new EBikeAPIImpl(bike));
    }

    public String getId() {
        return realId(this.id);
    }

    private String realId(int id) {
        return "ride-" + id;
    }
}
