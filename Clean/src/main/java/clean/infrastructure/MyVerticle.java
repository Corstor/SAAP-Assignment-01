package clean.infrastructure;


import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

import clean.domain.ebike.EBikeFactory;
import io.vertx.core.AbstractVerticle;
import io.vertx.core.Vertx;
import io.vertx.core.http.HttpMethod;
import io.vertx.core.http.HttpServer;
import io.vertx.core.http.HttpServerResponse;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.RoutingContext;
import io.vertx.ext.web.handler.BodyHandler;

public abstract class MyVerticle extends AbstractVerticle {
    private HttpServer server;
    private final String name;
    private final String createdObjectName;
    
    protected final Logger logger;
    protected final int port;
    protected Router router;

    protected EBikeFactory bikeFactory;

    public MyVerticle(int port, String name, String createdObjectName) {
        this.port = port;
        this.name = name;
        this.createdObjectName = createdObjectName;
        this.bikeFactory = EBikeFactory.getIstance();
        logger = Logger.getLogger(name + "verticle logger");
    }

    public void launch() {
        Vertx vertx = Vertx.vertx();
        vertx.deployVerticle(this);
    }

    @Override
    public void start() {
        initialSetup();

        routerSetup();
        additionalSetups();

        endOfSetup();
    }

    private void initialSetup() {
        logger.log(Level.INFO, name + " web server initializing...");
        server = vertx.createHttpServer();
    }

    private void routerSetup() {
        router = Router.router(vertx);
        router.route().handler(BodyHandler.create());
        router.route(HttpMethod.POST, "/api/register").handler(this::post);
        router.route(HttpMethod.GET, "/api").handler(this::get);
        router.route(HttpMethod.GET, "/api/bikes").handler(this::getBikes);
    }

    private void endOfSetup() {
        server
		.requestHandler(router)
		.listen(port);

		logger.log(Level.INFO, name + " web server ready - port: " + port);
    }

    /**
     * Add other routes and their handler to the router if needed
     */
    protected abstract void additionalSetups();

    private void post(RoutingContext context) {
        logger.log(Level.INFO, "Request to create a new " + createdObjectName);
        JsonObject request = context.body().asJsonObject();
        JsonObject reply = new JsonObject();

        try {
            create(request);
            reply.put("result", "Ok");
        } catch (Exception e) {
            logger.log(Level.WARNING, e.getMessage());
            reply.put("result", "Error -> " + e.getMessage());
        }
        sendReply(context, reply);
    }

    /**
     * Create an object starting from the request body.
     * 
     * @param request the request from which the object will be created.
     * @throws IOException 
     */
    protected abstract void create(JsonObject request) throws IOException;

    private void get(RoutingContext context) {
        String id = context.request().getParam("id");
        logger.log(Level.INFO, "Asked to get data about " + id + " " + createdObjectName);
		JsonObject reply = new JsonObject();

        try {
            load(id, reply);
            reply.put("result", "Ok");
        } catch (Exception e) {
            logger.log(Level.WARNING, e.getMessage());
            reply.put("result", "Error -> " + e.getMessage());
        }
		
		sendReply(context, reply);
    }

    private void getBikes(RoutingContext context) {
        logger.log(Level.INFO, "Asked to get data about all the bikes");
		JsonObject reply = new JsonObject();

        try {
            loadAllBikes(reply);
            reply.put("result", "Ok");
        } catch (Exception e) {
            logger.log(Level.WARNING, e.getMessage());
            reply.put("result", "Error -> " + e.getMessage());
        }
		
		sendReply(context, reply);
    }

    private void loadAllBikes(JsonObject reply) throws IOException {
        this.bikeFactory.getEBikes().forEach(bike -> {
            var snapshot = bike.getEBikeSnapshot();
            reply.put("id", snapshot.id());
            reply.put("batteryLevel", snapshot.batteryLevel());
            reply.put("state", snapshot.state());
            reply.put("speed", snapshot.speed());
            reply.put("direction", snapshot.direction());
            reply.put("location", snapshot.location());
        });
    };

    protected abstract void load(String id, JsonObject reply) throws IOException;

    protected void sendReply(RoutingContext request, JsonObject reply) {
		HttpServerResponse response = request.response();
		response.putHeader("content-type", "application/json");
		response.end(reply.toString());
	}
}
