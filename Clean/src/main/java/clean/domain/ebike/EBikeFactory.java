package clean.domain.ebike;

import java.io.IOException;
import java.util.List;
import java.util.Optional;
import java.util.function.Function;

import clean.domain.P2d;
import clean.domain.Repository;

public class EBikeFactory {
    private final Repository<EBikeSnapshot> bikeRepository;
    private final List<EBike> bikes;

    public EBikeFactory(Repository<EBikeSnapshot> bikeRepository) throws IOException {
        this.bikeRepository = bikeRepository;
        this.bikes = this.bikeRepository.loadAllValues().stream().map(e -> {
            EBike bike = new EBikeImpl(e);
            return bike;
        }).toList();
    }

    public EBike createEBike(String id) throws IOException {
        checkEBikeExistance(id, (ebike) -> ebike.isPresent(), new EBikeAlreadyCreatedException(id));

        var bike = new EBikeImpl(id);
        this.bikeRepository.saveValue(bike.getEBikeSnapshot());
        this.bikes.add(bike);

        return bike;
    }

    public EBike createEBike(String id, double x, double y) throws IOException {
        checkEBikeExistance(id, (ebike) -> ebike.isPresent(), new EBikeAlreadyCreatedException(id));

        var bike = new EBikeImpl(id);
        bike.updateLocation(new P2d(x, y));

        this.bikeRepository.saveValue(bike.getEBikeSnapshot());
        this.bikes.add(bike);

        return bike;
    }

    private Optional<EBike> checkEBikeExistance(String id, Function<Optional<EBike>, Boolean> consumer,
            IllegalArgumentException exception) throws IOException {
        var ebike = this.bikes.stream().filter(e -> e.getEBikeSnapshot().id().equals(id)).findFirst();
        if (consumer.apply(ebike)) {
            throw exception;
        }
        return ebike;
    }

    public EBike getEBikeWithId(String id) throws IOException {
        return checkEBikeExistance(id, (ebike) -> ebike.isEmpty(), new EBikeDoesNotExists(id)).get();
    }

    public void deleteEBike(String id) throws IOException {
        var ebike = checkEBikeExistance(id, (b) -> b.isEmpty(), new EBikeDoesNotExists(id)).get();
        this.bikeRepository.deleteValue(ebike.getEBikeSnapshot());
        this.bikes.remove(ebike);
    }

    public List<EBike> getEBikes() throws IOException {
        return List.copyOf(this.bikes);
    }
}
