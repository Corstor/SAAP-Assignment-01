package clean.application.ride;

import java.io.IOException;
import java.util.Date;
import java.util.Optional;

import clean.domain.ebike.EBike;
import clean.domain.user.User;

class RideImpl implements Ride {
    private final String id;
    private final Date startedDate;
    private Optional<Date> endDate;
    private final User user;
    private final EBike bike;
    private final RideSimulation rideSimulation;
    private boolean onGoing;

    RideImpl(String id, User user, EBike bike) throws IOException {
        this.id = id;
        this.user = user;
        this.bike = bike;

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
}
