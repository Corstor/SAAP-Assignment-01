package clean.domain.ebike;

public interface EBikeListener {
    void bikeChanged(String bikeId, EBikeSnapshot bike);
}
