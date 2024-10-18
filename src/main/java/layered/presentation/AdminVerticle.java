package layered.presentation;

import java.util.logging.Logger;

import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.handler.StaticHandler;
import layered.business.ebike.EBikeFactory;

public class AdminVerticle extends MyVerticle {
    private final EBikeFactory bikeFactory;

    public AdminVerticle(final int port) {
        super(port, "Admin", "EBike");
        logger = Logger.getLogger("Adming Verticle");
        this.bikeFactory = EBikeFactory.getInstance();
    }

    /**
     * Create an electric bike with an id, a x coord and a y coord.
     * 
     * @param request json request with the id, x and y of the electric bike to create.
     */
    @Override
    protected void create(JsonObject request) {
        this.bikeFactory.createEBike(
            request.getString("id"),
            Double.parseDouble(request.getString("x")),
            Double.parseDouble(request.getString("y"))
        );
    }

    @Override
    protected void additionalSetups() {
        router.route().handler(StaticHandler.create("admin").setCachingEnabled(false));
    }

    @Override
    protected void load(String id, JsonObject reply) {
        // TODO Auto-generated method stub
    }
}
