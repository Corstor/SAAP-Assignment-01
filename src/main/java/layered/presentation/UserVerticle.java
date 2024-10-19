package layered.presentation;

import java.io.IOException;
import java.util.LinkedList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import io.vertx.core.http.HttpMethod;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.RoutingContext;
import io.vertx.ext.web.handler.StaticHandler;
import layered.business.Ride;
import layered.business.RideImpl;
import layered.business.ebike.EBikeFactory;
import layered.business.user.UserFactory;

public class UserVerticle extends MyVerticle {
    private UserFactory userFactory;
    private EBikeFactory bikeFactory;
    private final List<Ride> rides = new LinkedList<>();

    public UserVerticle(final int port) {
        super(port, "User", "user");
        logger = Logger.getLogger("User Verticle");
        try {
            this.userFactory = UserFactory.getInstance();
            this.bikeFactory = EBikeFactory.getInstance();
        } catch (IOException e) {
            logger.log(Level.WARNING, e.getMessage());
        }
    }

    @Override
    protected void additionalSetups() {
        router.route(HttpMethod.POST, "/api/ride/start").handler(this::startRide);
        router.route(HttpMethod.POST, "/api/ride/end").handler(this::endRide);

        router.route().handler(StaticHandler.create("user").setCachingEnabled(false));
    }

    /**
     * Create a user with an id.
     * 
     * @param request json request with the id of the user to create.
     * @throws IOException 
     */
    @Override
    protected void create(JsonObject request) throws IOException {
        this.userFactory.createUser(request.getString("id"), 100);
    }

    @Override
    protected void load(String id, JsonObject reply) throws IOException {
        var user = this.userFactory.getUserWithId(id);
        reply.put("id", id);
        reply.put("credit", user.getCredit());
    }

    private void startRide(RoutingContext context) {
        logger.log(Level.INFO, "Request to start a ride");
        JsonObject request = context.body().asJsonObject();
        JsonObject reply = new JsonObject();

        try {
            Ride ride = new RideImpl(request.getString("id"), request.getString("userId"), request.getString("bikeId"));
            rides.add(ride);
            ride.start();
            reply.put("result", "Ok");
        } catch (Exception e) {
            logger.log(Level.WARNING, e.getMessage());
            reply.put("result", "Error: " + e.getMessage());
        }
        sendReply(context, reply);
    }

    private void endRide(RoutingContext context) {
        logger.log(Level.INFO, "Request to end a ride");
        JsonObject request = context.body().asJsonObject();
        JsonObject reply = new JsonObject();

        try {
            Ride ride = rides.stream().filter(e -> e.getId().equals(request.getString("id"))).findFirst().get();
            ride.end();
            reply.put("id", ride.getId());
            reply.put("userId", ride.getUserId());
            reply.put("bikeId", ride.getBikeId());
            reply.put("startingDate", ride.getStartedDate().toString());
            reply.put("endDate", ride.getEndDate().toString());
        } catch (Exception e) {
            logger.log(Level.WARNING, e.getMessage());
            reply.put("result", "Error: " + e.getMessage());
        }
        sendReply(context, reply);
    }

    @Override
    protected void loadAllBikes(JsonObject reply) throws IOException {
        reply.put("bikes", this.bikeFactory.getEBikes());
    }
}
