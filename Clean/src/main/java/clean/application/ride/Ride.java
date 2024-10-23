package clean.application.ride;

import java.util.Date;
import java.util.Optional;

import clean.domain.ebike.EBikeSnapshot;
import clean.domain.user.UserSnapshot;

public interface Ride {
    String getId();
    UserSnapshot getUser();
    EBikeSnapshot getBike();

    void start();
    void end();

    Date getStartedDate();
    boolean isOngoing();
    Optional<Date> getEndDate();
}
