package clean.application;

import java.util.Date;
import java.util.Optional;

public interface Ride {
    String getId();
    String getUserId();
    String getBikeId();

    void start();
    void end();

    Date getStartedDate();
    boolean isOngoing();
    Optional<Date> getEndDate();
}
