package layered.persistence.ebike;

import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

public class EBikes {
    private static final List<EBike> bikes = new LinkedList<>();

    public static void createEBike(String id) {
        if (getEBikeWithIdOptional(id).isEmpty()) {
            bikes.add(new EBikeImpl(id));
        } else {
            throw new EBikeAlreadyCreatedException(id);
        }
    }

    public static Optional<EBike> getEBikeWithIdOptional(String id) {
        return bikes.stream().filter(e -> e.getId().equals(id)).findFirst();
    }

    public static EBike getEBikeWithId(String id) {
        Optional<EBike> optionalUser = getEBikeWithIdOptional(id);
        if (optionalUser.isEmpty()) {
            throw new EBikeDoesNotExists(id);
        }
        return optionalUser.get();
    }
}
