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
        checkBikeExistance(id);

        this.bikesStore.saveValue(id, new EBikeImpl(id));
    }

    public void createEBike(String id, double x, double y) throws IOException {
        checkBikeExistance(id);

        var bike = new EBikeImpl(id);
        bike.updateLocation(new P2d(x, y));
        this.bikesStore.saveValue(id, bike);
    }
    
    private void checkBikeExistance(String id) throws IOException {
        if (this.bikesStore.getValueFromIdOptional(id).isEmpty()) {
            throw new EBikeAlreadyCreatedException(id);
        }
    }

    public EBike getEBikeWithId(String id) throws IOException {
        Optional<EBike> bike = this.bikesStore.getValueFromIdOptional(id);
        if (bike.isEmpty()) {
            throw new EBikeDoesNotExists(id);
        }
        return bike.get();
    }
}
