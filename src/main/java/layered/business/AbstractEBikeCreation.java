package layered.business;

import java.util.LinkedList;
import java.util.List;

import layered.persistence.EBike;
import layered.persistence.EBikeImpl;
import layered.persistence.P2d;

public abstract class AbstractEBikeCreation implements EBikeCreation {
    private final List<EBike> bikes = new LinkedList<>();

    @Override
    public void createEbike(String id, P2d position) {
        if (bikeDoesNotExists(id)) {
            EBike bike = new EBikeImpl(id);
            bike.updateLocation(position);
            bikes.add(bike);
        } else {
            throw new EBikeAlreadyCreatedException(id);
        }
    }

    private boolean bikeDoesNotExists(String id) {
        return this.bikes.stream().filter(e -> e.getId() == id).toList().isEmpty();
    }
    
}
