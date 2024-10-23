package clean.application;

import java.io.IOException;
import java.util.List;

import clean.domain.ebike.EBike;
import clean.domain.ebike.EBikeFactory;
import clean.domain.user.User;
import clean.domain.user.UserFactory;

public class StateImpl implements State {
    private final User user;
    private final List<EBike> bikes;

    public StateImpl(String userId) throws IOException {
        this.user = UserFactory.getIstance().getUserWithId(userId);
        this.bikes = EBikeFactory.getIstance().getEBikes();
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
