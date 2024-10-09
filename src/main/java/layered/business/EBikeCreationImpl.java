package layered.business;

import layered.persistence.P2d;
import layered.persistence.ebike.EBikes;

public class EBikeCreationImpl implements EBikeCreation {
    @Override
    public void createEbike(final String id, final double x, final double y) {
        EBikes.createEBike(id);
        EBikes.getEBikeWithId(id).updateLocation(new P2d(x, y));
    }
}
