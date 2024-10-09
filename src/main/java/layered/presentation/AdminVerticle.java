package layered.presentation;

import java.util.logging.Logger;

import io.vertx.core.json.JsonObject;
import layered.business.EBikeCreation;
import layered.business.EBikeCreationImpl;

public class AdminVerticle extends MyVerticle {
    private final EBikeCreation bikeCreator;

    public AdminVerticle(final int port) {
        super(port, "Admin", "EBike");
        logger = Logger.getLogger("Adming Verticle");
        this.bikeCreator = new EBikeCreationImpl();
    }

    /**
     * Create an electric bike with an id, a x coord and a y coord.
     * 
     * @param request json request with the id, x and y of the electric bike to create.
     */
    @Override
    protected void create(JsonObject request) {
        this.bikeCreator.createEbike(
            request.getString("id"),
            request.getDouble("x"),
            request.getDouble("y")
        );
    }

    @Override
    protected void additionalSetups() {}
}
