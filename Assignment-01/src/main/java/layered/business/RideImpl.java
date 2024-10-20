package layered.business;

import java.io.IOException;
import java.util.Date;
import java.util.Optional;

import layered.business.ebike.EBike;
import layered.business.ebike.EBikeFactory;
import layered.business.user.User;
import layered.business.user.UserFactoryImpl;

class RideImpl implements Ride {
    private final String id;
    private final Date startedDate;
    private Optional<Date> endDate;
    private final User user;
    private final EBike bike;
    private final RideSimulation rideSimulation;
    private boolean onGoing;

    RideImpl(String id, String userId, String bikeId) throws IOException {
        this.id = id;
        this.user = UserFactoryImpl.getInstance().getUserWithId(userId);
        this.bike = EBikeFactory.getInstance().getEBikeWithId(bikeId);

        this.startedDate = new Date();
        this.endDate = Optional.empty();
        this.rideSimulation = new RideSimulation(this.user, this.bike);
    }

    @Override
    public String getId() {
        return this.id;
    }

    @Override
    public Date getStartedDate() {
        return this.startedDate;
    }

    @Override
    public boolean isOngoing() {
        return this.onGoing;
    }

    @Override
    public Optional<Date> getEndDate() {
        return this.endDate;
    }

    @Override
    public void start() {
        onGoing = true;
        this.rideSimulation.start();
    }

    @Override
    public void end() {
        endDate = Optional.of(new Date());
        this.rideSimulation.stopSimulation();
        onGoing = false;
    }

    @Override
    public String getUserId() {
        return this.user.getId();
    }

    @Override
    public String getBikeId() {
        return this.bike.getId();
    }
}
