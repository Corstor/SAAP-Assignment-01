package clean.application.ride;

import java.io.IOException;

import clean.domain.ebike.EBikeFactory;
import clean.domain.user.UserFactory;

public class RideFactoryImpl implements RideFactory {
    private static RideFactory istance;
    private int id;

    private RideFactoryImpl() {
        this.id = 0;
    }

    public static RideFactory getIstance() {
        if (istance == null) {
            istance = new RideFactoryImpl();
        }
        return istance;
    }

    @Override
    public Ride createRide(String userId, String bikeId) throws IOException {
        var user = UserFactory.getIstance().getUserWithId(userId);
        var bike = EBikeFactory.getIstance().getEBikeWithId(bikeId);
        if (!bike.isAvailable()) {
            throw new BikeAlreadyRent(bike.getEBikeSnapshot().id());
        }

        return new RideImpl(realId(this.id++), user, bike);
    }

    @Override
    public String getId() {
        return realId(this.id);
    }

    private String realId(int id) {
        return "ride-" + id;
    }
}
