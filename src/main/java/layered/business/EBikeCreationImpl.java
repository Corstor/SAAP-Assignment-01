package layered.business;

import layered.persistence.P2d;
import layered.persistence.ebike.EBikes;

public class EBikeCreationImpl implements EBikeCreation {
    @Override
    public void createEbike(String id, P2d position) {
        EBikes.createEBike(id);
        EBikes.getEBikeWithIdOptional(id).get().updateLocation(position);
    }
}
