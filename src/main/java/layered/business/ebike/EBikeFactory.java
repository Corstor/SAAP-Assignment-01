package layered.business.ebike;

import java.io.IOException;
import java.util.Optional;

import layered.business.P2d;
import layered.persistence.Store;
import layered.persistence.StoreImpl;

public class EBikeFactory {
    private final Store<EBike> bikesStore;
    private static EBikeFactory instance;

    private EBikeFactory() throws IOException {
        this.bikesStore = new StoreImpl<>("EBikes", EBike.class);
    }

    public static EBikeFactory getInstance() throws IOException {
        if (instance == null) {
            instance = new EBikeFactory();
        }
        return instance;
    }

    public void createEBike(String id) throws IOException {
        if (getBikeFromStore(id).isEmpty()) {
            this.bikesStore.saveValue("ebike:" + id, new EBikeImpl(id));
        } else {
            throw new EBikeAlreadyCreatedException(id);
        }
    }

    public void createEBike(String id, double x, double y) throws IOException {
        if (getBikeFromStore(id).isEmpty()) {
            var bike = new EBikeImpl(id);
            bike.updateLocation(new P2d(x, y));

            this.bikesStore.saveValue("ebike:" + id, bike);
        } else {
            throw new EBikeAlreadyCreatedException(id);
        }
    }

    public EBike getEBikeWithId(String id) throws IOException {
        Optional<EBike> bike = getBikeFromStore(id);
        if (bike.isEmpty()) {
            throw new EBikeDoesNotExists(id);
        }
        return bike.get();
    }

    private Optional<EBike> getBikeFromStore(String id) throws IOException {
        return this.bikesStore.getValueFromIdOptional("bike:" + id);
    }
}
