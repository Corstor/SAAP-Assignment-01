package clean.application.extension;

import java.io.IOException;
import java.util.Date;
import java.util.Optional;

import clean.application.ride.Ride;
import clean.domain.ebike.EBike;
import clean.domain.user.User;

class CustomRide implements Ride {
    private final String id;
    private final Date startedDate;
    private Optional<Date> endDate;
    private final User user;
    private final EBike bike;
    private final CustomRideSimulation rideSimulation;
    private boolean onGoing;

    CustomRide(String id, User user, EBike bike) throws IOException {
        this.id = id;
        this.user = user;
        this.bike = bike;

        this.startedDate = new Date();
        this.endDate = Optional.empty();
        this.rideSimulation = new CustomRideSimulation(this, this.user, this.bike);
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
