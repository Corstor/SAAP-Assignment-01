package layered.persistence.ebike;

import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

public class EBikeFactory {
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
