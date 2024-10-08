package layered.persistence.ebike;

import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

public class EBikes {
    private static final List<EBike> bikes = new LinkedList<>();
    
    public static void createEBike(String id) {
        if (getEBikeWithId(id).isEmpty()) {
            bikes.add(new EBikeImpl(id));
        } else {
            throw new EBikeAlreadyCreatedException(id);
        }
    }

    public static Optional<EBike> getEBikeWithId(String id) {
        return bikes.stream().filter(e -> e.getId() == id).findFirst();
    }
}
