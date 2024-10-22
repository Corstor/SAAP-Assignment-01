package clean.application;

import java.util.List;

import clean.domain.ebike.EBike;
import clean.domain.user.User;

public interface State {
    User getUser();
    List<EBike> getBikes();
}
