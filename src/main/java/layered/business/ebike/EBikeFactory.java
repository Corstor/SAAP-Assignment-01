package layered.business.ebike;

import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

import layered.business.P2d;

public class EBikeFactory {
    //TODO Change bikes into using persistence layer
    private final List<EBike> bikes;
    private static EBikeFactory instance;

    private EBikeFactory() {
        this.bikes = new LinkedList<>();
    }

    public static EBikeFactory getInstance() {
        if (instance == null) {
            instance = new EBikeFactory();
        }
        return instance;
    }

    public void createEBike(String id) {
        if (getEBikeWithIdOptional(id).isEmpty()) {
            bikes.add(new EBikeImpl(id));
        } else {
            throw new EBikeAlreadyCreatedException(id);
        }
    }

    public void createEBike(String id, double x, double y) {
        if (getEBikeWithIdOptional(id).isEmpty()) {
            var bike = new EBikeImpl(id);
            bikes.add(bike);

            bike.updateLocation(new P2d(x, y));
        } else {
            throw new EBikeAlreadyCreatedException(id);
        }
    }

    public Optional<EBike> getEBikeWithIdOptional(String id) {
        return bikes.stream().filter(e -> e.getId().equals(id)).findFirst();
    }

    public EBike getEBikeWithId(String id) {
        Optional<EBike> optionalUser = getEBikeWithIdOptional(id);
        if (optionalUser.isEmpty()) {
            throw new EBikeDoesNotExists(id);
        }
        return optionalUser.get();
    }
}
