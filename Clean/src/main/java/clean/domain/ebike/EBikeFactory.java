package clean.domain.ebike;

import java.io.IOException;
import java.util.List;

import clean.domain.P2d;
import clean.domain.Repository;

public class EBikeFactory {
    private final Repository<EBikeSnapshot> bikeRepository;
    private final List<EBike> bikes;

    public EBikeFactory(Repository<EBikeSnapshot> bikeRepository) throws IOException {
        this.bikeRepository = bikeRepository;
        this.bikes = this.bikeRepository.loadAllValues().entrySet().stream().map(e -> {
            EBike bike = new EBikeImpl(e.getKey(), e.getValue());
            return bike;
        }).toList();
    }

    public EBike createEBike(String id) throws IOException {
        checkBikeExistance(id);

        var bike = new EBikeImpl(id);
        this.bikeRepository.saveValue(id, bike.getEBikeSnapshot());
        this.bikes.add(bike);

        return bike;
    }

    public EBike createEBike(String id, double x, double y) throws IOException {
        checkBikeExistance(id);

        var bike = new EBikeImpl(id);
        bike.updateLocation(new P2d(x, y));

        this.bikeRepository.saveValue(id, bike.getEBikeSnapshot());
        this.bikes.add(bike);

        return bike;
    }

    private void checkBikeExistance(String id) throws IOException {
        if (this.bikes.stream().filter(e -> e.getId().equals(id)).findFirst().isPresent()) {
            throw new EBikeAlreadyCreatedException(id);
        }
    }

    public EBike getEBikeWithId(String id) throws IOException {
        var bike = this.bikes.stream().filter(e -> e.getId().equals(id)).findFirst();

        if (bike.isEmpty()) {
            throw new EBikeDoesNotExists(id);
        }

        return bike.get();
    }

    public List<EBike> getEBikes() throws IOException {
        return List.copyOf(this.bikes);
    }
}
