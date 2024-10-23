package clean.domain.ebike;

import java.io.IOException;
import java.util.List;
import java.util.LinkedList;
import java.util.Optional;
import java.util.function.Function;

import clean.domain.P2d;
import clean.domain.Repository;

public class EBikeFactory {
    private static EBikeFactory istance;
    private Repository<EBikeSnapshot> bikeRepository;
    private final List<EBike> bikes;

    private EBikeFactory() {
        this.bikes = new LinkedList<>();
    }

    public static EBikeFactory getIstance() {
        if (istance == null) {
            istance = new EBikeFactory();
        }
        return istance;
    }

    public void setRepository(Repository<EBikeSnapshot> repo) throws IOException {
        this.bikeRepository = repo;
        this.bikes.forEach(bike -> {
            try {
                bikeRepository.saveValue(bike.getEBikeSnapshot());
            } catch (IOException e1) {
                e1.printStackTrace();
            }
        });
        this.bikes.clear();
        this.bikes.addAll(this.bikeRepository.loadAllValues().stream()
                .map(snapshot -> {
                    EBike ebike = new EBikeImpl(snapshot);
                    return ebike;
                }).toList());

    }

    public EBike createEBike(String id) throws IOException {
        checkEBikeExistance(id, (ebike) -> ebike.isPresent(), new EBikeAlreadyCreatedException(id));

        var bike = new EBikeImpl(id);
        saveBike(bike);

        return bike;
    }

    public EBike createEBike(String id, double x, double y) throws IOException {
        checkEBikeExistance(id, (ebike) -> ebike.isPresent(), new EBikeAlreadyCreatedException(id));

        var bike = new EBikeImpl(id);
        bike.updateLocation(new P2d(x, y));

        saveBike(bike);

        return bike;
    }

    private void saveBike(EBike bike) throws IOException {
        this.bikeRepository.saveValue(bike.getEBikeSnapshot());
        this.bikes.add(bike);
        bike.addEBikeListener(this.bikeRepository);
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
