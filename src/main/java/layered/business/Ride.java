package layered.business;

import java.util.Date;
import java.util.Optional;

public interface Ride {
    String getId();

    void start();

    void end();

    Date getStartedDate();

    boolean isOngoing();

    Optional<Date> getEndDate();
}
