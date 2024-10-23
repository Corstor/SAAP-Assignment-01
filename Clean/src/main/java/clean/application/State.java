package clean.application;

import java.util.List;

import clean.application.ride.RideFactory;
import clean.domain.ebike.EBike;
import clean.domain.user.User;

public interface State {
    User getUser();
    List<EBike> getBikes();
    RideFactory getRideFactory();
}
