package layered.business;

import java.util.Date;
import java.util.Optional;

import layered.persistence.ebike.EBike;
import layered.persistence.user.User;

public interface Ride {
    String getId();

    void start();

    void end();

    Date getStartedDate();

    boolean isOngoing();

    Optional<Date> getEndDate();

    User getUser();

    EBike getEBike();
}
