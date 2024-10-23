package clean.infrastructure;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

import clean.domain.Listener;
import clean.domain.P2d;
import clean.domain.Snapshot;
import clean.domain.V2d;
import clean.domain.ebike.EBikeFactory;
import clean.domain.ebike.EBikeSnapshot;
import clean.domain.user.UserSnapshot;
import io.vertx.core.AbstractVerticle;
import io.vertx.core.Vertx;
import io.vertx.core.eventbus.EventBus;
import io.vertx.core.http.HttpMethod;
import io.vertx.core.http.HttpServer;
import io.vertx.core.http.HttpServerResponse;
import io.vertx.core.http.ServerWebSocket;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.RoutingContext;
import io.vertx.ext.web.handler.BodyHandler;

public abstract class MyVerticle extends AbstractVerticle implements Listener<Snapshot> {
    private static final String CHANNEL = "User-EBike-channel";
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
        setupWebSocket();

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

    private void setupWebSocket() {
        server.webSocketHandler(this::handleEventSubscription);
    }

    private void handleEventSubscription(ServerWebSocket webSocket) {
        if (webSocket.path().equals("/api/events")) {
            webSocket.accept();
            logger.log(Level.INFO, "New Event update subscription accepted");

            EventBus eb = vertx.eventBus();
            eb.consumer(CHANNEL, msg -> {
                JsonObject ev = (JsonObject) msg.body();
                logger.log(Level.INFO, ev.encodePrettily());
                webSocket.writeTextMessage(ev.encodePrettily());
            });
        } else {
            logger.log(Level.WARNING, "Event update subscription refused");
            webSocket.reject();
        }
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
        reply.put("bikes", this.bikeFactory.getEBikes().stream().map(e -> {
            e.addEBikeListener(this);
            return e.getEBikeSnapshot();
        }).toList());
    };

    protected abstract void load(String id, JsonObject reply) throws IOException;

    protected void sendReply(RoutingContext request, JsonObject reply) {
        HttpServerResponse response = request.response();
        response.putHeader("content-type", "application/json");
        response.end(reply.toString());
    }

    @Override
    public void eventOccured(Snapshot value) {
        logger.log(Level.INFO, "New event");
        EventBus eb = vertx.eventBus();
        JsonObject obj = new JsonObject();
        putContent(obj, value);
        eb.publish(CHANNEL, obj);
    }

    private void putContent(JsonObject obj, Snapshot value) {
        obj.put("id", value.id());
        if (value instanceof EBikeSnapshot bike) {
            obj.put("event", "bike-update");
            obj.put("state", bike.state().name());
            obj.put("batteryLevel", bike.batteryLevel());
            obj.put("speed", bike.speed());

            // Convert V2d (direction and location) into JsonObject
            convertV2d(obj, bike.direction());
            convertP2d(obj, bike.location());
        } else if (value instanceof UserSnapshot user) {
            obj.put("event", "user-update");
            obj.put("credit", user.credit());
        }
    }

    private void convertP2d(JsonObject obj, P2d p2d) {
        obj.put("location-x", p2d.getX());
        obj.put("location-y", p2d.getY());
    }

    private void convertV2d(JsonObject obj, V2d v2d) {
        obj.put("direction-x", v2d.getX());
        obj.put("direction-y", v2d.getY());
    }
}
