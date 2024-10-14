package layered.business.ebike;

import layered.persistence.P2d;
import layered.persistence.ebike.EBikeFactory;

public class EBikeCreationImpl implements EBikeCreation {
    private final EBikeFactory factory = EBikeFactory.getInstance();

    @Override
    public void createEbike(final String id, final double x, final double y) {
        factory.createEBike(id);
        factory.getEBikeWithId(id).updateLocation(new P2d(x, y));
    }
}
