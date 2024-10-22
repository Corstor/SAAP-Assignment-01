package clean.application;

import java.util.List;

import clean.domain.ebike.EBike;
import clean.domain.user.User;

public class StateImpl implements State {
    private final User user;
    private final List<EBike> bikes;

    public StateImpl(User user, List<EBike> bikes) {
        this.user = user;
        this.bikes = bikes;
    }

    @Override
    public User getUser() {
        return this.user;
    }

    @Override
    public List<EBike> getBikes() {
        return this.bikes;
    }
    
}
