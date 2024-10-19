package layered.presentation;

import java.io.IOException;
import java.util.logging.Logger;
import java.util.logging.Level;

import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.handler.StaticHandler;
import layered.business.ebike.EBikeFactory;

public class AdminVerticle extends MyVerticle {
    private EBikeFactory bikeFactory;

    public AdminVerticle(final int port) {
        super(port, "Admin", "EBike");
        logger = Logger.getLogger("Adming Verticle");
        try {
            this.bikeFactory = EBikeFactory.getInstance();
        } catch (IOException e) {
            logger.log(Level.WARNING, e.getMessage());
        }
    }

    /**
     * Create an electric bike with an id, a x coord and a y coord.
     * 
     * @param request json request with the id, x and y of the electric bike to create.
     * @throws IOException 
     * @throws NumberFormatException 
     */
    @Override
    protected void create(JsonObject request) throws NumberFormatException, IOException {
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
