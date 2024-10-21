package clean.domain.ebike;

import java.io.IOException;
import java.util.List;

import clean.domain.P2d;
import clean.domain.Repository;

public class EBikeFactory {
    private final Repository<EBikeSnapshot> bikeRepository;

    public EBikeFactory(Repository<EBikeSnapshot> bikeRepository) throws IOException {
        this.bikeRepository = bikeRepository;
    }

    public EBike createEBike(String id) throws IOException {
        checkBikeExistance(id);

        var bike = new EBikeImpl(id);
        this.bikeRepository.saveValue(id, bike.getEBikeSnapshot());

        return bike;
    }

    public EBike createEBike(String id, double x, double y) throws IOException {
        checkBikeExistance(id);

        var bike = new EBikeImpl(id);
        bike.updateLocation(new P2d(x, y));
        this.bikeRepository.saveValue(id, bike.getEBikeSnapshot());

        return bike;
    }
    
    private void checkBikeExistance(String id) throws IOException {
        if (this.bikeRepository.getValueFromIdOptional(id).isPresent()) {
            throw new EBikeAlreadyCreatedException(id);
        }
    }

    public EBikeSnapshot getEBikeWithId(String id) throws IOException {
        var bike = this.bikeRepository.getValueFromIdOptional(id);
        if (bike.isEmpty()) {
            throw new EBikeDoesNotExists(id);
        }
        return bike.get();
    }

    public List<EBikeSnapshot> getEBikeSnapshots() throws IOException {
        return List.copyOf(this.bikeRepository.loadAllValues().values());
    }
}
